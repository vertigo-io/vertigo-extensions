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
package io.vertigo.datastore.entitystore.sql;

import io.vertigo.commons.CommonsFeatures;
import io.vertigo.commons.plugins.cache.memory.MemoryCachePlugin;
import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.core.node.config.ModuleConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.param.Param;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.database.DatabaseFeatures;
import io.vertigo.database.plugins.sql.connection.c3p0.C3p0ConnectionProviderPlugin;
import io.vertigo.datamodel.DataModelFeatures;
import io.vertigo.datamodel.smarttype.ModelDefinitionProvider;
import io.vertigo.datastore.DataStoreFeatures;
import io.vertigo.datastore.entitystore.StoreCacheDefinitionProvider;
import io.vertigo.datastore.entitystore.data.DtDefinitions;
import io.vertigo.datastore.entitystore.data.TestSmartTypes;
import io.vertigo.datastore.plugins.entitystore.sql.SqlEntityStorePlugin;

/**
 * NodeConfig builder for SqlStore tests. (Params for db specificities)
 * @author mlaroche
 *
 */
public class SqlDataStoreNodeConfig {

	public static NodeConfig build(final String dataBaseClass, final String jdbcDriver, final String jdbcUrl) {
		return NodeConfig.builder()
				.beginBoot()
				.withLocales("fr_FR")
				.addPlugin(ClassPathResourceResolverPlugin.class)
				.endBoot()
				.addModule(new CommonsFeatures()
						.withScript()
						.withJaninoScript()
						.withCache()
						.addPlugin(MemoryCachePlugin.class)
						.build())
				.addModule(new DatabaseFeatures()
						.withSqlDataBase()
						.addPlugin(C3p0ConnectionProviderPlugin.class,
								Param.of("dataBaseClass", dataBaseClass),
								Param.of("jdbcDriver", jdbcDriver),
								Param.of("jdbcUrl", jdbcUrl))
						.build())
				.addModule(new DataModelFeatures().build())
				.addModule(new DataStoreFeatures()
						.withEntityStore()
						.addPlugin(SqlEntityStorePlugin.class)
						.build())
				.addModule(ModuleConfig.builder("definition")
						.addDefinitionProvider(DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
								.addDefinitionResource("smarttypes", TestSmartTypes.class.getName())
								.addDefinitionResource("dtobjects", DtDefinitions.class.getName())
								.build())
						.addDefinitionProvider(StoreCacheDefinitionProvider.class)
						.build())
				.build();

	}

}
