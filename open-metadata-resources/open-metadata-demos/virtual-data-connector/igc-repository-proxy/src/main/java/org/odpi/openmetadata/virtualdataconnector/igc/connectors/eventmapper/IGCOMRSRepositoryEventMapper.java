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
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCObject;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
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
            while (true) {
                try {
                    ConsumerRecords<Long, String> records = consumer.poll(100);
                    for (ConsumerRecord<Long, String> record : records) {
                        ObjectMapper mapper = new ObjectMapper();
                        IGCKafkaEvent igcKafkaEvent = mapper.readValue(record.value(), IGCKafkaEvent.class);
                        log.info("Consumer Record: {}", record.value());

                        processEvent(igcKafkaEvent);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

    }

    private void processEvent(IGCKafkaEvent igcKafkaEvent) throws TypeErrorException {
        switch (igcKafkaEvent.getEventType()) {
            case IMAM_SHARE_EVENT:
                processIMAM(igcKafkaEvent);
                break;
            default:
                processAsset(igcKafkaEvent);
                break;
        }
    }

    /**
     * Propagate the importation of databases into IGC through the OMRS.
     *
     * @param igcKafkaEvent A Kafka event originating from IGC
     * @throws TypeErrorException
     */
    private void processIMAM(IGCKafkaEvent igcKafkaEvent) throws TypeErrorException {
        IGCObject igcTable = igcomrsRepositoryConnector.genericIGCQuery(igcKafkaEvent.getDatacollectionRID());
        IGCObject igcEndpoint = igcomrsRepositoryConnector.genericIGCQuery(igcTable.getContext().get(0).getId());
        IGCObject igcDatabase = igcomrsRepositoryConnector.genericIGCQuery(igcTable.getContext().get(1).getId());
        IGCObject igcSchema = igcomrsRepositoryConnector.genericIGCQuery(igcTable.getContext().get(2).getId());

        createEntity(igcDatabase, DATABASE, false);
        createEntity(igcEndpoint, ENDPOINT, false);
        createEntity(igcSchema, DEPLOYED_DATABASE_SCHEMA, false);
        createEntity(igcTable, RELATIONAL_TABLE, false);

        createEntity(igcSchema, RELATIONAL_DB_SCHEMA_TYPE, true);
        createEntity(igcTable, RELATIONAL_TABLE_TYPE, true);

        String relationalDBSchemaTypeID = RELATIONAL_DBSCHEMA_TYPE_RID + igcSchema.getId();
        String relationalTableTypeID = RELATIONAL_TABLE_TYPE_RID + igcTable.getId();

        createRelationship(
                DATA_CONTENT_FOR_DATA_SET,
                igcDatabase.getId(),
                DATABASE,
                igcSchema.getId(),
                DEPLOYED_DATABASE_SCHEMA);

        createRelationship(
                ASSET_SCHEMA_TYPE,
                igcSchema.getId(),
                DEPLOYED_DATABASE_SCHEMA,
                relationalDBSchemaTypeID,
                RELATIONAL_DB_SCHEMA_TYPE);

        createRelationship(
                ATTRIBUTE_FOR_SCHEMA,
                relationalDBSchemaTypeID,
                RELATIONAL_DB_SCHEMA_TYPE,
                igcTable.getId(),
                RELATIONAL_TABLE);

        createRelationship(
                SCHEMA_ATTRIBUTE_TYPE,
                igcTable.getId(),
                RELATIONAL_TABLE,
                relationalTableTypeID,
                RELATIONAL_TABLE_TYPE);

        //Propagate Connection/Connection type creations and relationships
        for (Item connection : igcDatabase.getDataConnections().getItems()) {
            IGCObject igcConnection = igcomrsRepositoryConnector.genericIGCQuery(connection.getId());
            IGCObject igcConnectorType = igcomrsRepositoryConnector.genericIGCQuery(igcConnection.getDataConnectors().getId());
            createEntity(igcConnection, CONNECTION, false);
            createEntity(igcConnectorType, CONNECTOR_TYPE, false);

            createRelationship(
                    CONNECTION_CONNECTOR_TYPE,
                    igcConnection.getId(),
                    CONNECTION,
                    igcConnectorType.getId(),
                    CONNECTOR_TYPE);

            createRelationship(
                    CONNECTION_ENDPOINT,
                    igcEndpoint.getId(),
                    ENDPOINT,
                    igcConnection.getId(),
                    CONNECTION);
        }

        //Propagate RelationalColumns and relationships
        for (Item column : igcTable.getDatabaseColumns().getItems()) {
            IGCObject igcColumn = igcomrsRepositoryConnector.genericIGCQuery(column.getId());

            createEntity(igcColumn, RELATIONAL_COLUMN, false);
            createEntity(igcColumn, RELATIONAL_COLUMN_TYPE, true);
            String relationalColumnTypeID = RELATIONAL_COLUMN_TYPE_RID + igcColumn.getId();

            createRelationship(
                    SCHEMA_ATTRIBUTE_TYPE,
                    igcTable.getId(),
                    RELATIONAL_COLUMN,
                    relationalColumnTypeID,
                    RELATIONAL_COLUMN_TYPE);

            createRelationship(
                    ATTRIBUTE_FOR_SCHEMA,
                    igcTable.getId(),
                    RELATIONAL_COLUMN,
                    relationalTableTypeID,
                    RELATIONAL_TABLE_TYPE);


        }
    }

    /**
     * Process individual IGC Kafka events and publish them through OMRS.
     *
     * @param igcKafkaEvent a Kafka event originating from IGC.
     */
    private void processAsset(IGCKafkaEvent igcKafkaEvent) {
        try {
            IGCObject igcObject = igcomrsRepositoryConnector.genericIGCQuery(igcKafkaEvent.getAssetRID());

            switch (igcKafkaEvent.getAssetType()) {
                case DATABASE_COLUMN:
                    //Relationship between a technical term and a glossary term.
                    if (igcKafkaEvent.getAction().equals("ASSIGNED_RELATIONSHIP")
                            || igcKafkaEvent.getAction().equalsIgnoreCase(MODIFY)) {
                        if (igcObject.getAssignedToTerms() != null) {
                            String glossaryTermID = igcObject.getAssignedToTerms().getItems().get(0).getId();

                            createRelationship(SEMANTIC_ASSIGNMENT,
                                    igcObject.getId(),
                                    RELATIONAL_COLUMN,
                                    glossaryTermID,
                                    GLOSSARY_TERM);
                        }
                    }
                    break;
                case TERM:
                    if (igcKafkaEvent.getAction().equals(CREATE)) {
                        //Creation of a glossary term.
                        createEntity(igcObject, GLOSSARY_TERM, false);
                        if (igcKafkaEvent.getAction().equals(CREATE)) {
                            //Creation of a  glossary category and assignment to its glossary category.
                            createEntity(igcObject, GLOSSARY_CATEGORY, false);
                            createRelationship(TERM_CATEGORIZATION,
                                    igcObject.getContext().get(0).getId(),
                                    GLOSSARY_CATEGORY,
                                    igcObject.getId(),
                                    GLOSSARY_TERM);
                        }
                    } else if (igcKafkaEvent.getAction().equals(MODIFY)) {
                        if (igcObject.getRelatedTerms() != null && igcObject.getRelatedTerms().getItems() != null) {
                            for (Item item : igcObject.getRelatedTerms().getItems()) {
                                createClassification(igcObject, CONFIDENTIALITY, item);
                            }
                        }
                    }
                    break;
                case CATEGORY:
                    if (igcKafkaEvent.getAction().equals(CREATE)) {
                        createEntity(igcObject, GLOSSARY_CATEGORY, false);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @throws TypeErrorException The type name is not recognized as a relationship type.
     */
    private void createRelationship(String relationshipType, String entityId1, String entityType1,
                                    String entityId2, String entityType2) throws TypeErrorException {

        Relationship relationship = getRelationship(
                relationshipType,
                entityId1, entityType1,
                entityId2, entityType2);

        this.repositoryEventProcessor.processNewRelationshipEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                relationship
        );

        log.info("Created relationship " + relationshipType + "(" + entityType1 + ", " + entityType2 + ")");
        log.info("Created relationship between " + entityId1 + " and " + entityId2);
    }

    private void createClassification(IGCObject igcObject, String classificationTypeName, Item item) throws TypeErrorException {

        EntityDetail entity = getEntityWithClassification(igcObject, classificationTypeName, item);

        repositoryEventProcessor.processClassifiedEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                entity);

        log.info("Classification created " + classificationTypeName + " for " + igcObject.getId());
    }

    private void createEntity(IGCObject igcObject, String typeName, boolean avoidDuplicate) throws TypeErrorException {

        String username = igcObject.getCreatedBy();
        InstanceProperties instanceProperties = getEntityInstanceProperties(igcObject, typeName);
        EntityDetail entityDetail = getEntityDetail(igcObject, typeName, avoidDuplicate, username, instanceProperties);

        this.repositoryEventProcessor.processNewEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                entityDetail

        );

        log.info("Created " + typeName + "." + igcObject.getId());
    }


    private Relationship getRelationship(String relationshipType, String entityId1,
                                         String entityType1, String entityId2, String entityType2) throws TypeErrorException {

        Relationship relationship = this.repositoryHelper.getNewRelationship(
                sourceName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                null,
                relationshipType,
                null
        );

        EntityProxy entityProxyOne = newEntityProxy(entityType1);
        EntityProxy entityProxyTwo = newEntityProxy(entityType2);

        entityProxyOne.setGUID(entityId1);
        entityProxyTwo.setGUID(entityId2);

        relationship.setEntityOneProxy(entityProxyOne);
        relationship.setEntityTwoProxy(entityProxyTwo);

        return relationship;
    }

    /**
     * Return a filled out entity.
     *
     * @param typeName name of the type
     * @return an entity that is filled out
     * @throws TypeErrorException the type name is not recognized as an entity type
     */
    private EntityProxy newEntityProxy(String typeName) throws TypeErrorException {
        EntityProxy entityProxy = this.repositoryHelper.getNewEntityProxy(
                sourceName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                null,
                typeName,
                null,
                null
        );
        entityProxy.setStatus(InstanceStatus.ACTIVE);

        return entityProxy;
    }

    private EntityDetail getEntityDetail(IGCObject igcObject, String typeName, boolean avoidDuplicate, String username,
                                         InstanceProperties instanceProperties) throws TypeErrorException {
        EntityDetail entityDetail = this.repositoryHelper.getNewEntity(
                sourceName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                username,
                typeName,
                instanceProperties,
                null
        );

        entityDetail.setStatus(InstanceStatus.ACTIVE);

        if (avoidDuplicate) {
            entityDetail.setGUID(typeName + ".rid." + igcObject.getId());
        } else {
            entityDetail.setGUID(igcObject.getId());
        }

        return entityDetail;
    }

    private EntityDetail getEntityWithClassification(IGCObject igcObject, String classificationTypeName, Item item) throws TypeErrorException {

        Classification classification = getClassification(classificationTypeName, item);
        InstanceProperties instanceProperties = getEntityInstanceProperties(igcObject, GLOSSARY_TERM);
        EntityDetail skeletonEntity = getEntityDetail(igcObject, GLOSSARY_TERM, false, null, instanceProperties);

        return repositoryHelper.addClassificationToEntity(sourceName,
                skeletonEntity,
                classification,
                "createClassification");
    }

    private Classification getClassification(String classificationTypeName, Item item) throws TypeErrorException {

        Classification classification = repositoryHelper.getNewClassification(
                sourceName,
                null,
                classificationTypeName,
                GLOSSARY_TERM,
                ClassificationOrigin.PROPAGATED,
                null,
                null);

        InstanceProperties classificationProperties = getClassificationProperties(item.getName());
        classification.setProperties(classificationProperties);

        return classification;
    }

    private InstanceProperties getEntityInstanceProperties(IGCObject igcObject, String typeName) {
        Map<String, InstancePropertyValue> properties = new HashMap<>();

        PrimitivePropertyValue qualifiedName = getStringPropertyValue(typeName + ".qualifiedName." + igcObject.getId());
        properties.put("qualifiedName", qualifiedName);

        PrimitivePropertyValue displayName = getStringPropertyValue(igcObject.getName());
        properties.put("displayName", displayName);

        PrimitivePropertyValue name = getStringPropertyValue(igcObject.getName());
        properties.put("name", name);

        PrimitivePropertyValue summary = getStringPropertyValue(igcObject.getShortDescription());
        properties.put("summary", summary);

        PrimitivePropertyValue description = getStringPropertyValue(igcObject.getLongDescription());
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
        PrimitivePropertyValue qualifiedName = new PrimitivePropertyValue();

        qualifiedName.setPrimitiveValue(value);
        qualifiedName.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);

        return qualifiedName;
    }

    private EnumPropertyValue getEnumPropertyValue(String value) {
        EnumPropertyValue propertyValue = new EnumPropertyValue();
        propertyValue.setSymbolicName(value);

        return propertyValue;
    }

}

