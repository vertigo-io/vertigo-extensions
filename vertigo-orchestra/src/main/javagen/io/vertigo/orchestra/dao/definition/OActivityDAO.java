package io.vertigo.orchestra.dao.definition;

import javax.inject.Inject;

import java.util.Optional;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Home;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.orchestra.domain.definition.OActivity;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OActivityDAO extends DAO<OActivity, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public OActivityDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(OActivity.class, storeManager, taskManager);
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
	 * Execute la tache TkGetActivitiesByProId.
	 * @param proId Long 
	 * @return DtList de OActivity dtOActivities
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.definition.OActivity> getActivitiesByProId(final Long proId) {
		final Task task = createTaskBuilder("TkGetActivitiesByProId")
				.addValue("proId", proId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetAllActivitiesInActiveProcesses.
	 * @return DtList de OActivity dtOActivities
	*/
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.orchestra.domain.definition.OActivity> getAllActivitiesInActiveProcesses() {
		final Task task = createTaskBuilder("TkGetAllActivitiesInActiveProcesses")
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetFirstActivityByProcess.
	 * @param proId Long 
	 * @return OActivity dtOActivity
	*/
	public io.vertigo.orchestra.domain.definition.OActivity getFirstActivityByProcess(final Long proId) {
		final Task task = createTaskBuilder("TkGetFirstActivityByProcess")
				.addValue("proId", proId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetNextActivityByActId.
	 * @param actId Long 
	 * @return Option de OActivity dtOActivity
	*/
	public Optional<io.vertigo.orchestra.domain.definition.OActivity> getNextActivityByActId(final Long actId) {
		final Task task = createTaskBuilder("TkGetNextActivityByActId")
				.addValue("actId", actId)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.definition.OActivity) getTaskManager()
				.execute(task)
				.getResult());
	}

}
