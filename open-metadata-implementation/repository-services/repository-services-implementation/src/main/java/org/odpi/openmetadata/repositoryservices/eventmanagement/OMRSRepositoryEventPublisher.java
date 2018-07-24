/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.auditlog.*;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;


/**
 * OMRSRepositoryEventPublisher publishes TypeDef and Instance OMRS Events to the supplied OMRSTopicConnector.
 */
public class OMRSRepositoryEventPublisher extends OMRSRepositoryEventBuilder
{
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER);

    private static final Logger log = LoggerFactory.getLogger(OMRSRepositoryEventPublisher.class);

    private OMRSTopicConnector omrsTopicConnector;


    /**
     * Typical constructor sets up the local metadata collection id for events.
     *
     * @param publisherName  name of the cohort (or enterprise virtual repository) that this event publisher
     *                       is sending events to.
     * @param topicConnector OMRS Topic to send requests on
     */
    public OMRSRepositoryEventPublisher(String publisherName,
                                        OMRSTopicConnector topicConnector)
    {
        super();

        String actionDescription = "Initialize event publisher";

        /*
         * The topic connector is needed to publish events.
         */
        if (topicConnector == null)
        {
            log.debug("Null topic connector");

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(publisherName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              actionDescription,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        this.omrsTopicConnector = topicConnector;

        log.debug("New Event Publisher: " + publisherName);
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
        log.debug("topicConnector: " + omrsTopicConnector);
        log.debug("typeDefEvent: " + typeDefEvent);
        log.debug("localEventOriginator: " + typeDefEvent.getEventOriginator());

        try
        {
            omrsTopicConnector.sendTypeDefEvent(typeDefEvent);
        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.SEND_TYPEDEF_EVENT_ERROR;

            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(sourceName),
                                  "typeDefEvent {" + typeDefEvent.toString() + "}",
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
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
        log.debug("topicConnector: " + omrsTopicConnector);
        log.debug("instanceEvent: " + instanceEvent);
        log.debug("localEventOriginator: " + instanceEvent.getEventOriginator());

        try
        {
            omrsTopicConnector.sendInstanceEvent(instanceEvent);
        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.SEND_INSTANCE_EVENT_ERROR;

            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(sourceName),
                                  "instanceEvent {" + instanceEvent.toString() + "}",
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);

            log.debug("Exception: ", error);
        }
    }
}