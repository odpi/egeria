/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The DinoViewErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMVS
 * It is used in conjunction with all OMVS Exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA. Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500: internal error</li>
 *         <li>501: not implemented </li>
 *         <li>503: Service not available</li>
 *         <li>400: invalid parameters</li>
 *         <li>401: unauthorized</li>
 *         <li>404: not found</li>
 *         <li>405: method not allowed</li>
 *         <li>409: data conflict errors, for example an item is already defined</li>
 *     </ul></li>
 *     <li>Error Message Id: to uniquely identify the message</li>
 *     <li>Error Message Text: includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction: describes the result of the error</li>
 *     <li>UserAction: describes how a user should correct the error</li>
 * </ul>
 */
public enum DinoViewErrorCode implements ExceptionMessageSet
{
    SERVICE_NOT_INITIALIZED
            (404, "OMVS-DINO-400-001",
             "The Dino Open Metadata View Service (OMVS) has not been initialized.",
             "The system is unable to perform the request because the service has not been initialized.",
             "Initialize the view service retry the request."),

    INVALID_CONFIG_PROPERTY
            (404, "OMVS-DINO-400-002",
             "The Dino Open Metadata View Service (OMVS) configuration has an invalid or missing property, property name {0}.",
             "The service is unable to initialize because the configuration is not valid or complete.",
             "Correct the view service configuration and restart the view server."),

