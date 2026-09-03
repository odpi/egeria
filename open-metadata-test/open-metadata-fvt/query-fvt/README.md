<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository Query Functional Verification Tests (query-fvt)

The query-fvt module is a functional verification test suite that gives the repository query surface -
paging, sorting, subtype filtering, status (soft-delete) filtering, complex property/classification
search, `asOfTime` historical queries, and `graphQueryDepth` - a thorough workout against a real
repository loaded with the (nearly) full set of open metadata archives from the top-level
[content-packs](../../../content-packs) directory.

The same tests run against either the
[PostgreSQL repository](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector)
or the
[in-memory repository](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector),
and running both is the point rather than a convenience. The two answer the same query in quite
different ways - PostgreSQL translates the search into SQL, while the in-memory store retrieves and
then filters in Java - and their observable behaviour is supposed to be identical. Where it silently
is not, only a suite that runs on both sides can tell.

Unlike [open-metadata-bvt](../../open-metadata-bvt), which is fast, hermetic and runs on every build, this
suite:

* loads ~27 archives worth of content before any query test even starts, which can take a while,
* needs a reachable PostgreSQL server for the PostgreSQL run (the in-memory run needs nothing), and
* is **not** part of the default build - it only runs when explicitly requested.

## Running it

```bash
./gradlew :open-metadata-test:open-metadata-fvt:query-fvt:test -PrunQueryFvt
```

```bash
./gradlew :open-metadata-test:open-metadata-fvt:query-fvt:test -PrunQueryFvtInMemory
```

`-PrunQueryFvt` uses the PostgreSQL repository and `-PrunQueryFvtInMemory` the in-memory one. Each run
exercises one repository; asking for both at once is refused rather than resolved to one of them. The
in-memory run needs no database and does its archive loading without database round trips, so it is
much the cheaper of the two to run - but it is not a substitute for the PostgreSQL run, since each
repository has behaviour the other cannot exercise.

Without one of those properties, the suite's `test` task is skipped, even by `./gradlew build`/`./gradlew test`
from the repo root - so it is safe to leave registered in `settings.gradle` without slowing down every
other build.

### Pointing it at your own PostgreSQL server

All of the environment-specific configuration - the platform's own port, and which PostgreSQL server,
database and secrets file the repository connects to - lives in
[`src/test/resources/application.properties`](src/test/resources/application.properties), specifically its
`platform.placeholder.variables`. Edit `repositoryDatabaseURL` there to point at a different server;
`repositorySecretCollectionName` and `egeriaServersSecretsStore` control which secrets collection (and
file) supplies the userId/password used to connect. The default targets the `egeria-shared-postgres`
docker container used elsewhere in this project for local testing (see the repo's
`local-test-database-infrastructure` notes), on `localhost:5442`, database `egeria`, using the
`PostgreSQLRepository` collection already present in
[`open-metadata-resources/open-metadata-deployment/secrets/egeria-servers.omsecrets`](../../../open-metadata-resources/open-metadata-deployment/secrets/egeria-servers.omsecrets).

The repository's own content lives in its own PostgreSQL schema
(`repository_queryFvtMetadataStore`), separate from anything else stored on that server, and from
`egeria-shared-postgres`'s other databases.

No Kafka broker is needed - the server is configured with `configureAllAccessServicesNoTopics`, matching
open-metadata-bvt.

## What it does

[OMAGPlatformExtension](src/test/java/org/odpi/openmetadata/queryfvt/OMAGPlatformExtension.java) is a
JUnit 5 extension, shared by every test class in this module, that once per test run:

1. starts the OMAG Server Platform in-process, entirely configured from
   [`application.properties`](src/test/resources/application.properties) (see above),
2. uses the
   [admin services client](../../../open-metadata-implementation/admin-services/admin-services-client) to
   configure a metadata access store server called `queryFvtMetadataStore` - PostgreSQL local repository,
   console audit log, all access services enabled with no event topics, and a startup archive list built
   from every archive under `content-packs`, in dependency order (the two purely self-contained "combo"
   archives, `CocoComboArchive` and `SimpleCatalog`, are left out - their content is a strict superset of
   the individual archives already being loaded, so including them too would only add load time, not
   coverage),
3. starts that server and confirms it is active - this is the slow step, since the PostgreSQL repository
   connector has to process every entity/relationship/classification in every archive,
4. purges any leftover elements from an earlier, possibly failed, run (see "Test data hygiene" below), and
5. shuts the server and platform down cleanly at the end of the whole run.

Each test class then builds its own
[connector context](../../../open-metadata-implementation/frameworks/open-metadata-framework/src/main/java/org/odpi/openmetadata/frameworks/openmetadata/connectorcontext)
via [ConnectorContextFactory](src/test/java/org/odpi/openmetadata/queryfvt/ConnectorContextFactory.java) -
the same object a connector is handed by the platform - backed by an `EgeriaOpenMetadataStoreClient`
pointed at the running server, and exercises `OpenMetadataStore` (and, where a typed client is a more
natural fit - for example creating elements to query, or checking `graphQueryDepth` on an
`OpenMetadataRootElement` - a typed client such as `CollectionClient` or `ProjectClient`) directly:

