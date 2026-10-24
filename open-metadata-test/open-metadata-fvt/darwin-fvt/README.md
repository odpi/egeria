<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Darwin Product Dependency Manager FVT (darwin-fvt)

This is a Functional Verification Test (FVT) suite for the
[Darwin Product Dependency Manager](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/README.md#darwin-product-dependency-manager),
the nanny integration connector that maintains the coarse-grained lineage implied by the finer-grained
lineage beneath it.  It covers each boundary between the layers of lineage that Darwin works across, in the order it works across them:

* **schema elements to the data fabric** - a `DataMapping` between two assets' columns, or between a column
  and a process, becomes a `DataFlow` between the assets (or the asset and the process);
* **the data fabric to the system fabric** - lineage between the data assets and processes that two servers'
  capabilities use (owned data assets; processes whatever the use type) becomes a relationship between the
  servers, following indirect paths through intermediate elements: a `DataFlow` for data lineage, a
  `ProcessCall` for a process call, and a `ControlFlow` for control flow;
* **data assets to digital products** - lineage between two products' assets becomes a
  `DigitalProductDependency` between the products, again following indirect paths.

It also covers the way Darwin treats relationships asserted by external users: filling in a missing
information supply chain when lineage proves the relationship, leaving the relationship alone otherwise, and
recording an `Exception` - with an exception type per relationship type - against the element that depends on an
asserted relationship that the layer beneath does not prove, provided that layer is catalogued for both of its
ends.

## Running it

Like the other FVTs, this suite is not part of the default build.  It needs a reachable PostgreSQL server and
an Apache Kafka broker, and it is only run deliberately:

```
./gradlew :open-metadata-test:open-metadata-fvt:darwin-fvt:test -PrunDarwinFvt
```

Where those servers are and how long the suite is prepared to wait for the integration daemon are set in
[application.properties](src/test/resources/application.properties).  The defaults target the
`egeria-shared-postgres` and `egeria-shared-kafka` containers used elsewhere in this project for local
testing.  The platform's own port is allocated at run time so that two checkouts can run the suite at once.

## What it stands up

| Server | What it is for |
|---|---|
| `darwinFvtMetadataStore` | Metadata access store with a PostgreSQL repository and its access services publishing to Kafka.  Loads the open metadata types, the core content pack (where Darwin's definitions live) and this suite's fixture archive. |
| `darwinFvtIntegrationDaemon` | Integration daemon running the core content pack's `Egeria:IntegrationGroup:Darwin`, which is where the Darwin Product Dependency Manager runs. |

## The fixture

[DarwinArchiveWriter](src/test/java/org/odpi/openmetadata/darwinfvt/DarwinArchiveWriter.java) builds the
fixture as an open metadata archive at test time, so it is deterministic and no binary test data is checked
in.  Loading it as an archive also means every relationship in it is owned by the archive rather than the
repository - which is exactly what a relationship "asserted by an external user" looks like to Darwin, and
what makes Darwin name the owning metadata collection when it fills in a supply chain.

Every element has a qualified name starting with `darwin-fvt:` and a fixed unique identifier, listed in
[DarwinFvtTestSupport](src/test/java/org/odpi/openmetadata/darwinfvt/DarwinFvtTestSupport.java).  The
repository persists between runs, so the suite purges the fixture's elements - and with them everything
Darwin created between them - before loading the archive again.

| Set | Contents | What it exercises |
|---|---|---|
| 1 - mapped | Two data assets whose columns are joined by a `DataMapping` (supply chain *mapped*); each owned by a server's capability and the member of a product; a `DigitalProductDependency` between the products with no supply chain | Asset-level and server-level data flows derived from the mapping; the supply chain filled in on the products' asserted dependency |
| 2 - indirect | A third asset, server and product, reached from the mapped source asset through a process along supply chain *indirect* | Indirect paths at the server and product levels |
| 3 - broken chain | A fourth asset and product reached from the same process, but along supply chain *other* | A path that changes supply chain is not followed |
| 4 - unproven | A product whose asset has no lineage, with an asserted dependency on the mapped source product | The exception Darwin records, and clears once the dependency is removed |
| 5 - stale | Two assets joined by a mapping and nothing else | The derived data flow withdrawn once the mapping is removed |
| 6 - present | Two assets joined by a mapping, with the data flow between them already asserted | Darwin does not add a second data flow |
| 7 - process use | A process used (`USES`) by one server's capability, feeding an asset owned by a second server and an asset that a third server's capability only uses | A process counts for the server that uses it; a data asset only for the server that owns it |
| 8 - control flow | Two processes on their own servers, each the member of its own product, joined by a `ControlFlow` | A `ControlFlow` between the servers, no `DataFlow`, and no product dependency |
| 9 - process call | Two processes on their own servers joined by a `ProcessCall` | A `ProcessCall` between the servers |
| 10 - table to process | A column of an asset mapped to a process | A `DataFlow` from the asset to the process |
| 11 - unproven data flow | Two assets whose columns are mapped to other assets but not to each other, with a `DataFlow` between them asserted by the archive | An `UnprovenDataFlow` exception against the asset the data reaches |
| 12 - unchecked data flow | Two assets with no schema detail and a `DataFlow` between them asserted by the archive | No exception - the missing detail is no evidence against the data flow |
| 13 - unproven control flow | Two servers each using a process, with a `ControlFlow` between the servers asserted by the archive and none between the processes | An `UnprovenControlFlow` exception against the server control passes to |
| 14 - unchecked dependency | A product with no members and an asserted dependency on the mapped source product | No exception - no lineage could prove or disprove it |
| 15 - supply chain within a path | Two products' assets joined through a process by an untagged `DataFlow` and then a tagged one | The dependency is derived for the tagged supply chain, and it is filled in on the untagged data flow |
| 16 - supply chain from above | Two products' assets joined by an untagged `DataFlow`, with a dependency between the products asserted for a supply chain | The untagged path proves the dependency (no exception, no extra untagged dependency), and the dependency's supply chain is filled in on the data flow |
| 17 - ambiguous supply chain | An untagged `DataFlow` to a process that feeds two products' assets along two supply chains | The data flow stays untagged, with one Darwin copy of it for each supply chain |
| 18 - data sets as views | Products whose members are data sets over physical assets (`DataSetContent`), with lineage between the physical assets; a third product's data set covers the same content for a different supply chain | The dependency is derived through the data sets' content; the third product gets none |
| 19 - data sets over tables | Products whose members are data sets over relational tables, with the tables joined through a process by `DataMapping` relationships and no asset-level lineage | The dependency is derived by following the table-to-process mappings |
| 20 - adopted data flow | Two assets whose columns are mapped, each also mapped to a third asset; the test edits the data flow Darwin derives, then removes the mapping | The adopted data flow is kept, with the edit, and recorded as unproven |

Sets 4, 5 and 20 are changed by the tests that use them and are read by nothing else, so the order the tests run
in does not matter to the rest of the suite.

## How the tests control Darwin

Darwin's refresh interval is an hour, so the tests drive it explicitly through the integration daemon's
`refreshConnector` API rather than waiting.  That call is synchronous only when the connector is idle: while a
refresh is already in progress - such as the first refresh, which the daemon starts as soon as Darwin is
running - it returns at once without refreshing.  So the suite waits for Darwin to be idle, with its first refresh
done, before asking, and again afterwards.  When that returns, every layer and the exceptions have been
reconciled.

## Reading a failure

Most of what this suite tests happens somewhere else: a test asks for a refresh and then looks at the
repository.  When an assertion fails, the reason is usually in the integration daemon's audit log, which is
written to `build/darwin-fvt-data/logs/audit.log` while the run is in progress.  The messages to look for:

| Message | Meaning |
|---|---|
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0002` | Darwin started, and the lineage depth it is using |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0012` | A lineage relationship was derived (a data flow in the data fabric, or a data flow, process call or control flow between servers) |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0013` | A derived lineage relationship was withdrawn |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0014` | A supply chain was filled in on an asserted lineage relationship |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0015` | The lineage levels completed, with tallies (including unproven and unchecked) |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0016` | An exception was recorded or updated for unproven lineage relationships |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0017` | An exception for unproven lineage relationships was cleared |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0018` | A copy of an untagged relationship was made for one of its supply chains |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0019` | A copy of an untagged relationship was removed |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0020` | The supply chains in the layers beneath were established, with tallies |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0003` | An exception type was created (one per relationship type, on first use) |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0004` | A product dependency was derived |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0006` | A supply chain was filled in on an asserted dependency |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0007` | An exception was recorded or updated |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0008` | An exception was cleared |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0010` | The product level completed, with tallies |

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
