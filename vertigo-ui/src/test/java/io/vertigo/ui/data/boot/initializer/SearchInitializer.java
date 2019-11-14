/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.ui.data.boot.initializer;

import javax.inject.Inject;

import io.vertigo.core.node.Home;
import io.vertigo.core.node.component.ComponentInitializer;
import io.vertigo.dynamo.search.SearchManager;
import io.vertigo.ui.data.domain.movies.Movie;

/**
 * Initializer de LocaleManager.
 * @author dchallas
 * @version $Id: LocaleManagerInitializer.java,v 1.4 2014/02/07 16:48:27 npiedeloup Exp $
 */
public final class SearchInitializer implements ComponentInitializer {

	@Inject
	private SearchManager searchManager;

	/** {@inheritDoc} */
	@Override
	public void init() {
		Home.getApp().registerPreActivateFunction(() -> searchManager.reindexAll(searchManager.findFirstIndexDefinitionByKeyConcept(Movie.class)));
	}
}
