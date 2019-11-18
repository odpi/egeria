<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Kafka Open Metadata Topic Connector

The Kafka Open Metadata Topic Connector implements 
an [Apache Kafka](https://kafka.apache.org/) connector for a topic that exchanges
Java Objects as JSON payloads.

# Default Configuration

## Producer

(see [Apache Kafka producer configurations](http://kafka.apache.org/0100/documentation.html#producerconfigs) for more information and options)

| Property Name | Property Value |
|---------------|----------------|
| bootstrap.servers | localhost:9092 |
| acks              | all |
| retries | 1 |
| batch.size | 16384 |
| linger.ms | 0 |
| buffer.memory | 33554432 |
| max.request.size | 10485760 |
| key.serializer | org.apache.kafka.common.serialization.StringSerializer |
| value.serializer | org.apache.kafka.common.serialization.StringSerializer |

## Consumer

(see [Apache Kafka consumer configurations](http://kafka.apache.org/0100/documentation.html#newconsumerconfigs) for more information and options)

| Property Name | Property Value |
|----------------|-----------------|
| bootstrap.servers | localhost:9092 |
| enable.auto.commit | true |
| auto.commit.interval.ms | 1000 |
| session.timeout.ms | 30000 |
| max.partition.fetch.bytes | 10485760 |
| key.deserializer | org.apache.kafka.common.serialization.StringDeserializer |
| value.deserializer| org.apache.kafka.common.serialization.StringDeserializer |

#  Security

By default kafka security is not configured. The exact configuration may depend on the specific kafka service being used. Service specific notes
are below. They may work for other providers, and feedback is welcome so that this documentation can be updated accordingly.

## IBM Event Streams on IBM Cloud

There are 2 key pieces of information that are provided in the documentation for your configured cloud service

 * List of brokers - try to specify at least 3. The hosts in the examples below will need updating
 * API key - referred to as MYAPIKEY below
 
 Given these, configure kafka properties for both provider and consumer as follows:
```
"broker.list: "broker-5-uniqueid.kafka.svcnn.region.eventstreams.cloud.ibm.com:9093, broker-3-uniqueid.kafka.svcnn.region.eventstreams.cloud.ibm.com:9093, broker-2-uniqueid.kafka.svcnn.region.eventstreams.cloud.ibm.com:9093, broker-0-uniqueid.kafka.svcnn.region.eventstreams.cloud.ibm.com:9093, broker-1-uniqueid.kafka.svcnn.region.eventstreams.cloud.ibm.com:9093, broker-4-uniqueid.kafka.svcnn.region.eventstreams.cloud.ibm.com:9093"
"security.protocol":"SASL_SSL",
"ssl.protocol":"TLSv1.2",
"ssl.enabled.protocols":"TLSv1.2",
"ssl.endpoint.identification.algorithm":"HTTPS",
"sasl.jaas.config":"org.apache.kafka.common.security.plain.PlainLoginModule required username='token' password='MYAPIKEY';",
"sasl.mechanism":"PLAIN"
```
An example of a use of this configuration can be found in the virtual data connector helm charts. See [odpi-egeria-vdc helm chart](../../../../../../open-metadata-resources/open-metadata-deployment/charts/odpi-egeria-vdc/README.md) 
