<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Servers

The open metadata governance servers pull combinations of the Egeria services together to
support different integration patterns.

* **[server-chassis](server-chassis)** - the server chassis provides an "empty" server to host the open metadata
services.

* **[admin-services](admin-services)** - the admin services support the configuration of the open metadata server chassis.
This configuration determines which of the open metadata services are active.  It also supports
querying the runtime (operational) state of the open metadata components.

* **[discovery-engine](discovery-engine)** - the discovery engine provides the server
capability to run [Open Discovery Services](../frameworks/open-discovery-framework/docs/discovery-service.md)
on demand.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.