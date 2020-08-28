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
package io.vertigo.commons.command;

import io.vertigo.core.lang.Assertion;

public final class CommandResponse<P> {

	private final CommandResponseStatus responseStatus;
	private final String display;
	private final String targetUrl; // may be null
	private final P payload; // may be null

	CommandResponse(
			final CommandResponseStatus responseStatus,
			final String display,
			final P payload,
			final String targetUrl) {
		Assertion.check()
				.isNotNull(responseStatus)
				.isNotNull(display);
		//---
		this.responseStatus = responseStatus;
		this.display = display;
		this.payload = payload;
		this.targetUrl = targetUrl;
	}

	public static <P> CommandResponseBuilder<P> builder() {
		return new CommandResponseBuilder<>();
	}

	public CommandResponseStatus getStatus() {
		return responseStatus;
	}

	public String getDisplay() {
		return display;
	}

	public P getPayload() {
		return payload;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

}
