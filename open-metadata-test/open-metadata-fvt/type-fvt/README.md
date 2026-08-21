<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Type Functional Verification Tests (type-fvt)

The type-fvt module is a functional verification test suite for the **open metadata type system itself**.
Where [query-fvt](../query-fvt) asks *"does the query surface behave correctly?"*, this suite asks
*"is every type in the model actually usable?"*

For every entity, classification and relationship type the server knows about, it:

* creates an instance with **every attribute that type declares** populated - inherited attributes
  included, and a value generated for each one appropriate to its data type,
* reads it back through the connector context and checks that every property survived the round trip,
* updates it (entities) and checks the update took without disturbing anything else, and
* removes it, and checks it is really gone.

Nothing is hand-listed. The type list comes from the running server, so **a type added to the model is
covered by the next run** without anyone remembering to add it here. A type that genuinely cannot be
instantiated belongs in `TypeCatalog`'s exclusion list, with a reason; a type that is simply broken fails
here, which is the point.

Everything is driven through the **connector context** - the same `ConnectorContextBase` and its
`OpenMetadataStore` / `OpenMetadataTypesClient` that the platform hands to a connector - so what is being
verified is the path real connector code takes, not a private back door.

Like query-fvt, this suite needs a reachable PostgreSQL server and is **not** part of the default build.
Unlike query-fvt it loads only the type archive, so its startup is quick; the time goes on the thousands
of create/read/delete round trips instead.

## Running it

```bash
./gradlew :open-metadata-test:open-metadata-fvt:type-fvt:test -PrunTypeFvt
```

Without `-PrunTypeFvt` the suite's `test` task is skipped, even by `./gradlew build` from the repo root,
so it is safe to leave registered in `settings.gradle` without slowing down every other build.

