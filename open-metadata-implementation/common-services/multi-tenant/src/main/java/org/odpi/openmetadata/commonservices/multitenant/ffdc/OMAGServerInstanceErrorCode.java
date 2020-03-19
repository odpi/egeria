/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OMAGServerInstanceErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with OMAG Server instances within the OMAG Server Platform
 * It is used in conjunction with all multi-tenant exceptions, both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA   Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OMAGServerInstanceErrorCode implements ExceptionMessageSet
{
    BAD_SERVER_SECURITY_CONNECTION(400, "OMAG-MULTI-TENANT-400-001",
            "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
            "The system is unable to validate the users issuing requests to this server.",
            "Review the error message to determine the cause of the problem."),

    SERVICES_NOT_SHUTDOWN(400, "OMAG-MULTI-TENANT-400-002",
            "The OMAG server {0} has been requested to shutdown but the following services are still running: {1}",
            "The system is unable to shutdown the server correctly.",
            "Review other error messages to determine the cause of the problem.  This is likely to be a logic error in the services listed in the message"),

    SERVER_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-001",
            "The OMAG Server {0} is not available to service a request from user {1}",
            "The system is unable to process the request because the server is not running.",
            "Retry the request when the OMAG Server is available."),

    SERVICE_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-002",
            "The {0} service is not available on OMAG Server {1} to handle a request from user {2}",
            "The system is unable to process the request because the service is not available.",
            "Verify that the correct server is being called on the correct platform and that the requested service is configured to run there.  " +
                                  "Once the correct environment is in place, retry the request."),

    SERVER_NAME_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-003",
            "The server name is not available for the {0} operation",
            "The system is unable to return the server name because it is not available.",
            "Check that the server where the access service is running initialized correctly.  " +
                            "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_INITIALIZED(404, "OMAG-MULTI-TENANT-404-004",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata repository services because they are not running in this server.",
            "Check that the server where the called service is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_AVAILABLE(404, "OMAG-MULTI-TENANT-404-005",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata repository services because they are not in the correct state to be called.",
            "Check that the server where the called service is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata repository services are available."),
    ;

    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for RepositoryHandlerErrorCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineErrorCode above.   For example:
     *
     *     RepositoryHandlerErrorCode   errorCode = RepositoryHandlerErrorCode.ASSET_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OMAGServerInstanceErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "OMAGServerInstanceErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
