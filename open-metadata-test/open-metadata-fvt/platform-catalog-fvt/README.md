<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# OMAG Server Platform Cataloguer FVT (platform-catalog-fvt)

This is a Functional Verification Test (FVT) suite for the **OMAG Server Platform Cataloguer**, the Egeria
system integration connector that keeps the open metadata description of an OMAG Server Platform - the
platform itself, the servers it has run, their endpoints and configuration, and the users in its user
directory - in step with the platform's live state.

Unlike the other connector suites, the resource under test is the test platform itself.  The suite starts
one OMAG Server Platform in-process and lets the cataloguer catalog it, so the "third party technology" and
the deployment running the connector are the same thing.  That is also how the connector is deployed in
practice: it self-registers the local platform as a catalog target when it starts.

## Running it

Like the other FVTs, this suite is not part of the default build.  It needs a reachable PostgreSQL server
and an Apache Kafka broker, and it is only run deliberately:

```
./gradlew :open-metadata-test:open-metadata-fvt:platform-catalog-fvt:test -PrunPlatformCatalogFvt
```

Where those servers are, and what the platform calls itself, are set in
[application.properties](src/test/resources/application.properties).  The defaults target the
`egeria-shared-postgres` and `egeria-shared-kafka` containers used elsewhere in this project for local
testing.  The platform's own port is allocated at run time so that two checkouts can run the suite at once.

## What it stands up

| Server | What it is for |
|---|---|
| `platformCatalogFvtMetadataStore` | Metadata access store with a PostgreSQL repository and its access services publishing to Kafka.  Loads the open metadata types, the core content pack and the Egeria content pack, where the cataloguer's definitions live.  It is also one of the servers the cataloguer catalogs. |
| `platformCatalogFvtIntegrationDaemon` | Integration daemon running the Egeria content pack's `Egeria:IntegrationGroup:Egeria`, which is where the OMAG Server Platform Cataloguer runs.  It is also one of the servers the cataloguer catalogs. |

### There is no fixture

Nothing this suite asserts on is created by the suite.  It is all created by the connector, from a platform
whose port is allocated afresh on every run - so every element's name differs from run to run and last run's
debris cannot be found by name.  The suite therefore drops and recreates the repository's PostgreSQL schema
before the metadata access store opens it.  A run is meaningful only against an ecosystem that has never
been catalogued before, and this is how that is guaranteed.

### Waiting for the metadata store

`activateWithStoredConfig` returns once a server is active, which is not the same as being able to answer
queries: the start-up archives are still being read, and the core and Egeria content packs together are a
large read.  The suite therefore waits until the metadata access store can return the integration group the
daemon is about to be configured with before it starts the daemon.
<br>
This is not belt-and-braces.  The cataloguer's `start()` is the first thing that runs in the daemon, every
call it makes goes to that store, and its catch-all turns "server not available yet" into a connector that
has quietly registered no catalog target and will not try again until it is restarted.  Without the wait,
roughly one run in five failed that way.

### Two endpoints the suite has to supply

[PlatformCatalogFvtAboutController](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogFvtAboutController.java)
serves `/api/about` and `/api/public/app/info`, and
[build-info.properties](src/test/resources/META-INF/build-info.properties) supplies the build information
behind the first of them.  On a real platform both endpoints belong to the `user-authn` module, which this
suite excludes because it puts `anyRequest().authenticated()` in front of the whole platform.

This is not only a test-harness detail - see defect 6 below.

## What it covers

