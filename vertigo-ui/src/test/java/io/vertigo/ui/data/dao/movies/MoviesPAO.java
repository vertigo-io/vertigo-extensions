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
package io.vertigo.ui.data.dao.movies;

import java.util.List;

import javax.inject.Inject;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Home;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.ui.data.domain.movies.MovieIndex;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class MoviesPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public MoviesPAO(final TaskManager taskManager) {
		Assertion.checkNotNull(taskManager);
		//-----
		this.taskManager = taskManager;
	}

	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Home.getApp().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache TK_LOAD_MOVIE_INDEX.
	 * @param dtc io.vertigo.dynamo.domain.model.DtList<io.vertigo.pandora.domain.movies.Dummy>
	 * @return io.vertigo.dynamo.domain.model.DtList<io.vertigo.pandora.domain.movies.MovieIndex> dtcIndex
	*/
	public io.vertigo.dynamo.domain.model.DtList<MovieIndex> loadMovieIndex(final List<Long> movieIds) {
		final Task task = createTaskBuilder("TkLoadMovieIndex")
				.addValue("movieIds", movieIds)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TK_REMOVE_ALL_ACTOR_ROLES.
	*/
	public void removeAllActorRoles() {
		final Task task = createTaskBuilder("TkRemoveAllActorRoles")
				.build();
		getTaskManager().execute(task);
	}

	/**
	 * Execute la tache TK_REMOVE_ALL_MOVIES.
	*/
	public void removeAllMovies() {
		final Task task = createTaskBuilder("TkRemoveAllMovies")
				.build();
		getTaskManager().execute(task);
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
