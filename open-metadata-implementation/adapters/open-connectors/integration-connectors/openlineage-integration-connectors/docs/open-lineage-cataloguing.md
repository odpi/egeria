<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Cataloguing OpenLineage events in open metadata

This note describes how the Open Lineage Cataloguer integration connector maps OpenLineage events
into open metadata, and assesses which information is best captured as each event arrives versus
derived later from a historical OpenLineage log store.

## Spec level

The beans in the Open Integration Framework (`org.odpi.openmetadata.frameworks.integration.openlineage`)
follow the OpenLineage core spec **2-0-2** (the level shipped with OpenLineage 1.53) and the current
versions of every standard facet in `spec/facets`:

| Kind | Facet key | Bean | Spec version |
|------|-----------|------|--------------|
| Run | `parent` (with `root`) | `OpenLineageParentRunFacet` | 1-2-0 |
| Run | `nominalTime` | `OpenLineageNominalTimeRunFacet` | 1-0-1 |
| Run | `environmentVariables` | `OpenLineageEnvironmentVariablesRunFacet` | 1-0-0 |
| Run | `errorMessage` | `OpenLineageErrorMessageRunFacet` | 1-0-1 |
| Run | `executionParameters` | `OpenLineageExecutionParametersRunFacet` | 1-0-0 |
| Run | `externalQuery` | `OpenLineageExternalQueryRunFacet` | 1-0-2 |
| Run | `extractionError` | `OpenLineageExtractionErrorRunFacet` | 1-1-2 |
| Run | `jobDependencies` | `OpenLineageJobDependenciesRunFacet` | 1-0-1 |
| Run | `processing_engine` | `OpenLineageProcessingEngineRunFacet` | 1-1-1 |
| Run | `tags` | `OpenLineageTagsRunFacet` | 1-0-0 |
| Run | `test` | `OpenLineageTestRunFacet` | 1-0-1 |
| Job | `documentation` | `OpenLineageDocumentationJobFacet` | 1-1-0 |
| Job | `sql` | `OpenLineageSQLJobFacet` | 1-1-0 |
| Job | `sourceCodeLocation` | `OpenLineageSourceCodeLocationJobFacet` | 1-1-0 |
| Job | `sourceCode` | `OpenLineageSourceCodeJobFacet` | 1-0-1 |
| Job | `jobType` | `OpenLineageJobTypeJobFacet` | 2-0-4 |
| Job | `ownership` | `OpenLineageOwnershipJobFacet` | 1-0-1 |
| Job | `tags` | `OpenLineageTagsJobFacet` | 1-0-0 |
| Job | `lineage` | `OpenLineageLineageJobFacet` | 1-0-0 |
| Dataset | `documentation` | `OpenLineageDocumentationDataSetFacet` | 1-1-0 |
| Dataset | `dataSource` | `OpenLineageDataSourceDataSetFacet` | 1-0-1 |
| Dataset | `schema` (nested fields, ordinal position) | `OpenLineageSchemaDataSetFacet` | 1-2-0 |
| Dataset | `catalog` | `OpenLineageCatalogDataSetFacet` | 1-1-0 |
| Dataset | `columnLineage` | `OpenLineageColumnLineageDataSetFacet` | 1-2-0 |
| Dataset | `datasetType` | `OpenLineageDataSetTypeDataSetFacet` | 1-0-1 |
| Dataset | `version` | `OpenLineageDataSetVersionDataSetFacet` | 1-0-1 |
| Dataset | `lifecycleStateChange` | `OpenLineageLifecycleStateChangeDataSetFacet` | 1-0-1 |
| Dataset | `ownership` | `OpenLineageOwnershipDataSetFacet` | 1-0-1 |
| Dataset | `storage` | `OpenLineageStorageDataSetFacet` | 1-0-1 |
| Dataset | `symlinks` | `OpenLineageSymlinksDataSetFacet` | 1-0-1 |
| Dataset | `tags` | `OpenLineageTagsDataSetFacet` | 1-0-0 |
| Dataset | `hierarchy` | `OpenLineageHierarchyDataSetFacet` | 1-0-0 |
| Dataset | `lineage` | `OpenLineageLineageDataSetFacet` | 1-0-0 |
| Dataset | `dataQualityMetrics` | `OpenLineageDataQualityMetricsDataSetFacet` | 1-0-0 |
| Input | `dataQualityAssertions` | `OpenLineageDataQualityAssertionsInputDataSetFacet` | 1-1-0 |
| Input | `dataQualityMetrics` | `OpenLineageDataQualityMetricsInputDataSetFacet` | 1-0-3 |
| Input | `inputStatistics` | `OpenLineageInputStatisticsInputDataSetFacet` | 1-0-0 |
| Input | `subset` | `OpenLineageInputSubsetInputDataSetFacet` | 1-0-0 |
| Output | `outputStatistics` | `OpenLineageOutputStatisticsOutputDataSetFacet` | 1-0-2 |
| Output | `subset` | `OpenLineageOutputSubsetOutputDataSetFacet` | 1-0-0 |
| Run (registry: gcp/composer) | `gcp_composer_run` | `OpenLineageGcpComposerRunFacet` | 1-0-0 |
| Run (registry: gcp/dataproc) | `gcp_dataproc` | `OpenLineageGcpDataprocRunFacet` | 1-0-0 |
| Job (registry: gcp/composer) | `gcp_composer_job` | `OpenLineageGcpComposerJobFacet` | 1-0-0 |
| Job (registry: gcp/lineage) | `gcp_lineage` | `OpenLineageGcpLineageJobFacet` | 1-0-0 |
| Input (registry: iceberg) | `icebergScanReport` | `OpenLineageIcebergScanReportInputDataSetFacet` | 1-0-1 |
| Output (registry: iceberg) | `icebergCommitReport` | `OpenLineageIcebergCommitReportOutputDataSetFacet` | 1-0-2 |

