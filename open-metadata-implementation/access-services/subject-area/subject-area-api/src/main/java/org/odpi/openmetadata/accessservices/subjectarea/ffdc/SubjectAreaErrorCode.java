/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The SubjectAreaErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Subject Area OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
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
 * <li>UserAction - describes how a SubjectAreaInterface should correct the error</li>
 * </ul>
 */
public enum SubjectAreaErrorCode implements ExceptionMessageSet {
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-SUBJECT-AREA-400-001",
            "The OMAG Server Platform root URL is null",
            "The system is unable to connect to the OMAG Server to retrieve metadata properties as the server url was not specified.",
            "Ensure a valid OMAG Server Platform root URL is passed to the Subject Area OMAS when it is created."),
    NULL_USER_ID(400, "OMAS-SUBJECT-AREA-400-002",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-SUBJECT-AREA-400-003",
            "The unique identifier (userId) passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a userId.",
            "Correct the code in the caller to provide the userId."),
    NULL_NAME(400, "OMAS-SUBJECT-AREA-400-004",
            "The name passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a name.",
            "Correct the code in the caller to provide the name."),
    GUID_DOES_NOT_EXIST(400, "OMAS-SUBJECT-AREA-400-005",
            "The unique identifier (userId) {0} passed on the {1} operation does not exist in the metadata repository",
            "The system is unable to process the request without a userId that exists.",
            "Correct the code in the caller to provide a userId that exists on the metadata server."),
    USER_NOT_AUTHORIZED(400, "OMAS-SUBJECT-AREA-400-006",
            "User {0} is not authorized to issue the {1} request for open metadata access service {2} on org.odpi.openmetadata.accessservices.subjectarea.server {3}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    METADATA_SERVER_UNCONTACTABLE_ERROR(400, "OMAS-SUBJECT-AREA-400-007",
            "An unexpected error with message {0} was returned by the property org.odpi.openmetadata.accessservices.subjectarea.server during {1} request for open metadata access service {2} on org.odpi.openmetadata.accessservices.subjectarea.server {3}",
            "The system is unable to process the request as the metadata server in not contactable.",
            "Contact your admin to correct configuration to enable the Metadata server to be contacted."),
    CREATE_WITHOUT_GLOSSARY(400, "OMAS-SUBJECT-AREA-400-008",
            "Cannot continue with {0} without a glossary",
            "The system is unable to process the request as the create requires a Glossary to be passed.",
            "Correct the code in the caller to create with a glossary."),
    GLOSSARY_TERM_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECT-AREA-400-009",
            "Cannot create a Term without a name",
            "The system is unable to process a term create request without a name.",
            "Correct the code in the caller to create a Term with a name."),
    GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECT-AREA-400-010",
            "Cannot create a Category without a name",
            "The system is unable to process a category create request without a name.",
            "Correct the code in the caller to create a Category with a name."),
    GLOSSARY_TERM_CREATE_WITH_CATEGORIES(400, "OMAS-SUBJECT-AREA-400-011",
            "Cannot create a Glossary Term {0} with categories",
            "The system is unable to process a Term create request specifying categories.",
            "Correct the code in the caller to remove the categories. Knit the term into categories after it has been created."),
    GLOSSARY_TERM_UPDATE_WITH_CATEGORIES(400, "OMAS-SUBJECT-AREA-400-013",
            "Cannot update a Glossary Term {0} with categories",
            "The system is unable to update a term with categories .",
            "Correct the code in the caller to remove the categories."),
    GLOSSARY_PROJECT_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECT-AREA-400-015",
            "Cannot create a Project without a name",
            "The system is unable to create a glossary project without a name.",
            "Correct the code in the caller to create a Project with a name."),
    INVALID_PROJECT_DELETION(400, "OMAS-SUBJECT-AREA-400-016",
            "Cannot delete Project as it was not intended for glossary use.",
            "The system is unable to delete a project that is not a glossary project.",
            "Correct the code in the caller to only delete Glossary Projects."),
    CREATE_WITH_GLOSSARY_RELATIONSHIP(400, "OMAS-SUBJECT-AREA-400-017",
            "Glossary relationship with userId {0} supplied on a create. The relationship cannot exist prior to the relationship end being created.",
            "The system is unable to proceed with the create request, as it has unexpectly found a relationship to a glossary already exists.",
            "Raise a Git issue indicating that OMAS-SUBJECT-AREA-400-017 has been issued."),
    CREATE_WITH_NON_EXISTANT_GLOSSARY_GUID(400, "OMAS-SUBJECT-AREA-400-018",
            "Glossary with guid {0} does not exist. Cannot create without a glossary",
            "The system is unable to process create request as the supplied glossary userId does not exist.",
            "Correct the code in the caller to create with a valid glossary userId."),
    GLOSSARY_TERM_CREATE_WITH_PROJECTS(400, "OMAS-SUBJECT-AREA-400-019",
            "Cannot create a Glossary Term {0} with projects",
            "The system is unable to process the Term create request with projects.",
            "Correct the code in the caller to remove the projects. Knit the term into a project after the term has been created."),
