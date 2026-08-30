<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Egeria Functional Verification Test (FVT) Suite
  
Functional Verification Tests (FVTs) test multiple components together to
ensure they function correctly.   Each test uses a different technology stack
to ensure that we achieve good coverage of the components.

Every suite is **opt-in**: each needs a running technology stack of its own - a PostgreSQL server, an Apache
Kafka broker, or both - so none of them run as part of an ordinary build. Each is started by naming its
property on the command line, shown against each suite below.

* **[query-fvt](query-fvt)** - gives the repository query surface a thorough workout against a real
  PostgreSQL repository: paging, sorting, subtype filtering, status (soft-delete) filtering, complex property
  and classification search, `asOfTime` historical queries, and `graphQueryDepth`.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:query-fvt:test -PrunQueryFvt
  ```

* **[type-fvt](type-fvt)** - tests the **open metadata type system itself**. Where query-fvt asks whether the
  query surface behaves correctly, this suite asks whether every type in the model is actually usable.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:type-fvt:test -PrunTypeFvt
  ```

* **[client-fvt](client-fvt)** - exercises the **connector context clients**, the typed clients
  (`CollectionClient`, `AssetClient`, `ProjectClient`, ...) that the platform hands to a connector through its
  `ConnectorContext`, each driven through its own create / retrieve / search / update / delete surface.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:client-fvt:test -PrunClientFvt
  ```

* **[templates-fvt](templates-fvt)** - verifies **templated cataloguing**: that a new element arrives already
  classified, already linked and already anchored, which is what makes governance hold for elements
  catalogued automatically rather than by hand.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:templates-fvt:test -PrunTemplatesFvt
  ```

* **[cts-fvt](cts-fvt)** - runs the [Open Metadata Conformance Suite](https://egeria-project.org/guides/cts/)
  against Egeria's own repositories. Unlike its sibling suites it does not test an API surface itself: it
  stands up the environment the conformance suite needs, lets the suite do the testing, and reports what the
  suite found. One repository per run, named on the command line, because a run certifies the repository it
  was pointed at.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:cts-fvt:test -PrunCtsFvtPostgres
  ./gradlew :open-metadata-test:open-metadata-fvt:cts-fvt:test -PrunCtsFvtInMemory
  ```

  It ships scoped to a small set of types so that an ordinary run is a check of a change rather than an
  overnight job. See [its README](cts-fvt) for what that scope covers and when to widen it.

* **[postgres-fvt](postgres-fvt)** - tests the **PostgreSQL connectors** and the **PostgreSQL content pack**
  that drives them. It does not call the connectors: it stands up the deployment they are designed to run in -
  a metadata access store, an integration daemon, an engine host and a view server - and drives them the way
  an operator would, by running the content pack's governance action processes through the **Automated
  Curation API**.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:postgres-fvt:test -PrunPostgresFvt
  ```

  A second mode runs the same tests with **no event bus at all**, which is how the suite shows that an engine
  host does not need one - it polls for the work it would otherwise be told about, and reacts a little later:

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:postgres-fvt:test -PrunPostgresFvtNoKafka
  ```

* **[files-fvt](files-fvt)** - tests the **file connectors** and the **Files content pack** that drives them:
  the folder and file survey services, the folder cataloguers, and the file provisioning actions. Built the
  same way as postgres-fvt - it stands up a metadata access store, an integration daemon, an engine host and a
  view server, and drives them through the **Automated Curation API** rather than calling any connector
  directly. It brings its own directory tree, so a survey of three files and a nested folder can be asserted
  against exactly.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:files-fvt:test -PrunFilesFvt
  ./gradlew :open-metadata-test:open-metadata-fvt:files-fvt:test -PrunFilesFvtNoKafka
  ```

  Its platform binds to an **ephemeral port** (`server.port=0`) rather than pinning one, so a run cannot
  collide with a dev platform, with a sibling suite, or with another checkout running this same suite.

* **[duplicate-fvt](duplicate-fvt)** - tests **duplicate management**: the repository handler combining
  confirmed duplicates on retrieval, the generic handler recording the duplicates it finds behind an
  ambiguous unique name, and the **Mendel Automated Duplicate Manager** that validates, retires and
  consolidates them. Duplicates cannot be created through the access services - they exist because something
  got past them - so the suite introduces them the way they arrive in the wild, through the repository layer.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:duplicate-fvt:test -PrunDuplicateFvt
  ```

* **[subscription-fvt](subscription-fvt)** - tests the **Open Metadata Digital Product Catalog** by following
  a consumer's journey through it: locate a digital product, find the subscriptions it offers, and subscribe
  to one of them. Nothing in the suite creates a product - the **Jacquard Digital Product Loom** builds the
  catalogue in an integration daemon, and the tests assert against the definitions Jacquard builds it from.
  Each of the four subscription types is taken out separately, and a product family is subscribed to as well
  as a single product, because a family subscription has to cover every product in the family.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:subscription-fvt:test -PrunSubscriptionFvt
  ```

* **[auth-fvt](auth-fvt)** - exercises the platform's own **authentication**: logging on, the bearer token
  that results, changing a password, and managing user accounts. It is the only suite that runs with
  `user-authn` wired in and the real Spring Security filter chain active - every other suite here, and the
  BVT, exclude it and run permit-all, which left the logon path with no automated coverage at all. It needs
  no database and loads no archives, so it runs in well under a minute.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:auth-fvt:test -PrunAuthFvt
  ```




----
Return to [open-metadata-test](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

