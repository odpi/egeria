/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The ServerAuthorViewErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMVS
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
public enum ServerAuthorViewErrorCode implements ExceptionMessageSet
{
    SERVICE_NOT_INITIALIZED(404, "OMVS-SERVER_AUTHOR-400-001",
                           "The Server Author Open Metadata View Service (OMVS) has not been initialized.",
                           "The system is unable to perform the request because the service has not been initialized.",
                           "Initialize the view service retry the request."),

    INVALID_CONFIG_PROPERTY(404, "OMVS-SERVER_AUTHOR-400-002",
                            "The Server Author Open Metadata View Service (OMVS) configuration has an invalid or missing property, property name {0}.",
                            "The service is unable to initialize because the configuration is not valid or complete.",
                            "Correct the view service configuration and restart the view server."),

    VIEW_SERVICE_NULL_PLATFORM_NAME(400, "OMVS-SERVER_AUTHOR-400-003",
                                            "The Server Author Open Metadata View Service (OMVS) has been called with a null platform name",
                                            "The system is unable to resolve the platform to query without knowing what it is called.",
                                            "The platform name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_NULL_SERVER_NAME(400, "OMVS-SERVER_AUTHOR-400-004",
                                          "The Server Author Open Metadata View Service (OMVS) has been called with a null server name",
                                          "The system is unable to resolve the server to query without knowing what it is called.",
                                          "The server name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

   VIEW_SERVICE_INACTIVE_SERVER_NAME(400, "OMVS-SERVER_AUTHOR-400-005",
                                      "The Server Author Open Metadata View Service (OMVS)'s cannot run {0}, because the view server with name {1} is not active, please contact your Platform administrator to start server {1}.",
                                      "The system is complete the call, because the view server is not active.",
                                     "The server name is supplied by the caller to the OMAG view service. Please ensure the named server is active and retry the call to the view service."),

    UNEXPECTED_EXCEPTION(400,"OMVS-SERVER_AUTHOR-400-006",
                                            "An unexpected Exception occurred. The Exception message is {0}.",
                                            "The system encountered an unexpected exception.",
                                            "Review the exception message to assess whether this is a logic error, in which case raise a git issue against Egeria, or a resource constraint - in which case address that issue."),

    INVALID_PARAMETER(400, "OMVS-SERVER_AUTHOR-400-007",
             "The server author view service operation {0} could not proceed with the setting of parameter {1}",
             "The system detected that the parameter was not set to a valid value and could not perform the requested action.",
             "Correct the parameter setting and retry the operation."),

    USER_NOT_AUTHORIZED(400, "OMVS-SERVER_AUTHOR-400-008",
             "The server author view service was not authorized to perform the requested operation {0}",
             "The system reported that  server author view service was not authorized to perform the requested action.",
             "Contact your platform administrator to configure the view service with appropriate credentials."),
    CONFIG_ERROR(400, "OMVS-SERVER_AUTHOR-400-009",
             "The server author view service requested an invalid configuration change in operation {0}",
             "The system reported that server author view service requested configuration change was invalid.",
             "Correct the requested configuration change and retry the operation."),
    ;


    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for RexViewErrorCode expects to be passed one of the enumeration rows defined in
     * ServerAuthorViewErrorCode above.   For example:
     *
     *     ServerAuthorViewErrorCode   errorCode = ServerAuthorViewErrorCode.SERVICE_NOT_INITIALIZED;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    ServerAuthorViewErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "ServerAuthorViewErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
