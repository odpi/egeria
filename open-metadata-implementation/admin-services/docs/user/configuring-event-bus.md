<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Setting up the default event bus

A [OMAG server](../concepts/omag-server.md) uses an event bus to exchange events with other
servers and tools.   An example of an event bus is [Apache Kafka](https://kafka.apache.org/).

Egeria manages the specific topic names and the event payloads;
however, it needs to know where the event bus is deployed and
any properties needed to configure it.

Since the event bus is used in multiple places,
the configuration document allows you to set up the details of the event bus
which are then incorporated into all of the places where the event bus is needed.
You need to set up this information before configuring
any of the following:

* Using an event topic as the destination for the [audit log](configuring-the-audit-log.md).
* Configuring the [access services](configuring-the-access-services.md) in a
[Metadata Server](../concepts/metadata-server.md) or
a [Metadata Access Point](../concepts/metadata-access-point.md).
* Configuring [registration to a cohort](configuring-registration-to-a-cohort.md) in a
[Metadata Server](../concepts/metadata-server.md),
a [Metadata Access Point](../concepts/metadata-access-point.md),
a [Repository Proxy](../concepts/repository-proxy.md) or
a [Conformance Test Server](../concepts/conformance-test-server.md).

The following command creates information about the event bus.
This information is used on the subsequent configuration of the OMAG server subsystems.
It does not affect any subsystems that have already been configured in the configuration document
and if the event bus is not needed, its values are ignored.

It is possible to add arbitrary name/value pairs as JSON in the
request body.  The correct properties to use are defined in the connector type for the event bus.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/event-bus
```

For example, when using Apache Kafka as your event bus you may want to configure properties that
control the behavior of the consumer that receives events and the producer that sends events.
This is a typical set of producer and consumer properties:

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

A different type of event bus would use different properties.

----
* Return to [configuring an OMAG server](configuring-an-omag-server.md)
* Return to [configuration document structure](../concepts/configuration-document.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.