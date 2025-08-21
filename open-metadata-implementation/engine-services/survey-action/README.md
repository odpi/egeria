<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Survey Action Open Metadata Engine Service (OMES)

The Survey Action Open Metadata Engine services provide support for
[survey action engines](https://egeria-project.org/concepts/survey-action-engine/)
that are part of the [Open Discovery Service (ODF)](https://egeria-project.org/frameworks/odf/overview/).
An survey action engine hosts [automated metadata discovery](https://egeria-project.org/features/discovery-and-stewardship/overview/).

The Survey Action OMES is capable of hosting one or more
[survey action engines](https://egeria-project.org/concepts/survey-action-engine/)
and supports a REST API to request that a survey action engine runs a
[survey action service](https://egeria-project.org/concepts/survey-action-service/)
to analyse an [asset](https://egeria-project.org/concepts/asset/) and to access the results.
The results of each of these
calls is a [survey report](https://egeria-project.org/survey-report/).

The survey action engine services call REST APIs
running in a Metadata Access Server to retrieve information about assets and to
store the results of the survey action services.

Detailed design documentation is found on the [egeria website](https://egeria-project.org/services/omes/survey-action/overview).

----
* Return to [engine services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.