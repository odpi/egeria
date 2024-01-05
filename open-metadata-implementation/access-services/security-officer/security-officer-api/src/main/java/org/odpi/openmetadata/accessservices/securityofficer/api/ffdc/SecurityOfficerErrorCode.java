/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

public enum SecurityOfficerErrorCode implements ExceptionMessageSet
{
    /**
     * OMAS-SECURITY-OFFICER-404-001 - The open metadata repository services are not initialized for server {0}
     */
    OMRS_NOT_INITIALIZED(404, "OMAS-SECURITY-OFFICER-404-001",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    /**
     * OMAS-SECURITY-OFFICER-404-003 - The repository connector {0} is not returning a metadata collection object"
     */
    NO_METADATA_COLLECTION(404, "OMAS-SECURITY-OFFICER-404-003",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),

    /**
     * OMAS-SECURITY-OFFICER-400-001 - Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize
     */
    NULL_TOPIC_CONNECTOR(400, "OMAS-SECURITY-OFFICER-400-001",
                         "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
                         "The local server will not connect to the cohort.",
                         "The connection to the connector is configured in the server configuration.  " +
                                 "Review previous error messages to determine the precise error in the " +
                                 "start up configuration. " +
                                 "Correct the configuration and reconnect the server to the cohort. "),

    /**
     * OMAS-SECURITY-OFFICER-400-017 - A null topic listener has been passed by user {0} on method {1}
     */
    NULL_LISTENER(400, "OMAS-SECURITY-OFFICER-400-017",
                  "A null topic listener has been passed by user {0} on method {1}",
                  "There is a coding error in the caller to the Governance Engine OMAS.",
                  "Correct the caller logic and retry the request."),

    /**
     * OMAS-SECURITY-OFFICER-500-004 - An unexpected exception occurred when sending an event through connector {0}
     * to the Security Officer OMAS out topic.  The failing event was {1}, the exception was {2} with message {2}
     */
    UNABLE_TO_SEND_EVENT(500, "OMAS-SECURITY-OFFICER-500-004",
                         "An unexpected exception occurred when sending an event through connector {0} to the Security Officer OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {2}",
                         "The access service has issued a call to publish an event on its Out Topic and it failed.",
                         "Look for errors in the event bus to understand why this is failing.  When the event bus is operating correctly, the event will" +
                                 " be able to be published again.  In the meantime, events are being lost."),

    /**
     * OMAS-SECURITY-OFFICER-503-005 - A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}
     */
    UNEXPECTED_INITIALIZATION_EXCEPTION(503, "OMAS-SECURITY-OFFICER-503-005",
                                        "A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}",
                                        "The system detected an unexpected error during start up and is now in an unknown start.",
                                        "The error message should indicate the cause of the error.  Otherwise look for errors in the " +
                                                "remote server's audit log and console to understand and correct the source of the error."),

    /**
     * OMAS-SECURITY-OFFICER-503-006 - A {0} exception was caught by method {1} during a call to the repository services by service {2} for server {3}. The error message was: {4}
     */
    UNEXPECTED_REPOSITORY_EXCEPTION(503,"OMAS-SECURITY-OFFICER-503-006",
                                    "A {0} exception was caught by method {1} during a call to the repository services by service {2} for server {3}. The error message was: {4}",
                                    "The system detected an unexpected exception during a call to the repository.",
                                    "The error message should indicate the cause of the error.  Otherwise look for other errors in the " +
                                            "audit log of both the local server and other members of the cohort."),
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
    SecurityOfficerErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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