* [PlatformAndServerFVT](src/test/java/org/odpi/openmetadata/queryfvt/PlatformAndServerFVT.java) - platform
  origin, server-is-active, and archive-content-loaded checks.
* [PagingFVT](src/test/java/org/odpi/openmetadata/queryfvt/PagingFVT.java) - paging through every page of a
  result set visits every element exactly once, with no duplicates or gaps, and the total matches
  `countMetadataElements`; partial final pages and paging past the end of the results.
* [SortingFVT](src/test/java/org/odpi/openmetadata/queryfvt/SortingFVT.java) -
  `PROPERTY_ASCENDING`/`PROPERTY_DESCENDING`, `GUID` (stable, repeatable ordering), and
  `LAST_UPDATE_RECENT` (reacts to an update after creation).
* [SubtypeFVT](src/test/java/org/odpi/openmetadata/queryfvt/SubtypeFVT.java) -
  `metadataElementSubtypeNames` combined with `skipSubtypes`: the include-list and exclude-list result
  sets are checked to be exact complements of each other.
* [StatusFVT](src/test/java/org/odpi/openmetadata/queryfvt/StatusFVT.java) - a soft-deleted element
  disappears from the default (active-only) view but is still found by widening
  `limitResultsByStatus` to include `DELETED`; a purged element is gone regardless of status filter.
* [ComplexQueryFVT](src/test/java/org/odpi/openmetadata/queryfvt/ComplexQueryFVT.java) - nested AND/OR
  property conditions, and a classification-based search (`Confidentiality`, with a numeric
  `confidentialityLevel` property condition).
* [AsOfTimeFVT](src/test/java/org/odpi/openmetadata/queryfvt/AsOfTimeFVT.java) - `asOfTime` reproduces an
  element's properties and active/deleted state as they were at an earlier checkpoint, not its current
  state, exercising the PostgreSQL connector's bi-temporal support.
* [GraphQueryDepthFVT](src/test/java/org/odpi/openmetadata/queryfvt/GraphQueryDepthFVT.java) -
  `GetOptions.graphQueryDepth=0` suppresses every relationship on a returned `OpenMetadataRootElement`,
  even when the relationship genuinely exists (as shown by the same query at the default depth).
* [DeleteMethodFVT](src/test/java/org/odpi/openmetadata/queryfvt/DeleteMethodFVT.java) - `ARCHIVE` classifies
  an element as `Memento` and hides it from a default (non-lineage) search while still finding it with
  `forLineage=true`; `LOOK_FOR_LINEAGE` archives an element that has an attached lineage relationship but
  soft-deletes one that doesn't; a soft-deleted relationship can only be purged (never a still-active one),
  and a purged relationship is genuinely gone, not just historically deleted, as shown by an `asOfTime`
  query for "now" still finding it after a soft-delete but not after a purge; and `findRelationshipsBetweenMetadataElements`
  honours `end1EntityGUIDs`/`end2EntityGUIDs` whether or not property conditions are also supplied.
* [SpecialCharacterFVT](src/test/java/org/odpi/openmetadata/queryfvt/SpecialCharacterFVT.java) - string
  property values containing characters that are significant to SQL round-trip unchanged and can still be
  found. A single quote (as in "Coco Pharmaceutical's Database") does not accumulate extra quotes as the
  same element is updated over and over, and is still matched by an exact property comparison, a unique
  name lookup and a "contains this string" search; the LIKE wildcards `%` and `_` and the LIKE escape
  character `\` are all matched literally rather than changing what the pattern means (or, when a value
  ends in a backslash, making the pattern invalid); a property *name* containing a quote returns no
  results rather than failing the query; and a value containing a null (U+0000) character - the one
  character a text column genuinely cannot hold - is rejected up front with an FFDC message naming the
  column, rather than being passed down to the database.

## Test data hygiene

The archives loaded by this suite are read-only reference content - tests must never modify or delete
anything that came from an archive. Every element a test creates uses a qualified name starting with
`query-fvt:` (see
[QueryFvtTestSupport.newQualifiedName(...)](src/test/java/org/odpi/openmetadata/queryfvt/QueryFvtTestSupport.java)).
Each test purges what it created in a `finally` block, and - because the PostgreSQL repository persists
across runs, unlike open-metadata-bvt's in-memory one - `OMAGPlatformExtension` also purges anything still
matching that prefix, in any status, before the first test runs, in case an earlier run crashed partway
through and left debris behind.

## Adding more query tests

Follow the shape of the existing test classes: build a connector context with
`ConnectorContextFactory.newContext()`, create whatever test elements you need with a qualified name from
`QueryFvtTestSupport.newQualifiedName(...)`, exercise `connectorContext.getOpenMetadataStore()` (or a
typed client), assert, and purge what you created in a `finally` block with
`QueryFvtTestSupport.purgeElement(...)`.

----
* Return to [Open Metadata FVT](..)
* Return to [Open Metadata Test](../..)
* Return to [Module Organization](../../../Content-Organization.md)
* Return to [Home](../../../index.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