The last six rows are the custom facets registered in the OpenLineage facet registry (`spec/registry`) by Google Cloud
and Apache Iceberg.  Facets that are not modelled (unregistered custom facets from integrations such as `airflow`,
`spark` or `dbt_model`)
are retained in the `additionalProperties` of the facet containers as generic facets, and unknown
properties inside a modelled facet are retained in the facet's `additionalProperties`, so an event
survives a round trip through the beans unchanged.  The `_deleted` marker on job and dataset facets is
supported.  The spec's `JobEvent` and `DatasetEvent` (`OpenLineageJobEvent`, `OpenLineageDataSetEvent`)
are parsed and delivered through new default methods on `OpenLineageEventListener`.

## Mapping to open metadata

| OpenLineage | Open metadata | Notes |
|-------------|---------------|-------|
| Job (namespace, name) | `DeployedSoftwareComponent` (a `Process`) | Matched on `namespacePath` + `resourceName` (see below); created as `DeployedSoftwareComponent::{namespace}::{name}`, from the catalog template chosen by the `jobType` facet where there is one.  The pre-existing `OpenLineageJob:{name}` naming is still recognised. |
| `documentation` job facet | `description` | Only set when the process has no description (stewards' edits are preserved). |
| `sql` job facet | `formula` / `formulaType` (`SQL:{dialect}`) | |
| `sourceCode` job facet | `implementationLanguage` | The source text itself is not stored. |
| `jobType` job facet | `deployedImplementationType` (`{integration} {jobType}`) plus additional properties | |
| `sourceCodeLocation`, `tags` job facets | additional properties (`sourceCodeLocation.*`, `tag:{key}`) | |
| `ownership` job/dataset facet | `Ownership` classification (first owner) | The owner name is matched to an actor profile (by user identity, then by name, with any `user:`/`team:` prefix stripped); if found the classification names the profile, otherwise it carries the OpenLineage name.  The OpenLineage name and type are always recorded in the classification's `additionalProperties` (new in this change).  Only added when the element is not already owned. |
| `parent` run facet (parent and root job) | `Process` for each, `ProcessHierarchy` (OWNED) root → parent → job | A newly created child process is anchored to its parent, so deleting the parent removes its children.  An existing process keeps its own anchor. |
| `jobDependencies` run facet, JOB entries of the `lineage` job facet | `ControlFlow` between processes | guard = status trigger rule, label = dependency type. |
| Run (runId) | `TransientEmbeddedProcess` owned by the job's process via `ProcessHierarchy` (OWNED) | Optional (`catalogRuns`, default off).  `activityStatus` from the event type, `startTime`/`completionTime` from event times, `requestedStartTime` from nominal start; run facets in additional properties. |
| Input / output dataset (namespace, name) | The element that describes the physical data: a catalogued `RelationalTable` (with its `DeployedDatabaseSchema` or `RelationalDatabase`), `Topic`, file, Unity Catalog table ... or, when nothing is catalogued, a new asset created from the catalog template for the technology in the namespace, or a generic `DataStore` | See "Matching datasets to the physical landscape" and "Creating datasets and jobs that are not catalogued" below.  A new asset is named `{typeName}::{namespace}::{name}` with namespace in `namespacePath`, name in `resourceName` (and `pathName` for files). |
| `documentation`, `version` dataset facets | `description`, `versionIdentifier` | |
| `dataSource`, `storage`, `catalog`, `symlinks`, `hierarchy`, `tags`, `datasetType`, `lifecycleStateChange` dataset facets | additional properties | |
| `lifecycleStateChange` RENAME (assets this connector created) | asset's `displayName`, `resourceName`, `namespacePath` (and `pathName`, `qualifiedName` when it used this connector's convention) updated in place | The repository is bi-temporal so the previous names remain visible at their point in time. |
| `lifecycleStateChange` DROP (assets this connector created) | asset archived (`Memento`) or deleted according to the connector's delete method | The dropped dataset takes no further part in the event's lineage. |
| `schema` dataset facet | `TabularSchemaType` + `TabularColumn`s (nested fields as nested attributes; type in `TypeEmbeddedAttribute`; ordinal position on the parent relationship) | Optional (`catalogSchemas`); only for assets this connector created, and only when the asset has no schema - the columns of a catalogued table come from the technology connector. |
| inputs → job → outputs | `DataFlow` relationships (input → process, process → output) | When a dataset is a table, the `DataFlow` is also created between the process and the table's schema (or database) asset, so the lineage is visible at the asset level as well as in detail.  Duplicate relationships are avoided by querying (and caching) existing ones. |
| `columnLineage` dataset facet, field entries of the `lineage` facets | `DataMapping` between columns, and between the tables that hold them | label/description from the transformations. |
| DATASET entries of the `lineage` job/dataset facets | `DataFlow` (and a `DataMapping` when both ends are tables) | |
| Abstractions over a dataset (`TabularDataSet`, `TabularDataSetCollection`) | `DataSetContent` from the abstraction to the element it is a view over | One relationship for each information supply chain (DataSetContent is multi-link). |
| `inputStatistics`, `outputStatistics`, `dataQualityMetrics` | `ResourceProfileAnnotation` (profileCounts, profileDoubles, profileDates) | Optional (`captureStatistics`). |
| `dataQualityAssertions`, `test` run facet | `QualityAnnotation` (dimension = assertion/test type, score 100/0, expected/actual/severity in the description, SQL in `expression`) | Optional (`captureDataQuality`). |
| Annotations | `SurveyReport` per event, anchored to the run (or job), `ReportOriginator` → run/job, `ReportSubject` → dataset, `AssociatedAnnotation` → dataset/process | Created only when the event carries statistics or quality results. |
| Write times and statistics | `DataScope` classification on output assets | Optional (`updateDataScope`); see below. |
| Run frequency and volume | `RunMetrics` classification on the job's process | Always maintained; see below. |

