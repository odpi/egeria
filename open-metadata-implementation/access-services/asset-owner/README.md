<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Asset Owner Open Metadata Access Service (OMAS)

The Asset Owner OMAS provides APIs and notifications for tools and applications supporting
the work of Asset Owners in protecting and enhancing their assets.

* [User Documentation](https://egeria-project.org/services/omas/asset-owner/overview)

# Design Information

The module structure for the Asset Owner OMAS follows the standard pattern as follows:

* [asset-owner-client](asset-owner-client) supports the client library.
* [asset-owner-api](asset-owner-api) supports the common Java classes that are used both by the client and the server.
* [asset-owner-server](asset-owner-server) supports in implementation of the access service and its related event management.
* [asset-owner-spring](asset-owner-spring) supports
  the REST API using the [Spring](https://egeria-project.org/guides/contributor/runtime/#spring) libraries.

It makes use of the [ocf-metadata-management](../../framework-services/ocf-metadata-management)
for its server side interaction with the metadata repository and so the
primary function of the Asset Owner OMAS is to manage the
APIs for the Asset Owner and translate between them and
the [Open Connector Framework (OCF)](../../frameworks/open-connector-framework) oriented interfaces
of ocf-metadata-management.

----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

