<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Build Verification Tests (BVT)

The open-metadata-bvt module is a build verification test suite that runs automatically as part of the
normal Gradle build.  It is a fast, hermetic sanity check that:

* the [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform/) starts up,
* a [metadata access store](https://egeria-project.org/concepts/metadata-access-store/) server can be
  configured and started on it, and
* the repository is working - metadata can be created, updated, found, retrieved and deleted through it.

It needs no Kafka broker and no PostgreSQL server - just the platform.  The platform runs in-process
(in the same JVM as the tests, via `SpringApplication`, on a random free port) rather than as a separate
process, so the whole suite runs in a few seconds.

## What it does

[OMAGPlatformExtension](src/test/java/org/odpi/openmetadata/bvt/OMAGPlatformExtension.java) is a JUnit 5
extension, shared by every test class in this module, that once per test run:

1. starts the platform in-process, with an in-memory local repository and console audit log available but
   nothing yet configured,
2. uses the
   [admin services client](../../open-metadata-implementation/admin-services/admin-services-client) to
   configure a metadata access store server called `bvtMetadataAccessStore` - in-memory repository,
   console audit log, and all access services enabled with no event topics,
3. uses the
   [platform services client](../../open-metadata-implementation/platform-services/platform-services-client)
   to start that server and confirm it is active, and
4. shuts the server and platform down cleanly at the end of the run.

Each test class then builds its own
[connector context](../../open-metadata-implementation/frameworks/open-metadata-framework/src/main/java/org/odpi/openmetadata/frameworks/openmetadata/connectorcontext)
- the same object a connector is handed by the platform - backed by an `EgeriaOpenMetadataStoreClient`
pointed at the running server, and drives one of its connector context clients through a full
create/get/update/find/delete lifecycle for a metadata type:

* [PlatformAndServerBVT](src/test/java/org/odpi/openmetadata/bvt/PlatformAndServerBVT.java) - platform
  origin check and server-is-active check.
* [CollectionClientBVT](src/test/java/org/odpi/openmetadata/bvt/CollectionClientBVT.java) - `CollectionClient`.
* [ProjectClientBVT](src/test/java/org/odpi/openmetadata/bvt/ProjectClientBVT.java) - `ProjectClient`.
* [AssetClientBVT](src/test/java/org/odpi/openmetadata/bvt/AssetClientBVT.java) - `AssetClient`.

The tests are self-contained: each one creates its own uniquely-named metadata element and deletes it
again at the end.

## Running it

The suite runs automatically as part of `./gradlew build` or `./gradlew test`.  To run just this module:

```bash
./gradlew :open-metadata-test:open-metadata-bvt:test
```

To skip it - for example when iterating quickly on an unrelated module - add `-PskipBvt`:

```bash
./gradlew build -PskipBvt
```

## Adding more connector context client tests

Each metadata-type test class follows the same shape: build a connector context with
`ConnectorContextFactory.newContext()`, get the client for the type under test off it (for example
`connectorContext.getGlossaryTermClient()`), then create, get, update, find and delete an instance,
asserting at each step.  Follow one of the existing `*ClientBVT` classes as a template.

----
* Return to [Open Metadata Test](..)
* Return to [Module Organization](../../Content-Organization.md)
* Return to [Home](../../index.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
