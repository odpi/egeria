/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.infrastructure.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The InfrastructureIntegratorAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum InfrastructureIntegratorAuditCode implements AuditLogMessageSet
{
    CONTEXT_INITIALIZING("OMIS-INFRASTRUCTURE-INTEGRATOR-0001",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "The infrastructure integrator context manager is being initialized for calls to server {0} on platform {1}",
                        "The Infrastructure Integrator OMIS is initializing its context manager.",
                        "Verify that the start up sequence goes on to initialize the context for each connector configured for this service."),

    CONNECTOR_CONTEXT_INITIALIZING("OMIS-INFRASTRUCTURE-INTEGRATOR-0002",
                        OMRSAuditLogRecordSeverity.STARTUP,
                        "Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}",
                        "A new context is created for an integration connector.  This acts as a client to the open metadata repositories " +
                                "enabling the integration connector to synchronize open metadata with the third party technology's metadata",
                        "Verify that this connector is being started with the correct configuration."),

    PERMITTED_SYNCHRONIZATION("OMIS-INFRASTRUCTURE-INTEGRATOR-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The context for connector {0} has its permitted synchronization set to {1}",
             "The context is set up to ensure that the connector can only issue requests that support the permitted synchronization.  " +
                     "If the connector issues requests that are not permitted it is returned UserNotAuthorizedExceptions.",
             "Check that this permitted synchronized value is as expected.  If it is not, " +
                     "change the configuration for this connector and restart the integration daemon."),

    DISABLED_EXCHANGE_SERVICES("OMIS-INFRASTRUCTURE-INTEGRATOR-0004",
                               OMRSAuditLogRecordSeverity.STARTUP,
                               "The following exchange services are disabled in the context for connector {1}: {2}",
                               "The context is set up to ensure that the connector can only issue requests to supported services.  " +
                                       "If the connector issues requests that are not permitted it is returned UserNotAuthorizedExceptions.",
                               "Check that this value is as expected.  If it is not, " +
                                       "change the configuration for this connector and restart the integration daemon."),

    NULL_CONTEXT("OMIS-INFRASTRUCTURE-INTEGRATOR-0005",
                 OMRSAuditLogRecordSeverity.ERROR,
                 "Integration connector {0} has a null context",
                 "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
                 "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                         "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),
    ;


    AuditLogMessageDefinition messageDefinition;



    /**
     * The constructor for InfrastructureIntegratorAuditCode expects to be passed one of the enumeration rows defined in
     * InfrastructureIntegratorAuditCode above.   For example:
     *
     *     InfrastructureIntegratorAuditCode   auditCode = InfrastructureIntegratorAuditCode.SERVER_SHUTDOWN;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    InfrastructureIntegratorAuditCode(String                     messageId,
                                    OMRSAuditLogRecordSeverity severity,
                                    String                     message,
                                    String                     systemAction,
                                    String                     userAction)
    {
        messageDefinition = new AuditLogMessageDefinition(messageId,
                                                          severity,
                                                          message,
                                                          systemAction,
                                                          userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
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
        return "InfrastructureIntegratorAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
