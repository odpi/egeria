/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.GaianQueryConstructor;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka.KafkaVirtualiserConsumer;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.views.ViewsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;

/**
 * Virtualiser is used to update Gaian and create business view and technical view for Information View OMAS
 */
@Service
public class KafkaVirtualiser{


    @Value("${information_view_out_topic}")
    private String informationViewOutTopic;
    @Value("${group_id_config}")
    private String groupIdConfig;

    @Value("${bootstrap_servers}")
    private String bootstrapServer;

    @Autowired
    private ViewsConstructor viewsConstructor;

    @Autowired
    private GaianQueryConstructor gaianQueryConstructor;

    private KafkaOpenMetadataTopicConnector inTopicKafkaConnector;
    private KafkaOpenMetadataTopicConnector outTopicKafkaConnector;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * use Kafka consumer to listen to Information View Out Topic
     * NOTE: this may throw VirtualiserCheckedException when Jackson is not able to parse or map and interrupted I/O options
     */
    @PostConstruct
    public void listenToIVOMAS() {


        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(informationViewOutTopic));
        KafkaVirtualiserConsumer kafkaVirtualiserConsumer = new KafkaVirtualiserConsumer("KafkaVirtualiserConsumer", consumer, gaianQueryConstructor, viewsConstructor);
        Thread thread = new Thread(kafkaVirtualiserConsumer);
        thread.start();

    }

}