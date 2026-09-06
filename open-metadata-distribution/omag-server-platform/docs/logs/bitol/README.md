<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Bitol document store

This directory is the default location where the Bitol File Store integration connector writes the
[Bitol](https://bitol.io) documents published in the integration daemon.  Each document is stored as
`{kind}/{id}/{version}.yaml` (or `.json` when the document arrived as JSON), so the directory can be committed to a
git repository as it stands:

* `DataProduct/` - Open Data Product Standard (ODPS) documents, one directory per product identifier.
* `DataContract/` - Open Data Contract Standard (ODCS) documents, one directory per contract identifier.
* `{kind}/unparsed/` - documents that were received but could not be parsed.

Documents arrive here from the Bitol Files Receiver (monitoring `loading-bay/bitol` by default), from the Bitol Event
Receiver (Apache Kafka topics), from the integration daemon's `publish-data-contract` and `publish-data-product` REST
calls, and from the Bitol Document Publisher, which generates documents for the digital products and data sharing
agreements defined in open metadata.

The Bitol support is described on the [main document website](https://egeria-project.org/features/digital-product-management/overview/).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
