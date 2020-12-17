<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0601 Open Discovery Engines and Services

An [open discovery engine](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-engine.md)
executes [open discovery services](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-service.md)
on request.

The OpenDiscoveryEngine entity creates a description of an instance of these types of engines.
It is represented as a special type [GovernanceEngine](0461-Governance-Engines.md) when it is documented in metadata.

[Open discovery services](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-service.md)
are pluggable components that analyse data sources and document the results.

The OpenDiscoveryService entity describes an implementation of an open discovery service.
This implementation is an [OCF Connector](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connector.md)
so the OpenDiscoveryService is linked with a DiscoveryServiceImplementation relationship to a 
[Connection](0201-Connectors-and-Connections.md) entity
that defines how to create an instance of the open discovery service.

A description of an open discovery service is linked with a [SupportedGovernanceService](0461-Governance-Engines.md)
relationship to the description of an [OpenDiscoveryEngine](0601-Open-Discovery-Engine.md) to indicate that
the discovery engine supports the open discovery service.

[Open discovery pipelines](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-pipeline.md)
are specialized [open discovery services](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-service.md)
that execute multiple open discovery services in a single run.

The OpenDiscoveryPipeline describes an open discovery pipeline.
It is typically linked to a [VirtualConnection](0205-Connection-Linkage.md) with the
connections of the open discovery services embedded within it.

![UML](0601-Open-Discovery-Engine.png#pagewidth)


Return to [Area 6](Area-6-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.