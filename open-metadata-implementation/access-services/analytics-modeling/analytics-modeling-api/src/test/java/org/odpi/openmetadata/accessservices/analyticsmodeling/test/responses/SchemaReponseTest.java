/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.test.responses;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Messages;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemasResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class SchemaReponseTest {
  @Test
  public void test() throws IOException {
	  
	SchemasResponse obj = new SchemasResponse();
	List<ResponseContainerDatabaseSchema> databaseSchemas = Arrays.asList(
		  TestUtilities.readObjectJson(JsonMocks.getResponseSchema("dbo", "Product", "user"), ResponseContainerDatabaseSchema.class),
		  TestUtilities.readObjectJson(JsonMocks.getResponseSchema("dbo", "master", "system"), ResponseContainerDatabaseSchema.class));
	obj.setSchemaList(databaseSchemas);
	Messages message = new Messages();
	message.addWarning(AnalyticsModelingErrorCode.WARNING_CREATE_METADATA_LINK.getMessageDefinition("I1", "I2"));
	message.addWarning(AnalyticsModelingErrorCode.WARNING_CREATE_METADATA_LINK.getMessageDefinition("I1", "I2"),
			"Message of the exception caused the warning.");
	obj.setMeta(message);

	String response = TestUtilities.readJsonFile("/src/test/resources/", "SchemasReponse");
	TestUtilities.assertObjectJson(obj, response);
  }
  
  
}
