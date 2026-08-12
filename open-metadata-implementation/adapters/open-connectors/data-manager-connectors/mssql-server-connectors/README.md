# Microsoft SQL Server Connectors

Connectors that catalog and survey a Microsoft SQL Server database server and its databases.

* The **MSSQLServerIntegrationConnector** catalogs the databases hosted by a SQL Server, creating the
  corresponding data assets, server capabilities and connections.  A friendship connector (typically the
  [JDBC Integration Connector](../../integration-connectors/jdbc-integration-connector)) can be attached to
  catalog inside each database - its schemas, tables and columns.
* The **MSSQLDatabaseSurveyActionService** and **MSSQLServerSurveyActionService** survey the databases, tables
  and columns found in a Microsoft SQL Server, extracting statistics via `MSSQLDatabaseStatsExtractor`.

Its Jar file includes the Microsoft SQL Server (mssql-jdbc) client driver.

See [](https://egeria-project.org/connectors/databases/mssql-database-server-survey-action-service/) for documentation.

----
Return to [data-manager-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


