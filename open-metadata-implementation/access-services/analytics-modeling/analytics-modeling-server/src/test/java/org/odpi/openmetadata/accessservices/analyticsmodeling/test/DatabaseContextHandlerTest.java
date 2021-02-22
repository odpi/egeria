/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.analyticsmodeling.converter.DatabaseConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.converter.EmptyConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.converter.SchemaConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.Database;
import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.Schema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Table;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DatabaseContextHandlerTest extends InMemoryRepositoryTest {

	private static final String TBL_LOCATION = "Location";
	private static final String TBL_DATE = "Date";
	private static final String DATABASE_ADVENTURE_WORKS = "AdventureWorks";
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
	private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

	@BeforeMethod
	public void setup() throws Exception {
		super.setup();
		OMEntityDao omEntityDaoReal = new OMEntityDao(enterpriseConnector, Collections.emptyList(), auditLog);
		String serviceName = "serviceName";
		String serverName = "serverName";
		
		RepositoryHandler repositoryHandler = new RepositoryHandler(auditLog, 
				new RepositoryErrorHandler(omrsRepositoryHelper, serviceName, serverName, auditLog),
				metadataCollection,
	            PAGE_SIZE);

		RelationalDataHandler<Database, Schema, Object, Object, Object, Object> relationalDataHandler =
		new RelationalDataHandler<>(
        		new DatabaseConverter(omrsRepositoryHelper, serviceName,serverName),
                Database.class,
        		new SchemaConverter(omrsRepositoryHelper, serviceName,serverName),
                Schema.class,
        		new EmptyConverter(omrsRepositoryHelper, serviceName,serverName),
                Object.class,
        		new EmptyConverter(omrsRepositoryHelper, serviceName,serverName),
                Object.class,
        		new EmptyConverter(omrsRepositoryHelper, serviceName,serverName),
                Object.class,
        		new EmptyConverter(omrsRepositoryHelper, serviceName,serverName),
                Object.class,
                serviceName,
                serverName,
                invalidParameterHandler,
                repositoryHandler,
                omrsRepositoryHelper,
                "localServerUserId",
                null, // securityVerifier,
                null, // supportedZones,
                null, // defaultZones,
                null, // publishZones,
                auditLog);
		
		databaseContextHandler = new DatabaseContextHandler(relationalDataHandler, omEntityDaoReal, invalidParameterHandler);
	}

	@Test
	public void getDatabases() throws AnalyticsModelingCheckedException {
		// setup repository
		createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseEntity(DATABASE_ADVENTURE_WORKS, SERVER_TYPE_MS_SQL, "2.0");

		// test
		List<ResponseContainerDatabase> databases = databaseContextHandler.getDatabases(USER_ID, FROM_INDEX, PAGE_SIZE);
		assertNotNull(databases);
		assertTrue(databases.size() == 2, "Failed retrieve databases.");

		ResponseContainerDatabase gs = databases.stream().filter(ds -> DATABASE_GOSALES.equals(ds.getDbName()))
				.findFirst().orElse(null);
		assertNotNull(gs);
		assertNotNull(gs.getGUID());
		assertEquals(SERVER_TYPE_MS_SQL, gs.getDbType());
		assertEquals("1.0", gs.getDbVersion());
	}

	@Test
	public void getDatabasesPage() throws AnalyticsModelingCheckedException {
		// setup repository with four databases sorted: AdventureWorks, DB_3, DB_4, DB_5, GOSALES
		createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseEntity(DATABASE_ADVENTURE_WORKS, SERVER_TYPE_MS_SQL, "2.0");
		createDatabaseEntity("DB_3", SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseEntity("DB_4", SERVER_TYPE_MS_SQL, "2.0");
		createDatabaseEntity("DB_5", SERVER_TYPE_MS_SQL, "2.0");

		List<ResponseContainerDatabase> page1 = databaseContextHandler.getDatabases(USER_ID, 0, 2);
		assertNotNull(page1);
		assertTrue(page1.size() == 2, "Failed retrieve databases first page 1.");
		List<ResponseContainerDatabase> page2 = databaseContextHandler.getDatabases(USER_ID, 2, 2);
		assertNotNull(page2);
		assertTrue(page2.size() == 2, "Failed retrieve databases internal page 2.");
		List<ResponseContainerDatabase> page3 = databaseContextHandler.getDatabases(USER_ID, 4, 2);
		assertNotNull(page3);
		assertTrue(page3.size() == 1, "Failed retrieve databases last not full page 3.");
		
		page1.addAll(page2);
		page1.addAll(page3);

		assertEquals(String.join(";", page1.stream().map(ResponseContainerDatabase::getDbName).sorted().collect(Collectors.toList())),
				"AdventureWorks;DB_3;DB_4;DB_5;GOSALES",
				"All five databases should be fetched");
	}

	@Test
	public void getDatabasesEmptyRepository() throws AnalyticsModelingCheckedException {
		List<ResponseContainerDatabase> databases = databaseContextHandler.getDatabases(USER_ID, FROM_INDEX, PAGE_SIZE);
		assertTrue(databases.size() == 0, "Database list expected to be empty.");
	}

	@Test
	public void getDatabaseSchemasWithEmptyCatalog()
			throws AnalyticsModelingCheckedException, InvalidParameterException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		// test
		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler
				.getDatabaseSchemas(USER_ID, guidDataSource, FROM_INDEX, PAGE_SIZE);
		assertTrue(schemas.isEmpty(), "Schemas list expected to be empty.");

	}

	@Test
	public void getDatabaseSchemas() throws AnalyticsModelingCheckedException, InvalidParameterException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		createDatabaseSchemaEntity(guidDataSource, SCHEMA_DBO);
		createDatabaseSchemaEntity(guidDataSource, "sys");

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler
				.getDatabaseSchemas(USER_ID, guidDataSource,FROM_INDEX, PAGE_SIZE);
		assertNotNull(schemas);
		assertTrue(schemas.size() == 2, "Failed to retrieve database schemas.");

		ResponseContainerDatabaseSchema schema = schemas.stream().filter(s -> SCHEMA_DBO.equals(s.getSchema()))
				.findFirst().orElse(null);
		assertNotNull(schema);
		assertEquals(DATABASE_GOSALES, schema.getCatalog());
		assertEquals("user", schema.getSchemaType());
	}

	@Test
	public void getDatabaseSchemasPage() throws AnalyticsModelingCheckedException, InvalidParameterException {
		// setup repository with four schemas sorted: dbo, s1, s2, s3, sys
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		createDatabaseSchemaEntity(guidDataSource, SCHEMA_DBO);
		createDatabaseSchemaEntity(guidDataSource, "s1");
		createDatabaseSchemaEntity(guidDataSource, "s2");
		createDatabaseSchemaEntity(guidDataSource, "s3");
		createDatabaseSchemaEntity(guidDataSource, "sys");

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler
				.getDatabaseSchemas(USER_ID, guidDataSource, 0, 2);
		assertNotNull(schemas);
		assertTrue(schemas.size() == 2, "Failed to retrieve database schemas first page.");

		List<ResponseContainerDatabaseSchema> schemas2 = databaseContextHandler
				.getDatabaseSchemas(USER_ID, guidDataSource, 2, 2);
		assertNotNull(schemas2);
		assertTrue(schemas2.size() == 2, "Failed to retrieve database schemas full page.");

		List<ResponseContainerDatabaseSchema> schemas3 = databaseContextHandler
				.getDatabaseSchemas(USER_ID, guidDataSource, 4, 2);
		assertNotNull(schemas3);
		assertTrue(schemas3.size() == 1, "Failed to retrieve database schemas last incomplete page.");

		schemas.addAll(schemas2);
		schemas.addAll(schemas3);
		
		assertEquals(String.join(";", schemas.stream().map(ResponseContainerDatabaseSchema::getSchema).sorted().collect(Collectors.toList())),
				"dbo;s1;s2;s3;sys",
				"All five schemas should be fetched");
	}

	@Test
	public void getDatabaseSchemasSameNameForTwoCatalogs()
			throws AnalyticsModelingCheckedException, InvalidParameterException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);
		entityDB = createDatabaseEntity(DATABASE_ADVENTURE_WORKS, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler
				.getDatabaseSchemas(USER_ID, entityDB.getGUID(),FROM_INDEX, PAGE_SIZE);
		assertNotNull(schemas);
		assertTrue(schemas.size() == 1, "Failed to retrieve database schema.");
		assertEquals(DATABASE_ADVENTURE_WORKS, schemas.get(0).getCatalog());
	}

	@Test
	public void getSchemaTablesForEmptySchema() throws AnalyticsModelingCheckedException, InvalidParameterException {
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
	public void getSchemaTables() throws AnalyticsModelingCheckedException, InvalidParameterException {
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

		EntityDetail entityTableDate = createSchemaTable(entitySchema, TBL_DATE);
		EntityDetail pkDate = addColumn(entityTableDate, "DateKey", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		setColumnPrimaryKey(pkDate, buildKey(PRIMARY_KEY_PREFIX, TBL_DATE, "DateKey"));
		addColumn(entityTableDate, TBL_DATE, "TIMESTAMP", "DATETIME");

		// table "Location" has combined primary key with two fields
		EntityDetail entityTableLocation = createSchemaTable(entitySchema, TBL_LOCATION);
		EntityDetail pkLatitude = addColumn(entityTableLocation, "Latitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnPrimaryKey(pkLatitude, buildKey(PRIMARY_KEY_PREFIX, TBL_LOCATION, "Latitude", "Longitude"));
		EntityDetail pkLongitude = addColumn(entityTableLocation, "Longitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnPrimaryKey(pkLongitude, buildKey(PRIMARY_KEY_PREFIX, TBL_LOCATION, "Latitude", "Longitude"));
		addColumn(entityTableLocation, "Destination", DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING);

		EntityDetail entityTableA = createSchemaTable(entitySchema, "Product");
		EntityDetail pkProduct = addColumn(entityTableA, "ProductId", DATA_TYPE_INTEGER, VENDOR_TYPE_INT32);
		setColumnPrimaryKey(pkProduct, buildKey(PRIMARY_KEY_PREFIX, "Product", "ProductId"));
		addColumn(entityTableA, "ProductName", DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING);
		EntityDetail columnEntity = addColumn(entityTableA, "ProductDescription", DATA_TYPE_VARCHAR,
				VENDOR_TYPE_STRING);

		setColumnProperty(columnEntity, Constants.IS_NULLABLE, Boolean.TRUE); // only test of nullable field

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
				SCHEMA_DBO, null);

		// assert result
		assertNotNull(moduleResponse);
		List<Table> tables = moduleResponse.getPhysicalModule().getDataSource().get(0).getTable();

		assertEquals(tables.size(), 4, "Failed to retrieve tables.");

		String module = TestUtilities.readJsonFile("/src/test/resources/", "getModule");
		TestUtilities.validateGUIDs(moduleResponse);
		TestUtilities.assertObjectJson(moduleResponse, module);
		
		// test table filter include
		ModuleTableFilter filter = new ModuleTableFilter();
		filter.getMeta().setIncludedTables(Arrays.asList(TBL_DATE, TBL_LOCATION));
		moduleResponse = databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES,	SCHEMA_DBO, filter);
		assertNotNull(moduleResponse);
		tables = moduleResponse.getPhysicalModule().getDataSource().get(0).getTable();
		assertEquals(tables.size(), 2, "Two tables should pass the filter.");

		// test table filter exclude
		filter = new ModuleTableFilter();
		filter.getMeta().setExcludedTables(Arrays.asList(TBL_DATE));
		moduleResponse = databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES,	SCHEMA_DBO, filter);
		assertNotNull(moduleResponse);
		tables = moduleResponse.getPhysicalModule().getDataSource().get(0).getTable();
		assertEquals(tables.size(), 3, "Three tables should pass the filter.");
}

	@Test
	public void getModuleFailure_BadDataSourceGuid() {

		String badGuid = "BadGuid";
		AnalyticsModelingCheckedException thrown = expectThrows(AnalyticsModelingCheckedException.class,
				() -> databaseContextHandler.getModule(badGuid, DATABASE_GOSALES, SCHEMA_DBO, null));
		assertEquals(AnalyticsModelingErrorCode.GET_ENTITY_EXCEPTION.getMessageDefinition().getMessageId(),
				thrown.getReportedErrorMessageId(), "Message Id is not correct");

		assertTrue(thrown.getMessage().contains(badGuid), "GUID should be in the message.");
	}

	@Test
	public void getModuleFailure_UnknownSchemaName() {

		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");

		String badSchema = "schemaUnknown";
		AnalyticsModelingCheckedException thrown = expectThrows(AnalyticsModelingCheckedException.class,
				() -> databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES, badSchema, null));

		assertEquals(AnalyticsModelingErrorCode.SCHEMA_UNKNOWN.getMessageDefinition().getMessageId(),
				thrown.getReportedErrorMessageId(), "Message Id is not correct");

		assertTrue(thrown.getMessage().contains(badSchema), "Failed schema name should be in the message.");
	}

	@Test
	public void getDatabaseSchemasInvalidParameter() {

		InvalidParameterException thrown = expectThrows(InvalidParameterException.class,
				() -> databaseContextHandler.getDatabaseSchemas(USER_ID, null, FROM_INDEX, PAGE_SIZE));
		assertEquals(thrown.getParameterName(), DatabaseContextHandler.DATA_SOURCE_GUID, "Incorrect parameter name.");
	}

	@Test
	public void getSchemaTablesInvalidParameter() {

		InvalidParameterException thrown = expectThrows(InvalidParameterException.class,
				() -> databaseContextHandler.getSchemaTables(null, SCHEMA_DBO));
		assertEquals(thrown.getParameterName(), DatabaseContextHandler.DATA_SOURCE_GUID, "Incorrect parameter name.");
	}

	@Test
	public void getModuleInvalidParameter() {

		InvalidParameterException thrown = expectThrows(InvalidParameterException.class,
				() -> databaseContextHandler.getModule(null, DATABASE_GOSALES, SCHEMA_DBO, null));
		assertEquals(thrown.getParameterName(), DatabaseContextHandler.DATA_SOURCE_GUID, "Incorrect parameter name.");
	}

	private String buildKey(String... param) {
		return String.join("_", param);
	}
}
