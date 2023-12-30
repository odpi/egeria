/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.catalog.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The CatalogIntegratorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum CatalogIntegratorAuditCode implements AuditLogMessageSet
{
    /**
     * OMIS-CATALOG-INTEGRATOR-0001 - The catalog integrator context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OMIS-CATALOG-INTEGRATOR-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The catalog integrator context manager is being initialized for calls to server {0} on platform {1}",
                         "The Catalog Integrator OMIS is initializing its context manager.",
                         "Verify that the start up sequence goes on to initialize the context for each connector configured for this service."),

    /**
     * OMIS-CATALOG-INTEGRATOR-0002 - Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}
     */
    CONNECTOR_CONTEXT_INITIALIZING("OMIS-CATALOG-INTEGRATOR-0002",
                                   AuditLogRecordSeverityLevel.STARTUP,
                        "Creating context for integration connector {0} ({1}) connecting to third party technology {2} with permitted synchronization of {3} and service options of {4}",
                        "A new context is created for an integration connector.  This acts as a client to the open metadata repositories " +
                                "enabling the integration connector to synchronize open metadata with the third party technology's metadata",
                        "Verify that this connector is being started with the correct configuration."),

    /**
     * OMIS-CATALOG-INTEGRATOR-0003 - The context for connector {0} has its permitted synchronization set to {1}
     */
    PERMITTED_SYNCHRONIZATION("OMIS-CATALOG-INTEGRATOR-0003",
                              AuditLogRecordSeverityLevel.STARTUP,
             "The context for connector {0} has its permitted synchronization set to {1}",
             "The context is set up to ensure that the connector can only issue requests that support the permitted synchronization.  " +
                     "If the connector issues requests that are not permitted it is returned UserNotAuthorizedExceptions.",
             "Check that this permitted synchronized value is as expected.  If it is not," +
                     "change the configuration for this connector and restart the integration daemon."),

    /**
     * OMIS-CATALOG-INTEGRATOR-0004 - The following exchange services are disabled in the context for connector {1}: {2}
     */
    DISABLED_EXCHANGE_SERVICES("OMIS-CATALOG-INTEGRATOR-0004",
                               AuditLogRecordSeverityLevel.STARTUP,
                               "The following exchange services are disabled in the context for connector {1}: {2}",
                               "The context is set up to ensure that the connector can only issue requests to supported services.  " +
                                       "If the connector issues requests that are not permitted it is returned UserNotAuthorizedExceptions.",
                               "Check that this value is as expected.  If it is not, " +
                                       "change the configuration for this connector and restart the integration daemon."),

    /**
     * OMIS-CATALOG-INTEGRATOR-0005 - Integration connector {0} has a null context
     */
    NULL_CONTEXT("OMIS-CATALOG-INTEGRATOR-0005",
                 AuditLogRecordSeverityLevel.ERROR,
                 "Integration connector {0} has a null context",
                 "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
                 "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                         "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),
    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for CatalogIntegratorAuditCode expects to be passed one of the enumeration rows defined in
     * CatalogIntegratorAuditCode above.   For example:
     * <br><br>
     *     CatalogIntegratorAuditCode   auditCode = CatalogIntegratorAuditCode.SERVER_SHUTDOWN;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    CatalogIntegratorAuditCode(String                      messageId,
                               AuditLogRecordSeverityLevel severity,
                               String                      message,
                               String                      systemAction,
                               String                      userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return new AuditLogMessageDefinition(logMessageId,
                                             severity,
                                             logMessage,
                                             systemAction,
                                             userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params)
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
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
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