To work on one part of it:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:type-fvt:test -PrunTypeFvt --tests '*EntityTypeFVT*'
```

Each type is a separate JUnit test case named after the type, so a failure report names the type that
failed rather than just "something in the model is broken".

### Pointing it at your own PostgreSQL server

All environment-specific configuration - the platform's own port (9447, one above query-fvt's 9446 so the
two can run side by side) and which PostgreSQL server, database and secrets file the repository uses -
lives in [`src/test/resources/application.properties`](src/test/resources/application.properties), in its
`platform.placeholder.variables`. The default targets the `egeria-shared-postgres` container used
elsewhere in this project on `localhost:5442`, database `egeria`, using the `PostgreSQLRepository`
collection in
[`egeria-servers.omsecrets`](../../../open-metadata-resources/open-metadata-deployment/secrets/egeria-servers.omsecrets).

The repository's content lives in its own schema, `repository_typeFvtMetadataStore`, so it shares nothing
with query-fvt or anything else on that server.

No Kafka broker is needed - the server is configured with `configureAllAccessServicesNoTopics`.

## What it does

[OMAGPlatformExtension](src/test/java/org/odpi/openmetadata/typefvt/OMAGPlatformExtension.java) is a
JUnit 5 extension shared by every test class here. Once per run it starts the OMAG Server Platform
in-process, configures and starts a metadata access store server called `typeFvtMetadataStore` backed by
the PostgreSQL repository connector, loads `OpenMetadataTypes.omarchive`, and purges any debris left by an
earlier run.

**Only the type archive is loaded.** The subject under test is the type definitions, and every instance
this suite works with it creates itself, so the instance content in the other content packs would add
substantial load time without adding type coverage. (query-fvt, whose tests query pre-existing content,
does load the full set.)

### The four test classes

On the model as it stands that is **629 test cases** - 337 entity types, 200 relationships, 86
classifications and 6 whole-model checks - and a full run takes about two minutes against a local
PostgreSQL server.

| Class | One test case per | What it proves |
|---|---|---|
| [`TypeDefinitionFVT`](src/test/java/org/odpi/openmetadata/typefvt/TypeDefinitionFVT.java) | (six whole-model checks) | the type system is internally consistent before anything tries to use it |
| [`EntityTypeFVT`](src/test/java/org/odpi/openmetadata/typefvt/EntityTypeFVT.java) | entity type | an instance can be created with every declared attribute, read back intact, updated, and purged |
| [`ClassificationTypeFVT`](src/test/java/org/odpi/openmetadata/typefvt/ClassificationTypeFVT.java) | classification | it can be attached to one of its own declared valid entities, read back intact, and detached |
| [`RelationshipTypeFVT`](src/test/java/org/odpi/openmetadata/typefvt/RelationshipTypeFVT.java) | relationship | both ends can be created and linked, and the relationship's properties survive the round trip |

`TypeDefinitionFVT`'s checks are the ones that catch a model which compiles and loads but is quietly
wrong:

* every supertype named by a type resolves to a real type,
* both ends of every relationship, and every entity a classification says it can attach to, resolve,
* no two types share a GUID,
* **every `OpenMetadataType` enum constant names a type the server has, with the same GUID** - this is
  what keeps the Java view of the model and the archive from drifting apart, and it fails loudly rather
  than at some unrelated call site months later,
* looking a type up by name and by GUID gives the same answer, and
* every exclusion in `TypeCatalog` still names a real type, so exclusions cannot silently rot.

### How types and values are handled

[`TypeCatalog`](src/test/java/org/odpi/openmetadata/typefvt/TypeCatalog.java) reads the type list once per
run from the server, via the connector context's `OpenMetadataTypesClient`, rather than from the archive
file. That matters: a type's real shape is its `newTypeDefs` entry **merged with every later
`typeDefPatch`**, and it is the server that has already done that merging. Reading `newTypeDefs` alone
gives a stale supertype, attribute list and relationship ends for most of the interesting types. Asking
for `getInheritedAttributes = true` means each type arrives with its inherited attributes folded in.

Deprecated and renamed types, and deprecated and renamed *attributes*, are skipped. They are still in the
model so that existing instances keep working, but they are not what new instances should be using, and a
rename pair would otherwise be written twice.

[`TypeValueFactory`](src/test/java/org/odpi/openmetadata/typefvt/TypeValueFactory.java) generates a value
for each attribute from its data type - strings, the numeric primitives, booleans, dates, enums (choosing
from the enum's own element list, so it stays legal as the enum grows), string arrays, and maps **typed by
their declared value type**. That last point is worth spelling out: about a third of the map attributes in
the model hold ints, longs, booleans, dates or doubles rather than strings, and the repository will happily
store a string in any of them - so writing strings everywhere would pass while proving nothing about
whether a `map<string,int>` can actually carry ints.
Values are derived from the property name, so a failure message shows which property went wrong and what
it should have held, and so a test that swaps two properties over is still caught.

Round-trip comparison is done on `ElementProperties.getPropertiesAsStrings()`, comparing the properties
that were *sent* against the properties that came *back* - both rendered by the same code. This avoids
predicting how each property value class chooses to print itself, which would turn a formatting change
into a wave of false failures.

If the factory meets a data type it cannot write, the affected test fails with the attribute and data type
named. That is deliberate: it means the model has grown a data type nothing here - and quite possibly
nothing else in the codebase - knows how to populate, and it should be noticed rather than quietly
skipped.

## Does a new type need a new test?

No. The type list is read from the running server at the start of each run, so a type added to the model
becomes a new test case on the next run with no edit here. The same goes for a new attribute on an existing
type, a new enum, or new values added to an existing enum - the factory reads the enum's own element list
rather than hard-coding values.

There are two things that do need a hand, and in both cases the suite tells you so rather than quietly
skipping:

* **An attribute using a data type the model has never used before** - a new primitive category, or a
  struct. `TypeValueFactory` records it and the test fails naming the attribute and the data type it could
  not write. Add a branch to the factory. Failing loudly is deliberate: silently skipping the attribute
  would leave the type looking covered when it is not.
* **A type that genuinely cannot be instantiated** - one the platform manages itself, as `Anchors` is. Add
  it to the relevant list in `TypeCatalog` *with a reason*. `TypeDefinitionFVT` then checks that the
  exclusion still names a real type, so it cannot rot after the type is renamed or removed.

## Two behaviours worth knowing about

Both of these were found by writing this suite, and both are correct platform behaviour rather than bugs -
but each one silently breaks a test that does not expect it.

* **A purged element is reported as an unknown GUID, not as an empty result.** Looking one up throws
  `InvalidParameterException` with a 404 rather than returning null, so a "check it is really gone" step has
  to assert that the lookup is refused.
* **`Memento` hides its element from ordinary calls.** Attaching it makes the element invisible not just to
  reads but to the lookup that a *detach* call does first - so `forLineage` has to be set on the write
  options as well as the read options, or the element becomes impossible to clean up through the same path
  that classified it.

Two types are excluded from instance testing, each with its reason recorded in `TypeCatalog`:
`Anchors` (the platform sets it itself from the anchor options on a create call, and its properties are
GUIDs of other elements that the platform resolves), and nothing else at present. Four types - `Like`,
`Rating`, `SearchKeyword` and `TranslationDetail` - descend straight from `OpenMetadataRoot` and have no
`qualifiedName`; they are tested like everything else, they just cannot be found again by the
leftover-debris sweep.

## Cleaning up

Every element this suite creates has a qualified name starting with `type-fvt:`, and each test purges its
own elements in a `finally` block. Because the PostgreSQL repository persists between runs, the extension
also purges anything left over from an earlier, failed run before the first test starts - see
[`TypeFvtTestSupport`](src/test/java/org/odpi/openmetadata/typefvt/TypeFvtTestSupport.java).
