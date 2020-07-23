<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


The Asset Catalog OMAS client interface supports the 


There is a single client called `AssetCatalog`.  It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the server platform](../../../../docs/concepts/client-server/omas-server-url-root.md)
where the Data Engine OMAS is running and its [server name](../../../../docs/concepts/client-server/omas-server-name.md).

Here is a code example with the user id and password specified:

```

AssetCatalogInterface client = new AssetCatalog(
                                        "cocoMDS1",
                                        "https://localhost:9444",
                                         "cocoUI",
                                         "cocoUIPassword");

```

This client is set up to call the `cocoMDS1` server running on the `https://localhost:9444`
OMAG Server Platform.  The userId and password is for the application
where the client is running.  The userId of the real end user is passed
on each request.

## Client operations

Once you have an instance of the client, you can use it to:
* Fetch asset's header, classification and properties - [getAssetDetails]()

* Fetch asset's header, classification, properties and relationships - [getAssetUniverse]()

* Fetch the relationships for a specific asset - [getAssetRelationships]()

* Fetch the classification for a specific asset - [getClassificationsForAsset]()

* Returns a sub-graph of intermediate assets that connected two assets - [getLinkingAssets]()

* Return a sub-graph of relationships that connect two assets - [getLinkingRelationships]()


* Returns the sub-graph that represents the list of assets that in neighborhood of the given asset - [getAssetsFromNeighborhood]()

* Return a list of assets matching the search criteria without the full context [searchByType]()

* Return the full context of an asset/glossary term based on its identifier. The response contains the list of the connections assigned to the asset - [getAssetContext]()

* Fetch relationship between entities details based on its unique identifier of the ends. Filtering based on the relationship type is supported - [getRelationshipBetweenEntities]()

> Note: The equivalent REST interfaces are documented in the
[asset-catalog-server](../../../asset-catalog-server/README.md)
module.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
