/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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

package io.vertigo.x.workflow;

import io.vertigo.app.config.AppConfig;
import io.vertigo.app.config.AppConfigBuilder;
import io.vertigo.commons.impl.CommonsFeatures;
import io.vertigo.commons.plugins.script.janino.JaninoExpressionEvaluatorPlugin;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.dynamo.impl.DynamoFeatures;
import io.vertigo.dynamo.plugins.environment.loaders.java.AnnotationLoaderPlugin;
import io.vertigo.dynamo.plugins.environment.registries.domain.DomainDynamicRegistryPlugin;
import io.vertigo.persona.impl.security.PersonaFeatures;
import io.vertigo.x.impl.account.AccountFeatures;
import io.vertigo.x.impl.workflow.WorkflowFeatures;
import io.vertigo.x.plugins.account.memory.MemoryAccountStorePlugin;
import io.vertigo.x.plugins.memory.MemoryRuleConstantsStorePlugin;
import io.vertigo.x.plugins.memory.MemoryRuleStorePlugin;
import io.vertigo.x.plugins.memory.MemoryWorkflowStorePlugin;
import io.vertigo.x.plugins.selector.SimpleRuleSelectorPlugin;
import io.vertigo.x.plugins.validator.SimpleRuleValidatorPlugin;
import io.vertigo.x.workflow.data.MyDummyDtObjectProvider;
import io.vertigo.x.workflow.data.TestUserSession;
import io.vertigo.x.workflow.plugin.MemoryItemStorePlugin;

/**
 * Config for Junit
 * @author xdurand
 *
 */
public class MyAppConfig {

	/**
	 * Configuration de l'application pour Junit
	 * @return AppConfig for Junit
	 */
	public static AppConfig config() {
		// @formatter:off
		final AppConfigBuilder appConfigBuilder =  new AppConfigBuilder()
				.beginBoot()
					.withLocales("fr")
					.addPlugin(ClassPathResourceResolverPlugin.class)
					.addPlugin(AnnotationLoaderPlugin.class)
					.addPlugin(DomainDynamicRegistryPlugin.class)
					.silently()
				.endBoot()
				.addModule(new PersonaFeatures().withUserSession(TestUserSession.class).build())
				.addModule(new CommonsFeatures().build())
				.addModule(new DynamoFeatures().build())
				.addModule(new AccountFeatures()
					.getModuleConfigBuilder()
					.addPlugin(MemoryAccountStorePlugin.class)
					.build());


		appConfigBuilder.addModule( new WorkflowFeatures()
			.getModuleConfigBuilder()
				.addDefinitionProvider(MyDummyDtObjectProvider.class)
				.addPlugin(MemoryWorkflowStorePlugin.class)
				.addPlugin(MemoryItemStorePlugin.class)
				.addPlugin(MemoryRuleStorePlugin.class)
				.addPlugin(MemoryRuleConstantsStorePlugin.class)
				.addPlugin(SimpleRuleSelectorPlugin.class)
				.addPlugin(SimpleRuleValidatorPlugin.class)
				.addPlugin(JaninoExpressionEvaluatorPlugin.class)
		   .build());
		// @formatter:on
		return appConfigBuilder.build();
	}

}
