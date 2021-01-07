/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.derby.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * This is the interface for the generic operations on data virtualization solutions
 */
public enum DerbyViewConnectorAuditCode implements AuditLogMessageSet
{
    CONNECTOR_INITIALIZING("DERBY-VIEW-CONNECTOR-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The connector is being initialized",
            "The OMAG server has started up a new instance of the derby view generating connector.",
            "Validate that this connector is running in a Virtualizer Server."),
    CONNECTOR_INITIALIZED("DERBY-VIEW-CONNECTOR-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The derby view generating connector has initialized a new instance.  It is connected to database {0} at {1}",
            "The virtualizer server has completed initialization of a new instance of this connector.",
            "Verify that the connector has started and it able to connect both to the metadata server/access point and the " +
                                  "virtualization platform hosting the database where the views are to be configured."),
    CONNECTOR_SHUTDOWN("DERBY-VIEW-CONNECTOR-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The derby view generating connector is shutting down its instance for database {0} at {1}",
            "The local server has requested shut down of a derby view generating connector.",
            "Verify that shutdown has been requested and that all of the resources this connector was using have been released."),
    ENDPOINT_CONFIGURATION_ERROR("DERBY-VIEW-CONNECTOR-0004",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "The connection for the derby view generating connector is not valid. The endpoint server address is {0} and the additional properties " +
                                                 "are {1}",
                                 "The local server is unable to create an instance of the derby view generating connector because the " +
                                         "endpoint of this connector's connection either is missing the address of the data virtualization platform" +
                                         " or critical properties for the additional properties collection.",
                                 "The missing values need to be added to the connection object for this connector.  This is located in the " +
                                         "configuration document for the virtualizer server"),
    NO_ENDPOINT("DERBY-VIEW-CONNECTOR-0005",
                OMRSAuditLogRecordSeverity.EXCEPTION,
                "There is no endpoint linked to the connection object for this connector.",
                "The local server is unable to initialize this connector because there is no endpoint. The endpoint is where critical" +
                        "properties about the data virtualization platform that it is configuring are located.",
                "Reconfigure the connection object for this connector in the configuration document of the hosting " +
                        "Virtualizer Server to ensure it has the correct endpoint values."),
    CONNECTION_CONFIGURATION_ERROR("DERBY-VIEW-CONNECTOR-0006.",
                                   OMRSAuditLogRecordSeverity.EXCEPTION,
                                   "There are no additional properties in the connection object for this connector",
                                   "The local server is unable to initialize a derby view generating connector because the additional" +
                                           " properties are not configured in the connection object for this connector.  The connection object is " +
                                           "located in the configuration document for the hosting Virtualizer Server.",
                                   "Reconfigure this connector's connection object to include the additional properties it needs to create logical " +
                                           "tables."),
    CONNECTOR_SERVER_CONNECTION_ERROR("DERBY-VIEW-CONNECTOR-0007.",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to connect to the data virtualization platform {0} at {1} using driver {2}.",
            "The local server is unable to establish a connection to the data virtualization platform.",
            "Validate/correct the database name, address and the driver being used."),
    CONNECTOR_QUERY_ERROR("DERBY-VIEW-CONNECTOR-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The logical table cannot be queried.",
            "No query result will be provided.",
            "Check if the query is valid."),
    CONNECTOR_INBOUND_EVENT_ERROR("DERBY-VIEW-CONNECTOR-0009",
                           OMRSAuditLogRecordSeverity.EXCEPTION,
            "Provided inbound event is invalid.",
                                   "No action on database will be executed.",
                                   "Check if the event is valid.");


    private AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for DerbyViewConnectorAuditCode expects to be passed one of the enumeration rows defined in
     * DerbyViewConnectorAuditCode above.   For example:
     *
     *     DerbyViewConnectorAuditCode   auditCode = DerbyViewConnectorAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId unique Id for the message
     * @param severity severity of the message
     * @param message text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    DerbyViewConnectorAuditCode(String                     messageId,
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
}
