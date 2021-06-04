<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Governance Daemon Connectors

The governance daemon connectors contain specialist connectors for the [governance
servers](../../../governance-servers) that make active use of open metadata.

* **[open-lineage-connectors](open-lineage-connectors)** provide plugins to the
[Open Lineage Server](../../../admin-services/docs/concepts/open-lineage-server.md)
that allow the [Open Lineage Services](../../../governance-servers/open-lineage-services)
to store lineage information.

## Deprecated connectors

The following connectors were written for governance servers that are now deprecated.
They will be removed as new versions are created for the
replacement [Integration Daemon](../../../admin-services/docs/concepts/integration-daemon.md)
and [Engine Host](../../../admin-services/docs/concepts/engine-host.md) OMAG Servers.

* **[data-platform-connectors](data-platform-connectors)** provide plugins to the
[Data Platform Server](../../../admin-services/docs/concepts/data-platform-server.md)
that allow the [Data Platform Services](../../../governance-servers/data-platform-services)
to connect with database servers and other data platforms in order to retrieve
information about the data assets they host.

   * These connectors will become Database Integration Connectors for the 
    [Database Integrator Open Metadata Integration Service (OMIS)](../../../integration-services/database-integrator)
    running in the Integration Daemon.

* **[security-officer-connectors](security-officer-connectors)** provide plugins to the
[Security Officer Server](../../../admin-services/docs/concepts/security-officer-server.md)
to allow the [Security Officer Services](../../../governance-servers/security-officer-services)
to set up security classifications on data assets.

   * These connectors will become [Governance Action Services](../../../frameworks/governance-action-framework/docs/governance-action-service.md)
     managed by the [Governance Action Open Metadata Engine Service (OMES)](../../../engine-services/governance-action)
     running in the Engine Host.

* **[security-sync-connectors](security-sync-connectors)** provide plugins to the
[Security Sync Server](../../../admin-services/docs/concepts/security-sync-server.md)
to synchronize classifications, user information and governance definitions
with security enforcement points.

   * These connectors will become Security Integration Connectors for the 
     [Security Integrator Open Metadata Integration Service (OMIS)](../../../integration-services/security-integrator)
     running in the Integration Daemon. 

* **[view-generator-connectors](view-generator-connectors)** provide plugins to the
[Virtualizer](../../../admin-services/docs/concepts/virtualizer.md)
that automatically configure data virtualization platforms from
open metadata.

  * These connectors will become Database Integration Connectors for the 
    [Database Integrator Open Metadata Integration Service (OMIS)](../../../integration-services/database-integrator)
    running in the Integration Daemon.

----
* Return to the [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