### Matching jobs to existing elements

The OpenLineage namespace and name are extracted from the technology, so they are the most reliable identity
for a resource.  For each job the connector:

1. looks for the process it would have created itself (`DeployedSoftwareComponent::{namespace}::{name}`, or the
   legacy job name);
2. otherwise searches the catalog for processes whose `resourceName` equals the name and whose `namespacePath`
   equals the namespace, and uses the match if there is exactly one and it is a `DeployedSoftwareComponent`;
3. otherwise creates a new process and links every match to it with a `PeerDuplicateLink` in DISCOVERED status
   (source = connector name, notes explain the match), leaving the decision to the duplicate management process
   (stewards or the Mendel automated duplicate manager).

A run from the governance action publisher (one with the `egeria_governanceAction` facet naming a process) is not
catalogued as a job.  `OpenLineageGovernanceActionResolver` (in the Open Integration Framework, shared with the
Lovelace services) finds the elements behind it from the facet's `processName` and `processStepName`:

* the lineage is attached to the `GovernanceActionProcess` that the run's process instance is governed by - the
  same element that provisioning services such as Wedgwood connect their own lineage to when they are asked for
  top-level lineage, so the two views of a delivery meet at one process;
* the `RunMetrics` classification is kept on the `GovernanceActionProcessStep`, because each step of a process runs
  separately (RunMetrics is valid on a Process and on a GovernanceActionProcessStep);
