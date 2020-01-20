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
package io.vertigo.datastore.entitystore.sql.vendor.oracle;

import java.util.List;

import org.junit.jupiter.api.Disabled;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.util.ListBuilder;
import io.vertigo.database.impl.sql.vendor.oracle.Oracle11DataBase;
import io.vertigo.datastore.entitystore.data.domain.car.Car;
import io.vertigo.datastore.entitystore.sql.AbstractSqlStoreManagerTest;
import io.vertigo.datastore.entitystore.sql.SqlDataStoreNodeConfig;
import io.vertigo.dynamo.ngdomain.SmartTypeDefinition;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskResult;
import io.vertigo.dynamox.task.TaskEngineProc;

/**
 * Test of sql storage in Oracle DB.
 * @author mlaroche
 *
 */
@Disabled
public final class OracleSqlStoreManagerTest extends AbstractSqlStoreManagerTest {

	@Override
	protected NodeConfig buildNodeConfig() {
		return SqlDataStoreNodeConfig.build(
				Oracle11DataBase.class.getCanonicalName(),
				"oracle.jdbc.OracleDriver",
				"jdbc:oracle:thin:DT_VERTIGO/DT_VERTIGO@selma.dev.klee.lan.net:1521/O11UTF8");
	}

	@Override
	protected List<String> getCreateFamilleRequests() {
		return new ListBuilder<String>()
				.add(" create table famille(fam_id NUMBER , LIBELLE varchar(255))")
				.add(" create sequence SEQ_FAMILLE start with 10001 increment by 1")
				.build();
	}

	@Override
	protected List<String> getCreateCarRequests() {
		return new ListBuilder<String>()
				.add(" create table fam_car_location(fam_id NUMBER , ID NUMBER)")
				.add(" create table car(ID NUMBER, FAM_ID NUMBER, MANUFACTURER varchar(50), MODEL varchar(255), DESCRIPTION varchar(512), YEAR INT, KILO INT, PRICE INT, CONSOMMATION NUMERIC(8,2), MTY_CD varchar(50) )")
				.add(" create sequence SEQ_CAR start with 10001 increment by 1")
				.build();
	}

	@Override
	protected List<String> getCreateFileInfoRequests() {
		return new ListBuilder<String>()
				.add(" create table VX_FILE_INFO(FIL_ID NUMBER , FILE_NAME varchar(255), MIME_TYPE varchar(255), LENGTH NUMBER, LAST_MODIFIED date, FILE_DATA BLOB)")
				.add(" create sequence SEQ_VX_FILE_INFO start with 10001 increment by 1")
				.build();
	}

	@Override
	protected final List<String> getDropRequests() {
		return new ListBuilder<String>()
				.add(" drop table VX_FILE_INFO ")
				.add(" drop sequence SEQ_VX_FILE_INFO")
				.add(" drop table fam_car_location")
				.add(" drop table car")
				.add(" drop sequence SEQ_CAR")
				.add(" drop table famille")
				.add(" drop sequence SEQ_FAMILLE")
				.build();
	}

	@Override
	protected void nativeInsertCar(final Car car) {
		Assertion.checkArgument(car.getId() == null, "L'id n'est pas null {0}", car.getId());
		//-----
		final DefinitionSpace definitionSpace = getApp().getDefinitionSpace();
		final SmartTypeDefinition smartTypeCar = definitionSpace.resolve("STyDtCar", SmartTypeDefinition.class);

		final TaskDefinition taskDefinition = TaskDefinition.builder("TkInsertCar")
				.withEngine(TaskEngineProc.class)
				.withRequest("insert into CAR (ID, FAM_ID,MANUFACTURER, MODEL, DESCRIPTION, YEAR, KILO, PRICE, MTY_CD) values "
						+ "(SEQ_CAR.nextval, #dtoCar.famId#, #dtoCar.manufacturer#, #dtoCar.model#, #dtoCar.description#, #dtoCar.year#, #dtoCar.kilo#, #dtoCar.price#, #dtoCar.mtyCd#)")
				.addInAttribute("dtoCar", smartTypeCar, Cardinality.ONE)
				.build();

		final Task task = Task.builder(taskDefinition)
				.addValue("dtoCar", car)
				.build();
		final TaskResult taskResult = taskManager
				.execute(task);
		nop(taskResult);
	}

}
