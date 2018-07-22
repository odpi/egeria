<!-- SPDX-License-Identifier: Apache-2.0 -->

# Connectors consumed by the OMRS

A Connector is a Java class that supports the standard Open Connector Framework (OCF) APIs.
The Open Metadata Repository Services (OMRS) defines the following connectors to enable
support for specific operating platform services to be plugged into the OMRS.

* **[Audit Log Store Connector](audit-log-store-connector.md)** - supports the reading and writing of
audit log records to specific destinations on behalf of the **[OMRS Audit Log](../audit-log.md)**.

* **[Cohort Registry Store Connector](cohort-registry-store-connector.md)** - supports the
reading and writing of the cohort registry store to specific destinations on behalf of
the **[Cohort Registry](../cohort-registry.md)**.

* **[Event Mapper Connector](event-mapper-connector.md)** - receives proprietary repository
events from a metadata repository and passes them to the OMRS for distribution.

* **[OMRS Topic Connector](omrs-topic-connector.md)** - manages the exchange 
of **[OMRS Events](../../event-descriptions/README.md)** with
the **[OMRS Topic](../../omrs-event-topic.md)** by calling
the **[Open Metadata Topic Connector](open-metadata-topic-connector.md)**.

* **[Open Metadata Archive Store Connector](open-metadata-archive-store-connector.md)** - supports the
reading an writing of **[open metadata archives](../../open-metadata-archive.md)**
on behalf of the **[archive manager](../archive-manager.md)**.

* **[Open Metadata Topic Connector](open-metadata-topic-connector.md)** - manages
the calls to the **[event bus](../../../../adapters/open-connectors/event-bus-connectors)** to support
the **[OMRS Topic](../../omrs-event-topic.md)**.

* **[Repository Connector](repository-connector.md)** supports calls to a real metadata repository on behalf
of the **[local-repository-connector](../local-repository-connector.md).

The OMRS defines the interfaces for these connectors and selected implementations
are located in the **[adapters](../../../../adapters/open-connectors/repository-services-connectors)** package.

All of these connectors support
the [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework)
which means the OMRS uses the 
OCF [Connector Broker](../../../../frameworks/open-connector-framework/docs/connector-broker.md)
to create the instances of the connectors it needs.
The type of connector needed is defined in a connection object.

The connection objects for OMRS's connectors are defined in the
OMAG Server configuration document that is passed to the
server when the open metadata services are activated.

This means that new implementations can be specified without needing
to change the OMRS code.