| Class | What it asks |
|---|---|
| [PlatformCatalogBasicsFVT](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogBasicsFVT.java) | Is the platform catalogued once, named from its own public properties?  Is every server the platform reports catalogued, attached to the platform, and qualified by the platform's URL root?  Does a second refresh change anything? |
| [PlatformCatalogOrganizationNameFVT](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogOrganizationNameFVT.java) | An organization name is set on a server's configuration document after that server has already been catalogued.  Is the element that is already there renamed, or is a second one created beside it?  And does clearing the organization name again put it back? |
| [PlatformCatalogMultiplePlatformsFVT](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogMultiplePlatformsFVT.java) | Two platforms, with identically named servers behind both.  Are they catalogued as two platforms with two sets of servers?  Does restarting the connector catalog its own platform a second time?  And is a platform element left under an earlier release's name adopted rather than catalogued again? |
| [PlatformCatalogLocalServerURLFVT](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogLocalServerURLFVT.java) | The integration daemon is on a different platform from the metadata access store it writes to.  Is the daemon's own platform catalogued and taken under management as well as the store's - and without registering the same platform twice when the two coincide? |
| [PlatformCatalogCohortFVT](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogCohortFVT.java) | Two real platforms, each with its own metadata access store and its own instance of the connector, the two stores joined in one cohort so every query is federated across both.  Both platforms carry identical names.  Do the two instances of the connector clash? |

### How two platforms are staged

A second OMAG Server Platform cannot be started in the same JVM - the platform keeps its live servers in
static registries, so a second Spring context would share the first's servers rather than have its own.
Instead the suite catalogs the one running platform twice, through two `SoftwareServerPlatform` elements
that reach it at two spellings of its address (`http://localhost:port` and `http://127.0.0.1:port`).

That reproduces the part of the real situation the connector's naming conventions have to cope with: two
catalog targets at two addresses, with servers of exactly the same names behind both.  It does not
reproduce two platforms with genuinely different configurations, and the tests stay away from anything that
would depend on that.

### The two-platform test

[PlatformCatalogCohortFVT](src/test/java/org/odpi/openmetadata/platformcatalogfvt/PlatformCatalogCohortFVT.java)
is the only place in this repository where two OMAG Server Platforms run at once.  It starts the second one
in a JVM of its own - a platform keeps its live servers in static registries, so two cannot share a JVM -
using the same classpath the test worker has, handed over by Gradle, and its own
[platform-b.properties](src/test/resources/platform-b.properties).  Each platform gets a metadata access
store with its own PostgreSQL repository and an integration daemon running its own instance of the
cataloguer, and the two stores join one open metadata repository cohort.

The two platforms are **deliberately indistinguishable by name**: same `platform.name`, same
`platform.organization.name`.  That is what two installations look like when nobody has named them, since
the shipped defaults are "Development OMAG Server Platform" and an empty organization.  A null organization
name on both is the same case, because the names the connector builds are made from each platform's
address and neither value enters into them.

**The answer: they do not clash.**  Each platform is catalogued once, under a qualified name built from its
own address; each platform's servers are attached to it and named after it; and every element is homed in
exactly one of the two repositories, with the other holding a reference copy.  The suite checks that last
point by reading each store separately rather than through the federated view, because a federated read
deduplicates on the way out and would hide two competing copies.

**They did, however, both start out doing all the work.**  The two daemons run the same integration
connector *definition* from the content pack, so both ask open metadata the same question - "what are the
catalog targets of connector `dee84e6e-...`?" - and through the cohort that question is answered from every
repository at once.  Each connector found the other's platforms alongside its own and maintained all of
them: in an estate of N platforms each running the cataloguer, every platform was refreshed N times a cycle
instead of once, and every daemon needed to reach - and hold credentials for - every other platform.

What kept that merely wasteful rather than harmful is the naming.  Every instance computes the same names
from the same addresses, so they converged on the same elements instead of competing for them.  Before the
naming was fixed they would have competed: two identically named platforms both wanted one qualified name,
and with a federated view each connector would have found the other's element and tried to make it its own.

**The catalog targets are now filtered by the identifier of the metadata collection that homes them**, in
`RequestedCatalogTargetsManager` - so this applies to every dynamic integration connector, not only this
one.  A daemon refreshes the targets registered through its own metadata access store and leaves the rest
to the daemons that registered them, reporting what it left as `OIF-CONNECTOR-0024`.  In this suite the
second platform's connector leaves three targets to the first and refreshes one, its own.

