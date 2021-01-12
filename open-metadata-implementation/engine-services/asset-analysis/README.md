<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Asset Analysis Open Metadata Engine Service (OMES)

The Asset Analysis Open Metadata Engine services provide support for
[open discovery engines](../../frameworks/open-discovery-framework/docs/discovery-engine.md)
that are part of the [Open Discovery Service (ODF)](../../frameworks/open-discovery-framework).



## Open Discovery Engines

A discovery engine hosts [automated metadata discovery](../../../open-metadata-publication/website/metadata-discovery).

The Asset Analysis OMES is capable of hosting one or more
[discovery engines](../../frameworks/open-discovery-framework/docs/discovery-engine.md)
and supports a REST API to request that a discovery engine runs an
[discovery service](../../frameworks/open-discovery-framework/docs/discovery-service.md)
to analyse an [asset](../../access-services/docs/concepts/assets) and to access the results.
The results of each of these
calls is a [discovery analysis report](../../frameworks/open-discovery-framework/docs/discovery-analysis-report.md).

The REST API also supports a request to a discovery engine to run a specific open discovery service
against each asset it has access to.

The discovery engine services call the
[Discovery Engine Open Metadata Access Service (OMAS)](../../access-services/discovery-engine)
running in an open metadata server to retrieve information about assets and to
store the results of the discovery services.

----
* Return to [engine services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.