/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEventErrorCode;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEventType;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


/**
 * OMRSRegistryEventPublisher publishes OMRS Events to the supplied OMRSTopicConnector.
 */
public class OMRSRegistryEventPublisher extends OMRSRegistryEventProcessor
{
    private static final Logger log = LoggerFactory.getLogger(OMRSRegistryEventPublisher.class);

    private final String                   publisherName;
    private final List<OMRSTopicConnector> omrsTopicConnectors;
    private final AuditLog auditLog;


    /**
     * Typical constructor sets up the local metadata collection id for events.
     *
     * @param publisherName  name of the cohort (or enterprise virtual repository) that this event publisher
     *                       is sending events to.
     * @param topicConnectors OMRS Topic to send requests on
     * @param auditLog       audit log for this component.
     */
    public OMRSRegistryEventPublisher(String                   publisherName,
                                      List<OMRSTopicConnector> topicConnectors,
                                      AuditLog                 auditLog)
    {
        super();

        String actionDescription = "Initialize event publisher";

        this.auditLog = auditLog;

        /*
         * Save the publisherName
         */
        this.publisherName = publisherName;

        /*
         * The topic connector is needed to publish events.
         */
        if ((topicConnectors == null) || (topicConnectors.isEmpty()))
        {
            log.debug("Null topic connector");

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(publisherName),
                                              this.getClass().getName(),
                                              actionDescription);

        }

        this.omrsTopicConnectors = topicConnectors;

