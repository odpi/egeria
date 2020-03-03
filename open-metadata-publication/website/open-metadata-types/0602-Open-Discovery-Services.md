<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0602 Open Discovery Services

[Open discovery services](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-service.md)
are pluggable components that analyse data sources and document the results.

The OpenDiscoveryService entity describes an implementation of an open discovery service.
This implementation is an [OCF Connector](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connector.md)
so the OpenDiscoveryService is linked with a DiscoveryServiceImplementation relationship to a 
[Connection](0201-Connectors-and-Connections.md) entity
that defines how to create an instance of the open discovery service.

A description of an open discovery service is linked with a SupportedDiscoveryService to the
description of an [OpenDiscoveryEngine](0601-Open-Discovery-Engine.md) to indicate that
the discovery engine supports the open discovery service.

![UML](0602-Open-Discovery-Services.png#pagewidth)


Return to [Area 6](Area-6-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.