<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Published DCAT catalog

This directory is the default location where the DCAT Publisher integration connector writes descriptions of assets
in the W3C [Data Catalog Vocabulary (DCAT)](https://www.w3.org/TR/vocab-dcat-3/).  It is the network address of the
endpoint in the connector's connection.

Each collection attached to the connector as a catalog target is published as a DCAT catalog in its own
subdirectory, named after the collection and the first eight characters of its unique identifier - for example
`DCAT-Publishing-Catalog-9b07a6ef` for the *DCAT Publishing Catalog* collection that the DCAT Content Pack supplies
and attaches.  The files in each subdirectory are JSON-LD:

* `catalog.jsonld` - the catalog (`dcat:Catalog`) along with the description of every published asset.  This is the
  file to give to a portal that harvests DCAT catalogs.
* `datasets/` - one file for each asset published as a `dcat:Dataset`, with its distributions.
* `data-services/` - one file for each asset published as a `dcat:DataService`.

An asset is published when it is a member of the collection, or of a collection folder nested in it.  When an asset
is removed from the collection, its file is removed and it disappears from the catalog.  When a collection is
detached from the connector, its subdirectory is removed.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
