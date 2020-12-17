<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0461 Governance Action Engines

A governance action engine is a server capability that is able to run specific services
on demand.  These services, called governance action services, typically implement specific logic that
is needed to govern an organization's asset or the metadata associated with them.

Governance action engines and services are defined by
the [Governance Action Framework](../../../open-metadata-implementation/frameworks/governance-action-framework).
Their implementation runs in the [engine services](../../../open-metadata-implementation/engine-services)
running in an [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
OMAG Server.

The **GovernanceActionEngine** and the linked **GovernanceActionService** define the
configuration that drives the processing of [Governance Actions](0463-Governance-Actions.md).
The configuration in the Engine Host server identifies the engines to run
by specifying the **qualifiedName** property of the **GovernanceActionEngine**.



![UML](0461-Governance-Engines.png#pagewidth)


Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.