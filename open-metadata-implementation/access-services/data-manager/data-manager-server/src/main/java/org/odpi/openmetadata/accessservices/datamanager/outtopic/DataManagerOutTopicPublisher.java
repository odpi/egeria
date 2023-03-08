/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.outtopic;

import org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic.DataManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutboundEvent;
import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutboundEventType;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

/**
 * DataManagerOutTopicPublisher is responsible for sending events on the Data Manager OMAS's out topic.
 * It is called from the Data Manager OMAS's OMRS Topic Listener.
 */
public class DataManagerOutTopicPublisher
{
    private final DataManagerOutTopicServerConnector outTopicServerConnector;
    private final AuditLog                           outTopicAuditLog;
    private final String                             outTopicName;

    private final String actionDescription = "Out topic configuration refresh event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     */
    public DataManagerOutTopicPublisher(DataManagerOutTopicServerConnector outTopicServerConnector,
                                        String                             outTopicName,
                                        AuditLog                           outTopicAuditLog)
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
     * Send the event to all listening parties.
     *
     * @param event event structure
     * @param principleElementGUID unique identifier for the subject of the event
     * @param principleElementTypeName type of the subject of the event
     */
    private void sendEvent(DataManagerOutboundEvent event,
                           String                        principleElementGUID,
                           String                        principleElementTypeName)
    {

        try
        {
            outTopicServerConnector.sendEvent(event);
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(event.getEventType().getEventTypeName(),
                                                                                                                 principleElementGUID,
                                                                                                                 principleElementTypeName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                              error);
            }
        }
    }


    /**
     * Send an event that relates to an entity.
     *
     * @param eventType type of change to the entity
     * @param elementGUID unique identifier for the entity
     * @param elementTypeName type of entity
     * @param classificationName if a classification has changed this identifies which classification
     * @param elementStub encoded header of the entity
     */
    public void sendEntityEvent(DataManagerOutboundEventType eventType,
                                String                            elementGUID,
                                String                            elementTypeName,
                                String                            classificationName,
                                ElementStub elementStub)
    {
        DataManagerOutboundEvent event = new DataManagerOutboundEvent();

        event.setEventType(eventType);
        event.setPrincipleElement(elementStub);
        event.setClassificationName(classificationName);
        sendEvent(event, elementGUID, elementTypeName);
    }


    /**
     * Send an event that relates to a relationship.
     *
     * @param eventType type of change to the relationship
     * @param relationshipGUID unique identifier for the relationship
     * @param relationshipTypeName type of relationship
     * @param relationshipElementStub encoded header of the relationship
     * @param endOneElementStub encoded header of the entity at end 1 of the relationship
     * @param endTwoElementStub encoded header of the entity at end 2 of the relationship
     */
    public void sendRelationshipEvent(DataManagerOutboundEventType eventType,
                                      String                            relationshipGUID,
                                      String                            relationshipTypeName,
                                      ElementStub                       relationshipElementStub,
                                      ElementStub                       endOneElementStub,
                                      ElementStub                       endTwoElementStub)
    {
        DataManagerOutboundEvent event = new DataManagerOutboundEvent();

        event.setEventType(eventType);
        event.setPrincipleElement(relationshipElementStub);
        event.setEndOneElement(endOneElementStub);
        event.setEndTwoElement(endTwoElementStub);
        sendEvent(event, relationshipGUID, relationshipTypeName);
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
