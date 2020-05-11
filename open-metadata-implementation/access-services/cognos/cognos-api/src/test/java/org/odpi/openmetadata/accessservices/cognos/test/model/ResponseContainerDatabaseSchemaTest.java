/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;

public class ResponseContainerDatabaseSchemaTest {

	private static final String CATALOG_NAME = "GOSALES";
	private static final String SCHEMA_NAME = "sys";
	

	String master = "{\r\n" + 
			"  \"attributes\" : {\r\n" + 
			"    \"catalog\" : \"GOSALES\",\r\n" + 
			"    \"schema\" : \"sys\",\r\n" + 
			"    \"schemaType\" : \"user\"\r\n" + 
			"  },\r\n" + 
			"  \"id\" : \"GOSALES/sys\",\r\n" + 
			"  \"type\" : \"schema\"\r\n" + 
			"}";
		
	@Test
	public void toJson() {
		ResponseContainerDatabaseSchema ds = new ResponseContainerDatabaseSchema();
		
		ds.setSchema(SCHEMA_NAME);
		ds.setCatalog(CATALOG_NAME);
		ds.setId(ds.buildId());
		
		TestUtilities.assertObjectJson(ds, master);
	}
	
	@Test
	public void fromJson() {
		
		ResponseContainerDatabaseSchema ds = TestUtilities.readObjectJson(master, ResponseContainerDatabaseSchema.class);

		assertEquals(SCHEMA_NAME, ds.getSchema());
		assertEquals(CATALOG_NAME, ds.getCatalog());
		assertEquals("user", ds.getSchemaType());
		assertEquals(CATALOG_NAME + "/" + SCHEMA_NAME, ds.getId());
		assertEquals(ResponseContainerDatabaseSchema.TYPE_SCHEMA, ds.getType());
	}

}
