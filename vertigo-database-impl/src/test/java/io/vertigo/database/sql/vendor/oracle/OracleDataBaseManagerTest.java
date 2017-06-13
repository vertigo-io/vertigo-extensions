package io.vertigo.database.sql.vendor.oracle;

import io.vertigo.database.sql.AbstractSqlDataBaseManagerTest;
import io.vertigo.database.sql.connection.SqlConnection;
import io.vertigo.database.sql.vendor.SqlDialect.GenerationMode;

public final class OracleDataBaseManagerTest extends AbstractSqlDataBaseManagerTest {
	private static final String CREATE_TABLE_MOVIE = "CREATE TABLE movie ( "
			+ "id 						NUMBER(6), "
			+ "title 					VARCHAR2(255), "
			+ "fps 						NUMBER(6,3), "
			+ "income 					NUMBER(6,3), "
			+ "color 					NUMBER(1), "
			+ "release_date 			DATE, "
			+ "release_local_date 		DATE, "
			+ "release_zoned_date_time 	DATE, "
			+ "icon 					BLOB"
			+ ")";
	private static final String CREATE_SEQUENCE_MOVIE = "CREATE SEQUENCE seq_movie";

	@Override
	protected final void doSetUp() throws Exception {
		//A chaque test on recrée la table famille
		final SqlConnection connection = obtainMainConnection();
		try {
			execpreparedStatement(connection, CREATE_TABLE_MOVIE);
			execpreparedStatement(connection, CREATE_SEQUENCE_MOVIE);
		} finally {
			connection.release();
		}
	}

	@Override
	protected GenerationMode getExpectedGenerationMode() {
		return GenerationMode.GENERATED_COLUMNS;
	}
}