Identifiers, not names.  A metadata collection's name is optional and can be set to anything, so it cannot
be used to decide whether a connector should be doing any work.  But nothing hands a connector the
identifier of the store it writes through either, and the obvious server-side source is wrong: the
repository connector an access service holds is the *enterprise* one, whose identifier is the federated
collection rather than the local repository's.

So the connector reads it off an instance it is certain it created - its own catalog target relationship,
written through its own metadata access store and therefore homed in that store's metadata collection - and
hands it to the framework through `setLocalMetadataCollectionId`.  Until a connector supplies it nothing is
filtered at all, which is the safe default: a guess about which targets belong to somebody else, made
wrong, silences a connector completely.

When every target really does belong elsewhere, they are all left alone and `OIF-CONNECTOR-0025` says so -
because a connector with nothing to refresh and a connector that has stopped refreshing look identical from
outside.

### What is not covered

**The `UserIdentity` and `Exception` handling in `catalogUsers`.**  That code only runs when the platform
report carries a platform security connection, which means configuring a platform security provider and a
user directory.  Doing that here means bringing `user-authn` back in, which puts the whole platform behind
a bearer token - including the cataloguer's own REST calls, whose token would have to be fetched from a
`tokenAPI` pointing at a port that is not known until the suite starts.  It is a worthwhile suite; it is a
different one, closer in shape to [auth-fvt](../auth-fvt) than to this.

**The platform's own organization name after cataloguing.**  `platform.organization.name` is fixed when the
Spring context starts, so changing it means restarting the platform, which a single-platform suite cannot
do.  What can be asserted about it - that it reaches the platform element's resource name, and that the
element's qualified name is the address-based one the connector will look it up by next time - is covered
by `PlatformCatalogBasicsFVT`.

## What it found

The first run of this suite failed 4 of its 12 tests, and every one of them was a defect in the connector.
They have been fixed; what follows is the record of what they were, because the tests that now pass are
only meaningful if it is clear what they are guarding against.  The failures came from two root causes.

### Root cause A - the platform element was renamed away from the name it is looked up by

`OMAGServerPlatformCatalogConnector.start()` creates the local platform element with the qualified name
`OMAG Server Platform::<platformURLRoot>`, relying on the template call's "retrieve if it already exists"
behaviour to find the element a previous start created.  `updatePlatform` then replaced that name with
`OMAG Server Platform::<organizationName>::<platformName>`, which contains nothing platform-specific.

1. **Restarting the connector catalogued the platform again.**  The next `start()` looked for the URL-based
   name, did not find it, and created a second platform element.  This happened on every platform restart,
   every redeployment and every restart of the integration daemon - and the suite saw it happen without
   asking, because the daemon calls `start()` more than once bringing the connector up.
   *(`restartingTheConnectorDoesNotDuplicateThePlatform`)*

2. **Two platforms wanted the same qualified name.**  Built only from `platform.name` and
   `platform.organization.name`, two platforms that have not been given distinctive names collided - and
   that is what two default installations look like, since `platform.name` ships as "Development OMAG
   Server Platform" and `platform.organization.name` ships empty.  The repository accepted the duplicate
   silently.  *(`secondPlatformIsCataloguedSeparately`)*

3. **The half-built duplicate kept a placeholder as its resource name.**  It carried
   `resourceName=~{resourceName}~`, because `catalogPlatform` supplied no `resourceName` placeholder value.

**The fix.**  `updatePlatform` no longer writes the qualified name.  The address-based name `start()`
creates is left alone, so it stays unique per platform and stays findable on the next start.  The
platform's configured name and organization are not lost - they were always the better fit for the
resource name and display name, which is where they now go, and a test asserts they arrive there.
`catalogPlatform` also supplies a `resourceName` placeholder so a freshly created element is never left
carrying the literal placeholder text.

