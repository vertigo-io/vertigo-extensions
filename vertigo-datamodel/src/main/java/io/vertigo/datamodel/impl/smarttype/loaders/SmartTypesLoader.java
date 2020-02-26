package io.vertigo.datamodel.impl.smarttype.loaders;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.util.ClassUtil;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.impl.smarttype.dynamic.DynamicDefinition;
import io.vertigo.datamodel.smarttype.AdapterConfig;
import io.vertigo.datamodel.smarttype.ConstraintConfig;
import io.vertigo.datamodel.smarttype.FormatterConfig;
import io.vertigo.datamodel.smarttype.SmartTypeDefinition;
import io.vertigo.datamodel.smarttype.SmartTypeDefinition.Scope;
import io.vertigo.datamodel.smarttype.annotations.Adapter;
import io.vertigo.datamodel.smarttype.annotations.Constraint;
import io.vertigo.datamodel.smarttype.annotations.Constraints;
import io.vertigo.datamodel.smarttype.annotations.Formatter;
import io.vertigo.datamodel.smarttype.annotations.FormatterDefault;
import io.vertigo.datamodel.smarttype.annotations.SmartTypeProperty;
import io.vertigo.datamodel.structure.metamodel.DtProperty;
import io.vertigo.datamodel.structure.metamodel.Properties;
import io.vertigo.datamodel.structure.metamodel.PropertiesBuilder;
import io.vertigo.datamodel.structure.metamodel.Property;
import io.vertigo.datamodel.structure.model.DtObject;

public class SmartTypesLoader implements Loader {

	@Override
	public List<DynamicDefinition> load(final String resourcePath) {
		final Class<? extends Enum> smartTypesDictionnaryClass = (Class<? extends Enum>) ClassUtil.classForName(resourcePath);
		return Stream.of(smartTypesDictionnaryClass.getEnumConstants())
				.map(enumConstant -> {
					try {
						final SmartTypeDefinition smartTypeDefinition = readSmartTypeDefinition(smartTypesDictionnaryClass.getField(enumConstant.name()));
						return new DynamicDefinition(smartTypeDefinition.getName(), Collections.emptyList(), ds -> smartTypeDefinition);
					} catch (NoSuchFieldException | SecurityException e) {
						throw WrappedException.wrap(e);
					}
				}).collect(Collectors.toList());

	}

	private static SmartTypeDefinition readSmartTypeDefinition(final Field field) {
		final String smartTypeName = "STy" + field.getName();
		final Scope scope;
		final Class targetJavaClass = field.getAnnotation(io.vertigo.datamodel.smarttype.annotations.SmartTypeDefinition.class).value();
		final Optional<BasicType> dataTypeOpt = BasicType.of(targetJavaClass);
		final FormatterConfig formatterConfig;
		final List<ConstraintConfig> constraintConfigs;
		final List<AdapterConfig> adapterConfigs = new ArrayList<>();
		final PropertiesBuilder propertiesBuilder = Properties.builder();

		// DataType and Mapper
		if (dataTypeOpt.isPresent()) {
			//we are a primitive
			scope = Scope.PRIMITIVE;
		} else {
			//we are not primitive, we need a mapper
			final Adapter[] adapters = field.getAnnotationsByType(Adapter.class);
			Assertion.checkState(adapters.length > 0,
					"Your smarttype '{0}' is not a primitive one, you need to specify a mapper to a targeted DataType with the @Adapter annotation", smartTypeName);
			for (final Adapter adapter : adapters) {
				adapterConfigs.add(new AdapterConfig(adapter.type(), adapter.clazz(), adapter.targetBasicType()));
			}
			scope = DtObject.class.isAssignableFrom(targetJavaClass) ? Scope.DATA_OBJECT : Scope.VALUE_OBJECT;
		}

		// Formatter
		if (field.isAnnotationPresent(Formatter.class)) {
			final Formatter formatter = field.getAnnotation(Formatter.class);
			formatterConfig = new FormatterConfig(formatter.clazz(), formatter.arg());
		} else if (field.isAnnotationPresent(FormatterDefault.class)) {
			formatterConfig = new FormatterConfig(io.vertigo.datamodel.impl.smarttype.formatter.FormatterDefault.class, null);
		} else {
			formatterConfig = null;
		}
		// Constraints
		if (field.isAnnotationPresent(Constraint.class) || field.isAnnotationPresent(Constraints.class)) {
			final Constraint[] constraints = field.getAnnotationsByType(Constraint.class);
			constraintConfigs = Arrays.stream(constraints)
					.map(contraint -> new ConstraintConfig(contraint.clazz(), contraint.arg(), contraint.msg()))
					.collect(Collectors.toList());

			constraintConfigs.stream()
					.forEach(constraintConfig -> {
						final Optional<String> msgOpt = StringUtil.isEmpty(constraintConfig.getMsg()) ? Optional.empty() : Optional.of(constraintConfig.getMsg());
						final Constructor<? extends io.vertigo.datamodel.structure.metamodel.Constraint> constructor = ClassUtil.findConstructor(constraintConfig.getConstraintClass(), new Class[] { String.class, Optional.class });
						final io.vertigo.datamodel.structure.metamodel.Constraint newConstraint = ClassUtil.newInstance(constructor, new Object[] { constraintConfig.getArg(), msgOpt });
						propertiesBuilder.addValue(newConstraint.getProperty(), newConstraint.getPropertyValue());
					});

		} else {
			constraintConfigs = Collections.emptyList();
		}

		for (final SmartTypeProperty smartTypeProperty : field.getAnnotationsByType(SmartTypeProperty.class)) {
			propertiesBuilder.addValue((Property<String>) DtProperty.valueOf(StringUtil.camelToConstCase(smartTypeProperty.property())), smartTypeProperty.value());
			//TODO
		}

		return new SmartTypeDefinition(
				smartTypeName,
				scope,
				targetJavaClass.getName(),
				adapterConfigs,
				formatterConfig,
				constraintConfigs,
				propertiesBuilder.build());

	}

	@Override
	public String getType() {
		return "smarttypes";
	}

}