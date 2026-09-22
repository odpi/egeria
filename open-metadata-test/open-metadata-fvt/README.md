<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Egeria Functional Verification Test (FVT) Suite
  
Functional Verification Tests (FVTs) test multiple components together to
ensure they function correctly.   Each test uses a different technology stack
to ensure that we achieve good coverage of the components.

Every suite is **opt-in** and none of them run as part of an ordinary build. Most need a running technology
stack of their own - a PostgreSQL server, an Apache Kafka broker, or both; auth-fvt and server-fvt need
neither and run in well under a minute. Each is started by naming its property on the command line, shown
against each suite below.

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
  the folder and file survey services, the folder cataloguers, the file and folder governance actions, and the
  catalog templates the cataloguer chooses between. Built the same way as postgres-fvt - it stands up a
  metadata access store, an integration daemon, an engine host and a view server, and drives them through the
  **Automated Curation API** rather than calling any connector directly. It brings its own directory tree, so
  a survey of three files and a nested folder can be asserted against exactly, and a file catalogued as the
  wrong type is a failure rather than a detail nobody looked at.

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

* **[darwin-fvt](darwin-fvt)** - tests the **Darwin Product Dependency Manager**, the nanny connector that
  maintains the coarse-grained lineage implied by the finer-grained lineage beneath it: data flows between
  data assets from the data mappings between their schema elements, data flows between software servers from
  the lineage between the data assets their capabilities own, and `DigitalProductDependency` relationships
  between digital products from the lineage between their assets. The fixture is an archive the suite builds
  itself, so every relationship in it is owned by an external source - which is what exercises Darwin's
  handling of relationships asserted by external users, including the exceptions it records for the ones
  that lineage does not prove.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:darwin-fvt:test -PrunDarwinFvt
  ```

* **[platform-catalog-fvt](platform-catalog-fvt)** - tests the **OMAG Server Platform Cataloguer**, the Egeria
  system connector that keeps the open metadata description of an OMAG Server Platform in step with the
  platform's live state. The resource under test is the suite's own platform: it starts one in-process and
  lets the connector catalog it, which is how the connector is deployed in practice since it self-registers
  the local platform as a catalog target. Beyond the basics it covers the two things that go wrong in a real
  estate - an organization name set after the ecosystem has already been catalogued, and a second platform
  whose servers have the same names as the first's. Its README records what it found.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:platform-catalog-fvt:test -PrunPlatformCatalogFvt
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

* **[openlineage-fvt](openlineage-fvt)** - tests the **Open Lineage integration connectors** by publishing Open
  Lineage run events for a small pipeline into an integration daemon - through its REST API and through a Kafka
  topic attached to the Kafka listener - and checking that the cataloguer builds the processes, data assets,
  lineage, schema, run metrics, data scope and data quality reports they describe, and that the file publisher
  writes every event to its log store.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:openlineage-fvt:test -PrunOpenLineageFvt
  ```

* **[bitol-fvt](bitol-fvt)** - tests the **Bitol connectors** - the integration connectors that receive,
  catalog, generate and store [Bitol](https://bitol.io) Open Data Contract Standard (ODCS) and Open Data
  Product Standard (ODPS) documents - together with the **Bitol content pack** that defines them. Built the
  same way as files-fvt: it stands up the metadata access store and integration daemon the connectors are
  designed to run in and drives them through the integration daemon's REST API rather than calling any
  connector directly. Its round trip is the point - a document received is catalogued, and the document
  generated from the catalogue is compared against the one that went in.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:bitol-fvt:test -PrunBitolFvt
  ```

  A second mode carries the out topics over the in-memory topic connector, so that only PostgreSQL is needed:

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:bitol-fvt:test -PrunBitolFvtInMemory
  ```

* **[tabular-data-fvt](tabular-data-fvt)** - tests the **tabular data set connectors**, the connectors that
  turn open metadata into rows and columns so it can be handed out as a digital product. Everything
  downstream of them - the report generator, the provisioning governance action that copies a data set into a
  file or a database table - works from the shape the connector describes rather than from anything it knows
  about the data, so the suite asserts the shape: a table name, columns whose `getColumnNumber` agrees with
  the position they occupy, no duplicate column names, rows the width of the column list, and a read past
  the end that does not answer with a row. Coverage follows the product catalogue rather than a list -
  every product definition that names a connector provider is driven - so a data set added tomorrow is
  covered tomorrow. Its first run found rows one value too wide in the open metadata types data sets, and a
  column declared twice in the location data set; [its README](tabular-data-fvt) records both.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:tabular-data-fvt:test -PrunTabularDataFvt
  ```

* **[auth-fvt](auth-fvt)** - exercises the platform's own **authentication**: logging on, the bearer token
  that results, changing a password, and managing user accounts. It is the only suite that runs with
  `user-authn` wired in and the real Spring Security filter chain active - every other suite here, and the
  BVT, exclude it and run permit-all, which left the logon path with no automated coverage at all. It needs
  no database and loads no archives, so it runs in well under a minute.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:auth-fvt:test -PrunAuthFvt
  ```

* **[server-fvt](server-fvt)** - exercises the services that **configure, start, inspect and stop servers** -
  `admin-services`, `platform-services`, `server-operations`, `repository-services`, `engine-services`,
  `governance-server-services` and `user-security` - through their **Java clients**. Those clients are what
  the Runtime Manager API is being built on and they had almost no automated coverage, which is how several
  of them came to be addressing endpoints that no longer exist: the client and the controller share only a
  string literal, so nothing fails at compile time. It needs no database, no broker and no content packs, so
  it runs in well under a minute.

  The client defects it found have been fixed. Unlike the other suites here, some of its tests are still
  **expected to fail**: each asserts the behaviour the API should have, and what is left is server-side
  behaviour rather than a client defect. [Its README](server-fvt) lists both.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:server-fvt:test -PrunServerFvt
  ```

* **[security-fvt](security-fvt)** - exercises the **metadata-security** module: the authorization decisions
  that the platform, the servers on it and the generic handlers delegate to the open metadata security
  connectors. It is auth-fvt's companion - that suite asks *who are you?*, this one asks *what may you do?* -
  and it runs the Open Metadata Access Security Connector, the one shipped with the platform, as both the
  platform security connector and a server's security connector, against a generated user directory of
  accounts, roles, groups and governance zones. It covers the three platform roles, the server and service
  doors, the zone rules for reading, creating, updating and deleting elements, the dynamic groups built from
  account type, ownership and maintenance history, and access control management through the platform. It
  needs no database and loads no archives, so it runs in about a minute. Its first run found that the
  `instanceOwner` dynamic group admitted everyone, which has been fixed - [its README](security-fvt) records it.

  ```
  ./gradlew :open-metadata-test:open-metadata-fvt:security-fvt:test -PrunSecurityFvt
  ```



----
Return to [open-metadata-test](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