**Upgrading.**  An ecosystem catalogued by an earlier release has its platform elements under the old
names, so the first start after this change would not find them.  `start()` therefore adopts them: before
creating anything it looks for a platform element whose additional properties record this platform's
address - which every refresh writes, and which no renaming touched - and puts its qualified name right.
The correction is recorded once as `OMAG-CONNECTORS-0013`, and the element keeps its servers, its
endpoints and its history.  Nothing has to be tidied up by hand.
*(`platformCataloguedUnderAnOlderNameIsAdopted`)*

### Root cause B - a server was qualified by its platform, but everything hanging off it was not

`catalogServer` gave the server element a qualified name containing the platform's URL root, which is what
keeps identically named servers on different platforms apart.  Every element the template created beneath
it was named from the server type and server name alone:

```
Metadata Access Store::http://localhost:59419::platformCatalogFvtMetadataStore   <- the server, qualified
Metadata Access Server::platformCatalogFvtMetadataStore::Open Metadata Repository Access APIs
Metadata Access Server::platformCatalogFvtMetadataStore::SecretsStoreConnection
Metadata Access Server::platformCatalogFvtMetadataStore::SecretStoreEndpoint
Metadata Access Server::platformCatalogFvtMetadataStore:Connection
Metadata Access Server::platformCatalogFvtMetadataStore:Endpoint
```

So whenever `catalogServer` was asked to create a server whose name had been used before - on another
platform, or on the same platform under a different organization name - the server element was fine and
the capability collided, and the exception ended that whole refresh pass.

4. **Setting an organization name after cataloguing stopped the server being maintained.**  The cataloguer
   matched already-catalogued servers by resource name, and the organization name is part of that resource
   name.  Change it and the server was no longer recognised, so `catalogServer` ran and failed on the
   capability name.  The element that was already there was never renamed and never updated again.  There
   was no duplicate, which is almost worse: nothing in the catalog showed that anything had gone wrong.
   *(`settingTheOrganizationNameRenamesTheServer`)*

5. **A server could be left catalogued but not attached to its platform.**  `catalogServer` and the
   `deployITAsset` that links the server to the platform are separate steps in the same loop iteration, and
   the whole of `refresh()` was inside one `try`.  A collision while cataloguing one server abandoned the
   rest of the pass, so a server element could exist with nothing joining it to the platform it runs on.
   *(`serversAreCatalogued`)*

**The fix.**  Three changes, in `EgeriaSoftwareServerTemplateDefinition` and the connector:

* the server templates' qualified name now carries the platform's network address, so everything created
  beneath a server is unique to the platform it runs on.  This changes the content packs, and they have
  been regenerated - additively, with no existing element's GUID reallocated;
* the connector's own server qualified name no longer folds in the organization name, which makes it
  stable over the life of a server and identical to the name its template children are built from.  The
  organization name stays in the resource name and display name, where a changeable label belongs;
* a catalogued server is recognised by its `identifier`, which holds the server's own name, rather than by
  its resource name.  A platform does not run two servers of the same name, and the name is the one thing
  about a server an administrator cannot change without creating a different server.

Each server is also catalogued inside its own `try`, so one server that cannot be catalogued no longer
takes the rest of the platform with it.  It is reported as `OMAG-CONNECTORS-0012` and the pass continues.

### Two further defects, found while running the suite

6. **A platform with no `/api/about` could not be catalogued at all.**  `getPlatformReport()` called
   `getPlatformBuildProperties()` and `getPublicProperties()` unguarded, and both endpoints live in the
   `user-authn` module.  A 404 from either propagated out of the report and `refresh()`'s catch-all
   swallowed it, so nothing whatsoever was catalogued - not the platform, not its servers, not its users -
   and the only trace was one `OMAG-CONNECTORS-0001` audit record.  `updatePlatform` then dereferenced
   `getPlatformBuildProperties().getVersion()` with no null check, so a platform that had the endpoint but
   no Spring Boot build information behind it failed the same way.

   **The fix.**  `EgeriaExtractor.getPlatformReport()` treats both as optional, and the connector null-checks
   what it gets.  A caller that cannot learn the platform's name or version is far better off than one that
   learns nothing at all.  This suite still supplies both endpoints, so that the ordinary case - where they
   are there - is the one being tested.

