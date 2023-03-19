<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# OCF Metadata Management

OCF metadata management provides common services for
[Open Metadata Access Services (OMASs)](../../access-services) that are supporting
the [Open Connector Framework (OCF)](../../frameworks/open-connector-framework).

* [Documentation](https://egeria-project.org/services/ocf-metadata-management)

## Internal design detail

There are five modules in its implementation:

* **[ocf-metadata-api](ocf-metadata-api)** - for common classes used both by the client and the server.
* **[ocf-metadata-client](ocf-metadata-client)** - for embedding in client-side OMASs
* **[ocf-metadata-handlers](ocf-metadata-handlers)** - server-side handlers for maintaining and retrieving metadata
stored in an open metadata repository using OCF beans.
* **[ocf-metadata-server](ocf-metadata-server)** - server-side REST Service for OCF metadata.
* **[ocf-metadata-spring](ocf-metadata-spring)** - Spring-based server-side REST endpoint for OCF metadata.


----
* Return to [Common Services](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.