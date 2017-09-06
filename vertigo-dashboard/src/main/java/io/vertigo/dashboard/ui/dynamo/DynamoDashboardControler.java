package io.vertigo.dashboard.ui.dynamo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.vertigo.app.App;
import io.vertigo.app.Home;
import io.vertigo.commons.analytics.metric.Metric;
import io.vertigo.dashboard.ui.AbstractDashboardModuleControler;
import io.vertigo.dashboard.ui.dynamo.model.DomainModel;
import io.vertigo.dashboard.ui.dynamo.model.EntityModel;
import io.vertigo.dynamo.domain.metamodel.Domain;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtStereotype;

public class DynamoDashboardControler extends AbstractDashboardModuleControler {

	@Override
	public void doBuildModel(final App app, final Map<String, Object> model) {
		buildEntityModel(app, model);
		buildDomainModel(app, model);
	}

	private void buildEntityModel(final App app, final Map<String, Object> model) {
		final Map<String, Double> entityCounts = new HashMap<>();
		final List<Metric> metrics = dataProvider.getMetrics();

		metrics
				.stream()
				.filter(metric -> "entityCount".equals(metric.getName()))
				.forEach(metric -> entityCounts.put(metric.getTopic(), metric.getValue()));

		final Map<String, Double> taskCounts = new HashMap<>();
		metrics
				.stream()
				.filter(metric -> "definitionUsageInDao".equals(metric.getName()))
				.forEach(metric -> taskCounts.put(metric.getTopic(), metric.getValue()));

		final Map<String, Double> fieldCount = new HashMap<>();
		metrics
				.stream()
				.filter(metric -> "definitionFieldCount".equals(metric.getName()))
				.forEach(metric -> fieldCount.put(metric.getTopic(), metric.getValue()));

		final Collection<DtDefinition> dtDefinitions = Home.getApp().getDefinitionSpace().getAll(DtDefinition.class);
		final List<EntityModel> entities = dtDefinitions
				.stream()
				.filter(DtDefinition::isPersistent)
				.map(dtDefinition -> new EntityModel(
						dtDefinition,
						entityCounts.get(dtDefinition.getName()),
						taskCounts.get(dtDefinition.getName()),
						fieldCount.get(dtDefinition.getName())))
				.collect(Collectors.toList());
		model.put("entities", entities);
		//---
		model.put("entityCount", entities.size());
		final long keyConceptCount = dtDefinitions.stream().filter(dtDefinition -> dtDefinition.getStereotype() == DtStereotype.KeyConcept).count();
		model.put("keyConceptCount", keyConceptCount);
	}

	private void buildDomainModel(final App app, final Map<String, Object> model) {
		final Map<String, Double> taskCount = new HashMap<>();
		final List<Metric> metrics = dataProvider.getMetrics();
		metrics
				.stream()
				.filter(metric -> "domainUsageInTasks".equals(metric.getName()))
				.forEach(metric -> taskCount.put(metric.getTopic(), metric.getValue()));

		final Map<String, Double> dtDefinitionCount = new HashMap<>();
		metrics
				.stream()
				.filter(metric -> "domainUsageInDtDefinitions".equals(metric.getName()))
				.forEach(metric -> dtDefinitionCount.put(metric.getTopic(), metric.getValue()));

		final Collection<Domain> domains = Home.getApp().getDefinitionSpace().getAll(Domain.class);
		final List<DomainModel> domainModels = domains
				.stream()
				.filter(domain -> domain.getDataType().isPrimitive()) // we display only primitives
				.map(domain -> new DomainModel(
						domain,
						taskCount.get(domain.getName()),
						dtDefinitionCount.get(domain.getName())))
				.collect(Collectors.toList());

		model.put("domains", domainModels);

	}

}