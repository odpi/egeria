/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ResponseContainerDatabaseSchemaTest {

	private static final String CATALOG_NAME = "GOSALES";
	private static final String SCHEMA_NAME = "sys";
	

	String master = String.format("{%n" + 
			"  \"attributes\" : {%n" + 
			"    \"catalog\" : \"GOSALES\",%n" + 
			"    \"schema\" : \"sys\",%n" + 
			"    \"schemaType\" : \"user\"%n" + 
			"  },%n" + 
			"  \"id\" : \"GOSALES/sys\",%n" + 
			"  \"type\" : \"schema\"%n" + 
			"}");
		
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
