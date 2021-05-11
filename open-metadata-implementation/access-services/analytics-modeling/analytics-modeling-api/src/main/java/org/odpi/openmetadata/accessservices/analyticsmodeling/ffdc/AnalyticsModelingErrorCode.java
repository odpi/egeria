/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The AnalyticsModelingErrorCode is definition of the first failure data capture (FFDC) for errors that occur when working with
 * the Analytics Modeling OMAS Services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The fields in the enum are defined in {@link org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition}.
 * The object is used to build exception augmented with parameters to identify related context:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Id - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a AssetConsumerInterface should correct the error</li>
 * </ul>
 */

public enum AnalyticsModelingErrorCode implements ExceptionMessageSet {

	ADD_RELATIONSHIP_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-001", 
			"Relationship {0} could not be added.",
			"The system is unable to process the request.", 
			"Verify the topic event."),
	GET_ENTITY_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-002", 
			"Entity matching criteria [{0}={1}] could not be fetched.",
			"The system is unable to process the request.", 
			"Verify the topic event."),
	GET_RELATIONSHIP_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-003", 
			"Relationship {0} for entity {1} could not be fetched.",
			"The system is unable to process the request.", 
			"Verify the topic event."),
	SERVICE_NOT_INITIALIZED(
			"OMAS-ANALYTICS-MODELING-004",
			"The access service has not been initialized for server {0} and can not support REST API calls",
			"The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
			"If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
	BAD_CONFIG(
			"OMAS-ANALYTICS-MODELING-005",
			"The Information View Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property.  The resulting exception of {2} included the following message: {3}",
			"The access service has not been passed valid configuration.",
			"Correct the configuration and restart the service."),
	ENTITY_NOT_FOUND_EXCEPTION(404,
			"OMAS-ANALYTICS-MODELING-006", 
			"The entity matching criteria [{0}={1}] was not found.",
			"The system is unable to process the request.", 
			"Correct the request payload submitted."),

	INCORRECT_MODEL_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-007", 
			"The model for entity {0} is not correct: {1}",
			"The system is unable to process the request.", 
			"Correct the metadata model."),

	DELETE_ENTITY_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-008",
			"Entity matching criteria [{0}={1}] could not be deleted.",
			"The system is unable to process the request.",
			"Verify the topic event."),
	NULL_TOPIC_CONNECTOR(400, 
			"OMAS-ANALYTICS-MODELING-009",
			"Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
			"The local server will not connect to the cohort.",
			"The connection to the connector is configured in the server configuration.  "
					+ "Review previous error messages to determine the precise error in the "
					+ "start up configuration. "
					+ "Correct the configuration and reconnect the server to the cohort. "),
	SCHEMA_UNKNOWN(403, 
			"OMAS-ANALYTICS-MODELING-010", 
			"Requested schema {0} does not exist.",
			"The system is unable to process the request.",
			"Refresh data."),
	UPDATE_PROPERTY_EXCEPTION( 
			"OMAS-ANALYTICS-MODELING-011", 
			"Propertyies for entity {0} can not be updated with properties {1}.",
			"The system is unable to process the request.",
			"Refresh data."),
	CLASSIFICATION_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-012", 
			"Classification for entity {0} failed set classification {1}.",
			"The system is unable to process the request.",
			"Refresh data."),
    ERROR_INITIALIZING_ANALYTICS_MODELING_TOPIC_CONNECTION(400,
    		"OMAS-ANALYTICS-MODELING-0013",
            "Unable to initialize the connection to topic {0} in the Analytics Modeling Open Metadata Access Service (OMAS) instance for server {1} ",
            "The connection to Analytics Modeling topic could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SERVICE_INSTANCE_FAILURE(
			"OMAS-ANALYTICS-MODELING-014",
			"The access service can't find instance for server {0}, user {1}, operation {2}.",
			"The system is unable to process the request.",
            "Review the exception and resolve the configuration. "),
    INVALID_REQUEST_PARAMER(400,
			"OMAS-ANALYTICS-MODELING-015",
			"The request parameter {0} has invalid value.",
			"The system is unable to process the request.",
            "Verify parameter value."),
	FAILED_FETCH_DATABASES(
			"OMAS-ANALYTICS-MODELING-016", 
			"Databases could not be fetched.",
			"The system is unable to process the request.", 
			"Review the exception to resolve the issue."),
	FAILED_FETCH_DATABASE_SCHEMAS(
			"OMAS-ANALYTICS-MODELING-017", 
			"Schemas for database {0} could not be fetched.",
			"The system is unable to process the request.", 
			"Review the exception to resolve the issue."),
	FAILED_FIND_DATABASE_SCHEMA(
			"OMAS-ANALYTICS-MODELING-018", 
			"Schema {0} for database {1} could not be found.",
			"The system is unable to process the request.", 
			"Review the exception to resolve the issue."),
	FAILED_FETCH_SCHEMAS_TABLES(
			"OMAS-ANALYTICS-MODELING-019", 
			"Tables for schemas {0} of the database {1} could not be fetched.",
			"The system is unable to process the request.", 
			"Review the exception to resolve the issue."),
	UNEXPECTED_CLASS(
			"OMAS-ANALYTICS-MODELING-020", 
			"Unexpected class {0} of the AnalyticsMetadata.",
			"The system is unable to process the request.", 
			"Fix data in repository."),
	FAILED_CREATE_BEAN(
			"OMAS-ANALYTICS-MODELING-021", 
			"Failed to create bean of class {0} of the AnalyticsMetadata.",
			"The system is unable to process the request.", 
			"Fix data in repository or class path."),
	MISSING_BEAN_CLASS(
			"OMAS-ANALYTICS-MODELING-022", 
			"Java class of the AnalyticsMetadata is not defined.",
			"The system is unable to process the request.", 
			"Fix data in repository."),
	MISSING_BEAN_PROPERTIES(
			"OMAS-ANALYTICS-MODELING-023", 
			"Entity {0} of the AnalyticsMetadata is missing properties.",
			"The system is unable to process the request.", 
			"Fix data in repository."),
	INCORRECT_ARTIFACT_DEFINITION(
			"OMAS-ANALYTICS-MODELING-024", 
			"Definition of Analytics artifact cannot be parsed: {0}.",
			"The system is unable to process the request.",
			"Fix the definition."),
	FAILED_CREATE_ARTIFACT(
			"OMAS-ANALYTICS-MODELING-025", 
			"Failed to create analytics artifact.",
			"The system is unable to process the request.",
			"Review the exception to resolve the issue."),
	FAILED_CREATE_SERVER_CAPABILITY(
			"OMAS-ANALYTICS-MODELING-026", 
			"User {0} failed to create server capability {1}. Cause: {2}",
			"The system is unable to process the request.",
			"Review the exception to resolve the issue."),
	FAILED_UPDATE_ARTIFACT(
			"OMAS-ANALYTICS-MODELING-027", 
			"User {0} failed to update artifact {1}. Cause: {2}",
			"The system is unable to process the request.",
			"Review the exception to resolve the issue and repeat request to fix artifact."),
	
    UNEXPECTED_EXCEPTION(400, "OMAG-ANALYTICS-MODELING-499",
            "An unexpected {0} exception was caught by {1}; error message was {2}",
            "The system is unable to process the request and has returned an exception to the caller.",
            "Review the error message.  Also look up its full message definition which includes the system action " +
                    "and user action.  This is most likely to describe the correct action to take to resolve the error.  " +
                    "If that does not help, look for other diagnostics created at the same time.  Also validate that the " +
                    "caller is a valid client of this server and is operating correctly."),
	UNKNOWN_ERROR(
			"OMAS-ANALYTICS-MODELING-500", 
			"Server failed to execute request with unknown reason.",
			"The system is unable to process the request.", 
			"Report the error to administrator.");
	
	private static final long    serialVersionUID = 1L;

	private ExceptionMessageDefinition messageDefinition;


	/**
	 * Constructor with all fields initialized from passed parameters. 
	 * @param httpErrorCode suggested for response.
	 * @param errorMessageId to uniquely identify the message. 
	 * @param errorMessage placeholder to allow additional values to be captured from context.
	 * @param systemAction describes the result of the error
	 * @param userAction describes how a consumer should correct the error.
	 */
	AnalyticsModelingErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction,
			String userAction) {
		this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
				errorMessageId,
				errorMessage,
				systemAction,
				userAction);
	}

	/**
	 * Constructor for HTTP response status 500.
	 * @param errorMessageId to uniquely identify the message. 
	 * @param errorMessage placeholder to allow additional values to be captured from context.
	 * @param systemAction describes the result of the error
	 * @param userAction describes how a consumer should correct the error.
	 */
	AnalyticsModelingErrorCode(String errorMessageId, String errorMessage, String systemAction, String userAction) {
		this(500, errorMessageId, errorMessage, systemAction, userAction);
	}

	/**
	 * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
	 *
	 * @return message definition object.
	 */
	@Override
	public ExceptionMessageDefinition getMessageDefinition()
	{
		return messageDefinition;
	}


	/**
	 * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
	 *
	 * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
	 * @return message definition object.
	 */
	@Override
	public ExceptionMessageDefinition getMessageDefinition(String... params)
	{
		messageDefinition.setMessageParameters(params);

		return messageDefinition;
	}
}
