/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.ArrayList;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.AnalyticsMessage;

/**
 * Response to return error.
 * 
 * Sample of an error:
 *{
 *  "class" : "ErrorResponse",
 *  "relatedHTTPCode" : 500,
 *  "exceptionClassName": "org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException",
 *  "exceptionCausedBy": "org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException",
 *  "actionDescription": "getModule",
 *  "exceptionErrorMessage": "OMAG-COMMON-400-016 An unexpected org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException exception was caught by getModule; error message was OMAS-ANALYTICS-MODELING-003 Relationship AttributeForSchema for entity gen!RDBST@(host)=vottcteds4.canlab.ibm.com::(database)=adventworksDW::(database_schema)=dbo could not be fetched.",
 *  "exceptionErrorMessageId": "OMAG-COMMON-400-016",
 *  "exceptionErrorMessageParameters": [
 *      "org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException",
 *      "getModule",
 *      "OMAS-ANALYTICS-MODELING-003 Relationship AttributeForSchema for entity gen!RDBST@(host)=vottcteds4.canlab.ibm.com::(database)=adventworksDW::(database_schema)=dbo could not be fetched."
 *  ],
 *  "exceptionSystemAction": "The system is unable to process the request.",
 *  "exceptionUserAction": "Review the error message and other diagnostics created at the same time.", 
 *  "errors" : [
 *		{
 *			org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.AnalyticsMessage
 *		},
 *		...
 *	]
 *}
 *
 */
public class ErrorResponse extends AnalyticsModelingOMASAPIResponse {

    private List<AnalyticsMessage> errors;

	
	public ErrorResponse() {
		// required to deserialize
	}
	/**
	 * Constructor initialized from exception thrown.
	 * @param source of the error.
	 */
	public ErrorResponse(AnalyticsModelingCheckedException source) {
		setRelatedHTTPCode(source.getReportedHTTPCode());
		
		AnalyticsMessage error = AnalyticsMessage.createError(Integer.toString(source.getReportedHTTPCode()), 
				source.getReportedErrorMessageId(),	source.getMessage(), source.getErrorCause());
		
		errors = new ArrayList<>();
		errors.add(error);
	}

    /**
     * Get the errors.
     * @return the response errors.
     */
    public List<AnalyticsMessage> getErrors() {
        return errors;
    }

    /**
     * Set response errors.
     * @param errors response errors.
     */
    protected void setErrors(List<AnalyticsMessage> errors) {
        this.errors = errors;
    }


    /**
     * Add error to the response.
     * @param messageId of the error.
     * @param httpStatus of the error.
     * @param message title.
     * @param detail of the error.
     */
	public void addError(String messageId, String httpStatus, String message, String detail) {
		if (errors == null) {
			errors = new ArrayList<>();
		}

		AnalyticsMessage error = new AnalyticsMessage();
		errors.add(error);
		error.setCode(messageId);
		error.setStatus(httpStatus);
		error.setTitle(message);
		error.setDetail(detail);
	}  

}