    VIEW_SERVICE_NULL_PLATFORM_NAME
            (400, "OMVS-DINO-400-003",
             "The Dino Open Metadata View Service (OMVS) has been called with a null platform name",
             "The system is unable to resolve the platform to query without knowing what it is called.",
             "The platform name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_NULL_SERVER_NAME
            (400, "OMVS-DINO-400-004",
             "The Dino Open Metadata View Service (OMVS) has been called with a null server name",
             "The system is unable to resolve the server to query without knowing what it is called.",
             "The server name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_UNKNOWN_SERVER_NAME
            (400, "OMVS-DINO-400-005",
             "The Dino Open Metadata View Service (OMVS)'s {0} method has been called with an unknown server name of {1}",
             "The system is unable to resolve the server name.",
             "The server name is supplied by the caller to the OMAG view service. Please ensure a known server name is passed and retry the call to the view service."),

    USER_NOT_AUTHORIZED
            (400, "OMVS-REPOSITORY-EXPLORER-400-004",
             "The dino view service could not perform the requested operation {0} on behalf of user {1}",
             "The system reported that the user is not authorized to perform the requested action.",
             "Request that the name used to log in to the UI is given authority to perform the request."),

    REPOSITORY_NOT_AVAILABLE
            (400, "OMVS-DINO-400-005",
             "The dino view service operation {0} found that server {1} is not available",
             "The system reported that the server is not available to perform the requested action.",
             "Retry the request when the server is available."),

    PLATFORM_NOT_AVAILABLE
            (400, "OMVS-DINO-400-006",
             "The dino view service operation {0} could not reach platform {1}",
             "The system reported that the platform is not reachable using the provided URL.",
             "Check the platform is running and check the dino resource endpoint configuration for the server and its platform."),

    REPOSITORY_ERROR
            (400, "OMVS-DINO-400-007",
             "The dino view service operation {0} encountered a problem connecting to the repository, the message is {1}",
             "The system could not complete a repository operation due to the error indicated.",
             "Look at the error message and check the configuration and state of the repository server."),

    INVALID_PARAMETER
            (400, "OMVS-DINO-400-008",
             "The dino view service operation {0} could not proceed with the setting of parameter {1}",
             "The system detected that the parameter was not set to a valid value and could not perform the requested action.",
             "Check the parameter setting and retry the operation."),

    ENTITY_NOT_KNOWN_IN_REPOSITORY
            (400, "OMVS-DINO-400-009",
             "The dino view service operation {0} found no entity with guid {1} in repository {2}",
             "The system reported that the entity is not available in the specified repository.",
             "If you expect that the entity exists, check the GUID is correct, try a server running a repository with the home instance or reference copy, or enable the enterprise option."),

    ENTITY_NOT_KNOWN_IN_ENTERPRISE
            (400, "OMVS-DINO-400-010",
             "The dino view service operation {0} found no entity with guid {1} at enterprise scope for repository {2}",
             "The system reported that the entity is not available at enterprise scope for the specified repository server.",
             "If you expect that the entity exists, check the GUID is correct and the selected repository server is in the cohort where you would expect to find the entity."),

    ENTITY_PROXY_ONLY
            (400, "OMVS-DINO-400-011",
             "The dino view service operation {0} reported that an entity is only available as a proxy - {1}",
             "The system reported that the entity is only available as a proxy object.",
             "Try to retrieve the entity detail from a server running a repository with the home instance or reference copy, or enable the enterprise option."),

    RELATIONSHIP_NOT_KNOWN_IN_REPOSITORY
            (400, "OMVS-DINO-400-012",
             "The dino view service operation {0} found no relationship with guid {1} in repository {2}",
             "The system reported that the relationship is not available in the specified repository.",
             "If you expect that the relationship exists, check the GUID is correct, try a server running a repository with the home instance or reference copy, or enable the enterprise option."),

    RELATIONSHIP_NOT_KNOWN_IN_ENTERPRISE
            (400, "OMVS-DINO-400-013",
             "The dino view service operation {0} found no relationship with guid {1} at enterprise scope for repository {2}",
             "The system reported that the relationship is not available at enterprise scope for the specified repository server.",
             "If you expect that the relationship exists, check the GUID is correct and the selected repository server is in the cohort where you would expect to find the relationship."),

    VIEW_SERVICE_REQUEST_BODY_MISSING
            (400, "OMVS-DINO-400-014",
             "The dino view service could not perform the requested operation {0} because te requestBody was missing",
             "The system could not perform the requested action.",
             "Check the calling code to make sure that a requestBody is passed to the view service API."),

    TYPE_ERROR
            (400, "OMVS-DINO-400-015",
             "The dino view service operation {0} reported a type error - {1}",
             "The system could not perform the requested action.",
             "Check the calling code to make sure that a valid type is passed to the view service API."),

    PROPERTY_ERROR
            (400, "OMVS-DINO-400-016",
             "The dino view service operation {0} reported a property error - {1}",
             "The system could not perform the requested action with the supplied properties.",
             "Check the detailed message and check the properties supplied to the operation."),

    PAGING_ERROR
            (400, "OMVS-DINO-400-017",
             "The dino view service operation {0} reported a paging error - {1}",
             "The system could not perform the requested action.",
             "Check the calling code to make sure that the requested operation is valid."),

    FUNCTION_NOT_SUPPORTED_ERROR
            (400, "OMVS-DINO-400-018",
             "The dino view service could not perform the requested operation {0} because it is not supported by repository {1}",
             "The system could not perform the requested action.",
             "Check the conformance of the target repository and retry with a supported operation."),

    COULD_NOT_CREATE_HANDLER
            (400, "OMVS-DINO-400-019",
             "The dino view service could not instantiate its handler",
             "The system could not perform the requested action.",
             "Check the configuration of the view service and its platform."),

    COULD_NOT_RETRIEVE_SERVER_CONFIGURATION
            (400, "OMVS-DINO-400-020",
             "The dino view service operation {0} could not retrieve the configuration for server {1}",
             "The system could not perform the requested action.",
             "Check the configuration of the view service and its platform."),

    COULD_NOT_RETRIEVE_SERVICE_CONFIGURATION
            (400, "OMVS-DINO-400-021",
             "The dino view service operation {0} could not retrieve the configuration for service {1}",
             "The system could not perform the requested action.",
             "Check the configuration of the view service and its platform."),

    COULD_NOT_RETRIEVE_GOVERNANCE_ENGINE
            (400, "OMVS-DINO-400-022",
             "The dino view service operation {0} could not find a governance engine with qualified name {1}",
             "The system could not perform the requested action.",
             "Please check the supplied engine name matches the engine service's engine configuration."),

    /*
     * Provide a catch-all error code in case Rex catches a Throwable. It will capture the Throwable
     * and package it into the REST response, but does not specific error handling for the error
     */
    UNKNOWN_ERROR
            (400, "OMVS-DINO-400-099",
             "The dino view service caught an unknown error during operation {0}. The detailed message is {1}",
             "The system could not perform the requested action.",
             "Please check the detailed message and the system log.")

    ;


    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for DinoViewErrorCode expects to be passed one of the enumeration rows defined in
     * DinoViewErrorCode above.   For example:
     *
     *     DinoViewErrorCode   errorCode = DinoViewErrorCode.SERVICE_NOT_INITIALIZED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    DinoViewErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "DinoViewErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
