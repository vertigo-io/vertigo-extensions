<#import "macro_ao.ftl" as lib>
package ${dao.packageName};

import javax.inject.Inject;

<#if dao.options >
import java.util.Optional;
</#if>
import io.vertigo.core.lang.Generated;
<#if !dao.taskDefinitions.empty >
import io.vertigo.core.node.Home;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
</#if>
<#if dao.keyConcept>
import io.vertigo.dynamo.domain.model.UID;
</#if>
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import ${dao.dtClassCanonicalName};

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class ${dao.classSimpleName} extends DAO<${dao.dtClassSimpleName}, ${dao.idFieldType}> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public ${dao.classSimpleName}(final StoreManager storeManager, final TaskManager taskManager) {
		super(${dao.dtClassSimpleName}.class, storeManager, taskManager);
	}

	<#if dao.keyConcept>
	/**
	 * Indique que le keyConcept associé à cette UID va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement)
	 * @param UID UID du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	public ${dao.dtClassSimpleName} readOneForUpdate(final UID<${dao.dtClassSimpleName}> uid) {
		return dataStore.readOneForUpdate(uid);
	}

	/**
	 * Indique que le keyConcept associé à cet id va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement)
	 * @param id Clé du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	public ${dao.dtClassSimpleName} readOneForUpdate(final ${dao.idFieldType} id) {
		return readOneForUpdate(createDtObjectUID(id));
	}
	</#if>
	<#if !dao.taskDefinitions.empty>
	<@lib.generateBody dao.taskDefinitions/>  
	</#if>
}