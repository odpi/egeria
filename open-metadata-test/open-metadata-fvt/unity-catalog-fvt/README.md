<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Unity Catalog Connectors Functional Verification Tests (unity-catalog-fvt)

This suite tests the
[unity-catalog-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors)
module — the Unity Catalog server and inside-catalog synchronizers and the Unity Catalog survey action
services — together with the **Unity Catalog content pack** that defines and drives them.

It does not call those connectors directly. It stands up the deployment they are designed to run in and then
drives them the way an operator would: through the **Automated Curation API**, by running the governance
action processes and governance action types the content pack ships, against two real Unity Catalog servers.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:unity-catalog-fvt:test -PrunUnityCatalogFvt
```

There is a second mode, which runs the same tests with no event bus at all:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:unity-catalog-fvt:test -PrunUnityCatalogFvtNoKafka
```

## What it stands up

Four servers on one in-process platform:

| Server | What it is | What it runs |
| --- | --- | --- |
| `unityCatalogFvtMetadataStore` | Metadata access store | PostgreSQL local repository; all access services with Kafka out topics; `OpenMetadataTypes`, `CoreContentPack`, `FilesContentPack` and `UnityCatalogContentPack` loaded at start-up |
| `unityCatalogFvtEngineHost` | Engine host | `UnityCatalogSurvey`, `UnityCatalogGovernance` and `Stewardship` governance engines |
| `unityCatalogFvtIntegrationDaemon` | Integration daemon | `UnityCatalogIntegrationGroup` - the Unity Catalog Server Synchronizer and the Inside Catalog Synchronizer |
| `unityCatalogFvtViewServer` | View server | Automated Curation OMVS |

The Files content pack is loaded because the Unity Catalog content pack is built on it. The **Stewardship**
engine is configured because the Unity Catalog "create and survey" process ends with a step, addressed to
Stewardship, that writes the survey report out as a markdown document.

## The Unity Catalog servers

Two Unity Catalog servers are needed, and they are treated differently:

| Setting | Default | Used for |
| --- | --- | --- |
| `unitycatalog.fvt.readonly.*` | `http://localhost:8087` | Catalogued, synchronized and surveyed. **Nothing is ever written to it**, so it may be in use by something else. It must hold the standard Unity Catalog sample: catalog `unity`, schema `default`, with the tables `marksheet`, `marksheet_uniform`, `numbers` and `user_countries`, the volumes `json_files` and `txt_files`, and the functions `lowercase` and `sum`. |
| `unitycatalog.fvt.writable.*` | `http://localhost:8187` | The suite creates a catalog of its own here - and deletes it again - so that it can change Unity Catalog underneath the synchronizer. Only catalogs whose names start with `unitycatalog.fvt.catalog.prefix` (`egeria_ucfvt`) are ever created or deleted. |

The synchronizers name everything they catalogue after the Unity Catalog server's network address rather
than after the server asset, so only one test class catalogues each server.

## What it tests

* **ContentPackFVT** - the Unity Catalog content pack loaded: its integration group and connectors, governance
  engines, governance action types and processes are all in the repository, and the governance servers are
  running them.
* **UnityCatalogServerCatalogFVT** - catalogues the read-only server end to end: the create-as-catalog-target
  process, the server synchronizer cataloguing the catalog and handing it to the inside-catalog synchronizer,
  and that synchronizer cataloguing the schema, tables, columns, volumes and functions - then the delete
  process. The server is catalogued with filters (`includeCatalogNames`, `excludeTableNames`,
  `excludeVolumeNames`, `excludeFunctionNames`, all given as short names) and the filtered elements are
  checked to be absent once their siblings have arrived.
* **UnityCatalogSurveyFVT** - the create-and-survey process for the server, then the catalog survey (as it
  comes, and with `excludeSchemaNames`) and the schema survey, checking the counts each records.
* **UnityCatalogSyncFVT** - builds a catalog on the writable server and checks that it is synchronized, that
  a table added in Unity Catalog arrives on the next refresh, and that a table dropped in Unity Catalog is
  removed.

## Clean-up

The repository's PostgreSQL schema is dropped and recreated at the start of every run, so a run never
inherits a previous run's elements or abandoned engine actions. Set `unitycatalog.fvt.clear.down=false` to
keep it for inspection. The fixture catalog on the writable server is deleted at the start and at the end of
`UnityCatalogSyncFVT`.

## Diagnosing a failure

The audit log of all four servers is written to `build/unity-catalog-fvt-data/logs/audit.log`. The
synchronizers' recommended action for each element is logged there as `OIF-CONNECTOR-0019`, and an element
the synchronizer could not classify as `OIF-CONNECTOR-0011`.

## Prerequisites

* Two Unity Catalog servers, as above.
* A PostgreSQL server for the repository - by default the `egeria-shared-postgres` container on port 5442.
* For the default mode, an Apache Kafka broker - by default `oak.local:9194`.
