/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.search.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The SearchIntegratorAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum SearchIntegratorAuditCode implements AuditLogMessageSet
{
    /**
     * OMIS-SEARCH-INTEGRATOR-0001 - The search integrator context manager is being initialized for calls to server {0} on platform {1}
     */
    CONTEXT_INITIALIZING("OMIS-SEARCH-INTEGRATOR-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The search integrator context manager is being initialized for calls to server {0} on platform {1}",
            "The search Integrator OMIS is initializing its context manager.",
            "Verify that the start up sequence goes on to initialize the context for each connector configured for this service."),

    /**
     * OMIS-SEARCH-INTEGRATOR-0002 - Creating context for integration connector {0} ({1}) connecting to third party technology {2} and service options of {3}
     */
    CONNECTOR_CONTEXT_INITIALIZING("OMIS-SEARCH-INTEGRATOR-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "Creating context for integration connector {0} ({1}) connecting to third party technology {2} and service options of {3}",
            "A new context is created for an integration connector.  This acts as a client to the open metadata repositories " +
                    "enabling the integration connector to synchronize open metadata with the third party technology's metadata",
            "Verify that this connector is being started with the correct configuration."),

    /**
     * OMIS-SEARCH-INTEGRATOR-0003 - Integration connector {0} has a null context
     */
    NULL_CONTEXT("OMIS-SEARCH-INTEGRATOR-0003",
            OMRSAuditLogRecordSeverity.ERROR,
            "Integration connector {0} has a null context",
            "The integration connector is running but does not have a context.  This is a timing issue in the integration daemon.",
            "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                    "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help."),

    /**
     * OMIS-SEARCH-INTEGRATOR-0004 - Integration connector {0} has an exception while attempting to register an Asset Catalog event listener
     */
    REGISTER_CATALOG_LISTENER_ERROR("OMIS-SEARCH-INTEGRATOR-0004",
            OMRSAuditLogRecordSeverity.ERROR,
            "Integration connector {0} has an exception while attempting to register an Asset Catalog event listener",
            "The integration connector is running, but does not have a listener",
            "Gather information about the connector's configuration, the types of metadata it was integrating, the audit log messages " +
                    "from the integration daemon and its partner metadata server.  Then contact the Egeria community to get help.")

    ;

    private final AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for SearchIntegratorAuditCode expects to be passed one of the enumeration rows defined in
     * SearchIntegratorAuditCode above.       *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    SearchIntegratorAuditCode(String                     messageId,
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
    public AuditLogMessageDefinition getMessageDefinition() {
        return messageDefinition;
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
        return "SearchIntegratorAuditCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
