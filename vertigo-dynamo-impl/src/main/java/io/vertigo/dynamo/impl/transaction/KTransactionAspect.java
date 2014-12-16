/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.impl.transaction;

import io.vertigo.core.aop.Aspect;
import io.vertigo.core.aop.AspectMethodInvocation;
import io.vertigo.dynamo.transaction.KTransactionManager;
import io.vertigo.dynamo.transaction.KTransactionWritable;
import io.vertigo.dynamo.transaction.Transactional;
import io.vertigo.lang.Assertion;

import javax.inject.Inject;

/**
 * Aspect pour la gestion des transactions au niveau
 * de la couche service.
 * @author prahmoune
 */
public final class KTransactionAspect implements Aspect {
	private final KTransactionManager transactionManager;

	@Inject
	public KTransactionAspect(final KTransactionManager transactionManager) {
		Assertion.checkNotNull(transactionManager);
		//---------------------------------------------------------------------
		this.transactionManager = transactionManager;
	}

	@Override
	public Object invoke(final Object[] args, final AspectMethodInvocation methodInvocation) throws Throwable {
		//La transaction est REQUIRED : si elle existe on l'utilise, sinon on la crée.
		if (transactionManager.hasCurrentTransaction()) {
			return methodInvocation.proceed(args);
		}
		//Dans le cas ou il n'existe pas de transaction on en crée une.
		try (final KTransactionWritable transaction = transactionManager.createCurrentTransaction()) {
			final Object o = methodInvocation.proceed(args);
			transaction.commit();
			return o;
		}
	}

	@Override
	public Class<?> getAnnotationType() {
		return Transactional.class;
	}
}