<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Developer Platform Functional Detail


![Developer Platform Logo](developer-platform-logo.png)

Egeria's **Developer Platform** contains the core Egeria implementation and provides support for:
 * Integrating third party technology into the open metadata ecosystem;
 * Extending Egeria to run in different environments or to use different infrastructure services.

Below is a summary of the capabilities it includes.
These capabilities support the [Integration Platform](integration-platform-functional-detail.md)
and the [Governance Solutions](governance-solution-functional-detail.md).
Its use is described in the [Developer's Guide](../developer-guide).


## Access Services
The Access Services provide specialist APIs/Events for different
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

## Governance Services

The [governance services](../../../open-metadata-implementation/governance-servers) provide
support for automatic exchange of metadata with third party tools as well as
supporting metadata management for [governance solutions](governance-solution-functional-detail.md).
The governance services each form the principle subsystem of a 
[governance server](../../../open-metadata-implementation/admin-services/docs/concepts/governance-server-types.md).

## View Services

The [view services](../../../open-metadata-implementation/view-services) provide the services used by UIs.
They are typically fine-grained services and they
run in the [view server](../../../open-metadata-implementation/admin-services/docs/concepts/view-server.md).

## Open Metadata Types
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

## Repository Services
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

## OMAG Server Platform

The [Open Metadata and Governance (OMAG) Server Platform](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md)
provides a multi-tenant runtime platform for [OMAG Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md).
Each OMAG Server hosts the connectors along with the Egeria services to integrate third party technology.

Specifically the OMAG Server Platform provides
support for the 
[configuration document store connector](../../../open-metadata-implementation/admin-services/docs/user/configuring-the-configuration-document-store.md)
and [the connector that is responsible for authorization of platform services requests]();
Platform Services; Multi-tenancy; Metadata Security;
[Administration Services](../../../open-metadata-implementation/admin-services) for configuring and operating the OMAG Platform and Servers.

## Open Metadata Frameworks for plug-in components

The [open metadata frameworks](../../../open-metadata-implementation/frameworks) define the interfaces implemented by components that
"plug-in" to Egeria, either to integrate calls to third party technology or extend the function of Egeria.  The frameworks are as follows:

* [Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework) - base framework for all types of plug-in components called connectors.
* [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework) - specialized connectors called discovery services that support automated metadata discovery,
* [Governance Action Framework (GAF)](../../../open-metadata-implementation/frameworks/governance-action-framework) - 
* [Audit Log Framework (ALF)](../../../open-metadata-implementation/frameworks/audit-log-framework) - extensions for all types of connectors to enable natural language diagnostics
such as exceptions and audit log messages. 

----
Return to [Status Overview](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.