<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# JDBC Integration Connector

Catalogs a database via JDBC, extracting catalogs, schemas and the following table types: "TABLE", "VIEW", "FOREIGN TABLE" and "MATERIALIZED VIEW". 
It will mark the primary key columns and extract the foreign key relationships.


### Configuration

For more details regarding bellow request, visit [Egeria documentation site](https://egeria-project.org/guides/admin/servers/configuring-an-integration-daemon/#configure-the-integration-services)

In addition, the integration connector uses an EmbeddedConnection in order to make use of the [JDBC resource connector](https://egeria-project.org/connectors/#databases), which keeps the connection details to target database 
```
POST {{baseURL}}/open-metadata/admin-services/users/{{user}}/servers/{{server}}/integration-services/{{integrationServiceURLMarker}}
```
Body
```
{
    "class": "IntegrationServiceRequestBody",
    "omagserverPlatformRootURL": "<access-service-omag-url>",
    "omagserverName": "<omas-server-name>",
    "integrationConnectorConfigs":[ 
        {
            "class": "IntegrationConnectorConfig",
            "connectorName": "<connector-name>",
            "connection":{
                "class": "VirtualConnection",
                "connectorType" : {
                    "class": "ConnectorType",
                    "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider"
                },
                "embeddedConnections":[
                    {
                        "class" : "EmbeddedConnection",
                        "embeddedConnection" : {
                            "class" : "Connection",
                            "userId" : "<user>",
                            "clearPassword" : "<password>",
                            "connectorType" : {
                                "class": "ConnectorType",
                                "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider"
                            },
                            "endpoint":{
                                "class": "Endpoint",
                                "address" : "<jdbc-format-database-address>"
                            }
                        }
                    }
                ],
                "configurationProperties": {
                    "includeSchemaNames": [],
                    "excludeSchemaNames": [],
                    "includeTableNames": [],
                    "excludeTableNames": [],
                    "includeViewNames": [],
                    "excludeViewNames": [],
                    "includeColumnNames": [],
                    "excludeColumnNames": []
                }
            },  
            "metadataSourceQualifiedName": "Source",
            "refreshTimeInterval": "60", 
            "usesBlockingCalls": "false",
            "permittedSynchronization": "FROM_THIRD_PARTY"
        }
    ]
}
```

**access-service-omag-url** - url of omag server that hosts the paired access service

**omas-server-name** - name of paired access server

**connector-name** - this connectors name

**user** - database user

**password** - database password

**jdbc-format-database-address** - database address

**include/exclude** properties - control which schemas, tables, views and columns, respectively, are imported
if include is set, then the import is restricted to specified entities; 
if exclude is set, the import will ignore specified entities; 
if both are set, the import will take into account only the property include;

