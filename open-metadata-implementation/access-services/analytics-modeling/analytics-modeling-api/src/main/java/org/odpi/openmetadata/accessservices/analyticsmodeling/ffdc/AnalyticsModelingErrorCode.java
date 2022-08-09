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
 * <li>UserAction - describes how an AssetConsumerInterface should correct the error</li>
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
			"Relationship {0} for entity {1} could not be fetched. Cause: {2}",
			"The system is unable to fetch relationship for entity.", 
			"Review the exception cause and retry to fetch the relationship."),
	SERVICE_NOT_INITIALIZED(
			"OMAS-ANALYTICS-MODELING-004",
			"The access service has not been initialized for server {0} and can not support REST API calls",
			"The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
			"If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
	ENTITY_NOT_FOUND_EXCEPTION(404,
			"OMAS-ANALYTICS-MODELING-006", 
			"The entity with GUID = {0} was not found.",
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
	FAILED_UPDATE_UNKNOWN_ARTIFACT(404,
			"OMAS-ANALYTICS-MODELING-031", 
			"User {0} failed to update artifact {1}. The artifact is unknown.",
			"The system is unable to update unknown artifact.",
			"Confirm the artifact exists."),
	FAILED_FIND_ARTIFACT_ASSETS(404,
			"OMAS-ANALYTICS-MODELING-032", 
			"User {0} failed to find assets for analytics artifact {1}. Cause: {2}",
			"The system is unable to find asset for the artifact.",
			"Review the exception to resolve the issue to remove assets for the artifact."),
	MULTIPLE_ASSETS_SAME_QUALIFIEDNAME(
			"OMAS-ANALYTICS-MODELING-033", 
			"Multiple referenced assets have same qualified name {0}",
			"The system is unable to uniquely define source of metadata.",
			"Resolve ambiguity with assets qualified names."),
	FAILED_GET_ASSET_BY_QUALIFIEDNAME(
			"OMAS-ANALYTICS-MODELING-034", 
			"Failed to fetch assets by qualified name {0}. Cause: {1}",
			"The system is unable to fetch assets.",
			"Review exception message and requested qualified name."),
	FAILED_GET_SCHEMA_ATTRIBUTES_BY_QUALIFIEDNAME(
			"OMAS-ANALYTICS-MODELING-035", 
			"Failed to fetch schema attributes by qualified name {0}. Cause: {1}",
			"The system is unable to fetch schema attributes.",
			"Review exception message and requested qualified name pattern."),
	FAILED_GET_SERVER_CAPABILITY_GUID(
			"OMAS-ANALYTICS-MODELING-036", 
			"User {0} failed to retrieve server capability with GUID {1}. Cause: {2}",
			"The system is unable to retrieve server capability by GUID.",
			"Verify the server capability GUID and access rights."),
	FAILED_GET_SERVER_CAPABILITY_NAME(
			"OMAS-ANALYTICS-MODELING-037", 
			"User {0} failed to retrieve server capability with name {1}. Cause: {2}",
			"The system is unable to retrieve server capability by name.",
			"Verify the server capability name, and access rights."),
	
	
	// information messages codes 200-299, HTTP status 100: Continue.
	
//    INFO_SAMPLE(100,
//    		"OMAS-ANALYTICS-MODELING-200",
//            "Short description of the state or event.",
//            "Description of the state or event from system point of view.",
//            "Description of the state or event from user point of view."),
    
    // warning messages 300-499, HTTP status 206: Partial Content.
    
    WARNING_TABLE_NO_COLUMNS(206,
    		"OMAS-ANALYTICS-MODELING-300",
            "The tables {0} is excluded from import because it doesn't have any column.",
            "Table without columns is not useful. It is excluded from the import to avoid confusion.",
            "Review the message and fix the table definition, permissions, etc."),
    WARNING_TABLES_NO_COLUMNS(206,
    		"OMAS-ANALYTICS-MODELING-301",
            "Tables {0} are excluded from import because they don't have any column.",
            "Tables without columns are not useful. They are excluded from the import to avoid confusion.",
            "Review the message and fix the tables definition, permissions, etc."),
	TABLE_COLUMN_RELATIONSHIPS_EXCEPTION(206,
			"OMAS-ANALYTICS-MODELING-302", 
			"Failed to get relationships for table {0}.",
			"The system is unable to fetch relationships for the table.", 
			"Review the exception cause to fix table relationships."),
	GLOSSARY_TERM_EXCEPTION(206,
			"OMAS-ANALYTICS-MODELING-303", 
			"Failed to get glossary terms for entity {0}.",
			"The system is unable to fetch relationships for the entity.", 
			"Review the exception cause to fix entity relationships."),
	BUILD_GLOSSARY_TERM_EXCEPTION(206,
			"OMAS-ANALYTICS-MODELING-304", 
			"Failed to get build glossary terms {0}.",
			"The system is unable to build glossary term.", 
			"Review the exception cause to fix json serialization."),
	WARNING_ENTITY_NOT_FOUND(206,
			"OMAS-ANALYTICS-MODELING-305", 
			"The entity with GUID = {0} was not found while loading other object.",
			"The system is unable to find entity with the requested GUID while loading other object.", 
			"The warning may be the cause of the citical error."),
	WARNING_FOREIGN_KEY_UNKNOWN_CATALOG(206,
			"OMAS-ANALYTICS-MODELING-306", 
			"The catalog was not found while loading foreign key for column {0}.",
			"The system is unable to define foreign key to unknown catalog.", 
			"This is normal state when target catalog is not imported."),
	WARNING_FOREIGN_KEY_UNKNOWN_SCHEMA(206,
			"OMAS-ANALYTICS-MODELING-307", 
			"The schema was not found while loading foreign key for column {0}.",
			"The system is unable to define foreign key to unknown schema.", 
			"This is normal state when target schema is not imported."),
	WARNING_FOREIGN_KEY_UNKNOWN_TABLE(206,
			"OMAS-ANALYTICS-MODELING-308", 
			"The table was not found while loading foreign key for column {0}.",
			"The system is unable to define foreign key to unknown table.", 
			"This is normal state when target table is not imported."),
	WARNING_COLUMN_NOT_FOUND(206,
			"OMAS-ANALYTICS-MODELING-309", 
			"The column with GUID = {0} was not found.",
			"The system is unable to find column with the requested GUID.", 
			"The warning may be the cause of the missing column."),
	WARNING_COLUMN_NO_DATATYPE(206,
			"OMAS-ANALYTICS-MODELING-310", 
			"The column {0} does not have data type.",
			"The system is unable to find column data type.", 
			"Fix the column data type if the column is required."),
	WARNING_FOREIGN_KEY(206,
			"OMAS-ANALYTICS-MODELING-311", 
			"Failed to load foreign key for the column {0}.",
			"The system is unable to load foreign key from relationship.", 
			"Review the error message and check foreign key of the column."),
	WARNING_UNRESOLVED_REFERENCE(206,
			"OMAS-ANALYTICS-MODELING-312", 
			"Asset {0} has unresolved reference {1}.",
			"The system is unable to resolve reference to another asset.", 
			"Review the error message and validate GUID of the reference."),
	WARNING_CREATE_METADATA_LINK(206,
			"OMAS-ANALYTICS-MODELING-313",
			"Can not create metadata link between item {0} and item {1}.",
			"The system is unable to create metadata link.", 
			"Review the error message and permissions."),
	WARNING_UPDATE_METADATA_LINK(206,
			"OMAS-ANALYTICS-MODELING-314",
			"Can't update metadata links of the item {0}.",
			"The system is unable to update metadata link.", 
			"Review the error message and link existance and permissions."),
	WARNING_DELETE_METADATA_LINK(206,
			"OMAS-ANALYTICS-MODELING-315",
			"Can not delete metadata link {0} of the item {1}.",
			"The system is unable to delete metadata link.", 
			"Review the error message and link existance."),
	WARNING_LINKED_ASSET_NO_REFERENCE(206,
			"OMAS-ANALYTICS-MODELING-316",
			"Asset {0} referencing the asset {1} missed link definition.",
			"The assets connected by relathionship but missing link definition.", 
			"Review the asset dependencies and fix link."),
	WARNING_MANUAL_UPDATE_DEPENDENTS(206,
			"OMAS-ANALYTICS-MODELING-317",
			"All assets referencing the asset {0} should be updated manually.",
			"Failed to get relationships of dependent assets.", 
			"Update dependent assets manually."),
	WARNING_REVERENCE_NO_ALIAS(206,
			"OMAS-ANALYTICS-MODELING-318",
			"The reference {0} of the asset {1} is missing alias and can not be used.",
			"Referenced stored in repository is missing alias.", 
			"Remove unused reference or define its alias."),
	WARNING_FAILED_RESTORE_METADATA_LINK(206,
			"OMAS-ANALYTICS-MODELING-319",
			"Can't resore metadata link between item {0} and item {1}.",
			"The system is unable to restore metadata link.", 
			"Review the error message and update dependent asset."),
	WARNING_REVERENCE_NO_GUID(206,
			"OMAS-ANALYTICS-MODELING-320",
			"The reference {0} of the asset {1} is missing guid.",
			"Referenced stored in repository is missing guid.", 
			"Fix reference definition in the repository."),
	WARNING_ASSET_NO_REVERENCE(206,
			"OMAS-ANALYTICS-MODELING-321",
			"The asset {1} does not refere any asset.",
			"Asset does not have reference to any asset. It is normal for modules or visualizations with only constant constructs.", 
			"Define source of metadata for asset content."),
	WARNING_ASSET_NO_UPDATE(206,
			"OMAS-ANALYTICS-MODELING-322",
			"Dependent asset with qualified name {0} was not updated. Cause: {1}",
			"The system is unable to update dependent asset.",
			"Review exception message and request the update of the dependent asset."),
	
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
