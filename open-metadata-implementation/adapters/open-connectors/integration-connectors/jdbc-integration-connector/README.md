<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# JDBC Integration Connector

Catalogs a database via JDBC, extracting catalogs, schemas and the following table types: "TABLE", "VIEW", "FOREIGN TABLE" and "MATERIALIZED VIEW". 
It will mark the primary key columns and extract the foreign key relationships.


The JDBC integration connector connects to a relational database and extracts its database schema information and catalogs it as open metadata.

![Figure 1](docs/jdbc-integration-connector.png)
> **Figure 1:** JDBC integration connector accessing a database and cataloguing its schemas in a metadata access server

It uses an embedded [JDBC Digital Resource Connector](../../data-store-connectors/jdbc-resource-connector) to access the database.

## Catalogued elements

The JDBC integration connector catalogs a database asset, database schema assets, tables, views, columns, primary and foreign keys.
(See [Open metadata types used to catalog a database](https://egeria-project.org/types/5/database) for more information)

If the endpoint information is available, it will also attach the connection information to access the database through the [JDBC Digital Resource Connector](https://egeria-project.org/connectors/resource/jdbc-resource-connector).

![Figure 2](docs/jdbc-integration-connector-connection-structure.png)
> **Figure 2:** Connection information attached to catalogued database enables consumers of the database to get access to the database contents

## Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/servers/by-server-type/configuring-an-integration-daemon).

```json linenums="1" hl_lines="14"
{
    "connection" : 
    {
        "class": "VirtualConnection",
        "connectorType" : 
        {
            "class": "ConnectorType",
            "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JdbcIntegrationConnectorProvider"
        },
        "embeddedConnections":
        [
            {
                "class" : "EmbeddedConnection",
                "embeddedConnection" :
                {
                    "class" : "Connection",
                    "userId" : " ... ",
                    "clearPassword" : " ... ",
                    "connectorType" : 
                    {
                        "class": "ConnectorType",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JdbcConnectorProvider"
                    },
                    "endpoint":
                    {
                        "class": "Endpoint",
                        "address" : " ... "
                    }
                }
            }
        ],
        "configurationProperties": 
        {
            "catalog" : " ... ",
            "includeSchemaNames": [],
            "excludeSchemaNames": [],
            "includeTableNames": [],
            "excludeTableNames": [],
            "includeViewNames": [],
            "excludeViewNames": [],
            "includeColumnNames": [],
            "excludeColumnNames": []
        }
    }
}
```

- `userId`: user
- `clearPassword`: password
- `address`: jdbc format address
- `catalog` (optional): null or missing means catalog will not be used during querying, empty string means objects that belong to no catalog will be queried, actual value means objects belonging to specified catalog will be queried
- `include/exclude` (optional): lists with database object names to filter out the import, no wildcards supported


## Implementation Notes

This connector aims to maximise the compatibility with as many JDBC supporting databases as possible, although most testing is done on PostgreSQL.

The use of null as a wildcard (or "no-filter") for catalog, schemaPattern, and tableNamePattern parameters in JDBC DatabaseMetaData methods is the standard behavior defined by the JDBC specification, and it is correct for almost all compliant JDBC drivers, not just PostgreSQL.

1. JDBC Specification Rules
   According to the JDBC API documentation for DatabaseMetaData:
   - null: Means that the parameter's value should not be used to narrow the search. It acts as a wildcard, returning all available results regardless of that attribute.
   - "" (Empty String): Specifically matches those items that have no catalog/schema. In many databases, an empty string literal search will return nothing because objects are almost always associated with some schema or catalog (even if it's "public", "dbo", or the user's name).
2. Why PostgreSQL was failing with ""
   In PostgreSQL:
   - Databases are mapped to JDBC catalogs.
   - Schemas (like public) are mapped to JDBC schemas.
   - If you pass schemaPattern = "" to getTables(), the driver looks for tables with literally no schema. Since all tables in PostgreSQL reside in a schema, it returns an empty result set.
   - Passing null tells the driver "return tables from all schemas."
3. Behavior in Other Databases
   Most enterprise databases follow the same logic:
   - Oracle: Uses null to see all schemas. Using "" will typically return nothing because every object belongs to a schema (user).
   - SQL Server: Catalogs map to databases and schemas map to owners (like dbo). null is the correct way to ignore these filters.
   - MySQL: Note that MySQL is a slight outlier—it often treats catalogs and schemas as synonymous. However, even in MySQL, null is the safest way to indicate "any".
4. Summary of Changes in Egeria
   The recent changes to JDBCIntegrationConnector aligned the implementation with the JDBC standard:
   - Initializing catalog to null instead of "" allows the connector to browse all catalogs (databases) available on the connection.
   - Passing null for schemaName when no schema is specified ensures that tables are retrieved from all schemas rather than searching for "schema-less" tables.
   
### Conclusion
Using null is the correct and portable approach for generic JDBC metadata retrieval. It ensures the widest compatibility across different database vendors by following the specification's definition of a non-narrowing parameter.
