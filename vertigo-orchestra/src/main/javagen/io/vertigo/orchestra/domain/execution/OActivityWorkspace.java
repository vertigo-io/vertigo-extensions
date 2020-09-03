/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2020, Vertigo.io, team@vertigo.io
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
package io.vertigo.orchestra.domain.execution;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.datamodel.structure.stereotype.DataSpace("orchestra")
public final class OActivityWorkspace implements Entity {
	private static final long serialVersionUID = 1L;

	private Long acwId;
	private Boolean isIn;
	private String workspace;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "ATkwTke",
			fkFieldName = "aceId",
			primaryDtDefinitionName = "DtOActivityExecution",
			primaryIsNavigable = true,
			primaryRole = "ActivityExecution",
			primaryLabel = "ActivityExecution",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtOActivityWorkspace",
			foreignIsNavigable = false,
			foreignRole = "ActivityWorkspace",
			foreignLabel = "ActivityWorkspace",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.execution.OActivityExecution> aceIdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.execution.OActivityExecution.class, "ActivityExecution");

	/** {@inheritDoc} */
	@Override
	public UID<OActivityWorkspace> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @return Long acwId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOIdentifiant", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id de l'execution d'un processus")
	public Long getAcwId() {
		return acwId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @param acwId Long <b>Obligatoire</b>
	 */
	public void setAcwId(final Long acwId) {
		this.acwId = acwId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Workspace in/out'.
	 * @return Boolean isIn <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOBooleen", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Workspace in/out")
	public Boolean getIsIn() {
		return isIn;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Workspace in/out'.
	 * @param isIn Boolean <b>Obligatoire</b>
	 */
	public void setIsIn(final Boolean isIn) {
		this.isIn = isIn;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Contenu du workspace'.
	 * @return String workspace
	 */
	@Field(smartType = "STyOJsonText", label = "Contenu du workspace")
	public String getWorkspace() {
		return workspace;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Contenu du workspace'.
	 * @param workspace String
	 */
	public void setWorkspace(final String workspace) {
		this.workspace = workspace;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'ActivityExecution'.
	 * @return Long aceId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOIdentifiant", label = "ActivityExecution", fkDefinition = "DtOActivityExecution" )
	public Long getAceId() {
		return (Long) aceIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'ActivityExecution'.
	 * @param aceId Long <b>Obligatoire</b>
	 */
	public void setAceId(final Long aceId) {
		aceIdAccessor.setId(aceId);
	}

 	/**
	 * Association : ActivityExecution.
	 * @return l'accesseur vers la propriété 'ActivityExecution'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.execution.OActivityExecution> activityExecution() {
		return aceIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
