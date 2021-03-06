/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2021, Vertigo.io, team@vertigo.io
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
package io.vertigo.orchestra.monitoring.dao.summary;

import javax.inject.Inject;

import io.vertigo.core.node.Node;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datamodel.task.definitions.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.impl.dao.StoreServices;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
 @Generated
public final class SummaryPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public SummaryPAO(final TaskManager taskManager) {
		Assertion.check().isNotNull(taskManager);
		//-----
		this.taskManager = taskManager;
	}

	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder 
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Node.getNode().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache TkGetExecutionSummariesByDate.
	 * @param dateMin Instant
	 * @param dateMax Instant
	 * @param status String
	 * @return DtList de OExecutionSummary dtcExecutionSummary
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetExecutionSummariesByDate",
			request = "select 	* from (select " + 
 "        		from_exec.PRO_ID," + 
 "        		from_exec.PROCESS_NAME," + 
 "        		from_exec.PROCESS_LABEL," + 
 "        		from_exec.SUCCESSFUL_COUNT," + 
 "        		from_exec.ERRORS_COUNT," + 
 "        		from_exec.RUNNING_COUNT," + 
 "        		from_exec.AVERAGE_EXECUTION_TIME," + 
 "        		from_exec.LAST_EXECUTION_TIME," + 
 "        		from_exec.HEALTH," + 
 "        		" + 
 "        		lat_planif.NEXT_EXECUTION_TIME," + 
 "        		lat_planif.MISFIRED_COUNT" + 
 "				from (" + 
 "					select (select pro1.PRO_ID from o_process pro1 where pro1.name = exec.name and pro1.active_version is true) as PRO_ID," + 
 "						exec.name as PROCESS_NAME," + 
 "						exec.label as PROCESS_LABEL," + 
 "						sum(exec.SI_SUCCESS) as SUCCESSFUL_COUNT," + 
 "						sum(exec.SI_ERREUR) as ERRORS_COUNT," + 
 "						sum(exec.SI_RUNNING) as RUNNING_COUNT," + 
 "						round(extract('epoch' from avg(exec.execution_time))) as AVERAGE_EXECUTION_TIME," + 
 "						max(begin_time) as LAST_EXECUTION_TIME," + 
 "						case when min(exec.ERROR_RANK) = 1 then 'ERROR'" + 
 "							when min(exec.ERROR_RANK) < 6 then 'WARNING'" + 
 "							else 'SUCCESS' end as HEALTH" + 
 "						" + 
 "					from (" + 
 "						select 	pro.pro_id," + 
 "							pro.name," + 
 "							pro.label," + 
 "							pre.END_TIME-pre.BEGIN_TIME as execution_time," + 
 "							case when pre.est_cd='DONE' then 1 else 0 end as SI_SUCCESS," + 
 "							case when pre.est_cd='ERROR' then 1 else 0 end as SI_ERREUR," + 
 "							case when pre.est_cd='RUNNING' then 1 else 0 end as SI_RUNNING," + 
 "							pre.begin_time," + 
 "							pre.EST_CD," + 
 "							case when pre.est_cd='ERROR' then" + 
 "								rank() over(PARTITION by pro.name order by pre.begin_time desc)" + 
 "								else null" + 
 "							end as ERROR_RANK" + 
 "						from o_process pro " + 
 "						join o_process_execution pre on pre.pro_id = pro.pro_id" + 
 "						where pre.begin_time between #dateMin# and #dateMax#" + 
 "						" + 
 "					) exec" + 
 "					group by exec.name, exec.label" + 
 "				) as from_exec" + 
 "				" + 
 "				join ( 	select 	planif.name," + 
 "								min(planif.waitingexpected_time) as NEXT_EXECUTION_TIME," + 
 "								sum(case when planif.SST_CD='MISFIRED' and planif.expected_time between #dateMin# and #dateMax# then 1 else 0 end) as MISFIRED_COUNT" + 
 "						from (" + 
 "							select p2.name," + 
 "								prp.expected_time as expected_time," + 
 "								case when prp.SST_CD = 'WAITING' then prp.expected_time else null end as waitingexpected_time," + 
 "								prp.SST_CD" + 
 "							from o_process_planification prp" + 
 "							join o_process p2 on p2.pro_id=prp.pro_id) as planif " + 
 "						group by planif.name" + 
 "					) lat_planif  on lat_planif.name = from_exec.PROCESS_NAME) as s" + 
 "        " + 
 "			where 1=1" + 
 "			<%if (\"ERROR\".equals(status)) {%>" + 
 "				and s.ERRORS_COUNT > 0" + 
 "			<%}%>" + 
 "			<%if (\"DONE\".equals(status)) {%>" + 
 "				and s.SUCCESSFUL_COUNT > 0" + 
 "			<%}%>" + 
 "			<%if (\"MISFIRED\".equals(status)) {%>" + 
 "				and s.MISFIRED_COUNT > 0" + 
 "			<%}%>" + 
 "			;",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOExecutionSummary")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.orchestra.monitoring.domain.summary.OExecutionSummary> getExecutionSummariesByDate(@io.vertigo.datamodel.task.proxy.TaskInput(name = "dateMin", smartType = "STyOTimestamp") final java.time.Instant dateMin, @io.vertigo.datamodel.task.proxy.TaskInput(name = "dateMax", smartType = "STyOTimestamp") final java.time.Instant dateMax, @io.vertigo.datamodel.task.proxy.TaskInput(name = "status", smartType = "STyOCodeIdentifiant") final String status) {
		final Task task = createTaskBuilder("TkGetExecutionSummariesByDate")
				.addValue("dateMin", dateMin)
				.addValue("dateMax", dateMax)
				.addValue("status", status)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetExecutionSummaryByDateAndName.
	 * @param dateMin Instant
	 * @param dateMax Instant
	 * @param name String
	 * @return OExecutionSummary dtExecutionSummary
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetExecutionSummaryByDateAndName",
			request = "select " + 
 "        		from_process.PRO_ID," + 
 "        		from_process.PROCESS_NAME," + 
 "        		from_process.PROCESS_LABEL," + 
 "        		coalesce(from_exec.SUCCESSFUL_COUNT,0) as SUCCESSFUL_COUNT," + 
 "        		coalesce(from_exec.ERRORS_COUNT,0) as ERRORS_COUNT," + 
 "        		coalesce(from_exec.RUNNING_COUNT,0) as RUNNING_COUNT," + 
 "        		from_exec.AVERAGE_EXECUTION_TIME," + 
 "        		from_exec.LAST_EXECUTION_TIME," + 
 "        		from_exec.HEALTH," + 
 "        		" + 
 "        		lat_planif.NEXT_EXECUTION_TIME," + 
 "        		coalesce(lat_planif.MISFIRED_COUNT, 0) as MISFIRED_COUNT" + 
 "				from ( select  " + 
 "						pro.PRO_ID as PRO_ID," + 
 "						pro.name as PROCESS_NAME," + 
 "						pro.label as PROCESS_LABEL" + 
 "						from o_process pro " + 
 "						where pro.NAME = #name#" + 
 "						  and pro.ACTIVE_VERSION) as from_process" + 
 "				left join lateral (" + 
 "					select " + 
 "						sum(exec.SI_SUCCESS) as SUCCESSFUL_COUNT," + 
 "						sum(exec.SI_ERREUR) as ERRORS_COUNT," + 
 "						sum(exec.SI_RUNNING) as RUNNING_COUNT," + 
 "						round(extract('epoch' from avg(exec.execution_time))) as AVERAGE_EXECUTION_TIME," + 
 "						max(begin_time) as LAST_EXECUTION_TIME," + 
 "						case when min(exec.ERROR_RANK) = 1 then 'ERROR'" + 
 "							when min(exec.ERROR_RANK) < 6 then 'WARNING'" + 
 "							else 'SUCCESS' end as HEALTH" + 
 "						" + 
 "					from (" + 
 "						select 	pre.END_TIME-pre.BEGIN_TIME as execution_time," + 
 "							case when pre.est_cd='DONE' then 1 else 0 end as SI_SUCCESS," + 
 "							case when pre.est_cd='ERROR' then 1 else 0 end as SI_ERREUR," + 
 "							case when pre.est_cd='RUNNING' then 1 else 0 end as SI_RUNNING," + 
 "							pre.begin_time," + 
 "							pre.EST_CD," + 
 "							case when pre.est_cd='ERROR' then" + 
 "								rank() over(PARTITION by pro.name order by pre.begin_time desc)" + 
 "								else null" + 
 "							end as ERROR_RANK" + 
 "						from o_process pro " + 
 "						left join o_process_execution pre on pre.pro_id = pro.pro_id" + 
 "						where pre.begin_time between #dateMin# and #dateMax# and pro.name = #name#" + 
 "						" + 
 "					) exec" + 
 "					" + 
 "				) as from_exec on true" + 
 "" + 
 "				left join lateral" + 
 "				" + 
 "				 ( 	select 	" + 
 "								min(planif.waitingexpected_time) as NEXT_EXECUTION_TIME," + 
 "								sum(case when planif.SST_CD='MISFIRED' and planif.expected_time between #dateMin# and #dateMax# then 1 else 0 end) as MISFIRED_COUNT" + 
 "						from (" + 
 "							select p2.name," + 
 "								prp.expected_time as expected_time," + 
 "								case when prp.SST_CD = 'WAITING' then prp.expected_time else null end as waitingexpected_time," + 
 "								prp.SST_CD" + 
 "							from o_process_planification prp" + 
 "							join o_process p2 on p2.pro_id=prp.pro_id" + 
 "							where p2.name = #name#) as planif " + 
 "						where planif.name = from_process.PROCESS_NAME" + 
 "						group by planif.name" + 
 "					) lat_planif on true;",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOExecutionSummary")
	public io.vertigo.orchestra.monitoring.domain.summary.OExecutionSummary getExecutionSummaryByDateAndName(@io.vertigo.datamodel.task.proxy.TaskInput(name = "dateMin", smartType = "STyOTimestamp") final java.time.Instant dateMin, @io.vertigo.datamodel.task.proxy.TaskInput(name = "dateMax", smartType = "STyOTimestamp") final java.time.Instant dateMax, @io.vertigo.datamodel.task.proxy.TaskInput(name = "name", smartType = "STyOLibelle") final String name) {
		final Task task = createTaskBuilder("TkGetExecutionSummaryByDateAndName")
				.addValue("dateMin", dateMin)
				.addValue("dateMax", dateMax)
				.addValue("name", name)
				.addContextProperty("connectionName", io.vertigo.datastore.impl.dao.StoreUtil.getConnectionName("orchestra"))
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
