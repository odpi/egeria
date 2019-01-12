/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCVersionEnum;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
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
 * IGCOMRSRepositoryEventMapper supports the event mapper function for the IBM Information Server suite
 * (IBM InfoSphere Information Governance Catalog, Metadata Asset Manager, and Information Analyzer)
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

    private IGCVersionEnum igcVersion;
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
        log.info("IGC Event Mapper initializing...");

        // Setup IGC OMRS Repository connectivity
        this.igcomrsRepositoryConnector = (IGCOMRSRepositoryConnector) this.repositoryConnector;
        this.igcVersion = igcomrsRepositoryConnector.getIGCVersion();
        this.igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // TODO: Pick the best topic available based on the version of IGC (for now always default to InfosphereEvents)
        this.igcKafkaTopic = "InfosphereEvents";

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
        log.info("IGC Event Mapper starting...");
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
                if (embeddedConnector instanceof OpenMetadataTopicConnector) {
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
        if (this.eventBusConnectors.isEmpty() && auditLog != null) {
            OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_DEAF;
            auditLog.logRecord(methodName,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(repositoryEventMapperName),
                    this.getConnection().toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
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

            // TODO: Likely need to tweak these settings to give further processing time for large events
            //  like IMAM shares -- or even switch to manual offset management rather than auto-commits
            //  (see: https://kafka.apache.org/0110/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html)
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
        if (igcVersion.isEqualTo(IGCVersionEnum.V11702) || igcVersion.isHigherThan(IGCVersionEnum.V11702)) {
            processEventV117(event);
        } else {
            processEventV115(event);
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
            processAsset(rid, null, null);
        }

        // Then iterate through any updated entities
        for (String rid : updatedRIDs) {
            processAsset(rid, null, null);
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
                processAsset(event.getCreatedRID(), "data_connection", null);
                break;
            case InfosphereEventsDCEvent.ACTION_MODIFY:
                processAsset(event.getMergedRID(), "data_connection", null);
                break;
            default:
                log.warn("Found unhandled action type '{}' for data connection on event: {}", action, event);
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
                processAsset(assetRid, event.getAssetType(), null);
                break;
            case InfosphereEventsAssetEvent.ACTION_ASSIGNED_RELATIONSHIP:
                log.debug("Ignoring ASSIGNED_RELATIONSHIP event -- should be handled already by an earlier CREATE or MODIFY event: {}", event);
                break;
            default:
                log.warn("Action '{}' is not yet implemented: {}", action, event);
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

        switch(action) {
            case InfosphereEventsIAEvent.COL_ANALYZED:
            case InfosphereEventsIAEvent.COL_CLASSIFIED:
                // TODO: could potentially retrieve more from these via IA REST API...
                log.warn("Column / field analyzed or classified, but not yet published -- skipping: {}", event);
                break;
            case InfosphereEventsIAEvent.TBL_PUBLISHED:
                // This is the only event we can really do something with, as IGC API can only see
                // published information
                String containerRid = event.getDataCollectionRid();
                processAsset(containerRid, null, null);
                // We should also check the columns / file fields within the table / file for changes to be processed,
                // as the relationship itself between column and table may not change but there may be
                // new classifications on the columns / fields from the publication
                Reference containerAsset = igcRestClient.getAssetRefById(containerRid);
                String searchProperty = null;
                String searchAssetType = null;
                switch(containerAsset.getType()) {
                    case "database_table":
                        searchProperty = "database_table_or_view";
                        searchAssetType = "database_column";
                        break;
                    case "data_file_record":
                        searchProperty = "data_file_record";
                        searchAssetType = "data_file_field";
                        break;
                    default:
                        log.warn("Unimplemented asset type '{}' for IA publishing: {}", containerAsset.getType(), event);
                        break;
                }
                IGCSearchCondition igcSearchCondition = new IGCSearchCondition(searchProperty, "=", containerAsset.getId());
                IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
                IGCSearch igcSearch = new IGCSearch(searchAssetType, new String[]{ searchProperty }, igcSearchConditionSet);
                ReferenceList subAssets = igcRestClient.search(igcSearch);
                if (subAssets != null) {
                    subAssets.getAllPages(igcRestClient);
                    log.debug("Processing {} child assets from IA publication: {}", subAssets.getPaging().getNumTotal(), containerRid);
                    for (Reference child : subAssets.getItems()) {
                        processAsset(child.getId(), child.getType(), null);
                    }
                } else {
                    log.warn("Unable to find any sub-assets for IA published container '{}': {}", containerRid, event);
                }
                break;
            default:
                log.warn("Action '{}' is not yet implemented for IA: {}", action, event);
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
                log.warn("Unexpected number of tokens ({}) from event payload: {}", subTokens.length, payload);
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
     * @param relationshipGUID the relationship GUID that triggered this asset to be processed (or null if not triggered
     *                         by relationship being processed)
     */
    private void processAsset(String rid, String assetType, String relationshipGUID) {

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

            // Calculate the delta between the latest version and the previous saved stub
            ChangeSet changeSet = new ChangeSet(igcRestClient, latestVersion, stub);
            Set<String> changedProperties = changeSet.getChangedProperties();

            // Output any entities first
            if (stub == null) {
                // If there is no stub, we need to treat this as a new entity
                sendNewEntity(latestVersion);
            } else if (!changedProperties.isEmpty()) {
                // Otherwise, it should be treated as an updated entity, but only if there was some change
                sendUpdatedEntity(latestVersion, stub);
            } else {
                log.info("Skipping asset - no changes detected: {}", latestVersion.getId());
            }

            // Retrieve the mapping from IGC property name to OMRS relationship type
            Map<String, List<RelationshipMapping>> relationshipMap = igcomrsMetadataCollection.getIgcPropertiesToRelationshipMappings(
                    latestVersion.getType(),
                    localServerUserId
            );
            log.debug(" ... found mappings: {}", relationshipMap);

            // And then recursively process relationships (which will in turn recursively process further
            // assets), to ensure top-level entities are ultimately output before lower-level entities
            if (!changedProperties.isEmpty()) {
                // Iterate through the properties that differ, looking for any that represent a mapped relationship
                for (String igcProperty : changeSet.getChangedProperties()) {
                    log.debug(" ... checking for any relationship on: {}", igcProperty);
                    if (relationshipMap.containsKey(igcProperty)) {
                        List<ChangeSet.Change> changesForProperty = changeSet.getChangesForProperty(igcProperty);
                        log.debug(" ...... found differences for property: {}", changesForProperty);
                        for (RelationshipMapping relationshipMapping : relationshipMap.get(igcProperty)) {
                            processRelationships(relationshipMapping, latestVersion, changesForProperty, relationshipGUID);
                        }
                    }
                }
            }

            // Regardless of whether changedProperties is empty or not, we need to potentially process the
            // relationships for generated types (ie. where the igcProperty = self-reference sentinel), and this
            // self-reference sentinel will never be in the change set since it isn't a real property on the asset!
            if (relationshipMap.containsKey(RelationshipMapping.SELF_REFERENCE_SENTINEL)) {
                for (RelationshipMapping relationshipMapping : relationshipMap.get(RelationshipMapping.SELF_REFERENCE_SENTINEL)) {
                    processSelfReferencingRelationship(relationshipMapping, latestVersion, stub, relationshipGUID);
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
     * @param relationshipTriggerGUID the GUID of the relationship that triggered this processing (or null if not
     *                                triggered initially by the processing of another relationship)
     */
    private void processRelationships(RelationshipMapping relationshipMapping,
                                      Reference latestVersion,
                                      List<ChangeSet.Change> changesForProperty,
                                      String relationshipTriggerGUID) {

        log.debug("processRelationships called with relationshipMapping {}, reference {} and changes {}", relationshipMapping, latestVersion, changesForProperty);

        for (ChangeSet.Change change : changesForProperty) {

            String assetType = latestVersion.getType();
            Class pojo = igcRestClient.getPOJOForType(assetType);
            if (pojo != null) {
                List<String> referenceListProperties = Reference.getPagedRelationalPropertiesFromPOJO(pojo);

                Object relatedValue = change.getNewValue(referenceListProperties);
                log.debug(" ... found value: {}", relatedValue);
                if (relatedValue != null) {
                    if (Reference.isReferenceList(relatedValue)) {

                        log.debug(" ... found ReferenceList, processing each item");
                        ReferenceList related = (ReferenceList) relatedValue;
                        for (Reference relatedAsset : related.getItems()) {
                            processOneOrMoreRelationships(
                                    relationshipMapping,
                                    latestVersion,
                                    relatedAsset,
                                    change,
                                    relationshipTriggerGUID
                            );
                        }

                    } else if (Reference.isReference(relatedValue)) {
                        log.debug(" ... found single Reference, processing it");
                        Reference relatedAsset = (Reference) relatedValue;
                        processOneOrMoreRelationships(
                                relationshipMapping,
                                latestVersion,
                                relatedAsset,
                                change,
                                relationshipTriggerGUID
                        );
                    } else if (change.getIgcPropertyPath().endsWith("_id") && Reference.isSimpleType(relatedValue)) {
                        // In cases where a single object has been replaced, the JSON Patch may only show each property
                        // as needing replacement rather than the object as a whole -- so just watch for any changes to '_id'
                        // and if found pull back a new asset reference for the related asset to use
                        log.debug(" ... found single Reference by '_id', processing it");
                        Reference relatedAsset = igcRestClient.getAssetRefById((String) relatedValue);
                        processOneOrMoreRelationships(
                                relationshipMapping,
                                latestVersion,
                                relatedAsset,
                                change,
                                relationshipTriggerGUID
                        );
                    } else {
                        log.warn("Expected relationship for path '{}' for guid {} but found neither Reference nor ReferenceList: {}", change.getIgcPropertyPath(), latestVersion.getId(), relatedValue);
                    }
                } else {
                    log.warn("Expected relationship for path '{}' for guid {} but found nothing: {}", change.getIgcPropertyPath(), latestVersion.getId(), relatedValue);
                }
            } else {
                log.warn("No registered POJO to translate asset type '{}' for guid {} -- skipping its relationships.", assetType, latestVersion.getId());
            }

        }

    }

    /**
     * Maps the received assets to one or more assets that ultimately represent relationships of this type. In most
     * cases, this will simply be a one-to-one mapping, but this method ensures for the rare cases where the mapping
     * is one-to-many or many-to-many that all such relationships are processed.
     *
     * @param relationshipMapping the relationship mapping through which to translate the relationship
     * @param latestVersion the latest version (non-stub) for one end of the relationship
     * @param relatedAsset the latest version (non-stub) for the other end of the relationship
     * @param change the JSON Patch entry indicating a specific change
     * @param relationshipTriggerGUID passthrough of GUID for relationship that triggered this process (if not triggered
     *                                directly from an event), null if not triggered by another relationship
     */
    private void processOneOrMoreRelationships(RelationshipMapping relationshipMapping,
                                               Reference latestVersion,
                                               Reference relatedAsset,
                                               ChangeSet.Change change,
                                               String relationshipTriggerGUID) {

        String omrsRelationshipType = relationshipMapping.getOmrsRelationshipType();
        String latestVersionRID = latestVersion.getId();

        String relatedRID = relatedAsset.getId();

        // Only proceed if there is actually some related RID (ie. it wasn't just an empty list of relationships)
        if (relatedRID != null && !relatedRID.equals("null")) {

            log.debug("processOneOrMoreRelationships processing between {} and {} for type: {}", latestVersionRID, relatedRID, omrsRelationshipType);

            RelationshipMapping.ProxyMapping pmOne = relationshipMapping.getProxyOneMapping();
            RelationshipMapping.ProxyMapping pmTwo = relationshipMapping.getProxyTwoMapping();

            // Translate the provided assets into the actual assets needed for the relationship
            String latestVersionType = latestVersion.getType();
            String relatedAssetType = relatedAsset.getType();
            List<Reference> proxyOnes = new ArrayList<>();
            List<Reference> proxyTwos = new ArrayList<>();
            if (pmOne.matchesAssetType(latestVersionType) && pmTwo.matchesAssetType(relatedAssetType)) {
                proxyOnes = relationshipMapping.getProxyOneAssetFromAsset(latestVersion, igcRestClient);
                proxyTwos = relationshipMapping.getProxyTwoAssetFromAsset(relatedAsset, igcRestClient);
            } else if (pmTwo.matchesAssetType(latestVersionType) && pmOne.matchesAssetType(relatedAssetType)) {
                proxyOnes = relationshipMapping.getProxyOneAssetFromAsset(relatedAsset, igcRestClient);
                proxyTwos = relationshipMapping.getProxyTwoAssetFromAsset(latestVersion, igcRestClient);
            } else if (relationshipMapping.hasLinkingAsset()) {
                String linkingType = relationshipMapping.getLinkingAssetType();
                if ( (latestVersionType.equals(linkingType) && pmTwo.matchesAssetType(relatedAssetType))
                        || (relatedAssetType.equals(linkingType) && pmOne.matchesAssetType(latestVersionType)) ) {
                    proxyOnes = relationshipMapping.getProxyOneAssetFromAsset(latestVersion, igcRestClient);
                    proxyTwos = relationshipMapping.getProxyTwoAssetFromAsset(relatedAsset, igcRestClient);
                } else if ( (relatedAssetType.equals(linkingType) && pmTwo.matchesAssetType(latestVersionType))
                        || (latestVersionType.equals(linkingType) && pmOne.matchesAssetType(relatedAssetType)) ) {
                    proxyOnes = relationshipMapping.getProxyOneAssetFromAsset(relatedAsset, igcRestClient);
                    proxyTwos = relationshipMapping.getProxyTwoAssetFromAsset(latestVersion, igcRestClient);
                } else {
                    log.warn("Unable to match assets to proxies through linking asset '{}' for guids {} and {} through relationship type {} -- skipped, but this is likely indicative of a problem with the mapping.", linkingType, latestVersion.getId(), relatedAsset.getId(), omrsRelationshipType);
                }
            } else {
                log.warn("Unable to match assets {} and {} to proxies for any asset translation for relationship type {} -- skipped, but this is likely indicative of a problem with the mapping.", latestVersion.getId(), relatedAsset.getId(), omrsRelationshipType);
                proxyOnes.add(latestVersion);
                proxyTwos.add(relatedAsset);
            }

            for (Reference proxyOne : proxyOnes) {
                for (Reference proxyTwo : proxyTwos) {
                    processSingleRelationship(
                            relationshipMapping,
                            proxyOne,
                            proxyTwo,
                            change,
                            relationshipTriggerGUID
                    );
                }
            }

        } else {
            log.warn("Related RID for guid {} and relationship {} was null -- skipped.", latestVersion.getId(), omrsRelationshipType);
        }

    }

    /**
     * Processes a single relationship based on the provided mapping, IGC objects and JSON Patch entry. As part of the
     * processing, will determine automatically which IGC object provided should be which end of the relationship,
     * based on the provided mapping.
     *
     * @param relationshipMapping the relationship mapping through which to translate the relationship
     * @param latestVersion the latest version (non-stub) for one end of the relationship
     * @param relatedAsset the latest version (non-stub) for the other end of the relationship
     * @param change the JSON Patch entry indicating a specific change
     * @param relationshipTriggerGUID passthrough of GUID for relationship that triggered this process (if not triggered
     *                                directly from an event), null if not triggered by another relationship
     */
    private void processSingleRelationship(RelationshipMapping relationshipMapping,
                                           Reference latestVersion,
                                           Reference relatedAsset,
                                           ChangeSet.Change change,
                                           String relationshipTriggerGUID) {

        String omrsRelationshipType = relationshipMapping.getOmrsRelationshipType();
        String latestVersionRID = latestVersion.getId();

        String relatedRID = relatedAsset.getId();

        // Only proceed if there is actually some related RID (ie. it wasn't just an empty list of relationships)
        if (relatedRID != null && !relatedRID.equals("null")) {

            log.debug("processSingleRelationship processing between {} and {} for type: {}", latestVersionRID, relatedRID, omrsRelationshipType);

            RelationshipMapping.ProxyMapping pmOne = relationshipMapping.getProxyOneMapping();
            RelationshipMapping.ProxyMapping pmTwo = relationshipMapping.getProxyTwoMapping();

            String relationshipGUID = RelationshipMapping.getRelationshipGUID(
                    relationshipMapping,
                    latestVersion,
                    relatedAsset,
                    change.getIgcPropertyName(),
                    null
            );
            log.debug(" ... calculated relationship GUID: {}", relationshipGUID);

            // Only continue if this relationship is different from the one that triggered the processing in the first place
            if (relationshipTriggerGUID == null || !relationshipTriggerGUID.equals(relationshipGUID)) {
                String changeType = change.getOp();
                log.debug(" ... change action: {}", changeType);

                if (changeType.equals("remove")) {
                    try {
                        RelationshipDef relationshipDef = (RelationshipDef) igcomrsMetadataCollection.getTypeDefByName(
                                localServerUserId,
                                omrsRelationshipType
                        );
                        sendPurgedRelationship(relationshipDef, relationshipGUID);
                        // After purging the relationship, process any other updates
                        // on the assets at each end of the relationship
                        processAsset(RelationshipMapping.getProxyOneGUIDFromRelationshipGUID(relationshipGUID),
                                pmOne.getIgcAssetType(),
                                relationshipGUID);
                        processAsset(RelationshipMapping.getProxyTwoGUIDFromRelationshipGUID(relationshipGUID),
                                pmTwo.getIgcAssetType(),
                                relationshipGUID);
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
                        processAsset(relatedRID, relatedAsset.getType(), relationshipGUID);
                        processAsset(latestVersionRID, latestVersion.getType(), relationshipGUID);

                        // Send the appropriate patch-defined action
                        switch (changeType) {
                            case "add":
                                sendNewRelationship(relationship);
                                break;
                            case "replace":
                                sendUpdatedRelationship(relationship);
                                break;
                            default:
                                log.warn("Unknown action '{}' for relationship {}", changeType, relationshipGUID);
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
            } else {
                log.info("Relationship was same as one that triggered this processing -- skipping: {}", relationshipTriggerGUID);
            }

        } else {
            log.warn("Related RID for relationship type {} and guid {} was null.", omrsRelationshipType, latestVersionRID);
        }

    }

    /**
     * Process relationship for a self-referencing IGC entity (ie. one that splits between two OMRS entities).
     *
     * @param relationshipMapping the relationship mapping through which to translate the relationship
     * @param latestVersion the self-referencing IGC asset
     * @param stub the stub for the last version of the self-referencing IGC asset (or null if new)
     * @param relationshipTriggerGUID passthrough of GUID for relationship that triggered this process (if not triggered
     *                                directly from an event), null if not triggered by another relationship
     */
    private void processSelfReferencingRelationship(RelationshipMapping relationshipMapping,
                                                    Reference latestVersion,
                                                    OMRSStub stub,
                                                    String relationshipTriggerGUID) {

        String omrsRelationshipType = relationshipMapping.getOmrsRelationshipType();
        String latestVersionRID = latestVersion.getId();

        log.debug("processSelfReferencingRelationship processing for {} and type: {}", latestVersionRID, omrsRelationshipType);

        String relationshipGUID = RelationshipMapping.getRelationshipGUID(
                relationshipMapping,
                latestVersion,
                latestVersion,
                RelationshipMapping.SELF_REFERENCE_SENTINEL,
                null
        );
        log.debug(" ... calculated relationship GUID: {}", relationshipGUID);

        // Only continue if this relationship is different from the one that triggered the processing in the first place
        if (relationshipTriggerGUID == null || !relationshipTriggerGUID.equals(relationshipGUID)) {
            try {
                Relationship relationship = igcomrsMetadataCollection.getRelationship(localServerUserId, relationshipGUID);
                log.debug(" ... retrieved relationship: {}", relationship);
                // If there was no stub, this is a new entity
                if (stub == null) {
                    sendNewRelationship(relationship);
                } else {
                    sendUpdatedRelationship(relationship);
                }
                // Note: no need to call back to processAsset with prefixed GUID as it will have been handled already
                // by the base asset being processed
            } catch (RelationshipNotKnownException e) {
                log.error("Unable to find relationship with GUID: {}", relationshipGUID);
            } catch (InvalidParameterException | RepositoryErrorException e) {
                log.error("Unknown error occurred trying to retrieve relationship: {}", relationshipGUID, e);
            } catch (UserNotAuthorizedException e) {
                log.error("User not authorized to retrieve relationship: {}", relationshipGUID, e);
            }
        } else {
            log.info("Relationship was same as the one that triggered this processing -- skipping: {}", relationshipTriggerGUID);
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

            // TODO: for now this sends the same set of classifications every time, known design issue with how
            //  classifications are currently handled (to be changed once classifications are reworked)
            List<Classification> classifications = detail.getClassifications();
            if (classifications != null) {
                for (Classification classification : classifications) {
                    sendNewClassification(detail);
                }
            }

            // See if there are any generated entities to send an event for (ie. *Type)
            List<ReferenceableMapper> referenceableMappers = igcomrsMetadataCollection.getMappers(asset.getType(), localServerUserId);
            for (ReferenceableMapper referenceableMapper : referenceableMappers) {
                // Generated entities MUST have a prefix, so if there is no prefix ignore that mapper
                String ridPrefix = referenceableMapper.getIgcRidPrefix();
                if (ridPrefix != null) {
                    EntityDetail genDetail = getEntityDetailForAssetWithRID(asset, ridPrefix + asset.getId());
                    if (genDetail != null) {
                        repositoryEventProcessor.processNewEntityEvent(
                                sourceName,
                                metadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                null,
                                genDetail
                        );
                        // TODO: for now this sends the same set of classifications every time, known design issue with how
                        //  classifications are currently handled (to be changed once classifications are reworked)
                        classifications = genDetail.getClassifications();
                        if (classifications != null) {
                            for (Classification classification : classifications) {
                                sendNewClassification(genDetail);
                            }
                        }
                    } else {
                        log.warn("Unable to generate new entity for asset type {} with prefix {} and RID: {}", asset.getType(), ridPrefix, asset.getId());
                    }
                } else {
                    log.debug("No prefix found in mapper {}, skipping for generated new entity.", referenceableMapper.getClass().getCanonicalName());
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

            processClassifications(latest, latest.getClassifications(), last == null ? new ArrayList<>() : last.getClassifications());

            // See if there are any generated entities to send an event for (ie. *Type)
            List<ReferenceableMapper> referenceableMappers = igcomrsMetadataCollection.getMappers(latestVersion.getType(), localServerUserId);
            for (ReferenceableMapper referenceableMapper : referenceableMappers) {
                // Generated entities MUST have a prefix, so if there is no prefix ignore that mapper
                String ridPrefix = referenceableMapper.getIgcRidPrefix();
                if (ridPrefix != null) {
                    String prefixedRID = ridPrefix + latestVersion.getId();
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
                        processClassifications(genDetail, genDetail.getClassifications(), genLast == null ? new ArrayList<>() : genLast.getClassifications());
                    } else {
                        log.warn("Unable to generate updated entity for asset type {} with prefix {} and RID: {}", latestVersion.getType(), ridPrefix, latestVersion.getId());
                    }
                } else {
                    log.debug("No prefix found in mapper {}, skipping for generated update entity.", referenceableMapper.getClass().getCanonicalName());
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
     * Determine differences in classifications in order to send appropriate new, changed or remove events.
     *
     * @param detail the latest detail of the entity
     * @param latestClassifications the latest set of classifications for the entity
     * @param lastClassifications the set of classifications for the entity from its previous version
     */
    private void processClassifications(EntityDetail detail,
                                        List<Classification> latestClassifications,
                                        List<Classification> lastClassifications) {

        Map<String, Classification> latestClassificationByGUID = getClassificationMapFromList(latestClassifications);
        Map<String, Classification> lastClassificationByGUID = getClassificationMapFromList(lastClassifications);

        // First iterate through to see if there are any new classifications, keeping track of each classification
        // of the same type between the two lists
        ArrayList<String> matchingGUIDs = new ArrayList<>();
        for (String guid : latestClassificationByGUID.keySet()) {
            if (!lastClassificationByGUID.containsKey(guid)) {
                sendNewClassification(detail);
            } else {
                matchingGUIDs.add(guid);
            }
        }
        // Then iterate through the last version, to find any removed classifications
        for (String guid : lastClassificationByGUID.keySet()) {
            if (!latestClassificationByGUID.containsKey(guid)) {
                sendRemovedClassification(detail);
            }
        }
        // Finally, iterate through the classifications that are the same type and see if there are any changes
        for (String matchingGUID : matchingGUIDs) {
            Classification latest = latestClassificationByGUID.get(matchingGUID);
            Classification last = lastClassificationByGUID.get(matchingGUID);
            if (!latest.equals(last)) {
                sendChangedClassification(detail);
            }
        }

    }

    /**
     * Translate a list of classifications in to a Map keyed by the classification type's GUID.
     * (Note that this assumes a given classification type will only have one instance in the list!)
     *
     * @param classifications
     * @return
     */
    private Map<String, Classification> getClassificationMapFromList(List<Classification> classifications) {
        HashMap<String, Classification> map = new HashMap<>();
        if (classifications != null) {
            for (Classification classification : classifications) {
                String typeGUID = classification.getType().getTypeDefGUID();
                if (map.containsKey(typeGUID)) {
                    log.warn("Found multiple classifications of type {} -- clobbering!", typeGUID);
                }
                map.put(typeGUID, classification);
            }
        }
        return map;
    }

    /**
     * Send an event for a new classification being added to the entity's detail.
     *
     * @param detail
     */
    private void sendNewClassification(EntityDetail detail) {
        repositoryEventProcessor.processClassifiedEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                null,
                detail
        );
    }

    /**
     * Send an event for an existing classification being changed on an entity's detail.
     *
     * @param detail
     */
    private void sendChangedClassification(EntityDetail detail) {
        repositoryEventProcessor.processReclassifiedEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                null,
                detail
        );
    }

    /**
     * Send an event for a classification having been removed from an entity's detail.
     *
     * @param detail
     */
    private void sendRemovedClassification(EntityDetail detail) {
        repositoryEventProcessor.processDeclassifiedEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                null,
                detail
        );
    }

    /**
     * Send an event out on OMRS topic for a purged entity.
     *
     * @param assetTypeName the IGC display name of the asset type (ie. ASSET_TYPE from the event)
     * @param rid the IGC Repository ID (RID) of the asset
     */
    private void sendPurgedEntity(String assetTypeName, String rid) {

        // Purge entities by getting all mappers used for that entity (ie. *Type generated entities
        // as well as non-generated entities)
        String igcAssetType = igcomrsMetadataCollection.getIgcAssetTypeForAssetName(assetTypeName);
        if (igcAssetType != null) {
            List<ReferenceableMapper> referenceableMappers = igcomrsMetadataCollection.getMappers(igcAssetType, localServerUserId);
            for (ReferenceableMapper referenceableMapper : referenceableMappers) {
                String typeDefName = referenceableMapper.getOmrsTypeDefName();
                TypeDef typeDef = igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                        igcomrsRepositoryConnector.getRepositoryName(),
                        typeDefName
                );
                String ridToPurge = rid;
                String ridPrefix = referenceableMapper.getIgcRidPrefix();
                if (ridPrefix != null) {
                    ridToPurge = ridPrefix + ridToPurge;
                }
                repositoryEventProcessor.processPurgedEntityEvent(
                        sourceName,
                        metadataCollectionId,
                        originatorServerName,
                        originatorServerType,
                        null,
                        typeDef.getGUID(),
                        typeDef.getName(),
                        ridToPurge
                );
            }

            // Finally, remove the stub (so that if such an asset is created in the future it is recognised as new
            // rather than an update)
            igcomrsMetadataCollection.deleteOMRSStubForAsset(rid, assetTypeName);

        } else {
            log.warn("Unable to find asset type from display name '{}' for RID: {}", assetTypeName, rid);
        }

    }

    /**
     * Method to process events from v11.7 of Information Server.
     *
     * @param event inbound event
     */
    private void processEventV117(String event) {
        // TODO: implement processEventV117
        log.debug("Not yet implemented as v11.7-specific -- backing to v11.5 processing: {}", event);
        processEventV115(event);
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
