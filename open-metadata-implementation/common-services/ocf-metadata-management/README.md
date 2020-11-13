<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

# OCF Metadata Management

OCF metadata management provides common services for
[Open Metadata Access Services (OMASs)](../../access-services) that are supporting
the [Open Connector Framework (OCF)](../../frameworks/open-connector-framework).

Connectors defined in the OCF are client-side Java objects.  They support a method called
`getConnectedAssetProperties`.  If the connector is created by one of the OMAS clients,
this method is configured to retrieve metadata from the same open metadata repository as
this OMAS.  The primary purpose of the OCF Metadata Management module is to support the
REST APIs behind `getConnectedAssetProperties`.  It has also grown to providing common
handlers for OMASs working with similar metadata (such as the [Asset Owner OMAS](../../access-services/asset-owner)).
In particular, it manages the filtering of [Assets](../../access-services/docs/concepts/assets)
based on their [Governance Zones](../../access-services/docs/concepts/governance-zones) and the implementation
of specific [metadata security methods](../metadata-security) related to assets.

## Internal design detail

There are five modules in its implementation:

* **[ocf-metadata-api](ocf-metadata-api)** - for common classes used both by the client and the server.
* **[ocf-metadata-client](ocf-metadata-client)** - for embedding in client-side OMASs
* **[ocf-metadata-handlers](ocf-metadata-handlers)** - server-side handlers for maintaining and retrieving metadata
stored in an open metadata repository using OCF beans.
* **[ocf-metadata-server](ocf-metadata-server)** - server-side REST Service for OCF metadata.
* **[ocf-metadata-spring](ocf-metadata-spring)** - Spring-based server-side REST endpoint for OCF metadata.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.