* the `GovernanceActionProcessInstance` already represents the run, so no run element is created and survey
  reports are anchored to the instance.

None of these are assets, so they are read through the open metadata store.

### Matching datasets to the physical landscape

The lineage from OpenLineage events is attached to the elements that describe the physical landscape - the
catalogued tables, topics and files - and the higher level abstractions over them (tabular data sets and tabular
data set collections, such as the data sets of digital products) are linked above them with `DataSetContent`.

`OpenLineageDataSetResolver` (in the Open Integration Framework, so the Lovelace services use the same rules) finds
every element that describes a dataset:

* the element named by the dataset, for datasets in the `egeria` namespace (this is how the governance action
  publisher names action targets that have no OpenLineage identity);
* the elements whose `resourceName` and `namespacePath` are the dataset's name and namespace;
* the elements catalogued by Egeria's technology connectors, found through the endpoints of their connections.
  Egeria's technology connectors do not name what they catalogue the OpenLineage way: they name servers by their
  logical server name and record the host and port only in the endpoints.  Namespaces are compared after they are
  reduced to the OpenLineage spelling of the scheme (`postgresql` → `postgres`, `sqlserver` → `mssql`, `athena` →
  `awsathena`, ...) with the technology's default port filled in, so `postgres://db` matches the endpoint
  `jdbc:postgresql://db:5432/sales` (see `OpenLineageNamespace`).

| Namespace | Found through | Physical element |
|-----------|---------------|------------------|
| `postgres`, `mssql`, `oracle`, `db2` with name `{database}.{schema}.{table}` | an endpoint whose JDBC URL names the host, port and database → its connection → the `RelationalDatabase` (or `DeployedDatabaseSchema`) asset → the `RelationalTable` the JDBC integration connector named `{databaseQN}::{schema}::{table}` (or `{schemaQN}::{table}`; Oracle and Db2 names are also tried in upper case) | The `RelationalTable`, with the `DeployedDatabaseSchema` (or, if the schema is not catalogued, the `RelationalDatabase`) as its asset. |
| the same, with name `{database}.{schema}` | as above | The `DeployedDatabaseSchema`. |
| `kafka` with a topic name | an endpoint whose network address is the topic name, and whose connection's `bootstrap.servers` includes the broker | The `Topic`. |
| `file` (local file system) | the data stores whose `pathName` is the dataset name | The file or folder. |
| `unitycatalog` with name `{catalog}.{schema}.{table}` | the qualified name the Unity Catalog connectors give a table (`Unity Catalog Table::{server URL}::{full name}`) | The `VirtualRelationalTable`. |

