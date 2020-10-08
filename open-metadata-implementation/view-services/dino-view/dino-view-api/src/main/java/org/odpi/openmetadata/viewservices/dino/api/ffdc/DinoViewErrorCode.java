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
    SERVICE_NOT_INITIALIZED(404, "OMVS-DINO-400-001",
                           "The Dino Open Metadata View Service (OMVS) has not been initialized.",
                           "The system is unable to perform the request because the service has not been initialized.",
                           "Initialize the view service retry the request."),

    INVALID_CONFIG_PROPERTY(404, "OMVS-DINO-400-002",
                            "The Dino Open Metadata View Service (OMVS) configuration has an invalid or missing property, property name {0}.",
                            "The service is unable to initialize because the configuration is not valid or complete.",
                            "Correct the view service configuration and restart the view server."),

    VIEW_SERVICE_NULL_PLATFORM_NAME(400, "OMVS-DINO-400-003",
                                            "The Dino Open Metadata View Service (OMVS) has been called with a null platform name",
                                            "The system is unable to resolve the platform to query without knowing what it is called.",
                                            "The platform name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_NULL_SERVER_NAME(400, "OMVS-DINO-400-004",
                                          "The Dino Open Metadata View Service (OMVS) has been called with a null server name",
                                          "The system is unable to resolve the server to query without knowing what it is called.",
                                          "The server name is supplied by the caller to the OMAG view service. This call needs to be corrected before the view service can perform the request."),

    VIEW_SERVICE_UNKNOWN_SERVER_NAME(400, "OMVS-DINO-400-005",
                                             "The Dino Open Metadata View Service (OMVS)'s {0} method has been called with an unknown server name " +
                                                     "of {1}",
                                             "The system is unable to resolve the server name.",
                                             "The server name is supplied by the caller to the OMAG view service. Please ensure a known server name is passed and retry the call to the view service."),

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
