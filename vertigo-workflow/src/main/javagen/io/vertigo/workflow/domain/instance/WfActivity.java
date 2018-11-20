/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.workflow.domain.instance;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.ListVAccessor;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class WfActivity implements Entity {
	private static final long serialVersionUID = 1L;

	private Long wfaId;
	private java.util.Date creationDate;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_WFW_WFA",
			fkFieldName = "WFW_ID",
			primaryDtDefinitionName = "DT_WF_WORKFLOW",
			primaryIsNavigable = true,
			primaryRole = "WfWorkflow",
			primaryLabel = "WfWorkflow",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_WF_ACTIVITY",
			foreignIsNavigable = false,
			foreignRole = "WfActivity",
			foreignLabel = "WfActivity",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.workflow.domain.instance.WfWorkflow> wfwIdAccessor = new VAccessor<>(io.vertigo.workflow.domain.instance.WfWorkflow.class, "WfWorkflow");

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_WFAD_WFA",
			fkFieldName = "WFAD_ID",
			primaryDtDefinitionName = "DT_WF_ACTIVITY_DEFINITION",
			primaryIsNavigable = true,
			primaryRole = "WfActivityDefinition",
			primaryLabel = "WfActivityDefinition",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_WF_ACTIVITY",
			foreignIsNavigable = false,
			foreignRole = "WfActivity",
			foreignLabel = "WfActivity",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.workflow.domain.model.WfActivityDefinition> wfadIdAccessor = new VAccessor<>(io.vertigo.workflow.domain.model.WfActivityDefinition.class, "WfActivityDefinition");

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_WFE_WFA",
			fkFieldName = "WFA_ID",
			primaryDtDefinitionName = "DT_WF_ACTIVITY",
			primaryIsNavigable = false,
			primaryRole = "WfActivity",
			primaryLabel = "WfActivity",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_WF_DECISION",
			foreignIsNavigable = true,
			foreignRole = "WfDecision",
			foreignLabel = "WfDecision",
			foreignMultiplicity = "0..*")
	private final ListVAccessor<io.vertigo.workflow.domain.instance.WfDecision> wfDecisionAccessor = new ListVAccessor<>(this, "A_WFE_WFA", "WfDecision");

	/** {@inheritDoc} */
	@Override
	public UID<WfActivity> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id activity'.
	 * @return Long wfaId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_WF_ID", type = "ID", required = true, label = "Id activity")
	public Long getWfaId() {
		return wfaId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id activity'.
	 * @param wfaId Long <b>Obligatoire</b>
	 */
	public void setWfaId(final Long wfaId) {
		this.wfaId = wfaId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'creation date'.
	 * @return Date creationDate
	 */
	@Field(domain = "DO_WF_DATE", label = "creation date")
	public java.util.Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'creation date'.
	 * @param creationDate Date
	 */
	public void setCreationDate(final java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'WfWorkflow'.
	 * @return Long wfwId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_WF_ID", type = "FOREIGN_KEY", required = true, label = "WfWorkflow")
	public Long getWfwId() {
		return (Long) wfwIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'WfWorkflow'.
	 * @param wfwId Long <b>Obligatoire</b>
	 */
	public void setWfwId(final Long wfwId) {
		wfwIdAccessor.setId(wfwId);
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'WfActivityDefinition'.
	 * @return Long wfadId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_WF_ID", type = "FOREIGN_KEY", required = true, label = "WfActivityDefinition")
	public Long getWfadId() {
		return (Long) wfadIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'WfActivityDefinition'.
	 * @param wfadId Long <b>Obligatoire</b>
	 */
	public void setWfadId(final Long wfadId) {
		wfadIdAccessor.setId(wfadId);
	}

	/**
	 * Association : WfActivityDefinition.
	 * @return l'accesseur vers la propriété 'WfActivityDefinition'
	 */
	public VAccessor<io.vertigo.workflow.domain.model.WfActivityDefinition> wfActivityDefinition() {
		return wfadIdAccessor;
	}

	@Deprecated
	public io.vertigo.workflow.domain.model.WfActivityDefinition getWfActivityDefinition() {
		// we keep the lazyness
		if (!wfadIdAccessor.isLoaded()) {
			wfadIdAccessor.load();
		}
		return wfadIdAccessor.get();
	}

	/**
	 * Retourne l'URI: WfActivityDefinition.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.UID<io.vertigo.workflow.domain.model.WfActivityDefinition> getWfActivityDefinitionURI() {
		return wfadIdAccessor.getURI();
	}

	/**
	 * Association : WfDecision.
	 * @return l'accesseur vers la propriété 'WfDecision'
	 */
	public ListVAccessor<io.vertigo.workflow.domain.instance.WfDecision> wfDecision() {
		return wfDecisionAccessor;
	}

	/**
	 * Association : WfDecision.
	 * @return DtList de io.vertigo.workflow.domain.instance.WfDecision
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.DtList<io.vertigo.workflow.domain.instance.WfDecision> getWfDecisionList() {
		// we keep the lazyness
		if (!wfDecisionAccessor.isLoaded()) {
			wfDecisionAccessor.load();
		}
		return wfDecisionAccessor.get();
	}

	/**
	 * Association URI: WfDecision.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.metamodel.association.DtListURIForSimpleAssociation getWfDecisionDtListURI() {
		return (io.vertigo.dynamo.domain.metamodel.association.DtListURIForSimpleAssociation) wfDecisionAccessor.getDtListURI();
	}

	/**
	 * Association : WfWorkflow.
	 * @return l'accesseur vers la propriété 'WfWorkflow'
	 */
	public VAccessor<io.vertigo.workflow.domain.instance.WfWorkflow> wfWorkflow() {
		return wfwIdAccessor;
	}

	@Deprecated
	public io.vertigo.workflow.domain.instance.WfWorkflow getWfWorkflow() {
		// we keep the lazyness
		if (!wfwIdAccessor.isLoaded()) {
			wfwIdAccessor.load();
		}
		return wfwIdAccessor.get();
	}

	/**
	 * Retourne l'URI: WfWorkflow.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.UID<io.vertigo.workflow.domain.instance.WfWorkflow> getWfWorkflowURI() {
		return wfwIdAccessor.getURI();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
