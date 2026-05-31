<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The OpenAPI Integration Connector

The OpenAPI integration connector catalogs APIs by extracting their OpenAPISpecification from the swagger endpoint.

## Deployment and configuration

The Open API integration connector is included in the main Egeria assembly.
It runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

The Open API monitor integration connector connects to an endpoint and extracts the open API specification through the `GET {{serverURL}}/v3/api-docs` request.  It creates a [DeployedAPI](https://egeria-project.org/types/2/0212-Deployed-APIs) asset for each API Tag that is known to the server. A new [APIOperation](https://egeria-project.org/types/5/0536-API-Schemas) is created for each combination of path name and operation (GET, POST, PUT, DELETE).

![Figure 1](docs/open-api-monitor-integration-connector.png)
> **Figure 1:** Operation of the Open API monitor integration connector


## Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/servers/by-server-type/configuring-an-integration-daemon).

```json linenums="1" hl_lines="15"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "APIMonitorConnection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider"
                    },
                    "endpoint" :
                    {
                        "class" : "Endpoint",
                        "address" : "{{serverURL}}"
                    }
                }
}
```

- Replace `{{serverURL}}` with the network address of the process where the API is hosted (for example, `localhost:7443`).


----
* Return to [Integration Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.