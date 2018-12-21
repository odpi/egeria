/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * IGCOMRSRepositoryEventMapper supports the event mapper function for the IBM InfoSphere Information Governance Catalog
 * when used as an open metadata repository.
 */
public class IGCOMRSRepositoryEventMapper extends OMRSRepositoryEventMapperBase
        implements VirtualConnectorExtension, OpenMetadataTopicListener {

    private List<Connector> embeddedConnectors = null;
    private List<OpenMetadataTopicConnector> eventBusConnectors = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSRepositoryEventMapper.class);

    private String sourceName;
    private IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    private IGCOMRSMetadataCollection igcomrsMetadataCollection;
    private String metadataCollectionId;
    private String originatorServerName;
    private String originatorServerType;

    private String igcVersion;
    private Properties igcKafkaProperties;
    private String igcKafkaBootstrap;
    private String igcKafkaTopic;

    private ObjectMapper mapper;

    /**
     * Default constructor
     */
    public IGCOMRSRepositoryEventMapper() {
        super();
        this.sourceName = "IGCOMRSRepositoryEventMapper";
    }


    /**
     * Pass additional information to the connector needed to process events.
     *
     * @param repositoryEventMapperName repository event mapper name used for the source of the OMRS events.
     * @param repositoryConnector ths is the connector to the local repository that the event mapper is processing
     *                            events from.  The repository connector is used to retrieve additional information
     *                            necessary to fill out the OMRS Events.
     */
    @Override
    public void initialize(String                  repositoryEventMapperName,
                           OMRSRepositoryConnector repositoryConnector) {

        super.initialize(repositoryEventMapperName, repositoryConnector);
        log.debug("IGC Event Mapper initializing...");

        // Setup IGC OMRS Repository connectivity
        this.igcomrsRepositoryConnector = (IGCOMRSRepositoryConnector) this.repositoryConnector;
        this.igcVersion = igcomrsRepositoryConnector.getIGCVersion();

        // Pick the best topic available based on the version of IGC
        if (igcVersion.equals(IGCRestClient.VERSION_115)) {
            this.igcKafkaTopic = "InfosphereEvents";
        } else if (igcVersion.equals(IGCRestClient.VERSION_117)) {
            this.igcKafkaTopic = "IgcUnifiedGovEvents";
        }

        // Retrieve connection details to configure Kafka connectivity
        this.igcKafkaBootstrap = this.connectionBean.getEndpoint().getAddress();
        igcKafkaProperties = new Properties();
        igcKafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, igcKafkaBootstrap);
        igcKafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "IGCOMRSRepositoryEventMapper_consumer");
        igcKafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        igcKafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Setup ObjectMapper for (de-)serialisation of events
        this.mapper = new ObjectMapper();
        this.mapper.enableDefaultTyping();

    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException {

        super.start();
        log.debug("IGC Event Mapper starting...");
        this.igcomrsMetadataCollection = (IGCOMRSMetadataCollection) igcomrsRepositoryConnector.getMetadataCollection();
        this.metadataCollectionId = igcomrsRepositoryConnector.getMetadataCollectionId();
        this.originatorServerName = igcomrsRepositoryConnector.getServerName();
        this.originatorServerType = igcomrsRepositoryConnector.getServerType();

        final String  methodName = "start";

        /*
         * Step through the embedded connectors, selecting only the OpenMetadataTopicConnectors
         * to use.
         */
        if (embeddedConnectors != null) {

            for (Connector  embeddedConnector : embeddedConnectors) {
                if ((embeddedConnector != null) && (embeddedConnector instanceof OpenMetadataTopicConnector)) {
                    /*
                     * Successfully found an event bus connector of the right type.
                     */
                    OpenMetadataTopicConnector realTopicConnector = (OpenMetadataTopicConnector)embeddedConnector;

                    String   topicName = realTopicConnector.registerListener(this);
                    this.eventBusConnectors.add(realTopicConnector);

                    if (auditLog != null) {
                        OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_REGISTERED;
                        auditLog.logRecord(methodName,
                                auditCode.getLogMessageId(),
                                auditCode.getSeverity(),
                                auditCode.getFormattedLogMessage(repositoryEventMapperName, topicName),
                                this.getConnection().toString(),
                                auditCode.getSystemAction(),
                                auditCode.getUserAction());
                    }
                }
            }
        }

        /*
         * OMRSTopicConnector needs at least one event bus connector to operate successfully.
         */
        if (this.eventBusConnectors.isEmpty()) {
            if (auditLog != null) {
                OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_DEAF;
                auditLog.logRecord(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(repositoryEventMapperName),
                        this.getConnection().toString(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
            }
        }

        log.info("Starting consumption from IGC Kafka bus.");
        new Thread(new IGCKafkaConsumerThread()).start();

    }


    /**
     * Class to support multi-threaded consumption of IGC Kafka events.
     */
    private class IGCKafkaConsumerThread implements Runnable {

        /**
         * Read IGC Infosphere topic Kafka events.
         */
        @Override
        public void run() {

            log.info("Starting IGC Event Mapper consumer thread.");
            final Consumer<Long, String> consumer = new KafkaConsumer<>(igcKafkaProperties);
            consumer.subscribe(Collections.singletonList(igcKafkaTopic));

            while (true) {
                try {
                    ConsumerRecords<Long, String> events = consumer.poll(100);
                    for (ConsumerRecord<Long, String> event : events) {
                        processEvent(event.value());
                    }
                } catch (Exception e) {
                    log.error("Failed trying to consume IGC events from Kafka.", e);
                }
            }
        }

    }


    /**
     * Registers itself as a listener of any OpenMetadataTopicConnectors that are passed as
     * embedded connectors.
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors) {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    @Override
    public void processEvent(String event) {
        log.debug("Processing event: {}", event);
        switch(igcVersion) {
            case IGCRestClient.VERSION_117:
                processEventV117(event);
                break;
            default:
                processEventV115(event);
                break;
        }
    }

    /**
     * Method to process events from v11.5 of Information Server.
     * Basically this method will simply route between processing IMAM events and normal asset events.
     *
     * @param event inbound event
     */
    public void processEventV115(String event) {

        try {
            InfosphereEvents eventObj = this.mapper.readValue(event, InfosphereEvents.class);
            switch(eventObj.getEventType()) {
                case "IMAM_SHARE_EVENT":
                    processIMAMShareEventV115((InfosphereEventsIMAMEvent)eventObj);
                    break;
                case "DC_CREATE_EVENT":
                case "DC_MERGED_EVENT":
                    processDataConnectionEventV115((InfosphereEventsDCEvent)eventObj);
                    break;
                case "IA_COLUMN_CLASSIFIED_EVENT":
                case "IA_COLUMN_ANALYZED_EVENT":
                case "IA_TABLE_RESULTS_PUBLISHED":
                    processIAEventV115((InfosphereEventsIAEvent)eventObj);
                    break;
                case "IA_PROJECT_CREATED_EVENT":
                case "IA_TABLE_ADDED_TO_PROJECT":
                case "IA_TABLE_REMOVED_FROM_PROJECT":
                case "IA_DATARULE_CREATED_EVENT":
                case "IA_COLUMN_ANALYSIS_SUBMITTED_EVENT":
                case "IA_DATAQUALITY_ANALYSIS_SUBMITTED":
                case "IA_COLUMN_ANALYSIS_STARTED_EVENT":
                case "IA_PROFILE_BATCH_COMPLETED_EVENT":
                case "IA_DATAQUALITY_ANALYSIS_STARTED_EVENT":
                case "IA_DATAQUALITY_ANALYSIS_FINISHED_EVENT":
                    log.info("Found Information Analyzer event that cannot be processed via APIs, skipping.");
                    break;
                default:
                    processAssetEventV115((InfosphereEventsAssetEvent)eventObj);
                    break;
            }
        } catch (IOException e) {
            log.error("Unable to translate event {} into object.", event, e);
        }

    }

    /**
     * Processes IMAM_SHARE_EVENT events from v11.5 of Information Server.
     *
     * @param event inbound event
     */
    public void processIMAMShareEventV115(InfosphereEventsIMAMEvent event) {
        // TODO: implement processIMAMShareEventV115
    }

    /**
     * Processes Data Connection events from v11.5 of Information Server.
     * @param event
     */
    public void processDataConnectionEventV115(InfosphereEventsDCEvent event) {

        String action = event.getEventType();
        EntityDetail detail;

        switch(action) {
            case InfosphereEventsDCEvent.ACTION_CREATE:
                detail = getEntityDetailForRID(event.getCreatedRID());
                if (detail != null) {
                    repositoryEventProcessor.processNewEntityEvent(
                            sourceName,
                            metadataCollectionId,
                            originatorServerName,
                            originatorServerType,
                            null,
                            detail
                    );
                }
                break;
            case InfosphereEventsDCEvent.ACTION_MODIFY:
                detail = getEntityDetailForRID(event.getMergedRID());
                if (detail != null) {
                    repositoryEventProcessor.processUpdatedEntityEvent(
                            sourceName,
                            metadataCollectionId,
                            originatorServerName,
                            originatorServerType,
                            null,
                            null,
                            detail
                    );
                }
                break;
            default:
                log.warn("Found unhandled action type for data connection: {}", action);
                break;
        }

    }

    /**
     * Processes all asset-specific events from v11.5 of Information Server.
     *
     * @param event inbound event
     */
    public void processAssetEventV115(InfosphereEventsAssetEvent event) {

        String assetRid = event.getAssetRid();
        String action = event.getAction();

        if (action.equals(InfosphereEventsAssetEvent.ACTION_CREATE)
                || action.equals(InfosphereEventsAssetEvent.ACTION_MODIFY)) {

            // Retrieve the detailed entity itself
            EntityDetail detail = getEntityDetailForRID(assetRid);
            log.debug("Retrieved EntityDetail: {}", detail);

            // And propagate based on whether it is new or modified
            switch (action) {
                case InfosphereEventsAssetEvent.ACTION_CREATE:
                    if (detail != null) {
                        log.debug("... sending as new entity event");
                        repositoryEventProcessor.processNewEntityEvent(
                                sourceName,
                                metadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                null,
                                detail
                        );
                    }
                    break;
                case InfosphereEventsAssetEvent.ACTION_MODIFY:
                    if (detail != null) {
                        log.debug("... sending as update entity event");
                        repositoryEventProcessor.processUpdatedEntityEvent(
                                sourceName,
                                metadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                null,
                                null,
                                detail
                        );
                    }
                    break;
            }

        } else if (action.equals(InfosphereEventsAssetEvent.ACTION_DELETE)) {

            // Note that IGC does not do soft deletes, so immediately send a purge
            // (We cannot send a delete first as we cannot lookup the entity to include with a delete event)
            String assetType = event.getAssetType();
            List<TypeDef> typeDefs = igcomrsMetadataCollection.getTypeDefsForAssetName(assetType);

            if (typeDefs == null || typeDefs.isEmpty()) {
                log.error("Unable to find any mapped type definition for asset type: {}", assetType);
            } else {
                if (typeDefs.size() > 1) {
                    log.warn("Found multiple type definitions mapped to asset type '{}' -- only purging first: {}", assetType, typeDefs);
                }
                TypeDef typeDef = typeDefs.get(0);
                repositoryEventProcessor.processPurgedEntityEvent(
                        sourceName,
                        metadataCollectionId,
                        originatorServerName,
                        originatorServerType,
                        null,
                        typeDef.getGUID(),
                        typeDef.getName(),
                        assetRid
                );
            }

        } else if (action.equals(InfosphereEventsAssetEvent.ACTION_ASSIGNED_RELATIONSHIP)) {

            // TODO: how to determine which relationship events to send?
            //  - there will be a CREATE or MODIFY on one asset, immediately followed by this ACTION_ASSIGNED_RELATIONSHIP
            //    {"ASSET_TYPE":"Term","ASSET_RID":"6662c0f2.e1b1ec6c.000mfk201.tuflodt.0oooqu.878gi1lu8mo4r0rl1i8ao","eventType":"IGC_BUSINESSTERM_EVENT","ASSET_CONTEXT":"TestSubCategory","ACTION":"CREATE","ASSET_NAME":"TestTerm"}
            //    {"ASSET_TYPE":"Category","ASSET_RID":"6662c0f2.ee6a64fe.000mfk1vs.5h84rl7.p4q6o9.g1709cvgh2g7vl79u104o","eventType":"IGC_BUSINESSCATEGORY_EVENT","ASSET_CONTEXT":"","ACTION":"ASSIGNED_RELATIONSHIP","ASSET_NAME":"TestSubCategory"}
            //    or
            //    {"ASSET_TYPE":"Category","ASSET_RID":"6662c0f2.ee6a64fe.000mfk1vs.5h84rl7.p4q6o9.g1709cvgh2g7vl79u104o","eventType":"IGC_BUSINESSCATEGORY_EVENT","ASSET_CONTEXT":"TestCategory","ACTION":"MODIFY","ASSET_NAME":"TestSubCategory"}
            //    {"ASSET_TYPE":"Category","ASSET_RID":"6662c0f2.ee6a64fe.000mfk1vp.7uociko.d39iml.ogmt0b235vqka3m9mlkv8","eventType":"IGC_BUSINESSCATEGORY_EVENT","ASSET_CONTEXT":"","ACTION":"ASSIGNED_RELATIONSHIP","ASSET_NAME":"TestCategory"}
            //  - the RIDs will not match (the first will be the changed object, the latter the target of the relationship)
            //  - if a relationship is removed, there will only be a MODIFY on the one asset (no related asset event)
            //    {"ASSET_TYPE":"Term","ASSET_RID":"6662c0f2.e1b1ec6c.000mfk201.tuflodt.0oooqu.878gi1lu8mo4r0rl1i8ao","eventType":"IGC_BUSINESSTERM_EVENT","ASSET_CONTEXT":"TestCategory","ACTION":"MODIFY","ASSET_NAME":"TestTerm"}

/*            repositoryEventProcessor.processNewRelationshipEvent(
                    sourceName,
                    metadataCollectionId,
                    originatorServerName,
                    originatorServerType,
                    null,
                    relationship
            ); */

        } else {
            log.warn("Action '{}' is not yet implemented: {}", action, assetRid);
        }

    }

    /**
     * Processes all process-able Information Analyzer events from v11.5 of Information Server.
     *
     * @param event inbound event
     */
    public void processIAEventV115(InfosphereEventsIAEvent event) {

        String action = event.getEventType();

        // TODO: figure out how we want to process these IA events

        switch(action) {

            case InfosphereEventsIAEvent.COL_ANALYZED:
                break;
            case InfosphereEventsIAEvent.COL_CLASSIFIED:
                break;
            case InfosphereEventsIAEvent.TBL_PUBLISHED:
                break;
            default:
                log.warn("Action is not yet implemented for IA: {}", action);
                break;
        }

    }

    /**
     * Attempt to retrieve the EntityDetail object for the provided IGC RID, and handle any errors if unable
     * to do so.
     *
     * @param rid the IGC Repository ID (RID) for which to retrieve an EntityDetail object
     * @return EntityDetail
     */
    public EntityDetail getEntityDetailForRID(String rid) {

        EntityDetail detail = null;
        try {
            detail = igcomrsMetadataCollection.getEntityDetail(igcomrsRepositoryConnector.getServerUserId(), rid);
        } catch (InvalidParameterException | RepositoryErrorException e) {
            log.error("Unexpected error in retrieving EntityDetail for RID: {}", rid, e);
        } catch (EntityNotKnownException | EntityProxyOnlyException e) {
            log.error("EntityDetail could not be found as entity did not exist in repository {}", rid, e);
        } catch (UserNotAuthorizedException e) {
            log.error("User not authorized to retrieve the EntityDetail for {}", rid, e);
        }
        return detail;

    }

    /**
     * Method to process events from v11.7 of Information Server.
     *
     * @param event inbound event
     */
    public void processEventV117(String event) {
        // TODO: implement processEventV117
        log.info("Not yet implemented -- skipping event: {}", event);
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException {
        super.disconnect();
        // TODO: stop Kafka consumption thread?
    }

}
