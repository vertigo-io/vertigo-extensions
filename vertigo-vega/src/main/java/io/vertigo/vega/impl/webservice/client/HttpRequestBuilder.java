package io.vertigo.vega.impl.webservice.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Builder;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.vega.engines.webservice.json.JsonEngine;
import io.vertigo.vega.webservice.definitions.WebServiceDefinition.Verb;
import io.vertigo.vega.webservice.definitions.WebServiceParam;
import io.vertigo.vega.webservice.model.ExtendedObject;

public final class HttpRequestBuilder implements Builder<HttpRequest> {

	private final JsonEngine jsonWriterEngine;

	private final String urlPrefix;
	private final String resourcePath;

	private final java.net.http.HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
	private final Map<String, String> pathParams = new HashMap<>();
	private final Map<String, String> queryParams = new HashMap<>();
	private Verb verb = null;
	private Object body = null;

	private final Set<String> includedFields = new HashSet<>();
	private final Set<String> excludedFields = new HashSet<>();

	public HttpRequestBuilder(final String urlPrefix, final String resourcePath, final JsonEngine jsonWriterEngine) {
		Assertion.check()
				.isTrue(resourcePath.startsWith("/"), "resourcePath ({0}) must starts with /", resourcePath)
				.isNotNull(jsonWriterEngine);
		//---
		this.urlPrefix = urlPrefix;
		this.resourcePath = resourcePath;
		this.jsonWriterEngine = jsonWriterEngine;

	}

	@Override
	public HttpRequest build() {
		httpRequestBuilder.uri(buildURI());

		BodyPublisher bodyPublisher = BodyPublishers.noBody();
		if (body != null) {
			final String jsonBody;
			if (body instanceof ExtendedObject) {
				final ExtendedObject extendedObject = (ExtendedObject) body;
				jsonBody = jsonWriterEngine.toJsonWithMeta(extendedObject.getInnerObject(), extendedObject, includedFields, excludedFields);
			} else {
				jsonBody = jsonWriterEngine.toJsonWithMeta(body, Collections.emptyMap(), includedFields, excludedFields);
			}
			bodyPublisher = BodyPublishers.ofString(jsonBody);
		}

		switch (verb) {
			case Delete:
				httpRequestBuilder.DELETE();
				break;
			case Get:
				httpRequestBuilder.GET();
				break;
			case Patch:
				httpRequestBuilder.method("patch", bodyPublisher);
				break;
			case Post:
				httpRequestBuilder.POST(bodyPublisher);
				break;
			case Put:
				httpRequestBuilder.PUT(bodyPublisher);
				break;
			default:
				break;
		}

		return httpRequestBuilder.build();
	}

	public URI buildURI() {
		final StringBuilder resourceQuery = new StringBuilder(urlPrefix);
		String resourcePathMerged = resourcePath;
		for (final Entry<String, String> param : pathParams.entrySet()) {
			resourcePathMerged = resourcePathMerged.replace("{" + param.getKey() + "}", encodeURL(param.getValue()));
		}
		resourceQuery.append(resourcePathMerged);
		char sep = '?';
		for (final Entry<String, String> param : queryParams.entrySet()) {
			resourceQuery.append(sep)
					.append(encodeURL(param.getKey()))
					.append('=')
					.append(encodeURL(param.getValue()));
			sep = '&';
		}
		return URI.create(resourceQuery.toString());
	}

	public void header(final String name, final String value) {
		httpRequestBuilder.setHeader(name, value);
	}

	public void pathParam(final String name, final String value) {
		pathParams.put(name, value);
	}

	public void queryParam(final String name, final String value) {
		queryParams.put(name, value);
	}

	private String encodeURL(final String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (final UnsupportedEncodingException e) {
			throw WrappedException.wrap(e);
		}
	}

	public void innerBodyParam(final String name, final Object object, final WebServiceParam webServiceParam) {
		if(body == null) {
			body = new HashMap<>();
		}
		Assertion.check().isTrue(body instanceof Map, "Can't merge body content");
		//---
		final Map<String, Object> innerBody = (Map<String, Object>) body;

		includedFields.addAll(webServiceParam.getIncludedFields().stream()
				.map(n -> name + "." + n)
				.collect(Collectors.toSet()));
		excludedFields.addAll(webServiceParam.getExcludedFields().stream()
				.map(n -> name + "." + n)
				.collect(Collectors.toSet()));
		innerBody.put(name, object);
	}

	public void bodyParam(final Object object, final WebServiceParam webServiceParam) {
		final ExtendedObject extendedObject = new ExtendedObject(object);
		includedFields.addAll(webServiceParam.getIncludedFields());
		excludedFields.addAll(webServiceParam.getExcludedFields());
		if (body != null) {
			Assertion.check().isTrue(body instanceof Map, "Can't merge two bodies {0}", webServiceParam.getName());
			extendedObject.putAll((Map<String, Object>) body);
		}
		body = extendedObject;
	}

	public void verb(final Verb verb) {
		this.verb = verb;
	}

}