The matches are divided into two lists:

* **Physical elements.**  The first is used for the dataset: elements catalogued by technology connectors come
  before elements created from OpenLineage events (named `{typeName}::{namespace}::{name}`), and within each group
  the oldest comes first.  Each other physical element is linked to it with a `PeerDuplicateLink` in DISCOVERED
  status, if they are not already linked.  A new asset is only created when there is no physical element.
* **Abstractions** - `TabularDataSet` and `TabularDataSetCollection` assets that are not ones this connector
  created.  They never take lineage.  Those whose endpoint names the dataset's schema are found too: a collection
  over the schema that holds a table is linked to the table's schema asset, and a tabular data set whose display
  name is `{database}.{schema}.{table}` is linked to the table.  Each is linked with a `DataSetContent` relationship
  (data set at end 1, content at end 2) for the event's information supply chain, if there is not one already.
  An abstraction that is the dataset itself (for example a collection named in the `egeria` namespace) is resolved
  through its own endpoint to the physical element it is a view over; if there is none, the abstraction itself is
  used rather than creating a new asset.

The dataset facets only update the properties of assets this connector created; elements catalogued by technology
connectors gain an ownership classification if they have none, and the OpenLineage identity (`resourceName` and
`namespacePath`) if they have neither.  Renames and drops are only applied to assets this connector created.
Results are cached for a few minutes.

### Creating datasets and jobs that are not catalogued

