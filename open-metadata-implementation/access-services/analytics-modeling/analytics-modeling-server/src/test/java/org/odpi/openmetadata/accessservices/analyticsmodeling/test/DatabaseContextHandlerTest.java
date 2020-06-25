/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.util.Collections;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Table;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DatabaseContextHandlerTest extends InMemoryRepositoryTest {

	private static final String DATABASE_ADWENTURE_WORKS = "AdwentureWorks";
	private static final String DATABASE_GOSALES = "GOSALES";
	private static final String DATA_TYPE_DECIMAL = "DECIMAL";
	private static final String DATA_TYPE_INTEGER = "INTEGER";
	private static final String DATA_TYPE_VARCHAR = "VARCHAR";
	private static final String PRIMARY_KEY_PREFIX = "PK";
	private static final String SCHEMA_DBO = "dbo";
	private static final String SERVER_TYPE_MS_SQL = "MS SQL";
	private static final String VENDOR_TYPE_INT32 = "INT32";
	private static final String VENDOR_TYPE_STRING = "STRING";
	private static final Integer FROM_INDEX = 0;
	private static final Integer PAGE_SIZE = 20;

	private DatabaseContextHandler databaseContextHandler;

	@BeforeMethod
	public void setup() throws Exception {
		super.setup();
		OMEntityDao omEntityDaoReal = new OMEntityDao(enterpriseConnector, Collections.emptyList(), auditLog);
		databaseContextHandler = new DatabaseContextHandler(omEntityDaoReal);
	}

	@Test
	public void getDatabases() throws AnalyticsModelingCheckedException {
		// setup repository
		createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseEntity(DATABASE_ADWENTURE_WORKS, SERVER_TYPE_MS_SQL, "2.0");

		// test
		List<ResponseContainerDatabase> databases = databaseContextHandler.getDatabases(FROM_INDEX, PAGE_SIZE);
		assertNotNull(databases);
		assertTrue(databases.size() == 2, "Failed retrieve databases.");

		ResponseContainerDatabase gs = databases.stream().filter(ds -> DATABASE_GOSALES.equals(ds.getDbName()))
				.findFirst().orElse(null);
		assertNotNull(gs);
		assertEquals(SERVER_TYPE_MS_SQL, gs.getDbType());
		assertEquals("1.0", gs.getDbVersion());
	}

	@Test
	public void getDatabasesEmptyRepository() throws AnalyticsModelingCheckedException {
		List<ResponseContainerDatabase> databases = databaseContextHandler.getDatabases(FROM_INDEX, PAGE_SIZE);
		assertTrue(databases.size() == 0, "Database list expected to be empty.");
	}

	@Test
	public void getDatabaseSchemasWithEmptyCatalog() throws AnalyticsModelingCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		// test
		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler.getDatabaseSchemas(guidDataSource);
		assertTrue(schemas.isEmpty(), "Schemas list expected to be empty.");

	}

	@Test
	public void getDatabaseSchemas() throws AnalyticsModelingCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		createDatabaseSchemaEntity(guidDataSource, SCHEMA_DBO);
		createDatabaseSchemaEntity(guidDataSource, "sys");

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler.getDatabaseSchemas(guidDataSource);
		assertNotNull(schemas);
		assertTrue(schemas.size() == 2, "Failed to retrieve database schemas.");

		ResponseContainerDatabaseSchema schema = schemas.stream().filter(s -> SCHEMA_DBO.equals(s.getSchema()))
				.findFirst().orElse(null);
		assertNotNull(schema);
		assertEquals(DATABASE_GOSALES, schema.getCatalog());
		assertEquals("user", schema.getSchemaType());
	}

	@Test
	public void getDatabaseSchemasSameNameForTwoCatalogs() throws AnalyticsModelingCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);
		entityDB = createDatabaseEntity("AdventureWorks", SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler.getDatabaseSchemas(entityDB.getGUID());
		assertNotNull(schemas);
		assertTrue(schemas.size() == 1, "Failed to retrieve database schema.");
		assertEquals("AdventureWorks", schemas.get(0).getCatalog());
	}

	@Test
	public void getSchemaTablesForEmptySchema() throws AnalyticsModelingCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);

		ResponseContainerSchemaTables tables = databaseContextHandler.getSchemaTables(entityDB.getGUID(), SCHEMA_DBO);
		assertTrue(tables.getTablesList().isEmpty(), "Table list expected to be empty.");
	}

	@Test
	public void getSchemaTablesForUnknownSchema() {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String schemaName = "NonExistingSchemaName";

		AnalyticsModelingCheckedException thrown = expectThrows(AnalyticsModelingCheckedException.class,
				() -> databaseContextHandler.getSchemaTables(entityDB.getGUID(), schemaName));

		assertEquals(AnalyticsModelingErrorCode.SCHEMA_UNKNOWN.getMessageDefinition().getMessageId(),
				thrown.getReportedErrorMessageId(), "Message Id is not correct");

		assertTrue(thrown.getMessage().contains(schemaName), "Failed schema name should be in the message.");
	}

	@Test
	public void getSchemaTables() throws AnalyticsModelingCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		EntityDetail entitySchema = createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);
		createSchemaTable(entitySchema, "A");
		createSchemaTable(entitySchema, "B");

		ResponseContainerSchemaTables tableResponse = databaseContextHandler.getSchemaTables(entityDB.getGUID(),
				SCHEMA_DBO);
		assertNotNull(tableResponse);
		List<String> tables = tableResponse.getTablesList();

		assertTrue(tables.size() == 2, "Failed to retrieve tables.");
		Collections.sort(tables);
		assertEquals("A,B", String.join(",", tables), "All table names are correct.");
	}

	@Test
	public void getModule() throws Exception {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		EntityDetail entitySchema = createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);

		EntityDetail entityTableDate = createSchemaTable(entitySchema, "Date");
		EntityDetail pkDate = addColumn(entityTableDate, "DateKey", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		setColumnPrimaryKey(pkDate, buildKey(PRIMARY_KEY_PREFIX, "Date", "DateKey"));
		addColumn(entityTableDate, "Date", "TIMESTAMP", "DATETIME");

		// table "Location" has combined primary key with two fields
		EntityDetail entityTableLocation = createSchemaTable(entitySchema, "Location");
		EntityDetail pkLatitude = addColumn(entityTableLocation, "Latitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnPrimaryKey(pkLatitude, buildKey(PRIMARY_KEY_PREFIX, "Location", "Latitude", "Longitude"));
		EntityDetail pkLongitude = addColumn(entityTableLocation, "Longitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnPrimaryKey(pkLongitude, buildKey(PRIMARY_KEY_PREFIX, "Location", "Latitude", "Longitude"));
		addColumn(entityTableLocation, "Destination", DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING);

		EntityDetail entityTableA = createSchemaTable(entitySchema, "Product");
		EntityDetail pkProduct = addColumn(entityTableA, "ProductId", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		setColumnPrimaryKey(pkProduct, buildKey(PRIMARY_KEY_PREFIX, "Product", "ProductId"));
		addColumn(entityTableA, "ProductName", DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING);
		EntityDetail columnEntity = addColumn(entityTableA, "ProductDescription", DATA_TYPE_VARCHAR,
				VENDOR_TYPE_STRING);
		
		setColumnProperty(columnEntity, Constants.IS_NULLABLE, Boolean.TRUE);	// only test of nullable field

		columnEntity = addColumn(entityTableA, "ProductIntroductionDate", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		defineColumnForeignKey(columnEntity, pkDate);
		columnEntity = addColumn(entityTableA, "ProductTerminationDate", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		defineColumnForeignKey(columnEntity, pkDate);

		// fact table tests multiple foreign keys
		EntityDetail entityTableSales = createSchemaTable(entitySchema, "Sales");
		columnEntity = addColumn(entityTableSales, "ProductId", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		defineColumnForeignKey(columnEntity, pkProduct);
		columnEntity = addColumn(entityTableSales, "DateKey", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		defineColumnForeignKey(columnEntity, pkDate);
		addColumn(entityTableSales, "Quantity", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);

		columnEntity = addColumn(entityTableSales, "Price", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnNoteLogProperty(columnEntity, Constants.LENGTH, "19");

		columnEntity = addColumn(entityTableSales, "Discount", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnNoteLogProperty(columnEntity, Constants.LENGTH, "19");

		columnEntity = addColumn(entityTableSales, "ShipmentDestinationLatitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		defineColumnForeignKey(columnEntity, pkLatitude);
		columnEntity = addColumn(entityTableSales, "ShipmentDestinationLongitude", DATA_TYPE_DECIMAL,
				DATA_TYPE_DECIMAL);
		defineColumnForeignKey(columnEntity, pkLongitude);

		// testing is done here
		ResponseContainerModule moduleResponse = databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES,
				SCHEMA_DBO);

		// assert result
		assertNotNull(moduleResponse);
		List<Table> tables = moduleResponse.getModule().getDataSource().get(0).getTable();

		assertEquals(4, tables.size(), "Failed to retrieve tables.");

		String module = TestUtilities.readJsonFile("/src/test/resources/", "getModule");
		TestUtilities.assertObjectJson(moduleResponse, module);
	}

	@Test
	public void getModuleFailure_BadDataSourceGuid() {

		String badGuid = "BadGuid";
		AnalyticsModelingCheckedException thrown = expectThrows(AnalyticsModelingCheckedException.class,
				() -> databaseContextHandler.getModule(badGuid, DATABASE_GOSALES, SCHEMA_DBO));
		assertEquals(AnalyticsModelingErrorCode.GET_ENTITY_EXCEPTION.getMessageDefinition().getMessageId(),
				thrown.getReportedErrorMessageId(), "Message Id is not correct");

		assertTrue(thrown.getMessage().contains(badGuid), "GUID should be in the message.");
	}

	@Test
	public void getModuleFailure_UnknownSchemaName() {

		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");

		String badSchema = "schemaUnknown";
		AnalyticsModelingCheckedException thrown = expectThrows(AnalyticsModelingCheckedException.class,
				() -> databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES, badSchema));

		assertEquals(AnalyticsModelingErrorCode.SCHEMA_UNKNOWN.getMessageDefinition().getMessageId(),
				thrown.getReportedErrorMessageId(), "Message Id is not correct");

		assertTrue(thrown.getMessage().contains(badSchema), "Failed schema name should be in the message.");
	}

	private String buildKey(String... param) {
		return String.join("_", param);
	}
}
