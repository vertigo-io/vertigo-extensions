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
package io.vertigo.datastore.entitystore.sql;

import java.util.List;
import java.util.Optional;

import io.vertigo.basics.task.TaskEngineProc;
import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.commons.transaction.VTransactionWritable;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datamodel.task.definitions.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.StoreUtil;

public final class SqlUtil {
	public static void execRequests(
			final VTransactionManager transactionManager,
			final TaskManager taskManager,
			final List<String> requests,
			final String taskName,
			final Optional<String> optDataSpace) {
		//A chaque test on recrée la table famille
		try (VTransactionWritable transaction = transactionManager.createCurrentTransaction()) {
			for (final String request : requests) {
				final TaskDefinition taskDefinition = TaskDefinition.builder(taskName)
						.withEngine(TaskEngineProc.class)
						.withRequest(request)
						.withDataSpace(optDataSpace.orElse(null))
						.build();
				final Task task = Task.builder(taskDefinition)
						.addContextProperty("connectionName", StoreUtil.getConnectionName(optDataSpace.orElse(EntityStoreManager.MAIN_DATA_SPACE_NAME)))
						.build();
				taskManager.execute(task);
			}
		}
	}
}
