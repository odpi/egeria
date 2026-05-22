<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Runtime Manager OMVS

The Runtime Manager OMVS enables the caller to retrieve configuration and status from platforms and servers.
It is also used to manage the lifecycle of servers, their registration in cohorts and the loading
of open metadata archives.

Key features of the service include:

* **Platform Management**: Retrieving configuration and status (reports) from OMAG Server Platforms.
* **Server Management**: Retrieving configuration and status (reports) from OMAG Servers.
* **Server Lifecycle Management**: Activating and shutting down OMAG Servers using stored configuration.
* **Cohort Management**: Managing open metadata repository cohorts and their members, including connecting, disconnecting, and unregistering servers.
* **Archive Management**: Loading open metadata archives into running repositories.
* **Engine and Integration Management**: Refreshing configuration for governance engines and integration daemons, and managing integration connectors (configuration properties, endpoints, and connections).
* **Open Lineage Support**: Publishing open lineage events to the integration service.

Sample REST API requests can be found in the
[Egeria-api-runtime-manager.http](Egeria-api-runtime-manager.http) file.

## Further information

* [Runtime Manager OMVS Overview](https://egeria-project.org/services/omvs/runtime-manager/overview/)
* [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform/)
* [OMAG Server](https://egeria-project.org/concepts/omag-server/)
* [Cohort](https://egeria-project.org/concepts/cohort/)
* [Open Metadata Archive](https://egeria-project.org/concepts/open-metadata-archive/)
* [Governance Engine](https://egeria-project.org/concepts/governance-engine/)
* [Integration Connector](https://egeria-project.org/concepts/integration-connector/)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.