<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# 0057 Integration Capabilities

A **SoftwareService** provides a well defined software component that can be
called by remote clients across the network.  They may offer
a request response or an event driven interface or both.

Typically software services implement specific business
functions, such as onboarding a new customer or taking an order
or sending an invoice.  Egeria offers specialized software services
related to the capture and management of open metadata.
These are shown as specialist types:

* **MetadataIntegrationService** describes an [Open Metadata Integration Service (OMIS)](../../../open-metadata-implementation/integration-services)
that runs in an [Integration Daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).

* **MetadataAccessService** describes an [Open Metadata Access Service (OMAS)](../../../open-metadata-implementation/integration-services)
that runs in a [Metadata Access Point](../../../open-metadata-implementation/admin-services/docs/concepts/metadata-access-point.md).

* **EngineHostingService** describes an [Open Metadata Engine Service (OMES)](../../../open-metadata-implementation/engine-services)
that runs in an [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).

* **UserViewService** describes an [Open Metadata View Service (OMVS)](../../../open-metadata-implementation/view-services)
that runs in a [View Server](../../../open-metadata-implementation/admin-services/docs/concepts/view-server.md).

![UML](0057-Integration-Capabilities.png#pagewidth)

Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.