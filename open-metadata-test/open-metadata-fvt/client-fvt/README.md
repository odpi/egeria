<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector Context Client Functional Verification Tests (client-fvt)

The client-fvt module exercises the **connector context clients** - the typed clients
(`CollectionClient`, `AssetClient`, `ProjectClient`, ...) that the platform hands to a connector through its
`ConnectorContext`. Each is driven through its own create / retrieve / search / update / delete surface
against a real PostgreSQL-backed repository, on a server whose access services publish their out-topic
events to a real Apache Kafka broker.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:client-fvt:test -PrunClientFvt
```

Without `-PrunClientFvt` the suite is skipped, even by `./gradlew build` from the repo root.

## What it needs

More environment than its sibling suites: **query-fvt and type-fvt both run without Kafka, this one does
not.** It needs a reachable PostgreSQL server *and* a reachable Kafka broker, both configured in
[`src/test/resources/application.properties`](src/test/resources/application.properties).

The defaults target the `egeria-shared-postgres` and `egeria-shared-kafka` containers used elsewhere in this
project. One detail is worth knowing about the Kafka endpoint: it must be the broker's **EXTERNAL** advertised
listener (`oak.local:9194`), not the internal one (`egeria-shared-kafka:9192`). This suite runs on the host
rather than inside the docker network, and a client that connects on the internal listener is handed back an
address it cannot resolve.

The server is configured with an event bus and `configureAllAccessServices` - *with* topics - under the topic
root `egeria.omag.client-fvt`, unique to this suite so its events cannot be confused with anything else on
the same broker. Exercising the clients with the event infrastructure switched on is the point: a client call
that behaves correctly against a silent server but trips over event publication is exactly the kind of fault
this suite exists to surface.

The repository uses its own schema, `repository_clientFvtMetadataStore`, and the platform listens on port
9448 - one above type-fvt - so all three suites can run side by side.

## Keeping it up to date

**This suite has to be extended as the client interfaces grow.** Unlike type-fvt, whose coverage follows the
type system automatically, every client here has its own method names, its own properties bean and its own
idea of a sensible instance, so coverage cannot be derived.

[`ClientCatalog`](src/test/java/org/odpi/openmetadata/clientfvt/ClientCatalog.java) is the one file to edit.
Every client the connector context hands out must appear in it - either in the lifecycle list, or in the
not-yet-covered list **with a reason**.

[`ClientCoverageFVT`](src/test/java/org/odpi/openmetadata/clientfvt/ClientCoverageFVT.java) enforces that:

* a client on the connector context that the catalog does not mention **fails the run**, naming it, so a new
  client cannot be added without this suite noticing;
* a catalog entry naming a client that no longer exists also fails, so entries cannot rot;
* every client the catalog claims is under test must be obtainable from a live context, so a getter returning
  null cannot leave tests silently doing nothing.

What reflection *cannot* police is a new **method** on a client that is already listed. That still needs a new
assertion written by hand.

## What it covers today

**54 test cases, all passing, covering every one of the 51 clients the connector context hands out.**

| Class | Cases | What it does |
|---|---|---|
| [`ClientLifecycleFVT`](src/test/java/org/odpi/openmetadata/clientfvt/ClientLifecycleFVT.java) | 35 | drives each client with the standard lifecycle surface through create → getByGUID → getByName → find → update → delete |
| [`FeedbackClientFVT`](src/test/java/org/odpi/openmetadata/clientfvt/FeedbackClientFVT.java) | 6 | comments, likes, ratings, search keywords, property facets and note logs - attached to a host element, found on it, then removed |
| [`ReadAndReferenceClientFVT`](src/test/java/org/odpi/openmetadata/clientfvt/ReadAndReferenceClientFVT.java) | 7 | the generic store and types client, the classification explorer, lineage, information supply chains, specification properties and valid metadata values |
| [`AttachmentClientFVT`](src/test/java/org/odpi/openmetadata/clientfvt/AttachmentClientFVT.java) | 3 | contribution records, translations and the template classification |
| [`ClientCoverageFVT`](src/test/java/org/odpi/openmetadata/clientfvt/ClientCoverageFVT.java) | 3 | the catalog and the connector context agree, in both directions |

One case per client and element type, named after it, so a failure names the client that broke.
`NetworkClient` appears twice because it creates two element types.

The 35 share a uniform shape - `create<Stem>`, `get<Stem>ByGUID`, `get<Stem>sByName`, `find<Stem>s`,
`update<Stem>`, `delete<Stem>` - so
[`ClientExerciser`](src/test/java/org/odpi/openmetadata/clientfvt/ClientExerciser.java) drives all of them
rather than 35 near-identical test classes. Methods are bound by name and their arguments filled **by
parameter type, not position**: several clients take extra arguments the others do not (`ProjectClient` has an
additional classification name), and binding by type absorbs that instead of breaking on it.

### What the assertions do and do not prove

Worth being precise, because a green run should not be read as more than it is:

* **create** and **getByGUID** are strict - the element must be created and must come back.
* **update** is strict - it must complete and the element must still be retrievable afterwards.
* **getByName** and **find** check the call *executes*, not that it returns this run's element. Clients differ
  in which property their by-name search covers - most look at `qualifiedName`, but `InformalTagClient` and
  `AnnotationClient` search their own name properties - so a generic driver cannot assert a hit without
  knowing each client's search property. Asserting a by-name hit is worth doing and needs per-client tests.
* Only `qualifiedName`, `displayName` and `name` are set on each properties bean. Whether a type's own
  attributes round-trip is **type-fvt**'s job, and it already covers every attribute of every type.

### Findings

Three things this suite turned up while it was being written. None is a test problem, and all three are worth
knowing:

* **`TemplateClient.getTemplatesByName(...)` failed with a 500 - now fixed.** `TemplateHandler` built its
  search with a copy-paste fault: the block that should have populated the `resourceName` condition set its
  values on the `displayName` condition instead, leaving one condition completely empty (which the repository
  rejects, reported as a 500) and overwriting the only condition that searched `displayName`. The
  `resourceName` condition should not have been there at all - `resourceName` is an `Asset` property, not one
  of the four the Template classification declares. The search now covers `displayName`, `description` and
  `versionIdentifier` **on the classification**, which is what names a template: the entity's own properties
  describe what the template will produce and are typically placeholders. `AttachmentClientFVT` asserts the
  full round trip - classify, find by name, declassify, no longer found.
* **The multi-language operations were not implemented on the server - now implemented.** `setTranslation`,
  `clearTranslation`, `getTranslation` and `getTranslations` were `// todo` stubs that returned an empty
  response without touching the repository, so `setTranslation` reported success while storing nothing. They
  now store a `TranslationDetail` entity anchored to the element it translates and attached by a
  `TranslationLink`, with the element at end 1. `setTranslation` updates in place when a translation already
  exists for that language and locale rather than adding a duplicate, and the locale qualifies the match where
  an element carries several translations for one language - supply it and it must match, leave it null and a
  translation with no locale is preferred. `AttachmentClientFVT` covers all of that: two locales of the same
  language held independently, re-setting one updating rather than duplicating, and clearing one leaving the
  other alone.
