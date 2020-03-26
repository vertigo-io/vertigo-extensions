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
package io.vertigo.commons.app;

import io.vertigo.commons.CommonsFeatures;
import io.vertigo.commons.plugins.app.registry.redis.RedisAppNodeRegistryPlugin;
import io.vertigo.connectors.redis.RedisFeatures;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.param.Param;

public class RedisAppNodeRegistryPluginTest extends AbstractAppManagerTest {

	@Override
	protected NodeConfig buildNodeConfig() {

		final String redisHost = "redis-pic.part.klee.lan.net";
		final int redisPort = 6379;
		final int redisDatabase = 11;

		return buildRootNodeConfig()
				.addModule(new RedisFeatures()
						.withJedis(
								Param.of("host", redisHost),
								Param.of("port", Integer.toString(redisPort)),
								Param.of("database", Integer.toString(redisDatabase)))
						.build())
				.addModule(new CommonsFeatures()
						.withNodeRegistryPlugin(RedisAppNodeRegistryPlugin.class)
						.build())
				.build();
	}

}