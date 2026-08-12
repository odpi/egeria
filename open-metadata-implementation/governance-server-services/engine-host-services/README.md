<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


![Released](../../../images/egeria-content-status-released.png#pagewidth)
# Engine Host Services

The engine host services provide the base implementation of the
[Engine Host](https://egeria-project.org/concepts/engine-host) OMAG Server.

An Engine Host runs one or more of the [Open Metadata Engine Services (OMES)](../../engine-services)
(for example [Governance Action](../../engine-services/governance-action),
[Repository Governance](../../engine-services/repository-governance),
[Survey Action](../../engine-services/survey-action) and
[Watchdog Action](../../engine-services/watchdog-action)), each of which hosts one or more governance engines.
`GovernanceEngineHandler` manages the run-time state of a single governance engine, and `GovernanceEngineMap`
provides a thread-safe mapping of governance engine names to their handlers, allowing handlers to be added once
their governance engine's definition has been retrieved from the metadata access server - which may happen after
the Engine Host has started processing requests.

* [Documentation](https://egeria-project.org/services/engine-host-services)


----
* Return to the [Governance Servers](..).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.