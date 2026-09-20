<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Darwin Product Dependency Manager FVT (darwin-fvt)

This is a Functional Verification Test (FVT) suite for the
[Darwin Product Dependency Manager](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/README.md#darwin-product-dependency-manager),
the nanny integration connector that maintains the coarse-grained lineage implied by the finer-grained
lineage beneath it.  It covers all three levels Darwin works through, in the order it works through them:

* **schema elements to data assets** - a `DataMapping` between two assets' columns becomes a `DataFlow`
  between the assets;
* **data assets to software servers** - lineage between the data assets that two servers' capabilities own
  becomes a `DataFlow` between the servers, following indirect paths through intermediate elements;
* **data assets to digital products** - lineage between two products' assets becomes a
  `DigitalProductDependency` between the products, again following indirect paths.

It also covers the way Darwin treats relationships asserted by external users: filling in a missing
information supply chain when lineage proves the relationship, leaving the relationship alone otherwise, and
recording an `Exception` against a product whose asserted dependency no lineage proves.

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

Sets 4 and 5 are changed by the tests that use them and are read by nothing else, so the order the tests run
in does not matter to the rest of the suite.

## How the tests control Darwin

Darwin's refresh interval is an hour, so the tests drive it explicitly through the integration daemon's
`refreshConnector` API rather than waiting.  The refresh is synchronous: when the call returns, all three
levels and the exceptions have been reconciled.

## Reading a failure

Most of what this suite tests happens somewhere else: a test asks for a refresh and then looks at the
repository.  When an assertion fails, the reason is usually in the integration daemon's audit log, which is
written to `build/darwin-fvt-data/logs/audit.log` while the run is in progress.  The messages to look for:

| Message | Meaning |
|---|---|
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0002` | Darwin started, and the lineage depth it is using |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0012` | A data flow was derived (between assets or between servers) |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0013` | A derived data flow was withdrawn |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0014` | A supply chain was filled in on an asserted data flow |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0015` | The lineage levels completed, with tallies |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0004` | A product dependency was derived |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0006` | A supply chain was filled in on an asserted dependency |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0007` | An exception was recorded or updated |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0008` | An exception was cleared |
| `DARWIN-PRODUCT-DEPENDENCY-MANAGER-0010` | The product level completed, with tallies |

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
