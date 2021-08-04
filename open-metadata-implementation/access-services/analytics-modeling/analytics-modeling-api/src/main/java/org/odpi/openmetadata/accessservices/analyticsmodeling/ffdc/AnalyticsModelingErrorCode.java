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
			"The system is unable to create relationship.", 
			"Review the exception cause and retry to create the relationship."),
	GET_ENTITY_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-002", 
			"Entity matching criteria [{0}={1}] could not be fetched.",
			"The system is unable to fetch entity with requested property.", 
			"Review the exception cause and retry to fetch the entity."),
	GET_RELATIONSHIP_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-003", 
			"Relationship {0} for entity {1} could not be fetched.",
			"The system is unable to fetch relationship for entity.", 
			"Review the exception cause and retry to fetch the relationship."),
	SERVICE_NOT_INITIALIZED(
			"OMAS-ANALYTICS-MODELING-004",
			"The access service has not been initialized for server {0} and can not support REST API calls",
			"The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
			"If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
	ENTITY_NOT_FOUND_EXCEPTION(404,
			"OMAS-ANALYTICS-MODELING-006", 
			"The entity with GUID = {1} was not found.",
			"The system is unable to find entity with the requested GUID.", 
			"Review the exception cause and confirm the requested entity exists."),
	INCORRECT_MODEL_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-007", 
			"The model for entity {0} is not correct: {1}",
			"The system is unable to process the model.", 
			"Correct the metadata model."),
	SCHEMA_UNKNOWN(403, 
			"OMAS-ANALYTICS-MODELING-010", 
			"Requested schema {0} does not exist.",
			"The system is unable to find requested schema.",
			"Review the exception cause and confirm the schema exists."),
	UPDATE_PROPERTY_EXCEPTION( 
			"OMAS-ANALYTICS-MODELING-011", 
			"Propertyies for entity {0} can not be updated with values {1}.",
			"The system is unable to update properties.",
			"Review the exception cause to fix problem and retry to update properties."),
	CLASSIFICATION_EXCEPTION(
			"OMAS-ANALYTICS-MODELING-012", 
			"Classification for entity {0} failed set classification {1}.",
			"The system is unable to process the request.",
			"Refresh data."),
    ERROR_INITIALIZING_ANALYTICS_MODELING_TOPIC_CONNECTION(400,
    		"OMAS-ANALYTICS-MODELING-0013",
            "Unable to initialize the connection to topic {0} in the Analytics Modeling Open Metadata Access Service (OMAS) instance for server {1} ",
            "The connection to Analytics Modeling topic could not be initialized.",
            "Review the exception and resolve the connection topic problem."),
    SERVICE_INSTANCE_FAILURE(
			"OMAS-ANALYTICS-MODELING-014",
			"The access service can't find instance for server {0}, user {1}, operation {2}.",
			"The system is unable to complete operation on the server.",
            "Review the exception and resolve the server configuration. "),
    INVALID_REQUEST_PARAMER(400,
			"OMAS-ANALYTICS-MODELING-015",
			"The request parameter {0} has invalid value.",
			"The system is unable to process the request due to invalid parameter.",
            "Verify parameter value."),
	FAILED_FETCH_DATABASES(
			"OMAS-ANALYTICS-MODELING-016", 
			"Databases could not be fetched.",
			"The system failed to find databases.", 
			"Review the exception to resolve the issue."),
	FAILED_FETCH_DATABASE_SCHEMAS(
			"OMAS-ANALYTICS-MODELING-017", 
			"Schemas for database {0} could not be fetched.",
			"The system is unable to fetch schemas for database.", 
			"Review the exception to resolve the issue and validate database GUID."),
	UNEXPECTED_CLASS(
			"OMAS-ANALYTICS-MODELING-020", 
			"Unexpected class {0} of the AnalyticsMetadata for entity {1}.",
			"The system is unable to create bean for the unexpected class.", 
			"Fix data in repository. Provide correct class for the entity."),
	FAILED_CREATE_BEAN(
			"OMAS-ANALYTICS-MODELING-021", 
			"Failed to create bean of class {0} of the AnalyticsMetadata.",
			"The system is unable to create bean for the requested class.", 
			"Fix data in repository or class path."),
	MISSING_BEAN_CLASS(
			"OMAS-ANALYTICS-MODELING-022", 
			"Java class of the AnalyticsMetadata is not defined for entity {0}.",
			"The system is unable to create bean without defined class.", 
			"Fix data in repository. Define class for entity."),
	MISSING_BEAN_PROPERTIES(
			"OMAS-ANALYTICS-MODELING-023", 
			"Entity {0} of the AnalyticsMetadata is missing properties.",
			"The system is unable process entity without properties.", 
			"Fix data in repository. Define entity properties."),
	FAILED_CREATE_ARTIFACT(
			"OMAS-ANALYTICS-MODELING-025", 
			"Failed to create analytics artifact.",
			"The system is unable to create assets for analytics artifact.",
			"Review the exception to resolve the issue. Verify artifact definition."),
	FAILED_CREATE_SERVER_CAPABILITY(
			"OMAS-ANALYTICS-MODELING-026", 
			"User {0} failed to create server capability {1}. Cause: {2}",
			"The system is unable to create server capability.",
			"Review the exception to resolve the issue with the server capability."),
	FAILED_UPDATE_ARTIFACT(
			"OMAS-ANALYTICS-MODELING-027", 
			"User {0} failed to update artifact {1}. Cause: {2}",
			"The system is unable update assets.",
			"Review the exception to resolve the issue and repeat request to fix assets for the artifact."),
	FAILED_DELETE_ARTIFACT(
			"OMAS-ANALYTICS-MODELING-028", 
			"User {0} failed to delete analytics artifact {1}. Cause: {2}",
			"The system is unable to delete asset for the artifact.",
			"Review the exception to resolve the issue and repeat request to remove assets for the artifact."),
    FAIL_REST_CALL(
			"OMAS-ANALYTICS-MODELING-029", 
			"Rest call {0} failed. Cause: {1}",
			"The client is unable to make the REST request.",
			"Review the exception to resolve the issue and repeat request."),
    UNAUTHORIZED_USER (
			"OMAS-ANALYTICS-MODELING-030", 
			"User {0} is not authorized to perform operation: {1}",
			"The system is unable to process the unauthorized request.",
			"Review the user permissions to resolve the issue and repeat request."),
	FAILED_UPDATE_UNKNOWN_ARTIFACT(
			"OMAS-ANALYTICS-MODELING-031", 
			"User {0} failed to update artifact {1}. The artifact is unknown.",
			"The system is unable to update unknown artifact.",
			"Confirm the artifact exists."),
	
	
    UNEXPECTED_EXCEPTION(400,
    		"OMAS-ANALYTICS-MODELING-500",
            "An unexpected {0} exception was caught by {1}; error message was {2}",
            "The system is unable to process the request and has returned an exception to the caller.",
            "Review the error message. Also look up its full message definition which includes the system action " +
                    "and user action. This is most likely to describe the correct action to take to resolve the error. " +
                    "If that does not help, look for other diagnostics created at the same time. Also validate that the " +
                    "caller is a valid client of this server and is operating correctly.");
	
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
