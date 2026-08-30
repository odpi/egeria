<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Digital Product Subscription FVT (subscription-fvt)

This is a Functional Verification Test (FVT) suite for the **Open Metadata Digital Product Catalog**.  It
follows one journey, the consumer's:

1. **locate a digital product** in the catalogue;
2. **find the subscriptions it offers**;
3. **subscribe to one of them** - and get the data delivered somewhere real.

Nothing in the suite creates a product.  The catalogue is built by the **Jacquard Digital Product Loom**
running in an integration daemon, from the definitions in `ProductDefinitionEnum`, and the tests assert
against those definitions rather than against a written-out list of names.  Adding a product, or a
subscription type, therefore extends this suite's coverage without anybody editing it - and adding one that
Jacquard cannot build fails here.

## Running it

Like the other FVTs, this suite is not part of the default build.  It needs a reachable PostgreSQL server and
an Apache Kafka broker, and it is only run deliberately:

```
./gradlew :open-metadata-test:open-metadata-fvt:subscription-fvt:test -PrunSubscriptionFvt
```

Where those servers are, which port the suite's own platform listens on, and how long it is prepared to wait
for a governance action are all set in [application.properties](src/test/resources/application.properties).
The defaults target the same shared PostgreSQL and Kafka containers the other suites use.

## What it stands up

| Server | What it is for |
|---|---|
| `subscriptionFvtMetadataStore` | Metadata access store with a PostgreSQL repository and its access services publishing to Kafka.  Loads the open metadata types and the Core, PostgreSQL and Open Metadata Digital Products content packs. |
| `subscriptionFvtIntegrationDaemon` | Integration daemon running `Egeria:IntegrationGroup:Jacquard` - where the catalogue comes from. |
| `subscriptionFvtEngineHost` | Engine host running the Egeria Governance engine (the create- and cancel-subscription services) and the Egeria Watchdog engine (the Baudot subscription manager). |

Kafka is genuinely required.  Neither governance server is told what to run: each asks the metadata access
store for its configuration at start-up and then listens on the access services' out topics for configuration
changes and for new engine actions.  Without a broker every server still starts, but an engine action sits at
`WAITING` for ever because no engine host hears about it.

**There is no view server.**  The Product Catalog OMVS - whose stated purpose is "searching a digital product
catalogue and subscribing to specific products and product families" - currently publishes no endpoints at
all: `ProductCatalogResource` is a registered, empty controller.  So the consumer's journey cannot yet be
driven through the API meant for it, and this suite drives the clients underneath instead.  That is worth
knowing when reading these tests: they show the catalogue *can* be navigated, not that a consumer has a
supported way to navigate it.

## How a subscription option is published

Jacquard publishes each subscription option as a **subscribing action process** - a governance action process
pre-loaded with everything about the product that does not depend on who is asking: the product, its data,
its license, its notification type, its owner and its service level objective.  The process is attached to the
product by a `ResourceList` relationship whose resource use is `CreateSubscription`, and that relationship is
the list a consumer reads.

Two things are left for the consumer to supply, and they are exactly the two that depend on who is asking:

| Action target | What it is |
|---|---|
| `digitalSubscriptionRequester` | who is subscribing |
| `destinationDataSet` | where the data is to be delivered |

## Where the data goes

A subscription delivers somewhere, so the suite catalogues real PostgreSQL objects as destinations from the
PostgreSQL content pack's own templates.  The two shapes are not interchangeable:

| Subscribing to | Destination | Catalogued as |
|---|---|---|
| a digital product | one table | tabular data set |
| a digital product family | one schema - room for a table per product | tabular data set collection |

Each subscription type gets its own destination schema, because a subscription is named after the destination
it delivers to; sharing one would leave four subscriptions whose names differ only by a timestamp.

## What it tests

