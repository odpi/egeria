/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.outtopic;

import org.odpi.openmetadata.accessservices.assetconsumer.connectors.outtopic.AssetConsumerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.assetconsumer.events.NewAssetEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.events.UpdatedAssetEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 * AssetConsumerPublisher is the connector responsible for publishing information about
 * new and changed assets.
 */
public class AssetConsumerPublisher
{
    private AssetConsumerOutTopicServerConnector  outTopicServerConnector;
    private AuditLog                              outTopicAuditLog;
    private String                                outTopicName;

    private final String actionDescription = "Out topic event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     */
    public AssetConsumerPublisher(AssetConsumerOutTopicServerConnector outTopicServerConnector,
                                  String                                  outTopicName,
                                  AuditLog                                outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, AssetConsumerAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Output a new asset event.
     *
     * @param event event to send
     */
    public void publishNewAssetEvent(NewAssetEvent event)
    {
        final String methodName = "publishNewAssetEvent";

        try
        {
            if (outTopicServerConnector != null)
            {
                outTopicServerConnector.sendEvent(event);
            }
        }
        catch (Exception  error)
        {
            logUnexpectedPublishingException(error, methodName);
        }
    }


    /**
     * Output an updated asset event.
     *
     * @param event event to send.
     */
    public void publishUpdatedAssetEvent(UpdatedAssetEvent  event)
    {
        final String methodName = "publishUpdatedAssetEvent";

        try
        {
            if (outTopicServerConnector != null)
            {
                outTopicServerConnector.sendEvent(event);
            }
        }
        catch (Exception  error)
        {
            logUnexpectedPublishingException(error, methodName);
        }
    }



    /**
     * Log any exceptions that have come from the publishing process.
     *
     * @param error this is the exception that was thrown
     * @param methodName this is the calling method
     */
    private void logUnexpectedPublishingException(Exception  error,
                                                  String     methodName)
    {
        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logException(methodName,
                                          AssetConsumerAuditCode.OUT_TOPIC_FAILURE.getMessageDefinition(outTopicName,
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage()),
                                          error);
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
                outTopicAuditLog.logMessage(actionDescription, AssetConsumerAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              AssetConsumerAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                       outTopicName,
                                                                                                                       error.getMessage()),
                                              error);
            }
        }
    }
}
