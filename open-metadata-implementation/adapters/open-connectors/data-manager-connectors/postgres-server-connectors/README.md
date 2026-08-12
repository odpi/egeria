<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# PostgreSQL Server Connectors

Connectors that catalog and survey a PostgreSQL database server and its databases.

* The **PostgresServerIntegrationConnector** catalogs the databases hosted by a PostgreSQL server, creating the
  corresponding data assets, server capabilities and connections.  A friendship connector (typically the
  [JDBC Integration Connector](../../integration-connectors/jdbc-integration-connector)) can be attached to
  catalog inside each database - its schemas, tables and columns.
* The **PostgresDatabaseSurveyActionService** and **PostgresServerSurveyActionService** survey the databases,
  tables and columns found in a PostgreSQL database server, extracting statistics via `PostgresDatabaseStatsExtractor`.

Its Jar file includes the PostgreSQL client driver.

See [](https://egeria-project.org/connectors/databases/postgres-database-server-survey-action-service/) for documentation.

Sample REST API requests can be found in [Postgres-cataloguing.http](Postgres-cataloguing.http).

----
Return to [data-manager-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

