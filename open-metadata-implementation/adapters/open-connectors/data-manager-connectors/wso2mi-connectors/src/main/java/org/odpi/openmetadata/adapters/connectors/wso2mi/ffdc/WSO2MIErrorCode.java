/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The WSO2MIErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the WSO2 Micro Integrator connectors.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA.  Typical values are 500 (internal error),
 *     400 (invalid parameters), 404 (not found) and 409 (data conflict).</li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum WSO2MIErrorCode implements ExceptionMessageSet
{
    /**
     * WSO2MI-CONNECTOR-400-001 - Connection {0} has been configured without a network address for the WSO2 Micro Integrator Management API
     */
    NULL_URL(400, "WSO2MI-CONNECTOR-400-001",
             "Connection {0} has been configured without a network address for the WSO2 Micro Integrator Management API",
             "The connector cannot start because it does not know where to send Management API requests.",
             "Update the connection's endpoint with the base URL of the WSO2 Micro Integrator Management API, for example https://localhost:9164/management.",
             "https://egeria-project.org/connectors/"),

    /**
     * WSO2MI-CONNECTOR-401-001 - The {0} connector could not authenticate with the WSO2 Micro Integrator Management API at {1}; the login call returned no access token
     */
    NO_ACCESS_TOKEN(401, "WSO2MI-CONNECTOR-401-001",
             "The {0} connector could not authenticate with the WSO2 Micro Integrator Management API at {1}; the login call returned no access token",
             "The connector cannot make Management API calls because it has no bearer token.",
             "Check that the configured user name and password are valid for the Management API and that the /management/login resource is enabled.",
             "https://egeria-project.org/connectors/"),

    /**
     * WSO2MI-CONNECTOR-500-001 - The {0} WSO2 Micro Integrator connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "WSO2MI-CONNECTOR-500-001",
             "The {0} WSO2 Micro Integrator connector received an unexpected exception {1} during method {2}; the error message was: {3}",
             "The connector cannot process the current request.",
             "Use the details from the error message to determine the cause of the error and retry the request once it is resolved.",
             "https://egeria-project.org/connectors/"),

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
     * @param httpErrorCode  error code to use over REST calls
     * @param errorMessageId unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction     instructions for resolving the error
     */
    WSO2MIErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * Constructor.
     *
     * @param httpErrorCode  error code to use over REST calls
     * @param errorMessageId unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction     instructions for resolving the error
     * @param url            link to a page that describes the component or concept behind this message
     */
    WSO2MIErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.httpErrorCode  = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage   = errorMessage;
        this.systemAction   = systemAction;
        this.userAction     = userAction;
        this.url            = url;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, url);
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
     * JSON-style toString.
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "WSO2MIErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
