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
| `OpenLineageCataloguerFVT` | A START/COMPLETE pair becomes a `DeployedSoftwareComponent` with its description, SQL, ownership and resource name; the parent run facet becomes an owning `ProcessHierarchy`; the inputs and output become a `TabularDataSet`, a `DataFile` and a `TabularDataSet` linked by `DataFlow`; the schema facet becomes columns; `RunMetrics` and `DataScope` are maintained; the data quality assertions become a survey report on the input; further runs accumulate; a failed run is counted; a RENAME renames the asset in place; a DROP removes it. |
| `OpenLineageFilePublisherFVT` | Every event reaching the daemon is written, unchanged, to the file-based log store under `logs/openlineage/{namespace}/{job}/`. |
| `OpenLineageKafkaListenerFVT` | A topic catalogued from the core content pack's Kafka topic template and attached to the Kafka listener as a catalog target delivers the events published to it to the cataloguer. |
| `OpenLineageLovelaceServicesFVT` | Three regularly spaced runs are published and reach the file-based log store; the three Lovelace analysis services are then started as engine actions against that log store and the run profile (an HOURLY schedule inferred from the run series), the data scope (an APPENDED write pattern) and the data quality summary (a survey report with a pass rate per dimension) they produce are checked. |

The API publisher (which needs a Marquez server) is started by the daemon but not driven by this suite, and the
governance action publisher's events (from the engine actions the Lovelace test starts) are not asserted on.

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
