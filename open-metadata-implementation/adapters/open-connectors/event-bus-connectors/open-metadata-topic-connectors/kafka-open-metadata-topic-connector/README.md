<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Kafka Open Connector

The Kafka Open Connector implements 
an [Apache Kafka](https://kafka.apache.org/) connector for a topic that exchanges
Java Objects as JSON payloads.

These are some of the default properties and sample values that can be set when creating [Apache Kafka](https://kafka.apache.org/)
producer and consumer.

Producer configs:

| Property Name | Property Value |
|---------------|----------------|
| bootstrap.servers | localhost:59092 |
| acks              | all |
| retries | 0 |
| batch.size | 16384 |
| linger.ms | 1 |
| buffer.memory | 33554432 |
| max.request.size | 10485760 |
| key.serializer | org.apache.kafka.common.serialization.StringSerializer |
| value.serializer | org.apache.kafka.common.serialization.StringSerializer |

Consumer configs:

| Property Name | Property Value |
|----------------|-----------------|
| bootstrap.servers | localhost:59092 |
| zookeeper.session.timeout.ms | 400 |
| zookeeper.sync.time.ms | 200 |
| fetch.message.max.bytes | 10485760 |
| max.partition.fetch.bytes | 10485760 |
| key.deserializer | org.apache.kafka.common.serialization.StringDeserializer |
| value.deserializer| org.apache.kafka.common.serialization.StringDeserializer |


For more information on Apache Kafka producer and consumer properties,
see Apache Kafka documentation .

[Apache Kafka producer configurations](http://kafka.apache.org/0100/documentation.html#producerconfigs)

[Apache Kafka consumer configurations](http://kafka.apache.org/0100/documentation.html#newconsumerconfigs)
