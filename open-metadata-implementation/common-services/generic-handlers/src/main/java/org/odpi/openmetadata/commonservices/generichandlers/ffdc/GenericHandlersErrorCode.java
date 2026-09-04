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
     * OMAG-GENERIC-HANDLERS-400-005 - Governance Engine with unique name of {0} is not found by calling service {1} running in server {2}
     */
    UNKNOWN_ENGINE_NAME(400, "OMAG-GENERIC-HANDLERS-400-005",
                        "Governance Engine with unique name of {0} is not found by calling service {1} running in server {2}",
                        "The system cannot initiate an engine action because the nominated governance engine is not found in the metadata repository.",
                        "Investigate whether the requested name is incorrect or the definition is missing. " +
                                "Then retry the request once the issue is resolved.",
                                "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-006 - Unable to initiate an instance of an engine action because the governance action process step {0} does not have a Governance Engine linked via the {1} relationship
     */
    UNKNOWN_EXECUTOR(400, "OMAG-GENERIC-HANDLERS-400-006",
                        "Unable to initiate an instance of an engine action because the governance action process step {0} does not have a Governance Engine linked via the {1} relationship",
                        "The system cannot initiate a governance action process because is its implementation definition is incomplete.",
                        "Update the definition of the first governance action process step so that it is linked to a governance engine to execute the requested action. " +
                                "Then retry the request once the definition is corrected.",
                                "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-007 - Unable to initiate an instance of the {0} governance action process because there is no first governance action process step defined
     */
    NO_PROCESS_IMPLEMENTATION(400, "OMAG-GENERIC-HANDLERS-400-007",
                     "Unable to initiate an instance of the {0} governance action process because there is no first governance action process step defined",
                     "The system cannot initiate a governance action process because its implementation definition is missing.",
                     "Link a governance action process step to the governance action process.  If the process is to have multiple steps to it, link " +
                             "additional governance action process steps to this first one to describe the execution flow. " +
                             "Then retry the request once the definition is corrected.",
                             "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-009 - Governance Engine {0} ({1}) does not support request type {2}; requested via service {3} running in server {4}
     */
    UNKNOWN_REQUEST_TYPE(400, "OMAG-GENERIC-HANDLERS-400-009",
                        "Governance Engine {0} ({1}) does not support request type {2}; requested via service {3} running in server {4}",
                        "The system cannot initiate a engine action because the nominated request type is not found in the metadata repository.",
                        "Investigate whether the request type is incorrect or the definition is missing. " +
                                "Then retry the request once the issue is resolved.",
                                "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-010 - Governance Engine {0} ({1}) does not support any request types and so it cannot run request type {2}; requested via service {3} running in server {4}
     */
    NO_REQUEST_TYPE_FOR_ENGINE(400, "OMAG-GENERIC-HANDLERS-400-010",
                         "Governance Engine {0} ({1}) does not support any request types and so it cannot run request type {2}; requested via service {3} running in server {4}",
                         "The system cannot initiate an engine action because the nominated governance engine has no supported governance services.",
                         "Investigate why there are no supported governance services for the governance engine. " +
                                 "Then retry the request once the issue is resolved.",
                                 "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-011 - Unable to initiate an instance of the {0} governance action process because the name is not recognized
     */
    UNKNOWN_PROCESS(400, "OMAG-GENERIC-HANDLERS-400-011",
                              "Unable to initiate an instance of the {0} governance action process because the name is not recognized",
                              "The system cannot initiate a governance action process because its definition is missing.",
                              "Verify that the process name (qualifiedName of a GovernanceActionProcess entity) is correct.  " +
                                      "Either set up the caller to use the correct name or create a GovernanceActionProcess entity with the requested qualifiedName.  " +
                                      "Then retry the request once the definition is added.",
                                      "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-013 - Unable to initiate an instance of the {0} governance action type because the name is not recognized
     */
    UNKNOWN_GOVERNANCE_ACTION_TYPE(400, "OMAG-GENERIC-HANDLERS-400-013",
                                   "Unable to initiate an instance of the {0} governance action type because the name is not recognized",
                                   "The system cannot initiate a governance action type because its definition is missing.",
                                   "Verify that the name (qualifiedName of a GovernanceActionType entity) is correct.  " +
                                           "Either set up the caller to use the correct name or create a GovernanceActionType entity with the requested qualifiedName.  " +
                                           "Then retry the request once the definition is added.",
                                           "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-400-014 - The {0} element carries the TemplateSubstitute classification but has no
     * SourcedFrom relationship to the template it stands in for
     */
    NO_SUBSTITUTE_TEMPLATE(400, "OMAG-GENERIC-HANDLERS-400-014",
                           "The {0} element carries the TemplateSubstitute classification but has no SourcedFrom " +
                                   "relationship to the template it stands in for, so the {1} request has no template to work from",
                           "The system cannot create an element from this template because the TemplateSubstitute " +
                                   "classification directs it to the element the substitute is sourced from, and there is no such element.",
                           "Either attach a SourcedFrom relationship from the substitute to the template it stands in for, " +
                                   "or remove the TemplateSubstitute classification so that the element is used as a template in its own right.",
                                   "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-403-001 - The {0} method cannot delete the requested relationship between {1} {2} and {3} {4} because it
     * was not created by the requesting user {5}
     */
    ONLY_CREATOR_CAN_DELETE(403, "OMAG-GENERIC-HANDLERS-403-001",
            "The {0} method cannot delete the requested relationship between {1} {2} and {3} {4} because it " +
                                    "was not created by the requesting user {5}",
                            "The request fails because the user does not have the rights to take this action.",
                            "Retry the request with a relationship created with this user, or request that the user who created " +
                                    "the relationship issues the delete request.",
                                    "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-403-002 - Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for engine action {2} because it is already being processed by Engine Host OMAG Server with a userId of {3}
     */
    INVALID_PROCESSING_USER(403, "OMAG-GENERIC-HANDLERS-403-002",
                            "Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for engine action {2} because it is already being processed by Engine Host OMAG Server with a userId of {3}",
                            "The system cannot update an engine action that a different engine host has claimed.",
                            "Investigate why the Engine Host OMAG Server is attempting to process this engine action.  If you have multiple Engine Host OMAG Servers " +
                                    "running the same governance engines then it is possible that they both attempted to claim the engine action at the same time.  If this is the case, " +
                                    "validate that the engine action is processed successful by the victorious engine host.  If this happens frequently, it may be necessary to " +
                                    "separate the workload amongst distinct governance engines that support the same governance services.",
                                    "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-403-006 - Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for engine action {2} because the engine action has not been claimed
     */
    ENGINE_ACTION_NOT_CLAIMED(403, "OMAG-GENERIC-HANDLERS-403-006",
                              "Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for engine action {2} because the engine action has not been claimed",
                              "The system cannot update an engine action that nobody has claimed, because the update would not be attributable to the engine host doing the work.",
                              "Claim the engine action before updating it.  An engine action is claimed by the engine host that is going to run it, which records itself as the " +
                                      "processing engine user and moves the action to ACTIVATING.  If the caller is not an engine host, this request is not one it should be " +
                                      "making: the status of an unclaimed engine action is managed by the governance engine that picks it up.",
                              "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-403-003 - Engine Host OMAG Server with a userId of {0} is not allowed claim the engine action {1} because it is already being processed by Engine Host OMAG Server with a userId of {2} and is in status {3}
     */
    INVALID_ENGINE_ACTION_STATUS(403, "OMAG-GENERIC-HANDLERS-403-003",
                                 "Engine Host OMAG Server with a userId of {0} is not allowed claim the engine action {1} because it is already being processed by Engine Host OMAG Server with a userId of {2} and is in status {3}",
                                 "The system cannot claim an engine action because another Engine Host OMAG Server has got there first.",
                                 "This is a normal event if there are more than one Engine Host OMAG Server running the same governance engine.",
                                 "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-403-004 - A delete of {0} data asset {1} is not permitted because it is being used by {2} data set {3}
     */
    DATA_STORE_IN_USE(403, "OMAG-GENERIC-HANDLERS-403-004",
                      "A delete of {0} data asset {1} is not permitted because it is being used by {2} data set {3}",
                      "The system cannot delete a data asset because it is connected to a data set that is using it to supply its data content.",
                      "This call requires a cascaded delete to allow an element that is in use, or with dependent elements to be removed.  Either delete the relationship to the data set, or use the cascaded delete option.",
                      "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-403-005 - A delete of {0} element {1} is not permitted because it still has a dependent {2} element {3}
     */
    DEPENDENT_ELEMENTS_FOUND(403, "OMAG-GENERIC-HANDLERS-403-005",
                      "A delete of {0} element {1} is not permitted because it still has a dependent {2} element {3}",
                      "The system cannot delete an element because it is connected to other elements that are dependent on it.",
                      "This call requires a cascaded delete to allow an element that with these dependent elements, or with dependent elements to be removed.  Either delete the dependent elements, or use the cascaded delete option.",
                      "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-404-002 - Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}
     */
    MULTIPLE_ENTITIES_FOUND(404, "OMAG-GENERIC-HANDLERS-404-002",
            "Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}",
            "The system cannot process a request because multiple entities have been discovered and it is unsure which entity to use.",
            "Investigate why multiple entities exist.  Then retry the request once the issue is resolved.",
            "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-404-004 - Multiple {0} relationships where found between {1} entity {2} and {3} entity {4}: the identifiers of the returned relationships are {5}; the calling method is {6} and the server is {7}
     */
    MULTIPLE_RELATIONSHIPS_FOUND(404, "OMAG-GENERIC-HANDLERS-404-004",
                            "Multiple {0} relationships where found between {1} entity {2} and {3} entity {4}: the identifiers of the returned relationships are {5}; the calling method is {6} and the server is {7}",
                            "The system cannot process a request because multiple relationships have been discovered and it is unsure which relationship to use.",
                            "Investigate why multiple relationship exist.  Then retry the request once the issue is resolved.",
                            "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-001 - An unsupported bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; error message was: {4}
     */
    INVALID_BEAN_CLASS(500, "OMAG-GENERIC-HANDLERS-500-001",
                       "An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on " +
                               "server {3}; error message was: {4}",
                       "The system cannot process the request because it is not able to instantiate the bean.",
                       "Correct the code that initializes the converter during server start up.",
                       "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-002 - The {0} service has not implemented the {1} method in a subclass of the {2} converter class for
     * bean class {3} and so cannot create the bean for method {4}
     */
    MISSING_CONVERTER_METHOD(500, "OMAG-GENERIC-HANDLERS-500-002",
                       "The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is " +
                               "unable to create the bean for method {4}",
                       "The system cannot process the request because it is not able to populate the bean.",
                       "Correct the converter implementation as part of this module.",
                       "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-003 - An unexpected bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; the expected class name is: {4}
     */
    UNEXPECTED_BEAN_CLASS(500, "OMAG-GENERIC-HANDLERS-500-003",
                       "An unexpected bean class named {0} was passed to the repository services by the {1} request for " +
                               "open metadata access service {2} on server {3}; " +
                               "the expected class name is: {4}",
                       "The system cannot process the request because it is not able to support the bean's methods.",
                       "Correct the code that sets up the converter as part of this service.",
                       "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-004 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} has not passed to method {3}
     */
    MISSING_METADATA_INSTANCE(500, "OMAG-GENERIC-HANDLERS-500-004",
                          "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                  "because a metadata instance of type {2} has not passed to method {3}",
                          "The system cannot process the request because it is missing one or more metadata elements " +
                                      "needed to instantiate the bean.",
                          "Correct the handler code that calls the converter as part of this request since it has not passed sufficient" +
                                      " metadata instances to the converter.  Alternatively, these instances may not be in the repositories " +
                                      "(legitimately) and the converter needs to be able to handle that variation.",
                                      "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-005 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}
     */
    BAD_INSTANCE_TYPE(500, "OMAG-GENERIC-HANDLERS-500-005",
                              "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                      "because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}",
                              "The system cannot process the request because the wrong type of instances have been retrieved from " +
                              "the metadata repositories.",
                              "The error is likely to be either in the handler code that called the converter, or more likely, " +
                              "in the way that the handler and the converter were initialized at server start up.",
                              "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-007 - The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} is null
     */
    MISSING_ENGINE_ACTION(500, "OMAG-GENERIC-HANDLERS-500-007",
                          "The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} is null",
                          "The system cannot process the request because the handler has failed to retrieve the entity for the " +
                                    "identifier.  Normally this would result in an InvalidParameterException and it is curious that it did not.",
                          "The error is likely to be in one of the repository connectors, but it may be either in the handler code " +
                                    "or the governance engines managing the engine action entities.",
                                    "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-008 - The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} has null properties
     */
    MISSING_ENGINE_ACTION_PROPERTIES(500, "OMAG-GENERIC-HANDLERS-500-008",
                                     "The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} has null properties",
                                     "The system cannot process the request because the handler has retrieved an engine action entity " +
                                      "that has no properties.  The handler does not know how to proceed.",
                                     "The error is likely to be in one of the repository connectors " +
                                      "or the governance engines managing the engine action entities.",
                                      "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-009 - An anchor GUID of "unknown" has been passed to local method {0} by the {1} service through method {2}
     */
    UNKNOWN_ANCHOR_GUID(500, "OMAG-GENERIC-HANDLERS-500-009",
                                         "An anchor GUID of <unknown> has been passed to local method {0} by the {1} service through method {2}",
                                         "The system cannot process the request because the handler has an invalid anchor GUID.",
                                         "Gather diagnostics and add them to issue #4680.",
                                         "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-011 - An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_ENTITY(500, "OMAG-GENERIC-HANDLERS-500-011",
                     "An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system cannot format all or part of the response because the repositories have returned an invalid entity.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  " +
                             "There is probably an error in the implementation of the repository that originated the entity.",
                             "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-500-013 - A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_RELATIONSHIP(500, "OMAG-GENERIC-HANDLERS-500-013",
                     "A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system cannot format all or part of the response because the repositories have returned an invalid relationship.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  " +
                             "There is probably an error in the implementation of the repository that originated the relationship.",
                             "https://egeria-project.org/services/generic-handlers/"),

    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;
    private final String url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GenericHandlersErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    GenericHandlersErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                              userAction,
                                              url);
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
                                                                                      userAction,
                                                                                      url);

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
                       ", url='" + url + '\'' +
                       '}';
    }
}
