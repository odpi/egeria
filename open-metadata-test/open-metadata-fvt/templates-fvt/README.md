<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Templated Cataloguing Functional Verification Tests (templates-fvt)

Templated cataloguing is how a new element arrives already classified, already linked and already anchored -
which is what makes governance hold for elements that are catalogued automatically rather than by hand. This
suite verifies it.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:templates-fvt:test -PrunTemplatesFvt
```

It runs against a real PostgreSQL-backed repository with the full content-packs archive set loaded. **No
Kafka** - the access services are configured without topics. The platform listens on 9449, one above
client-fvt, so all four FVT suites can run side by side. Like the others it is opt-in and skipped by a normal
build.

Background: [templated cataloguing](https://egeria-project.org/features/templated-cataloguing/overview/) and
[model 0011](https://egeria-project.org/types/0/0011-Templates/).

## Part one - the templates in the content packs

[`ContentPackTemplateFVT`](src/test/java/org/odpi/openmetadata/templatesfvt/ContentPackTemplateFVT.java) uses
every shipped template for real, one test case per template so a failure names the template that is broken.

For each one it reads the placeholder specification the way a caller is expected to - the
`SpecificationPropertyAssignment` relationships attached to the template, taking those whose `propertyName` is
`placeholderProperty` - supplies a value for every placeholder, creates an element, and then checks the two
things that decide whether templating actually worked:

* **No placeholder survived.** The whole anchored graph is searched, not just the root: a template usually
  brings a cluster with it, and a marker left in a child is as broken as one left in the parent. Entity
  properties, classification properties and relationship properties are all covered, and a value is inspected
  as its string form so a placeholder buried in one entry of a map is still found.
* **`SourcedFrom` is correctly linked**, with the new element at end 1 and the template at end 2. That link is
  what answers "where did this element come from?" later.

Two guards keep the class honest: one asserts the content packs actually ship templates, so a discovery query
that returned nothing could not leave the parameterised test silently passing with no cases; the other reports
templates that use placeholders but declare none.

Where a template declares no specification, the suite falls back to reading the placeholders out of the
template's own property values so that it can still be exercised. That fallback is a convenience for the test,
not an endorsement - the missing specification is reported separately, because a caller following the
documented route has no way to discover what to supply.

## Part two - the mechanics

[`TemplateMechanicsFVT`](src/test/java/org/odpi/openmetadata/templatesfvt/TemplateMechanicsFVT.java) builds
templates of its own, so it controls exactly what is in them and can push harder than the shipped templates
do.

| Test | What it proves |
|---|---|
| `placeholdersAreSubstitutedEverywhereTheyAppear` | substitution reaches entity properties, **classification** properties and **relationship** properties - the last two are held separately from the element and are the easy ones to miss |
| `replacementPropertiesOverrideTheTemplate` | a replacement property overrides what the template holds, including a value that is not a placeholder at all, without disturbing unrelated substitution |
| `sourcedFromLinksTheNewElementToItsTemplate` | provenance is recorded, in the right direction |
| `templateSubstituteRedirectsToTheRealTemplate` | a `TemplateSubstitute` stands in for another element: the content comes from the real template behind it, and provenance is a chain - new element → substitute → real template |
| `aTemplateSubstituteWithNothingBehindItIsRefusedClearly` | a substitute with no `SourcedFrom` is refused with an error that explains itself |

**All five pass.**

### One design question, pinned down rather than decided

An element created through a substitute is linked by `SourcedFrom` to **the substitute**, not to the real
template its content came from. Both readings are defensible - the caller named the substitute, and the chain
is traversable either way - so the test asserts the chain that exists today rather than asserting a preference.
If the intent is that the new element should point straight at the real template, that is a deliberate change
and this test is where it would be made.

## Changing a content pack

The repository lives in a PostgreSQL schema that **persists between runs**, so a regenerated content pack is
loaded into a repository that already holds the previous version of its content.  That is meant to work: the
archive writer stamps each element with a `version` taken from the build time, the repository compares it with
the version it already stores, and the higher version wins.

It did not work, and finding out why was the most valuable thing this suite has done so far.  The repository
only compares versions once it has satisfied itself that the two elements really are the same element, and the
test it uses for that is equality of `createTime`
(`LocalOMRSInstanceEventProcessor.compareAndValidateReferenceInstance`).  Every element in a content pack takes
its `createTime` from the archive's creation date - and `ContentPackBaseArchiveWriter` set that creation date to
`new Date()`, so it moved with every build.  Rebuilt element and stored element therefore never matched, the
version comparison was skipped, and **the updated content was silently discarded**.  No error, no warning: the
archive loaded successfully and changed nothing.

This affected every deployment upgrading its content packs, not just this test suite - it is a release migration
bug, not a test inconvenience.  The fix was to give the content packs a fixed creation date, the way every other
archive in the repository already did, using the date the 6.0 content packs were released with so that
repositories loaded from 6.0 recognise later content packs as the same elements:

```java
private static final Date creationDate = new Date(1775025949989L); /* creation date of the Egeria 6.0 content packs */
```

Checked against the released 6.0 archives, element by element: of the 1566 elements that `CoreContentPack` has in
common between 6.0 and now, all 1566 carry a matching `createTime` and a higher `version` - the two conditions the
loader requires before it will apply an update.  Before the fix, none of them did.

`createTime` is now stable and `version` still moves with each build, which is the pairing the loader expects.
Regenerate the packs and re-run, and the change lands:

```bash
./gradlew :open-metadata-resources:open-metadata-archives:core-content-pack:run
```

The writers run with the repository root as their working directory (set on the `run` task in each archive
module) so that they find the `EgeriaContentPacksGUIDMap.json` that keeps element identities stable, and write
their archives straight into `content-packs/`.  Run from anywhere else they allocate fresh GUIDs for content
that has already shipped, and say nothing about it - so after regenerating, it is worth diffing entity GUIDs
against `HEAD` and checking that nothing was lost.

One transitional exception: a repository whose content packs were loaded *before* this fix holds elements
stamped with a moving `createTime`, so the first reload after upgrading still cannot match them.  (Egeria 6.1
shipped with the bug too and needs the same fix applied to its branch.)  Those repositories need their schema
dropped once:

```bash
docker exec egeria-shared-postgres psql -h 127.0.0.1 -p 5442 -U postgres -d egeria \
  -c 'DROP SCHEMA IF EXISTS "repository_templatesfvtmetadatastore" CASCADE;'
