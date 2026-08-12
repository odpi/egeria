<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Unity Catalog Connectors

[Unity Catalog](https://www.unitycatalog.io/) is a data manager catalog that governs access to data.  It is typically managing data from data lakes and lakehouses where much of the data is in a parquet format, with a table abstraction over the top.  Unity catalog also supports folders of files (called Volumes) and functions.  Unity catalog is able to provide access to the data in its catalogs, and run the functions.
The connectors in this module support interaction with the open source unity catalog.  

See [https://github.com/unitycatalog/unitycatalog](https://github.com/unitycatalog/unitycatalog).

Unity Catalog is an operational metadata catalog that supports controlled access to data managed through a data 
platform.  It provides metadata to describe the organization of the data.  This can be used to overlay a security model to control access.

These connectors form the core implementations for Egeria's [*Leveraging your Unity Catalog Estate* solution](https://egeria-project.org/egeria-solutions/leveraging-unity-catalog/overview/).

## OSS Unity Catalog Resource Connector

The *OSS Unity Catalog resource connector* provides a client wrapper to the Open Source version of Unity Catalog.  It aims to provide a buffer to the integration connector and the survey service to protect them from changes to the unity catalog API (which are expected).

![Connector Operation](docs/resource-connector.svg)

## OSS Unity Catalog Server Survey Service

The Server Survey Service extracts information about the catalogs, schemas, tables, functions and volumes managed in a specific catalog within an OSS Unity Catalog Server.

![Connector Operation](docs/server-survey-service.svg)

## OSS Unity Catalog Inside Catalog Survey Service

The Catalog Survey Service extracts information about the schemas, tables, functions and volumes managed in a specific catalog within an OSS Unity Catalog Server.

![Connector Operation](docs/catalog-survey-service.svg)


## OSS Unity Catalog Schema Survey Service

The Schema Survey Service extracts information about the tables, functions and volumes managed in a specific schema within an OSS Unity Catalog Server.

![Connector Operation](docs/schema-survey-service.svg)

# OSS Unity Catalog (UC) Server Synchronizing Connector

The *Unity Catalog OSS Synchronization Connector* is an [integration connector](https://egeria-project.org/concepts/integration-connector) that exchanges metadata between the open-source version of Unity Catalog and the Open Metadata Ecosystem.

Its work is scoped by the metadata collection that represents Unity Catalog.  This is identified by the *metadataSourceQualifiedName* property in the connector's configuration.
It performs two passes each time it is called.

![Connector Operation](docs/sync-server-connector.svg)

## OSS Unity Catalog (UC) Inside a Catalog Synchronizing Connector

The Unity Catalog Inside Catalog Synchronizer Connector is an [integration connector](https://egeria-project.org/concepts/integration-connector) that synchronizes metadata between catalogs in Unity Catalog and the open metadata ecosystem.  If an element originates in Unity Catalog, the copy in open metadata is kept in sync with the unity catalog version and vice versa.

![Connector Operation](docs/sync-catalog-connector.svg)

----
Return to [data-manager-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

