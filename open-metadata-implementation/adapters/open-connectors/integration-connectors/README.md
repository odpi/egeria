<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Integration Connectors

The integration connectors are responsible for exchanging metadata with third
party technologies through the [Open Integration Framework (OIF)](../../../frameworks/open-integration-framework).
They run in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon)
OMAG Server.

Details of Egeria's integration connectors are found in the
[Connector Catalog](https://egeria-project.org/connectors/#integration-connectors)

* **[csv-lineage-import-integration-connector](csv-lineage-import-integration-connector)** loads lineage
  relationships between existing open metadata elements from a CSV file.
* **[files-integration-connectors](files-integration-connectors)** monitor changes in a file directory and
  update the open metadata repositories to reflect changes to the files and folders underneath it.
* **[jdbc-integration-connector](jdbc-integration-connector)** catalogs a database via JDBC, extracting
  catalogs, schemas and tables/views.
* **[kafka-audit-integration-connector](kafka-audit-integration-connector)** listens for Audit Log Records sent
  by an OMAG Server over Apache Kafka.
* **[openapi-integration-connector](openapi-integration-connector)** catalogs APIs by extracting their
  OpenAPI specification from the swagger endpoint.
* **[openlineage-integration-connectors](openlineage-integration-connectors)** exchange lineage metadata with
  tools that support the Open Lineage standard.
* **[smart-collections-integration-connector](smart-collections-integration-connector)** maintains the
  membership of Results Set collections based on a linked saved query.

----

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.