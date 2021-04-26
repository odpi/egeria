<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0420 Governance Controls

Governance is enabled through People, Process and Technology.
These are controlled through a combination of technical controls (implemented IT function) and organizational controls (training, responsibility, buddy-checking etc).

![UML](0420-Governance-Controls.png#pagewidth)

## Further Information

* Governance controls are types of **GovernanceDefinitions** which are located in model [0401](0401-Governance-Definitions.md).
  The governance policies are defined in model [0415](0415-Governance-Responses.md).

* The [Governance Program OMAS](../../../open-metadata-implementation/access-services/governance-program)
  provides support for defining governance policies through its **GovernancePolicyMakingInterface**. 

* There is further detail on the content of the governance controls in the following models:

     * [0430 Technical Controls](0430-Technical-Controls.md) - describe automated behaviour that implements a governance control.
       * [0438 Naming Standards](0438-Naming-Standards.md) - defines naming standard rules.
       * [0460 Governance Execution Points](0460-Governance-Execution-Points.md) - describe classifications for software components that link them to a technical control.
       * [0461 Governance Action Engines](0461-Governance-Engines.md) - support the execution of technical controls.
       * [0462 Governance Action Types](0462-Governance-Action-Types.md) - provide the choreography of the execution of technical controls.
     * [0440 Organizational Controls](0440-Organizational-Controls.md) - identity governance roles and manual procedures (such as approvals) that implement a governance control.
       * [0445 Governance Roles](0445-Governance-Roles.md) - define governance roles and the people associated with them.


----

Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.