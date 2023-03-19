/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.outtopic;

import org.odpi.openmetadata.accessservices.digitalarchitecture.connectors.outtopic.DigitalArchitectureOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.digitalarchitecture.converters.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.digitalarchitecture.events.DigitalArchitectureEventType;
import org.odpi.openmetadata.accessservices.digitalarchitecture.events.DigitalArchitectureOutTopicEvent;
import org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc.DigitalArchitectureAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * DigitalArchitectureOutTopicPublisher is responsible for sending events on the IT Infrastructure OMAS's out topic.
 * It is called from the IT Infrastructure OMAS's OMRS Topic Listener.
 */
public class DigitalArchitectureOutTopicPublisher
{
    private final DigitalArchitectureOutTopicServerConnector outTopicServerConnector;
    private final AuditLog                                   outTopicAuditLog;
    private final String                                     outTopicName;
    private final ElementHeaderConverter<ElementHeader>      headerConverter;
    private final OMRSRepositoryHelper                       repositoryHelper;

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
    public DigitalArchitectureOutTopicPublisher(DigitalArchitectureOutTopicServerConnector outTopicServerConnector,
                                                String                                     outTopicName,
                                                AuditLog                                   outTopicAuditLog,
                                                OMRSRepositoryHelper                       repositoryHelper,
                                                String                                     serviceName,
                                                String                                     serverName)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;
        this.repositoryHelper        = repositoryHelper;

        this.headerConverter = new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName);

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, DigitalArchitectureAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Send the event to the embedded event bus connector(s).
     *
     * @param entity entity that is the subject of the event
     * @param eventType type of event
     */
    public void publishEntityEvent(EntityDetail                 entity,
                                   DigitalArchitectureEventType eventType)
    {
        this.publishEntityEvent(eventType, entity, null, null, null);
    }


    /**
     * Send the event to the embedded event bus connector(s).
     *
     * @param entity entity that is the subject of the event
     * @param previousEntity previous version of the entity
     * @param eventType type of event
     * @param newClassification latest classification information (if the event relates to a classification)
     * @param previousClassification previous classification information (if the event relates to a classification)
     */
    public void publishEntityEvent(DigitalArchitectureEventType eventType,
                                   EntityDetail              entity,
                                   EntityDetail              previousEntity,
                                   Classification            newClassification,
                                   Classification            previousClassification)
    {
        final String methodName = "publishEntityEvent";

        if (outTopicServerConnector != null)
        {
            DigitalArchitectureOutTopicEvent event = new DigitalArchitectureOutTopicEvent();

            try
            {
                event.setEventType(eventType);

                if (entity.getUpdateTime() == null)
                {
                    event.setEventTime(entity.getCreateTime());
                }
                else
                {
                    event.setEventTime(entity.getUpdateTime());
                }

                event.setElementHeader(headerConverter.getNewBean(ElementHeader.class, entity, methodName));
                event.setElementProperties(repositoryHelper.getInstancePropertiesAsMap(entity.getProperties()));

                if (previousEntity != null)
                {
                    event.setPreviousElementHeader(headerConverter.getNewBean(ElementHeader.class, previousEntity, methodName));
                    event.setPreviousElementProperties(repositoryHelper.getInstancePropertiesAsMap(previousEntity.getProperties()));
                }

                if (newClassification != null)
                {
                    event.setClassificationName(newClassification.getName());
                }

                if (previousClassification != null)
                {
                    event.setClassificationName(previousClassification.getName());
                    event.setPreviousClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(previousClassification.getProperties()));
                }

                outTopicServerConnector.sendEvent(event);
            }
            catch (Exception error)
            {
                outTopicAuditLog.logException(methodName,
                                              DigitalArchitectureAuditCode.PROCESS_EVENT_EXCEPTION.getMessageDefinition(event.toString(),
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
                outTopicAuditLog.logMessage(actionDescription, DigitalArchitectureAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              DigitalArchitectureAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
