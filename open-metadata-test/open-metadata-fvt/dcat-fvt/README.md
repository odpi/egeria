<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# DCAT Connectors Functional Verification Tests (dcat-fvt)

This suite tests the **DCAT connectors** - the integration connectors that receive, catalog and publish descriptions of
data assets in the W3C [Data Catalog Vocabulary (DCAT)](https://www.w3.org/TR/vocab-dcat-3/) - together with the
**DCAT content pack** that defines them and the **DCAT Publishing Catalog** collection from the Core content pack.

Like [bitol-fvt](../bitol-fvt), it does not call the connectors directly.  It stands up the deployment they are
designed to run in and drives them the way an operator would.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:dcat-fvt:test -PrunDcatFvt
```

This needs a PostgreSQL server and an Apache Kafka broker.  Both are named in
`src/test/resources/application.properties`; the defaults are the `egeria-shared-postgres` (port 5442) and
`egeria-shared-kafka` (external listener `localhost:9194`) containers used elsewhere in this project.  The suite's
repository schema, `repository_dcatFvtMetadataStore`, is dropped at the start of every run so the repository always
starts empty.

A second mode carries the out topics over the in-memory topic connector, so that only PostgreSQL is needed.  The
Kafka receiver test is skipped in this mode.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:dcat-fvt:test -PrunDcatFvtInMemory
```

## What it stands up

Two servers on one in-process platform, on a port allocated at run time:

| Server | What it is | What it runs |
| --- | --- | --- |
| `dcatFvtMetadataStore` | Metadata access store | PostgreSQL local repository; `OpenMetadataTypes`, `CoreContentPack` and `DCATContentPack` loaded at start-up |
| `dcatFvtIntegrationDaemon` | Integration daemon | `DCATIntegrationGroup`, where the four DCAT connectors run |

The connectors' default endpoints (`loading-bay/dcat` for the files receiver and `distribution-hub/dcat-catalog` for
the publisher) are relative paths, resolved against this module's directory because that is the working directory of
the test JVM.  Both directories are emptied at the start of each run and are ignored by git.

## What it tests

* **[ContentPackFVT](src/test/java/org/odpi/openmetadata/dcatfvt/ContentPackFVT.java)** - the content pack loaded and
  the integration daemon started every connector it defines.  The expected contents come from the definitions in
  `core-content-pack`, so adding a connector to the pack extends the test without editing it.

* **[ReceiversFVT](src/test/java/org/odpi/openmetadata/dcatfvt/ReceiversFVT.java)** - a dataset description dropped
  into the loading bay becomes a `DataSet` with a `DataFile` and a `DataStore` for its distributions and an
  `Organization` for its publisher; and a catalog sent to the integration daemon's `publish-dcat-document` endpoint
  becomes a `CollectionFolder` holding a dataset series, its member data sets and the `DeployedAPI` that serves them.

* **[KafkaReceiverFVT](src/test/java/org/odpi/openmetadata/dcatfvt/KafkaReceiverFVT.java)** - a Kafka topic is
  catalogued from the Core content pack's template and attached to the event receiver as a catalog target; a dataset
  description produced onto the topic is catalogued.

* **[PublisherFVT](src/test/java/org/odpi/openmetadata/dcatfvt/PublisherFVT.java)** - a data set catalogued from a
  DCAT description is added to the DCAT Publishing Catalog and a data set created directly in open metadata is added
  to a collection folder nested in it.  The publisher writes `catalog.jsonld` describing both, and a file for each
  in `datasets`; a second refresh leaves the files alone; and removing a data set from the catalog removes its file
  and its entry in the catalog.

The mapping between DCAT and open metadata is also exercised, in both directions, through the Asset Catalog OMVS by
`AssetCatalogFVT` in [view-services-fvt](../view-services-fvt).

The integration daemon's audit log is written to `build/dcat-fvt-data/logs/audit.log`.

----
Return to [open-metadata-fvt](..)
