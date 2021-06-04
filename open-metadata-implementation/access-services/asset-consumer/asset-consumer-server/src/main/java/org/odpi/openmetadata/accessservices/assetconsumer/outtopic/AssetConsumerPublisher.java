/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetconsumer.events.AssetConsumerEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.events.NewAssetEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.events.UpdatedAssetEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * AssetConsumerPublisher is the connector responsible for publishing information about
 * new and changed assets.
 */
public class AssetConsumerPublisher
{
    private static final Logger log = LoggerFactory.getLogger(AssetConsumerPublisher.class);

    private OpenMetadataTopicConnector  connector = null;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic connection to the out topic
     * @param auditLog log file for the connector.
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
     */
    public AssetConsumerPublisher(Connection assetConsumerOutTopic,
                                  AuditLog   auditLog) throws OMAGConfigurationErrorException
    {
        if (assetConsumerOutTopic != null)
        {
            connector = this.getTopicConnector(assetConsumerOutTopic, auditLog);
        }
    }


    /**
     * Output a new asset event.
     *
     * @param event event to send
     */
    public void publishNewAssetEvent(NewAssetEvent event)
    {
        try
        {
            if (connector != null)
            {
                connector.sendEvent(this.getJSONPayload(event));
            }
        }
        catch (Exception  error)
        {
            log.error("Unable to publish new asset event: " + event.toString() + "; error was " + error.toString());
        }
    }


    /**
     * Output an updated asset event.
     *
     * @param event event to send.
     */
    public void publishUpdatedAssetEvent(UpdatedAssetEvent  event)
    {
        try
        {
            if (connector != null)
            {
                connector.sendEvent(this.getJSONPayload(event));
            }
        }
        catch (Exception  error)
        {
            log.error("Unable to publish undated asset event: " + event.toString() + "; error was " + error.toString());
        }
    }


    /**
     * Create the topic connector.
     *
     * @param topicConnection connection to create the connector
     * @param auditLog audit log for the connector
     * @return open metadata topic connector
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection  topicConnection,
                                                         AuditLog    auditLog) throws OMAGConfigurationErrorException
    {
        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector       connector       = connectorBroker.getConnector(topicConnection);

            OpenMetadataTopicConnector topicConnector  = (OpenMetadataTopicConnector)connector;

            topicConnector.setAuditLog(auditLog);

            topicConnector.start();

            return topicConnector;
        }
        catch (Exception   error)
        {
            String methodName = "getTopicConnector";

            log.error("Unable to create topic connector: " + error.toString());

            throw new OMAGConfigurationErrorException(AssetConsumerErrorCode.BAD_OUT_TOPIC_CONNECTION.getMessageDefinition(topicConnection.toString(),
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
        }
    }


    /**
     * Return the event as a String where the field contents are encoded in JSON.   The event beans
     * contain annotations that mean the whole event, down to the lowest subclass, is serialized.
     *
     * @param event event to serialize
     * @return JSON payload (as String)
     */
    private String getJSONPayload(AssetConsumerEvent event)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(event);
        }
        catch (Exception  error)
        {
            log.error("Unable to create event payload: " + error.toString());
        }

        return jsonString;
    }

}
