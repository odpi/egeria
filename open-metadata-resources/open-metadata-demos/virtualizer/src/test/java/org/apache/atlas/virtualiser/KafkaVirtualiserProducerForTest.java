/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * KafkaVirtualiserProducer is used to send information view in topic out
 * to Information View OMAS and it contains two json files which include business view
 * and technical view
 */
public class KafkaVirtualiserProducerForTest {
    /**
     * topic will send out
     */
    private final static String INFORMATION_VIEW_IN_TOPIC = "information_view_out_topic";
    /**
     * server address where kafka is running
     */
   // private final static String BOOTSTRAP_SERVERS = "lx13v7.ad.ing.net:59092";//,localhost:9093,localhost:9094";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";


    /**
     * producer used to send the message out
     */
    private Producer<Long, String> producer=null;

    /**
     * get a producer object to send the message
     * @return a completed set up producer
     */
    private Producer<Long, String> getProducer() {
        if (producer == null) {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaOMRSProducer");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producer = new KafkaProducer<>(props);
        }
        return producer;
    }

    /**
     * send out business view or technical view to Information View in topic
     * @param message the json file(business view or technical view)
     * @throws Exception when there is a problem when the topic is sent out
     */
    public void runProducer(String message) throws Exception {
        final Producer<Long, String> producer = getProducer();
        try {
            long index = System.currentTimeMillis();
            final ProducerRecord<Long, String> record =
                    new ProducerRecord<>(INFORMATION_VIEW_IN_TOPIC, index,message);
            producer.send(record);
        } finally {
            producer.flush();
        }
    }

    /**
     * close the producer. This function has to be called every time when
     * two views are sent out.
     */
    public void closeProducer(){
        producer.close();
    }



}
