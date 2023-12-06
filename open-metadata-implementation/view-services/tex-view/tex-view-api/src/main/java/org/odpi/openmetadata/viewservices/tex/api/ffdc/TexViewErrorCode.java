/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The TexViewErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMVS
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
public enum TexViewErrorCode implements ExceptionMessageSet
{
    SERVICE_NOT_INITIALIZED
            (404, "OMVS-TYPE-EXPLORER-400-001",
             "The Type Explorer Open Metadata View Service (OMVS) has not been initialized.",
             "The system is unable to perform the request because the service has not been initialized.",
             "Initialize the view service retry the request."),

    INVALID_CONFIG_PROPERTY
            (404, "OMVS-TYPE-EXPLORER-400-002",
             "The Type Explorer Open Metadata View Service (OMVS) configuration has an invalid or missing property, property name {0}.",
             "The service is unable to initialize because the configuration is not valid or complete.",
             "Correct the view service configuration and restart the view server."),

    VIEW_SERVICE_NULL_PLATFORM_NAME
            (400, "OMVS-TYPE-EXPLORER-400-003",
             "The Type Explorer Open Metadata View Service (OMVS) has been called with a null platform name",
             "The system is unable to resolve the platform to query without knowing what it is called.",
             "The platform name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_NULL_SERVER_NAME
            (400, "OMVS-TYPE-EXPLORER-400-004",
             "The Type Explorer Open Metadata View Service (OMVS) has been called with a null server name",
             "The system is unable to resolve the server to query without knowing what it is called.",
             "The server name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_UNKNOWN_SERVER_NAME
            (400, "OMVS-TYPE-EXPLORER-400-005",
             "The Type Explorer Open Metadata View Service (OMVS)'s {0} method has been called with an unknown server name of {1}",
             "The system is unable to resolve the server name.",
             "The server name is supplied by the caller to the OMAG view service. Please ensure a known server name is passed and retry the call to the view service."),

    USER_NOT_AUTHORIZED
            (400, "OMVS-TYPE-EXPLORER-400-006",
             "The type explorer view service could not perform the requested operation {0} on behalf of user {1}",
             "The system reported that the user is not authorized to perform the requested action.",
             "Request that the name used to log in to the UI is given authority to perform the request."),

    REPOSITORY_NOT_AVAILABLE
            (400, "OMVS-TYPE-EXPLORER-400-007",
             "The type explorer view service operation {0} found that server {1} is not available",
             "The system reported that the server is not available to perform the requested action.",
             "Retry the request when the server is available."),

    PLATFORM_NOT_AVAILABLE
            (400, "OMVS-TYPE-EXPLORER-400-008",
             "The type explorer view service operation {0} found that the platform for server {1} is not available",
             "The system reported that the platform is not reachable using the provided URL.",
             "Check the platform is running and check the type explorer resource endpoint configuration for the server and its platform."),

    REPOSITORY_ERROR
            (400, "OMVS-TYPE-EXPLORER-400-009",
             "The type explorer view service operation {0} encountered a problem with the repository, the message is {1}",
             "The system could not complete a repository operation due to the error indicated.",
             "Look at the error message and check the configuration and state of the repository server."),

    INVALID_PARAMETER
            (400, "OMVS-TYPE-EXPLORER-400-010",
             "The type explorer view service operation {0} could not proceed with the setting of parameter {1}",
             "The system detected that the parameter was not set to a valid value and could not perform the requested action.",
             "Check the parameter setting and retry the operation."),

    VIEW_SERVICE_REQUEST_BODY_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-011",
             "The type explorer view service could not perform the requested operation {0} because te requestBody was missing",
             "The system could not perform the requested action.",
             "Check the calling code to make sure that a requestBody is passed to the view service API."),

    TYPE_SYSTEM_ENTITY_DEF_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-012",
             "The type explorer view service method {0} detected that entity type {1} does not have an EntityDef, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_ENTITY_SUPERTYPE_NAME_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-013",
             "The type explorer view service method {0} detected that entity type {1} has a super type with no name, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_ENTITY_SUPERTYPE_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-014",
             "The type explorer view service method {0} detected that entity type {1} refers a super type {2} that is not in the type gallery, as returned by repository {3} on platform {4}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_RELATIONSHIP_DEF_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-015",
             "The type explorer view service method {0} detected that relationship type {1} does not have a RelationshipDef, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_RELATIONSHIP_END_DEF_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-016",
             "The type explorer view service method {0} detected that relationship type {1} is missing a RelationshipEndDef, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_CLASSIFICATION_DEF_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-017",
             "The type explorer view service method {0} detected that classification type {1} does not have a ClassificationDef, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_CLASSIFICATION_VALID_ENTITY_NAME_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-018",
             "The type explorer view service method {0} detected that the classification type {1} refers to a validEntityDef that has no name, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_CLASSIFICATION_VALID_ENTITY_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-019",
             "The type explorer view service method {0} detected that classification type {1} refers to a validEntityDef for entity type {2} that is not in the type gallery, as returned by repository {3} on platform {4}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_CLASSIFICATION_SUPERTYPE_NAME_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-020",
             "The type explorer view service method {0} detected that the classification type {1} refers to a supertype that has no name, as returned by repository {2} on platform {3}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery."),

    TYPE_SYSTEM_CLASSIFICATION_SUPERTYPE_MISSING
            (400, "OMVS-TYPE-EXPLORER-400-021",
             "The type explorer view service method {0} detected that the classification type {1} refers to a supertype {2} that is not in the type gallery, as returned by repository {3} on platform {4}",
             "The system could not resolve the type system.",
             "Check the type system returned by the repository's getAllTypes method returns a complete and consistent TypeDefGallery.")

            ;

    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for RexViewErrorCode expects to be passed one of the enumeration rows defined in
     * TexViewErrorCode above.   For example:
     *
     *     RexViewErrorCode   errorCode = TexViewErrorCode.SERVICE_NOT_INITIALIZED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    TexViewErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "TexViewErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
