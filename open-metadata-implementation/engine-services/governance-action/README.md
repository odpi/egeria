<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Governance Action Open Metadata Engine Services (OMES)

Governance Action Open Metadata Engine Service (OMES) runs [Governance Action Engines](https://egeria-project.org/concepts/governance-action-engine).

The Governance Action OMES is capable of hosting one or more governance action engines and supports a REST API
to request that a governance action engine runs a
[governance action service](https://egeria-project.org/concepts/governance-action-service) against a target
element, along with the ability to query the status of governance actions in progress.

The governance action engine services call REST APIs running in a Metadata Access Server to retrieve information
about the governance action and to record its results.

Detailed design documentation is found on the [egeria website](https://egeria-project.org/services/omes/governance-action/overview).


----
* Return to the [Engine Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.