* **`getTranslations` passed a request body to a GET - fixed.** Found by the test above once the service
  existed. `OpenMetadataClientBase.getTranslations` passed `new NullRequestBody()` into a varargs list whose
  arguments are the URL parameters, shifting every one along a place - the server saw `NullRequestBody{}` as
  the server name and the server name as the userId. A GET carries no body. No other GET call in that client
  makes the same mistake.
* **`ValidMetadataValuesClient` threw a `NullPointerException` without an audit log - now fixed.** The audit
  log is optional throughout the framework: `ConnectorContextBase` accepts null, and the handlers guard their
  logging with `if (auditLog != null)` - five of the ten call sites did so. `ValidMetadataValueHandler`'s four
  calls did not, so any caller without an audit log got an NPE instead of a valid metadata value. The same
  latent fault was in `OpenMetadataHandlerBase` - one unguarded `logException` in the shared base class every
  handler inherits, reached when an anchor cannot be retrieved during a zone check. All five now follow the
  house convention. This suite deliberately passes a **null** audit log, like query-fvt and type-fvt, so the
  guard is what the run exercises.

## Cleaning up

Every element this suite creates has a qualified name starting with `client-fvt:`, and each test deletes its
own in a `finally` block. Because the PostgreSQL repository persists between runs, the extension also purges
anything left by an earlier failed run before the first test starts.
