<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Published DCAT catalog

This directory is the default location where the DCAT Publisher integration connector writes descriptions of assets
in the W3C [Data Catalog Vocabulary (DCAT)](https://www.w3.org/TR/vocab-dcat-3/).  The files are JSON-LD:

* `catalog.jsonld` - the catalog (`dcat:Catalog`) along with the description of every published asset.  This is the
  file to give to a portal that harvests DCAT catalogs.
* `datasets/` - one file for each asset published as a `dcat:Dataset`, with its distributions.
* `data-services/` - one file for each asset published as a `dcat:DataService`.

An asset is published when it is a member of the *DCAT Publishing Catalog* collection (supplied by the Core Content
Pack), or of a collection folder nested in it.  When an asset is removed from the collection, its file is removed
from this directory and it disappears from the catalog.  A different collection can be used by setting the
connector's `publishingCatalog` configuration property, and additional destination directories can be attached to
the connector as catalog targets.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
