<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Bitol documents to catalog

This directory is the default location monitored by the Bitol Files Receiver integration connector.  Place
[Bitol](https://bitol.io) Open Data Contract Standard (ODCS) documents (`kind: DataContract`) and Open Data Product
Standard (ODPS) documents (`kind: DataProduct`) here as `.yaml`, `.yml` or `.json` files, in any directory structure.
The connector publishes each new or changed document to the Bitol listeners in the integration daemon, where the
cataloguers turn them into data sharing agreements and digital products.

A git checkout of a repository holding data contracts may be placed here, or attached to the connector as a catalog
target instead.

The Bitol support is described on the [main document website](https://egeria-project.org/features/digital-product-management/overview/).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