| Test class | What it covers |
|---|---|
| [ProductCatalogFVT](src/test/java/org/odpi/openmetadata/subscriptionfvt/ProductCatalogFVT.java) | Jacquard built an entry for every product it defines, with the right type; a product can be found by name and by search; a family holds its member products |
| [SubscriptionOfferingsFVT](src/test/java/org/odpi/openmetadata/subscriptionfvt/SubscriptionOfferingsFVT.java) | a product offers an option for each subscription type it declares, each one a subscribing action process; the same for a product family; and a product with no data to deliver offers none |
| [ProductSubscriptionFVT](src/test/java/org/odpi/openmetadata/subscriptionfvt/ProductSubscriptionFVT.java) | subscribing to a product, run **once for each of the four subscription types**: the subscription is created, identifies its type, and records both its subscriber and its product |
| [ProductFamilySubscriptionFVT](src/test/java/org/odpi/openmetadata/subscriptionfvt/ProductFamilySubscriptionFVT.java) | subscribing to a family covers every product in it, through a nested subscription per member |

### Products with nothing to deliver

A digital product's data is produced by a connector, and 20 of the catalogue's 34 products name no connector
provider yet - they describe the data they would carry and have nothing to produce it, so `addProductAsset`
gives them no asset.  Those products offer no subscriptions, because a subscription to one could create the
agreement and then never deliver anything.

A product **family** also has no asset and *does* offer subscriptions.  The two cases look alike and are not:
a family's data is its members' data, and its subscription delivers by way of them.  That distinction is
asserted rather than left implicit, because it is one guard in Jacquard away from being lost.

### The catalogue is reused unless it has to be rebuilt

Jacquard reuses a catalogue it finds: a product already in the repository is not rebuilt, and neither are its
subscription options.  That is right for a running deployment and wrong for a suite that tests how the
catalogue gets built - a change would be invisible, and the tests that check what is *absent* would go on
passing against options that should no longer be offered.

Building it is also most of the suite's run time.  Jacquard creates 47 products and 181 notification types
and offers no way to ask for fewer, so a full rebuild takes tens of minutes and puts enough load on an
FVT-sized deployment - one engine host, one integration daemon, a shared PostgreSQL server - that runs start
failing for reasons that have nothing to do with subscriptions.

So the suite **reuses an existing catalogue by default**, and rebuilds only when it has to:

| Situation | What happens |
|---|---|
| Products are missing from the catalogue | Built by the refresh - a partial catalogue is never tested against |
| The catalogue is complete | Reused; Jacquard is still refreshed, which is what starts the subscription manager |
| `-Dsubscription.fvt.rebuild.catalogue=true` | Purged and rebuilt from scratch |

Jacquard is refreshed on every run even when nothing needs building, because refreshing is also what activates
the **Baudot Subscription Manager**.  A run that skips it has no subscription manager at all: subscriptions are
taken out and nothing ever delivers them.

That is a deliberate trade.  A run that reuses the catalogue is testing subscriptions, not catalogue
construction: Jacquard reuses what it finds, so a change to *how* products or their subscription options are
built is invisible until the catalogue is rebuilt.  **Ask for a rebuild after changing Jacquard**, and expect
that run to take considerably longer.

### Why each subscription type is tested separately

The four types are not variations on a theme.  An **evaluation** subscription delivers once and is never
repeated; **daily** and **weekly** deliver on a fixed interval; **ongoing update** delivers whenever the
source data changes.  Those differences are carried by the notification type behind each option, and a suite
that subscribed to only one of them would not notice if the other three had been wired to the wrong one.

## Reading a failure

Most of what this suite tests happens somewhere else: a test asks for a subscription and then looks at the
repository.  When an assertion fails, the reason is usually in the engine host's or the integration daemon's
audit log, written to `build/subscription-fvt-data/logs/audit.log` while the run is in progress.  The
`EngineActionWaiter` also distinguishes the three ways a governance action can fail to finish - never
claimed, still running, or failed and reported why - rather than reporting them all as a timeout.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