```

After that one drop, ordinary content pack updates apply on reload.

## What it found

The suite now passes - 82 tests, one per template plus the mechanics cases. Getting there meant fixing six
things in the shipped content, all found by running the templates the way a caller would.

**The database templates declared one description placeholder and used another.** The schema-level templates
(`~{databaseName}~.~{schemaName}~`) declare `schemaDescription` and the table-level ones
(`~{databaseName}~.~{schemaName}~.~{tableName}~`) declare `tableDescription`, but both set their description
from the generic `~{description}~`, which neither declares. Every element catalogued from them carried the
literal text `~{description}~` as its description. Nine template definitions across Postgres, MSSQL, Oracle
and DB2 LUW now use the placeholder they declare. The CSV templates were left alone - their specification does
declare `description`, so they were correct already.

**Seven placeholder specifications were missing variables their own templates use.** Every software server
template is written with `resourceName` set to `~{resourceName}~`, and the Egeria server templates set
`namespacePath` to `~{organizationName}~` - but several of the placeholder sets those templates declare listed
neither. A catalogued server ended up with `~{resourceName}~` sitting in its `resourceName`. The OMAG platform
template had a third variant: it uses `~{platformUserId}~` for its connection user, while its specification
declared `connectionUserId`, a variable the template never mentions. The real caller
(`OMAGServerPlatformCatalogConnector`) supplies `platformUserId` and `organizationName`, which confirms the
templates were right and the specifications had drifted. Fixed in `PlaceholderProperty`,
`OMAGServerPlatformPlaceholderProperty`, `UnityCatalogPlaceholderProperty` and `KafkaPlaceholderProperty`.

**`FileSystem` templates could not be used at all.** Three templates carried a *classification* named
`FileSystem`, and creating from any of them failed with "Classification FileSystem is not a recognized
classification type". `FileSystem` became an entity type (a subtype of `ResourceManager`) that defines
`format` and `encryption` as its own attributes, and these templates already *are* `FileSystem` entities - the
classification was a leftover from the older model. The archive helper now returns those values as entity
properties, and `getFileSystemClassification` is gone so it cannot be reached for again.

**The Unity Catalog storage-location folder was classified as a template.** It is an anchored part of the
table template, but it was handed its parent's classification list, `Template` included. That made it show up
as something a caller could create from, even though its placeholder specification and its anchor belong to
the table above it. It now gets its own classification list.

**Five templates carried no placeholder specification.** The Coco clinical trial supply chains and
certification types, the solution blueprint template and the core information supply chain template all embed
placeholders in their properties but attached no `placeholderProperty` specification, so nothing told a caller
what to supply. Each now attaches one where the `Template` classification is applied.

**A template substitute with nothing behind it failed unhelpfully.** `OpenMetadataAPITemplateHandler` followed
the `SourcedFrom` link from a template substitute without checking whether it found anything. A substitute with
no such link left a null travelling on until it surfaced as a null `entityOneGUID` on an `addRelationship`
call, reported as an internal error that said nothing about the real problem. It now fails with
`OMAG-GENERIC-HANDLERS-400-014`, naming the substitute and explaining that it has nothing behind it.

Two further failures turned out to be artefacts of how the suite chose its values, and are worth recording
because both produce convincing-looking failures about the product.

* A specification's documented *example* can name real shipped content - the clinical trial templates give
  `PROJ-CT-TBDF`, the identifier of the Teddy Bear Drop Foot trial that the Coco archives also catalogue in
  full. Using the example verbatim rebuilt the qualified name of that shipped element, so the create either
  failed as a duplicate or quietly matched it - and the test then purged content it had never created.
* Templates are not independent of one another. A template that has other templates as collection members
  brings copies of them along when it is used, built from the same placeholder values. The later case for one
  of those member templates then found the copy the earlier case had made, matched it instead of creating
  anything, and reported a missing `SourcedFrom` - which is exactly what the handler does when it matches
  rather than creates.

Both are handled by deriving each case's placeholder values from a marker unique to the template being used.

## Cleaning up

Every element this suite creates has a qualified name starting with `templates-fvt:`, and each test removes
what it created in a `finally` block, most recent first so anchored elements go before their anchors. Because
the PostgreSQL repository persists between runs, the extension also purges anything left by an earlier failed
run before the first test starts.
