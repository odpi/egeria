<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Discovery Configuration Server

The discovery configuration server is the server responsible for
holding and managing the configuration needed by the
[discovery servers](discovery-server.md) and
the [discovery engines](discovery-engine.md) within them.

This configuration consists of defining which [discovery request types](discovery-request-type.md) are supported and
which [discovery services](discovery-service.md) they map to.

## Implementation in Egeria

Egeria's discovery configuration server support is implemented by the
[Discovery Engine OMAS](../../../access-services/discovery-engine).
It has a [client](../../../access-services/discovery-engine/discovery-engine-client) called `DiscoveryConfigurationClient` that
implements the ODF's  `DiscoveryConfigurationServer` interface.
It also supports event notifications through 
the [Discovery Engine OMAS's out topic](../../../access-services/discovery-engine/docs/concepts/discovery-engine-omas-out-topic.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.