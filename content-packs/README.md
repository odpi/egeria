<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Content Packs

The Egeria content packs provide a means to distribute standard metadata to different organizations.
They are formatted as [Open Metadata Archives](../open-metadata-resources/open-metadata-archives)
and can be loaded using the Administration Services
[at server start up](../open-metadata-implementation/admin-services/docs/user/configuring-the-startup-archives.md)
or [while the server is running](../open-metadata-implementation/admin-services/docs/user/operating-omag-server.md).

They are as follows:

* **CloudInformationModel.json** - extracted metadata from the [Cloud Information Model](https://cloudinformationmodel.org)'s
[JSONLD formatted model](https://raw.githubusercontent.com/cloudinformationmodel/cloudinformationmodel/master/dist/model.jsonld).

* **OpenConnectorsArchive** - Connector type metadata for connecting to data sources and other third party technology.

* **OpenMetadataTypes** - Archive of all of the open metadata types defined by Egeria.

* **SimpleAPICatalog** - API metadata typically found in an API catalog.

* **SimpleDataCatalog** - Data Source metadata typically found in an Data catalog.

* **SimpleEventCatalog** - Event metadata typically found in an API catalog.

* **SimpleGovernanceCatalog** - A glossary term linked to metadata elements in the API, Event, Data catalogs.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.