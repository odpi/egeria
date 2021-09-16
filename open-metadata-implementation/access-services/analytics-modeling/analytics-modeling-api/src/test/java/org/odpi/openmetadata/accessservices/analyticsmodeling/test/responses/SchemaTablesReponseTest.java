/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.test.responses;

import org.testng.annotations.Test;

import java.io.IOException;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Messages;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemaTablesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class SchemaTablesReponseTest {
  @Test
  public void test() throws IOException {
	  
	SchemaTablesResponse obj = new SchemaTablesResponse();
	ResponseContainerSchemaTables tables = TestUtilities.readObjectJson(
			JsonMocks.getResponseSchemaTables("Country", "Product", "Sales"), ResponseContainerSchemaTables.class);
	
	obj.setTableList(tables);
	Messages message = new Messages();
	message.addWarning(AnalyticsModelingErrorCode.WARNING_CREATE_METADATA_LINK.getMessageDefinition("I1", "I2"));
	message.addWarning(AnalyticsModelingErrorCode.WARNING_CREATE_METADATA_LINK.getMessageDefinition("I1", "I2"),
			"Message of the exception caused the warning.");
	obj.setMeta(message);

	String response = TestUtilities.readJsonFile("/src/test/resources/", "SchemaTablesReponse");
	TestUtilities.assertObjectJson(obj, response);
  }
  
  
}
