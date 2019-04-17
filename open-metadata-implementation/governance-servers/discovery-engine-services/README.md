<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Discovery Engine Services

The discovery engine services provide the implementation of a
[discovery server](../../frameworks/open-discovery-framework/docs/discovery-server.md))
that hosts one or more
[discovery engines](../../frameworks/open-discovery-framework/docs/discovery-engine.md).

A discovery engine is a server capability that runs
[Open Discovery Services](../../frameworks/open-discovery-framework/docs/discovery-service.md) on request.

An open discovery service is a specialized component from the
[Open Discovery Framework (ODF)](../../frameworks/open-discovery-framework/docs).
It provides analysis of the properties of a supplied asset in the form of
[Annotations](../../frameworks/open-discovery-framework/docs/discovery-annotation.md)

The discovery engine is initialized with information about the
[Discovery Engine OMAS](../../access-services/discovery-engine) it should connect with to
retrieve information about the discovery services and to record their results.

The discovery engine provides a REST API to request that a specific discovery service is run for an asset, or
that all applicable discovery services are run for the asset.

The results of each of these
calls is a [discovery analysis report](docs/concepts/discovery-analysis-report.md).

The discovery engine calls the Discovery Engine OMAS to retrieve information about the Asset's supported discovery
services and to attach the discovery analysis report to the Asset.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.