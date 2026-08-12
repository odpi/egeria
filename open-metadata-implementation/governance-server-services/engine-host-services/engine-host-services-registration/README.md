<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Engine Host Services - registration

The **engine-host-services-registration** module contains the abstract base classes and interfaces
that each [Open Metadata Engine Service (OMES)](../../../engine-services) implements and extends in order to
register itself with the engine host and run one or more
[governance engines](https://egeria-project.org/concepts/governance-engine/).  `OMAGEngineServiceRegistration`
allows an engine service to register itself with the OMAG Server, `EngineServiceAdmin` is implemented by each
engine service to receive its configuration, and `GovernanceEngineHandlerFactory` is implemented to create the
`GovernanceEngineHandler` for its type of governance engine.  The resulting handlers are held by the
`GovernanceEngineMap`, along with the `GovernanceServiceCache` of governance service connectors and the
`EngineConfigurationRefreshThread` that keeps the governance engine definitions up to date.

----
* Return to [module overview](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.