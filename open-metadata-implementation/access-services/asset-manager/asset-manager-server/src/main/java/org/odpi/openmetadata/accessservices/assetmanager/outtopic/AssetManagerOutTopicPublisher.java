/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.outtopic;

import org.odpi.openmetadata.accessservices.assetmanager.connectors.outtopic.AssetManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.assetmanager.converters.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerEventType;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetmanager.ffdc.AssetManagerAuditCode;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * AssetManagerOutTopicPublisher is responsible for sending events on the Asset Manager OMAS's out topic.
 * It is called from the Asset Manager OMAS's OMRS Topic Listener.
 */
public class AssetManagerOutTopicPublisher
{
    private AssetManagerOutTopicServerConnector   outTopicServerConnector;
    private AuditLog                              outTopicAuditLog;
    private String                                outTopicName;
    private ElementHeaderConverter<ElementHeader> headerConverter;

    private final String actionDescription = "Out topic configuration refresh event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetManagerOutTopicPublisher(AssetManagerOutTopicServerConnector outTopicServerConnector,
                                         String                              outTopicName,
                                         AuditLog                            outTopicAuditLog,
                                         OMRSRepositoryHelper                repositoryHelper,
                                         String                              serviceName,
                                         String                              serverName)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        this.headerConverter = new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName);

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, AssetManagerAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Send the event to the embedded event bus connector(s).
     *
     * @param entity entity that is the subject of the event
     * @param eventType type of event
     */
    public void publishEntityEvent(EntityDetail          entity,
                                   AssetManagerEventType eventType)
    {
        this.publishEntityEvent(entity, eventType, null);
    }


    /**
     * Send the event to the embedded event bus connector(s).
     *
     * @param entity entity that is the subject of the event
     * @param eventType type of event
     * @param classificationName name of the classification if the event relates to a classification
     */
    public void publishEntityEvent(EntityDetail          entity,
                                   AssetManagerEventType eventType,
                                   String                classificationName)
    {
        final String methodName = "publishEntityEvent";

        if (outTopicServerConnector != null)
        {
            AssetManagerOutTopicEvent event = new AssetManagerOutTopicEvent();

            try
            {
                event.setElementHeader(headerConverter.getNewBean(ElementHeader.class, entity, methodName));
                event.setEventType(eventType);
                event.setClassificationName(classificationName);

                outTopicServerConnector.sendEvent(event);

                outTopicAuditLog.logMessage(methodName, AssetManagerAuditCode.OUT_TOPIC_EVENT.getMessageDefinition(event.toString()));
            }
            catch (Exception error)
            {
                outTopicAuditLog.logException(methodName,
                                              AssetManagerAuditCode.PROCESS_EVENT_EXCEPTION.getMessageDefinition(event.toString(),
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                              error);
            }
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
