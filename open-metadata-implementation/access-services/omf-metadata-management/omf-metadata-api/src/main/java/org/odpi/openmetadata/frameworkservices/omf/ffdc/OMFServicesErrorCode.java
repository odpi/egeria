/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OMFServicesErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Open Metadata Framework (OMF) Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
public enum OMFServicesErrorCode implements ExceptionMessageSet
{
    /**
     * OMF-SERVICES-400-011 - The permitted synchronization direction of {0} does not allow connector {1} to issue {2} requests
     */
    NOT_PERMITTED_SYNCHRONIZATION(400,"OMF-SERVICES-400-011",
                                  "The permitted synchronization direction of {0} does not allow connector {1} to issue {2} requests",
                                  "The request is not issued and an exception is returned to the caller.",
                                  "The request was disabled through the administration calls for the integration connector." +
                                          "Either change the configuration of the integration service or change the connector to skip the part of the " +
                                          "synchronization that uses this request since the organization does not want this type of metadata synchronized."),

    /**
     * OMF-SERVICES-404-001 - The open metadata repository services are not initialized for the {0} operation
     */
    OMRS_NOT_INITIALIZED(404, "OMF-SERVICES-404-001",
                         "The open metadata repository services are not initialized for the {0} operation",
                         "The system is unable to connect to the open metadata property server.",
                         "Check that the server where the Open Metadata Store Services are running initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),


    /**
     * OMF-SERVICES-409-001 - Multiple {0} relationships are attached to {1} metadata element {2}
     */
    MULTIPLE_RELATIONSHIPS_FOUND(404, "OMF-SERVICES-409-001",
                         "Multiple {0} relationships are attached to metadata element {1}",
                         "This relationship type is a singleton, which means that only once relationship of this type can be attached to an element.  The system is unable to retrieve the singleton relationship because there are more than one relationship defined.",
                         "Using a different method, retrieve all of the relationships of this type for this element and either delete/archive the relationships no longer needed, or adjust their effectivity date(s) so that only one relationship is effective at any one time."),


    /**
     * OMF-SERVICES-500-001 A null topic listener has been passed by user {0} on method {1}
     */
    NULL_LISTENER(500, "OMF-SERVICES-500-001",
                  "A null topic listener has been passed by user {0} on method {1}",
                  "There is a coding error in the caller to the OMF Services.",
                  "Correct the caller logic and retry the request."),

    /**
     * OMF-SERVICES-500-004 An unexpected exception occurred when sending an event through connector {0} to the OMF Services out topic.
     * The failing event was {1}, the exception was {2} with message {2}
     */
    UNABLE_TO_SEND_EVENT(500, "OMF-SERVICES-500-004",
                         "An unexpected exception occurred when sending an event through connector {0} to the OMF Services out topic.  The failing " +
                                 "event was {1}, the exception was {2} with message {2}",
                         "The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.",
                         "Look for errors in the remote server's audit log and console to understand and correct the source of the error."),

    /**
     * OMF-SERVICES-500-006 The requested connector for connection named {0} has not been created.
     * The connection was provided by the {1} service running in OMAG Server {2} at {3}
     */
    NULL_CONNECTOR_RETURNED(500, "OMF-SERVICES-500-006",
                            "The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service" +
                                    " running in OMAG Server {2} at {3}",
                            "The system is unable to create a connector which means some of its services will not work.",
                            "This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connection" +
                                    "and correct if necessary.  If the connection is correct, contact the Egeria community for help."),

    /**
     * OMF-SERVICES-500-007 The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2}
     * at {3} is not of the required type. It should be an instance of {4}
     */
    WRONG_TYPE_OF_CONNECTOR(500, "OMF-SERVICES-500-007",
                            "The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is " +
                                    "not of the required type. It should be an instance of {4}",
                            "The system is unable to create the required connector which means some of its services will not work.",
                            "Verify that the OMAG server is running and the OMAS service is correctly configured."),

    /**
     * OMF-SERVICES-500-008 - The Design Model OMAS has received an unexpected {0} exception during method {1}.  The message was: {2}
     */
    UNEXPECTED_EXCEPTION(500, "OMF-SERVICES-500-008",
                         "The OMF Services has received an unexpected {0} exception during method {1} for service {2}.  The message was: {3}",
                         "The request returns with a PropertyServerException to indicate there has been an internal server error. The server also created a detailed error message and stack trace in the audit log.",
                         "Review the stack trace to identify where the error occurred and work to resolve the cause."),



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
    OMFServicesErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
