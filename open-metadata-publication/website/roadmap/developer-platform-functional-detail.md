<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Developer Platform Functional Detail


![Developer Platform Logo](developer-platform-logo.png)

Egeria's **Developer Platform** contains the core Egeria implementation and provides support for:
 * Integrating third party technology into the open metadata ecosystem;
 * Extending Egeria to run in different environments or to use different infrastructure services.

Its use is described in the [Developer's Guide](../developer-guide).

Figure 1 summarizes the capabilities it includes.  They are described
in the sections the follow.

![Developer Platform Detail](developer-platform-functional-detail.png)
> **Figure 1:** Developer Platform Detail

## Open Metadata and Governance (OMAG) Services

### Access Services
The [Open Metadata Access Services (OMASs)](../../../open-metadata-implementation/access-services) provide specialist APIs/Events for different
types of tools.  They work with the pre-defined open metadata types
described [below](#Open-Metadata-Types) and use the
[repository services](#Repository-Services) to access metadata.

There are access services for 
[data catalogs](../../../open-metadata-implementation/access-services/asset-consumer) and
[curation](../../../open-metadata-implementation/access-services/asset-owner) tools;
[glossary tools](../../../open-metadata-implementation/access-services/subject-area);
[data managers](../../../open-metadata-implementation/access-services/data-manager) and
[engines](../../../open-metadata-implementation/access-services/data-engine);
[data science/AI workbenches](../../../open-metadata-implementation/access-services/data-science),
[Business Intelligence (BI) and reporting platforms](../../../open-metadata-implementation/access-services/information-view);
[dev-ops pipelines and tools](../../../open-metadata-implementation/access-services/dev-ops);
[digital service management](../../../open-metadata-implementation/access-services/digital-architecture);
[software development tools](../../../open-metadata-implementation/access-services/software-developer),
[governance](../../../open-metadata-implementation/access-services/governance-program),
[privacy](../../../open-metadata-implementation/access-services/data-privacy) and 
[security](../../../open-metadata-implementation/access-services/governance-engine) tools;
[design modeling tools](../../../open-metadata-implementation/access-services/design-model),
[IT infrastructure management](../../../open-metadata-implementation/access-services/it-infrastructure),
[automated metadata discovery](../../../open-metadata-implementation/access-services/discovery-engine) tools,
[stewardship and workflow](../../../open-metadata-implementation/access-services/stewardship-action) tools.

The access services run in the following types of [cohort members](../../../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md):
[Metadata Access Point](../../../open-metadata-implementation/admin-services/docs/concepts/metadata-access-point.md),
[Metadata Server](../../../open-metadata-implementation/admin-services/docs/concepts/metadata-server.md) and
[Repository Proxy](../../../open-metadata-implementation/admin-services/docs/concepts/repository-proxy.md).

### Engine Services

The Open Metadata Engine Services (OMES) each provide the services that host a specific
type of governance engine.  They are hosted in an [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
governance server.

These services are still in design.  There is more information
described in its [engine-services](../../../open-metadata-implementation/engine-services) code module.

### Integration Services

The [Open Metadata Integration Services (OMIS)](../../../open-metadata-implementation/integration-services) each provide a
specialized API to integration connectors.  These are hosted in an
[integration daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).

The purpose of the integration services is to simplify the implementation and management of
connectors that integrate metadata exchange with third party technologies.  Follow the link
for the integration daemon to understand more.

### View Services

The [Open Metadata View Services](../../../open-metadata-implementation/view-services) provide the services used by UIs.
They are typically fine-grained services and they
run in the [view server](../../../open-metadata-implementation/admin-services/docs/concepts/view-server.md).
The use of the separate server (and server platform) enables an extra firewall to be erected between the
view servers and the metadata servers and governance servers, hiding the internal systems from end users.

### Repository Services

The [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) provide
the basic ability to share metadata between
metadata repositories.  The metadata repositories are organized into
[open metadata repository cohorts](../../../open-metadata-implementation/repository-services/docs/open-metadata-repository-cohort.md).
These cohorts define the scope of the metadata sharing.
There are two complementary mechanisms that
are operating together to ensure metadata is available to all consumers
in the cohort.  The first is the ability for any member of a cohort to issue
a federated query that includes all other members of the cohort.
There is also optional metadata replication occurring across the cohort
allowing a repository to selectively cache metadata from other members.

The repository services implementation includes the
[Type Definitions and Instance Structures](../../../open-metadata-implementation/repository-services/docs/metadata-meta-model.md)
that are used to support the open metadata types described above;
[Event Payloads](../../../open-metadata-implementation/repository-services/docs/event-descriptions)
for the asynchronous exchange of metadata between repositories in a cohort;
[Repository connector APIs](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/repository-connector.md)
to allow third party tools to "plug in" to open metadata
and [repository implementations](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)
to expand the metadata storage capability;
[Cohort Registration and Membership](../../../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md)
that broadcasts details of new members to automatically configure the repository services in
other members of the cohort;
Federated queries through the [enterprise repository services](../../../open-metadata-implementation/repository-services/docs/subsystem-descriptions/enterprise-repository-services.md);
a [Repository Conformance Workbench](../../../open-metadata-conformance-suite/docs/repository-workbench)
to test the ability of a cohort member to operate correctly in the cohort;
[Open Metadata Archives](../../../open-metadata-resources/open-metadata-archives)
for managing metadata import/export as well as content packs of standard definitions;
[Audit Log Implementation](../../../open-metadata-implementation/repository-services/docs/component-descriptions/audit-log.md) with routing to
[multiple destinations](../../../open-metadata-implementation/admin-services/docs/user/configuring-the-audit-log.md) as needed by the
operations teams.

# Open Metadata Types

The [open metadata types](../open-metadata-types) provide common definitions for the different types of
metadata needed by an organization.  The open metadata type system is
[extendable](../../../open-metadata-resources/open-metadata-archives/open-metadata-types).
However, by providing a comprehensive starter set, and encouraging tools
to use them, then Egeria ensures metadata can be seamlessly shared amongst them.

The predefined types are organized as follows:
IT Infrastructure ([Area 0](../open-metadata-types/Area-0-models.md)); 
Collaboration ([Area 1](../open-metadata-types/Area-1-models.md)); 
Assets ([Area 2](../open-metadata-types/Area-2-models.md));
Glossary ([Area 3](../open-metadata-types/Area-3-models.md));
Governance ([Area 4](../open-metadata-types/Area-4-models.md));
Schemas, Reference Data, Models ([Area 5](../open-metadata-types/Area-5-models.md));
Automatic Metadata Discovery ([Area 6](../open-metadata-types/Area-6-models.md));
Lineage ([Area 7](../open-metadata-types/Area-7-models.md)).

# Open Metadata and Governance (OMAG) Server Platform

The [OMAG Server Platform](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md)
provides a multi-tenant runtime platform for [OMAG Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md).
Each OMAG Server hosts the connectors along with the Egeria services to integrate third party technology.

### Server Chassis

The [server chassis](../../../open-metadata-implementation/server-chassis) uses Spring Boot to provide the web server and REST API support
for the platform.

### Administration Services

The [admin services](../../../open-metadata-implementation/admin-services) supports configuring and operating the OMAG Platform and Servers.
Details of how to use the admin services are provided in the
[Administration Guide](../../../open-metadata-implementation/admin-services/docs/user)

### Platform Services

The [platform services](../../../open-metadata-implementation/platform-services) provide the means to
query the OMAG servers and services running on an OMAG Server Platform.
 
### Multi-tenancy Management

The [multi-tenant](../../../open-metadata-implementation/common-services/multi-tenant) module supports
multiple OMAG Servers running on an OMAG Server Platform.

### Metadata Security

The [metadata-security](../../../open-metadata-implementation/common-services/metadata-security) module
provides
customizable authorization checks for calls to the OMAG Server Platform, OMAG Server and the open metadata instances
themselves.


### Governance Services

The [governance services](../../../open-metadata-implementation/governance-servers) each provide the principle subsystem of a 
[governance server](../../../open-metadata-implementation/admin-services/docs/concepts/governance-server-types.md).
They include:

* [data-engine-proxy-services](../../../open-metadata-implementation/governance-servers/data-engine-proxy-services) for
the [Data Engine Proxy](../../../open-metadata-implementation/admin-services/docs/concepts/data-engine-proxy.md).
* [discovery-engine-services](../../../open-metadata-implementation/governance-servers/discovery-engine-services) for
the [Discovery Server](../../../open-metadata-implementation/admin-services/docs/concepts/discovery-server.md).
* [integration-daemon-services](../../../open-metadata-implementation/governance-servers/integration-daemon-services) for
the [Integration Daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).
* [engine-host-services](../../../open-metadata-implementation/governance-servers/integration-daemon-services) for
the [Engine Hosting Server](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).
* [open-lineage-services](../../../open-metadata-implementation/governance-servers/open-lineage-services) for
the [Open Lineage Server](../../../open-metadata-implementation/admin-services/docs/concepts/open-lineage-server.md).
* [security-officer-services](../../../open-metadata-implementation/governance-servers/security-officer-services) for
the [Security Server](../../../open-metadata-implementation/admin-services/docs/concepts/security-officer-server.md).
* [security-sync-services](../../../open-metadata-implementation/governance-servers/security-officer-services) for
the [Security Sync Server](../../../open-metadata-implementation/admin-services/docs/concepts/security-sync-server.md).
* [stewardship-engine-services](../../../open-metadata-implementation/governance-servers/stewardship-engine-services) for
the [Stewardship Server](../../../open-metadata-implementation/admin-services/docs/concepts/stewardship-server.md).

Each governance server is making use of open metadata to actively manage an aspect of the digital landscape.

Note: as our experience with writing governance servers increases, there is planned
consolidation in the governance services:
* The integration services that run in the integration daemon will be extended to provide
the support now offered by the data engine proxy services and security sync services.
* The engine services that run in the engine hosting server will be implemented to support the
function currently offered by the discovery-engine-services and the stewardship-engine-services.

### Generic Handlers

The [generic handlers](../../../open-metadata-implementation/common-services/generic-handlers) provide support for the
type specific maintenance and retrieval of metadata that follows the [open metadata
types](../open-metadata-types).  This includes managing visibility of metadata through the
[Governance Zones](../../../open-metadata-implementation/access-services/docs/concepts/governance-zones),
calls to [Open Metadata Security](../../../open-metadata-implementation/common-services/metadata-security) and
[metadata management using templates](../cataloging-assets/templated-cataloging.md).

# Open Metadata Frameworks for plug-in components

The [open metadata frameworks](../../../open-metadata-implementation/frameworks) define the interfaces implemented by components that
"plug-in" to Egeria, either to integrate calls to third party technology or extend the function of Egeria.  The frameworks are as follows:

* [Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework) - base framework for all types of plug-in components called connectors.
* [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework) - specialized connectors called discovery services that support automated metadata discovery,
* [Governance Action Framework (GAF)](../../../open-metadata-implementation/frameworks/governance-action-framework) - specialized connectors for the triage and remediation of issues found in the digital landscape.
* [Audit Log Framework (ALF)](../../../open-metadata-implementation/frameworks/audit-log-framework) - extensions for all types of connectors to enable natural language diagnostics
such as exceptions and audit log messages. 

----
Return to [Status Overview](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.