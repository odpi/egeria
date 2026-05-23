<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Asset Catalog OMVS

The Asset Catalog Open Metadata View Service (OMVS) provides a REST API to search for digital resources 
(assets) that are catalogued in open metadata. It also enables the caller to retrieve asset 
properties, schema, lineage and other related information.

## Key Features 
The Asset Catalog API supports the following key features:

* **Asset Search**: Locate assets using a search string or regular expression across the asset domain.
* **Asset Graphs**: Retrieve all elements anchored to an asset, including their relationships, to 
  visualize the asset's structure and context.
* **Lineage Graphs**: Explore the lineage of an asset to understand how data flows in and out of it, 
  including relationships from the asset itself and its anchored schema elements.
* **Supported Types**: Query the subtypes of assets supported by the ecosystem.

## Further information

* [Asset Catalog API Overview](https://egeria-project.org/services/omvs/asset-catalog/overview/)
* [Asset Concept](https://egeria-project.org/concepts/asset/)
* [Lineage Management](https://egeria-project.org/features/lineage-management/overview/)

Sample requests for the REST API can be found in `Egeria-api-asset-catalog.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.