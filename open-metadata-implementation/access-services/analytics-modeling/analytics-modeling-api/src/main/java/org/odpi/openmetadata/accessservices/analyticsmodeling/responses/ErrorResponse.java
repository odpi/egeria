/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response to return error.
 * 
 * Sample of an error:
 *{
 *	"msg": "MSR_GEN_0097 Error while getting catalogs and schemas...",
 *	"code": "MSR_GEN_0097",
 *	"exceptionCauseMsg": "com.ibm.AppException: Cannot open ..."
 *}
 *
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse extends AnalyticsModelingOMASAPIResponse {

	@JsonProperty("msg") 
	private String message;

	@JsonProperty("code")
	private String errorCode;

	@JsonProperty("exceptionCauseMsg")
	private String exceptionCause;
	
	public ErrorResponse() {
		// required to deserialize
	}
	/**
	 * Constructor initialized from exception thrown.
	 * @param source of the error.
	 */
	public ErrorResponse(AnalyticsModelingCheckedException source) {
		setRelatedHTTPCode(source.getReportedHTTPCode());
		message = source.getMessage();
		errorCode = source.getReportedErrorMessageId();
		exceptionCause = source.getErrorCause();
	}
	/**
	 * Get error message 
	 * @return error message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set error message.
	 * @param message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get error code.
	 * @return error code.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Set error code.
	 * @param errorCode to set.
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Get message of original cause - details.
	 * @return message of original cause.
	 */
	public String getExceptionCause() {
		return exceptionCause;
	}

	/**
	 * Set details of the error.
	 * @param exceptionCause description of details.
	 */
	public void setExceptionCause(String exceptionCause) {
		this.exceptionCause = exceptionCause;
	}

}
