<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# GAF Metadata Management server-side implementation

`GAFOperationalServices` initializes the REST services that support the [Open Governance Framework (OGF)](../../../frameworks/open-governance-framework)
open metadata store calls.  The server-side support is organized as follows:

* `GovernanceConfigRESTServices` and `GovernanceEngineConfigurationHandler` implement the configuration of
  governance engines and governance services.
* `IntegrationGroupConfigurationHandler` implements the configuration of integration groups and their
  integration connectors.
* `OpenGovernanceRESTServices` implements the services used by a governance engine to manage requests to
  execute governance services and engine actions.
* `OpenGovernanceOMRSTopicListener` and `OpenGovernanceOutTopicPublisher` manage the events published on the
  service's out topic to notify listeners (such as watchdog governance services) of relevant changes to open
  metadata.
* The converter classes translate open metadata repository instances into the beans returned through the API,
  and `GAFServicesInstance` caches the server's runtime state for use by these handlers.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
