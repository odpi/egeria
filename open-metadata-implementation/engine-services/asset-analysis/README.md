<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Asset Analysis Open Metadata Engine Service (OMES)

The Asset Analysis engine service runs [Open Discovery Engines](../../)


## Open Discovery Engines

The Asset Analysis engine services provide the core subsystem for a
[discovery server](../../admin-services/docs/concepts/discovery-server.md).
A discovery server is an [OMAG Server](../../admin-services/docs/concepts/omag-server.md)
that hosts [automated metadata discovery](../../../open-metadata-publication/website/metadata-discovery).

The discovery engine services subsystem is capable of hosting one or more
[discovery engines](../../frameworks/open-discovery-framework/docs/discovery-engine.md)
and supports a REST API to request that a discovery engine runs an
[discovery service](../../frameworks/open-discovery-framework/docs/discovery-service.md)
to analyse an [asset](../../access-services/docs/concepts/assets) and to access the results.

The REST API also supports a request to a discovery engine to run a specific open discovery service
against each asset it has access to.

The discovery engine services call the
[Discovery Engine Open Metadata Access Service (OMAS)](../../access-services/discovery-engine)
running in an open metadata server to retrieve information about assets and to
store the results of the discovery services.

## Open Discovery Framework (ODF)

The terminology associated with the asset analysis engine services comes from the
[Open Discovery Framework (ODF)](../../frameworks/open-discovery-framework/docs).
This framework provides the interface
definitions for open discovery services
and the APIs that support them.

The Asset Analysis engine services provide the core subsystem for the ODF
[discovery server](../../frameworks/open-discovery-framework/docs/discovery-server.md).

The discovery engines hosted by the Asset Analysis engine services are the ODF
[discovery engines](../../frameworks/open-discovery-framework/docs/discovery-engine.md).

The Asset Analysis engine services
also supports a REST API to request that a discovery engine runs an
[discovery service](../../frameworks/open-discovery-framework/docs/discovery-service.md)
to analyse an [asset](../../access-services/docs/concepts/assets) and to access the results.

An open discovery service is a specialized component from the
[Open Discovery Framework (ODF)](../../frameworks/open-discovery-framework/docs).
It provides analysis of the properties of a supplied asset in the form of
[Annotations](../../frameworks/open-discovery-framework/docs/discovery-annotation.md)

The Asset Analysis engine services is initialized with information about the
[Discovery Engine OMAS](../../access-services/discovery-engine) it should connect with to.
This is the metadata service that provides the definition of the discovery services
supported by the list of named discovery engines  and to record their results.

The discovery engine provides a REST API to request that a specific discovery service is run for an asset, or
that all applicable discovery services are run for the asset.

The results of each of these
calls is a [discovery analysis report](../../frameworks/open-discovery-framework/docs/discovery-analysis-report.md).

The discovery engine calls the Discovery Engine OMAS to retrieve information about the Asset's supported discovery
services and to attach the discovery analysis report to the Asset.

----
* Return to [engine services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.