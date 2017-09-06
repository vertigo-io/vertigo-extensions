package io.vertigo.dashboard.impl.services.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Series;

import io.vertigo.commons.analytics.AnalyticsManager;
import io.vertigo.commons.analytics.health.HealthCheck;
import io.vertigo.commons.analytics.metric.Metric;
import io.vertigo.dashboard.services.data.DataFilter;
import io.vertigo.dashboard.services.data.DataProvider;
import io.vertigo.dashboard.services.data.TimeFilter;
import io.vertigo.dashboard.services.data.TimedDataSerie;
import io.vertigo.dashboard.services.data.TimedDatas;
import io.vertigo.lang.Assertion;

public class InfluxDbDataProvider implements DataProvider {

	private final InfluxDB influxDB;

	private List<HealthCheck> appHealthChecks;
	private final Object healthChecksLock = new Object();
	private List<Metric> appMetrics;
	private final Object metricsLock = new Object();

	@Inject
	private AnalyticsManager analyticsManager;

	@Inject
	public InfluxDbDataProvider(
			@Named("host") final String host,
			@Named("user") final String user,
			@Named("password") final String password) {
		Assertion.checkArgNotEmpty(host);
		Assertion.checkArgNotEmpty(user);
		Assertion.checkArgNotEmpty(password);
		//---
		influxDB = InfluxDBFactory.connect(host, user, password);
	}

	@Override
	public TimedDatas getTimeSeries(final DataFilter dataFilter, final TimeFilter timeFilter) {
		final StringBuilder queryBuilder = new StringBuilder("select ");

		String separator = "";
		for (final String measure : dataFilter.getMeasures()) {
			final String[] measureDetails = measure.split(":");
			queryBuilder.append(separator).append(measureDetails[1]).append("(\"").append(measureDetails[0]).append("\") as \"").append(measure).append("\"");
			separator = " ,";
		}
		queryBuilder.append(" from ").append(dataFilter.getMeasurement())
				.append(" where time > ").append(timeFilter.getFrom()).append(" and time <").append(timeFilter.getTo());
		if (!"*".equals(dataFilter.getName())) {
			queryBuilder.append(" and \"name\"='").append(dataFilter.getName()).append("'");
		}
		if (!"*".equals(dataFilter.getLocation())) {
			queryBuilder.append(" and \"location\"='").append(dataFilter.getLocation()).append("'");
		}
		if (dataFilter.getTopic() != null && "*".equals(dataFilter.getTopic())) {
			queryBuilder.append(" and \"topic\"='").append(dataFilter.getTopic()).append("'");
		}

		queryBuilder.append(" group by time(").append(timeFilter.getDim()).append(")");

		final Query query = new Query(queryBuilder.toString(), "pandora");
		final QueryResult queryResult = influxDB.query(query);

		final Series series = queryResult.getResults().get(0).getSeries().get(0);

		final List<TimedDataSerie> dataSeries = series
				.getValues()
				.stream()
				.map(values -> new TimedDataSerie(LocalDateTime.parse(values.get(0).toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toEpochSecond(ZoneOffset.UTC), buildMapValue(series.getColumns(), values)))
				.collect(Collectors.toList());
		return new TimedDatas(dataSeries, series.getColumns().subList(1, series.getColumns().size()));//we remove the first one

	}

	final static Map<String, Object> buildMapValue(final List<String> columns, final List<Object> values) {
		final Map<String, Object> valueMap = new HashMap<>();
		// we start at 1 because time is always the first row
		for (int i = 1; i < columns.size(); i++) {
			valueMap.put(columns.get(i), values.get(i));
		}
		return valueMap;
	}

	@Override
	public List<HealthCheck> getHealthChecks() {
		if (appHealthChecks == null) {
			loadHealthChecks();
		}
		return appHealthChecks;
	}

	private void loadMetrics() {
		synchronized (metricsLock) {
			appMetrics = analyticsManager.getMetrics();
		}

	}

	@Override
	public List<Metric> getMetrics() {
		if (appMetrics == null) {
			loadMetrics();
		}
		return appMetrics;
	}

	private void loadHealthChecks() {
		synchronized (healthChecksLock) {
			appHealthChecks = analyticsManager.getHealthChecks();
		}

	}

}