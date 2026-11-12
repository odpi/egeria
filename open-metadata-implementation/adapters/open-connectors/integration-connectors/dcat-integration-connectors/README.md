<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# DCAT Integration Connectors

The W3C [Data Catalog Vocabulary (DCAT)](https://www.w3.org/TR/vocab-dcat-3/) is the standard that governments and
many other organizations use to describe the data assets that they make available: what each dataset is about, who
publishes it, how it is licensed and where it can be obtained.  The connectors in this module exchange these
descriptions with Egeria:

* **Receiving** - a description of a catalog, dataset, dataset series or data service that arrives in a file, on an
  Apache Kafka topic or through a REST API call is catalogued as assets in open metadata.
* **Publishing** - the assets that an organization chooses to make public are written out as DCAT descriptions, along
  with a DCAT catalog that lists them, ready to be harvested by a data portal.

The connectors work with the **JSON-LD** serialization of DCAT version 3.  Documents in Turtle or RDF/XML need to be
converted to JSON-LD before they are passed to Egeria.  Any arrangement of JSON-LD can be read (nested, flattened, with
prefixes or with full IRIs).  Egeria never retrieves a JSON-LD context from the network: a document should embed its
context, and one that refers to a remote context is read using the standard prefixes and names of the DCAT terms.
This is enough to read the `data.json` catalogs published by US government agencies.

## The connectors

| Connector | Purpose | Default location |
| --- | --- | --- |
| **DCAT Files Receiver** (`DcatFilesReceiverIntegrationConnector`) | Scans directories for `.jsonld` and `.json` files that describe DCAT resources and publishes new and changed documents to the DCAT listeners in the integration daemon. | `loading-bay/dcat`, plus the file folders attached as `directoryToMonitor` catalog targets |
| **DCAT Event Receiver** (`DcatEventReceiverIntegrationConnector`) | Publishes each event received from an Apache Kafka topic as a DCAT document to the DCAT listeners in the integration daemon. | The topics attached as `topicToMonitor` catalog targets |
| **DCAT Cataloguer** (`DcatCataloguerIntegrationConnector`) | Listens for DCAT documents and catalogs what they describe. | |
| **DCAT Publisher** (`DcatPublisherIntegrationConnector`) | Writes the DCAT description of each asset in the publishing catalog, and of the catalog itself. | `distribution-hub/dcat-catalog`, plus the file folders attached as `publishDirectory` catalog targets |

A DCAT document can also be sent to the cataloguer through the integration daemon's `publish-dcat-document` REST API,
and through the Asset Catalog OMVS (`/integration-daemons/{serverGUID}/dcat/publish-document`).  The Asset Catalog
OMVS also catalogs a document directly, without an integration daemon (`/assets/dcat/import-document`), and returns
the DCAT description of any asset (`/assets/{assetGUID}/dcat-document`).

The connectors are defined in the **DCAT Content Pack** (`DCATContentPack.omarchive`), in an integration group called
`Egeria:IntegrationGroup:DCAT`.

## How DCAT is catalogued

| DCAT | Open metadata |
| --- | --- |
| `dcat:Dataset` | `DataSet` asset |
| `dcat:Distribution` | `DataFile` asset (if it has a `dcat:downloadURL`) or `DataStore` asset (if it only has a `dcat:accessURL`), anchored to the data set and linked to it by a `DataSetContent` relationship.  A distribution that is removed from the dataset's description is deleted. |
| `dcat:DatasetSeries` | `DataSet` asset linked to its member data sets by `DataSetContent` relationships |
| `dcat:DataService` | `DeployedAPI` asset, with an `Endpoint` for each `dcat:endpointURL`, linked to the data sets it serves by `DataSetContent` relationships |
| `dcat:Catalog` | `CollectionFolder` whose members are the catalog's datasets, data services and nested catalogs |
| `dct:title`, `dct:description`, `dct:identifier`, `dcat:version`, `dcat:landingPage` | `displayName`, `description`, `identifier`, `versionIdentifier`, `url` |
| `dcat:keyword` | `SearchKeyword` |
| `dct:publisher` | `Organization` linked by a `ScopedBy` relationship |
| `dct:creator` | `authors` |
| `dcat:contactPoint` | `ContactDetails` linked by a `ContactThrough` relationship |
| `dct:license` | `LicenseType` linked by a `License` relationship |
| `dct:conformsTo`, `foaf:page` | `ExternalReference` linked by an `ExternalReferenceLink` relationship labelled `conformsTo` or `documentation` |
| Everything else | Additional properties whose names begin `dcat.` |

Each element is given a qualified name built from the IRI of the resource it represents (or the resource's
identifier or title if it has no IRI), so sending a description again updates the elements rather than creating new
ones.  Properties that are not part of DCAT, such as those added by the DCAT-AP and DCAT-US profiles, are preserved in
the `dcat.otherProperties` additional property and written out again when the asset is published.

## Publishing

The DCAT Publisher is told which assets to publish through a collection: the **publishing catalog**.  An asset is
published if it is a member of this collection, or of a collection folder nested in it.  By default, the publishing
catalog is the *DCAT Publishing Catalog* root collection (`Egeria::DCATPublishingCatalog`) that is supplied by the Core
Content Pack.  To publish an asset, add it to this collection; to withdraw it, remove it.

On each refresh, the publisher writes the following files to each of its destination directories.  A file is only
written when its content has changed.

* `catalog.jsonld` - the publishing catalog as a `dcat:Catalog`, along with the description of every published asset.
* `datasets/<name>.jsonld` - one file for each asset published as a `dcat:Dataset`.
* `data-services/<name>.jsonld` - one file for each asset published as a `dcat:DataService`.

A `DeployedAPI` asset is published as a data service.  A `DataSet` asset is published as a dataset whose distributions
are the assets that supply its data.  Any other type of asset is published as a dataset with a single distribution.
An asset that was catalogued from a DCAT description keeps its original IRI; any other asset is identified by
`urn:uuid:` followed by its unique identifier.

| Configuration property | Description |
| --- | --- |
| `publishingCatalog` | The qualified name (or display name) of the collection that lists the assets to publish.  The default is `Egeria::DCATPublishingCatalog`. |
| `defaultPublisher` | The name of the organization to show as the publisher of an asset that is not linked to an organization. |

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
