package io.vertigo.orchestra.domain.referential;

import io.vertigo.core.lang.Generated;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.dynamo.domain.stereotype.DataSpace("orchestra")
public final class OExecutionState implements Entity {
	private static final long serialVersionUID = 1L;

	private String estCd;
	private String label;

	/** {@inheritDoc} */
	@Override
	public UID<OExecutionState> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Code'.
	 * @return String estCd <b>Obligatoire</b>
	 */
	@Field(domain = "DoOCodeIdentifiant", type = "ID", required = true, label = "Code")
	public String getEstCd() {
		return estCd;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Code'.
	 * @param estCd String <b>Obligatoire</b>
	 */
	public void setEstCd(final String estCd) {
		this.estCd = estCd;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Libellé'.
	 * @return String label
	 */
	@Field(domain = "DoOLibelle", label = "Libellé")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Libellé'.
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
