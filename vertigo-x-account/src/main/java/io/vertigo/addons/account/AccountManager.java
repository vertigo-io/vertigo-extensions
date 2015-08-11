package io.vertigo.addons.account;

import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.file.model.VFile;
import io.vertigo.lang.Component;

import java.util.Collection;
import java.util.Set;

/**
 * @author pchretien
 */
public interface AccountManager extends Component {

	void login(URI<Account> accountURI);

	URI<Account> getLoggedAccount();

	//----

	long getNbAccounts();

	Account getAccount(URI<Account> accountURI);

	Set<URI<AccountGroup>> getGroupURIs(URI<Account> accountURI);

	//l'id doit être renseigné !!
	void saveAccount(Account account);

	//-----Gestion des groupes
	long getNbGroups();

	//il est possible de proposer tous les groupes mais pas tous les accounts ?
	Collection<AccountGroup> getAllGroups();

	AccountGroup getGroup(URI<AccountGroup> groupURI);

	Set<URI<Account>> getAccountURIs(URI<AccountGroup> groupURI);

	void saveGroup(AccountGroup group);

	//-----
	void attach(URI<Account> accountURI, URI<AccountGroup> groupURI);

	void detach(URI<Account> accountURI, URI<AccountGroup> groupURI);

	//-----
	void setPhoto(URI<Account> accountURI, VFile photo);

	VFile getPhoto(URI<Account> accountURI);

}