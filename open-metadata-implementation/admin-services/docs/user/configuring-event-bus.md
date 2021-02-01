<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Setting up the default event bus

A [OMAG server](../concepts/omag-server.md) uses an event bus to exchange events with other
servers and tools.   Egeria manages the specific topic names;
however, it needs to know where the event bus implementation is and
any properties needed to configure it.

The following command creates information about the event bus.
This information is used on the subsequent configuration of the OMAG server subsystems.
It does not affect any subsystems that have already been configured in the configuration document.

It is possible to add arbitrary name/value pairs as JSON in the
request body.  The correct properties to use are defined in the connector type for the event bus.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/event-bus
```

For example, when using Apache Kafka as your event bus you may want to configure properties such as:

```json
{
	"producer": {
		"bootstrap.servers":"localhost:9092",
		"acks":"all",
		"retries":"0",
		"batch.size":"16384",
		"linger.ms":"1",
		"buffer.memory":"33554432",
		"max.request.size":"10485760",
		"key.serializer":"org.apache.kafka.common.serialization.StringSerializer",
		"value.serializer":"org.apache.kafka.common.serialization.StringSerializer",
		"kafka.omrs.topic.id":"cocoCohort"
	},
	"consumer": {
   		"bootstrap.servers":"localhost:9092",
   		"zookeeper.session.timeout.ms":"400",
   		"zookeeper.sync.time.ms":"200",
   		"fetch.message.max.bytes":"10485760",
   		"max.partition.fetch.bytes":"10485760",
   		"key.deserializer":"org.apache.kafka.common.serialization.StringDeserializer",
   		"value.deserializer":"org.apache.kafka.common.serialization.StringDeserializer",
   		"kafka.omrs.topic.id":"cocoCohort"
	}
}
```

----
* Return to [configuring an OMAG server](configuring-an-omag-server.md)
* Return to [configuration document structure](../concepts/configuration-document.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.