<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Content Packs

The Egeria content packs provide a means to distribute standard metadata to different organizations.
They are formatted as [Open Metadata Archives](https://egeria-project.org/concepts/open-metadata-archive)
and can be loaded using the Administration Services
[at server start up](https://egeria-project.org/guides/admin/servers/configuring-the-startup-archives)
or [while the server is running](https://egeria-project.org/guides/operations/adding-archive-to-running-server).

They are as follows:

* **CloudInformationModel.json** - glossary from the **Cloud Information Model**'s
[JSONLD formatted model](https://raw.githubusercontent.com/cloudinformationmodel/cloudinformationmodel/master/dist/model.jsonld).
The content covers basic commerce concepts such as Party, Product, Invoice and Shipping.
The cloud information project has been archived.  However, this is a useful starter set glossary,

The next four archives are built from the [coco-metadata-archives](https://github.com/odpi/egeria/tree/main/open-metadata-resources/open-metadata-samples/sample-metadata/coco-metadata-archives) module.
They supply metadata to support the [Coco Pharmaceuticals scenarios](https://egeria-project.org/practices/coco-pharmaceuticals/).

* **CocoBusinessSystemsArchive** provides a catalog of the business systems and the lineage between
  them and the load of their data into the data lake.  This archive simulates the type of metadata expected from
  an ETL tool suite.  It is intended for **cocoMDS5** in the open metadata labs but can be used in any server.

* **Coco Pharmaceuticals Organizations** - provides the profiles, user identifies and team of the featured
  personas of Coco Pharmaceuticals.

* **CocoClinicalTrialsTemplatesArchive** - provides the template assets used for onboarding weekly patient measurements during a clinical trial.

* **Governance Program Definitions** - provides the metadata to describe Coco Pharmaceuticals governance program.

* **CocoGovernanceEngineDefinitionsArchive** - provides the metadata to describe Coco Pharmaceuticals three governance engines:
  `AssetGovernance`, `AssetDiscovery` and `AssetQuality`.

* **Coco Pharmaceuticals Sustainability Initiative** provides the base definitions for Coco Pharmaceutical's
  sustainability initiative.

* **CocoTypesArchive** - provides additional types for Coco Pharmaceuticals.  These are `BiopsyScope` Enum, `BiopsyReport` Entity, `BiopsySupportingEvidence` Relationship and
  `ReviewedByClinicalTrials` Classification.

These next archives contain connector type and connector category definitions.

* **DataStoreConnectorTypes** - A subset of the *OpenConnectorsArchive* retained in case anyone is using it. 
  It should be considered deprecated and the OpenConnectorsArchive used instead.

* **OpenConnectorsArchive** - Connector type metadata for connecting to data sources and other third party technology.

This archive is a summary of all the open metadata type definitions provided by Egeria.  It is supplied for
external utilities since each OMAG server capable of being a cohort member will load these types on start up.

* **OpenMetadataTypes** - Archive of the open metadata types defined by Egeria.

This final four archives are for a demo showing 4 metadata access servers connected together in a single cohort.
The archives are each loaded into a different server.  
It is then possible to show how the cohort integrates metadata from different catalogs.
These archives are used in the *Development labs* which are part of the [Open Metadata Labs](https://egeria-project.org/education/open-metadata-labs/overview/).

* **SimpleAPICatalog** - API metadata typically found in an API catalog.

* **SimpleDataCatalog** - Data Source metadata typically found in an Data catalog.

* **SimpleEventCatalog** - Event metadata typically found in an API catalog.

* **SimpleGovernanceCatalog** - A glossary term linked to metadata elements in the API, Event, Data catalogs.

There is also **SimpleCatalog** that rolls up the content of the four catalogs into a single content pack for use in other scenarios.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.