        log.debug("New Event Publisher: " + publisherName);
    }


    /**
     * Send the registry event to the OMRS Topic connector and manage errors
     *
     * @param registryEvent properties of the event to send
     * @return boolean flag to report if the call succeeded or not.
     */
    private boolean sendRegistryEvent(OMRSRegistryEvent registryEvent)
    {
        final String actionDescription = "Send Registry Event";
        boolean      successFlag       = false;

        log.debug("Sending registryEvent for cohort: " + publisherName);
        log.debug("registryEvent: " + registryEvent);
        log.debug("localEventOriginator: " + registryEvent.getEventOriginator());

        try
        {
            List<CompletableFuture<Boolean>> results = new ArrayList<>();
            for (OMRSTopicConnector omrsTopicConnector : omrsTopicConnectors)
            {
                log.debug("topicConnector: " + omrsTopicConnector);
                results.add(omrsTopicConnector.sendRegistryEvent(registryEvent));
            }
            successFlag = results.stream().map(CompletableFuture::join).reduce(true, (r1, r2) -> r1 && r2);
        }
        // exceptions from sendEvent are wrapped in CompletionException
        catch (CompletionException exception)
        {
            auditLog.logException(actionDescription,
                                  OMRSAuditCode.SEND_REGISTRY_EVENT_ERROR.getMessageDefinition(publisherName),
                                  "registryEvent : " + registryEvent,
                                  exception.getCause());

            log.debug("Exception: " + exception.getCause() + "; Registry Event: " + registryEvent);
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OMRSAuditCode.SEND_REGISTRY_EVENT_ERROR.getMessageDefinition(publisherName),
                                  "registryEvent : " + registryEvent,
                                  error);

            log.debug("Exception: " + error + "; Registry Event: " + registryEvent);
        }

        return successFlag;
    }




    /**
     * Introduces a new server/repository to the metadata repository cohort.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param metadataCollectionId  unique identifier of metadata collection of originator.
     * @param metadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param registrationTimestamp  the time that the server/repository issued the registration request.
     * @param remoteConnection  the Connection properties for the connector used to call the registering server.
     * @return flag indicating if the event was sent or not.
     */
    public boolean processRegistrationEvent(String sourceName,
                                            String metadataCollectionId,
                                            String metadataCollectionName,
                                            String originatorServerName,
                                            String originatorServerType,
                                            String originatorOrganizationName,
                                            Date registrationTimestamp,
                                            Connection remoteConnection)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(metadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSRegistryEvent registryEvent = new OMRSRegistryEvent(OMRSRegistryEventType.REGISTRATION_EVENT,
                                                                registrationTimestamp,
                                                                metadataCollectionName,
                                                                remoteConnection);

        registryEvent.setEventOriginator(eventOriginator);

        return sendRegistryEvent(registryEvent);
    }


    /**
     * Requests that the other servers in the cohort send re-registration events.
     *
     * @param sourceName                 name of the source of the event.  It may be the cohort name for incoming events or the
     *                                   local repository, or event mapper name.
     * @param originatorServerName       name of the server that the event came from.
     * @param originatorServerType       type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     */
    public boolean processRegistrationRefreshRequest(String sourceName,
                                                     String originatorServerName,
                                                     String originatorServerType,
                                                     String originatorOrganizationName)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSRegistryEvent registryEvent = new OMRSRegistryEvent(OMRSRegistryEventType.REFRESH_REGISTRATION_REQUEST);

        registryEvent.setEventOriginator(eventOriginator);

        return sendRegistryEvent(registryEvent);
    }


    /**
     * Refreshes the other servers in the cohort with the originating server's registration.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param metadataCollectionId  unique identifier of metadata collection of originator.
     * @param metadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param registrationTimestamp  the time that the server/repository first registered with the cohort.
     * @param remoteConnection  the Connection properties for the connector used to call the registering server.
     * @return flag indicating if the event was sent or not.
     */
    public boolean processReRegistrationEvent(String sourceName,
                                              String metadataCollectionId,
                                              String metadataCollectionName,
                                              String originatorServerName,
                                              String originatorServerType,
                                              String originatorOrganizationName,
                                              Date registrationTimestamp,
                                              Connection remoteConnection)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(metadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSRegistryEvent registryEvent = new OMRSRegistryEvent(OMRSRegistryEventType.RE_REGISTRATION_EVENT,
                                                                registrationTimestamp,
                                                                metadataCollectionName,
                                                                remoteConnection);

        registryEvent.setEventOriginator(eventOriginator);

        return sendRegistryEvent(registryEvent);
    }


    /**
     * A server/repository is being removed from the metadata repository cohort.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param metadataCollectionId  unique identifier of metadata collection of originator.
     * @param metadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @return flag indicating if the event was sent or not.
     */
    public boolean processUnRegistrationEvent(String sourceName,
                                              String metadataCollectionId,
                                              String metadataCollectionName,
                                              String originatorServerName,
                                              String originatorServerType,
                                              String originatorOrganizationName)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(metadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSRegistryEvent registryEvent = new OMRSRegistryEvent(OMRSRegistryEventType.UN_REGISTRATION_EVENT);

        registryEvent.setEventOriginator(eventOriginator);

        return sendRegistryEvent(registryEvent);
    }


    /**
     * There is more than one member of the open metadata repository cohort that is using the same metadata
     * collection Id.  This means that their metadata instances can be updated in more than one server and their
     * is a potential for data integrity issues.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier of metadata collection of originator.
     * @param originatorMetadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param conflictingMetadataCollectionId  unique identifier for the metadata collection that is registering with the cohort.
     * @param errorMessage  details of the conflict
     */
    public void processConflictingCollectionIdEvent(String sourceName,
                                                    String originatorMetadataCollectionId,
                                                    String originatorMetadataCollectionName,
                                                    String originatorServerName,
                                                    String originatorServerType,
                                                    String originatorOrganizationName,
                                                    String conflictingMetadataCollectionId,
                                                    String errorMessage)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSRegistryEvent registryEvent = new OMRSRegistryEvent(OMRSRegistryEventErrorCode.CONFLICTING_COLLECTION_ID,
                                                                errorMessage,
                                                                conflictingMetadataCollectionId,
                                                                null);

        registryEvent.setEventOriginator(eventOriginator);

        sendRegistryEvent(registryEvent);
    }


    /**
     * A connection to one of the members of the open metadata repository cohort is not usable by one of the members.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier of metadata collection of originator.
     * @param originatorMetadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId  Id for the repository with the bad remote connection.
     * @param remoteConnection  the Connection properties for the connector used to call the registering server.
     * @param errorMessage  details of the error that occurs when the connection is used.
     */
    public void processBadConnectionEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorMetadataCollectionName,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          String targetMetadataCollectionId,
                                          Connection remoteConnection,
                                          String errorMessage)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSRegistryEvent registryEvent = new OMRSRegistryEvent(OMRSRegistryEventErrorCode.CONFLICTING_COLLECTION_ID,
                                                                errorMessage,
                                                                targetMetadataCollectionId,
                                                                remoteConnection);

        registryEvent.setEventOriginator(eventOriginator);

        sendRegistryEvent(registryEvent);
    }
}