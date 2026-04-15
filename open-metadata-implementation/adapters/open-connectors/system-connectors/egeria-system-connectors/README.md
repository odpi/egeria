<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria System Connectors

The connectors in this module are used to work with Egeria.


## Egeria Platform Cataloguing Integration Connector

The *Egeria Platform Cataloguing Connector* is an *Integration Connector* that creates a metadata representation of an OMAG Server Platform.


![Figure 1](docs/egeria-infrastructure-cataloguer-integration-connector.png)
> **Figure 1:** Operation of the Egeria infrastructure cataloguer integration connector

The connector automatically configures itself to catalog the OMAG Server Platform that it is calling to retrieve metadata.  It will also catalog other platforms attached as catalog targets.


The details of each configured OMAG server is used to create appropriate `SoftwareService` entities linked by `SupportedSoftwareCapability` relationships to the server's `SoftwareServer` entity.  Most OMAG services manage one or more connectors and these are represented using `DeployedConnector` entities linked by `ServerAssetUse` relationships.

If the API for the platform is catalogued, the connector uses the service name in the urls to link the individual operations to the appropriate software service using the `ProcessCall` relationship.  The service url marker used to match the API operations is extracted by requesting the [*registered services*](https://egeria-project.org/services/platform-services/overview/#registered-services) from the platform.

![Figure 2](docs/egeria-infrastructure-cataloguer-integration-connector-software-services.png)
> **Figure 2:** Adding the configured services

The connection information is catalogued for each of the deployed connectors and where the connector type is recognized, additional assets (such as Apache Kafka topics) may be created/linked to the deployed connectors.

![Figure 3](docs/egeria-infrastructure-cataloguer-integration-connector-deployed-connector.png)
> **Figure 3:** Adding the connections for the deployed connectors

This processing occurs each time the refresh method is called, and the connector catalogs as much as is possible given the status of the environment on each iteration.

### Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/servers/by-server-type/configuring-an-integration-daemons).

```json linenums="1"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "TopicMonitorConnection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.devprojects.connectors.integration.egeria.EgeriaInfrastructureIntegrationProvider"
                    }
                }
}
```


----
* Return to [System Connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.