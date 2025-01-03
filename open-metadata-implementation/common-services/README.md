<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Common Services (common-services)

This module provides common java functions to clients and the specialized services that
run in the OMAG Server.

* **[First-Failure Data Capture (FFDC) Services](ffdc-services)** - supports common exceptions
and error handling.  It can but used by clients and server-side services.

* **[Multi-Tenant Services](multi-tenant)** - supports the management of
[Open Metadata and Governance (OMAG) Server](https://egeria-project.org/concepts/omag-server) instances
running in an [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform).

* **[Metadata Observability](metadata-observability)** - enables the gathering of observability data.

* **[Metadata Security](metadata-security)** - supports authorization of access to OMAG Services and specific
    metadata instances.

* **[Generic Handlers](generic-handlers)** - supports the management of specific types of open metadata elements.
The generic handlers provide services to translate OMAS requests built around that service's private beans into calls
to the repository services (through the repository handler).

* **[Repository Handler](repository-handler)** - supports access to multiple related metadata instances from the
[Open Metadata Repository Services (OMRS)](../repository-services).  It manages the provenance checking and
translates [repository services (OMRS)](../repository-services) exceptions into 
[Open Connector Framework (OCF)](../frameworks/open-connector-framework) exceptions that are
used extensively across the [access services (OMASs)](../access-services).

----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.