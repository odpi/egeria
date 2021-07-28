<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Event Broker

An event broker is an infrastructure service that provides a publish-subscribe capability
through [topics](topic.md).

There are many different event broker implementations with greater or
lesser reliability and performance.
Egeria's default event broker is [Apache Kafka](https://kafka.apache.org/).
This is an open source event broker with a high level of performance, scalability and reliability.

Many organizations establish a standard choice of their event
broker service which is why Egeria uses connectors to connect to the event broker so that Apache Kafka can be swapped out
for a different event broker implementation.
As such, each topic is accessed through an
[open metadata topic connector](../connector-catalog/runtime-connectors.md#Open Metadata Topic Connectors).

## Further information

* [Configuring the event broker for Egeria](../../../open-metadata-implementation/admin-services/docs/concepts/event-bus.md)
* [Cataloguing topics and event types for an event broker](../../../open-metadata-implementation/integration-services/topic-integrator)

----
* Return to [Glossary](../open-metadata-glossary.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.