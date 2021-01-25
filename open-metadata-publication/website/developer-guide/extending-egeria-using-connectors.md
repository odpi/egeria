<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

## Extending Egeria using connectors

Egeria has extended the basic concept of the OCF connector and created specialized connectors for
different purposes.  The following types of connectors are supported by the Egeria subsystems with links to the 
documentation and implementation examples. 


| Type of Connector             | Description | Documentation| Owning module | Implementation Examples |
| :-----------------------------| :---------- | :----------- | :--------------- | :---------------------- |
| Integration Connector         | Implements metadata exchange with third party tools. | [Building Integration Connectors](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md)| [Integration Daemon Services](../../../open-metadata-implementation/governance-servers/integration-daemon-services) | [integration- connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors) |
| Open Discovery Service        | Implements automated metadata discovery.| [Open Discovery Services](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-service.md) | [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework) | [discovery-service- connectors](../../../open-metadata-implementation/adapters/open-connectors/discovery-service-connectors) |
| Governance Action Service     | Implements automated governance. | [Governance Action Services](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/governance-action-service.md) | [Governance Action Framework (GAF)](../../../open-metadata-implementation/frameworks/governance-action-framework) | [governance-action- connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors) |
| Configuration Document Store  | Persists the configuration document for an OMAG Server. | [Configuration Document Store Connectors](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document-store-connector.md)  | [Administration Services](../../../open-metadata-implementation/admin-services) | [configuration-store- connectors](../../../open-metadata-implementation/adapters/open-connectors/configuration-store-connectors) |
| Platform Security Connector   | Manages service authorization for the OMAG Server Platform.| [Metadata Security Connectors](../../../open-metadata-implementation/common-services/metadata-security/metadata-security-connectors) | [Metadata Security](../../../open-metadata-implementation/common-services/metadata-security/metadata-security-connectors) | [open-metadata- security-samples](../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) |
| Server Security Connector     | Manages service and metadata instance authorization for an OMAG Server.| [Metadata Security Connectors](../../../open-metadata-implementation/common-services/metadata-security/metadata-security-connectors) | [Metadata Security](../../../open-metadata-implementation/common-services/metadata-security/metadata-security-connectors) | [open-metadata- security-samples](../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) |
| Metadata Collection (repository) Store | Interfaces with a metadata repository API for retrieving and storing metadata.| [OMRS Repository Connectors](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/repository-connector.md)| [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) | [open-metadata- collection-store- connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)
| Metadata Collection (repository) Event Mapper | Maps events from a third party metadata repository to open metadata events.| [OMRS Event Mappers](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/event-mapper-connector.md) | [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) | none |
| Open Metadata Archive Store   | Reads an open metadata archive from a particular type of store.| [OMRS Open Metadata Archive Store Connector](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/open-metadata-archive-store-connector.md) | [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) | [open-metadata- archive-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)
| Audit Log Store               | Audit logging destination | [OMRS Audit Log Store Connector](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/audit-log-store-connector.md) | [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) | [audit-log-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors) |
| Cohort Registry Store         | Local store of membership of an open metadata repository cohort.| [OMRS Cohort Registry Store](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/cohort-registry-store-connector.md)| [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) | [cohort-registry- store-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors) |
| Open Metadata Topic Connector | Connects to a topic on an external event bus such as Apache Kafka.| [OMRS Open Metadata Topic Connectors](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/open-metadata-topic-connector.md) | [Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services) | [open-metadata- topic-connectors](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors) |


You can write your own connectors to integrate additional types of technology or extend the
capabilities of Egeria - and if you think your connector is more generally useful,
you could consider [contributing it to the Egeria project](../../../Community-Guide.md).


----
* Return to [Developer Guide](.)
* Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.