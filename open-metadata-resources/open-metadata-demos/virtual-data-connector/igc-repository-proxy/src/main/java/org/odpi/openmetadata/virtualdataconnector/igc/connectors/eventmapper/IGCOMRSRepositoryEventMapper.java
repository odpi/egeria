/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.model.IGCKafkaEvent;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.Context;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCColumn;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCObject;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.model.Constants.*;


/**
 * IGCOMRSRepositoryEventMapper provides an implementation of a repository event mapper for the
 * IBM Governance Catalog (IGC).
 */

public class IGCOMRSRepositoryEventMapper extends OMRSRepositoryEventMapperBase {

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSRepositoryEventMapper.class);

    private IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    private String originatorServerName;
    private String metadataCollectionId;
    private String originatorServerType;
    private String sourceName;
    private String originatorOrganizationName;

    /**
     * Default constructor
     */
    public IGCOMRSRepositoryEventMapper() {
    }

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException - there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException {
        super.start();
        this.igcomrsRepositoryConnector = (IGCOMRSRepositoryConnector) this.repositoryConnector;
        this.originatorServerName = this.localServerName;
        this.metadataCollectionId = igcomrsRepositoryConnector.getMetadataCollectionId();
        this.originatorServerType = "IGC";
        this.sourceName = "IGCOMRSRepositoryEventMapper";
        this.originatorOrganizationName = "ING";
        runConsumer();
    }

    /**
     * Start a new thread to read IGC kafka events.
     */
    public void runConsumer() {
        new Thread(new ConsumerThread()).start();
    }

    /**
     * Set properties for the kafka consumer.
     *
     * @return kafka consumer object
     */
    private Consumer<Long, String> createConsumer() {
        final Properties props = getProperties();
        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);

        String topicName = this.connectionBean.getEndpoint().getAddress().split("/")[1];
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }

    private Properties getProperties() {
        final Properties props = new Properties();

        String address = this.connectionBean.getEndpoint().getAddress().split("/")[0];
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_IGC_Consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return props;
    }

    private class ConsumerThread implements Runnable {

        /**
         * Read the Kafka events originating from IGC.
         */
        @Override
        public void run() {
            log.info("Started IGC Event Mapper");

            final Consumer<Long, String> consumer = createConsumer();
            ObjectMapper mapper = new ObjectMapper();

            while (true) {
                try {
                    ConsumerRecords<Long, String> events = consumer.poll(100);
                    for (ConsumerRecord<Long, String> event : events) {
                        IGCKafkaEvent igcKafkaEvent = getIGCKafkaEvent(mapper, event);
                        processEvent(igcKafkaEvent);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

    }

    private IGCKafkaEvent getIGCKafkaEvent(ObjectMapper mapper, ConsumerRecord<Long, String> event) throws java.io.IOException {
        final String eventContent = event.value();
        log.info("Consumer Record: {}", eventContent);

        return mapper.readValue(eventContent, IGCKafkaEvent.class);
    }

    private void processEvent(IGCKafkaEvent igcKafkaEvent) {
        switch (igcKafkaEvent.getEventType()) {
            case IMAM_SHARE_EVENT:
                processIMAM(igcKafkaEvent);
                break;
            case IGC_IMPORTED_ASSET_EVENT:
                processAssetImport(igcKafkaEvent);
                break;
            default:
                process(igcKafkaEvent);
                break;
        }
    }

    /**
     * Generate the OMRS topic for IGC events of importing assets
     *
     * @param igcKafkaEvent A Kafka event originating from IGC when importing assets
     */
    private void processAssetImport(IGCKafkaEvent igcKafkaEvent) {
        log.info("Process Asset Import started!");

        try {
            IGCObject igcObject = igcomrsRepositoryConnector.genericIGCQuery(igcKafkaEvent.getAssetRID());

            log.info("Process Asset Type: " + igcKafkaEvent.getAssetType() + " id = " + igcObject.getId() + " action = " + igcKafkaEvent.getAction());
            switch (igcKafkaEvent.getAssetType()) {
                case TERM:{

                    log.info("A term in the import action is to be processed.");

                    String glossaryCategoryName = getGlossaryCategoryName(igcObject);
                    String glossaryTermName = getGlossaryTermName(glossaryCategoryName, igcObject.getName());
                    createEntity(igcObject, false, GLOSSARY_TERM, glossaryTermName);

                    createRelationship(TERM_CATEGORIZATION,
                            igcObject.getContext().get(0).getId(),
                            GLOSSARY_CATEGORY,
                            glossaryCategoryName,
                            igcObject.getId(),
                            GLOSSARY_TERM,
                            glossaryTermName);

                    break;
                }
                case CATEGORY:{

                    log.info("A category in the import action is to be processed.");

                    String glossaryCategoryName = igcObject.getName();
                    log.info("The glossary name is " +  glossaryCategoryName);
                    createEntity(igcObject, false, GLOSSARY_CATEGORY, glossaryCategoryName);
                }
            }
        } catch (Exception e) {
            log.info("Unable to process Asset: " + e.getMessage());
        }
    }

    /**
     * Propagate the importation of databases into IGC through the OMRS.
     *
     * @param igcKafkaEvent A Kafka event originating from IGC
     */
    private void processIMAM(IGCKafkaEvent igcKafkaEvent) {
        log.info("Process IMAM started!");

        List<String> createdRIDs = igcKafkaEvent.getDatacollectionRID();
        for (String id : createdRIDs) {
            createSchema(id);
        }

        log.info("Process IMAM ended!");
    }

    private void createSchema(String id) {
        IGCObject igcTable = igcomrsRepositoryConnector.genericIGCQuery(id);
        IGCObject igcEndpoint = igcomrsRepositoryConnector.genericIGCQuery(igcTable.getContext().get(0).getId());
        IGCObject igcDatabase = igcomrsRepositoryConnector.genericIGCQuery(igcTable.getContext().get(1).getId());
        IGCObject igcSchema = igcomrsRepositoryConnector.genericIGCQuery(igcTable.getContext().get(2).getId());

        String tableQualifiedName = getQualifiedName(igcTable.getContext(), igcTable.getName());
        String tableTypeQualifiedName = getTypeQualifiedName(tableQualifiedName);
        String dbSchemaQualifiedName = getQualifiedName(igcSchema.getContext(), igcSchema.getName());
        String dbSchemaTypeQualifiedName = getTypeQualifiedName(dbSchemaQualifiedName);
        String databaseQualifiedName = getQualifiedName(igcDatabase.getContext(), igcDatabase.getName());
        String endpointQualifiedName = getQualifiedName(null, igcEndpoint.getName());

        createEntity(igcDatabase, false, DATABASE, databaseQualifiedName);
        createEntity(igcEndpoint, false, ENDPOINT, endpointQualifiedName);
        createEntity(igcSchema, false, DEPLOYED_DATABASE_SCHEMA, dbSchemaQualifiedName);
        createEntity(igcTable, false, RELATIONAL_TABLE, tableQualifiedName);
        createEntity(igcSchema, true, RELATIONAL_DB_SCHEMA_TYPE, dbSchemaTypeQualifiedName);
        createEntity(igcTable, true, RELATIONAL_TABLE_TYPE, tableTypeQualifiedName);

        String relationalDBSchemaTypeID = RELATIONAL_DB_SCHEMA_TYPE + "." + igcSchema.getId();
        String relationalTableTypeID = RELATIONAL_TABLE_TYPE + "." + igcTable.getId();

        createRelationship(
                DATA_CONTENT_FOR_DATA_SET,
                igcDatabase.getId(),
                DATABASE,
                databaseQualifiedName,
                igcSchema.getId(),
                DEPLOYED_DATABASE_SCHEMA,
                dbSchemaQualifiedName);

        createRelationship(
                ASSET_SCHEMA_TYPE,
                igcSchema.getId(),
                DEPLOYED_DATABASE_SCHEMA,
                dbSchemaQualifiedName,
                relationalDBSchemaTypeID,
                RELATIONAL_DB_SCHEMA_TYPE,
                dbSchemaTypeQualifiedName);

        createRelationship(
                ATTRIBUTE_FOR_SCHEMA,
                relationalDBSchemaTypeID,
                RELATIONAL_DB_SCHEMA_TYPE,
                dbSchemaTypeQualifiedName,
                igcTable.getId(),
                RELATIONAL_TABLE,
                tableQualifiedName);

        createRelationship(
                SCHEMA_ATTRIBUTE_TYPE,
                igcTable.getId(),
                RELATIONAL_TABLE,
                tableQualifiedName,
                relationalTableTypeID,
                RELATIONAL_TABLE_TYPE,
                tableTypeQualifiedName);

        //Propagate Connection/Connection type creations and relationships
        for (Item connection : igcDatabase.getDataConnections().getItems()) {
            IGCObject igcConnection = igcomrsRepositoryConnector.genericIGCQuery(connection.getId());
            IGCObject igcConnectorType = igcomrsRepositoryConnector.genericIGCQuery(igcConnection.getDataConnectors().getId());

            String connectionQualifiedName = getConnectionQualifiedName(igcConnection);
            createEntity(igcConnection, false, CONNECTION, connectionQualifiedName);

            String connectorTypeQualifiedName = getConnectorQualifiedName(igcConnectorType);
            createEntity(igcConnectorType, false, CONNECTOR_TYPE, connectorTypeQualifiedName);

            createRelationship(
                    CONNECTION_CONNECTOR_TYPE,
                    igcConnection.getId(),
                    CONNECTION,
                    connectionQualifiedName,
                    igcConnectorType.getId(),
                    CONNECTOR_TYPE,
                    connectorTypeQualifiedName);

            createRelationship(
                    CONNECTION_ENDPOINT,
                    igcEndpoint.getId(),
                    ENDPOINT,
                    endpointQualifiedName,
                    igcConnection.getId(),
                    CONNECTION,
                    connectionQualifiedName);
        }

        //Propagate RelationalColumns and relationships
        IGCObject databaseColumns = igcomrsRepositoryConnector.getDatabaseColumns(id, DEFAULT_PAGE_SIZE);
        for (Item column : databaseColumns.getDatabaseColumns().getItems()) {
            IGCColumn igcColumn = igcomrsRepositoryConnector.getIGCColumn(column.getId());

            String columnQualifiedName = getQualifiedName(igcColumn.getContext(), igcColumn.getName());
            createEntity(igcColumn, RELATIONAL_COLUMN, false, columnQualifiedName);

            String columnTypeQualifiedName = getTypeQualifiedName(columnQualifiedName);
            createEntity(igcColumn, RELATIONAL_COLUMN_TYPE, true, columnTypeQualifiedName);
            String relationalColumnTypeID = RELATIONAL_COLUMN_TYPE + "." + igcColumn.getId();

            createRelationship(
                    SCHEMA_ATTRIBUTE_TYPE,
                    igcColumn.getId(),
                    RELATIONAL_COLUMN,
                    columnQualifiedName,
                    relationalColumnTypeID,
                    RELATIONAL_COLUMN_TYPE,
                    columnTypeQualifiedName);

            createRelationship(
                    ATTRIBUTE_FOR_SCHEMA,
                    relationalTableTypeID,
                    RELATIONAL_TABLE_TYPE,
                    tableTypeQualifiedName,
                    igcColumn.getId(),
                    RELATIONAL_COLUMN,
                    columnQualifiedName);

            if (igcColumn.getDefinedForeignKey() != null &&
                    igcColumn.getDefinedForeignKeyReferences() != null) {
                for (Item foreignKey : igcColumn.getDefinedForeignKeyReferences().getItems()) {
                    final String foreignKeyId = foreignKey.getId();
                    IGCColumn foreignKeyRelationalColumn = igcomrsRepositoryConnector.getIGCColumn(foreignKeyId);
                    String foreignKeyQualifiedName = getQualifiedName(foreignKeyRelationalColumn.getContext(), foreignKeyRelationalColumn.getName());

                    createRelationship(
                            FOREIGN_KEY,
                            igcColumn.getId(),
                            RELATIONAL_COLUMN,
                            columnQualifiedName,
                            foreignKeyId,
                            RELATIONAL_COLUMN,
                            foreignKeyQualifiedName);
                }
            }
        }
    }

    /**
     * Process individual IGC Kafka events and publish them through OMRS.
     *
     * @param igcKafkaEvent a Kafka event originating from IGC.
     */
    private void process(IGCKafkaEvent igcKafkaEvent) {
        try {
            IGCObject igcObject = igcomrsRepositoryConnector.genericIGCQuery(igcKafkaEvent.getAssetRID());

            log.info("Process Asset Type: " + igcKafkaEvent.getAssetType() + " id = " + igcObject.getId() + " action = " + igcKafkaEvent.getAction());
            switch (igcKafkaEvent.getAssetType()) {
                case DATABASE_COLUMN:
                    //Relationship between a technical term and a glossary term.
                    if (igcKafkaEvent.getAction().equals("ASSIGNED_RELATIONSHIP")
                            || igcKafkaEvent.getAction().equalsIgnoreCase(MODIFY)) {
                        if (igcObject.getAssignedToTerms() != null) {
                            String glossaryTermID = igcObject.getAssignedToTerms().getItems().get(0).getId();
                            IGCObject glossaryTerm = igcomrsRepositoryConnector.genericIGCQuery(glossaryTermID);

                            String glossaryTermName = getGlossaryTermName(glossaryTerm);
                            String columnQualifiedName = getQualifiedName(igcObject.getContext(), igcObject.getName());

                            createRelationship(SEMANTIC_ASSIGNMENT,
                                    igcObject.getId(),
                                    RELATIONAL_COLUMN,
                                    columnQualifiedName,
                                    glossaryTermID,
                                    GLOSSARY_TERM,
                                    glossaryTermName);
                        }
                    }
                    break;
                case TERM:
                    if (igcKafkaEvent.getAction().equals(CREATE)) {
                        String glossaryCategoryName = getGlossaryCategoryName(igcObject);
                        String glossaryTermName = getGlossaryTermName(glossaryCategoryName, igcObject.getName());

                        createEntity(igcObject, false, GLOSSARY_TERM, glossaryTermName);
                        createEntity(igcObject, false, GLOSSARY_CATEGORY, glossaryCategoryName);

                        createRelationship(TERM_CATEGORIZATION,
                                igcObject.getContext().get(0).getId(),
                                GLOSSARY_CATEGORY,
                                glossaryCategoryName,
                                igcObject.getId(),
                                GLOSSARY_TERM,
                                glossaryTermName);
                    } else if (igcKafkaEvent.getAction().equals(MODIFY)) {
                        String glossaryCategoryName = getGlossaryCategoryName(igcObject);
                        String glossaryTermName = getGlossaryTermName(glossaryCategoryName, igcObject.getName());

                        updateGlossaryTerm(igcObject, glossaryTermName);
                    }
                    break;
                case CATEGORY:
                    if (igcKafkaEvent.getAction().equals(CREATE)) {
                        final String glossaryCategoryName = getGlossaryCategoryName(igcObject);
                        createEntity(igcObject, false, GLOSSARY_CATEGORY, glossaryCategoryName);
                    }
            }
        } catch (Exception e) {
            log.info("Unable to process Asset: " + e.getMessage());
        }
    }

    /**
     * Set relationship between two entities.
     *
     * @param relationshipType name of the type
     * @param entityId1        String unique identifier
     * @param entityType1      name of the type
     * @param entityId2        String unique identifier
     * @param entityType2      name of the type
     */
    private void createRelationship(String relationshipType,
                                    String entityId1, String entityType1, String qualifiedName1,
                                    String entityId2, String entityType2, String qualifiedName2) {

        Relationship relationship = getRelationship(relationshipType,
                entityId1, entityType1, qualifiedName1,
                entityId2, entityType2, qualifiedName2);

        if (relationship == null) {
            log.info("Unable to create a new relationship !" + relationshipType
                    + " between: " + entityId1 + " [" + entityType1 + "] and " + entityId2 + "[" + entityType2 + "]");
            return;
        }

        sendNewRelationshipEvent(relationship);

        log.info("[New Relationship Event] Created " + relationshipType + " between: " + entityId1 + " [" + entityType1 + "] and " + entityId2 + "[" + entityType2 + "]");
    }

    private void sendNewRelationshipEvent(Relationship relationship) {

        this.repositoryEventProcessor.processNewRelationshipEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                relationship
        );

    }

    private void updateGlossaryTerm(IGCObject igcObject, String qualifiedName) {

        final List<Classification> classifications = createClassifications(igcObject, CONFIDENTIALITY);
        InstanceProperties instanceProperties = getEntityProperties(GLOSSARY_TERM, igcObject, qualifiedName);

        EntityDetail entity = getEntityDetail(igcObject.getId(),
                GLOSSARY_TERM,
                false,
                igcObject.getCreatedBy(),
                instanceProperties,
                classifications);

        sendUpdateEntityEvent(entity);
    }

    private void sendUpdateEntityEvent(EntityDetail entity) {

        repositoryEventProcessor.processUpdatedEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                null,
                entity
        );

        log.info("[Entity] update the entity with type = " + entity.getType().getTypeDefName() + "; guid = " + entity.getGUID());
    }

    private void createEntity(IGCColumn igcColumn, String typeName, boolean avoidDuplicate, String qualifiedName) {

        InstanceProperties instanceProperties = getEntityProperties(
                qualifiedName,
                igcColumn.getShortDescription(),
                igcColumn.getLongDescription());

        List<Classification> classifications = new ArrayList<>();
        if (typeName.equals(RELATIONAL_COLUMN)) {
            Map<String, InstancePropertyValue> relationalColumnInstanceProperties = getRelationalColumnInstanceProperties(igcColumn);
            instanceProperties = combineInstanceProperties(relationalColumnInstanceProperties, instanceProperties);
            if (igcColumn.getDefinedPrimaryKey() != null) {
                classifications = getPrimaryKeyClassification();
            }
        }

        EntityDetail entityDetail = getEntityDetail(igcColumn.getId(), typeName, avoidDuplicate, igcColumn.getCreatedBy(), instanceProperties, classifications);

        sendNewEntityEvent(typeName, entityDetail);
    }

    private void createEntity(IGCObject igcObject, boolean avoidDuplicate, String typeName, String qualifiedName) {

        List<Classification> classifications = new ArrayList<>();
        if (GLOSSARY_TERM.equals(typeName)) {
            classifications = createClassifications(igcObject, CONFIDENTIALITY);
        }

        InstanceProperties instanceProperties = getEntityProperties(typeName, igcObject, qualifiedName);

        EntityDetail entityDetail = getEntityDetail(igcObject.getId(),
                typeName,
                avoidDuplicate,
                igcObject.getCreatedBy(),
                instanceProperties,
                classifications);

        sendNewEntityEvent(typeName, entityDetail);
    }

    private void sendNewEntityEvent(String typeName, EntityDetail entityDetail) {

        if (entityDetail == null) {
            log.info("Unable to create an entity" + typeName);
            return;
        }

        this.repositoryEventProcessor.processNewEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                entityDetail

        );

        log.info("[Entity] create new entity with type =  " + typeName + " guid = " + entityDetail.getGUID());
    }


    private Relationship getRelationship(String relationshipType,
                                         String entityId1, String entityType1, String qualifiedName1,
                                         String entityId2, String entityType2, String qualifiedName2) {

        Relationship relationship = null;
        try {
            relationship = this.repositoryHelper.getNewRelationship(
                    sourceName,
                    metadataCollectionId,
                    InstanceProvenanceType.LOCAL_COHORT,
                    null,
                    relationshipType,
                    null);


        } catch (TypeErrorException e) {
            log.info("Unable to create Relationship [type = " + relationshipType + "] :" + e.getErrorMessage());
        }

        if (relationship != null) {
            EntityProxy entityProxyOne = newEntityProxy(entityType1);
            EntityProxy entityProxyTwo = newEntityProxy(entityType2);

            if (entityProxyOne != null && entityProxyTwo != null) {
                entityProxyOne.setGUID(entityId1);
                entityProxyTwo.setGUID(entityId2);

                entityProxyOne.setUniqueProperties(getMandatoryProperty(qualifiedName1));
                entityProxyTwo.setUniqueProperties(getMandatoryProperty(qualifiedName2));

                relationship.setEntityOneProxy(entityProxyOne);
                relationship.setEntityTwoProxy(entityProxyTwo);
            }
        }

        return relationship;
    }

    private EntityProxy newEntityProxy(String typeName) {

        try {
            EntityProxy entityProxy = this.repositoryHelper.getNewEntityProxy(
                    sourceName,
                    metadataCollectionId,
                    InstanceProvenanceType.LOCAL_COHORT,
                    null,
                    typeName,
                    null,
                    null);
            entityProxy.setStatus(InstanceStatus.ACTIVE);

            return entityProxy;
        } catch (TypeErrorException e) {
            log.info("Unable to create Entity Proxy [type = " + typeName + "] :" + e.getErrorMessage());
        }

        return null;
    }

    private EntityDetail getEntityDetail(String id, String typeName, boolean avoidDuplicate, String username,
                                         InstanceProperties instanceProperties, List<Classification> classifications) {


        try {
            EntityDetail entityDetail = this.repositoryHelper.getNewEntity(
                    sourceName,
                    metadataCollectionId,
                    InstanceProvenanceType.LOCAL_COHORT,
                    username,
                    typeName,
                    instanceProperties,
                    classifications);
            entityDetail.setStatus(InstanceStatus.ACTIVE);

            if (avoidDuplicate) {
                entityDetail.setGUID(typeName + "." + id);
            } else {
                entityDetail.setGUID(id);
            }

            return entityDetail;
        } catch (TypeErrorException e) {
            log.info("Unable to create Entity [type = " + typeName + "] :" + e.getErrorMessage());
        }
        return null;
    }

    private List<Classification> getPrimaryKeyClassification() {

        List<Classification> classificationList = new ArrayList<>(1);

        try {
            Classification classification = repositoryHelper.getNewClassification(
                    sourceName,
                    null,
                    PRIMARY_KEY,
                    RELATIONAL_COLUMN,
                    ClassificationOrigin.PROPAGATED,
                    null,
                    null);

            classificationList.add(classification);
        } catch (TypeErrorException e) {
            log.info("Unable to create Primary Key Classification :" + e.getErrorMessage());
        }

        return classificationList;
    }

    private List<Classification> createClassifications(IGCObject igcObject, String classificationTypeName) {

        List<Classification> classifications = new ArrayList<>();

        if (igcObject.getRelatedTerms() != null && igcObject.getRelatedTerms().getItems() != null) {
            for (Item item : igcObject.getRelatedTerms().getItems()) {
                final Classification classification = getClassification(classificationTypeName, item.getName());
                if (classification != null) {
                    classifications.add(classification);
                }
            }
        }

        return classifications;
    }

    private Classification getClassification(String classificationTypeName, String name) {

        try {
            Classification classification = repositoryHelper.getNewClassification(
                    sourceName,
                    null,
                    classificationTypeName,
                    GLOSSARY_TERM,
                    ClassificationOrigin.PROPAGATED,
                    null,
                    null);

            InstanceProperties classificationProperties = getClassificationProperties(name);
            classification.setProperties(classificationProperties);
            return classification;
        } catch (TypeErrorException e) {
            log.info("Unable to create Classification [type = " + classificationTypeName + "] :" + e.getErrorMessage());
        }

        return null;
    }


    private InstanceProperties getMandatoryProperty(String qualifiedNameValue) {
        Map<String, InstancePropertyValue> properties = new HashMap<>();
        PrimitivePropertyValue qualifiedName = getStringPropertyValue(qualifiedNameValue);
        properties.put("qualifiedName", qualifiedName);

        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties.setInstanceProperties(properties);
        return instanceProperties;
    }

    private InstanceProperties getEntityProperties(String typeName, IGCObject igcObject, String qualifiedName) {

        InstanceProperties instanceProperties = getEntityProperties(
                qualifiedName,
                igcObject.getShortDescription(),
                igcObject.getLongDescription());

        if (GLOSSARY_TERM.equals(typeName)) {
            Map<String, InstancePropertyValue> glossaryTermInstanceProperties = getGlossaryTermInstanceProperties(igcObject);
            return combineInstanceProperties(glossaryTermInstanceProperties, instanceProperties);
        }

        return instanceProperties;
    }

    private InstanceProperties combineInstanceProperties(
            Map<String, InstancePropertyValue> newProperties,
            InstanceProperties instanceProperties) {

        Map<String, InstancePropertyValue> instanceProperties1 = instanceProperties.getInstanceProperties();
        newProperties.putAll(instanceProperties1);
        instanceProperties.setInstanceProperties(newProperties);
        return instanceProperties;
    }

    private Map<String, InstancePropertyValue> getGlossaryTermInstanceProperties(IGCObject igcObject) {
        Map<String, InstancePropertyValue> properties = new HashMap<>();

        PrimitivePropertyValue example = getStringPropertyValue(igcObject.getExample());
        properties.put("examples", example);

        PrimitivePropertyValue abbreviation = getStringPropertyValue(igcObject.getAbbreviation());
        properties.put("abbreviation", abbreviation);

        PrimitivePropertyValue usage = getStringPropertyValue(igcObject.getUsage());
        properties.put("usage", usage);
        return properties;
    }

    private Map<String, InstancePropertyValue> getRelationalColumnInstanceProperties(IGCColumn igcObject) {
        Map<String, InstancePropertyValue> properties = new HashMap<>();

        Boolean unique = igcObject.getUnique();
        Boolean nullable = igcObject.getAllowsNullValues();
        Boolean isPK = Boolean.FALSE;

        if (igcObject.getDefinedPrimaryKey() != null && igcObject.getDefinedPrimaryKey().getItems() != null) {
            unique = Boolean.TRUE;
            nullable = Boolean.FALSE;
            isPK = Boolean.TRUE;
        }
        PrimitivePropertyValue isUnique = getBooleanPropertyValue(unique);
        properties.put("isUnique", isUnique);
        PrimitivePropertyValue isNullable = getBooleanPropertyValue(nullable);
        properties.put("isNullable", isNullable);
        PrimitivePropertyValue isPrimaryKey = getBooleanPropertyValue(isPK);
        properties.put("isPrimaryKey", isPrimaryKey);

        return properties;
    }


    private InstanceProperties getEntityProperties(String name, String shortDescription, String longDescription) {
        Map<String, InstancePropertyValue> properties = new HashMap<>();

        PrimitivePropertyValue qualifiedName = getStringPropertyValue(name);
        properties.put("qualifiedName", qualifiedName);

        PrimitivePropertyValue displayName = getStringPropertyValue(name);
        properties.put("displayName", displayName);

        PrimitivePropertyValue summary = getStringPropertyValue(shortDescription);
        properties.put("summary", summary);

        PrimitivePropertyValue description = getStringPropertyValue(longDescription);
        properties.put("description", description);

        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties.setInstanceProperties(properties);
        return instanceProperties;
    }

    private InstanceProperties getClassificationProperties(String name) {
        Map<String, InstancePropertyValue> properties = new HashMap<>();

        EnumPropertyValue classificationLevel = getEnumPropertyValue(name);
        properties.put("level", classificationLevel);

        PrimitivePropertyValue source = getStringPropertyValue(sourceName);
        properties.put("source", source);

        EnumPropertyValue status = getEnumPropertyValue("Imported");
        properties.put("status", status);

        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties.setInstanceProperties(properties);

        return instanceProperties;
    }

    private PrimitivePropertyValue getStringPropertyValue(String value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveValue(value);
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);

        return propertyValue;
    }

    private PrimitivePropertyValue getBooleanPropertyValue(Boolean value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveValue(value);
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);

        return propertyValue;
    }

    private EnumPropertyValue getEnumPropertyValue(String value) {
        EnumPropertyValue propertyValue = new EnumPropertyValue();
        propertyValue.setSymbolicName(value);

        return propertyValue;
    }

    private String getGlossaryCategoryName(IGCObject igcObject) {
        final Map<String, Object> additionalProperties = igcObject.getAdditionalProperties();
        final HashMap<String, String> parent_category
                = (HashMap<String, String>) additionalProperties.get("parent_category");
        return parent_category.get("_name");
    }

    private String getGlossaryTermName(String glossaryCategoryName, String name) {
        return glossaryCategoryName + "." + name;
    }

    private String getGlossaryTermName(IGCObject igcObject) {
        return getGlossaryCategoryName(igcObject) + "." + igcObject.getName();
    }

    private String getConnectionQualifiedName(IGCObject igcObject) {
        final Map<String, Object> additionalProperties = igcObject.getAdditionalProperties();
        final String property = (String) additionalProperties.get("connection_string");

        return property + "." + CONNECTION;
    }

    private String getConnectorQualifiedName(IGCObject igcConnectorType) {
        return igcConnectorType.getName();
    }

    private String getQualifiedName(List<Context> contexts, String defaultName) {

        if (contexts == null || contexts.isEmpty()) {
            return defaultName;
        }

        StringBuilder contextName = new StringBuilder();
        for (Context context : contexts) {
            if (context.getType().equals("host")) {
                contextName.append(context.getName() + "." + CONNECTION + ".");
            } else {
                contextName.append(context.getName() + ".");
            }
        }

        return contextName.toString() + defaultName;
    }

    private String getTypeQualifiedName(String typeName) {
        return typeName + TYPE;
    }
}

