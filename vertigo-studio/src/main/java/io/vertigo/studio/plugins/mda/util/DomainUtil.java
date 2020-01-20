/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, Vertigo.io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.studio.plugins.mda.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.node.Home;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.metamodel.association.AssociationDefinition;
import io.vertigo.dynamo.domain.metamodel.association.AssociationNNDefinition;
import io.vertigo.dynamo.domain.metamodel.association.AssociationSimpleDefinition;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.ngdomain.SmartTypeDefinition;
import io.vertigo.dynamo.task.metamodel.TaskAttribute;

/**
 * Helper.
 *
 * @author emangin
 */
public final class DomainUtil {

	/**
	 * Constructeur privé pour classe utilitaire.
	 */
	private DomainUtil() {
		//RAS
	}

	/**
	 * Construite le type java (sous forme de chaine de caractère) correspondant
	 * à un champ.
	 * @param dtField the field
	 * @return String
	 */
	public static String buildJavaType(final DtField dtField) {
		return buildJavaType(dtField.getDomain(), dtField.getCardinality(), getManyTargetJavaClass(dtField.getDomain()));
	}

	/**
	 * Construite le type java (sous forme de chaine de caractère) correspondant
	 * à un attribut de tache.
	 * @param taskAttribute the attribute
	 * @return String
	 */
	public static String buildJavaType(final TaskAttribute taskAttribute) {
		return buildJavaType(taskAttribute.getDomain(), taskAttribute.getCardinality(), getManyTargetJavaClass(taskAttribute.getDomain()));
	}

	/**
	 * Construite le label du type java (sous forme de chaine de caractère) correspondant
	 * à un DtField.
	 * @param dtField DtField
	 * @return String
	 */
	public static String buildJavaTypeLabel(final DtField dtField) {
		return buildJavaTypeLabel(dtField.getDomain(), dtField.getCardinality(), getManyTargetJavaClass(dtField.getDomain()));
	}

	/**
	 * Construite le label du type java (sous forme de chaine de caractère) correspondant
	 * à un attribut de tache.
	 * @param taskAttribute the attribute
	 * @return String
	 */
	public static String buildJavaTypeLabel(final TaskAttribute taskAttribute) {
		return buildJavaTypeLabel(taskAttribute.getDomain(), taskAttribute.getCardinality(), getManyTargetJavaClass(taskAttribute.getDomain()));
	}

	private static Class getManyTargetJavaClass(final SmartTypeDefinition smartTypeDefinition) {
		switch (smartTypeDefinition.getScope()) {
			case DATA_OBJECT:
				return DtList.class;
			case PRIMITIVE:
			case VALUE_OBJECT:
				return List.class;
			default:
				throw new IllegalStateException();
		}
	}

	private static String buildJavaType(final SmartTypeDefinition smartTypeDefinition, final Cardinality cardinality, final Class manyTargetClass) {
		final String className;
		switch (smartTypeDefinition.getScope()) {
			case PRIMITIVE:
				String javaType = smartTypeDefinition.getJavaClass().getName();

				//On simplifie l'écriture des types primitifs
				//java.lang.String => String
				if (javaType.startsWith("java.lang.")) {
					javaType = javaType.substring("java.lang.".length());
				}
				className = javaType;
				break;
			case DATA_OBJECT:
			case VALUE_OBJECT:
				className = smartTypeDefinition.getJavaClass().getName();
				break;
			default:
				throw new IllegalStateException();
		}
		if (cardinality.hasMany()) {
			return manyTargetClass.getName() + '<' + className + '>';
		}
		return className;
	}

	public static String buildJavaTypeLabel(final SmartTypeDefinition smartTypeDefinition, final Cardinality cardinality, final Class manyTargetClass) {
		final String classLabel = smartTypeDefinition.getJavaClass().getSimpleName();
		if (cardinality.hasMany()) {
			return manyTargetClass.getSimpleName() + " de " + classLabel;
		}
		return classLabel;
	}

	public static Collection<DtDefinition> getDtDefinitions() {
		return sortDefinitionCollection(Home.getApp().getDefinitionSpace().getAll(DtDefinition.class));
	}

	public static Map<String, Collection<DtDefinition>> getDtDefinitionCollectionMap() {
		return getDefinitionCollectionMap(getDtDefinitions());
	}

	public static Collection<AssociationSimpleDefinition> getSimpleAssociations() {
		return sortAssociationsCollection(Home.getApp().getDefinitionSpace().getAll(AssociationSimpleDefinition.class));
	}

	public static Collection<AssociationNNDefinition> getNNAssociations() {
		return sortAssociationsCollection(Home.getApp().getDefinitionSpace().getAll(AssociationNNDefinition.class));
	}

	/**
	 * trie de la collection.
	 * @param definitionCollection collection à trier
	 * @return collection triée
	 */
	public static List<DtDefinition> sortDefinitionCollection(final Collection<DtDefinition> definitionCollection) {
		final List<DtDefinition> list = new ArrayList<>(definitionCollection);
		list.sort(Comparator.comparing(DtDefinition::getName));
		return list;
	}

	/**
	 * @param definitionCollection collection à traiter
	 * @return map ayant le package name en clef
	 */
	private static Map<String, Collection<DtDefinition>> getDefinitionCollectionMap(final Collection<DtDefinition> definitions) {
		final Map<String, Collection<DtDefinition>> map = new LinkedHashMap<>();

		for (final DtDefinition definition : definitions) {
			map.computeIfAbsent(definition.getPackageName(),
					k -> new ArrayList<>())
					.add(definition);
		}
		return map;
	}

	private static <A extends AssociationDefinition> Collection<A> sortAssociationsCollection(final Collection<A> associationCollection) {
		final List<A> list = new ArrayList<>(associationCollection);
		list.sort(Comparator.comparing(AssociationDefinition::getName));
		return list;
	}
}
