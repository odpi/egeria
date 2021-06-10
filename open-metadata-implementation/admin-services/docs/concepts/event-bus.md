<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Event Bus

The event bus is an infrastructure service that provides
the ability to reliably pass events between different servers.
The events are organized into topics.
The [OMAG Servers](omag-server.md) can connect to a topic and both add and read events from it.
The event bus maintained a pointer to where the server has got to reading the events so that,
in the absence of failures, it receives each event that is added.

There are many different event bus implementations with greater or
lesser reliability and performance.
Many organizations establish a standard choice of their event
bus service.

Egeria uses [Apache Kafka](https://kafka.apache.org/)
as its default event bus implementation.
However, to allow other event bus implementations to
be plugged into the OMAG servers, all calls to the
event bus topics are issued through an
[open metadata topic connector](../../../adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors).

Details of open metadata topic connectors are added to a server's 
[configuration document](configuration-document.md) in various places.

To simplify this configuration the [Event Bus Config](../user/configuring-event-bus.md)
is added to the server's configuration document at the start of the configuration process.
The event bus config establishes a set of defaults for the
event bus.  These defaults are then used whenever open metadata topic connectors
are configured.

Figure 1 shows the event bus in action.

![Figure 1](event-bus-role.png#pagewidth)
> **Figure 1:** The event bus in use by OMAG servers and other technologies

It is hosting a number of topics.  Each topic manages the event exchange
for a particular collection of events.  For example, the
[OMRS Cohort Topic](../../../repository-services/docs/omrs-event-topic.md)
manages [repository services events](../../../repository-services/docs/event-descriptions).

The components using the event bus have a specialized connector that
supports event exchange for a specific collection of event.
Since it is necessary to be able to swap the event bus implementation,
These connectors embed a generic topic connector for the type of
event bus in use.  This is illustrated in Figure 2.

![Figure 2](nested-topic-connectors.png#pagewidth)
> **Figure 2:** Nested topic connectors

The configuration for these nested connectors is illustrated in
Figure 3.

![Figure 3](embedded-event-bus-config.png#pagewidth)
> **Figure 3:** Embedded event bus configuration

1. The common configuration for the event bus is identified and
configured using the [Event Bus Config](../user/configuring-event-bus.md).

2. This configuration is encoded in a
[Connection object](../../../frameworks/open-connector-framework/docs/concepts/connection.md) for the
generic open metadata topic connector.

3. When the consuming component is configured, a Connection object for
its specialized topic connector is created, with the generic open metadata
topic connector embedded inside.

4. When the [Connector Broker](../../../frameworks/open-connector-framework/docs/concepts/connector-broker.md)
is called upon to create the specialized topic connector at server start up,
it navigates the hierarchy of Connection objects, creating the nested hierarchy of connectors
as specified.

----

* Return to [Configuring a cohort](../user/configuring-registration-to-a-cohort.md)
* Return to [Configuring an OMAS Server](../user/configuring-an-omag-server.md)
* Return to [Administration Guide](../user)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.