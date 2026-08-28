<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# PostgreSQL Connectors Functional Verification Tests (postgres-fvt)

This suite tests the
[postgres-server-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors)
module — the PostgreSQL server integration connector, the PostgreSQL server and database survey action
services, and the PostgreSQL tabular data set resource connectors — together with the **PostgreSQL core
content pack** that defines and drives them.

It does not call those connectors directly. It stands up the deployment they are designed to run in and then
drives them the way an operator would: through the **Automated Curation API**, by running the governance
action processes the content pack ships.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:postgres-fvt:test -PrunPostgresFvt
```

There is a second mode, which runs the same tests with no event bus at all:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:postgres-fvt:test -PrunPostgresFvtNoKafka
```

Asking for both at once is refused rather than resolved to one of them. See
[Running without an event bus](#running-without-an-event-bus).

## What it stands up

Four servers on one in-process platform, a real PostgreSQL server, and — in the default mode — a real Apache
Kafka broker:

| Server | What it is | What it runs |
| --- | --- | --- |
| `postgresFvtMetadataStore` | Metadata access store | PostgreSQL local repository; all access services **with** Kafka out topics; `OpenMetadataTypes`, `CoreContentPack` and `PostgresContentPack` loaded at start-up |
| `postgresFvtEngineHost` | Engine host | `PostgreSQLSurvey`, `PostgreSQLGovernance` and `Stewardship` governance engines |
| `postgresFvtIntegrationDaemon` | Integration daemon | `PostgreSQLIntegrationGroup`, which is where the PostgreSQL Server Cataloguer runs |
| `postgresFvtViewServer` | View server | Automated Curation OMVS |

**What Kafka is for here** is worth being explicit about, because nothing in the tests mentions it. Neither
governance server is told what to run. Each asks the metadata access store for its configuration when it
starts, then *listens* on the access services' out topics — for configuration changes, and for new engine
actions. That is what makes an engine action start within a second of being requested.

It is not, however, required — see [Running without an event bus](#running-without-an-event-bus).

Only three archives are loaded. The Core content pack is needed because the PostgreSQL processes call its
generic governance services — create an asset from a template, delete it again, add it to an integration
connector as a catalog target — and because the PostgreSQL Server Cataloguer hands each database it finds on
to the Core pack's JDBC Database Cataloguer. Nothing else is loaded: the other content packs would add load
time and a large amount of unrelated metadata for the suite's searches to work around.

The **Stewardship** engine is configured even though it belongs to the Core pack rather than the PostgreSQL
one. The PostgreSQL "create and survey" process ends with a third step that writes the survey report out as a
markdown document, and that step is addressed to Stewardship. Without it the first two steps would run, the
survey report would be produced, and the process would then sit at its last step for ever — which reads
exactly like a broken survey.

## What it tests

* **[ContentPackFVT](src/test/java/org/odpi/openmetadata/postgresfvt/ContentPackFVT.java)** — the content pack
  loaded, and the two governance servers picked up the parts of it they are configured to run. What the pack
  is supposed to contain is not written out: the test iterates the definitions in `core-content-pack` filtered
  to the PostgreSQL pack and checks the repository against them, so adding a connector, an engine or a request
  type to the pack extends this test's coverage without anybody editing it — and adding one without
  regenerating the archive fails here.

* **[AutomatedCurationFVT](src/test/java/org/odpi/openmetadata/postgresfvt/AutomatedCurationFVT.java)** — the
  read half of the Automated Curation API, which is what makes a content pack usable by someone who did not
  write it. A curator does not begin by knowing that a process called
  `PostgreSQLServer::CreateAsCatalogTargetGovernanceActionProcess` exists; they begin by asking what Egeria
  knows about PostgreSQL. So the tests ask in that order, and check the answers are complete enough to act on:
  the technology type is findable by search, its report names the catalog templates and governance action
  processes the rest of this suite runs, and a template named in that report can then be used exactly as
  described.

* **[PostgresServerSurveyFVT](src/test/java/org/odpi/openmetadata/postgresfvt/PostgresServerSurveyFVT.java)** —
  runs `PostgreSQLServer:CreateAndSurveyGovernanceActionProcess`. This is the suite's longest chain and every
  link is a different component: the view server records the request, the engine host hears it on the Open
  Governance out topic and claims it, step 1 builds a PostgreSQL server asset from the catalog template,
  step 2 runs the **PostgreSQL server survey action service** against the real database server, and step 3
  writes the resulting report out as markdown. The test checks the asset arrived with every placeholder
  substituted, that the survey produced a report with annotations on it, and that the markdown file was
  written.

* **[PostgresServerCatalogFVT](src/test/java/org/odpi/openmetadata/postgresfvt/PostgresServerCatalogFVT.java)** —
  the cataloguing lifecycle, in three ordered stages that share state because they are three parts of one
  story. `PostgreSQLServer::CreateAsCatalogTargetGovernanceActionProcess` creates the asset and attaches it to
  the PostgreSQL Server Cataloguer as a catalog target; the integration daemon is then asked to refresh, and
  the databases it catalogues are checked for; finally
  `PostgreSQLServer:DeleteAssetWithTemplateGovernanceActionProcess` takes it all away again, and the test
  checks that the database assets anchored beneath the server went with it. Nothing here calls the integration
  connector — a *different server* delivers the work to it, and that arrangement is as much what is being
  tested as the connector itself.

* **[PostgresTabularDataSetFVT](src/test/java/org/odpi/openmetadata/postgresfvt/PostgresTabularDataSetFVT.java)** —
  the two tabular data set connectors, which are the part of the module no governance action process reaches.
  They are *resource* connectors, so the test builds the virtual connection and asks the connector broker for
  them, exactly as the platform would on behalf of a caller holding an asset. Every count the connector
  reports is checked against PostgreSQL directly as well, so a connector that agreed with itself but not with
  the database would still fail.

## The database under test

The suite creates its own database (`postgres_fvt` by default) on the PostgreSQL server under test, with one
schema and one table in it, and drops and recreates it at the start of every run.

This is deliberate rather than incidental. Cataloguing whatever happens to be on a shared development server
gives nothing specific to assert — "the cataloguer created an asset for `postgres_fvt`" is a real assertion
where "the cataloguer created some assets" is not. It also lets the cataloguer and the survey be scoped with
`includeDatabaseList`, so that a run does not walk every database on a server it shares with other work.

By default the server under test is the same container that backs the metadata repository, so one running
database is enough to run the suite. They are configured separately (`repositoryDatabaseURL` versus the
`postgres.fvt.server.*` settings) because they play different roles: one says where Egeria keeps its own
metadata, the other says which server this suite is asking Egeria to catalogue.

## Clean-up

Everything is cleared at the **start** of a run rather than the end. Clearing up afterwards leaves the debris
behind whenever a run is killed or crashes — which is exactly when it is most likely to be in a state the next
run should not inherit.

* In the repository, every element the suite causes to be created carries `postgres-fvt` somewhere in its
  qualified name. The suite does not set most of those names — the catalog templates do, from the server names
  the suite chooses — which is why the clean-up searches for the marker anywhere in the name rather than at the
  start: a server asset is `PostgreSQL Server::postgres-fvt-catalog`, and a database catalogued beneath it is
  `PostgreSQL Relational Database::postgres-fvt-catalog::postgres_fvt`.
* On the PostgreSQL server, the suite's own database is dropped and recreated.

Set `postgres.fvt.clear.down=false` to keep both for inspection after a run. The tests will then be reading a
repository that holds an earlier run's elements as well as this one's, and may report that as a failure.

The metadata repository schema itself (`repository_postgresFvtMetadataStore`) is **not** dropped, because
reloading the Core content pack into an empty schema is the slowest thing this suite does — see below. To
start completely fresh:

```bash
docker exec egeria-shared-postgres psql -h 127.0.0.1 -p 5442 -U postgres -d egeria \
  -c 'DROP SCHEMA IF EXISTS "repository_postgresfvtmetadatastore" CASCADE;'
```

## Running without an event bus

`-PrunPostgresFvtNoKafka` configures the access services **without** out topics and needs no broker at all.
Every test is expected to pass exactly as it does in the default mode.

That is the point of the mode: an engine host does not require an event bus. It hears nothing on a topic, so
it finds the same things by asking — engine actions that have been requested for its engines but not started,
and configuration that has changed — on a short cycle. Without a broker it reacts a little later; it does not
stop working.

The two modes therefore answer different questions, which is why asking for both at once is refused rather
than resolved to one of them:

| | `-PrunPostgresFvt` | `-PrunPostgresFvtNoKafka` |
| --- | --- | --- |
| Access services | out topics on Kafka | no out topics |
| Needs a broker | yes | no |
| How an engine host learns of work | told, then polls as a backstop | polls |
| Expected result | all tests pass | all tests pass |

## Timing

**The first run is slow, and almost all of it is one step.** Loading `CoreContentPack.omarchive` — 5763
instances — into an empty PostgreSQL repository takes upwards of half an hour. Later runs reuse the schema and
are much quicker. If a first run looks hung, the audit log will show it working steadily through
`OMRS-AUDIT-0050`/`0053` for the Core content pack.

The tests themselves are governed by their own timeouts, all set in
[`application.properties`](src/test/resources/application.properties) and all overridable for a single run:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:postgres-fvt:test -PrunPostgresFvt \
    -Dpostgres.fvt.engine.action.timeout.seconds=600
```

## Diagnosing a failure

Almost nothing this suite tests happens in the test's own thread, so the audit log is where the answer is.
All four servers write to a single file that can be read **while a run is in progress**:

```
open-metadata-test/open-metadata-fvt/postgres-fvt/build/postgres-fvt-data/logs/audit.log
```

Gradle buffers a test JVM's console output until the task ends, so on a run that is stuck the console tells
you nothing until it is too late to be useful.

Engine action failures are reported by kind rather than as a plain timeout, because the three cases have
nothing to do with each other:

* still `REQUESTED` or `APPROVED` — nobody claimed it. The failure prints the engine the action names against
  the engines the host reports running, which is the comparison the engine host makes silently. If those match
  and it still was not claimed, the engine neither heard the event nor swept for it — in the default mode,
  check that the broker named by `kafkaEndpoint` is reachable from this JVM (it must be the **external**
  advertised listener, not the internal one);
* still `IN_PROGRESS` — it was claimed and the governance service is running but did not finish;
* `FAILED` — it ran and said why, and the failure carries the completion message the service recorded.

An integration connector that reports a failing exception at start-up is almost always a class loading
problem: its provider class is named in the content pack but its implementation is not on this module's test
runtime classpath. The start-up check says so explicitly, because the fix is in `build.gradle` rather than
anywhere the audit log would point at.

## Prerequisites

* A reachable **PostgreSQL** server — `egeria-shared-postgres` by default (`localhost:5442`), with an
  `egeria` database and an `egeria_user` role. The credentials come from the `PostgreSQLRepository` collection
  of [`egeria-servers.omsecrets`](../../../open-metadata-resources/open-metadata-deployment/secrets/egeria-servers.omsecrets),
  and need only be able to create a schema in the `egeria` database - see
  [The database under test](#the-database-under-test).
* A reachable **Apache Kafka** broker — `egeria-shared-kafka` by default, and only for the default mode;
  `-PrunPostgresFvtNoKafka` needs none. It must be the broker's EXTERNAL
  advertised listener (`oak.local:9194`), not the internal one: this suite runs on the host rather than inside
  the docker network, and a client connecting on the internal listener is handed back an address it cannot
  resolve.

Both, along with the platform's port (9451) and every timeout, are set in
[`src/test/resources/application.properties`](src/test/resources/application.properties).

## Notes on the connectors under test

Two behaviours of the tabular data set connectors shape what
[PostgresTabularDataSetFVT](src/test/java/org/odpi/openmetadata/postgresfvt/PostgresTabularDataSetFVT.java)
can check, and are recorded here rather than left to be rediscovered:

* the connector composes `INSERT INTO <table> VALUES (...)` by concatenating the supplied values **verbatim**,
  so a caller writing text has to supply it as a SQL literal, quotes included;
* `deleteRecord` is not implemented — it is a no-op — so the suite does not exercise it. A test that called it
  and asserted nothing would read as coverage it does not have.

----
Return to [open-metadata-fvt](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
