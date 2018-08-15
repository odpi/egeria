/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.atlas.virtualiser.ffdc.VirtualiserCheckedException;
import org.apache.atlas.virtualiser.gaian.GaianQueryConstructor;
import org.apache.atlas.virtualiser.kafka.KafkaVirtualiserConsumer;
import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.apache.atlas.virtualiser.views.ViewsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Virtualiser is used to update Gaian and create business view and technical view for Information View OMAS
 */
public class KafkaVirtualiser{
    //pick up the information view in omas topic
    private KafkaVirtualiserConsumer kafkaVirtualiserConsumer;
    private static final String THREADNAME_PREFIX = KafkaVirtualiser.class.getSimpleName();


    /**
     * use Kafka consumer to listen to Information View Out Topic
     * @throws VirtualiserCheckedException when Jackson is not able to parse or map and interrupted I/O options
     */
    public void listenToIVOMAS() {
        ViewsConstructor viewsConstructor=new ViewsConstructor();
        GaianQueryConstructor gaianQueryConstructor=new GaianQueryConstructor();

        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertiesHelper.properties.getProperty("bootstrap_servers"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG,PropertiesHelper.properties.getProperty("group_id_config"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(PropertiesHelper.properties.getProperty("information_view_out_topic")));
        ExecutorService executorService = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setNameFormat(THREADNAME_PREFIX + " thread-%d").build());
        executorService.submit(new KafkaVirtualiserConsumer("KafkaVirtualiserConsumer", consumer,gaianQueryConstructor,viewsConstructor));

    }



}