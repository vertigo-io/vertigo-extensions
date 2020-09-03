/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2020, Vertigo.io, team@vertigo.io
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
package io.vertigo.quarto.publisher.data;

import java.time.LocalDate;

import io.vertigo.basics.constraint.ConstraintStringLength;
import io.vertigo.basics.formatter.FormatterDefault;
import io.vertigo.core.lang.DataStream;
import io.vertigo.datamodel.smarttype.annotations.Constraint;
import io.vertigo.datamodel.smarttype.annotations.Formatter;
import io.vertigo.datamodel.smarttype.annotations.SmartTypeDefinition;
import io.vertigo.datamodel.smarttype.annotations.SmartTypeProperty;

public enum TestPublisherSmartTypes {

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@SmartTypeProperty(property = "indexType", value = "text.fr")
	String,

	@SmartTypeDefinition(Long.class)
	@Formatter(clazz = FormatterDefault.class)
	Long,

	@SmartTypeDefinition(Double.class)
	@Formatter(clazz = FormatterDefault.class)
	Double,

	@SmartTypeDefinition(Integer.class)
	@Formatter(clazz = FormatterDefault.class)
	Integer,

	@SmartTypeDefinition(LocalDate.class)
	@Formatter(clazz = FormatterDefault.class)
	Date,

	@SmartTypeDefinition(Boolean.class)
	@Formatter(clazz = FormatterDefault.class)
	Boolean,

	@SmartTypeDefinition(Long.class)
	@Formatter(clazz = FormatterDefault.class)
	Identifiant,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintStringLength.class, arg = "250", msg = "")
	LibelleLong,

	@SmartTypeDefinition(DataStream.class)
	@Formatter(clazz = FormatterDefault.class)
	Stream;

}
