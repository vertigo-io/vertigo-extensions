package io.vertigo.ui.data.dao.users;

import javax.inject.Inject;

import io.vertigo.core.lang.Generated;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.dynamo.ngdomain.ModelManager;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.ui.data.domain.users.UserAuthentification;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class UserAuthentificationDAO extends DAO<UserAuthentification, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public UserAuthentificationDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final ModelManager modelManager) {
		super(UserAuthentification.class, entityStoreManager, taskManager, modelManager);
	}

}
