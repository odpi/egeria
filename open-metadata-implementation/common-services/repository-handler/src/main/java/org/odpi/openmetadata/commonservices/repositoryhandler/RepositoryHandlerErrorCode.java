/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The RepositoryHandlerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Repository Handler Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>403 - forbidden</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum RepositoryHandlerErrorCode implements ExceptionMessageSet
{
    INVALID_PROPERTY(400, "OMAG-REPOSITORY-HANDLER-400-001",
            "An unsupported property named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}",
            "The system is unable to process the request because it has no place to store the property.",
            "Correct the types and property names of the properties passed on the request."),
    INVALID_PARAMETER(400, "OMAG-REPOSITORY-HANDLER-400-002",
            "The {0} parameter passed by the {1} request for open metadata access service {2} on server " +
                             "{3} is invalid; value passed was: {4}",
            "The system is unable to process the request because it does no know how to process this value.",
            "Correct the value of the properties passed on the request."),
    BAD_CLASSIFICATION_PROPERTIES(400, "OMAG-REPOSITORY-HANDLER-400-003",
                                  "Service {0} is unable to process the properties supplied with classification {1}.  The associated error message was: {2}",
                                  "The system is unable to create a new instance with invalid properties in any of the classifications.",
                                  "Correct the classification parameters passed with this request."),
    INVALID_TYPE(404, "OMAG-REPOSITORY-HANDLER-400-004",
            "An unsupported type named {0} was passed to the repository services by the {1} request for open metadata access service {2} " +
                             "on server {3}; error message was: {4}",
            "The system is unable to process the request because the repository services are unable to store the supplied information.",
            "Change the call being made - or look to expand the collective capabilities of the available repositories by " +
                         "connecting an Egeria metadata server to the open metadata repository cohort that this server is connected to."),
    INVALID_PROPERTY_VALUE(400, "OMAG-REPOSITORY-HANDLER-400-005",
            "The property named {0} with value of {1} supplied on method {2} does not match the stored value of {3} for entity {4}",
            "The system is unable to process the request because there is a possibility that the caller is requesting changes to the wrong object.",
            "Correct the values of the properties passed on the request and retry."),
    DUPLICATE_CREATE_REQUEST(400, "OMAG-REPOSITORY-HANDLER-400-006",
            "Unable to create a new {0} with {1} of {2} as there is already an entity of that name with guid {3}",
            "The system is unable to create the requested entity because there is an entity of the same name already defined.",
            "Either use the existing entity or change the name for the new one and re-run the create request."),
    WRONG_EXTERNAL_SOURCE(400, "OMAG-REPOSITORY-HANDLER-400-007",
            "Method {0} running on behalf of external source {1} ({2}) is unable to modify {3} instance {4} because " +
                                     "it is has metadata provenance of {5} with an externalSourceGUID of {6} and an externalSourceName of {7}",
            "The system is unable to modify the requested instance because it does not have the correct " +
                                  "ownership rights to the instance.",
            "Route the request through a different process that is set up to use the correct external source identifiers."),
    LOCAL_CANNOT_CHANGE_EXTERNAL(400, "OMAG-REPOSITORY-HANDLER-400-008",
            "Method {0} is unable to modify {1} instance {2} because it is has metadata provenance of {3} with " +
                                  "an externalSourceGUID of {4} and an externalSourceName of {5} and user {6} issued a request with the Local Cohort metadata provenance set",
            "The system is unable to modify the requested instance because it does not have ownership rights to the instance.",
            "Route the request through a process that is set up to use the correct external source identifiers.  " +
                    "To understand more about this behavior, lookup Metadata Provenance in Egeria's Glossary."),
    UNRECOGNIZED_PROPERTY(400, "OMAG-REPOSITORY-HANDLER-400-009",
            "The property named {0} with value of {1} supplied on method {2} is not found in entity {3}",
            "The system does no process the request because there is a possibility that the caller is requesting changes to the wrong object.",
                           "Correct the value of the property passed on the request and retry."),
    UNAVAILABLE_ENTITY( 400, "OMAG-REPOSITORY-HANDLER-400-010",
                        "A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10}",
                        "The system is unable to format all or part of the response because the entity either has effectivity dates that are not effective for the time that the entity is retrieved or it is classified as a memento.",
                        "Use knowledge of the request and the contents of the repositories to determine if the entity is set up correctly or needs to be updated."),
    NO_ENTITY( 400, "OMAG-REPOSITORY-HANDLER-400-011",
                        "A {0} entity with unique identifier {1} can not be retrieved by method {2} from service {3} for caller {4}",
                        "The system is unable to format all or part of the response because the entity was not retrieved from the repository.",
                        "Use knowledge of the request and the contents of the repositories to determine if the identifier is correct."),

    USER_NOT_AUTHORIZED(403, "OMAG-REPOSITORY-HANDLER-403-001",
            "User {0} is not authorized to issue the {1} request for open metadata access service {2} on server {3}",
            "The system is unable to process the request because the user should not be making this request.",
            "Verify the access rights of the user."),
    ONLY_CREATOR_CAN_DELETE(403, "OMAG-REPOSITORY-HANDLER-403-002",
            "The {0} method is unable to delete the requested relationship between {1} {2} and {3} {4} because it " +
                                    "was not created by the requesting user {5}",
            "The request fails because the user does not have the rights to take this action.",
            "Retry the request with a relationship created with this user, or request that the user who created " +
                                    "the relationship issues the delete request."),
    INSTANCE_WRONG_TYPE_FOR_GUID(404, "OMAG-REPOSITORY-HANDLER-404-001",
            "The {0} method has retrieved a object for unique identifier (guid) {1} which is of type {2} rather than type {3} on behalf " +
                    "of method {4}",
            "The service is not able to return the requested object.",
            "Check that the unique identifier is correct and the metadata server(s) supporting the service is running."),
    OMRS_NOT_INITIALIZED(404, "OMAG-REPOSITORY-HANDLER-404-002",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to an open metadata repository.",
            "Check that the server initialized correctly.  " +
                      "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAG-REPOSITORY-HANDLER-404-003",
            "The open metadata repository services are not available for the {0} operation",
            "The system called a function that has not been enabled in this open metadata server.",
            "Check that the server initialized correctly and is not shutting down.  " +
                       "Correct any errors discovered and retry the request when the requested server, and its respective services are available."),
    NO_METADATA_COLLECTION(404, "OMAG-REPOSITORY-HANDLER-404-004",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata from the open metadata repositories because it does not have access to the API it needs.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),
    PROXY_ENTITY_FOUND(404, "OMAG-REPOSITORY-HANDLER-404-005",
            "Only an entity proxy for requested {0} object with unique identifier (guid) {1} is found in the open metadata server {2}, error message was: {3}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the OMAS Server URL are correct.  Retry the request when the connection is available in the OMAS Service"),
    MULTIPLE_RELATIONSHIPS_FOUND(404, "OMAG-REPOSITORY-HANDLER-404-006",
            "Multiple {0} relationships are connected to the {1} entity with unique identifier {2}: the relationship identifiers are {3}; the calling method is {4} and the server is {5}",
            "The system is unable to process a request because multiple relationships have been discovered and it is unsure which relationship to follow.",
            "Investigate why multiple relationships exist.  Then retry the request once the issue is resolved."),
    UNKNOWN_ENTITY(404, "OMAG-REPOSITORY-HANDLER-404-007",
            "The {0} entity with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}",
            "The system is unable to update information associated with the entity because none of the connected open metadata repositories recognize the entity's unique identifier.",
            "The unique identifier of the entity is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),
    NO_RELATIONSHIPS_FOUND(404, "OMAG-REPOSITORY-HANDLER-404-008",
            "No {0} relationships are connected to the {1} entity with unique identifier {2}: the calling method is {3} and the server is {4}",
            "The system is unable to process a request because no relationships have been discovered and it is unable to retrieve all the information it needs.",
            "Check that the unique identifier of the entity is correct and the metadata server supporting the request is running.  If these are " +
                                   "both correct, investigate why the relationships are missing.  It is likely that the process responsible for " +
                                   "the creation of the relationships has not yet run, or failed part way through its operation."),
    NULL_ENTITY_RETURNED(404, "OMAG-REPOSITORY-HANDLER-404-009",
            "A null entity was returned to method {0} of server {1} during a request for entity of type {2} (guid {3}) and properties of: {4}",
            "The system is unable to process a request because it can not find the requested entity.",
            "This may be a logic error in the caller or the server.  Alternatively the cohort may not be sharing information correctly.  Look for " +
                                 "errors in the server's audit log and console" +
                                 " to understand and correct the source of the error."),
    MULTIPLE_ENTITIES_FOUND(404, "OMAG-REPOSITORY-HANDLER-404-010",
            "Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}",
            "The system is unable to process a request because multiple entities have been discovered and it is unsure which entity to use.",
            "Investigate why multiple entities exist.  Then retry the request once the issue is resolved."),
    NULL_ENTITY_RETURNED_FOR_CLASSIFICATION(404, "OMAG-REPOSITORY-HANDLER-404-011",
            "A null entity was returned to method {0} of server {1} during a request to add a classification of type {4} (guid {3}) to entity {2} with properties of: {5}",
            "The system is unable to process a request because it can not find the requested entity to update.",
            "This may be a logic error or a configuration error (such as the cohort does not contain the correct members.  Look for errors in the " +
                                                    "server's audit log and console to understand and correct the source of any error."),
    UNKNOWN_RELATIONSHIP(404, "OMAG-REPOSITORY-HANDLER-404-012",
                   "The {0} relationship with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}",
                   "The system is unable to update information associated with the relationship because none of the connected open metadata repositories recognize the relationship's unique identifier.",
                   "The unique identifier of the relationship is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),
    NOT_EFFECTIVE_ELEMENT(404, "OMAG-REPOSITORY-HANDLER-404-013",
                          "The {0} element with unique identifier {1} is found for method {2} of access service {3} in open metadata server {4} however its effectivity dates are from: {5} to {6} and the requested effective date was {7}",
                          "The system is unable to return the element because the element is not active at this time.",
                          "The unique identifier of the element is supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the element are as expected. Once all errors have been resolved, and the time is right, retry the request."),

    BROADER_EFFECTIVE_RELATIONSHIP(404, "OMAG-REPOSITORY-HANDLER-404-015",
                                   "The {0} relationship with unique identifier {1} claims all effective dates which makes it broader than than the requested effective dates of {2} to {3}",
                                   "The system is unable to process with the request because the requested effectivity dates are incompatible with the existing relationships.",
                                   "The effectivity dates of the relationship are supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the request and retrieved relationship are as expected. Once all errors have been resolved, retry the request."),
    NARROWER_EFFECTIVE_RELATIONSHIP(404, "OMAG-REPOSITORY-HANDLER-404-016",
                                   "The {0} relationship with unique identifier {1} has narrower effective dates of {2} to {3} than the requested effective dates of {4} to {5}",
                                   "The system is unable to proceed because two relationships are attempting to occupying the same effectivity times.",
                                   "The effectivity dates are supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the relationship are as expected. If the command is to update the effectivity dates, rather than the relationship properties, use the specialist method for this purpose.  Once all errors have been resolved, and the time is right, retry the request."),
    OVERLAPPING_EFFECTIVE_RELATIONSHIPS(404, "OMAG-REPOSITORY-HANDLER-404-017",
                                         "The {0} relationship with unique identifier {1} has overlapping effective dates of {2} to {3} than the requested effective dates of {4} to {5}",
                                         "The system is unable to continue processing with these incompatible values.",
                                         "The effectivity dates are supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the retrieved relationship are also correct.  If the command is to update the effectivity dates, rather than the relationship properties, use the specialist method for this purpose.  Once all errors have been resolved, and the time is right, retry the request."),


    PROPERTY_SERVER_ERROR(500, "OMAG-REPOSITORY-HANDLER-500-001",
                          "An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service " +
                                  "{2} on server {3}; message was {0}",
                          "The system is unable to process the request because of an internal error.",
                          "Verify the sanity of the server.  This is probably a logic error.  If you can not work out what happened, ask the Egeria community for help."),

    UNABLE_TO_SET_ANCHORS(500, "OMAG-REPOSITORY-HANDLER-500-002",
                          "The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}." +
                                  " The resulting exception was {3} with error message {4}",
                          "The server was attempting to add Anchors classifications to a collection of metadata instances that are " +
                                  "logically part of the same object.  This classification is used to optimize the retrieval and " +
                                  "maintenance of complex objects.  It is optional function.  The server continues to " +
                                  "process the original request which will complete successfully unless something else goes wrong.",
                          "No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimal" +
                                  "because none of the repositories in the cohort support the Anchors classification.  To enable the " +
                                  "optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  " +
                                  "This will provide the support for the Anchors classification."),
    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for RepositoryHandlerErrorCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineErrorCode above.   For example:
     *
     *     RepositoryHandlerErrorCode   errorCode = RepositoryHandlerErrorCode.ASSET_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    RepositoryHandlerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
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


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "RepositoryHandlerErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