//    GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY(400, "OMAS-SUBJECT-AREA-400-020",
//            "Glossary with name {0} does not exist. Cannot create a Category without a glossary",
//            "The system is unable to process the request.",
//            "Correct the code in the caller to create a Category with a valid glossaryName."),
    GLOSSARY_TERM_CREATE_FAILED_ADDING_CLASSIFICATIONS(400, "OMAS-SUBJECT-AREA-400-021",
            "Term creation with name {0} failed, because it was unable to create its classifications",
            "The system has deleted the Glossary term and is unable to process the request.",
            "Retry the Glossary Term creation with correct classifications."),
    GLOSSARY_CREATE_FAILED_ADDING_CLASSIFICATIONS(400, "OMAS-SUBJECT-AREA-400-022",
            "Glossary creation with name {0} failed, because it was unable to create its classifications",
            "The system has deleted the Glossary and is unable to process the requested classifications.",
            "Retry the Glossary creation with valid classifications."),
    GLOSSARY_CONTENT_PREVENTED_DELETE(400, "OMAS-SUBJECT-AREA-400-023",
            "Glossary (guid {0}) deletion failed, because there is glossary content",
            "The system is unable to process the glossary delete becase the glossary has content.",
            "Retry the Glossary deletion when it is is empty."),
    GLOSSARY_CATEGORY_CREATE_FAILED_ADDING_CLASSIFICATIONS(400, "OMAS-SUBJECT-AREA-400-024",
            "Category creation with name {0} failed, because it was unable to create its classifications",
            "The system has deleted the Glossary category and is unable to add the requested classifications .",
            "Retry the Glossary Category creation with valid classifications."),
    GLOSSARY_CATEGORY_CREATE_FAILED_KNITTING_TO_GLOSSARY(400, "OMAS-SUBJECT-AREA-400-025",
            "Glossary Category creation with name {0} failed, because it was unable to create the relationship with its Glossary",
            "The system has deleted the Glossary category and is unable to create a relationship to the requested glossary.",
            "Retry the Glossary Category creation with a valid glossary."),
    CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY(400, "OMAS-SUBJECT-AREA-400-028",
            "Glossary Category update with failed, because the category has been deleted.",
            "The system is unable to process the Category update request - becase the category has already been deleted..",
            "Retry the Glossary Category update against a category that has not been deleted."),
    GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY(400, "OMAS-SUBJECT-AREA-400-029",
            "Glossary update with failed, because the glossary has been deleted.",
            "The system is unable to process the glossary update request, because the glossary has been deleted..",
            "Retry the Glossary update against a glossary that has not been deleted."),
    TERM_UPDATE_FAILED_ON_DELETED_TERM(400, "OMAS-SUBJECT-AREA-400-030",
            "Term update with failed, because the term has been deleted.",
            "The system is unable to process the Term update request, because the Term has been deleted.",
            "Retry the term update against a term that has not been deleted."),
    TYPEDEF_NOT_KNOWN(400, "OMAS-SUBJECT-AREA-400-031",
            "A request {0} has been made for type {1} which is not known.",
            "The system is unable to process the request with an unknown TypeDef",
            "Look into whether the typename was correctly specified."),
    TYPEDEF_ERROR(400, "OMAS-SUBJECT-AREA-400-032",
            "An error occurred when processing a request {0}; the instance is not associated with a known type.",
            "The system is unable to process the request as there was an error associated with the requested type.",
            "Correct the call so that the Typedef is well valid."),
    ENTITY_NOT_KNOWN_ERROR(400, "OMAS-SUBJECT-AREA-400-033",
            "An error occurred when processing a request {0} involving entity {1}.",
            "The system is unable to process the request because the entity was not known.",
            "Retry the request with a known entity."),
    RELATIONSHIP_NOT_KNOWN_ERROR(400, "OMAS-SUBJECT-AREA-400-034",
            "An error occurred when processing a request {0}  involving relationship {1} .",
            "The system is unable to process the request for the relationship, because it is not known.",
            "Retry the request with a known relationship."),

    // The following Exceptions are not descriptive enough - they need more information from the OMRS Exception
    INVALID_PARAMETER(400, "OMAS-SUBJECT-AREA-400-035",
            "Invalid parameter processing request {0}.",
            "The system is unable to process the request due to an invalid parameter.",
            "Please correct the parameter and retry."),
    CLASSIFICATION_ERROR(400, "OMAS-SUBJECT-AREA-400-036",
            "Invalid classification processing request {0}.",
            "The system is unable to process the request due to a classification error.",
            "Please correct the classification and retry."),
    STATUS_NOT_SUPPORTED_ERROR(400, "OMAS-SUBJECT-AREA-400-037",
            "Status not supported occurred processing request {0}.",
            "The system is unable to process the request because the requested status is not supported",
            "Please correct the status and retry."),
    FUNCTION_NOT_SUPPORTED(400, "OMAS-SUBJECT-AREA-400-038",
            "Function not supported occurred processing request {0}.",
            "The system is unable to process the request, as the requested function is not supported.",
            "Please so not use this function as it is unsupported."),
    PAGING_ERROR(400, "OMAS-SUBJECT-AREA-400-039",
            "Paging error occurred processing request {0}.",
            "The system is unable to process the request due to a paging error.",
            "Please retry the request with different paging options."),
    SUBJECT_AREA_FAILED_TO_INITIALISE(400, "OMAS-SUBJECT-AREA-400-040",
            "An error occurred when initializing the subject area client .",
            "The system is unable to initialize the subject area client, because it failed to connect to the omas server.",
            "Retry the request with a known available server name and url."),

    // End of The following Exception


    CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR(400, "OMAS-SUBJECT-AREA-400-043",
            "Received unexpected response category {0} from the server.",
            "The system is unable to process the request as it has received an unexpected error response.",
            "Contact your administrator to review the audit log to find the cause of the unexpected error."),

    INVALID_STATUS_VALUE_SUPPLIED(400, "OMAS-SUBJECT-AREA-400-0045",
            "A status value {0} was supplied - but this is not a valid status",
            "The system is unable to process the request with invalid parameters.",
            "Correct the code in the caller to provide a valid status."),
    STATUS_UPDATE_TO_DELETED_NOT_ALLOWED(400, "OMAS-SUBJECT-AREA-400-0046",
            "A status was attempted to be updated to deleted, this is not permitted.",
            "The system is unable to process the request to change the status to deleted.",
            "Correct the code in the caller to provide a valid status. Use delete call to change status to deleted."),

    GLOSSARY_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECT-AREA-400-049",
            "Cannot create a Glossary without a name",
            "The system is unable to process the Glossary create request without a name.",
            "Correct the code in the caller to create a Glossary with a name."),

    UNABLE_TO_PARSE_SUPPLIED_JSON(400, "OMAS-SUBJECT-AREA-400-0054",
            "Unable to parse the supplied json.",
            "The system is unable to process the request because it cannot parse the json.",
            "Correct the code in the caller to provide a valid json."),
    INVALID_NODETYPE(400, "OMAS-SUBJECT-AREA-400-057",
            "The nodeType passed is not valid for this operation",
            "The system is unable to process the request with an invalid node type.",
            "Correct the code in the caller to provide a valid NodeType"),
    UNEXPECTED_NODETYPE(400, "OMAS-SUBJECT-AREA-400-058",
            "The returned nodeType {0} did not equal the requested nodeType {1}",
            "The system returned an unexpected node type in the response.",
            "Raise a github issue on the system indicating that you received message OMAS-SUBJECT-AREA-400-058."),

    GUID_NOT_DELETED_ERROR(400, "OMAS-SUBJECT-AREA-400-060",
            "A restore was issued for userId {0}, but the status was not deleted. Restores can only succeed after a soft delete.",
            "The system returns the not deleted response.",
            "Issue a soft delete prior to attempting a restore."),
    ERROR_ENCODING_QUERY_PARAMETER(400, "OMAS-SUBJECT-AREA-400-064",
            "An error occurred when attempting to encode the value of Query parameter {0}",
            "The client is unable to send the rest call as the supplied query parameter {0} cannot be encoded.",
            "Correct the code in the caller to supply a query parameter that can be encoded."),
    MAPPER_ENTITY_GUID_TYPE_ERROR(400, "OMAS-SUBJECT-AREA-400-065",
            "An error occurred because userId {0} has type {1} but was expected to have type {2} ,",
            "The server is unable to continue the call.",
            "Correct the code in the caller to supply a userId that corresponds to appropriate type."),
    MAPPER_RELATIONSHIP_GUID_TYPE_ERROR(400, "OMAS-SUBJECT-AREA-400-066",
            "An error occurred because userId {0} is not a {1} so the Relationship cannot be mapped to a {1} ,",
            "The server is unable to continue the call as the supplied relationship userId is not associated with the expected type.",
            "Correct the code in the caller to supply a userId that corresponds to relationship type {1}."),
    RELATIONSHIP_UPDATE_ATTEMPTED_WITH_NO_PROPERTIES(400, "OMAS-SUBJECT-AREA-400-067",
                                                     "Cannot update a relationship if no properties are supplied",
                                                     "The system is unable to process the relationship update request as no properties were supplied.",
                                                     "Correct the code in the caller to only issue updates if there is something to update."),
    SERVER_NAME_NOT_SPECIFIED(400, "OMAS-SUBJECT-AREA-400-069",
            "The OMAG server name is null",
            "The system is unable to connect to the OMAG Server to retrieve metadata properties as the server name was not specified.",
            "Ensure a valid OMAG Server name is passed to the Subject Area OMAS when it is created."),
    UNEXPECTED_EXCEPTION(400, "OMAS-SUBJECT-AREA-400-070",
            "An unexpected Exception occurred. The Exception message is {0}.",
            "The system encountered an unexpected exception.",
            "Review the exception message to assess whether this is a logic error, in which case raise a git issue against Egeria, or a resource constraint - in which case address that issue."),
    BAD_OUT_TOPIC_CONNECTION(400, "OMAS-SUBJECT-AREA-400-071",
            "The Subject Area Open Metadata Access Service (OMAS) has been passed an invalid connection for publishing events.  The connection was {0}.  The resulting exception of {1} included the following message: {2}",
            "The access service has not been passed valid configuration for its out topic connection.",
            "Correct the topic configuration and restart the server."),
    PROJECT_CONTENT_PREVENTED_DELETE(400, "OMAS-SUBJECT-AREA-400-072",
            "Project (guid {0}) deletion failed, because there is project content",
            "The system is unable to process the project delete because the project has content.",
            "Retry the Project deletion when it is is empty."),
    TERM_CREATE_WITH_BAD_CATEGORIES(400, "OMAS-SUBJECT-AREA-400-073",
            "Cannot continue with create with the supplied Categories, as they do not exist",
            "The system is unable to process the request as the Term create requires the supplied Categories to exist.",
            "Correct the code in the caller to create Terms specifying not categories opr existing categories."),
    INVALID_RELATIONSHIPTYPES_FOR_GRAPH(400, "OMAS-SUBJECT-AREA-400-076",
                                                               "Cannot continue with the {0} operation because an invalid relationship name {1} was supplied in the filter",
                                                               "The system is unable to process the request as it cannot identify the requested Relationship type.",
                                                               "Amend the code to supply only valid Relationship Types for the graph operation."),
    UNKNOWN_RELATIONSHIPTYPE_REQUESTED_FOR_GRAPH(400, "OMAS-SUBJECT-AREA-400-077",
                                                 "Cannot continue with the {0} operation because there is the Unknown relationship name {1} was supplied in the filter",
                                                 "The system is unable to process the request as it cannot identify the requested Unknown Relationship type.",
                                                 "Amend the code to supply only known Relationship Types for the graph operation."),
    OMRS_NOT_INITIALIZED(404, "OMAS-SUBJECT-AREA-404-001",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Subject Area OMAS is running initialized correctly." +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-SUBJECT-AREA-404-002",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata server.",
            "Check that the server where the Subject Area OMAS is running initialized OMRS correctly." +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "OMAS-SUBJECT-AREA-404-003",
            "The requested connection {0} is not found in OMAG Server {1}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the OMAG Server Platform URL is correct. Retry the request when the connection is available in the" +
                                   " OMAS Service"),

    NULL_CONNECTION_RETURNED(500, "OMAS-SUBJECT-AREA-500-001",
            "The requested connection named {0} is not returned by the open metadata Server {1}",
            "The system is unable to create a connector because the OMAG Server is not returning the Connection properties.",
            "Verify that the OMAG server running and the connection definition is correctly " +
                                     "configured."),
    NULL_CONNECTOR_RETURNED(500, "OMAS-SUBJECT-AREA-500-002",
            "The requested connector for connection named {0} is not returned by the OMAG Server {1}",
            "The system is unable to create a connector.",
            "Verify that the server is running and the connection definition is correctly configured."),
    NULL_RESPONSE_FROM_API(503, "OMAS-SUBJECT-AREA-503-001",
            "A null response was received from REST API call {0} to server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
            "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-SUBJECT-AREA-503-002",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Look for errors in the local server's console to understand and correct the source of the error. This could be due to the url being incorrect or the server not being up."),
    CLIENT_SIDE_API_REST_RESPONSE_ERROR(503, "OMAS-SUBJECT-AREA-503-003",
            "The rest call successfully completed, but the response content could not be interpreted for API call {0} to repository {1}.  The error message was {2}",
            "REST API in a remote server completed, but the response returned was not as expected.",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(504, "OMAS-SUBJECT-AREA-503-004",
            "The access service has not been initialized and can not support REST API call {0}",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is " +
                                    "not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    NOT_FOUND_CLIENT(500, "OMAS-SUBJECT-AREA-500-003",
            "Not found client for {0}.",
            "During a method call `getClient` the cache could not find a client instance for this class.",
            "Check if the type passed to the getClient method is correct. Check if a client has been created for this type.")
    ;
    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    SubjectAreaErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode,
                                              errorMessageId,
                                              errorMessage,
                                              systemAction,
                                              userAction);
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
        ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                                      errorMessageId,
                                                                                      errorMessage,
                                                                                      systemAction,
                                                                                      userAction);

        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}

