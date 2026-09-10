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
| Job (namespace, name) | `DeployedSoftwareComponent` (a `Process`) | Matched on `namespacePath` + `resourceName` (see below); created as `DeployedSoftwareComponent::{namespace}::{name}`. The pre-existing `OpenLineageJob:{name}` naming is still recognised. |
| `documentation` job facet | `description` | Only set when the process has no description (stewards' edits are preserved). |
| `sql` job facet | `formula` / `formulaType` (`SQL:{dialect}`) | |
| `sourceCode` job facet | `implementationLanguage` | The source text itself is not stored. |
| `jobType` job facet | `deployedImplementationType` (`{integration} {jobType}`) plus additional properties | |
| `sourceCodeLocation`, `tags` job facets | additional properties (`sourceCodeLocation.*`, `tag:{key}`) | |
| `ownership` job/dataset facet | `Ownership` classification (first owner) | The owner name is matched to an actor profile (by user identity, then by name, with any `user:`/`team:` prefix stripped); if found the classification names the profile, otherwise it carries the OpenLineage name.  The OpenLineage name and type are always recorded in the classification's `additionalProperties` (new in this change).  Only added when the element is not already owned. |
| `parent` run facet (parent and root job) | `Process` for each, `ProcessHierarchy` (OWNED) root → parent → job | A newly created child process is anchored to its parent, so deleting the parent removes its children.  An existing process keeps its own anchor. |
| `jobDependencies` run facet, JOB entries of the `lineage` job facet | `ControlFlow` between processes | guard = status trigger rule, label = dependency type. |
| Run (runId) | `TransientEmbeddedProcess` owned by the job's process via `ProcessHierarchy` (OWNED) | Optional (`catalogRuns`, default off).  `activityStatus` from the event type, `startTime`/`completionTime` from event times, `requestedStartTime` from nominal start; run facets in additional properties. |
| Input / output dataset (namespace, name) | Data asset: `TabularDataSet` (tables/views/lakehouse tables), `DataFile`/`DataFolder` (object stores and file systems), `Topic` (Kafka etc.), otherwise `DataSet` | Matched on `namespacePath` + `resourceName` (see below).  Type chosen from the namespace scheme and the `datasetType`/`storage` facets; created as `{typeName}::{namespace}::{name}` with namespace in `namespacePath`, name in `resourceName`/`pathName`. |
| `documentation`, `version` dataset facets | `description`, `versionIdentifier` | |
| `dataSource`, `storage`, `catalog`, `symlinks`, `hierarchy`, `tags`, `datasetType`, `lifecycleStateChange` dataset facets | additional properties | |
| `lifecycleStateChange` RENAME | asset's `displayName`, `resourceName`, `namespacePath` (and `pathName`, `qualifiedName` when it used this connector's convention) updated in place | The repository is bi-temporal so the previous names remain visible at their point in time. |
| `lifecycleStateChange` DROP | asset archived (`Memento`) or deleted according to the connector's delete method | The dropped dataset takes no further part in the event's lineage. |
| `schema` dataset facet | `TabularSchemaType` + `TabularColumn`s (nested fields as nested attributes; type in `TypeEmbeddedAttribute`; ordinal position on the parent relationship) | Optional (`catalogSchemas`); only when the asset has no schema. |
| inputs → job → outputs | `DataFlow` relationships (input asset → process, process → output asset) | Duplicate relationships are avoided by querying (and caching) existing ones. |
| `columnLineage` dataset facet, field entries of the `lineage` facets | `LineageMapping` between `TabularColumn`s | label/description from the transformations. |
| DATASET entries of the `lineage` job/dataset facets | `DataFlow` | |
| `inputStatistics`, `outputStatistics`, `dataQualityMetrics` | `ResourceProfileAnnotation` (profileCounts, profileDoubles, profileDates) | Optional (`captureStatistics`). |
| `dataQualityAssertions`, `test` run facet | `QualityAnnotation` (dimension = assertion/test type, score 100/0, expected/actual/severity in the description, SQL in `expression`) | Optional (`captureDataQuality`). |
| Annotations | `SurveyReport` per event, anchored to the run (or job), `ReportOriginator` → run/job, `ReportSubject` → dataset, `AssociatedAnnotation` → dataset/process | Created only when the event carries statistics or quality results. |
| Write times and statistics | `DataScope` classification on output assets | Optional (`updateDataScope`); see below. |
| Run frequency and volume | `RunMetrics` classification on the job's process | Always maintained; see below. |

### Matching jobs and datasets to existing elements

The OpenLineage namespace and name are extracted from the technology, so they are the most reliable identity
for a resource, and other connectors record the same values in `namespacePath` and `resourceName`.  For each job
and dataset the connector:

1. looks for the element it would have created itself (`{typeName}::{namespace}::{name}`, or the legacy job name);
2. otherwise searches the catalog for assets whose `resourceName` equals the name and whose `namespacePath` equals
   the namespace;
3. uses the match if there is exactly one and its type is the expected type or a subtype of it;
4. otherwise (no match, several matches, or an incompatible type) creates a new element and links every match to it
   with a `PeerDuplicateLink` in DISCOVERED status (source = connector name, notes explain the match), leaving the
   decision to the duplicate management process (stewards or the Mendel automated duplicate manager).

Other technologies' naming conventions can be plugged into `findMatchingAssets` as they are discovered.

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
  DataFlow / ControlFlow / ProcessHierarchy / LineageMapping relationships between them.  These change
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
