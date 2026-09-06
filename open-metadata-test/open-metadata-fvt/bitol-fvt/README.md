<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Bitol Connectors Functional Verification Tests (bitol-fvt)

This suite tests the **Bitol connectors** - the integration connectors that receive, catalog, generate and
store [Bitol](https://bitol.io) Open Data Contract Standard (ODCS) and Open Data Product Standard (ODPS)
documents - together with the **Bitol content pack** that defines them.

Like [files-fvt](../files-fvt), it does not call the connectors directly.  It stands up the deployment they are
designed to run in and drives them the way an operator would: through the integration daemon's REST API.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:bitol-fvt:test -PrunBitolFvt
```

This is the normal deployment: a PostgreSQL repository and access services publishing to a real Apache Kafka
broker, which is how the publisher connector hears about the products and agreements the cataloguers create.
Both servers are named in `src/test/resources/application.properties`; the defaults are the `egeria-shared-postgres`
(port 5442) and `egeria-shared-kafka` (external listener `oak.local:9194`) containers used elsewhere in this project.
The suite's repository schema, `repository_bitolFvtMetadataStore`, is dropped at the start of every run so the
repository always starts empty; the credentials come from the same secrets store the server is configured with.

A second mode carries the out topics over the in-memory topic connector, so that only PostgreSQL is needed:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:bitol-fvt:test -PrunBitolFvtInMemory
```

## What it stands up

Two servers on one in-process platform, on a port allocated at run time:

| Server | What it is | What it runs |
| --- | --- | --- |
| `bitolFvtMetadataStore` | Metadata access store | PostgreSQL local repository, Apache Kafka out topics; `OpenMetadataTypes`, `CoreContentPack` and `BitolContentPack` loaded at start-up |
| `bitolFvtIntegrationDaemon` | Integration daemon | `BitolIntegrationGroup`, where the six Bitol connectors run |

The connectors' default endpoints (`loading-bay/bitol` for the files receiver and `logs/bitol` for the file
store) are relative paths, resolved against this module's directory because that is the working directory of
the test JVM.  Both directories are emptied at the start of each run and are ignored by git.

## What it tests

* **[ContentPackFVT](src/test/java/org/odpi/openmetadata/bitolfvt/ContentPackFVT.java)** - the content pack
  loaded and the integration daemon started every connector it defines.  The expected contents come from the
  definitions in `core-content-pack`, so adding a connector to the pack extends the test without editing it.

* **[BitolRoundTripFVT](src/test/java/org/odpi/openmetadata/bitolfvt/BitolRoundTripFVT.java)** - publishes
  the Coco Pharmaceuticals clinical trial sample documents (from
  `open-metadata-resources/open-metadata-samples/sample-data/coco-clinical-trial-bitol-documents`) to the
  daemon's `publish-data-contract` and `publish-data-product` endpoints and checks that:
    1. each data contract becomes an `Agreement` classified as a `DataSharingAgreement` with a `DataStructure`
       per schema object, and the `DataContractGenerator` turns it back into a contract with the same identity,
       schema properties and access roles;
    2. the data product becomes a `DigitalProduct` in the `DigitalProductCatalog` for its domain, with a
       `SolutionComponent` carrying one `SolutionPort` per input and output port, and the `DataProductGenerator`
       turns it back into a product with the same ports and contract references;
    3. the file store wrote every document as `logs/bitol/{kind}/{id}/{version}.yaml`;
    4. attaching the product catalog to the publisher connector as a catalog target and refreshing it causes
       the product's document to be regenerated and rewritten by the store.

* **[KafkaReceiverFVT](src/test/java/org/odpi/openmetadata/bitolfvt/KafkaReceiverFVT.java)** - catalogs a Kafka
  topic from the Core content pack's Apache Kafka topic template, attaches it to the Bitol Event Receiver as a
  catalog target, produces a data contract onto the topic with a plain Kafka producer (as a third party would) and
  checks that the contract is catalogued and written to the store.  Skipped in the in-memory mode, which is the mode
  for machines without a broker.

## Diagnosing a failure

Both servers write their audit logs to `build/bitol-fvt-data/logs/audit.log` while the run is in progress.  A
document that is published but never catalogued is reported there by the cataloguer (`BITOL-INTEGRATION-CONNECTOR-0007`
records a mapping warning, `0008` a document without an identifier).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
