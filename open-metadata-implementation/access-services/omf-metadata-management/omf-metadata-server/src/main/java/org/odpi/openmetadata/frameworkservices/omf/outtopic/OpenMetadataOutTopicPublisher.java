/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.outtopic;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic.OMFOutTopicServerConnector;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesAuditCode;
import org.odpi.openmetadata.frameworkservices.omf.converters.ElementHeaderConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataOutTopicPublisher is responsible for sending events on the Open Metadata Store's out topic.
 * It is called from the Open MetadataStore's OMRS Topic Listener.
 */
public class OpenMetadataOutTopicPublisher
{
    private final OMFOutTopicServerConnector outTopicServerConnector;
    private final AuditLog                   outTopicAuditLog;
    private final String                                outTopicName;
    private final ElementHeaderConverter<ElementHeader> headerConverter;
    private final OutTopicEventPropertyLimiter          propertyLimiter = new OutTopicEventPropertyLimiter();

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
    public OpenMetadataOutTopicPublisher(OMFOutTopicServerConnector outTopicServerConnector,
                                         String                     outTopicName,
                                         AuditLog                   outTopicAuditLog,
                                         OMRSRepositoryHelper       repositoryHelper,
                                         String                     serviceName,
                                         String                     serverName)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        this.headerConverter = new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName);

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, OMFServicesAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Shorten any property that is too large to send, so the event is not dropped by the event bus, and say
     * which properties were shortened.
     *
     * @param properties properties for the event
     * @param elementHeader header of the element the properties belong to (for the audit log)
     * @param methodName calling method
     * @return properties to send
     */
    private ElementProperties limitProperties(ElementProperties properties,
                                              ElementHeader     elementHeader,
                                              String            methodName)
    {
        List<OutTopicEventPropertyLimiter.ShortenedProperty> shortenedProperties = new ArrayList<>();

        ElementProperties limitedProperties = propertyLimiter.limitProperties(properties, shortenedProperties);

        if (outTopicAuditLog != null)
        {
            for (OutTopicEventPropertyLimiter.ShortenedProperty shortenedProperty : shortenedProperties)
            {
                outTopicAuditLog.logMessage(methodName,
                                            OMFServicesAuditCode.EVENT_PROPERTY_SHORTENED.getMessageDefinition(shortenedProperty.propertyName(),
                                                                                                                ((elementHeader == null) || (elementHeader.getType() == null)) ? "element" : elementHeader.getType().getTypeName(),
                                                                                                                (elementHeader == null) ? "" : elementHeader.getGUID(),
                                                                                                                Long.toString(shortenedProperty.estimatedSize()),
                                                                                                                Integer.toString(shortenedProperty.valuesKept())));
            }
        }

        return limitedProperties;
    }


    /**
     * Send the event to the embedded event bus connector(s).
     *
     * @param entity entity that is the subject of the event
     * @param eventType type of event
     */
    public void publishEntityEvent(EntityDetail          entity,
                                   OpenMetadataEventType eventType)
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
    public void publishEntityEvent(OpenMetadataEventType eventType,
                                   EntityDetail          entity,
                                   EntityDetail          previousEntity,
                                   Classification        newClassification,
                                   Classification        previousClassification)
    {
        final String methodName = "publishEntityEvent";

        if (outTopicServerConnector != null)
        {
            OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

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
                event.setElementProperties(this.limitProperties(headerConverter.mapElementProperties(entity.getProperties()), event.getElementHeader(), methodName));

                if (previousEntity != null)
                {
                    event.setPreviousElementHeader(headerConverter.getNewBean(ElementHeader.class, previousEntity, methodName));
                    event.setPreviousElementProperties(this.limitProperties(headerConverter.mapElementProperties(previousEntity.getProperties()), event.getElementHeader(), methodName));
                }

                if (newClassification != null)
                {
                    event.setClassificationName(newClassification.getName());
                    event.setClassificationProperties(this.limitProperties(headerConverter.mapElementProperties(newClassification.getProperties()), event.getElementHeader(), methodName));
                }

                if (previousClassification != null)
                {
                    event.setClassificationName(previousClassification.getName());
                    event.setPreviousClassificationProperties(this.limitProperties(headerConverter.mapElementProperties(previousClassification.getProperties()), event.getElementHeader(), methodName));
                }

                outTopicServerConnector.sendEvent(event);
            }
            catch (Exception error)
            {
                outTopicAuditLog.logException(methodName,
                                              OMFServicesAuditCode.PROCESS_EVENT_EXCEPTION.getMessageDefinition(event.toString(),
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                              error);
            }
        }
    }


    /**
     * Send the event to the embedded event bus connector(s).
     *
     * @param eventType type of event
     * @param relationship relationship that is the subject of the event
     * @param previousRelationship previous version of the relationship
     * @param end1 entity from end 1 of the relationship
     * @param end2 entity from end 2 of the relationship
     */
    public void publishRelationshipEvent(OpenMetadataEventType eventType,
                                         Relationship          relationship,
                                         Relationship          previousRelationship,
                                         EntityDetail          end1,
                                         EntityDetail          end2)
    {
        final String methodName = "publishRelationshipEvent";

        if (outTopicServerConnector != null)
        {
            OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

            try
            {
                event.setEventType(eventType);

                if (relationship.getUpdateTime() == null)
                {
                    event.setEventTime(relationship.getCreateTime());
                }
                else
                {
                    event.setEventTime(relationship.getUpdateTime());
                }

                event.setElementHeader(headerConverter.getNewBean(ElementHeader.class, relationship, methodName));
                event.setElementProperties(this.limitProperties(headerConverter.mapElementProperties(relationship.getProperties()), event.getElementHeader(), methodName));

                if (previousRelationship != null)
                {
                    event.setPreviousElementHeader(headerConverter.getNewBean(ElementHeader.class, previousRelationship, methodName));
                    event.setPreviousElementProperties(this.limitProperties(headerConverter.mapElementProperties(previousRelationship.getProperties()), event.getElementHeader(), methodName));
                }

                event.setEndOneElementHeader(headerConverter.getNewBean(ElementHeader.class, end1, methodName));
                event.setEndOneElementProperties(this.limitProperties(headerConverter.mapElementProperties(end1.getProperties()), event.getEndOneElementHeader(), methodName));

                event.setEndTwoElementHeader(headerConverter.getNewBean(ElementHeader.class, end2, methodName));
                event.setEndTwoElementProperties(this.limitProperties(headerConverter.mapElementProperties(end2.getProperties()), event.getEndTwoElementHeader(), methodName));

                outTopicServerConnector.sendEvent(event);
            }
            catch (Exception error)
            {
                outTopicAuditLog.logException(methodName,
                                              OMFServicesAuditCode.PROCESS_EVENT_EXCEPTION.getMessageDefinition(event.toString(),
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
                outTopicAuditLog.logMessage(actionDescription, OMFServicesAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              OMFServicesAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
