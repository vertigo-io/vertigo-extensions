package io.vertigo.orchestra.domain.execution;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
 @Generated
@io.vertigo.dynamo.domain.stereotype.DataSpace("orchestra")
public final class OJobRunning implements Entity {
	private static final long serialVersionUID = 1L;

	private Long jruId;
	private String jobname;
	private Long nodId;
	private java.util.Date dateExec;
	private final VAccessor<io.vertigo.orchestra.domain.referential.OUser> usrIdAccessor = new VAccessor<>(io.vertigo.orchestra.domain.referential.OUser.class, "user");

	/** {@inheritDoc} */
	@Override
	public URI<OJobRunning> getURI() {
		return DtObjectUtil.createURI(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id de la definition du job'.
	 * @return Long jruId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "ID", required = true, label = "Id de la definition du job")
	public Long getJruId() {
		return jruId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id de la definition du job'.
	 * @param jruId Long <b>Obligatoire</b>
	 */
	public void setJruId(final Long jruId) {
		this.jruId = jruId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nom du job'.
	 * @return String jobname
	 */
	@Field(domain = "DO_O_LIBELLE", label = "Nom du job")
	public String getJobname() {
		return jobname;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nom du job'.
	 * @param jobname String
	 */
	public void setJobname(final String jobname) {
		this.jobname = jobname;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id du noeud'.
	 * @return Long nodId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", required = true, label = "Id du noeud")
	public Long getNodId() {
		return nodId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id du noeud'.
	 * @param nodId Long <b>Obligatoire</b>
	 */
	public void setNodId(final Long nodId) {
		this.nodId = nodId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de début'.
	 * @return java.util.Date dateExec
	 */
	@Field(domain = "DO_O_TIMESTAMP", label = "Date de début")
	public java.util.Date getDateExec() {
		return dateExec;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de début'.
	 * @param dateExec java.util.Date
	 */
	public void setDateExec(final java.util.Date dateExec) {
		this.dateExec = dateExec;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'User'.
	 * @return Long usrId
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "FOREIGN_KEY", label = "User")
	public Long getUsrId() {
		return (Long)  usrIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'User'.
	 * @param usrId Long
	 */
	public void setUsrId(final Long usrId) {
		usrIdAccessor.setId(usrId);
	}

	/**
	 * Association : User.
	 * @return io.vertigo.orchestra.domain.referential.OUser
	 */
	public io.vertigo.orchestra.domain.referential.OUser getUser() {
		return usrIdAccessor.get();
	}

	/**
	 * Retourne l'URI: User.
	 * @return URI de l'association
	 */
	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_JOB_USR",
			fkFieldName = "USR_ID",
			primaryDtDefinitionName = "DT_O_USER",
			primaryIsNavigable = true,
			primaryRole = "User",
			primaryLabel = "User",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_O_JOB_RUNNING",
			foreignIsNavigable = false,
			foreignRole = "JobRunning",
			foreignLabel = "JobRunning",
			foreignMultiplicity = "0..*")
	public io.vertigo.dynamo.domain.model.URI<io.vertigo.orchestra.domain.referential.OUser> getUserURI() {
		return usrIdAccessor.getURI();
	}


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
