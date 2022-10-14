/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;

import java.util.ArrayList;
import java.util.List;


/**
 * OMRSRepositoryEventPublisher publishes TypeDef and Instance OMRS Events to the supplied OMRSTopicConnector.
 */
public class OMRSRepositoryEventPublisher extends OMRSRepositoryEventBuilder
{
    private static final Logger log = LoggerFactory.getLogger(OMRSRepositoryEventPublisher.class);

    private OpenMetadataEventsSecurity securityVerifier = new OMRSMetadataDefaultEventsSecurity();

    private final List<OMRSTopicConnector>   typesTopicConnectors;
    private final List<OMRSTopicConnector>   instancesTopicConnectors;
    private final AuditLog                   auditLog;


    /**
     * Typical constructor sets up the local metadata collection id for events.
     *
     * @param publisherName  name of the cohort (or enterprise virtual repository) that this event publisher
     *                       is sending events to.
     * @param topicConnector OMRS Topic to send requests on
     * @param auditLog audit log for this component.
     */
    public OMRSRepositoryEventPublisher(String             publisherName,
                                        OMRSTopicConnector topicConnector,
                                        AuditLog           auditLog)
    {
        super(publisherName);

        String actionDescription = "Initialize event publisher";

        this.auditLog = auditLog;

        /*
         * The topic connector is needed to publish events.
         */
        if (topicConnector == null)
        {
            log.debug("Null topic connector");

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(publisherName),
                                              this.getClass().getName(),
                                              actionDescription);

        }

        typesTopicConnectors = new ArrayList<>();
        typesTopicConnectors.add(topicConnector);

        instancesTopicConnectors = new ArrayList<>();
        instancesTopicConnectors.add(topicConnector);

        log.debug("New Event Publisher: " + publisherName);
    }


    /**
     * Typical constructor sets up the local metadata collection id for events.
     *
     * @param publisherName  name of the cohort (or enterprise virtual repository) that this event publisher
     *                       is sending events to.
     * @param typesTopicConnectors list of OMRS Topic connectors to send type requests on
     * @param instancesTopicConnectors list of OMRS Topic connectors to send type requests on
     * @param auditLog audit log for this component.
     */
    public OMRSRepositoryEventPublisher(String                   publisherName,
                                        List<OMRSTopicConnector> typesTopicConnectors,
                                        List<OMRSTopicConnector> instancesTopicConnectors,
                                        AuditLog                 auditLog)
    {
        super(publisherName);

        String actionDescription = "Initialize event publisher";

        this.auditLog = auditLog;

        /*
         * The topic connector(s) is/are needed to publish events.
         */
        if ((typesTopicConnectors == null) || (typesTopicConnectors.isEmpty()))
        {
            log.debug("Null topic connector");

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(publisherName + " (types)"),
                                              this.getClass().getName(),
                                              actionDescription);

        }

        if ((instancesTopicConnectors == null) || (instancesTopicConnectors.isEmpty()))
        {
            log.debug("Null topic connector");

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(publisherName + " (instances)"),
                                              this.getClass().getName(),
                                              actionDescription);

        }

        this.typesTopicConnectors = typesTopicConnectors;
        this.instancesTopicConnectors = instancesTopicConnectors;

        log.debug("New Event Publisher: " + publisherName);
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for whether individual events should be sent/received.
     * Authorization checks are enabled through the OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataEventsSecurity securityVerifier)
    {
        if (securityVerifier != null)
        {
            this.securityVerifier = securityVerifier;
        }
    }


    /**
     * Send the TypeDef event to the OMRS Topic connector (providing TypeDef Events are enabled).
     *
     * @param sourceName name of caller
     * @param typeDefEvent properties of the event to send
     */
    public void sendTypeDefEvent(String           sourceName,
                                 OMRSTypeDefEvent typeDefEvent)
    {
        String actionDescription = "Send TypeDef Event";

        log.debug("Sending typeDefEvent for cohort: " + sourceName);
        log.debug("typeDefEvent: " + typeDefEvent);
        log.debug("localEventOriginator: " + typeDefEvent.getEventOriginator());

        try
        {
            for (OMRSTopicConnector omrsTopicConnector : typesTopicConnectors)
            {
                log.debug("topicConnector: " + omrsTopicConnector);
                omrsTopicConnector.sendTypeDefEvent(typeDefEvent);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OMRSAuditCode.SEND_TYPEDEF_EVENT_ERROR.getMessageDefinition(sourceName),
                                  "typeDefEvent {" + typeDefEvent.toString() + "}",
                                  error);

            log.debug("Exception: ", error);
        }
    }


    /**
     * Set the instance event to the OMRS Topic connector if the instance
     * event is of the permitted type.
     *
     * @param sourceName name of caller
     * @param instanceEvent properties of the event to send
     */
    public void sendInstanceEvent(String            sourceName,
                                  OMRSInstanceEvent instanceEvent)
    {
        String actionDescription = "Send Instance Event";

        log.debug("Sending instanceEvent for cohort: " + sourceName);
        log.debug("instanceEvent: " + instanceEvent);
        log.debug("localEventOriginator: " + instanceEvent.getEventOriginator());

        try
        {

            OMRSInstanceEvent validatedEvent = securityVerifier.validateOutboundEvent(eventProcessorName, instanceEvent);

            if (validatedEvent != null)
            {
                for (OMRSTopicConnector omrsTopicConnector : instancesTopicConnectors)
                {
                    log.debug("topicConnector: " + omrsTopicConnector);
                    omrsTopicConnector.sendInstanceEvent(instanceEvent);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OMRSAuditCode.SEND_INSTANCE_EVENT_ERROR.getMessageDefinition(sourceName),
                                  "instanceEvent {" + instanceEvent.toString() + "}",
                                  error);

            log.debug("Exception: ", error);
        }
    }
}