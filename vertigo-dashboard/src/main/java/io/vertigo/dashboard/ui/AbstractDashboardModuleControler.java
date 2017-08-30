package io.vertigo.dashboard.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.app.App;
import io.vertigo.app.config.ModuleConfig;
import io.vertigo.commons.analytics.health.HealthCheck;
import io.vertigo.dashboard.services.data.DataProvider;
import io.vertigo.lang.Assertion;

public abstract class AbstractDashboardModuleControler implements DashboardModuleControler {

	@Inject
	protected DataProvider dataProvider;

	@Override
	public Map<String, Object> buildModel(final App app, final String moduleName) {
		final Map<String, Object> model = new HashMap<>();
		//---
		initModuleModel(app, model, moduleName);
		doBuildModel(app, model);
		return model;
	}

	private void initModuleModel(final App app, final Map<String, Object> model, final String moduleName) {
		final Set<String> modules = app.getConfig().getModuleConfigs().stream().map(ModuleConfig::getName).collect(Collectors.toSet());
		Assertion.checkState(modules.contains(moduleName), "no module with name '{0}' found in the app", moduleName);
		//---
		model.put("modules", modules);
		//---
		final List<HealthCheck> healthChecks = dataProvider.getHealthChecks();
		final Map<String, List<HealthCheck>> healthChecksByTopic = healthChecks
				.stream()
				.filter(healthCheck -> moduleName.equals(healthCheck.getFeature()))
				.collect(Collectors.groupingBy(HealthCheck::getTopic, Collectors.toList()));

		final Set<String> topics = healthChecks
				.stream()
				.filter(healthCheck -> moduleName.equals(healthCheck.getFeature()))
				.map(HealthCheck::getTopic)
				.collect(Collectors.toSet());

		//---
		model.put("topics", topics);
		model.put("healthchecksByTopic", healthChecksByTopic);
		model.put("moduleName", moduleName);
	}

	public abstract void doBuildModel(final App app, final Map<String, Object> model);

}
