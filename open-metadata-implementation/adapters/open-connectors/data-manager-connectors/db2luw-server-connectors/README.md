# Db2 for Linux, UNIX and Windows (LUW) Server Connectors

Connectors that catalog and survey an IBM Db2 for Linux, UNIX and Windows (LUW) database server and its databases.
Db2 for LUW was formerly known as Db2 UDB (Universal Database) - this connector suite covers that
distributed/open-systems implementation of Db2, not the Db2 for z/OS mainframe implementation, which has a
different system catalog and is out of scope here.

* The **DB2LUWServerIntegrationConnector** catalogs the databases hosted by a Db2 LUW server, creating the
  corresponding data assets, server capabilities and connections.  A friendship connector (typically the
  [JDBC Integration Connector](../../integration-connectors/jdbc-integration-connector)) can be attached to
  catalog inside each database - its schemas, tables and columns.
* The **DB2LUWDatabaseSurveyActionService** and **DB2LUWServerSurveyActionService** survey the databases, schemas,
  tables and columns found in a Db2 LUW server, extracting statistics via `DB2LUWDatabaseStatsExtractor`.

Its Jar file includes the IBM Data Server Driver for JDBC and SQLJ (`jcc`) client driver.

See [](https://egeria-project.org/connectors/databases/db2luw-database-server-survey-action-service/) for documentation.

----
Return to [data-manager-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
