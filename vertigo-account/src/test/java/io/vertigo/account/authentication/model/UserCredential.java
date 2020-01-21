/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, Vertigo.io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.account.authentication.model;

import io.vertigo.core.lang.Cardinality;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.model.KeyConcept;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.dynamo.domain.util.JsonMapper;
import io.vertigo.dynamo.ngdomain.annotations.Mapper;

/**
 * UserCredential.
 */
@Mapper(clazz = JsonMapper.class, dataType = DataType.String)
public final class UserCredential implements KeyConcept {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private String ucrId;
	private String login;
	private String password;
	private String mail;

	/** {@inheritDoc} */
	@Override
	public UID<UserCredential> getUID() {
		return UID.of(this);
	}

	@Field(domain = "STyCode", type = "ID", cardinality = Cardinality.ONE, label = "Id")
	public String getUcrId() {
		return ucrId;
	}

	public void setUcrId(final String ucrId) {
		this.ucrId = ucrId;
	}

	@Field(domain = "STyLabel", cardinality = Cardinality.ONE, label = "login")
	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	@Field(domain = "STyLabel", cardinality = Cardinality.ONE, label = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Field(domain = "STyLabel", cardinality = Cardinality.ONE, label = "mail")
	public String getMail() {
		return mail;
	}

	public void setMail(final String mail) {
		this.mail = mail;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
