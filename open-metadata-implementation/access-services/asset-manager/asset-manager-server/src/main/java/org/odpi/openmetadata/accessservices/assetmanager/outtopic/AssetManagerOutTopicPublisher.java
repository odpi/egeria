/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.outtopic;

import org.odpi.openmetadata.accessservices.assetmanager.connectors.outtopic.AssetManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.assetmanager.ffdc.AssetManagerAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * AssetManagerOutTopicPublisher is responsible for sending events on the Asset Manager OMAS's out topic.
 * It is called from the Asset Manager OMAS's OMRS Topic Listener.
 */
public class AssetManagerOutTopicPublisher
{
    private AssetManagerOutTopicServerConnector outTopicServerConnector;
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
    public AssetManagerOutTopicPublisher(AssetManagerOutTopicServerConnector outTopicServerConnector,
                                         String                                 outTopicName, 
                                         AuditLog                               outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, AssetManagerAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
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
                outTopicAuditLog.logMessage(actionDescription, AssetManagerAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              AssetManagerAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
