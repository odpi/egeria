/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

public enum SecurityOfficerErrorCode implements ExceptionMessageSet {

    OMRS_NOT_INITIALIZED(404, "OMAS-SECURITY-OFFICER-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    OMRS_NOT_AVAILABLE(404, "OMAS-SECURITY-OFFICER-404-002 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Security Officer OMAS is running initialized correctly and is not in the process of shutting down.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),

    NO_METADATA_COLLECTION(404, "OMAS-SECURITY-OFFICER-404-003 ",
            "The repository connector {0} is not returning a metadata collection object",
            "The system is unable to access any metadata.",
            "Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator."),

    SERVICE_NOT_INITIALIZED(503, "OMAS-SECURITY-OFFICER-503-001 ",
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),

    NULL_TOPIC_CONNECTOR(400, "SECURITY-OFFICER-400-001",
                         "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
                         "The local server will not connect to the cohort.",
                         "The connection to the connector is configured in the server configuration.  " +
                                 "Review previous error messages to determine the precise error in the " +
                                 "start up configuration. " +
                                 "Correct the configuration and reconnect the server to the cohort. "),

    NULL_LISTENER(400, "OMAS-SECURITY-OFFICER-400-017",
                  "A null topic listener has been passed by user {0} on method {1}",
                  "There is a coding error in the caller to the Governance Engine OMAS.",
                  "Correct the caller logic and retry the request."),

    UNABLE_TO_SEND_EVENT(500, "OMAS-SECURITY-OFFICER-500-004",
                         "An unexpected exception occurred when sending an event through connector {0} to the Security Officer OMAS out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {2}",
                         "The access service has issued a call to publish an event on its Out Topic and it failed.",
                         "Look for errors in the event bus to understand why this is failing.  When the event bus is operating correctly, the event will" +
                                 " be able to be published again.  In the meantime, events are being lost."),

    UNEXPECTED_INITIALIZATION_EXCEPTION(503, "OMAS-SECURITY-OFFICER-503-005",
                                        "A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}",
                                        "The system detected an unexpected error during start up and is now in an unknown start.",
                                        "The error message should indicate the cause of the error.  Otherwise look for errors in the " +
                                                "remote server's audit log and console to understand and correct the source of the error."),

    UNEXPECTED_REPOSITORY_EXCEPTION(503,"OMAS-SECURITY-OFFICER-503-006",
                                    "A {0} exception was caught by method {1} during a call to the repository services by service {2} for server {3}. The error message was: {4}",
                                    "The system detected an unexpected exception during a call to the repository.",
                                    "The error message should indicate the cause of the error.  Otherwise look for other errors in the " +
                                            "audit log of both the local server and other members of the cohort."),
    ;





    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for SecurityOfficerErrorCode expects to be passed one of the enumeration rows defined in
     * SecurityOfficerErrorCode above.   For example:
     *
     *     SecurityOfficerErrorCode   errorCode = SecurityOfficerErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    SecurityOfficerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
        return "SecurityOfficerErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}