/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaPlaceholderProperty;
import org.odpi.openmetadata.contentpacks.core.DataAssetTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Checks the route that a processing engine takes when it publishes through the Open Lineage proxy backend:
 * events on a Kafka topic.  The test catalogues the topic from the core content pack's Kafka topic template,
 * attaches it to the Open Lineage Kafka Listener as a catalog target, refreshes the listener so that it starts
 * consuming, publishes a run event to the topic with a Kafka producer, and waits for the cataloguer to catalog
 * the job it describes - proving the event travelled topic, listener, daemon, cataloguer.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OpenLineageKafkaListenerFVT
{
    private static final String SERVER_NAME         = "openlineage-fvt";
    private static final String CATALOG_TARGET_NAME = "openlineage-fvt-events";


    /**
     * Publish through Kafka and check the job is catalogued.
     *
     * @throws Exception test failure
     */
    @Test
    @DisplayName("An event published to a Kafka topic attached to the listener reaches the cataloguer")
    void eventOnKafkaTopicIsCatalogued() throws Exception
    {
        String kafkaEndpoint = OMAGPlatformExtension.getProperty("openlineage.fvt.kafka.endpoint", "oak.local:9194");
        String topicName     = OMAGPlatformExtension.getProperty("openlineage.fvt.kafka.topic", "egeria.openlineage-fvt.events");

        ensureTopicIsACatalogTarget(kafkaEndpoint, topicName);

        /*
         * Publish a run event to the topic.
         */
        UUID                runId = UUID.randomUUID();
        OpenLineageRunEvent event = OpenLineageEventFactory.kafkaJobEvent("COMPLETE", Instant.now(), runId);
        String              json  = OpenLineageEventFactory.toJSON(event);

        Properties producerProperties = new Properties();

        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        producerProperties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "30000");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties))
        {
            producer.send(new ProducerRecord<>(topicName, runId.toString(), json)).get(30, TimeUnit.SECONDS);
            producer.flush();
        }

        /*
         * The cataloguer only sees the event if the listener consumed it from the topic.
         */
        String processQualifiedName = OpenLineageFvtTestSupport.processQualifiedName(OpenLineageEventFactory.JOB_NAMESPACE, OpenLineageEventFactory.KAFKA_JOB_NAME);

        OpenMetadataRootElement process = OpenLineageFvtTestSupport.waitForAsset(processQualifiedName, "the job published through Kafka to be catalogued");

        assertNotNull(process, "The job described by the event on the Kafka topic should be catalogued");

        String topicAssetQualifiedName = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TOPIC.typeName, OpenLineageEventFactory.TOPIC_NAMESPACE, OpenLineageEventFactory.COUNTS_TOPIC);

        OpenMetadataRootElement topicAsset = OpenLineageFvtTestSupport.waitForAsset(topicAssetQualifiedName, "the kafka:// output dataset to be catalogued as a Topic");

        assertNotNull(topicAsset, "A kafka:// dataset should be catalogued as a Topic");
    }


    /**
     * Catalogue the Kafka topic from the template (if it is not already catalogued) and attach it to the Kafka
     * listener as a catalog target (if it is not already attached), then refresh the listener so that it picks
     * the new target up.
     *
     * @param kafkaEndpoint bootstrap servers
     * @param topicName topic name
     * @throws Exception problem with the metadata store or the daemon
     */
    private void ensureTopicIsACatalogTarget(String kafkaEndpoint,
                                             String topicName) throws Exception
    {
        AssetClient assetClient = ConnectorContextFactory.newContext().getAssetClient();

        String topicQualifiedName = DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName() + "::" + SERVER_NAME + "." + topicName + "::inOut";

        OpenMetadataRootElement topic = assetClient.getAssetByUniqueName(topicQualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, assetClient.getGetOptions());

        String topicGUID;

        if (topic == null)
        {
            String[] hostAndPort = kafkaEndpoint.split(":");

            Map<String, String> placeholders = new HashMap<>();

            /*
             * Supply every placeholder the template declares, so that none survives into the catalogued topic.
             */
            List<PlaceholderPropertyType> placeholderTypes = KafkaPlaceholderProperty.getKafkaTopicPlaceholderPropertyTypes();

            if (placeholderTypes != null)
            {
                for (PlaceholderPropertyType placeholderType : placeholderTypes)
                {
                    placeholders.put(placeholderType.getName(), "");
                }
            }

            placeholders.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), hostAndPort[0]);
            placeholders.put(PlaceholderProperty.PORT_NUMBER.getName(), (hostAndPort.length > 1) ? hostAndPort[1] : "9092");
            placeholders.put(PlaceholderProperty.SERVER_NAME.getName(), SERVER_NAME);
            placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "Topic that the openlineage-fvt suite publishes Open Lineage events to.");
            placeholders.put(KafkaPlaceholderProperty.FULL_TOPIC_NAME.getName(), topicName);
            placeholders.put(KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getName(), CATALOG_TARGET_NAME);

            TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

            templateOptions.setIsOwnAnchor(true);

            topicGUID = assetClient.createAssetFromTemplate(templateOptions,
                                                            DataAssetTemplateDefinition.KAFKA_TOPIC_TEMPLATE.getTemplateGUID(),
                                                            null,
                                                            null,
                                                            placeholders,
                                                            null);
        }
        else
        {
            topicGUID = topic.getElementHeader().getGUID();
        }

        String connectorGUID = IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getGUID();

        boolean alreadyATarget = false;

        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(connectorGUID, assetClient.getQueryOptions());

        if (catalogTargets != null)
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if ((catalogTarget != null) && (topicGUID.equals(catalogTarget.getElementHeader().getGUID())))
                {
                    alreadyATarget = true;
                }
            }
        }

        if (! alreadyATarget)
        {
            CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

            catalogTargetProperties.setCatalogTargetName(CATALOG_TARGET_NAME);

            assetClient.addCatalogTarget(connectorGUID, topicGUID, new MakeAnchorOptions(assetClient.getMetadataSourceOptions()), catalogTargetProperties);
        }

        /*
         * The listener picks up new catalog targets when it refreshes.
         */
        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getConnectorName());
    }
}
