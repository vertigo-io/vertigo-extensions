/*
 * Copyright 2017, Danny Rottstegge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertigo.ui.impl.thymeleaf.components;

import static java.util.Collections.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.thymeleaf.context.IEngineContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeNames;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IElementTag;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.Assignation;
import org.thymeleaf.standard.expression.AssignationSequence;
import org.thymeleaf.standard.expression.AssignationUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.VariableExpression;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.StringUtils;

public class ThymeleafComponentNamedElementProcessor extends AbstractElementModelProcessor {

	private static final String FRAGMENT_ATTRIBUTE = "fragment";
	private final String REPLACE_CONTENT_TAG;

	private static final int PRECEDENCE = 350;

	private final Set<String> excludeAttributes = singleton("params");
	private final String componentName;
	private final Optional<VariableExpression> selectionExpression;
	private final String frag;

	/**
	 * Constructor
	 *
	 * @param dialectPrefix Dialect prefix (tc)
	 * @param tagName Tag name to search for (e.g. panel)
	 * @param componentName Fragment to search for
	 */
	public ThymeleafComponentNamedElementProcessor(final String dialectPrefix, final ThymeleafComponent thymeleafComponent) {
		super(TemplateMode.HTML, dialectPrefix, thymeleafComponent.getName(), true, null, false, PRECEDENCE);
		REPLACE_CONTENT_TAG = dialectPrefix + ":content";
		componentName = thymeleafComponent.getFragmentTemplate();
		selectionExpression = thymeleafComponent.getSelectionExpression();
		frag = thymeleafComponent.getFrag();
	}

	@Override
	protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
		final IProcessableElementTag tag = processElementTag(context, model);
		final Map<String, String> attributes = processAttribute(tag);

		final String param = attributes.get("params");

		final IModel componentModel = model.cloneModel();
		componentModel.remove(0);

		if (componentModel.size() > 1) {
			componentModel.remove(componentModel.size() - 1);
		}

		if (!selectionExpression.isPresent() //no selector
				|| (boolean) selectionExpression.get().execute(context)) { //or selector valid
			final String fragmentToUse = "~{" + componentName + " :: " + frag + "}";

			final IModel fragmentModel = FragmentHelper.getFragmentModel(context,
					fragmentToUse + (param == null ? "" : "(" + param + ")"),
					structureHandler, StandardDialect.PREFIX, FRAGMENT_ATTRIBUTE);
			model.reset();

			final IModel replacedFragmentModel = replaceAllAttributeValues(attributes, context, fragmentModel);
			model.addModel(mergeModels(replacedFragmentModel, componentModel, REPLACE_CONTENT_TAG));

			processVariables(attributes, context, structureHandler, excludeAttributes);
		} // else nothing

	}

	private static IProcessableElementTag processElementTag(final ITemplateContext context, final IModel model) {
		final ITemplateEvent firstEvent = model.get(0);
		for (final IProcessableElementTag tag : context.getElementStack()) {
			if (locationMatches(firstEvent, tag)) {
				return tag;
			}
		}
		return null;
	}

	private static boolean locationMatches(final ITemplateEvent a, final ITemplateEvent b) {
		return Objects.equals(a.getTemplateName(), b.getTemplateName())
				&& Objects.equals(a.getLine(), b.getLine())
				&& Objects.equals(a.getCol(), b.getCol());
	}

	private void processVariables(final Map<String, String> attributes,
			final ITemplateContext context,
			final IElementModelStructureHandler structureHandler,
			final Set<String> excludeAttr) {
		for (final Map.Entry<String, String> entry : attributes.entrySet()) {
			if (excludeAttr.contains(entry.getKey()) || isDynamicAttribute(entry.getKey(), getDialectPrefix())) {
				continue;
			}
			String attributeValue = entry.getValue();
			if (attributeValue == null) {
				attributeValue = "${true}";
			}
			processWith(context, entry.getKey() + "=" + attributeValue, structureHandler);
		}
	}

	private static Map<String, String> processAttribute(final IProcessableElementTag tag) {
		final Map<String, String> attributes = new HashMap<>();
		if (tag != null) {
			for (final IAttribute attribute : tag.getAllAttributes()) {
				final String completeName = attribute.getAttributeCompleteName();
				if (!isDynamicAttribute(completeName, StandardDialect.PREFIX)) {
					attributes.put(completeName, attribute.getValue());
				}
			}
		}

		return attributes;
	}

	private static boolean isDynamicAttribute(final String attribute, final String prefix) {
		return attribute.startsWith(prefix + ":") || attribute.startsWith("data-" + prefix + "-");
	}

	private static IModel mergeModels(final IModel base, final IModel insert, final String replaceTag) {
		IModel mergedModel = insertModel(base, insert, replaceTag);
		mergedModel = removeTag(mergedModel, replaceTag);
		mergedModel = removeTag(mergedModel, replaceTag);
		return mergedModel;
	}

	private static IModel insertModel(final IModel base, final IModel insert, final String replaceTag) {
		final IModel clonedModel = base.cloneModel();
		final int index = findTagIndex(base, replaceTag, IElementTag.class);
		if (index > -1) {
			clonedModel.insertModel(index, insert);
		}
		return clonedModel;
	}

	private static IModel removeTag(final IModel model, final String tag) {
		final IModel clonedModel = model.cloneModel();
		final int index = findTagIndex(model, tag, IElementTag.class);
		if (index > -1) {
			clonedModel.remove(index);
		}
		return clonedModel;
	}

	private static int findTagIndex(final IModel model, final String search, final Class<?> clazz) {
		final int size = model.size();
		ITemplateEvent event = null;
		for (int i = 0; i < size; i++) {
			event = model.get(i);
			if ((clazz == null || clazz.isInstance(event))
					&& event.toString().contains(search)) {
				return i;
			}
		}
		return -1;
	}

	private IModel replaceAllAttributeValues(final Map<String, String> attributes, final ITemplateContext context, final IModel model) {
		final Map<String, String> replaceAttributes = findAllAttributesStartsWith(attributes, super.getDialectPrefix(), "repl-", true);

		if (replaceAttributes.isEmpty()) {
			return model;
		}
		final IModel clonedModel = model.cloneModel();
		final int size = model.size();
		for (int i = 0; i < size; i++) {
			final ITemplateEvent replacedEvent = replaceAttributeValue(context,
					clonedModel.get(i), replaceAttributes);
			if (replacedEvent != null) {
				clonedModel.replace(i, replacedEvent);
			}
		}
		return clonedModel;
	}

	private static ITemplateEvent replaceAttributeValue(final ITemplateContext context, final ITemplateEvent model, final Map<String, String> replaceValueMap) {
		IProcessableElementTag firstEvent = null;
		if (!replaceValueMap.isEmpty() && model instanceof IProcessableElementTag) {
			final IModelFactory modelFactory = context.getModelFactory();

			firstEvent = (IProcessableElementTag) model;
			for (final Map.Entry<String, String> entry : firstEvent.getAttributeMap().entrySet()) {
				final String oldAttrValue = entry.getValue();
				final String replacePart = getReplaceAttributePart(oldAttrValue);
				if (replacePart != null && replaceValueMap.containsKey(replacePart)) {
					final String newStringValue = oldAttrValue.replace("?[" + replacePart + "]", replaceValueMap.get(replacePart));
					firstEvent = modelFactory.replaceAttribute(firstEvent,
							AttributeNames.forTextName(entry.getKey()),
							entry.getKey(), newStringValue);
				}
			}
		}
		return firstEvent;
	}

	private static Map<String, String> findAllAttributesStartsWith(final Map<String, String> attributes, final String prefix, final String attributeName, final boolean removeStart) {
		final Map<String, String> matchingAttributes = new HashMap<>();
		for (final Map.Entry<String, String> entry : attributes.entrySet()) {
			String key = entry.getKey();
			final String value = entry.getValue();
			if (key.startsWith(prefix + ":" + attributeName)
					|| key.startsWith("data-" + prefix + "-" + attributeName)) {
				if (removeStart) {
					key = key.replaceAll("^" + prefix + ":" + attributeName,
							"");
					key = key.replaceAll(
							"^data-" + prefix + "-" + attributeName, "");
				}
				matchingAttributes.put(key, value);
			}
		}
		return matchingAttributes;
	}

	private static String getReplaceAttributePart(final String attributeValue) {
		final Pattern pattern = Pattern.compile(".*\\?\\[([\\w|\\d|.|\\-|_]*)\\].*");
		final Matcher matcher = pattern.matcher(attributeValue);
		while (matcher.find()) {
			if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	private static void processWith(final ITemplateContext context, final String attributeValue, final IElementModelStructureHandler structureHandler) {

		final AssignationSequence assignations = AssignationUtils.parseAssignationSequence(context, attributeValue, false /* no parameters without value */);
		if (assignations == null) {
			throw new TemplateProcessingException("Could not parse value as attribute assignations: \"" + attributeValue + "\"");
		}

		// Normally we would just allow the structure handler to be in charge of declaring the local variables
		// by using structureHandler.setLocalVariable(...) but in this case we want each variable defined at an
		// expression to be available for the next expressions, and that forces us to cast our ITemplateContext into
		// a more specific interface --which shouldn't be used directly except in this specific, special case-- and
		// put the local variables directly into it.
		IEngineContext engineContext = null;
		if (context instanceof IEngineContext) {
			// NOTE this interface is internal and should not be used in users' code
			engineContext = (IEngineContext) context;
		}

		final List<Assignation> assignationValues = assignations.getAssignations();
		final int assignationValuesLen = assignationValues.size();

		for (int i = 0; i < assignationValuesLen; i++) {

			final Assignation assignation = assignationValues.get(i);

			final IStandardExpression leftExpr = assignation.getLeft();
			final Object leftValue = leftExpr.execute(context);

			final IStandardExpression rightExpr = assignation.getRight();
			final Object rightValue = rightExpr.execute(context);

			final String newVariableName = leftValue == null ? null : leftValue.toString();
			if (StringUtils.isEmptyOrWhitespace(newVariableName)) {
				throw new TemplateProcessingException("Variable name expression evaluated as null or empty: \"" + leftExpr + "\"");
			}

			if (engineContext != null) {
				// The advantage of this vs. using the structure handler is that we will be able to
				// use this newly created value in other expressions in the same 'th:with'
				engineContext.setVariable(newVariableName, rightValue);
			} else {
				// The problem is, these won't be available until we execute the next processor
				structureHandler.setLocalVariable(newVariableName, rightValue);
			}
		}
	}
}
