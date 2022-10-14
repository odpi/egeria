<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../../../../images/egeria-content-status-released.png#pagewidth)
  
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
| bring.up.retries | 10 |
| bring.up.minSleepTime | 5000 |

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
| bring.up.retries | 10 |
| bring.up.minSleepTime | 5000 |

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
An example of a use of this configuration can be found in the virtual data connector helm charts. See [odpi-egeria-vdc helm chart](https://github.com/odpi/egeria-samples/tree/master/helm-charts/odpi-egeria-vdc/README.md)

## Handling Kafka Cluster Bring Up Issues

In some environments users have encountered issues when the Kafka Cluster hasn't become fully available, when attempting a connection to the Kafka Cluster.
The Egeria KafkaTopicConnector provides a mechanism that verifies that the Kafka Cluster is actually running brokers before attempting to connect.
This mechanism is controlled by two properties.

* bring.up.retries
* bring.up.minSleepTime

bring.up.retries 
defaults to 10 and specifies the number of times the Egeria KafkaTopicConnector will retry verification before reporting a failure.
 
bring.up.minSleepTime is set to 5000ms by default and is the minimum amount of time to wait before attempting a verification retry. 
If a Kafka verification attempt takes longer than this value the KafkaTopicConnector does not pause before retring the verification.

## Topic Creation

In addition many enterprise kafka services do not allow automatic topic creation.

You will need to manually create topics of the following form

BASE_TOPIC_NAME is the value used for topicURLRoot when configuring the egeria event bus. For example, the default
value is `egeria`.

### Cohort topics

For each cohort being used (such as `cocoCohort`):
 * BASE_TOPIC_NAME.omag.openmetadata.repositoryservices.cohort.COHORT_NAME.OMRSTopic
 
### OMAS Topics
These need to be done FOR EACH SERVER configured to run one or more OMASs.
(For example for Coco Pharmaceuticals this might include `cocoMDS1`, `cocoMDS2`, `cocoMDS3` etc).

FOR EACH OMAS configured (eg Asset Consumer OMAS, Data Platform OMAS, Governance Engine OMAS etc)

 * BASE_TOPIC_NAME.omag.server.SERVER_NAME.omas.OMAS_NAME.InTopic
 * BASE_TOPIC_NAME.omag.server.SERVER_NAME.omas.OMAS_NAME.OutTopic


One way to configure is to initially run against a kafka service which allows auto topic creation, then make note of the kafka
topics that have been created - so that they can be replicated on the restricted setup.

In addition review the Egeria Audit Log for any events beginning OCF-KAFKA-TOPIC-CONNECTOR so that
action may be taken if for example topics are found to be missing.


----
Return to the [open-metadata-topic-connectors](..) module.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

