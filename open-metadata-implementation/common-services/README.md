<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Common Services (common-services)

This module provides common services to clients and the specialized services that
run in the OMAG Server.

* **[First-failure Data Capture (FFDC) Services](ffdc-services)** - supports common exceptions
and error handling.  It can but used by clients and server-side services.

* **[Multi-Tenant Services](multi-tenant)** - supports the management of
[Open Metadata and Governance (OMAG) Server](../admin-services/docs/concepts/omag-server.md) instances
running in an [OMAG Server Platform](../admin-services/docs/concepts/omag-server-platform.md).

* **[Metadata Security](metadata-security)** - supports authorization of access to OMAG Services and specific
metadata instances.

* **[Repository Handler](repository-handler)** - supports access to multiple related metadata instances from the
[Open Metadata Repository Services (OMRS)](../repository-services).

In addition, there is a shared metadata management module for
server-side services that make use of the beans defined in the various [frameworks](../frameworks) that underpin Egeria.

* **[OCF Metadata Management](ocf-metadata-management)** - managing metadata about assets, connections and all of the
different types of metadata defined in the asset properties.

----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.