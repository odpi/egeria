<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# DCAT documents to catalog

This directory is the default location monitored by the DCAT Files Receiver integration connector.  Place
descriptions of catalogs, datasets and data services written in the W3C
[Data Catalog Vocabulary (DCAT)](https://www.w3.org/TR/vocab-dcat-3/) here as JSON-LD files (`.jsonld` or `.json`),
in any directory structure.  The connector publishes each new or changed document to the DCAT listeners in the
integration daemon, where the DCAT Cataloguer turns each dataset into a data set asset with an asset for each of its
distributions, each data service into a deployed API and each catalog into a collection folder.

The documents must be JSON-LD.  A catalog exported from a data portal in Turtle or RDF/XML needs to be converted to
JSON-LD first.  Egeria does not retrieve a JSON-LD context from the network, so a document should either embed its
context or use the standard names for the DCAT terms.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
