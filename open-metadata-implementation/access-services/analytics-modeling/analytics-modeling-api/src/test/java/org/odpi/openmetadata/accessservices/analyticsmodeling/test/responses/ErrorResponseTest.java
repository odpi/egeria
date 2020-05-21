/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.responses;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ErrorResponseTest {
	
	private static final String	MESSAGE = "short message";
	private static final String	CODE = "MSR_OMS_001";
	private static final String	EXCEPTIONCAUSE = "com.ibm.AppException: Cannot open ...";

	String master = String.format("{%n" + 
			"  \"class\" : \"ErrorResponse\",%n" + 
			"  \"relatedHTTPCode\" : 500,%n" + 
			"  \"msg\" : \"short message\",%n" + 
			"  \"code\" : \"MSR_OMS_001\",%n" + 
			"  \"exceptionCauseMsg\" : \"com.ibm.AppException: Cannot open ...\"%n" + 
			"}");

	@Test
	public void toJson() {
		
		AnalyticsModelingCheckedException ex = new AnalyticsModelingCheckedException(
				AnalyticsModelingErrorCode.INCORRECT_MODEL_EXCEPTION.getMessageDefinition("guidEntity", "error description"),
				"ErrorResponse",
				"");
		
		ErrorResponse er = new ErrorResponse(ex);
		
		er.setMessage(MESSAGE);
		er.setErrorCode(CODE);
		er.setExceptionCause(EXCEPTIONCAUSE);
		
		TestUtilities.assertObjectJson(er, master);
	}
	
	@Test
	public void fromJson() {
		
		ErrorResponse er = TestUtilities.readObjectJson(master, ErrorResponse.class);

		assertEquals(MESSAGE, er.getMessage());
		assertEquals(CODE, er.getErrorCode());
		assertEquals(EXCEPTIONCAUSE, er.getExceptionCause());

	}


}
