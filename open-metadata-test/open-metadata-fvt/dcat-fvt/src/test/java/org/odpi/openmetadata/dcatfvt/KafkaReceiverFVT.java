/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dcatfvt;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.GroupListing;
import org.apache.kafka.clients.admin.ListGroupsOptions;
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.integration.dcat.DcatEventReceiverIntegrationProvider;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMappingBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;

import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Feeds a dataset description through the DCAT Event Receiver.  A Kafka topic is catalogued from the Core content
 * pack's Apache Kafka topic template - the same way an operator would catalog a topic that a data portal publishes
 * its descriptions to - and attached to the receiver as a catalog target.  A plain Kafka producer, standing in for
 * that portal, then writes a DCAT document onto the topic, and the test checks that the dataset is catalogued as a
 * data set.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class KafkaReceiverFVT
{
    private static final String TOPIC_NAME        = "egeria.dcat-fvt.documents";
    private static final String DATASET_IRI = "https://data.example.gov/dcat-fvt/kafka/tide-times";


    @Test
    @DisplayName("A dataset description produced onto a Kafka topic attached as a catalog target is catalogued")
    public void testDatasetArrivesThroughKafka() throws Exception
    {
        Assumptions.assumeTrue("kafka".equals(OMAGPlatformExtension.getEventBusMode()),
                               "The Kafka receiver test needs a broker; this run uses the " + OMAGPlatformExtension.getEventBusMode() + " event bus.");

        String kafkaEndpoint = OMAGPlatformExtension.getKafkaEndpoint();

        ensureTopicExists(kafkaEndpoint);

        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = context.getOpenMetadataStore();
        IntegrationDaemon    integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();

        /*
         * Catalog the topic from the template.  The template carries the Kafka topic connector's connection, with
         * the broker and topic name filled in from the placeholders, which is what lets the integration daemon
         * build a connector to the topic for the receiver.
         */
        String topicGUID = catalogTopic(context, kafkaEndpoint);

        OpenMetadataElement topic = openMetadataStore.getMetadataElementByGUID(topicGUID);

        assertNotNull(topic, "The Kafka topic asset was not created from the template.");
        assertEquals(DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName(), topic.getType().getTypeName(), "The template did not create a Kafka topic asset.");

        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();
        catalogTargetProperties.setCatalogTargetName(DcatEventReceiverIntegrationProvider.CATALOG_TARGET_NAME);

        context.getAssetClient().addCatalogTarget(IntegrationConnectorDefinition.DCAT_EVENT_RECEIVER.getGUID(), topicGUID, null, catalogTargetProperties);

        /*
         * A refresh makes the receiver pick up the new catalog target and start listening to the topic.
         */
        integrationDaemon.refreshConnector(IntegrationConnectorDefinition.DCAT_EVENT_RECEIVER.getConnectorName());

        String rawDocument = """
                {
                  "@context": { "dcat": "http://www.w3.org/ns/dcat#", "dct": "http://purl.org/dc/terms/" },
                  "@id": "%s",
                  "@type": "dcat:Dataset",
                  "dct:title": "Tide times",
                  "dct:description": "Predicted times of high and low water."
                }
                """.formatted(DATASET_IRI);

        String qualifiedName = DcatMappingBase.DATASET_QUALIFIED_NAME_PREFIX + DATASET_IRI;

        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("dcat.fvt.refresh.timeout.seconds", 120) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("dcat.fvt.refresh.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        /*
         * The receiver's consumer joins the topic asynchronously, so the document is not produced until the
         * consumer has been assigned the topic's partition.  From then on it cannot be missed: on its first
         * assignment the consumer winds back to the time it started, so even a message that lands before it
         * fetches is read.  The document is then produced exactly once.  It used to be resent on every poll
         * until the data set appeared, which buries the cataloguer under copies of the same document - each
         * one fully reprocessed - so the copy that matters is still queued when the wait runs out.
         */
        waitForTopicConsumer(kafkaEndpoint, giveUpTime);

        try (KafkaProducer<String, String> producer = newProducer(kafkaEndpoint))
        {
            producer.send(new ProducerRecord<>(TOPIC_NAME, DATASET_IRI, rawDocument)).get();
        }

        OpenMetadataElement dataset = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        while ((dataset == null) && (System.currentTimeMillis() < giveUpTime))
        {
            Thread.sleep(pollMilliseconds);

            dataset = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);
        }

        if (dataset == null)
        {
            fail("No data set with qualifiedName '" + qualifiedName + "' appeared within " + (timeoutMilliseconds / 1000)
                         + " seconds of producing the document onto Kafka topic " + TOPIC_NAME + " at " + kafkaEndpoint
                         + ".  Check the integration daemon's audit log in build/dcat-fvt-data/logs/audit.log for DCAT-INTEGRATION-CONNECTOR-0001"
                         + " (the receiver attached to the topic) and 0005 (the resource catalogued).");
        }

        assertEquals(OpenMetadataType.DATA_SET.typeName, dataset.getType().getTypeName(), "The dataset from Kafka was not catalogued as a DataSet.");
    }


    /**
     * Create the topic ahead of time.  A consumer that subscribes to a topic which does not yet exist only learns of
     * its creation on its next metadata refresh, which by default is minutes away.
     *
     * @param kafkaEndpoint broker
     * @throws Exception the topic could not be created
     */
    private void ensureTopicExists(String kafkaEndpoint) throws Exception
    {
        Properties properties = new Properties();

        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "15000");

        try (AdminClient adminClient = AdminClient.create(properties))
        {
            adminClient.createTopics(List.of(new NewTopic(TOPIC_NAME, 1, (short) 1))).all().get();
        }
        catch (ExecutionException error)
        {
            if (! (error.getCause() instanceof TopicExistsException))
            {
                throw error;
            }
        }
    }


    /**
     * Wait until a consumer group has a live member that has been assigned this test's topic.  Only the
     * receiver consumes the topic, so that member is the receiver's consumer.  A group left behind by an
     * earlier run has no members, so it does not count.
     *
     * @param kafkaEndpoint broker
     * @param giveUpTime time to stop waiting
     * @throws Exception the receiver never started consuming the topic, or the broker could not be asked
     */
    private void waitForTopicConsumer(String kafkaEndpoint,
                                      long   giveUpTime) throws Exception
    {
        Properties properties = new Properties();

        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "15000");

        try (AdminClient adminClient = AdminClient.create(properties))
        {
            while (System.currentTimeMillis() < giveUpTime)
            {
                List<String> groupIds = new ArrayList<>();

                for (GroupListing groupListing : adminClient.listGroups(ListGroupsOptions.forConsumerGroups()).all().get())
                {
                    groupIds.add(groupListing.groupId());
                }

                if (! groupIds.isEmpty())
                {
                    for (ConsumerGroupDescription groupDescription : adminClient.describeConsumerGroups(groupIds).all().get().values())
                    {
                        for (MemberDescription member : groupDescription.members())
                        {
                            for (TopicPartition topicPartition : member.assignment().topicPartitions())
                            {
                                if (TOPIC_NAME.equals(topicPartition.topic()))
                                {
                                    return;
                                }
                            }
                        }
                    }
                }

                Thread.sleep(500);
            }
        }

        fail("The DCAT Event Receiver never started consuming Kafka topic " + TOPIC_NAME + " at " + kafkaEndpoint
                     + ": no consumer was assigned the topic's partition.  Check the integration daemon's audit log in"
                     + " build/dcat-fvt-data/logs/audit.log for DCAT-INTEGRATION-CONNECTOR-0001 (the receiver attached to the topic).");
    }


    /**
     * Catalog the topic from the Core content pack's Apache Kafka topic template.
     *
     * @param context connector context
     * @param kafkaEndpoint broker as host:port
     * @return unique identifier of the KafkaTopic asset
     * @throws Exception problem creating the asset
     */
    private String catalogTopic(ConnectorContextBase context,
                                String               kafkaEndpoint) throws Exception
    {
        int    separator = kafkaEndpoint.lastIndexOf(':');
        String host      = kafkaEndpoint.substring(0, separator);
        String port      = kafkaEndpoint.substring(separator + 1);

        Map<String, String> placeholders = new HashMap<>();

        placeholders.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), host);
        placeholders.put(PlaceholderProperty.PORT_NUMBER.getName(), port);
        placeholders.put(PlaceholderProperty.SERVER_NAME.getName(), "dcat-fvt-kafka");
        placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "Topic on which the dcat-fvt suite publishes DCAT documents to the DCAT Event Receiver.");
        placeholders.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "1.0");
        placeholders.put(KafkaPlaceholderProperty.FULL_TOPIC_NAME.getName(), TOPIC_NAME);
        placeholders.put(KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getName(), "dcat-fvt documents");
        placeholders.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");

        TemplateOptions templateOptions = new TemplateOptions(context.getAssetClient().getMetadataSourceOptions());

        templateOptions.setIsOwnAnchor(true);

        return context.getAssetClient().createAssetFromTemplate(templateOptions,
                                                                KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateGUID(),
                                                                null,
                                                                null,
                                                                placeholders,
                                                                null);
    }


    /**
     * A producer configured the way a team's own tooling would be - nothing Egeria-specific.
     *
     * @param kafkaEndpoint broker
     * @return producer
     */
    private KafkaProducer<String, String> newProducer(String kafkaEndpoint)
    {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "15000");

        return new KafkaProducer<>(properties);
    }
}
