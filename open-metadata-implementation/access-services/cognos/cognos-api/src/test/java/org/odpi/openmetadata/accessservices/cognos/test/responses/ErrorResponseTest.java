/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.test.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;
import org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions.CognosRuntimeException;
import org.odpi.openmetadata.accessservices.cognos.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;

public class ErrorResponseTest {
	
	private static final String	MESSAGE = "short message";
	private static final String	CODE = "MSR_OMS_001";
	private static final String	EXCEPTIONCAUSE = "com.ibm.cognos.AppException: Cannot open ...";

	String master = "{\r\n" + 
			"  \"class\" : \"ErrorResponse\",\r\n" + 
			"  \"relatedHTTPCode\" : 500,\r\n" + 
			"  \"msg\" : \"short message\",\r\n" + 
			"  \"code\" : \"MSR_OMS_001\",\r\n" + 
			"  \"exceptionCauseMsg\" : \"com.ibm.cognos.AppException: Cannot open ...\"\r\n" + 
			"}";

	@Test
	public void toJson() {
		
		CognosRuntimeException ex = new CognosRuntimeException(CognosErrorCode.INCORRECT_MODEL_EXCEPTION, (Throwable)null);
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
