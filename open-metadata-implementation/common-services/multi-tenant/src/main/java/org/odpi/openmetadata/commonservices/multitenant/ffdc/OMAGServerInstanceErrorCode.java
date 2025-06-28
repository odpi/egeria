/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OMAGServerInstanceErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with OMAG Server instances within the OMAG Server Platform
 * It is used in conjunction with all multi-tenant exceptions, both Checked and Runtime (unchecked).
 * <br><br>
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA   Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OMAGServerInstanceErrorCode implements ExceptionMessageSet
{
    /**
     * OMAG-MULTI-TENANT-400-001 - The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}
     */
    BAD_SERVER_SECURITY_CONNECTION(400, "OMAG-MULTI-TENANT-400-001",
            "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
            "The system is unable to validate the users issuing requests to this server.",
            "Review the error message to determine the cause of the problem."),


    /**
     * OMAG-MULTI-TENANT-400-002 - The OMAG server {0} has been requested to shut down but the following services are still running: {1}
     */
    SERVICES_NOT_SHUTDOWN(400, "OMAG-MULTI-TENANT-400-002",
            "The OMAG server {0} has been requested to shutdown but the following services are still running: {1}",
            "The system is unable to shutdown the server correctly.",
            "Review other error messages to determine the cause of the problem.  This is likely to be a logic error in the services listed in the message"),

    /**
     * OMAG-MULTI-TENANT-400-003 - Method {0} called on behalf of the {1} service is unable to create a client-side open metadata topic connection because the topic name is not configured in the configuration for this service.
     */
    NO_TOPIC_INFORMATION(400,"OMAG-MULTI-TENANT-400-003",
                         "Method {0} called on behalf of the {1} service is unable to create a client-side open " +
                                 "metadata topic connection because the topic name is not configured in the configuration for this service.",
                         "This is a configuration error and an exception is sent to the requester.",
                         "Correct the configuration of the access service to include the name of the topic."),

    /**
     * OMAG-MULTI-TENANT-400-004 - The connector provider class name {0} does not create a connector of class {1} which is required for the {2}
     */
    NOT_CORRECT_CONNECTOR_PROVIDER(400,"OMAG-MULTI-TENANT-400-004",
                                   "The connector provider class name {0} does not create a connector of class {1} which is required for the {2}",
                                   "An invalid parameter exception is returned to the caller.",
                                   "Either change the connector or the hosting environment because the current combination is not compatible."),

    /**
     * OMAG-MULTI-TENANT-400-005 - The URL marker {0} is not recognized
     */
    INVALID_URL_MARKER(400, "OMAG-MULTI-TENANT-400-005",
                       "The URL marker {0} is not recognized",
                       "The system is unable to continue with the request because the supplied URL marker does not match the registered services.",
                       "Update the parameters passed on the request to either remove the URL marker, or set it to a URL marker that is recognized by the OMAG Server Platform."),

    /**
     * OMAG-MULTI-TENANT-404-001 - The OMAG Server {0} is not available to service a request from user {1}
     */
    SERVER_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-001",
                         "The OMAG Server {0} is not available to service a request from user {1}",
                         "The system is unable to process the request because the server is not running on the called platform.",
                         "Verify that the correct server is being called on the correct platform and that this server is running. " +
                                 "Retry the request when the server is available."),

    /**
     * OMAG-MULTI-TENANT-404-002 - The {0} service is not available on OMAG Server {1} to handle a request from user {2}
     */
    SERVICE_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-002",
            "The {0} service is not available on OMAG Server {1} to handle a request from user {2}",
            "The system is unable to process the request because the service is not available.",
            "Verify that the correct server is being called on the correct platform and that the requested service is configured to run there.  " +
                                  "Once the correct environment is in place, retry the request."),

    /**
     * OMAG-MULTI-TENANT-404-003 - The server name is not available for the {0} operation
     */
    SERVER_NAME_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-003",
            "The server name is not available for the {0} operation",
            "The system is unable to return the server name because it is not available.",
            "Check that the server where the access service is running initialized correctly.  " +
                            "Correct any errors discovered and retry the request when the open metadata services are available."),

    /**
     * OMAG-MULTI-TENANT-404-004 - The open metadata repository services are not initialized for the {0} operation
     */
    OMRS_NOT_INITIALIZED(404, "OMAG-MULTI-TENANT-404-004",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata repository services because they are not running in this server.",
            "Check that the server where the called service is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    /**
     * OMAG-MULTI-TENANT-404-005 - The open metadata repository services are not available for the {0} operation
     */
    OMRS_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-005",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository services because they are not in the correct state to be called.",
            "Check that the server where the called service is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata repository services are available."),

    /**
     * OMAG-MULTI-TENANT-500-001 - An unsupported bean class named {0} was passed to the OMAG Server Platform by the {1} request for
     * open metadata view service {2} on server {3}; error message was: {4}
     */
    INVALID_BEAN_CLASS(500, "OMAG-MULTI-TENANT-500-001",
                       "An unsupported bean class named {0} was passed to the OMAG Server Platform by the {1} request for open metadata view service {2} on " +
                               "server {3}; error message was: {4}",
                       "The system is unable to process the request because it is not able to instantiate the bean.",
                       "Correct the code that initializes the converter during server start up."),


    /**
     * OMAG-MULTI-TENANT-500-003 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3}
     */
    BAD_TOPIC_CONNECTOR_PROVIDER(500, "OMAG-MULTI-TENANT-500-003",
                                 "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
                                 "This is an internal error.  The access service is not using a valid connector provider.",
                                 "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve."),


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
    OMAGServerInstanceErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
