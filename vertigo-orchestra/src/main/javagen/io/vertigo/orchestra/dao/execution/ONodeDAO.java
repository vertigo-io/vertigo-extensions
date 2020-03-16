package io.vertigo.orchestra.dao.execution;

import javax.inject.Inject;

import java.util.Optional;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Home;
import io.vertigo.datamodel.task.metamodel.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.orchestra.domain.execution.ONode;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class ONodeDAO extends DAO<ONode, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public ONodeDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(ONode.class, entityStoreManager, taskManager, smartTypeManager);
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
	 * Execute la tache StTkGetNodeByName.
	 * @param nodeName String
	 * @return Option de ONode dtoONode
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetNodeByName",
			request = "select " + 
 "        		nod.*" + 
 "        	from o_node nod" + 
 "        	where nod.NAME = #nodeName#",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtONode")
	public Optional<io.vertigo.orchestra.domain.execution.ONode> getNodeByName(@io.vertigo.datamodel.task.proxy.TaskInput(name = "nodeName", smartType = "STyOLibelle") final String nodeName) {
		final Task task = createTaskBuilder("TkGetNodeByName")
				.addValue("nodeName", nodeName)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.execution.ONode) getTaskManager()
				.execute(task)
				.getResult());
	}

}
