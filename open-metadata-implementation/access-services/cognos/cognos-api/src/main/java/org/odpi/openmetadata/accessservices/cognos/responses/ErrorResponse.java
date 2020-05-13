/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.responses;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions.CognosCheckedException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response to return error.
 * 
 * Sample of an error:
{
	"msg": "MSR_GEN_0097 Error while getting catalogs and schemas...",
	"code": "MSR_GEN_0097",
	"exceptionCauseMsg": "com.ibm.cognos.AppException: Cannot open ..."
}
 * @author YEVGENIYMarchenko
 *
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse extends CognosOMASAPIResponse {

	@JsonProperty("msg") 
	private String message;

	@JsonProperty("code")
	private String errorCode;

	@JsonProperty("exceptionCauseMsg")
	private String exceptionCause;
	
	public ErrorResponse() {
		// required to deserialize
	}
	
	public ErrorResponse(CognosCheckedException source) {
		setRelatedHTTPCode(source.getReportedHTTPCode());
		message = source.getMessage();
		errorCode = source.getReportedErrorMessageId();
		exceptionCause = source.getErrorCause();
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getExceptionCause() {
		return exceptionCause;
	}

	public void setExceptionCause(String exceptionCause) {
		this.exceptionCause = exceptionCause;
	}

}
