<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Tabular Data Set Functional Verification Tests (tabular-data-fvt)

This suite tests the **tabular data set connectors** — the connectors that turn open metadata into rows and
columns so it can be handed out as a digital product.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:tabular-data-fvt:test -PrunTabularDataFvt
```

It needs a reachable PostgreSQL server and nothing else: no Apache Kafka broker, because the access services
are configured without their out topics. Nothing in the suite consumes an event, so a broker would be a
dependency that bought it nothing.

## What it stands up

One metadata access store on an in-process platform, with a PostgreSQL local repository and
`OpenMetadataTypes` plus `CoreContentPack` loaded at start-up. The repository schema is dropped before the
server starts, so every run begins from the same place.

## What it tests

Everything downstream of these connectors — the report generator, the provisioning governance action that
copies a data set into a file or a database table — works from the shape the connector describes rather than
from anything it knows about the data. So the suite asserts the shape:

* **A table name.** It becomes the file name, or the database table name, wherever the data set is
  provisioned to.
* **Columns that agree with themselves.** `getColumnNumber` is how a consumer finds a value in a row, so it
  has to return the position that column actually occupies in `getColumnDescriptions`. A disagreement of one
  silently shifts every value into its neighbour's column: the data still arrives, and it is wrong.
* **No duplicate column names**, because a consumer looking a column up by name cannot tell which of two
  positions was meant.
* **Rows the width of the column list.** A row that is wider or narrower is a provisioned table whose values
  stop lining up with their headings.
* **An honest record count**, and a read past the end that does not answer with a row — so a consumer that
  miscounts gets an error rather than invented data.

It asserts on **shape rather than content**. What rows a data set has depends on what the repository holds,
and pinning that down would make this a test of the content packs rather than of the connectors. Shape holds
whatever the data is, including when there is none.

## Coverage follows the product catalogue

Every tabular data set Egeria publishes is a digital product, and
[`ProductDefinitionEnum`](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors)
is where those products are defined — each entry with an asset behind it names the connector provider that
serves the data. So the roster of connectors under test is simply "every product definition that names a
connector provider": a data set added to the catalogue tomorrow is covered tomorrow, with nothing to
remember. That is the same bargain [type-fvt](../type-fvt) makes with the type system, and it avoids the
hand-kept list that [client-fvt](../client-fvt) needs for its clients — where one case had been quietly lost
to a map key collision.

`TabularDataSetCatalog` records the two kinds of exception, each with its reason:

* **Not in the catalogue** — the *dynamic* data sets, where one connector serves many tables chosen at run
  time, so no single product definition names it. Their list connectors are in the catalogue and are covered,
  which is how the dynamic sets are reached.
* **Needs more than a connection** — `DigitalProductFamilyDataSetCollectionConnector` reads one digital
  product family, named in a configuration property. The families only exist once the Jacquard Digital
  Product Loom has built the catalogue, which [subscription-fvt](../subscription-fvt) stands up, so that is
  where this connector belongs.

## What its first run found

Both were the silent kind — a full, plausible, wrong table:

* **The open metadata types data sets returned rows one value too wide.** A stray
  `recordValues.add(...getStatus()...)` sat inside the `UPDATE_TIME` branch of
  `OpenMetadataTypesDataSetConnector.readRecord`, where it duplicated the value the status branch below
  already added. Every row of the Open Metadata Types List and the Open Metadata Data Types List carried 14
  values for 13 declared columns.
* **The location data set declared the same column twice.** `LOCATIONS` listed
  `ProductDataFieldDefinition.LOCATION_COORDINATES` in two consecutive positions, so a consumer looking up
  "Location Coordinates" by name got the first, and the second position held the same value under the same
  heading. The duplicate was removed; if a second location attribute was intended there, it needs a field
  definition of its own.

## A possible improvement

The suite needs PostgreSQL only because the harness it was built from does. Nothing here writes, so an
in-memory local repository would serve just as well and let the suite run anywhere, the way
[query-fvt](../query-fvt) offers both.

----
Return to [open-metadata-fvt](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
