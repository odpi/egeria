/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * OMRSEventPublisher publishes OMRS Events to the supplied OMRSTopicConnector.
 */
public class OMRSRegistryEventPublisher implements OMRSRegistryEventProcessor
{
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER);

    private static final Logger log = LoggerFactory.getLogger(OMRSRegistryEventPublisher.class);

    private String             publisherName;
    private OMRSTopicConnector omrsTopicConnector;


    /**
     * Typical constructor sets up the local metadata collection id for events.
     *
     * @param publisherName  name of the cohort (or enterprise virtual repository) that this event publisher
     *                       is sending events to.
     * @param topicConnector OMRS Topic to send requests on
     */
    public OMRSRegistryEventPublisher(String publisherName,
                                      OMRSTopicConnector topicConnector)
    {
        super();

        String actionDescription = "Initialize event publisher";

        /*
         * Save the publisherName
         */
        this.publisherName = publisherName;

        /*
         * The topic connector is needed to publish events.
         */
        if (topicConnector == null)
        {
            log.debug("Null topic connector");

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(publisherName);

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
     * Send the registry event to the OMRS Topic connector and manage errors
     *
     * @param registryEvent properties of the event to send
     * @return boolean flag to report if the call succeeded or not.
     */
    public boolean sendRegistryEvent(OMRSRegistryEvent registryEvent)
    {
        final String actionDescription = "Send Registry Event";
        boolean      successFlag       = false;

        log.debug("Sending registryEvent for cohort: " + publisherName);
        log.debug("topicConnector: " + omrsTopicConnector);
        log.debug("registryEvent: " + registryEvent);
        log.debug("localEventOriginator: " + registryEvent.getEventOriginator());

        try
        {
            omrsTopicConnector.sendRegistryEvent(registryEvent);
            successFlag = true;
        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.SEND_REGISTRY_EVENT_ERROR;

            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(publisherName),
                                  "registryEvent : " + registryEvent.toString(),
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);

            log.debug("Exception: " + error + "; Registry Event: " + registryEvent);
        }

        return successFlag;
    }




    /**
     * Introduces the local server/repository to the metadata repository cohort.
     *
     * @param sourceName                 name of the source of the event.  It may be the cohort name for incoming events or the
     *                                   local repository, or event mapper name.
     * @param metadataCollectionId       unique identifier for the metadata collection that is registering with the cohort.
     * @param originatorServerName       name of the server that the event came from.
     * @param originatorServerType       type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param registrationTimestamp      the time that the server/repository issued the registration request.
     * @param remoteConnection           the Connection properties for the connector used to call the registering server.
     */
    public boolean processRegistrationEvent(String sourceName,
                                            String metadataCollectionId,
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
     * Refreshes the other servers in the cohort with the local server's registration.
     *
     * @param sourceName                 name of the source of the event.  It may be the cohort name for incoming events or the
     *                                   local repository, or event mapper name.
     * @param metadataCollectionId       unique identifier for the metadata collection that is registering with the cohort.
     * @param originatorServerName       name of the server that the event came from.
     * @param originatorServerType       type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param registrationTimestamp      the time that the server/repository first registered with the cohort.
     * @param remoteConnection           the Connection properties for the connector used to call the registering server.
     */
    public boolean processReRegistrationEvent(String sourceName,
                                              String metadataCollectionId,
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
                                                                remoteConnection);

        registryEvent.setEventOriginator(eventOriginator);

        return sendRegistryEvent(registryEvent);
    }


    /**
     * A server/repository is being removed from the metadata repository cohort.
     *
     * @param sourceName                 name of the source of the event.  It may be the cohort name for incoming events or the
     *                                   local repository, or event mapper name.
     * @param metadataCollectionId       unique identifier for the metadata collection that is registering with the cohort.
     * @param originatorServerName       name of the server that the event came from.
     * @param originatorServerType       type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     */
    public boolean processUnRegistrationEvent(String sourceName,
                                              String metadataCollectionId,
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
     * collection Id.  This means that their metadata instances can be updated in more than one server and there
     * is a potential for data integrity issues.
     *
     * @param sourceName                      name of the source of the event.  It may be the cohort name for incoming events or the
     *                                        local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                        sent the event.
     * @param originatorServerName            name of the server that the event came from.
     * @param originatorServerType            type of server that the event came from.
     * @param originatorOrganizationName      name of the organization that owns the server that sent the event.
     * @param conflictingMetadataCollectionId unique identifier for the remote metadata collection that is
     *                                        registering with the cohort.
     * @param errorMessage                    details of the conflict
     */
    public void processConflictingCollectionIdEvent(String sourceName,
                                                    String originatorMetadataCollectionId,
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
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId     Id for the repository with the bad remote connection.
     * @param remoteConnection               the Connection properties for the connector used to call the registering server.
     * @param errorMessage                   details of the error that occurs when the connection is used.
     */
    public void processBadConnectionEvent(String sourceName,
                                          String originatorMetadataCollectionId,
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