package io.vertigo.studio.dao.car;

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
import io.vertigo.datamodel.smarttype.ModelManager;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.studio.domain.car.Car;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class CarDAO extends DAO<Car, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public CarDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final ModelManager modelManager) {
		super(Car.class, entityStoreManager, taskManager, modelManager);
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
	 * Execute la tache StTkGetFirstCar.
	 * @return Option de Car dtoCar
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkGetFirstCar",
			request = "select * from car" + 
 "				limit 1",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtCar")
	public Optional<io.vertigo.studio.domain.car.Car> getFirstCar() {
		final Task task = createTaskBuilder("TkGetFirstCar")
				.build();
		return Optional.ofNullable((io.vertigo.studio.domain.car.Car) getTaskManager()
				.execute(task)
				.getResult());
	}

	/**
	 * Execute la tache StTkListCars.
	 * @param dtoCarIn Car
	 * @return Car dtoCarOut
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkListCars",
			request = "hello",
			taskEngineClass = io.vertigo.studio.data.domain.CarEngine.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtCar")
	public io.vertigo.studio.domain.car.Car listCars(@io.vertigo.datamodel.task.proxy.TaskInput(name = "dtoCarIn", smartType = "STyDtCar") final io.vertigo.studio.domain.car.Car dtoCarIn) {
		final Task task = createTaskBuilder("TkListCars")
				.addValue("dtoCarIn", dtoCarIn)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache StTkSelectCarByIds.
	 * @param input DtList de Car
	 * @return DtList de Car dtoCar
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkSelectCarByIds",
			request = "select * from car where id in (#input.rownum.id#)",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtCar")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.studio.domain.car.Car> selectCarByIds(@io.vertigo.datamodel.task.proxy.TaskInput(name = "input", smartType = "STyDtCar") final io.vertigo.datamodel.structure.model.DtList<io.vertigo.studio.domain.car.Car> input) {
		final Task task = createTaskBuilder("TkSelectCarByIds")
				.addValue("input", input)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}