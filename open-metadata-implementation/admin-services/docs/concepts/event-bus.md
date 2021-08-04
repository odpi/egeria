<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Event Bus

Egeria's event bus service is constructed from
an [event broker](../../../../open-metadata-publication/website/basic-concepts/event-broker.md) infrastructure service
and a set of well known [topics](../../../../open-metadata-publication/website/basic-concepts/topic.md).
Collectively they provide the ability to reliably pass events between different OMAG servers:

* To register with an [Open Metadata Repository Cohort](cohort-member.md), exchange type definitions and
  share changes to metadata with other members of the cohort via the OMRS Topic(s).
  
* To exchange details of metadata changes through an Open Metadata Access Service (OMAS)'s
  [InTopic](../../../access-services/docs/concepts/client-server/in-topic.md) and
  [OutTopic](../../../access-services/docs/concepts/client-server/out-topic.md).

Each topic maintains a pointer to the last event that a server has read so that
it receives each event that is added even if it restarts.

There are many different event broker implementations with greater or
lesser reliability and performance.
Many organizations establish a standard choice of their event
broker service which is why Egeria uses connectors to implement its event bus.

Egeria's default event broker is [Apache Kafka](https://kafka.apache.org/).
Each topic is accessed through an
[open metadata topic connector](../../../../open-metadata-publication/website/connector-catalog/runtime-connectors.md#Open Metadata Topic Connectors).

Figure 1 shows the event bus in action.

![Figure 1](event-bus-role.png#pagewidth)
> **Figure 1:** The event bus in use by OMAG servers and other technologies

Details of open metadata topic connectors are needed in multiple places in a server's 
[configuration document](configuration-document.md).
To simplify this configuration, the [Event Bus Config](../user/configuring-event-bus.md)
is added to the server's configuration document at the start of the configuration process.
The event bus config establishes a set of defaults for the
open metadata topic connectors.  These defaults are used whenever open metadata topic connectors
are configured.

The subsystems using the event bus have a specialized connector that
supports event exchange for a specific type of event.
Since it is necessary to be able to swap the event broker implementation,
these connectors embed a open metadata topic connector within their implementation.
This is illustrated in Figure 2.

![Figure 2](nested-topic-connectors.png#pagewidth)
> **Figure 2:** Nested topic connectors

When the connection for one of these subsystem topic connectors is configured,
the defaults from the Event Bus Config
are used to set up the nested open metadata topic connection.

The resulting configuration for these nested connectors is illustrated in
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
inside Egeria's runtime is called upon to create the specialized topic connector at server start up,
it navigates the hierarchy of Connection objects, creating the nested hierarchy of connectors
as specified.

----

* Return to [Configuring a cohort](../user/configuring-registration-to-a-cohort.md)
* Return to [Configuring an OMAS Server](../user/configuring-an-omag-server.md)
* Return to [Administration Guide](../user)
* Return to [Connector Catalog](../../../../open-metadata-publication/website/connector-catalog)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.