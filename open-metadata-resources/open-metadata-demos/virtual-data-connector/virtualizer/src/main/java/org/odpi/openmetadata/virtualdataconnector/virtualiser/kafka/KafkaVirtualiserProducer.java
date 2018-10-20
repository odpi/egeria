/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * KafkaVirtualiserProducer is used to send information view in topic out
 * to Information View OMAS and it contains two json files which are business view
 * and technical view
 */
@Service
public class KafkaVirtualiserProducer {


    @Value("${information_view_out_topic}")
    private String informationViewOutTopic;
    @Value("${information_view_in_topic}")
    private String informationViewInTopic;
    @Value("${group_id_config}")
    private String groupIdConfig;
    @Value("${bootstrap_servers}")
    private String bootstrapServer;
    @Value("${client_id_config}")
    private String clientIdConfig;


    /**
     * producer used to send the message out
     */
    private Producer<Long, String> producer = null;

    /**
     * get a producer object to send the message
     *
     * @return a completed set up producer
     */
    private Producer<Long, String> getProducer() {
        if (producer == null) {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, clientIdConfig);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producer = new KafkaProducer<>(props);
        }
        return producer;
    }

    /**
     * send out business view or technical view to Information View in topic
     *
     * @param message the json file(business view or technical view)
     * @throws VirtualiserCheckedException when there is a problem when the topic is sent out
     */
    public void runProducer(String message) throws VirtualiserCheckedException {
        final String methodName = "runProducer";
        final Producer<Long, String> producer = getProducer();
        try {
            long index = System.currentTimeMillis();
            final ProducerRecord<Long, String> record =
                    new ProducerRecord<>(informationViewInTopic, index, message);
            producer.send(record);
        } catch (Exception e) {
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.SEND_TOPIC_FAIL;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } finally {
            producer.flush();
        }
    }

    /**
     * close the producer. This function has to be called every time when
     * two views are sent out.
     */
    public void closeProducer() {
        producer.close();
        producer = null;
    }


}
