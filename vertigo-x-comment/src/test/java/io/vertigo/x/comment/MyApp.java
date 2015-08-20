package io.vertigo.x.comment;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.commons.impl.codec.CodecManagerImpl;
import io.vertigo.commons.plugins.resource.java.ClassPathResourceResolverPlugin;
import io.vertigo.core.config.AppConfig;
import io.vertigo.core.config.AppConfigBuilder;
import io.vertigo.core.environment.EnvironmentManager;
import io.vertigo.core.impl.environment.EnvironmentManagerImpl;
import io.vertigo.core.impl.locale.LocaleManagerImpl;
import io.vertigo.core.impl.resource.ResourceManagerImpl;
import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.resource.ResourceManager;
import io.vertigo.dynamo.file.FileManager;
import io.vertigo.dynamo.impl.file.FileManagerImpl;
import io.vertigo.dynamo.plugins.environment.loaders.java.AnnotationLoaderPlugin;
import io.vertigo.dynamo.plugins.environment.registries.domain.DomainDynamicRegistryPlugin;
import io.vertigo.persona.impl.security.VSecurityManagerImpl;
import io.vertigo.persona.security.VSecurityManager;
import io.vertigo.vega.impl.rest.RestManagerImpl;
import io.vertigo.vega.plugins.rest.handler.ExceptionRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.handler.JsonConverterRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.handler.RestfulServiceRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.handler.SecurityRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.handler.SessionInvalidateRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.handler.SessionRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.handler.ValidatorRestHandlerPlugin;
import io.vertigo.vega.plugins.rest.instrospector.annotations.AnnotationsEndPointIntrospectorPlugin;
import io.vertigo.vega.plugins.rest.routesregister.sparkjava.SparkJavaRoutesRegisterPlugin;
import io.vertigo.vega.rest.RestManager;
import io.vertigo.vega.rest.RestfulService;
import io.vertigo.vega.rest.engine.GoogleJsonEngine;
import io.vertigo.vega.rest.engine.JsonEngine;
import io.vertigo.x.account.AccountManager;
import io.vertigo.x.connectors.redis.RedisConnector;
import io.vertigo.x.impl.account.AccountManagerImpl;
import io.vertigo.x.impl.comment.CommentManagerImpl;
import io.vertigo.x.plugins.account.redis.RedisAccountStorePlugin;
import io.vertigo.x.plugins.comment.redis.RedisCommentPlugin;
import io.vertigo.x.webapi.account.AccountWebServices;
import io.vertigo.x.webapi.comment.CommentWebServices;

public final class MyApp {
	public static AppConfig config() {
		// @formatter:off
		return new AppConfigBuilder()
			.beginBootModule()
				.beginComponent(LocaleManager.class, LocaleManagerImpl.class)
					.addParam("locales", "fr")
				.endComponent()
				.beginComponent(ResourceManager.class, ResourceManagerImpl.class)
					.beginPlugin( ClassPathResourceResolverPlugin.class).endPlugin()
				.endComponent()
				.beginComponent(EnvironmentManager.class, EnvironmentManagerImpl.class)
					.beginPlugin(AnnotationLoaderPlugin.class).endPlugin()
					.beginPlugin(DomainDynamicRegistryPlugin.class).endPlugin()
				.endComponent()
			.endModule()
			.beginBoot()
				.silently()
			.endBoot()
			.beginModule("persona")
				.beginComponent(VSecurityManager.class, VSecurityManagerImpl.class)
					.addParam("userSessionClassName", TestUserSession.class.getName())
				.endComponent()
			.endModule()
			.beginModule("dynamo").withNoAPI()
				.beginComponent(CodecManager.class, CodecManagerImpl.class).endComponent()
				.beginComponent(FileManager.class, FileManagerImpl.class).endComponent()
			.endModule()
			.beginModule("connector").withNoAPI()
				.beginComponent(RedisConnector.class, RedisConnector.class)
					.addParam("host", "kasper-redis")
					.addParam("port", "6379")
				.endComponent()
			.endModule()
			.beginModule("account")
				.beginComponent(AccountManager.class, AccountManagerImpl.class)
					.beginPlugin(RedisAccountStorePlugin.class).endPlugin()
				.endComponent()
			.endModule()
			.beginModule("comment")
				.beginComponent(CommentManager.class, CommentManagerImpl.class)
					.beginPlugin(RedisCommentPlugin.class).endPlugin()
				.endComponent()
			.endModule()
			.beginModule("restServices").withNoAPI().withInheritance(RestfulService.class)
				.beginComponent(AccountWebServices.class).endComponent()
				.beginComponent(CommentWebServices.class).endComponent()
				.beginComponent(TestWebServices.class).endComponent()
			.endModule()
			.beginModule("restCore").withNoAPI().withInheritance(Object.class)
				.beginComponent(JsonEngine.class, GoogleJsonEngine.class).endComponent()
				.beginComponent(RestManager.class, RestManagerImpl.class)
					.beginPlugin(AnnotationsEndPointIntrospectorPlugin.class).endPlugin()
					.beginPlugin(SparkJavaRoutesRegisterPlugin.class).endPlugin()
					//-- Handlers plugins
					.beginPlugin(ExceptionRestHandlerPlugin.class).endPlugin()
					.beginPlugin(SessionInvalidateRestHandlerPlugin.class).endPlugin()
					.beginPlugin(SessionRestHandlerPlugin.class).endPlugin()
					.beginPlugin(SecurityRestHandlerPlugin.class).endPlugin()
					//.beginPlugin(OldJsonConverterRestHandlerPlugin.class).endPlugin()
					.beginPlugin(JsonConverterRestHandlerPlugin.class).endPlugin()
					.beginPlugin(ValidatorRestHandlerPlugin.class).endPlugin()
					.beginPlugin(RestfulServiceRestHandlerPlugin.class).endPlugin()
				.endComponent()
			.endModule()
		.build();
		// @formatter:on
	}
}
