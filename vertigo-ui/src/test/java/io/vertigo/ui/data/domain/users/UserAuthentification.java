package io.vertigo.ui.data.domain.users;

import io.vertigo.core.lang.Generated;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.dynamo.ngdomain.annotations.Mapper(clazz = io.vertigo.dynamo.domain.util.JsonMapper.class, dataType = io.vertigo.dynamo.domain.metamodel.DataType.String)
public final class UserAuthentification implements Entity {
	private static final long serialVersionUID = 1L;

	private Long authId;
	private String login;
	private String password;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "AAuthUsr",
			fkFieldName = "usrId",
			primaryDtDefinitionName = "DtApplicationUser",
			primaryIsNavigable = true,
			primaryRole = "ApplicationUser",
			primaryLabel = "Application user",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtUserAuthentification",
			foreignIsNavigable = false,
			foreignRole = "UserAuthentification",
			foreignLabel = "User authentification",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.ui.data.domain.users.ApplicationUser> usrIdAccessor = new StoreVAccessor<>(io.vertigo.ui.data.domain.users.ApplicationUser.class, "ApplicationUser");

	/** {@inheritDoc} */
	@Override
	public UID<UserAuthentification> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'AUTH_ID'.
	 * @return Long authId <b>Obligatoire</b>
	 */
	@Field(domain = "STyId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "AUTH_ID")
	public Long getAuthId() {
		return authId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'AUTH_ID'.
	 * @param authId Long <b>Obligatoire</b>
	 */
	public void setAuthId(final Long authId) {
		this.authId = authId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Login'.
	 * @return String login
	 */
	@Field(domain = "STyLabelShort", label = "Login")
	public String getLogin() {
		return login;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Login'.
	 * @param login String
	 */
	public void setLogin(final String login) {
		this.login = login;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Password'.
	 * @return String password
	 */
	@Field(domain = "STyPassword", label = "Password")
	public String getPassword() {
		return password;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Password'.
	 * @param password String
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Application user'.
	 * @return Long usrId <b>Obligatoire</b>
	 */
	@io.vertigo.dynamo.domain.stereotype.ForeignKey(domain = "STyId", label = "Application user", fkDefinition = "DtApplicationUser" )
	public Long getUsrId() {
		return (Long) usrIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Application user'.
	 * @param usrId Long <b>Obligatoire</b>
	 */
	public void setUsrId(final Long usrId) {
		usrIdAccessor.setId(usrId);
	}

 	/**
	 * Association : Application user.
	 * @return l'accesseur vers la propriété 'Application user'
	 */
	public StoreVAccessor<io.vertigo.ui.data.domain.users.ApplicationUser> applicationUser() {
		return usrIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
