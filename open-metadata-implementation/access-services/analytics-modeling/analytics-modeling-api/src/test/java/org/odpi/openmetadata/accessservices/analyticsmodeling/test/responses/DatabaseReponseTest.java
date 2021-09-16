/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.test.responses;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Messages;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.DatabasesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class DatabaseReponseTest {
  @Test
  public void test() throws IOException {
	  
	DatabasesResponse obj = new DatabasesResponse();
	List<ResponseContainerDatabase> databases = Arrays.asList(
		  TestUtilities.readObjectJson(JsonMocks.getResponseDatabase("guid-1", "ProductDB", "DB2", "10.7"), ResponseContainerDatabase.class),
		  TestUtilities.readObjectJson(JsonMocks.getResponseDatabase("guid-2", "InventoryDB", "OR", "11.0"), ResponseContainerDatabase.class));
	obj.setDatabasesList(databases);
	Messages message = new Messages();
	message.addWarning(AnalyticsModelingErrorCode.WARNING_CREATE_METADATA_LINK.getMessageDefinition("I1", "I2"));
	obj.setMeta(message);

	String response = TestUtilities.readJsonFile("/src/test/resources/", "DatabaseReponse");
	TestUtilities.assertObjectJson(obj, response);
  }
  
  
}
