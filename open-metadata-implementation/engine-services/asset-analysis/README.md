<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Deprecated](../../../images/egeria-content-status-deprecated.png#pagewidth)

# Asset Analysis Open Metadata Engine Service (OMES)

The Asset Analysis Open Metadata Engine services provide support for
[open discovery engines](https://egeria-project.org/concepts/open-discovery-engine/)
that are part of the [Open Discovery Service (ODF)](https://egeria-project.org/frameworks/odf/overview/).
An open discovery engine hosts [automated metadata discovery](https://egeria-project.org/features/discovery-and-stewardship/overview/).

The Asset Analysis OMES is capable of hosting one or more
[discovery engines](https://egeria-project.org/concepts/open-discovery-engine/)
and supports a REST API to request that a discovery engine runs a
[discovery service](https://egeria-project.org/concepts/open-discovery-service/)
to analyse an [asset](https://egeria-project.org/concepts/asset/) and to access the results.
The results of each of these
calls is a [discovery analysis report](https://egeria-project.org/discovery-analysis-report/).

The discovery engine services call the
[Discovery Engine Open Metadata Access Service (OMAS)](https://egeria-project.org/services/omas/discovery-engine/overview)
running in a Metadata Access Server to retrieve information about assets and to
store the results of the discovery services.

Detailed design documentation is found on the [egeria website](https://egeria-project.org/services/omes/asset-analysis/overview).

----
* Return to [engine services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.