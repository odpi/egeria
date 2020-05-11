/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.cognos.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;

public class ResponseContainerSchemaTablesTest {
	
	String master = "{\r\n" + 
			"  \"attributes\" : {\r\n" + 
			"    \"tables\" : [ \"A\", \"C\", \"B\" ]\r\n" + 
			"  },\r\n" + 
			"  \"type\" : \"tables\"\r\n" + 
			"}";

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
