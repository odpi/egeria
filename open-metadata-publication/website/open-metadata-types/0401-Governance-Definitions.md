<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0401 Governance Definitions

The world of governance is divided into different governance domains that focus on a specific set of assets or activities.
Egeria aims to unify the metadata and governance activity across
these governance domains.  **GovernanceDomain** lists the
different types of governance domains that can be unified by Egeria.
Notice that there are overlaps between the domains:

* DATA - the governance of data.
* PRIVACY - the support for data privacy.
* SECURITY - the governance that ensures IT systems are secure.
* IT_INFRASTRUCTURE = the governance of the configuration and management of IT infrastructure and the software that runs on it.
* SOFTWARE_DEVELOPMENT - the governance of the software development lifecycle.
* CORPORATE - the governance of the organization as a legal entity.
* ASSET_MANAGEMENT - the governance of physical assets.

The leader of a governance domain is represented as a **GovernanceOfficer**.

**GovernanceDefinition** is a definition that controls an aspect of a governance program.
They are authored in the metadata repository.  
They inherit from [Referenceable](0010-Base-Model.md),
which means they have a unique identifier and link to external references for more information.

![UML](0401-Governance-Definitions.png#pagewidth)


Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.