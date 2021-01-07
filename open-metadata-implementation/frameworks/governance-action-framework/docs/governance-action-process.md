<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Action Process

A governance action process defines a prescribed sequence of
[governance actions](governance-action.md).
Its definition consists of a linked set of
[governance action types](governance-action-type.md).
Each governance action type describes which
[governance action service](governance-action-service.md)
to run from which [governance action engine](governance-action-engine.md)
along with the [request type](governance-action-request-type.md) and
[request parameters](governance-action-request-parameters.md) to pass.
The linkage between the the governance action types shows the
[guards](guard.md) that must be true to initiate the
next governance action in the flow.





## Open metadata types

[0462 Governance Action Types](../../../../open-metadata-publication/website/open-metadata-types/0470-Incident-Reporting.md)
shows the structure of the incident report.
It is a [Referenceable](../../../../open-metadata-publication/website/open-metadata-types/0010-Base-Model.md)
so it can support comments and have [governance actions](governance-action.md) linked to it.

## Further information

* Governance action processes are defined using the [Governance Engine OMAS](../../../access-services/governance-engine).

* The [Open Metadata Engine Services (OMES)](../../../engine-services) provide the mechanisms
  that support the different types of [governance action engines](governance-action-engine.md).  These engines
  run the [governance action services](governance-action-service.md) that execute the
  [governance actions](governance-action.md) defined by the governance action process.
 
 
----

* Return to [Governance Action Framework Overview (GAF)](..)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.