A dataset or job that nothing describes yet is created from a catalog template, so that it has the right open
metadata type and technology type (`deployedImplementationType`).  The templates create just the asset (most of
these technologies have no connectors yet); the connector supplies the names, the OpenLineage identity and the
properties from the facets, which replace the template's values.  The technology types and template types are in
`org.odpi.openmetadata.adapters.connectors.controls`, and the templates are in the core content pack (or, for
Postgres, SQL Server, Oracle, Db2 and Unity Catalog tables, in that technology's content pack).  When a template is
not loaded - for example because the Postgres content pack is not - the asset is created directly with the same type
and technology type.

| Dataset namespace scheme | Template (technology type) | Open metadata type |
|--------------------------|----------------------------|--------------------|
| `postgres`, `mssql`, `oracle`, `db2` | PostgreSQL / Microsoft SQL Server / Oracle / Db2 Table | `DataSet` |
| `sqlserver` (Azure Synapse), `awsathena`, `arn` (AWS Glue), `azurekusto`, `bigquery`, `fabric-warehouse`, `mysql`, `crate`, `hive`, `oceanbase`, `teradata`, `redshift`, `snowflake`, `spanner`, `trino` | Azure Synapse Analytics / Amazon Athena / AWS Glue / Azure Data Explorer / Google BigQuery / Microsoft Fabric Warehouse / MySQL / CrateDB / Apache Hive / OceanBase / Teradata / Amazon Redshift / Snowflake / Google Cloud Spanner / Trino Table | `DataSet` |
| `unitycatalog` | Unity Catalog Table | `VirtualRelationalTable` |
| `cassandra`, `azurecosmos`, `milvus` | Apache Cassandra Table / Azure Cosmos DB Collection / Milvus Collection | `DataSet` |
| `s3`, `gs`, `wasbs`, `abfss`, `hdfs`, `dbfs` | Amazon S3 / Google Cloud Storage / Azure Blob Storage Object, Azure Data Lake Storage / Apache Hadoop HDFS / Databricks File System File - or the matching Folder / Directory when the name ends with `/` | `DataFile` / `DataFolder` |
| `file` | Data File (technology type from the file's format, e.g. CSV Data File) / Data Folder | `DataFile` / `DataFolder` |
| `box`, `filenet`, `mssharepoint` | Box File / IBM FileNet Document / Microsoft SharePoint Document | `Document` |
| `kafka` | Apache Kafka Topic (the Kafka topic template, which also creates the topic's connection with its bootstrap servers) | `Topic` |
| `pubsub` | Google Cloud Pub/Sub Topic, or Subscription for `subscription:` names | `Topic` / `DataFeed` |
| `inmemory` | In-Memory Data Set | `DataSet` |
| anything else | none - a generic `DataStore` that can be retyped to a more specific subtype later | `DataStore` |

| `jobType` facet (integration / jobType) | Template (technology type) |
|-----------------------------------------|----------------------------|
| `AIRFLOW` / `DAG` | Apache Airflow DAG |
| `AIRFLOW` / anything else | Apache Airflow Task - and its parent job (from the `parent` run facet) is created as an Apache Airflow DAG |
| `SPARK` | Apache Spark Job |
| `DEBEZIUM` | Debezium Connector Task |
| `SQL`, or jobType `QUERY` | SQL Job |
| anything else | none - a generic `DeployedSoftwareComponent` |

### Information supply chains

Every lineage relationship created from an event (`DataFlow`, `ControlFlow`, `DataMapping`) and every
`DataSetContent` link to an abstraction is tagged with the event's information supply chain.  It comes from:

1. Egeria's `egeria_informationSupplyChain` run facet, which any producer can add - for example an Apache Airflow DAG
   (see "Egeria's custom facets" below); otherwise
2. the `iscQualifiedName` in the `egeria_governanceAction` run facet that the governance action publisher adds.

The same data flow can be part of several information supply chains, so a relationship is only reused if it is
tagged with the same one.

### Egeria's custom facets

Both are run facets, named with the `egeria_` prefix as the OpenLineage spec requires for custom facets.  Their
schemas are in the Open Integration Framework
(`src/main/resources/openlineage/facets/1-0-0/`) and their `_schemaURL`s point at
`https://egeria-project.org/openlineage/facets/1-0-0/{facet}.json`.

| Facet | Schema | Content |
|-------|--------|---------|
| `egeria_governanceAction` | `EgeriaGovernanceActionRunFacet.json` | Added by the governance action publisher: `iscQualifiedName`, `engineActionGUID`, `governanceEngineName`, `requestType`, `governanceActionTypeName`, `processName`, `processStepName`. |
| `egeria_informationSupplyChain` | `EgeriaInformationSupplyChainRunFacet.json` | `iscQualifiedName` - the information supply chain the run is part of.  For producers outside Egeria, such as Apache Airflow DAGs. |

An Apache Airflow DAG can add `egeria_informationSupplyChain` to its run events with the OpenLineage provider's
custom run facets option (`[openlineage] custom_run_facets`, which names functions that return extra run facets).
The facet looks like this in the event:

```json
"run": {
  "runId": "...",
  "facets": {
    "egeria_informationSupplyChain": {
      "_producer": "https://github.com/apache/airflow",
      "_schemaURL": "https://egeria-project.org/openlineage/facets/1-0-0/EgeriaInformationSupplyChainRunFacet.json#/$defs/EgeriaInformationSupplyChainRunFacet",
      "iscQualifiedName": "InformationSupplyChain::Personalized Treatment Ordering Information Supply Chain"
    }
  }
}
```

### Parent jobs and containment

The `parent` run facet is the only standard way an OpenLineage event names the job that spawned it, and it is
emitted by the child.  This is ownership: an Airflow task exists only as part of its DAG, a Spark action only as
part of its application, so the connector uses OWNED containment and anchors new child processes to their parent.

USED containment - a child process invoked by several parents that do not own it - cannot be expressed today
because the child's event names at most one parent and the parent's events say nothing about its children.  A
proposed extension is a custom **job facet on the parent** listing the child jobs it spawns (namespace, name and
whether it owns them).  When such a facet appears the connector would create USED `ProcessHierarchy` relationships
from the parent to each listed child without changing the children's anchors.

### DataScope

The `DataScope` classification describes the data held in the store.  It is maintained on each output dataset
when a run completes (or when a RUNNING event carries output statistics):

- `dataCollectionStartTime` is when the first record was stored: the earliest write seen, restarted when the
  `lifecycleStateChange` facet reports CREATE, OVERWRITE or TRUNCATE because the store then holds only that
  write's records;
- `dataCollectionEndTime` is the last known write: the latest write seen.  The write time is the event time of
  the event reporting the write, or the dataset's `lastUpdated` metric when the producer supplies it;
- `additionalProperties` records `writeCount`, `lastWrittenByRunId`, `lastWrittenAt`, `lastRowCount`,
  `lastSize`, `lastFileCount`, `lastSubsetWritten` (the `subset` output facet as JSON),
  `lastLifecycleStateChange` and `lastDatasetVersion`.

The run's nominal time window is not used: it describes the schedule slot, not the data.  `scopeElements` is
left alone.

Input datasets get `readCount`, `lastReadByRunId` and `lastReadAt` in the same classification.

### Process run metrics

Whether or not runs are catalogued as elements, the job's process carries the `RunMetrics` classification (new in
this change, defined in model 0215 Software Components of the 6.2 open metadata types and attachable to any `Process`).  Its properties are
`runCount`, `failedRunCount`, `firstRunStartTime`, `lastRunId`, `lastRunStartTime`, `lastRunEndTime`,
`lastRunStatus`, `lastRunDuration` and `totalRunDuration` (milliseconds), `lastRunRowsRead`, `lastRunRowsWritten`,
`lastRunBytesRead`, `lastRunBytesWritten`, `totalRowsRead`, `totalRowsWritten`, `totalBytesRead`,
`totalBytesWritten` and `additionalProperties`.  Together with `firstRunStartTime` the counts give the average run
interval and duration without reading the log store; the per-run series still lives in the log store.

### Facets that are deliberately not captured

The `environmentVariables` run facet and the source text in the `sourceCode` job facet are parsed by the beans but
never written to open metadata: environment variables routinely carry credentials and connection strings, and
source text can embed the same.  Only the source language is kept.  Both facets remain available to the log store.

## Assessment: capture on arrival or analyse the log store?

OpenLineage events are traces.  A busy pipeline produces two events per task run (START and COMPLETE),
streaming jobs emit RUNNING events continuously, and each event can be tens of kilobytes.  Every update
made to the metadata repository as an event arrives creates a new version of the element (and a new
out-topic event for every consumer), so the cost of event-time capture is proportional to the run rate,
not to the size of the estate.

**Capture on arrival (structural, low churn, needed immediately)**

- Processes for jobs, parent/root jobs and job dependencies, data assets for datasets, schemas, and the
  DataFlow / ControlFlow / ProcessHierarchy / DataMapping relationships between them.  These change
  only when the pipeline changes, so they are written once and then only re-validated (a lookup per event).
  This is what makes lineage queries answerable straight away.
- Descriptive facets (documentation, ownership, sql, source code location, tags, storage, catalog).  Also
  write-once.
- Lifecycle events for datasets: RENAME updates the asset's names in place (history is kept by the bi-temporal
  repository) and DROP archives or deletes the asset according to the connector's delete method.
- Failures (`errorMessage`, `extractionError`) — arguably worth an immediate, visible record (for example a
  `RequestForAction` annotation or an incident report) rather than only a property on the run element.

**Better derived from the log store (statistical, high churn, needs history)**

- **Run frequency and duration.**  The `RunMetrics` classification on the process is cheap to keep and gives
  averages, but it is a poor substitute for a schedule: how often a job *actually* runs, its typical duration, its failure rate over a
  window and its drift over time all need the series of START/COMPLETE pairs.  A periodic analysis over the
  log store (grouping by job, deriving the interval between START events) can then classify the process
  (hourly/daily/ad hoc), fill in expected duration and flag anomalies, writing to the process once per
  analysis cycle rather than twice per run.
- **Data volume.**  `outputStatistics`/`inputStatistics` give a per-run number.  The useful facts — average
  rows per run, growth of the target, day-of-week pattern, an outlier run — are aggregates.  The connector
  records the last value and running totals, which is enough to answer "is this store being written to and
  by whom", but trend analysis belongs with the log store.
- **DataScope.**  The first and last write times are cheap to maintain incrementally and the connector does so.
  What the log store adds is confidence: a producer that never emits `lifecycleStateChange` leaves the connector
  unable to tell a full refresh from an incremental load, so the start time can only be the first write it saw.
  An analysis over the history of `subset` facets, row counts and schema changes can decide which datasets
  accumulate and which are replaced, and correct the start time.  Geospatial scope cannot be derived from
  OpenLineage at all.
- **Data quality.**  A `QualityAnnotation` per assertion per run is the most repository-hungry part of the
  mapping: a dbt project with 500 tests produces 500 annotations and a survey report per run.  It gives a
  complete audit trail, but a governance view usually wants the *current* quality score of a dataset and its
  trend.  The decision (September 2026) is to start with the complete trail - one survey report per event with an
  annotation per assertion - and adjust if the volume proves too much.  A periodic analysis is a separate process
  that summarises under its own rules (pass rates, trends, one summarising annotation per dataset) and does not
  change what the cataloguer records.  The `captureDataQuality` and `captureStatistics` switches let a deployment
  turn the trail off for particular connectors.
- **Column-level lineage from many runs.**  Individual events often carry partial column lineage (only the
  columns touched by that query).  The union across runs — and the detection of columns that stopped being
  fed — is a log-store analysis.

**Recommendation.**  Keep the event-time connector responsible for the structural lineage and the
descriptive facets, and for the cheap "last seen" markers (last run, last write, last read) that make the
catalog feel live.  Route every event to a log store (the file-based or API-based log store connectors, or
Marquez) and add a scheduled governance service that reads a time window of the log store and maintains the
statistical metadata: the DataScope classification, process run profile (frequency, duration, volume,
failure rate) and data quality summaries.  Runs are not catalogued as elements unless `catalogRuns` is set; the
other optional behaviours default to on so the capability can be evaluated on a small pipeline, and should be
switched off for high-frequency jobs once the log-store analysis is in place.

## Log-store analysis: the Lovelace services

The periodic analysis recommended above is implemented as three Lovelace services in the
[lovelace-insights](../../../lovelace-insights) module, shipped in the Open Lineage content pack, orchestrated by the Babbage Analytical Engine and
selectable independently.  They read the file-based log store (the `openLineageLogStore` action target, or the
`logStoreDirectory` request parameter) for the last `analysisWindowDays` days and update only the elements they can
identify uniquely by namespace and resource name:

| Service | Reads | Writes |
|---------|-------|--------|
| Profile OpenLineage Runs | the START/COMPLETE/FAIL/ABORT series of each job | `RunMetrics.additionalProperties` on the process: runs, completions, failures and failure rate in the window, runs per day, mean/median/min/max interval between runs, interval regularity and an inferred schedule label (HOURLY, DAILY, ...), mean/median/max/p95 duration, mean rows and bytes per run, distinct inputs and outputs.  If the process has no `RunMetrics` yet (events only ever reached the log store) the typed counts are filled from the history too. |
| Refine OpenLineage Data Scope | the writes (with `lifecycleStateChange`, `subset` and statistics) and reads of each dataset | `DataScope` on the asset: the write pattern (REPLACED, PARTITIONED or APPENDED), the collection start time restarted at the latest replacement or moved back to the earliest known write, the end time, writes/readers/writers per window and per day, mean and maximum rows per write. |
| Summarise OpenLineage Data Quality | the `dataQualityAssertions` of each dataset and the `test` facet of each job | one `SurveyReport` per dataset (subject) or job (originator) with a `QualityAnnotation` per dimension (assertion type, qualified by column) whose score is the pass rate, plus an overall annotation. |

## Limitations and open questions

See the questions in the pull request / conversation that introduced this design.  In particular:
where
run-level statistics should live once a log-store analysis exists.
