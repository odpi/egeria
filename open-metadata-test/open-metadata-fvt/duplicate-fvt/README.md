<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Duplicate Management FVT (duplicate-fvt)

This is a Functional Verification Test (FVT) suite for Egeria's [duplicate
management](https://egeria-project.org/features/duplicate-management/overview/).  It covers the three pieces
that make up the loop:

* the **repository handler**, which combines confirmed duplicates on the way out of the repository so that
  ordinary callers see one element;
* the **generic handler**, which records the duplicates it finds when a lookup by a name that should be
  unique turns up more than one element;
* the **Mendel Automated Duplicate Manager**, the integration connector that manages the links and
  classifications the other two depend on.

It also covers `forDuplicateProcessing` - the switch that turns the deduplication off, which is how a
steward's tooling sees what is really in the repository.

## Running it

Like the other FVTs, this suite is not part of the default build.  It needs a reachable PostgreSQL server and
an Apache Kafka broker, and it is only run deliberately:

```
./gradlew :open-metadata-test:open-metadata-fvt:duplicate-fvt:test -PrunDuplicateFvt
```

Where those servers are, which port the suite's own platform listens on, and how long it is prepared to wait
for the integration daemon are all set in [application.properties](src/test/resources/application.properties).
The defaults target the `egeria-shared-postgres` and `egeria-shared-kafka` containers used elsewhere in this
project for local testing.

Kafka is genuinely required here, unlike in `query-fvt`: the integration daemon learns about its
configuration from the access services' out topics, and Mendel registers a listener on the OMF out topic once
it has worked through its first refresh.

## What it stands up

| Server | What it is for |
|---|---|
| `duplicateFvtMetadataStore` | Metadata access store with a PostgreSQL repository and its access services publishing to Kafka.  Loads the open metadata types, the core content pack (where Mendel's definitions live) and this suite's duplicate archive. |
| `duplicateFvtIntegrationDaemon` | Integration daemon running the core content pack's `Egeria:IntegrationGroup:Mendel`, which is where the Mendel Automated Duplicate Manager runs. |

## Where the duplicates come from

Duplicates cannot be created through the access services - they exist precisely because something got past
them.  This suite introduces them the way they arrive in the wild: through the repository layer, by loading an
open metadata archive whose entities deliberately share a qualified name.
[DuplicateArchiveWriter](src/test/java/org/odpi/openmetadata/duplicatefvt/DuplicateArchiveWriter.java) builds
that archive at test time, so the fixture is deterministic and no binary test data is checked in.

The archive holds seven sets, each aimed at a different part of duplicate management:

| Set | Contents | What it exercises |
|---|---|---|
| 1 - undetected | Two elements sharing a qualified name, nothing linking them | The generic handler recording a duplicate it could not choose between |
| 2 - close match | The same, plus a `DISCOVERED` `PeerDuplicateLink` | Mendel validating a close match on its own authority |
| 3 - distant match | Two elements with *different* qualified names and a `DISCOVERED` link | Mendel referring the decision to a steward |
| 4 - validated cluster | Three elements sharing a qualified name, `VALIDATED` links, `KnownDuplicate` on each | Mendel consolidating a cluster that has reached the configured size |
| 5 - retired | Two `KnownDuplicate` elements whose only link is `DEPRECATED` | Mendel taking the classifications back off |
| 6 - small cluster | Two elements, validated and classified - below the cluster size | The repository handler combining them; Mendel leaving them alone |
| 7 - untouched | Two elements sharing a qualified name that no test ever looks up | That duplicates are combined only once something confirms them |

Sets 6 and 7 exist so the retrieval tests have fixtures that nothing else rewrites underneath them.  Set 6
doubles as the test of the cluster size threshold.  Set 7 is deliberately identical in shape to set 1 but is
never looked up by name: set 1 is driven through detection, validation and combination by the detection
tests, so an assertion that it stays unmanaged would pass or fail on the order the test classes happened to
run in.

## How the tests control Mendel

Mendel's refresh interval is 24 hours, so the tests drive it explicitly through the integration daemon's
`refreshConnector` API rather than waiting.  The refresh is synchronous: when the call returns, all three of
its passes over the fixture are complete.

## The real-world version of this fixture

Between the 6.0 and 6.1 releases a large number of content pack elements were given new unique identifiers
while keeping their qualified names - so a repository that has loaded both releases' archives holds two copies
of each of them, the same type and qualified name with different identifiers.  That is exactly the shape of
duplicate Mendel's close match rule is built to recognise, and nobody created it deliberately.  In the core
content pack alone, **874 elements** differ this way between the two releases.

That population is measured by the
[content-pack-duplicate-report](../../../open-metadata-resources/open-metadata-dev-utilities/content-pack-duplicate-report)
utility rather than by this suite - it is a question about two archive files, not about a running server, and
the archives are far too large to check in (about 30MB each).

This suite works from the generated fixture instead, which is small, deterministic and lets the assertions be
exact.  What is **not** covered anywhere yet is loading two real release archives into a repository and
letting detection and Mendel work through all 874 pairs: that would prove duplicate management copes at
scale, and it is the natural next test to write.

## Reading a failure

Most of what this suite tests happens somewhere else: a test asks for a refresh and then looks at the
repository.  When an assertion fails, the reason is usually in the integration daemon's audit log, which is
written to `build/duplicate-fvt-data/logs/audit.log` while the run is in progress.  The messages to look for:

| Message | Meaning |
|---|---|
| `MENDEL-DUPLICATE-MANAGER-0002` | Mendel started, and the cluster size it is using |
| `MENDEL-DUPLICATE-MANAGER-0004` | A duplicate link was validated |
| `MENDEL-DUPLICATE-MANAGER-0005` | A to do was raised for a steward |
| `MENDEL-DUPLICATE-MANAGER-0006` | A `KnownDuplicate` classification was retired |
| `MENDEL-DUPLICATE-MANAGER-0007` | A cluster was consolidated |
| `MENDEL-DUPLICATE-MANAGER-0009` | Mendel registered its event listener |
| `OMAG-GENERIC-HANDLERS-0029` | The generic handler recorded duplicates it found |

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
