/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.cognos.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.cognos.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;
import org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions.CognosCheckedException;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.cognos.model.module.Table;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.cognos.utils.Constants;
import org.odpi.openmetadata.accessservices.cognos.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;


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
	
	
	private DatabaseContextHandler databaseContextHandler;

	@Before
	public void setup() throws Exception {
		super.setup();
		OMEntityDao omEntityDaoReal = new OMEntityDao(enterpriseConnector, Collections.emptyList(), auditLog);
		databaseContextHandler = new DatabaseContextHandler(omEntityDaoReal);
	}

	@Test
	public void getDatabases() throws CognosCheckedException {
		// setup repository
		createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseEntity(DATABASE_ADWENTURE_WORKS, SERVER_TYPE_MS_SQL, "2.0");

		// test
		List<ResponseContainerDatabase> databases = databaseContextHandler.getDatabases();
		assertNotNull(databases);
		assertTrue("Failed retrieve databases.", databases.size() == 2);

		ResponseContainerDatabase gs = databases.stream()
				.filter(ds->DATABASE_GOSALES.equals(ds.getDbName()))
				.findFirst().orElse(null);
		assertNotNull(gs);
		assertEquals(SERVER_TYPE_MS_SQL, gs.getDbType());
		assertEquals("1.0", gs.getDbVersion());
	}
	
	@Test
	public void getDatabasesEmptyRepository() throws CognosCheckedException {
		List<ResponseContainerDatabase> databases = databaseContextHandler.getDatabases();
		assertTrue( "Database list expected to be empty.", databases.size() == 0 );
	}

	@Test
	public void getDatabaseSchemasWithEmptyCatalog() throws CognosCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		// test
		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler.getDatabaseSchemas(guidDataSource);
		assertTrue("Schemas list expected to be empty.", schemas.isEmpty() );

	}
	
	@Test
	public void getDatabaseSchemas() throws CognosCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String guidDataSource = entityDB.getGUID();
		createDatabaseSchemaEntity(guidDataSource, SCHEMA_DBO);
		createDatabaseSchemaEntity(guidDataSource, "sys");

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler.getDatabaseSchemas(guidDataSource);
		assertNotNull(schemas);
		assertTrue( "Failed to retrieve database schemas.", schemas.size() == 2 );
		
		ResponseContainerDatabaseSchema schema = schemas.stream()
				.filter(s->SCHEMA_DBO.equals(s.getSchema()))
				.findFirst().orElse(null);
		assertNotNull(schema);
		assertEquals(DATABASE_GOSALES, schema.getCatalog());
		assertEquals("user", schema.getSchemaType());
	}
	
	@Test
	public void getDatabaseSchemasSameNameForTwoCatalogs() throws CognosCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);
		entityDB = createDatabaseEntity("AdventureWorks", SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);

		List<ResponseContainerDatabaseSchema> schemas = databaseContextHandler.getDatabaseSchemas(entityDB.getGUID());
		assertNotNull(schemas);
		assertTrue( "Failed to retrieve database schema.", schemas.size() == 1 );
		assertEquals("AdventureWorks", schemas.get(0).getCatalog());
	}
	
	
	@Test
	public void getSchemaTablesForEmptySchema() throws CognosCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);

		ResponseContainerSchemaTables tables = databaseContextHandler.getSchemaTables(entityDB.getGUID(), SCHEMA_DBO);
		assertTrue("Table list expected to be empty.", tables.getTablesList().isEmpty() );
	}
	
	@Test
	public void getSchemaTablesForUnknownSchema() {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		String schemaName = "NonExistingSchemaName";

		CognosCheckedException thrown = assertThrows(
		        "Request for tables of unknown schema fails with exception.",
				CognosCheckedException.class,
		        () -> databaseContextHandler.getSchemaTables(entityDB.getGUID(), schemaName)
		    );

		    assertEquals(CognosErrorCode.SCHEMA_UNKNOWN.getMessageDefinition().getMessageId(),
		    		thrown.getReportedErrorMessageId(),
		    		"Message Id is not correct");
		    
			assertTrue("Failed schema name should be in the message.", thrown.getMessage().contains(schemaName));
	}
	
	@Test
	public void getSchemaTables() throws CognosCheckedException {
		// setup repository
		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
		EntityDetail entitySchema = createDatabaseSchemaEntity(entityDB.getGUID(), SCHEMA_DBO);
		createSchemaTable(entitySchema, "A");
		createSchemaTable(entitySchema, "B");

		ResponseContainerSchemaTables tableResponse = databaseContextHandler.getSchemaTables(entityDB.getGUID(), SCHEMA_DBO);
		assertNotNull(tableResponse);
		List<String> tables = tableResponse.getTablesList();
		
		assertTrue( "Failed to retrieve tables.", tables.size() == 2 );
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
		EntityDetail columnEntity = addColumn(entityTableA, "ProductDescription", DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING);
		setColumnProperty(columnEntity, Constants.IS_NULLABLE, 
				EntityPropertiesUtils.createPrimitiveBooleanPropertyValue(Boolean.TRUE));	// only test of nullable field
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
		setColumnNoteLogProperty(columnEntity, Constants.LENGTH,
				EntityPropertiesUtils.createPrimitiveStringPropertyValue("19"));
		
		columnEntity = addColumn(entityTableSales, "Discount", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		setColumnNoteLogProperty(columnEntity, Constants.LENGTH,
				EntityPropertiesUtils.createPrimitiveStringPropertyValue("19"));
		
		columnEntity = addColumn(entityTableSales, "ShipmentDestinationLatitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		defineColumnForeignKey(columnEntity, pkLatitude);
		columnEntity = addColumn(entityTableSales, "ShipmentDestinationLongitude", DATA_TYPE_DECIMAL, DATA_TYPE_DECIMAL);
		defineColumnForeignKey(columnEntity, pkLongitude);

		// testing is done here
		ResponseContainerModule moduleResponse = databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES, SCHEMA_DBO);

		// assert result
		assertNotNull(moduleResponse);
		List<Table> tables = moduleResponse.getPhysicalModule().getDataSource().get(0).getTable();
		
		assertEquals( "Failed to retrieve tables.", 4, tables.size() );
		
		String module = TestUtilities.readJsonFile("/src/test/resources/", "getModule");
		TestUtilities.assertObjectJson(moduleResponse, module);
    }

	@Test
	public void getModuleFailure_BadDataSourceGuid() {

		String badGuid = "BadGuid";
		CognosCheckedException thrown = assertThrows(
		        "Illegal parameter GUID fails with exception.",
				CognosCheckedException.class,
		        () -> databaseContextHandler.getModule(badGuid, DATABASE_GOSALES, SCHEMA_DBO)
		    );
		    assertEquals("Message Id is not correct",
		    		CognosErrorCode.GET_ENTITY_EXCEPTION.getMessageDefinition().getMessageId(),
		    		thrown.getReportedErrorMessageId() );
		    
			assertTrue("GUID should be in the message.", thrown.getMessage().contains(badGuid));
	}
	
	@Test
	public void getModuleFailure_UnknownSchemaName() {

		EntityDetail entityDB = createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");

		String badSchema = "schemaUnknown";
		CognosCheckedException thrown = assertThrows(
		        "Request for missing schema name fails with exception.",
				CognosCheckedException.class,
		        () -> databaseContextHandler.getModule(entityDB.getGUID(), DATABASE_GOSALES, badSchema)
		    );
		
	    assertEquals("Message Id is not correct",
	    		CognosErrorCode.SCHEMA_UNKNOWN.getMessageDefinition().getMessageId(),
	    		thrown.getReportedErrorMessageId()
	    		);
	    
		assertTrue("Failed schema name should be in the message.", thrown.getMessage().contains(badSchema) );
	}
	
	private String buildKey(String ... param ) {
		return String.join("_", param);
	}
}
