<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Common Services

This module provides common services to clients and the specialized services that
run in the OMAG Server.

* **[First-failure Data Capture (FFDC) Services](ffdc-services)** - supports common exceptions
and error handling.  It can but used by clients and server-side services.

* **[Multi-Tenant Services](multi-tenant)** - supports the management of
[OMAG Server](../../../open-metadata-publication/website/omag-server) instances
running in an [OMAG Server Platform](../../../open-metadata-publication/website/omag-server).

In addition, there are shared metadata management functions for
server-side services that make use of the
[Open Connector Framework (OCF)](../../frameworks/open-connector-framework) beans on
their API.  These include:

* **[Asset Management](asset-management)** - support for managing Asset properties.
* **[Connection Management](connection-management)** - support for managing Connection properties.
* **[Schema Management](schema-management)** - support for managing Schema properties.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.