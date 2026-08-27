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




----
Return to [open-metadata-test](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

