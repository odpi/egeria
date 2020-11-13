/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ResponseContainerSchemaTablesTest {
	
	String master = String.format("{%n" + 
			"  \"attributes\" : {%n" + 
			"    \"tables\" : [ \"A\", \"C\", \"B\" ]%n" + 
			"  },%n" + 
			"  \"type\" : \"tables\"%n" + 
			"}");

	List<String> tables = Arrays.asList("A", "C", "B");
	
	@Test
	public void toJson() {
		ResponseContainerSchemaTables st = new ResponseContainerSchemaTables();
		
		st.setTablesList(tables);
		
		TestUtilities.assertObjectJson(st, master);
	}
	
	@Test
	public void fromJson() {
		
		ResponseContainerSchemaTables st = TestUtilities.readObjectJson(master, ResponseContainerSchemaTables.class);

		assertNull(st.getId());
		assertEquals(ResponseContainerSchemaTables.TYPE_TABLES, st.getType());
		assertEquals(tables, st.getTablesList());
		
	}

}
