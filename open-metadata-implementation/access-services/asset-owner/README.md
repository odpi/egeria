<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

# Asset Owner Open Metadata Access Service (OMAS)

The Asset Owner OMAS provides APIs and notifications for tools and applications supporting
the work of Asset Owners in protecting and enhancing their assets.

Every asset has an owner property.  This is the userId of the owner.  The owner is responsible
for the correct classification of assets and the assignment of connection(s) to the asset.
The owner typically links the asset (or more likely the asset's schema elements) to glossary
terms and declares the asset's associated licenses and certifications.

Asset owners can maintain a note log for each of their assets.  They can view the feedback and
respond to it.  The Asset Owner OMAS generates notifications about new feedback linked to an
asset.

The module structure for the Asset Owner OMAS is as follows:

* [asset-owner-client](asset-owner-client) supports the client library.
* [asset-owner-api](asset-owner-api) supports the common Java classes that are used both by the client and the server.
* [asset-owner-server](asset-owner-server) supports in implementation of the access service and its related event management.
* [asset-owner-spring](asset-owner-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

