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
package io.vertigo.ui.data.domain.users;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;
import io.vertigo.datastore.impl.entitystore.StoreListVAccessor;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class SecurityRole implements Entity {
	private static final long serialVersionUID = 1L;

	private String sroCd;
	private String label;

	@io.vertigo.datamodel.structure.stereotype.AssociationNN(
			name = "AnnProSro",
			tableName = "ProSro",
			dtDefinitionA = "DtProfil",
			dtDefinitionB = "DtSecurityRole",
			navigabilityA = false,
			navigabilityB = true,
			roleA = "Profil",
			roleB = "SecurityRole",
			labelA = "Profil",
			labelB = "Security role")
	private final StoreListVAccessor<io.vertigo.ui.data.domain.users.Profil> profilAccessor = new StoreListVAccessor<>(this, "StAnnProSro", "Profil");

	/** {@inheritDoc} */
	@Override
	public UID<SecurityRole> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'SRO_CD'.
	 * @return String sroCd <b>Obligatoire</b>
	 */
	@Field(smartType = "STyCode", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "SRO_CD")
	public String getSroCd() {
		return sroCd;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'SRO_CD'.
	 * @param sroCd String <b>Obligatoire</b>
	 */
	public void setSroCd(final String sroCd) {
		this.sroCd = sroCd;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String label
	 */
	@Field(smartType = "STyLabel", label = "Label")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Label'.
	 * @param label String
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
