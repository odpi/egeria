<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Action Process

A governance action process is a predefined sequence of
[governance actions](governance-action.md) that are coordinated by
the [Metadata Store Service](../..).

The steps in a governance action process are defined by linked [governance action types](governance-action-type.md).
Each governance action type provides the specification of the governance action to run.
The links between then show which guards cause the governance action to run.

Details of how to set up governance action process is described in the 
[Metadata Store Service User Guide](../user).


## Open metadata types

[0462 Governance Action Types](https://egeria-project.org/types/4/0470-Incident-Reporting)
shows the structure of the incident report.
It is a [Referenceable](https://egeria-project.org/types/0/0010-Base-Model.md)
so it can support comments and have [governance actions](governance-action.md) linked to it.

## Further information

* The [Open Metadata Engine Services (OMES)](../../../../engine-services) provide the mechanisms
  that support the different types of [governance engines](governance-engine.md).  These engines
  run the [governance services](governance-service.md) that execute the
  [governance actions](governance-action.md) defined by the governance action process.
 
 

----

* [Return to Metadata Store Service Concepts](.)
* [Return to Metadata Store Service Overview](../..)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.