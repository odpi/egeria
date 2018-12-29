/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.ReferenceableMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model.OMRSStub;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

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
    private IGCRestClient igcRestClient;
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
        this.igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

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
    private void processEventV115(String event) {

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
    private void processIMAMShareEventV115(InfosphereEventsIMAMEvent event) {

        List<String> createdRIDs = getRIDsFromEventString(event.getCreatedRIDs());
        List<String> updatedRIDs = getRIDsFromEventString(event.getMergedRIDs());
        List<String> deletedRIDs = getRIDsFromEventString(event.getDeletedRIDs());

        // Start by creating any entities needed by the new RIDs
        for (String rid : createdRIDs) {
            processAsset(rid, null);
        }

        // Then iterate through any updated entities
        for (String rid : updatedRIDs) {
            processAsset(rid, null);
        }

        if (!deletedRIDs.isEmpty()) {
            log.warn("Unable to propagate IMAM deleted RIDs, cannot determine type: {}", deletedRIDs);
        }

    }

    /**
     * Processes Data Connection events from v11.5 of Information Server.
     * @param event
     */
    private void processDataConnectionEventV115(InfosphereEventsDCEvent event) {

        String action = event.getEventType();

        switch(action) {
            case InfosphereEventsDCEvent.ACTION_CREATE:
                processAsset(event.getCreatedRID(), "data_connection");
                break;
            case InfosphereEventsDCEvent.ACTION_MODIFY:
                processAsset(event.getMergedRID(), "data_connection");
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
    private void processAssetEventV115(InfosphereEventsAssetEvent event) {

        String assetRid = event.getAssetRid();
        String action = event.getAction();

        // And propagate based on the action of the event
        switch (action) {
            case InfosphereEventsAssetEvent.ACTION_CREATE:
            case InfosphereEventsAssetEvent.ACTION_MODIFY:
            case InfosphereEventsAssetEvent.ACTION_DELETE:
                processAsset(assetRid, event.getAssetType());
                break;
            case InfosphereEventsAssetEvent.ACTION_ASSIGNED_RELATIONSHIP:
                log.debug("Ignoring ASSIGNED_RELATIONSHIP event -- should be handled already by an earlier CREATE or MODIFY event: {}", event);
                break;
            default:
                log.warn("Action '{}' is not yet implemented: {}", action, assetRid);
                break;
        }

    }

    /**
     * Processes all process-able Information Analyzer events from v11.5 of Information Server.
     *
     * @param event inbound event
     */
    private void processIAEventV115(InfosphereEventsIAEvent event) {

        String action = event.getEventType();

        // TODO: figure out how we want to process these IA events

        switch(action) {

            case InfosphereEventsIAEvent.COL_ANALYZED:
                log.warn("Action is not yet implemented for IA: {}", action);
                break;
            case InfosphereEventsIAEvent.COL_CLASSIFIED:
                log.warn("Action is not yet implemented for IA: {}", action);
                break;
            case InfosphereEventsIAEvent.TBL_PUBLISHED:
                log.warn("Action is not yet implemented for IA: {}", action);
                break;
            default:
                log.warn("Action is not yet implemented for IA: {}", action);
                break;
        }

    }

    /**
     * Attempt to retrieve the EntityDetail object for the provided asset, and handle any errors if unable to do so.
     *
     * @param asset the IGC asset for which to retrieve an EntityDetail object
     * @return EntityDetail
     */
    private EntityDetail getEntityDetailForAsset(Reference asset) {
        return getEntityDetailForAssetWithRID(asset, asset.getId());
    }

    /**
     * Attempt to retrieve the EntityDetail object for the provided asset, using the provided Repository ID (RID).
     * Useful for when the RID indicates there is some generated entity that does not actually exist on its own in
     * IGC. Will handle any errors if unable to retrieve the asset, and the EntityDetail will simply be null.
     *
     * @param asset the IGC asset for which to retrieve an EntityDetail object
     * @param rid the Repository ID (RID) to use for the asset
     * @return EntityDetail
     */
    private EntityDetail getEntityDetailForAssetWithRID(Reference asset, String rid) {

        EntityDetail detail = null;
        try {
            detail = igcomrsMetadataCollection.getEntityDetail(localServerUserId, rid, asset);
        } catch (RepositoryErrorException e) {
            log.error("Unexpected error in retrieving EntityDetail for RID: {}", rid, e);
        }
        return detail;

    }

    /**
     * Attempt to retrieve the EntityDetail object for the provided OMRS stub, and handle any errors if unable
     * to do so.
     *
     * @param stub the OMRS stub for which to retrieve an EntityDetail object
     * @return EntityDetail
     */
    private EntityDetail getEntityDetailForStub(OMRSStub stub) {
        return getEntityDetailForStubWithRID(stub, null);
    }

    /**
     * Attempt to retrieve the EntityDetail object for the provided OMRS stub, using the provided Repository ID (RID).
     * Useful for when the RID indicates there is some generated entity that does not actually exist on its own in
     * IGC. Will handle any errors if unable to retrieve the asset, and the EntityDetail will simply be null.
     *
     * @param stub the OMRS stub for which to retrieve an EntityDetail object
     * @param rid the Repository ID (RID) to use for the asset
     * @return EntityDetail
     */
    private EntityDetail getEntityDetailForStubWithRID(OMRSStub stub, String rid) {

        EntityDetail detail = null;
        log.debug("Retrieving EntityDetail for stub: {}", stub);
        log.debug(" ... specifically this payload: {}", stub.getPayload());
        Reference asset = igcomrsRepositoryConnector.getIGCRestClient().readJSONIntoPOJO(stub.getPayload());
        asset.setFullyRetrieved();
        // If no RID was provided, take it from the asset we retrieved
        if (rid == null) {
            rid = asset.getId();
        }
        log.debug(" ... retrieved asset from stub: {}", asset);
        try {
            detail = igcomrsMetadataCollection.getEntityDetail(localServerUserId, rid, asset);
        } catch (RepositoryErrorException e) {
            log.error("Unexpected error in retrieving EntityDetail for stub: {}", stub.getId());
        }
        return detail;

    }

    /**
     * Parses the RIDs out of the provided payload string.
     *
     * @param payload a string of RIDs from an IMAM share event
     * @return List<String> of just the RIDs
     */
    private List<String> getRIDsFromEventString(String payload) {

        ArrayList<String> rids = new ArrayList<>();

        for (String token : payload.split(",")) {
            token = token.trim();
            String[] subTokens = token.split(":");
            if (subTokens.length == 2) {
                rids.add(subTokens[1]);
            } else {
                log.warn("Unexpected number of tokens from event payload: {}", token);
            }
        }

        return rids;

    }

    /**
     * Processes the provided asset according to what we determine about its status (eg. deleted, new, or updated).
     * Will also call into processRelationship as-needed if a relationship is detected as changed.
     *
     * @param rid the Repository ID (RID) of the asset in question
     * @param assetType the type of asset (ie. if provided in the event payload)
     */
    private void processAsset(String rid, String assetType) {

        log.debug("processAsset called with rid {} and type {}", rid, assetType);

        Reference latestVersion = igcomrsMetadataCollection.getFullAssetDetails(rid);

        if (latestVersion == null) {
            // If we can't retrieve the asset by RID, it no longer exists -- so send a delete event
            sendPurgedEntity(assetType, rid);
            // Find any mapper(s) for this type that use a prefix and send a purge for the prefixed entity as well
            List<ReferenceableMapper> referenceableMappers = igcomrsMetadataCollection.getMappers(assetType, localServerUserId);
            for (ReferenceableMapper referenceableMapper : referenceableMappers) {
                List<RelationshipMapping> relationshipMappings = referenceableMapper.getRelationshipMappers();
                for (RelationshipMapping relationshipMapping : relationshipMappings) {
                    String prefixOne = relationshipMapping.getProxyOneMapping().getIgcRidPrefix();
                    String prefixTwo = relationshipMapping.getProxyTwoMapping().getIgcRidPrefix();
                    if (prefixTwo != null) {
                        sendPurgedEntity(assetType, prefixTwo + rid);
                    }
                    if (prefixOne != null) {
                        sendPurgedEntity(assetType, prefixOne + rid);
                    }
                }
            }
        } else {

            // Otherwise see if there's a stub...
            OMRSStub stub = igcomrsMetadataCollection.getOMRSStubForAsset(latestVersion);

            if (stub == null) {
                // If there is no stub, we need to treat this as a new entity
                sendNewEntity(latestVersion);
            } else {
                // Otherwise, it should be treated as an updated entity
                sendUpdatedEntity(latestVersion, stub);
            }

            // Retrieve the mapping from IGC property name to OMRS relationship type
            Map<String, RelationshipMapping> relationshipMap = igcomrsMetadataCollection.getIgcPropertiesToRelationshipMappings(
                    latestVersion.getType(),
                    localServerUserId
            );
            log.debug(" ... found mappings: {}", relationshipMap);

            // Calculate the delta between the latest version and the previous saved stub
            ChangeSet changeSet = new ChangeSet(igcRestClient, latestVersion, stub);

            // Iterate through the properties that differ, looking for any that represent a mapped relationship
            for (String igcProperty : changeSet.getChangedProperties()) {
                log.debug(" ... checking for any relationship on: {}", igcProperty);
                if (relationshipMap.containsKey(igcProperty)) {
                    List<ChangeSet.Change> changesForProperty = changeSet.getChangesForProperty(igcProperty);
                    log.debug(" ...... found differences for property: {}", changesForProperty);
                    processRelationship(relationshipMap.get(igcProperty), latestVersion, changesForProperty);
                }
            }

        }

    }

    /**
     * Processes the provided relationship mapping to what we determine about its status (ie. new, updated, etc).
     * Will also call into processAsset as-needed before outputting the relationship to ensure that any referenced
     * asset has already been published as an event before the relationship is published.
     *
     * @param relationshipMapping the relationship mapping defining the IGC and OMRS properties and relationship type
     * @param latestVersion the latest version of the IGC asset from which to get the relationship(s)
     * @param changesForProperty the list of changes for the IGC relationship property being processed
     */
    private void processRelationship(RelationshipMapping relationshipMapping,
                                     Reference latestVersion,
                                     List<ChangeSet.Change> changesForProperty) {

        log.debug("processRelationship called with relationshipMapping {}, reference {} and changes {}", relationshipMapping, latestVersion, changesForProperty);

        String omrsRelationshipType = relationshipMapping.getOmrsRelationshipType();
        String latestVersionRID = latestVersion.getId();
        String prefixOne = relationshipMapping.getProxyOneMapping().getIgcRidPrefix();
        String prefixTwo = relationshipMapping.getProxyTwoMapping().getIgcRidPrefix();

        for (ChangeSet.Change change : changesForProperty) {

            Reference relatedAsset = (Reference) change.getNewValue();
            String relatedRID = relatedAsset.getId();
            String relationshipGUID = null;

            // Only proceed if there is actually some related RID (ie. it wasn't just an empty list of relationships)
            if (relatedRID != null && !relatedRID.equals("null")) {

                RelationshipMapping.ProxyMapping pmOne = relationshipMapping.getProxyOneMapping();

                if (relationshipMapping.sameTypeOnBothEnds() || pmOne.getIgcAssetType().equals(relatedAsset.getType())) {
                    // If mapping is same on both ends, its relationships were retrieved by inverted searches,
                    // so invert the assets when sending for relationship
                    relationshipGUID = RelationshipMapping.getRelationshipGUID(
                            relatedRID,
                            latestVersionRID,
                            omrsRelationshipType,
                            prefixOne,
                            prefixTwo,
                            null
                    );
                } else if (pmOne.getIgcAssetType().equals(latestVersion.getType())) {
                    relationshipGUID = RelationshipMapping.getRelationshipGUID(
                            latestVersionRID,
                            relatedRID,
                            omrsRelationshipType,
                            prefixOne,
                            prefixTwo,
                            null
                    );
                } else {
                    log.error("Unable to determine ends of relationship: {}", relationshipMapping.getOmrsRelationshipType());
                }
                log.debug(" ... calculated relationship GUID: {}", relationshipGUID);

                String changeType = change.getOp();
                log.debug(" ... change action: {}", changeType);

                if (changeType.equals("remove")) {
                    try {
                        RelationshipDef relationshipDef = (RelationshipDef) igcomrsMetadataCollection.getTypeDefByName(
                                localServerUserId,
                                omrsRelationshipType
                        );
                        sendPurgedRelationship(relationshipDef, relationshipGUID);
                    } catch (InvalidParameterException | RepositoryErrorException | TypeDefNotKnownException e) {
                        log.error("Unable to retrieve relationship type definition: {}", omrsRelationshipType, e);
                    } catch (UserNotAuthorizedException e) {
                        log.error("User not authorized to retrieve type definition: {}", omrsRelationshipType, e);
                    }
                } else {
                    try {

                        Relationship relationship = igcomrsMetadataCollection.getRelationship(localServerUserId, relationshipGUID);
                        log.debug(" ... retrieved relationship: {}", relationship);

                        // Recursively call processAsset(rid, null) on any non-deletion events
                        // (do this first: so relationship comes after on unwinding from recursion)
                        processAsset(relatedRID, relatedAsset.getType());

                        // Send the appropriate patch-defined action
                        switch (changeType) {
                            case "add":
                                sendNewRelationship(relationship);
                                break;
                            case "replace":
                                sendUpdatedRelationship(relationship);
                                break;
                            default:
                                log.warn("Unknown action '{}' for relationship: {}", changeType, relationship);
                                break;
                        }

                    } catch (RelationshipNotKnownException e) {
                        log.error("Unable to find relationship with GUID: {}", relationshipGUID);
                    } catch (InvalidParameterException | RepositoryErrorException e) {
                        log.error("Unknown error occurred trying to retrieve relationship: {}", relationshipGUID, e);
                    } catch (UserNotAuthorizedException e) {
                        log.error("User not authorized to retrieve relationship: {}", relationshipGUID, e);
                    }
                }

            }

        }

    }

    /**
     * Send an event out on OMRS topic for a new relationship.
     *
     * @param relationship the new relationship to publish
     */
    private void sendNewRelationship(Relationship relationship) {
        if (relationship != null) {
            repositoryEventProcessor.processNewRelationshipEvent(
                    sourceName,
                    metadataCollectionId,
                    originatorServerName,
                    originatorServerType,
                    null,
                    relationship
            );
        }
    }

    /**
     * Send an event out on OMRS topic for an updated relationship.
     *
     * @param relationship the updated relationship to publish
     */
    private void sendUpdatedRelationship(Relationship relationship) {
        if (relationship != null) {
            repositoryEventProcessor.processUpdatedRelationshipEvent(
                    sourceName,
                    metadataCollectionId,
                    originatorServerName,
                    originatorServerType,
                    null,
                    null,
                    relationship
            );
        }
    }

    /**
     * Send an event out on OMRS topic for a purged relationship.
     *
     * @param omrsRelationshipDef the relationship definition
     * @param relationshipGUID the GUID of the purged relationship
     */
    private void sendPurgedRelationship(RelationshipDef omrsRelationshipDef, String relationshipGUID) {
        repositoryEventProcessor.processPurgedRelationshipEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                null,
                omrsRelationshipDef.getGUID(),
                omrsRelationshipDef.getName(),
                relationshipGUID
        );
    }

    /**
     * Send an event out on OMRS topic for a new entity.
     *
     * @param asset the IGC asset for which we should send a new entity event
     */
    private void sendNewEntity(Reference asset) {

        EntityDetail detail = getEntityDetailForAsset(asset);

        if (detail != null) {

            // Send an event for the entity itself
            repositoryEventProcessor.processNewEntityEvent(
                    sourceName,
                    metadataCollectionId,
                    originatorServerName,
                    originatorServerType,
                    null,
                    detail
            );

            // See if there are any generated entities to send an event for (ie. *Type)
            List<ReferenceableMapper> referenceableMappers = igcomrsMetadataCollection.getMappers(asset.getType(), localServerUserId);
            for (ReferenceableMapper referenceableMapper : referenceableMappers) {
                List<RelationshipMapping> relationshipMappings = referenceableMapper.getRelationshipMappers();
                for (RelationshipMapping relationshipMapping : relationshipMappings) {
                    EntityDetail genDetail = null;
                    String prefixOne = relationshipMapping.getProxyOneMapping().getIgcRidPrefix();
                    String prefixTwo = relationshipMapping.getProxyTwoMapping().getIgcRidPrefix();
                    if (prefixTwo != null) {
                        genDetail = getEntityDetailForAssetWithRID(asset, prefixTwo + asset.getId());
                    } else if (prefixOne != null) {
                        genDetail = getEntityDetailForAssetWithRID(asset, prefixOne + asset.getId());
                    }
                    if (genDetail != null) {
                        repositoryEventProcessor.processNewEntityEvent(
                                sourceName,
                                metadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                null,
                                genDetail
                        );
                    }
                }
            }

            // Finally, update the stub with the latest version of the asset
            // (if any of the above fail, this will also be missed, so we will simply have more updates on the next event)
            igcomrsMetadataCollection.upsertOMRSStubForAsset(asset);

        } else {
            log.error("EntityDetail could not be retrieved for RID: {}", asset.getId());
        }
    }

    /**
     * Send an event out on OMRS topic for an updated entity.
     *
     * @param latestVersion the IGC asset for which we should send an updated entity event, in its current state
     * @param stub the OMRS stub for the asset, containing the last version for which we successfully sent an event
     */
    private void sendUpdatedEntity(Reference latestVersion, OMRSStub stub) {

        EntityDetail latest = getEntityDetailForAsset(latestVersion);

        if (latest != null) {

            // Send an event for the entity itself
            EntityDetail last = getEntityDetailForStub(stub);
            repositoryEventProcessor.processUpdatedEntityEvent(
                    sourceName,
                    metadataCollectionId,
                    originatorServerName,
                    originatorServerType,
                    null,
                    last,
                    latest
            );

            // See if there are any generated entities to send an event for (ie. *Type)
            List<ReferenceableMapper> referenceableMappers = igcomrsMetadataCollection.getMappers(latestVersion.getType(), localServerUserId);
            for (ReferenceableMapper referenceableMapper : referenceableMappers) {
                List<RelationshipMapping> relationshipMappings = referenceableMapper.getRelationshipMappers();
                for (RelationshipMapping relationshipMapping : relationshipMappings) {
                    String prefixOne = relationshipMapping.getProxyOneMapping().getIgcRidPrefix();
                    String prefixTwo = relationshipMapping.getProxyTwoMapping().getIgcRidPrefix();
                    String prefixedRID = null;
                    if (prefixTwo != null) {
                        prefixedRID = prefixTwo + latestVersion.getId();
                    } else if (prefixOne != null) {
                        prefixedRID = prefixOne + latestVersion.getId();
                    }
                    if (prefixedRID != null) {
                        EntityDetail genDetail = getEntityDetailForAssetWithRID(latestVersion, prefixedRID);
                        if (genDetail != null) {
                            EntityDetail genLast = getEntityDetailForStubWithRID(stub, prefixedRID);
                            repositoryEventProcessor.processUpdatedEntityEvent(
                                    sourceName,
                                    metadataCollectionId,
                                    originatorServerName,
                                    originatorServerType,
                                    null,
                                    genLast,
                                    genDetail
                            );
                        }
                    }
                }
            }

            // Finally, update the stub with the latest version of the asset
            // (if any of the above fail, this will also be missed, so we will simply have more updates on the next event)
            igcomrsMetadataCollection.upsertOMRSStubForAsset(latestVersion);

        } else {
            log.error("Latest EntityDetail could not be retrieved for RID: {}", latestVersion.getId());
        }

    }

    /**
     * Send an event out on OMRS topic for a purged entity.
     *
     * @param assetTypeName the IGC display name of the asset type (ie. ASSET_TYPE from the event)
     * @param rid the IGC Repository ID (RID) of the asset
     */
    private void sendPurgedEntity(String assetTypeName, String rid) {

        List<TypeDef> typeDefs = igcomrsMetadataCollection.getTypeDefsForAssetName(assetTypeName);

        if (typeDefs == null || typeDefs.isEmpty()) {
            log.error("Unable to find any mapped type definition for asset type: {}", assetTypeName);
        } else {
            if (typeDefs.size() > 1) {
                log.warn("Found multiple type definitions mapped to asset type '{}' -- only purging first: {}",
                        assetTypeName,
                        typeDefs);
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
                    rid
            );
            igcomrsMetadataCollection.deleteOMRSStubForAsset(rid, assetTypeName);
        }

    }

    /**
     * Method to process events from v11.7 of Information Server.
     *
     * @param event inbound event
     */
    private void processEventV117(String event) {
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
