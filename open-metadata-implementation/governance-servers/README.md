<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Governance Servers

The open metadata governance servers pull combinations of the Egeria services together to
support different integration patterns.

* **[server-chassis](server-chassis)** - the server chassis provides an "empty" server to host the open metadata
services.

* **[admin-services](admin-services)** - the admin services support the configuration of the open metadata server chassis.
This configuration determines which of the open metadata services are active.  It also supports
the querying the runtime (operational) state of the open metadata components.  