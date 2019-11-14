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
public final class OUser implements Entity {
	private static final long serialVersionUID = 1L;

	private Long usrId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Boolean mailAlert;
	private Boolean active;

	/** {@inheritDoc} */
	@Override
	public UID<OUser> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long usrId <b>Obligatoire</b>
	 */
	@Field(domain = "DoOIdentifiant", type = "ID", required = true, label = "Id")
	public Long getUsrId() {
		return usrId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id'.
	 * @param usrId Long <b>Obligatoire</b>
	 */
	public void setUsrId(final Long usrId) {
		this.usrId = usrId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nom'.
	 * @return String firstName
	 */
	@Field(domain = "DoOLibelle", label = "Nom")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nom'.
	 * @param firstName String
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Prénom'.
	 * @return String lastName
	 */
	@Field(domain = "DoOLibelle", label = "Prénom")
	public String getLastName() {
		return lastName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Prénom'.
	 * @param lastName String
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Email'.
	 * @return String email
	 */
	@Field(domain = "DoOLibelle", label = "Email")
	public String getEmail() {
		return email;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Email'.
	 * @param email String
	 */
	public void setEmail(final String email) {
		this.email = email;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Mot de passe'.
	 * @return String password
	 */
	@Field(domain = "DoOLibelle", label = "Mot de passe")
	public String getPassword() {
		return password;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Mot de passe'.
	 * @param password String
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Alerté en cas d'erreur'.
	 * @return Boolean mailAlert
	 */
	@Field(domain = "DoOBooleen", label = "Alerté en cas d'erreur")
	public Boolean getMailAlert() {
		return mailAlert;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Alerté en cas d'erreur'.
	 * @param mailAlert Boolean
	 */
	public void setMailAlert(final Boolean mailAlert) {
		this.mailAlert = mailAlert;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Compte Actif'.
	 * @return Boolean active
	 */
	@Field(domain = "DoOBooleen", label = "Compte Actif")
	public Boolean getActive() {
		return active;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Compte Actif'.
	 * @param active Boolean
	 */
	public void setActive(final Boolean active) {
		this.active = active;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
