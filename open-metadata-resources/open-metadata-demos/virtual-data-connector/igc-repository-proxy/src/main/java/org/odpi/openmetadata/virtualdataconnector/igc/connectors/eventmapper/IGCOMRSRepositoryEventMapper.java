/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.IGCOMRSRepositoryConnector;

import java.util.Collections;
import java.util.Properties;


/**
 * IGCOMRSRepositoryEventMapper provides an implementation of a repository event mapper for the
 * IBM Governance Catalog (IGC).
 */

public class IGCOMRSRepositoryEventMapper extends OMRSRepositoryEventMapperBase {


    private static final Logger log = LoggerFactory.getLogger(IGCOMRSRepositoryEventMapper.class);
    private IGCColumn igcColumn;
    private String originatorServerName;
    private String businessTerm;
    private IGCOMRSRepositoryConnector igcomrsRepositoryConnector;

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

        runConsumer();
    }


    /**
     * Set properties for the kafka consumer.
     *
     * @return  kafka consumer object
     */
    private Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        String address = this.connectionBean.getEndpoint().getAddress().split("/")[0];
        String topicName = this.connectionBean.getEndpoint().getAddress().split("/")[1];
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_IGC_Consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }

    /**
     * Start a new thread to read IGC kafka events.
     */
    public  void runConsumer(){
        new Thread(new ConsumerThread()).start();
    }


    private class ConsumerThread implements Runnable{

        /**
         * Read the Kafka events originating from IGC.
         */
        @Override
        public void run() {
            log.info("Started IGC Eventmapper");
            IGCKafkaEvent igcKafkaEvent;
            final Consumer<Long, String> consumer = createConsumer();
            ConsumerRecords<Long, String> records;
            while (true) {
                try {
                    records = consumer.poll(100);
                    for (ConsumerRecord<Long, String> record : records) {
                        ObjectMapper mapper = new ObjectMapper();
                        igcKafkaEvent = mapper.readValue(record.value(), IGCKafkaEvent.class);
                        log.info("Consumer Record");
                        log.info(record.value());
                        processIGCEvent(igcKafkaEvent);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Process individual IGC Kafka events and publish them through OMRS.
     *
     * @param igcKafkaEvent a Kafka event originating from IGC.
     */
    public  void processIGCEvent(IGCKafkaEvent igcKafkaEvent) {
        if (igcKafkaEvent.getASSETTYPE().equals("Database Column") &&
                (
                        igcKafkaEvent.getACTION().equals("ASSIGNED_RELATIONSHIP") || igcKafkaEvent.getACTION().equals("MODIFY")
                )
                ) {
            try {
                igcColumn = igcomrsRepositoryConnector.queryIGCColumn(igcKafkaEvent.getASSETRID());
                originatorServerName = igcColumn.getName();
                if (igcColumn.getAssignedToTerms() != null) {
                    businessTerm = igcColumn.getAssignedToTerms().getItems().get(0).getName();
                    Relationship relationship = newRelationship("SemanticAssignment", igcKafkaEvent.getASSETRID(), "RelationalColumn", igcColumn.getAssignedToTerms().getItems().get(0).getId(), "GlossaryTerm");
                    this.repositoryEventProcessor.processNewRelationshipEvent(
                            "IGCOMRSRepositoryEventMapper",
                            igcomrsRepositoryConnector.getMetadataCollectionId(),
                            originatorServerName,
                            "IGC",
                            "",
                            relationship
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set Relationship linking an IGC technical term with an IGC business term
     *
     * @param relationshipType      name of the type
     * @param entityId1             String unique identifier
     * @param entityType1           name of the type
     * @param entityId2             String unique identifier
     * @param entityType2           name of the type
     * @return                      Relationship is a POJO that manages the properties of an open metadata relationship.  This includes information
     *                              about the relationship type, the two entities it connects and the properties it holds.
     *
     * @throws TypeErrorException   The type name is not recognized as a relationship type.
     */
    private  Relationship newRelationship(String relationshipType, String entityId1,String entityType1, String entityId2, String entityType2) throws TypeErrorException {
        Relationship relationship = initiateRelationship(relationshipType);

        EntityProxy entityProxyOne = newEntityProxy(entityType1);
        EntityProxy entityProxyTwo = newEntityProxy(entityType2);

        entityProxyOne.setGUID(entityId1);
        entityProxyTwo.setGUID(entityId2);

        relationship.setEntityOnePropertyName(originatorServerName);
        relationship.setEntityOneProxy(entityProxyOne);


        relationship.setEntityTwoPropertyName(businessTerm);
        relationship.setEntityTwoProxy(entityProxyTwo);
        return relationship;
    }

    /**
     *  Return a filled out relationship which just needs the entity proxies added.
     *
     * @param relationshipType      name of the type
     * @return                      a filled out relationship.
     * @throws TypeErrorException   the type name is not recognized as a relationship type.
     */
    private  Relationship initiateRelationship(String relationshipType) throws TypeErrorException {
        return this.repositoryHelper.getNewRelationship(
                "IGCOMRSRepositoryEventMapper",
                igcomrsRepositoryConnector.getMetadataCollectionId(),
                InstanceProvenanceType.LOCAL_COHORT,
                "",
                relationshipType,
                null
        );
    }

    /**
     * Return a filled out entity.
     *
     * @param typeName              name of the type
     * @return                      an entity that is filled out
     * @throws TypeErrorException   the type name is not recognized as an entity type
     */
    private EntityProxy newEntityProxy(String typeName) throws TypeErrorException {
        EntityProxy entityProxy = this.repositoryHelper.getNewEntityProxy(
                "IGCOMRSRepositoryEventMapper",
                igcomrsRepositoryConnector.getMetadataCollectionId(),
                InstanceProvenanceType.LOCAL_COHORT,
                "",
                typeName,
                null,
                null
        );
        entityProxy.setStatus(InstanceStatus.ACTIVE);
        return entityProxy;
    }

}

