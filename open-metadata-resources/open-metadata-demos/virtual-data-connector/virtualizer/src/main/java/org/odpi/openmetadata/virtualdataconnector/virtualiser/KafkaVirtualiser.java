/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.GaianQueryConstructor;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka.KafkaVirtualiserConsumer;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.views.ViewsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final String THREADNAME_PREFIX = KafkaVirtualiser.class.getSimpleName();

    /**
     * use Kafka consumer to listen to Information View Out Topic
     * @throws VirtualiserCheckedException when Jackson is not able to parse or map and interrupted I/O options
     */
    @PostConstruct
    public void listenToIVOMAS() {


        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(informationViewOutTopic));
        ExecutorService executorService = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder()
                .setNameFormat(THREADNAME_PREFIX + " thread-%d").build());
        executorService.submit(new KafkaVirtualiserConsumer("KafkaVirtualiserConsumer",
                consumer, gaianQueryConstructor, viewsConstructor));

    }



}