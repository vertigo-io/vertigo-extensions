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
package io.vertigo.dynamox.task;

import java.sql.SQLException;
import java.util.OptionalInt;

import javax.inject.Inject;

import io.vertigo.commons.script.ScriptManager;
import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.database.sql.SqlDataBaseManager;
import io.vertigo.database.sql.connection.SqlConnection;
import io.vertigo.database.sql.statement.SqlStatement;
import io.vertigo.datamodel.smarttype.ModelManager;
import io.vertigo.datastore.entitystore.EntityStoreManager;

/**
 * Permet l'appel de requête de manipulation de données (insert, update, delete)
 * ou de procédures stockées. Une tache utilisant ce provider ne traite pas les
 * DtList.<br>
 * <br>
 * Paramètres d'entrée : n String, Integer, Date, Boolean, ByteArray ou DtObject<br>
 * Paramètres de sortie : n String, Integer, Date, Boolean, ByteArray ou DtObject<br>
 * Paramètres d'entrée/sortie : n String, Integer, Date, Boolean, ByteArray ou DtObject<br>
 * <br>
 * Les paramètres de type DtObject ne peuvent pas être null.<br>
 * <br>
 * Chaine de configuration :<br>
 * La chaine de configuration utilise les délimiteurs #NOM# pour les paramètres IN,
 * %NOM% pour les paramètres OUT et @NOM@ pour les paramètres INOUT. L'utilisation
 * d'une valeur d'un DtObject est déclarée par #DTOBJECT.FIELD#, @DTOBJECT.FIELD@
 * ou %DTOBJECT.FIELD% de manière indépendant de la déclaration du mode d'entrée/sortie
 * pour le DtObject. Ainsi, un DtObject déclaré en IN peut voir un de ses champs utilisé
 * en paramètre OUT.<br>
 * Si un paramètre out ou in/out INT_SQL_ROWCOUNT est défini, il reçoit le résultat de executeUpdate.
 *
 * @author  FCONSTANTIN
 */
public class TaskEngineProc extends AbstractTaskEngineSQL {

	/**
	 * Constructeur.
	 * @param scriptManager Manager de traitment de scripts
	 * @param transactionManager Transaction manager
	 * @param entityStoreManager Store manager
	 * @param sqlDataBaseManager Sql dataBase manager
	 */
	@Inject
	public TaskEngineProc(
			final ScriptManager scriptManager,
			final VTransactionManager transactionManager,
			final EntityStoreManager entityStoreManager,
			final SqlDataBaseManager sqlDataBaseManager,
			final ModelManager modelManager) {
		super(scriptManager, transactionManager, entityStoreManager, sqlDataBaseManager, modelManager);
	}

	/** {@inheritDoc} */
	@Override
	protected OptionalInt doExecute(
			final SqlStatement sqlStatement,
			final SqlConnection connection) throws SQLException {
		return OptionalInt.of(getDataBaseManager().executeUpdate(sqlStatement, getModelManager().getTypeAdapters("sql"), connection));
	}

}