<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Governance Daemon Connectors

The governance daemon connectors contain connectors for the governance
daemon servers that monitor activity or synchronize metadata and configuration
asynchronously between different tools.

* **[data-platform-connectors](data-platform-connectors)** provide plugins to the
[Data Platform Server](../../../admin-services/docs/concepts/data-platform-server.md)
that allow the [Data Platform Services](../../../governance-servers/data-platform-services)
to connect with database servers and other data platforms in order to retrieve
information about the data assets they host.

* **[open-lineage-connectors](open-lineage-connectors)** provide plugins to the
[Open Lineage Server](../../../admin-services/docs/concepts/open-lineage-server.md)
that allow the [Open Lineage Services](../../../governance-servers/open-lineage-services)
to store lineage information.

* **[security-officer-connectors](security-officer-connectors)** provide plugins to the
[Security Officer Server](../../../admin-services/docs/concepts/security-officer-server.md)
to allow the [Security Officer Services](../../../governance-servers/security-officer-services)
to set up security classifications on data assets.

* **[security-sync-connectors](security-sync-connectors)** provide plugins to the
[Security Sync Server](../../../admin-services/docs/concepts/security-sync-server.md)
to synchronize classifications, user information and governance definitions
with security enforcement points.

* **[view-generator-connectors](view-generator-connectors)** provide plugins to the
[Virtualizer](../../../admin-services/docs/concepts/virtualizer.md)
that automatically configure data virtualization platforms from
open metadata.


Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.