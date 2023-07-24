/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OpenMetadataStoreErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Governance Action Framework (GAF) Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Identifier - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a GovernanceEngineInterface should correct the error</li>
 * </ul>
 */
public enum OpenMetadataStoreErrorCode implements ExceptionMessageSet
{
    OMRS_NOT_INITIALIZED(404, "OPEN-METADATA-STORE-404-002",
                         "The open metadata repository services are not initialized for the {0} operation",
                         "The system is unable to connect to the open metadata property server.",
                         "Check that the server where the Open Metadata Store Services are running initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),
    
    UNEXPECTED_INITIALIZATION_EXCEPTION(503, "OPEN-METADATA-STORE-503-005",
                                        "A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}",
                                        "The system detected an unexpected error during start up and is now in an unknown state.",
                                        "The error message should indicate the cause of the error.  Otherwise look for errors in the " +
                                                "remote server's audit log and console to understand and correct the source of the error.");



    private final ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for OpenMetadataStoreErrorCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataStoreErrorCode above.   For example:
     *
     *     OpenMetadataStoreErrorCode   errorCode = OpenMetadataStoreErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OpenMetadataStoreErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataStoreErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
