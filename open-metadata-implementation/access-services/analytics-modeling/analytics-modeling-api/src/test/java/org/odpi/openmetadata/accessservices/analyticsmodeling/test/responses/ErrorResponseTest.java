/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.responses;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ErrorResponseTest {
	

	String master = String.format("{%n" + 
			"  \"class\" : \"ErrorResponse\",%n" + 
			"  \"relatedHTTPCode\" : 500,%n" + 
			"  \"errors\" : [ {%n" + 
			"    \"status\" : \"500\",%n" + 
			"    \"code\" : \"OMAS-ANALYTICS-MODELING-007\",%n" + 
			"    \"title\" : \"OMAS-ANALYTICS-MODELING-007 The model for entity guidEntity is not correct: error description\",%n" + 
			"    \"meta\" : {%n" + 
			"      \"severity\" : \"error\"%n" + 
			"    }%n" + 
			"  } ]%n" + 
			"}");

	@Test
	public void toJson() {
		
		AnalyticsModelingCheckedException ex = new AnalyticsModelingCheckedException(
				AnalyticsModelingErrorCode.INCORRECT_MODEL_EXCEPTION.getMessageDefinition("guidEntity", "error description"),
				"ErrorResponse",
				"");
		
		ErrorResponse er = new ErrorResponse(ex);
		
		
		TestUtilities.assertObjectJson(er, master);
	}
	
	@Test
	public void fromJson() {
		
		ErrorResponse er = TestUtilities.readObjectJson(master, ErrorResponse.class);


	}


}
