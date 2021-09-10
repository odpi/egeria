/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.test.responses;

import org.testng.annotations.Test;

import java.io.IOException;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.MetadataModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Messages;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ModuleResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ModuleReponseTest {
  @Test
  public void test() throws IOException {
	  
	ModuleResponse obj = new ModuleResponse();
	ResponseContainerModule module = new ResponseContainerModule();
	module.setPhysicalModule(new MetadataModule());
	
	obj.setModule(module);
	Messages message = new Messages();
	message.addWarning(AnalyticsModelingErrorCode.WARNING_CREATE_METADATA_LINK.getMessageDefinition("I1", "I2"));
	obj.setMeta(message);

	String response = TestUtilities.readJsonFile("/src/test/resources/", "ModuleReponse");
	TestUtilities.assertObjectJson(obj, response);
  }
  
  
}
