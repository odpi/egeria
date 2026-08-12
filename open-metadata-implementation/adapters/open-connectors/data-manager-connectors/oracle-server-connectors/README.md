# Oracle Server Connectors

Connectors that catalog and survey an Oracle Database Server and its pluggable databases.

* The **OracleServerIntegrationConnector** catalogs the pluggable databases hosted by an Oracle Database Server,
  creating the corresponding data assets, server capabilities and connections.  A friendship connector (typically
  the [JDBC Integration Connector](../../integration-connectors/jdbc-integration-connector)) can be attached to
  catalog inside each database - its schemas, tables and columns.
* The **OracleDatabaseSurveyActionService** and **OracleServerSurveyActionService** survey the pluggable
  databases, schemas, tables and columns found in an Oracle Database Server, extracting statistics via
  `OracleDatabaseStatsExtractor`.

Its Jar file includes the Oracle JDBC (ojdbc) client driver.

See [](https://egeria-project.org/connectors/databases/oracle-database-server-survey-action-service/) for documentation.

----
Return to [data-manager-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


