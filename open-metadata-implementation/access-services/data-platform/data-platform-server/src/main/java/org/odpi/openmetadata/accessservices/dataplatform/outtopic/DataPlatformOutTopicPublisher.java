/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.outtopic;

import org.odpi.openmetadata.accessservices.dataplatform.connectors.outtopic.DataPlatformOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * DataPlatformOutTopicPublisher is responsible for sending events on the Data Platform OMAS's out topic.
 * It is called from the Data Platform OMAS's OMRS Topic Listener.
 */
public class DataPlatformOutTopicPublisher
{
    private DataPlatformOutTopicServerConnector outTopicServerConnector;
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
    public DataPlatformOutTopicPublisher(DataPlatformOutTopicServerConnector outTopicServerConnector, 
                                         String                                 outTopicName, 
                                         AuditLog                               outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, DataPlatformAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
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
                outTopicAuditLog.logMessage(actionDescription, DataPlatformAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Throwable error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              DataPlatformAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
