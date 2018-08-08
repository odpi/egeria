<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Kafka Open Metadata Topic Connector

The Kafka Open Metadata Topic Connector implements 
an [Apache Kafka](https://kafka.apache.org/) connector for a topic that exchanges
Java Objects as JSON payloads.

## Default Producer configuration

(see [Apache Kafka producer configurations](http://kafka.apache.org/0100/documentation.html#producerconfigs) for more information and options)

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

## Default Consumer configuration

(see [Apache Kafka consumer configurations](http://kafka.apache.org/0100/documentation.html#newconsumerconfigs) for more information and options)

| Property Name | Property Value |
|----------------|-----------------|
| bootstrap.servers | localhost:59092 |
| zookeeper.session.timeout.ms | 400 |
| zookeeper.sync.time.ms | 200 |
| fetch.message.max.bytes | 10485760 |
| max.partition.fetch.bytes | 10485760 |
| key.deserializer | org.apache.kafka.common.serialization.StringDeserializer |
| value.deserializer| org.apache.kafka.common.serialization.StringDeserializer |

