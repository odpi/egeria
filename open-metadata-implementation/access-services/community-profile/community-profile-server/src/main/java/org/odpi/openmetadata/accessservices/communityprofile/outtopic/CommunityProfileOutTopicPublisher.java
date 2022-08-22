/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.outtopic;

import org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic.CommunityProfileOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileAuditCode;
import org.odpi.openmetadata.accessservices.communityprofile.events.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;


/**
 * CommunityProfileOutTopicPublisher is responsible for publishing events about changes to personal profiles,
 * communities and related elements.  It is called when an interesting OMRS Event is added to the Enterprise
 * OMRS Topic (see CommunityProfileOMRSTopicListener).
 *
 * The actual sending of events is done by the super class CommunityProfileOutTopicPublisher.
 * This class logs a message to the OMRS Audit Log before calling the super class.
 */
public class CommunityProfileOutTopicPublisher
{
    private final CommunityProfileOutTopicServerConnector outTopicServerConnector;
    private final AuditLog                                outTopicAuditLog;
    private final String                                  outTopicName;

    private final String actionDescription = "Out topic configuration refresh event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param auditLog logging destination if anything goes wrong.
     */
    public CommunityProfileOutTopicPublisher(CommunityProfileOutTopicServerConnector outTopicServerConnector,
                                             String                                  outTopicName,
                                             AuditLog                                auditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = auditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, CommunityProfileAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Send the event to all listening parties.
     *
     * @param event event structure
     * @param principleElementGUID unique identifier for the subject of the event
     * @param principleElementTypeName type of the subject of the event
     */
    private void sendEvent(CommunityProfileOutboundEvent event,
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
                                              CommunityProfileAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(event.getEventType().getEventTypeName(),
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
    public void sendEntityEvent(CommunityProfileOutboundEventType eventType,
                                String                            elementGUID,
                                String                            elementTypeName,
                                String                            classificationName,
                                ElementStub                       elementStub)
    {
        CommunityProfileOutboundEvent event = new CommunityProfileOutboundEvent();

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
    public void sendRelationshipEvent(CommunityProfileOutboundEventType eventType,
                                      String                            relationshipGUID,
                                      String                            relationshipTypeName,
                                      ElementStub                       relationshipElementStub,
                                      ElementStub                       endOneElementStub,
                                      ElementStub                       endTwoElementStub)
    {
        CommunityProfileOutboundEvent event = new CommunityProfileOutboundEvent();

        event.setEventType(eventType);
        event.setPrincipleElement(relationshipElementStub);
        event.setEndOneElement(endOneElementStub);
        event.setEndTwoElement(endTwoElementStub);

        sendEvent(event, relationshipGUID, relationshipTypeName);
    }


    /**
     * Send an event that indicates that a person has achieved a karma point plateau.
     *
     * @param profileElementStub profile of person
     * @param contributingUserId the userId that took them to the plateau
     * @param isPublic can this be sent to colleagues
     * @param pointsTotal the number of points that the person has achieved
     * @param plateau the plateau achieved
     */
    public void sendKarmaPointPlateauEvent(ElementStub profileElementStub,
                                           String       contributingUserId,
                                           boolean      isPublic,
                                           long         pointsTotal,
                                           long         plateau)
    {
        CommunityProfileOutboundEvent event = new CommunityProfileOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.KARMA_POINT_PLATEAU_EVENT);
        event.setPrincipleElement(profileElementStub);
        event.setUserId(contributingUserId);
        event.setIsPublic(isPublic);
        event.setPointsTotal(pointsTotal);
        event.setPlateau(plateau);

        sendEvent(event, profileElementStub.getGUID(), profileElementStub.getType().getTypeName());
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
                outTopicAuditLog.logMessage(actionDescription, CommunityProfileAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              CommunityProfileAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                  outTopicName,
                                                                                                                  error.getMessage()),
                                              error);
            }
        }
    }
}
