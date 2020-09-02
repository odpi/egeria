<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Connectors consumed by the OMRS

A Connector is a Java class that supports the standard [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework) APIs.
The Open Metadata Repository Services (OMRS) defines the following connectors to enable
support for specific operating platform services to be plugged into the OMRS.

* **[Audit Log Store Connector](audit-log-store-connector.md)** - supports the reading and writing of
audit log records to specific destinations on behalf of the **[OMRS Audit Log](../audit-log.md)**.

* **[Cohort Registry Store Connector](cohort-registry-store-connector.md)** - supports the
reading and writing of the cohort registry store to specific destinations on behalf of
the **[Cohort Registry](../cohort-registry.md)**.

* **[Event Mapper Connector](event-mapper-connector.md)** - informs a cohort
of changes to metadata mastered in a third party metadata repository that
occurred through the third party technology's own mechanisms.
It is always paired with the repository connector for
the third party technology.

* **[OMRS Topic Connector](omrs-topic-connector.md)** - manages the exchange 
of **[OMRS Events](../../event-descriptions/README.md)** with
the **[OMRS Topic](../../omrs-event-topic.md)** by calling
the **[Open Metadata Topic Connector](open-metadata-topic-connector.md)**.

* **[Open Metadata Archive Store Connector](open-metadata-archive-store-connector.md)** - supports the
reading and writing of **[open metadata archives](../../open-metadata-archive.md)**
on behalf of the **[archive manager](../archive-manager.md)**.

* **[Open Metadata Topic Connector](open-metadata-topic-connector.md)** - manages
the calls to the **[event bus](../../../../admin-services/docs/concepts/event-bus.md)** to support
specific topic connectors such as the OMRS Topic Connector described above.

* **[Repository Connector](repository-connector.md)** defines the open metadata common interface
to a metadata repository.  This may be a repository with
native support for the open metadata types and instances or
one that is mapping to a third party metadata repository API.
In the case of it supporting an third party metadata repository,
it is often paired with an event mapper connector described above.

The OMRS defines the interfaces for these connectors and selected implementations
are located in the **[adapters](../../../../adapters/open-connectors/repository-services-connectors)** package.

All of these connectors support
the [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework)
which means the OMRS uses the 
OCF [Connector Broker](../../../../frameworks/open-connector-framework/docs/concepts/connector-broker.md)
to create the instances of the connectors it needs.
The type of connector needed is defined in a connection object.

The connection objects for OMRS's connectors are defined in the
OMAG Server configuration document that is passed to the
server when the open metadata services are activated.

This means that new implementations can be specified without needing
to change the OMRS code.

----
Return to the repository services [component descriptions](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
