<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Open Lineage Connectors FVT (openlineage-fvt)

This is a Functional Verification Test (FVT) suite for the
[Open Lineage integration connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors).
It publishes Open Lineage run events for a small pipeline into a running integration daemon - both through the
daemon's REST API, as a processing engine sending events straight to Egeria would, and through an Apache Kafka
topic, as the Open Lineage proxy backend does - and checks what the connectors made of them.

The events are built with the Open Integration Framework's Open Lineage beans (see `OpenLineageEventFactory`),
so the suite also exercises the beans against the real connectors.

## Running it

Like the other FVTs, this suite is not part of the default build.  It needs a reachable PostgreSQL server and an
Apache Kafka broker, and it is only run deliberately:

```
./gradlew :open-metadata-test:open-metadata-fvt:openlineage-fvt:test -PrunOpenLineageFvt
```

Where those servers are and how long the suite waits for a connector are set in
[application.properties](src/test/resources/application.properties); any setting can be overridden with `-D`
(for example `-Dopenlineage.fvt.kafka.endpoint=localhost:9092`).  The defaults target the same shared PostgreSQL
(`localhost:5442`) and Kafka (`oak.local:9194`, the container's external listener) that the other suites use.

## What it stands up

| Server | What it is for |
|---|---|
| `openlineageFvtMetadataStore` | Metadata access store with a PostgreSQL repository and its access services publishing to Kafka.  Loads the open metadata types and the Core and Open Lineage content packs. |
| `openlineageFvtIntegrationDaemon` | Integration daemon running `Egeria:IntegrationGroup:OpenLineage` from the Open Lineage content pack: the Kafka listener, the cataloguer, the file publisher, the API publisher and the governance action publisher. |
| `openlineageFvtEngineHost` | Engine host running the Egeria Governance Engine, which hosts the three Open Lineage Lovelace services (also from the Open Lineage content pack). |

Both servers are started in an OMAG Server Platform that runs inside the test JVM.  The metadata collection id
is fixed so that the PostgreSQL schema can be reused from run to run; the elements a previous run created are
purged at start-up (every one has `openlineage-fvt` in its qualified name).

## What it checks

| Test class | Covers |
|---|---|
| `OpenLineageCataloguerFVT` | A START/COMPLETE pair becomes a `DeployedSoftwareComponent` with its description, SQL, ownership and resource name; the parent run facet becomes an owning `ProcessHierarchy`; the inputs and output become a `DataSet` (from the PostgreSQL Table template), a `DataFile` and a `DataSet` linked by `DataFlow`; the schema facet becomes columns; `RunMetrics` and `DataScope` are maintained; the data quality assertions become a survey report on the input; further runs accumulate; a failed run is counted; a RENAME renames the asset in place; a DROP removes it. |
| `OpenLineageFilePublisherFVT` | Every event reaching the daemon is written, unchanged, to the file-based log store under `logs/openlineage/{namespace}/{job}/`. |
| `OpenLineageKafkaListenerFVT` | A topic catalogued from the core content pack's Kafka topic template and attached to the Kafka listener as a catalog target delivers the events published to it to the cataloguer. |
| `GovernanceActionPublisherFVT` | An engine action whose action targets are a data file with an `s3://` path name and a data set (named `destinationDataSet`) with no OpenLineage identity is published by the governance action publisher with the file as an input dataset (`s3://{bucket}` + object key) and the data set as an output (`egeria` + qualified name), with its request parameters, action targets and guards in the `executionParameters` facet and its information supply chain in the `egeria_governanceAction` facet; the cataloguer links the governance action's process to the data set itself, rather than a new asset, with a DataFlow tagged with the information supply chain. |
| `OpenLineageCorrelationFVT` | A run reading and writing tables catalogued the way Egeria's Postgres templates and JDBC integration connector catalogue them (database, schema, `RelationalTable`s and columns), with column lineage and an `egeria_informationSupplyChain` facet, attaches its lineage to the tables and to their schema asset, maps the columns and tables with `DataMapping`, creates no new assets, links a duplicate catalogue entry of the input table as a peer duplicate, and links a tabular data set collection and a tabular data set above the schema and table with `DataSetContent` - every relationship tagged with the information supply chain. |
| `OpenLineageTemplateFVT` | Datasets and jobs that are not catalogued are created from the catalog templates for their technology (checked through the SourcedFrom link to the template): a `snowflake://` table as a DataSet with the Snowflake Table technology type, an `s3://` object as a DataFile with the Amazon S3 Object technology type, an Apache Airflow task (from its `jobType` facet) with its parent job as an Apache Airflow DAG; and a dataset from an unknown technology as a generic DataStore. |
| `OpenLineageLovelaceServicesFVT` | Three regularly spaced runs are published and reach the file-based log store; the three Lovelace analysis services are then started as engine actions against that log store and the run profile (an HOURLY schedule inferred from the run series), the data scope (an APPENDED write pattern) and the data quality summary (a survey report with a pass rate per dimension) they produce are checked. |

The API publisher (which needs a Marquez server) is started by the daemon but not driven by this suite.

## Reading a failure

The tests wait for the connectors and the engine actions, so most failures are timeouts naming what did not
happen.  The audit log of every server is written to `build/openlineage-fvt-data/logs/audit.log` (Gradle buffers the console until the
task ends, so the file is the place to look while a test is waiting).  The events the file publisher wrote are
under `logs/openlineage/` in this module's directory.

----
Return to [open-metadata-fvt](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
