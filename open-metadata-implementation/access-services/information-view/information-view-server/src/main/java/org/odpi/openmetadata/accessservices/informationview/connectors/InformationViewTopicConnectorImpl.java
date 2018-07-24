/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.connectors;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InformationViewTopicConnectorImpl extends InformationViewTopicConnector {

    private static final String THREADNAME_PREFIX = InformationViewTopicConnector.class.getSimpleName();
    private static final String INFORMATION_VIEW_TOPIC_ADDRESS = "information.view.topic.address";
    private static final Logger log = LoggerFactory.getLogger(InformationViewTopicConnector.class);
    private KafkaProducer producer;
    private String topicName;

    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);

        String consumerGroup = "iv-omas" + UUID.randomUUID();//TODO this should be configurable?

        final Properties props = new Properties();
        int indexFirst = this.connectionBean.getEndpoint().getAddress().indexOf("/");
        int indexLast = this.connectionBean.getEndpoint().getAddress().lastIndexOf("/");
        String address = this.connectionBean.getEndpoint().getAddress().substring(0, indexFirst);
        topicName = this.connectionBean.getEndpoint().getAddress().substring(indexLast + 1);

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);//TODO
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));
        startConsumer(consumer);
        producer = new KafkaProducer(props);
    }

    /**
     * @param consumer A Kafka client that consumes records from a Kafka cluster.
     */
    private void startConsumer(Consumer<Long, String> consumer) {
        ExecutorService executorService = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setNameFormat(THREADNAME_PREFIX + " thread-%d").build());
        executorService.submit(new InfoViewTopicConsumerThread("InfoViewTopicConsumerThread", consumer));
    }


    @Override
    public void disconnect() throws ConnectorCheckedException {

    }

    @Override
    public void sendEvent(ColumnContextEvent event) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(event);

        ProducerRecord record = new ProducerRecord(topicName, json);

        Object response = producer.send(record).get();

        log.info("Published Event: ", json);
    }

    public class InfoViewTopicConsumerThread extends TopicConsumerThread<InformationViewEvent> {

        public InfoViewTopicConsumerThread(String name, Consumer consumer) {
            super(name, consumer);
        }

        @Override
        public void distributeEvents(InformationViewEvent event) throws Exception {
            distributeEvent(event);
        }

        public Class getType() {
            return InformationViewEvent.class;
        }

    }
}
