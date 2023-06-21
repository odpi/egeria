<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


The Asset Catalog OMAS client interface supports a REST API for requests and an event-based
interface for asynchronous integration. 


There is a single client called `AssetCatalog`. It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the server platform](https://egeria-project.org/concepts/platform-url-root/)
where Asset Catalog is running and its [server name](https://egeria-project.org/concepts/server-name/).

Here is a code example with the user ID and password specified:

```

AssetCatalogInterface client = new AssetCatalog(
                                        "cocoMDS1",
                                        "https://localhost:9444",
                                         "cocoUI",
                                         "cocoUIPassword");

```

This client is set up to call the `cocoMDS1` server running on the `https://localhost:9444`
OMAG Server Platform. The userID and password is for the application
where the client is running. The userID of the real end user is passed
on each request.

## Client operations

Once you have an instance of the client, you can use it to:
* Fetch asset's header, classifications and properties - `getAssetDetails()`

* Fetch asset's header, classifications, properties and relationships - `getAssetUniverse()`

* Fetch the relationships for a specific asset - `getAssetRelationships()`

* Fetch the classifications for a specific asset - `getClassificationsForAsset()`

* Returns the configured list of Asset Catalog's supported asset types - `getSupportedTypes()`

* Return a list of assets matching the search criteria without the full context `searchByType()`. Entity types are case
  in-sensitive, so proper values should be given in the request, otherwise an EntityNotKnownException will be thrown. If
  no type will be provided the operation will be made using the default search types. In order to determine de supported types
  call `getSupportedTypes()` before.

* Return the full context of an asset/glossary term based on its identifier. The response contains the list of the 
connections assigned to the asset - `getAssetContext()`

* Fetch relationship between entities details based on its unique identifier of the ends. Filtering based on the
relationship type is supported - `getRelationshipBetweenEntities()`

> Note: The equivalent REST interfaces are documented in the
[asset-catalog-server](../../../asset-catalog-server/README.md)
module.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
