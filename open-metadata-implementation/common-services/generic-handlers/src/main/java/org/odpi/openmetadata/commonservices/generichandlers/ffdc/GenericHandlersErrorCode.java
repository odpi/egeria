/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The GenericHandlersErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Repository Handler Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum GenericHandlersErrorCode implements ExceptionMessageSet
{
    /**
     * OMAG-GENERIC-HANDLERS-400-001 -  Service {0} is unable to process the properties supplied to method {1} because the requested owner
     * {2} ({3}) is not a recognized software capability.  The associated error message is: {4}
     */
    INTEGRATOR_NOT_RETURNED(400, "OMAG-GENERIC-HANDLERS-400-001",
                            "Service {0} is unable to process the properties supplied to method {1} because the requested owner {2} ({3}) is " +
                                    "not a recognized software capability.  The associated error message is: {4}",
                            "The system is unable to create a new instance in the metadata repository with an invalid integrator specified as" +
                                    " the owner.",
                            "Ensure the request includes the unique identifiers for a valid software capability entity to represent " +
                                    "the integrator and retry the request."),

    /**
     * OMAG-GENERIC-HANDLERS-400-002 - Service {0} is unable to process the properties supplied to method {1} because the unique name {2}
     * given for the requested owner does not match the unique name of {3} returned in software capability {4}
     */
    BAD_INTEGRATOR_NAME(400, "OMAG-GENERIC-HANDLERS-400-002",
                        "Service {0} is unable to process the properties supplied to method {1} because the unique name {2} given for the " +
                                "requested owner does not match the unique name of {3} returned in software capability {4}",
                        "The system is unable to create a new instance with an invalid integrator specified as the owner.",
                        "Retry the request with a matching the unique identifier and name for a valid software capability entity to " +
                                "represent the owner of the new instance."),

    /**
     * OMAG-GENERIC-HANDLERS-400-003 - Service {0} is unable to locate the external identifier {1} for the {2} ({3}) scope and {4} element {5}
     */
    UNKNOWN_EXTERNAL_IDENTITY (400, "OMAG-GENERIC-HANDLERS-400-003",
                        "Service {0} is unable to locate the external identifier {1} for the {2} ({3}) scope and {4} element {5}",
                        "The system is unable to confirm the synchronization of the element's property because the identifier " +
                                "from the third party technology (scope) is not known.",
                        "Investigate if the identifier is correct for the named element and scope.  Was it created successfully? " +
                                "Has something deleted it before this request ran? " +
                                "Typically these associations are created by an integration connector running in the Catalog Integrator OMIS." +
                                "Look for errors reported in the hosting integration daemon."),

    /**
     * OMAG-GENERIC-HANDLERS-400-004 - Service {0} is unable to locate the link between the external identifier {1} for the {2} ({3}) scope and {4} element {5}
     */
    UNKNOWN_RESOURCE_LINK(400, "OMAG-GENERIC-HANDLERS-400-004",
                          "Service {0} is unable to locate the link between the external identifier {1} for the {2} ({3}) scope and {4} element {5}",
                          "The system is unable to confirm the synchronization of the element's property because the identifier " +
                                  "from the third party technology (scope) is not linked to the element.",
                          "Investigate if the identifier is correct for the named element and scope. " +
                                  "Typically these associations are created by an integration connector running in the Catalog Integrator OMIS." +
                                  "Look for errors reported in the hosting integration daemon."),

    /**
     * OMAG-GENERIC-HANDLERS-400-005 - Governance Engine with unique name of {0} is not found by calling service {1} running in server {2}
     */
    UNKNOWN_ENGINE_NAME(400, "OMAG-GENERIC-HANDLERS-400-005",
                        "Governance Engine with unique name of {0} is not found by calling service {1} running in server {2}",
                        "The system is unable to initiate a governance action because the nominated governance engine is not found in the metadata repository.",
                        "Investigate whether the requested name is incorrect or the definition is missing. " +
                                "Then retry the request once the issue is resolved."),

    /**
     * OMAG-GENERIC-HANDLERS-400-006 - Unable to initiate an instance of a governance action because the governance action type {0} does not have a Governance Engine linked via the {1} relationship
     */
    UNKNOWN_EXECUTOR(400, "OMAG-GENERIC-HANDLERS-400-006",
                        "Unable to initiate an instance of a governance action because the governance action type {0} does not have a Governance Engine linked via the {1} relationship",
                        "The system is unable to initiate a governance action process because is its implementation definition is incomplete.",
                        "Update the definition of the first governance action type so that it is linked to a governance engine to execute the requested action. " +
                                "Then retry the request once the definition is corrected."),

    /**
     * OMAG-GENERIC-HANDLERS-400-007 - Unable to initiate an instance of the {0} governance action process because there is no first governance action type defined
     */
    NO_PROCESS_IMPLEMENTATION(400, "OMAG-GENERIC-HANDLERS-400-007",
                     "Unable to initiate an instance of the {0} governance action process because there is no first governance action type defined",
                     "The system is unable to initiate a governance action process because its implementation definition is missing.",
                     "Link a governance action type to the governance action process.  If the process is to have multiple steps to it, link " +
                             "additional governance action types to this first one to describe the execution flow. " +
                             "Then retry the request once the definition is corrected."),

    /**
     * OMAG-GENERIC-HANDLERS-400-008 - The {0} {1} does not match the {2} guid {3} at end {4} in the {5} relationship identified as {6} {7}
     */
    WRONG_END_GUID(400, "OMAG-GENERIC-HANDLERS-400-008",
                              "The {0} {1} does not match the {2} guid {3} at end {4} in the {5} relationship identified as {6} {7}",
                              "The request can not be processed because one of the unique identifiers supplied on the call does not match the " +
                                      "values stored in the open metadata repositories.",
                              "The most likely cause of the error is that the parameters passed on the call are incorrect.  " +
                                      "Correct the parameters and retry the request.  If the values are correct then save this error message along with " +
                                      "details of the stored metadata instances and contact the Egeria community."),

    /**
     * OMAG-GENERIC-HANDLERS-400-009 - Governance Engine {0} ({1}) does not support request type {2}; requested via service {3} running in server {4}
     */
    UNKNOWN_REQUEST_TYPE(400, "OMAG-GENERIC-HANDLERS-400-009",
                        "Governance Engine {0} ({1}) does not support request type {2}; requested via service {3} running in server {4}",
                        "The system is unable to initiate a governance action because the nominated request type is not found in the metadata repository.",
                        "Investigate whether the request type is incorrect or the definition is missing. " +
                                "Then retry the request once the issue is resolved."),

    /**
     * OMAG-GENERIC-HANDLERS-400-010 - Governance Engine {0} ({1}) does not support any request types and so it cannot run request type {2}; requested via service {3} running in server {4}
     */
    NO_REQUEST_TYPE_FOR_ENGINE(400, "OMAG-GENERIC-HANDLERS-400-010",
                         "Governance Engine {0} ({1}) does not support any request types and so it cannot run request type {2}; requested via service {3} running in server {4}",
                         "The system is unable to initiate a governance action because the nominated governance engine has no supported governance services.",
                         "Investigate why there are no supported governance services for the governance engine. " +
                                 "Then retry the request once the issue is resolved."),

    /**
     * OMAG-GENERIC-HANDLERS-400-011 - Unable to initiate an instance of the {0} governance action process because the name is not recognized
     */
    UNKNOWN_PROCESS(400, "OMAG-GENERIC-HANDLERS-400-011",
                              "Unable to initiate an instance of the {0} governance action process because the name is not recognized",
                              "The system is unable to initiate a governance action process because its definition is missing.",
                              "Verify that the process name (qualifiedName of a GovernanceActionProcess entity) is correct.  " +
                                      "Either set up the caller to use the correct name or create a GovernanceActionProcess entity with the requested qualifiedName.  " +
                                      "Then retry the request once the definition is added."),

    /**
     * OMAG-GENERIC-HANDLERS-400-012 - At least one of the properties supplied for a new relationship of type {0} are invalid.  The {1} exception was returned with error message: {2}
     */
    BAD_PARAMETER(400, "OMAG-GENERIC-HANDLERS-400-012",
                  "At least one of the properties supplied for a new relationship of type {0} are invalid.  The {1} exception was returned with error message: {2}",
                  "The system is unable to create the requested relationship because it can not parse the properties.",
                  "Correct the caller's logic so that the properties passed are correctly formatted and retry the request."),


    /**
     * OMAG-GENERIC-HANDLERS-403-001 - The {0} method is unable to delete the requested relationship between {1} {2} and {3} {4} because it
     * was not created by the requesting user {5}
     */
    ONLY_CREATOR_CAN_DELETE(403, "OMAG-GENERIC-HANDLERS-403-001",
            "The {0} method is unable to delete the requested relationship between {1} {2} and {3} {4} because it " +
                                    "was not created by the requesting user {5}",
                            "The request fails because the user does not have the rights to take this action.",
                            "Retry the request with a relationship created with this user, or request that the user who created " +
                                    "the relationship issues the delete request."),

    /**
     * OMAG-GENERIC-HANDLERS-403-002 - Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for governance action {2} because it is already being processed by Engine Host OMAG Server with a userId of {3}
     */
    INVALID_PROCESSING_USER(403, "OMAG-GENERIC-HANDLERS-403-002",
                            "Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for governance action {2} because it is already being processed by Engine Host OMAG Server with a userId of {3}",
                            "The system is unable to update a governance action because the requester has not claimed the governance action.",
                            "Investigate why the Engine Host OMAG Server is attempting to process this governance action.  If you have multiple Engine Host OMAG Servers " +
                                    "running the same governance engines then it is possible that they both attempted to claim the governance action at the same time.  If this is the case, " +
                                    "validate that the governance action is processed successful by the victorious engine host.  If this happens frequently, it may be necessary to " +
                                    "separate the workload amongst distinct governance engines that support the same governance services."),

    /**
     * OMAG-GENERIC-HANDLERS-403-003 - Engine Host OMAG Server with a userId of {0} is not allowed claim the governance action {1} because it is already being processed by Engine Host OMAG Server with a userId of {2} and is in status {3}
     */
    INVALID_GOVERNANCE_ACTION_STATUS(403, "OMAG-GENERIC-HANDLERS-403-003",
                            "Engine Host OMAG Server with a userId of {0} is not allowed claim the governance action {1} because it is already being processed by Engine Host OMAG Server with a userId of {2} and is in status {3}",
                            "The system is unable to claim a governance action because another Engine Host OMAG Server has got there first.",
                            "This is a normal event if there are more than one Engine Host OMAG Server running the same governance engine."),

    /**
     * OMAG-GENERIC-HANDLERS-404-002 - Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}
     */
    MULTIPLE_ENTITIES_FOUND(404, "OMAG-GENERIC-HANDLERS-404-002",
            "Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}",
            "The system is unable to process a request because multiple entities have been discovered and it is unsure which entity to use.",
            "Investigate why multiple entities exist.  Then retry the request once the issue is resolved."),

    /**
     * OMAG-GENERIC-HANDLERS-404-003 - Multiple {0} entities where found by method {1}: the values of the returned entities are {2}; the starting point is {3}; the calling service is {4} and the server is {5}
     */
    MULTIPLE_BEANS_FOUND(404, "OMAG-GENERIC-HANDLERS-404-003",
             "Multiple {0} entities where found by method {1}: the values of the returned entities are {2}; the starting point is {3}; the calling service is {4} and the server is {5}",
             "The system is unable to process a request because multiple entities have been discovered and this is not valid.",
             "Investigate why multiple entities exist.  It may be because they have been contributed by different repositories. " +
                     "Then retry the request once the issue is resolved."),

    /**
     * OMAG-GENERIC-HANDLERS-404-004 - Multiple {0} relationships where found between {1} entity {2} and {3} entity {4}: the identifiers of the returned relationships are {5}; the calling method is {6} and the server is {7}
     */
    MULTIPLE_RELATIONSHIPS_FOUND(404, "OMAG-GENERIC-HANDLERS-404-004",
                            "Multiple {0} relationships where found between {1} entity {2} and {3} entity {4}: the identifiers of the returned relationships are {5}; the calling method is {6} and the server is {7}",
                            "The system is unable to process a request because multiple relationships have been discovered and it is unsure which relationship to use.",
                            "Investigate why multiple relationship exist.  Then retry the request once the issue is resolved."),

    /**
     * OMAG-GENERIC-HANDLERS-500-001 - An unsupported bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; error message was: {4}
     */
    INVALID_BEAN_CLASS(500, "OMAG-GENERIC-HANDLERS-500-001",
                       "An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on " +
                               "server {3}; error message was: {4}",
                       "The system is unable to process the request because it is not able to instantiate the bean.",
                       "Correct the code that initializes the converter during server start up."),

    /**
     * OMAG-GENERIC-HANDLERS-500-002 - The {0} service has not implemented the {1} method in a subclass of the {2} converter class for
     * bean class {3} and so is unable to create the bean for method {4}
     */
    MISSING_CONVERTER_METHOD(500, "OMAG-GENERIC-HANDLERS-500-002",
                       "The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is " +
                               "unable to create the bean for method {4}",
                       "The system is unable to process the request because it is not able to populate the bean.",
                       "Correct the converter implementation as part of this module."),

    /**
     * OMAG-GENERIC-HANDLERS-500-003 - An unexpected bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; the expected class name is: {4}
     */
    UNEXPECTED_BEAN_CLASS(500, "OMAG-GENERIC-HANDLERS-500-003",
                       "An unexpected bean class named {0} was passed to the repository services by the {1} request for " +
                               "open metadata access service {2} on server {3}; " +
                               "the expected class name is: {4}",
                       "The system is unable to process the request because it is not able to support the bean's methods.",
                       "Correct the code that sets up the converter as part of this service."),

    /**
     * OMAG-GENERIC-HANDLERS-500-004 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} has not passed to method {3}
     */
    MISSING_METADATA_INSTANCE(500, "OMAG-GENERIC-HANDLERS-500-004",
                          "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                  "because a metadata instance of type {2} has not passed to method {3}",
                          "The system is unable to process the request because it is missing one or more metadata elements" +
                                      "needed to instantiate the bean.",
                          "Correct the handler code that calls the converter as part of this request since it has not passed sufficient" +
                                      " metadata instances to the converter.  Alternatively, these instances may not be in the repositories " +
                                      "(legitimately) and the converter needs to be able to handle that variation."),

    /**
     * OMAG-GENERIC-HANDLERS-500-005 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}
     */
    BAD_INSTANCE_TYPE(500, "OMAG-GENERIC-HANDLERS-500-005",
                              "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                      "because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}",
                              "The system is unable to process the request because the wrong type of instances have been retrieved from " +
                              "the metadata repositories.",
                              "The error is likely to be either in the handler code that called the converter, or more likely, " +
                              "in the way that the handler and the converter were initialized at server start up."),

    /**
     * OMAG-GENERIC-HANDLERS-500-006 - The entity for identifier {0} and {1} {2} ({3}) is null
     */
    NULL_EXTERNAL_ID_ENTITY(500, "OMAG-GENERIC-HANDLERS-500-006",
                      "The entity for identifier {0} and {1} {2} ({3}) is null",
                      "The system is unable to process the request because the handler has failed to retrieve the entity for the " +
                              "identifier correctly.",
                      "The error is likely to be either in the handler code or the integration connector that is managing the exchange" +
                              "of metadata for ."),

    /**
     * OMAG-GENERIC-HANDLERS-500-007 - The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} is null
     */
    MISSING_GOVERNANCE_ACTION(500, "OMAG-GENERIC-HANDLERS-500-007",
                            "The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} is null",
                            "The system is unable to process the request because the handler has failed to retrieve the entity for the " +
                                    "identifier.  Normally this would result in an InvalidParameterException and it is curious that it did not.",
                            "The error is likely to be in one of the repository connectors, but it may be either in the handler code " +
                                    "or the governance engines managing the governance action entities."),

    /**
     * OMAG-GENERIC-HANDLERS-500-008 - The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} has null properties
     */
    MISSING_GOVERNANCE_ACTION_PROPERTIES(500, "OMAG-GENERIC-HANDLERS-500-008",
                              "The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} has null properties",
                              "The system is unable to process the request because the handler has retrieved a governance action entity " +
                                      "that has no properties.  The handler does not know how to proceed.",
                              "The error is likely to be in one of the repository connectors " +
                                      "or the governance engines managing the governance action entities."),

    /**
     * OMAG-GENERIC-HANDLERS-500-009 - An anchor GUID of "unknown" has been passed to local method {0} by the {1} service through method {2}
     */
    UNKNOWN_ANCHOR_GUID(500, "OMAG-GENERIC-HANDLERS-500-009",
                                         "An anchor GUID of <unknown> has been passed to local method {0} by the {1} service through method {2}",
                                         "The system is unable to process the request because the handler has an invalid anchor GUID.",
                                         "Gather diagnostics and add them to issue #4680."),

    /**
     * OMAG-GENERIC-HANDLERS-500-010 - A null anchor GUID has been passed to local method {0} by the {1} service through method {2}
     */
    NULL_ANCHOR_GUID(500, "OMAG-GENERIC-HANDLERS-500-010",
                        "A null anchor GUID has been passed to local method {0} by the {1} service through method {2}",
                        "The system is unable to process the request because the handler has a null anchor GUID.",
                        "This typically means the caller has either been returned an entity with a null GUID or there is an error" +
                                "in the templated create logic.  Use the stack trace to determine the source of the error"),

    /**
     * OMAG-GENERIC-HANDLERS-500-011 - An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_ENTITY(500, "OMAG-GENERIC-HANDLERS-500-011",
                     "An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system is unable to format all or part of the response because the repositories have returned an invalid entity.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  " +
                             "There is probably an error in the implementation of the repository that originated the entity."),

    /**
     * OMAG-GENERIC-HANDLERS-500-012 - A relationship {0} has been retrieved by method {1} from service {2} that has an invalid entity proxy at end {3}: {4}
     */
    BAD_ENTITY_PROXY(500, "OMAG-GENERIC-HANDLERS-500-012",
                     "A relationship {0} has been retrieved by method {1} from service {2} that has an invalid entity proxy at end {3}: {4}",
                     "The system is unable to format all or part of the response because the repositories have returned a relationship with an " +
                             "invalid entity proxy that links it to an entity.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the relationship with the " +
                             "invalid entity proxy.  There is probably an error in the implementation of the repository that originated the relationship."),

    /**
     * OMAG-GENERIC-HANDLERS-500-013 - A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_RELATIONSHIP(500, "OMAG-GENERIC-HANDLERS-500-013",
                     "A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system is unable to format all or part of the response because the repositories have returned an invalid relationship.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  " +
                             "There is probably an error in the implementation of the repository that originated the relationship."),

    ;

    private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for GenericHandlersErrorCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineErrorCode above.   For example:
     * <br>
     *     GenericHandlersErrorCode   errorCode = GenericHandlersErrorCode.ASSET_NOT_FOUND;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GenericHandlersErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "GenericHandlersErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
