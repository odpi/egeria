/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.outtopic;

import org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic.DataManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * DataManagerOutTopicPublisher is responsible for sending events on the Data Manager OMAS's out topic.
 * It is called from the Data Manager OMAS's OMRS Topic Listener.
 */
public class DataManagerOutTopicPublisher
{
    private DataManagerOutTopicServerConnector outTopicServerConnector;
    private AuditLog                            outTopicAuditLog;
    private String                              outTopicName;

    private final String actionDescription = "Out topic configuration refresh event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     */
    public DataManagerOutTopicPublisher(DataManagerOutTopicServerConnector outTopicServerConnector,
                                         String                                 outTopicName, 
                                         AuditLog                               outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, DataManagerAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Shutdown the publishing process.
     */
    public void disconnect()
    {
        try
        {
            outTopicServerConnector.disconnect();

            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logMessage(actionDescription, DataManagerAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              DataManagerAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
