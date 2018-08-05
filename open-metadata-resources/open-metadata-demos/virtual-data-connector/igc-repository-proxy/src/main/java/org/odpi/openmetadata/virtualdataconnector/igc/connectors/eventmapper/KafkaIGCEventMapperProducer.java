/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;

import java.util.List;
import java.util.Properties;


public class KafkaIGCEventMapperProducer extends OpenMetadataTopicConnector {

    String topicName;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    Producer<Long, String> producer;

    public KafkaIGCEventMapperProducer()
    {
        super();

    }

    @Override
    protected List<String> checkForEvents() {
        return null;
    }

    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);
        producer = createProducer();
    }

    /**
     * Set properties for kafka producer.
     *
     * @return      kafka producer object
     */
    private Producer<Long, String> createProducer() {
        Properties props = new Properties();

        int indexFirst = this.connectionBean.getEndpoint().getAddress().indexOf("/");
        int indexLast = this.connectionBean.getEndpoint().getAddress().lastIndexOf("/");
        String server =  this.connectionBean.getEndpoint().getAddress().substring(0, indexFirst);
        topicName = this.connectionBean.getEndpoint().getAddress().substring(indexLast + 1);

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaOMRSProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);//TODO move this to initialize call
    }

    //    Note that the producer is initiated and closed each time the runProducer method is called.
//    Closing the producer prevents resource/memory leaks yet depending on the throughput of the
//    producer it might be more convenient to keep it open.

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException - there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException - there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }

    @Override
    public void sendEvent(String event) {

        long time = System.currentTimeMillis();
        try {
            long index = time;
            final ProducerRecord<Long, String> record =
                    new ProducerRecord<>(topicName, index,
                            MAPPER.writeValueAsString(event));
            RecordMetadata metadata = producer.send(record).get();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
