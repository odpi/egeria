/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.ffdc;

import java.text.MessageFormat;

public enum CognosErrorCode {

	ADD_RELATIONSHIP_EXCEPTION(
			"OMAS-COGNOS-005", 
			"Relationship {0} could not be added. Error: {1}",
			"The system is unable to process the request.", 
			"Verify the topic event."),
	GET_ENTITY_EXCEPTION(
			"OMAS-COGNOS-006", 
			"Entity matching criteria [{0}={1}] could not be fetched. Error: {2}",
			"The system is unable to process the request.", 
			"Verify the topic event."),
	GET_RELATIONSHIP_EXCEPTION(
			"OMAS-COGNOS-007", 
			"Relationship {0} for entity {1} could not be fetched. Error: {2}",
			"The system is unable to process the request.", 
			"Verify the topic event."),
	SERVICE_NOT_INITIALIZED(
			"OMAS-COGNOS-012",
			"The access service has not been initialized for server {0} and can not support REST API calls",
			"The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
			"If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
	BAD_CONFIG(
			"OMAS-COGNOS-013",
			"The Information View Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property.  The resulting exception of {2} included the following message: {3}",
			"The access service has not been passed valid configuration.",
			"Correct the configuration and restart the service."),
	ENTITY_NOT_FOUND_EXCEPTION(404,
			"OMAS-COGNOS-014", 
			"The entity matching criteria [{0}={1}] was not found.",
			"The system is unable to process the request.", 
			"Correct the request payload submitted."),

	INCORRECT_MODEL_EXCEPTION(
			"OMAS-COGNOS-016", 
			"The model for entity {0} is not correct: {1}",
			"The system is unable to process the request.", 
			"Correct the metadata model."),

	DELETE_ENTITY_EXCEPTION(
			"OMAS-COGNOS-026",
			"Entity matching criteria [{0}={1}] could not be deleted. Error: {2}",
			"The system is unable to process the request.",
			"Verify the topic event."),
	NULL_TOPIC_CONNECTOR(400, 
			"OMAS-COGNOS-029",
			"Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
			"The local server will not connect to the cohort.",
			"The connection to the connector is configured in the server configuration.  "
					+ "Review previous error messages to determine the precise error in the "
					+ "start up configuration. "
					+ "Correct the configuration and reconnect the server to the cohort. "),
	SCHEMA_UNKNOWN(403, 
			"OMAS-COGNOS-031", 
			"Requested schema {0} does not exist.",
			"The system is unable to process the request.",
			"Refresh data."),
	UPDATE_PROPERTY_EXCEPTION( 
			"OMAS-COGNOS-032", 
			"Propertyies for entity {0} can not be updated with properties {1}. Error {2}",
			"The system is unable to process the request.",
			"Refresh data."),
	CLASSIFICATION_EXCEPTION(
			"OMAS-COGNOS-033", 
			"Classification for entity {0} failed with {1}. Error {2}",
			"The system is unable to process the request.",
			"Refresh data."),

	UNKNOWN_ERROR(
			"OMAS-COGNOS-500", 
			"Server failed to execute request with unknown reason.",
			"The system is unable to process the request.", 
			"Report the error to administrator.");

	private int httpErrorCode;
	private String errorMessageId;
	private String errorMessage;
	private String systemAction;
	private String userAction;

	CognosErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction,
			String userAction) {
		this.httpErrorCode = httpErrorCode;
		this.errorMessageId = errorMessageId;
		this.errorMessage = errorMessage;
		this.systemAction = systemAction;
		this.userAction = userAction;
	}

	CognosErrorCode(String errorMessageId, String errorMessage, String systemAction, String userAction) {
		this(500, errorMessageId, errorMessage, systemAction, userAction);
	}

	public String getErrorMessageId() {
		return errorMessageId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getSystemAction() {
		return systemAction;
	}

	public String getUserAction() {
		return userAction;
	}

	public int getHttpErrorCode() {
		return httpErrorCode;
	}

	public String getFormattedErrorMessage(String... params) {
		return new MessageFormat(errorMessage).format(params);

	}
}
