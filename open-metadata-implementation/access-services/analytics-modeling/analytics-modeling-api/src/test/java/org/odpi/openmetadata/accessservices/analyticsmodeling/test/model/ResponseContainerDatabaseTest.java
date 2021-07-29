/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ResponseContainerDatabaseTest {

	private static final String GUID = "database@1234";
	private static final String DB_NAME = "GOSALES";
	private static final String TYPE = "Microsoft SQL Server";
	private static final String VERSION = "12.00.6108";
	

	String master = String.format("{%n" + 
			"  \"id\" : \"%s\",%n" + 
			"  \"type\" : \"database\",%n" + 
			"  \"attributes\" : {%n" + 
			"    \"guid\" : \"%s\",%n" + 
			"    \"name\" : \"%s\",%n" + 
			"    \"type\" : \"%s\",%n" + 
			"    \"version\" : \"%s\"%n" + 
			"  }%n" + 
			"}", DB_NAME, GUID, DB_NAME, TYPE, VERSION);
		
	@Test
	public void toJson() {
		ResponseContainerDatabase ds = new ResponseContainerDatabase();
		
		ds.setId(DB_NAME);
		ds.setGUID(GUID);
		ds.setDbName(DB_NAME);
		ds.setDbType(TYPE);
		ds.setDbVersion(VERSION);
		
		TestUtilities.assertObjectJson(ds, master);
	}
	
	@Test
	public void fromJson() {
		
		ResponseContainerDatabase ds = TestUtilities.readObjectJson(master, ResponseContainerDatabase.class);

		assertEquals(GUID, ds.getGUID());
		assertEquals(DB_NAME, ds.getDbName());
		assertEquals(TYPE, ds.getDbType());
		assertEquals(VERSION, ds.getDbVersion());
	}

}