7. **`EGERIA_TARGET_START` was passed two parameters for a one-parameter message.**  Every start of a
   catalog target logged `Too many parameters for message The monitoring of OMAG Server Platform {0} has
   started` instead of the message.  The message now takes both.

### One thing the suite reports that is not a defect

The second platform's metadata access store is skipped, with an `OMAG-CONNECTORS-0012`, because its
`MetadataCollection` element collides.  That is an artefact of how two platforms are staged here: both
catalog targets point at the same running platform, so the same metadata access store appears behind both
with the same metadata collection id - and a metadata collection genuinely can only be catalogued once.
Two real platforms have different collection ids.  It is left in rather than worked around, because it is a
fair demonstration of the per-server isolation doing its job: one server is skipped with a clear audit
record, and the rest of the platform is catalogued as normal.

## What it does not attempt

**A genuinely separate second platform.**  `PlatformCatalogLocalServerURLFVT` puts the connector into a
split deployment by giving the integration daemon a different spelling of the one platform's address, which
is enough to exercise the registration but is still one platform with one set of servers.  A suite that
could show two platforms with their own servers and their own configurations would need two JVMs.

**A registration that fails and is then retried.**  The connector retries a failed registration on its next
refresh, and the mechanism is small - a platform missing from the connector's record of what it has
registered is tried again.  Staging a realistic failure is not: the obvious way, stopping the metadata
access store, also stops the integration daemon from starting the connector at all, because the daemon
learns about its integration group from that same store.  The retry is covered by reading it rather than by
running it.

## Two further fixes the suite covers

**The connector's own platform in a split deployment.**  `start()` used to catalog
`getMetadataAccessServerPlatformURLRoot()` and nothing else - the platform hosting the metadata access
store.  In the ordinary deployment that is also the platform the connector runs on; where it is not, the
connector's own platform went uncatalogued unless somebody registered it by hand, and the connector had no
way of knowing its own address.  `getLocalServerURL()` now carries it, plumbed from the local server's
configured `localServerURL` through the integration daemon's operational services and context manager.  The
connector always catalogs the metadata access store's platform, and also catalogs its own whenever
`localServerURL` is set and different.

**The lineage check reached across the whole ecosystem.**  `checkServerLineage()` worked through every
`SoftwareServer` in open metadata and opened a connector to each one.  In an estate of any size that is
wrong twice over: a connector reached into servers on platforms it has no responsibility for, and every
platform's copy of the connector repeated all the others' work - including to servers that had stopped
running, each costing a refused connection.  It now works through the servers on its own catalog target's
platform, gathered as they are catalogued during the refresh.

## Reading a failure

Most of what this suite tests happens somewhere else: a test asks for a refresh and then looks at the
repository.  When an assertion fails, the reason is usually in the integration daemon's audit log, which is
written to `build/platform-catalog-fvt-data/logs/audit.log` while the run is in progress, and in full to the
console output Gradle captures.  The messages to look for:

| Message | Meaning |
|---|---|
| `OMAG-CONNECTORS-0002` | The cataloguer started, and the platforms it already knows about |
| `OMAG-CONNECTORS-0011` | A refresh has begun on one catalog target |
| `OMAG-CONNECTORS-0005` | A new server element was created |
| `OMAG-CONNECTORS-0012` | One server could not be catalogued; the rest of the platform carried on |
| `OMAG-CONNECTORS-0013` | A platform element left under an earlier release's name was adopted and renamed |
| `OMAG-CONNECTORS-0014` | A platform was taken under management as a catalog target |
| `OMAG-CONNECTORS-0015` | A platform could not be registered yet; the next refresh tries again |
| `OMAG-CONNECTORS-0001` | A refresh pass was abandoned - this is the one that matters, and it is the connector's only report of anything going wrong |

Because `refresh()` wraps everything in a single catch-all, an `OMAG-CONNECTORS-0001` means every step after
it in that pass did not happen.  A test that fails with "not attached to the platform element" or "was not
renamed" is nearly always downstream of one of these.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
