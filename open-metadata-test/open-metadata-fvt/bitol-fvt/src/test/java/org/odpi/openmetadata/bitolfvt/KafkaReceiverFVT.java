/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.integration.bitol.BitolEventReceiverIntegrationProvider;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.BitolMapperBase;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
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

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Feeds a data contract through the Bitol Event Receiver.  A Kafka topic is catalogued from the Core content pack's
 * Apache Kafka topic template - the same way an operator would catalog a topic that a team publishes its contracts
 * to - and attached to the receiver as a catalog target.  A plain Kafka producer, standing in for that team's
 * tooling, then writes a contract onto the topic, and the test checks that the contract is catalogued as a data
 * sharing agreement and written to the file store.
 * <br>
 * The document is the Oak Dene hospital contract with its version raised to 1.1.0, so that it is distinguishable
 * from the 1.0.0 version that {@link BitolRoundTripFVT} publishes through the REST API.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class KafkaReceiverFVT
{
    private static final String TOPIC_NAME        = "egeria.bitol-fvt.documents";
    private static final String CONTRACT_DOCUMENT = "DataContract/oak-dene-hospital-weekly-measurements.odcs.yaml";
    private static final String NEW_VERSION       = "1.1.0";


    @Test
    @DisplayName("A data contract produced onto a Kafka topic attached as a catalog target is catalogued")
    public void testContractArrivesThroughKafka() throws Exception
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
        catalogTargetProperties.setCatalogTargetName(BitolEventReceiverIntegrationProvider.CATALOG_TARGET_NAME);

        context.getAssetClient().addCatalogTarget(IntegrationConnectorDefinition.BITOL_EVENT_RECEIVER.getGUID(), topicGUID, null, catalogTargetProperties);

        /*
         * A refresh makes the receiver pick up the new catalog target and start listening to the topic.
         */
        integrationDaemon.refreshConnector(IntegrationConnectorDefinition.BITOL_EVENT_RECEIVER.getConnectorName());

        /*
         * Produce the document.  It is sent again on each poll until the agreement appears: the receiver's
         * consumer joins the topic asynchronously and a message sent before it is subscribed would otherwise be
         * missed, and a document that is catalogued twice is simply updated the second time.
         */
        String       rawDocument  = BitolFvtTestSupport.readSampleDocument(CONTRACT_DOCUMENT).replaceFirst("\nversion: 1\\.0\\.0", "\nversion: " + NEW_VERSION);
        DataContract dataContract = BitolDocumentFormatter.parseDataContract(rawDocument);

        assertEquals(NEW_VERSION, dataContract.getVersion(), "The test document did not get its new version.");

        String qualifiedName = BitolMapperBase.getDocumentQualifiedName(dataContract.getKind(), dataContract.getId(), dataContract.getVersion());

        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("bitol.fvt.refresh.timeout.seconds", 120) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        OpenMetadataElement agreement = null;

        try (KafkaProducer<String, String> producer = newProducer(kafkaEndpoint))
        {
            while ((agreement == null) && (System.currentTimeMillis() < giveUpTime))
            {
                producer.send(new ProducerRecord<>(TOPIC_NAME, dataContract.getId(), rawDocument)).get();

                Thread.sleep(OMAGPlatformExtension.getLongProperty("bitol.fvt.refresh.poll.seconds", 2) * 1000);

                agreement = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);
            }
        }

        if (agreement == null)
        {
            fail("No agreement with qualifiedName '" + qualifiedName + "' appeared within " + (timeoutMilliseconds / 1000)
                         + " seconds of producing the contract onto Kafka topic " + TOPIC_NAME + " at " + kafkaEndpoint
                         + ".  Check the integration daemon's audit log in build/bitol-fvt-data/logs/audit.log for BITOL-INTEGRATION-CONNECTOR-0001"
                         + " (the receiver attached to the topic) and 0003 (the document published).");
        }

        assertEquals(OpenMetadataType.AGREEMENT.typeName, agreement.getType().getTypeName(), "The contract from Kafka was not catalogued as an Agreement.");

        File stored = new File(new File(new File(BitolFvtTestSupport.STORE_DIRECTORY, dataContract.getKind()), dataContract.getId()), NEW_VERSION + ".yaml");

        BitolFvtTestSupport.waitFor("The file store writing " + stored.getPath(), stored::isFile);
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
        placeholders.put(PlaceholderProperty.SERVER_NAME.getName(), "bitol-fvt-kafka");
        placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "Topic on which the bitol-fvt suite publishes data contracts to the Bitol Event Receiver.");
        placeholders.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "1.0");
        placeholders.put(KafkaPlaceholderProperty.FULL_TOPIC_NAME.getName(), TOPIC_NAME);
        placeholders.put(KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getName(), "bitol-fvt documents");
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
