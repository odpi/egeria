<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Metadata Collection Id

Every [metadata repository](metadata-collection.md) has a unique identifier called 
the **local-metadata-collection-id**.
This identifier is assigned automatically during the configuration
of the local repository
but can be overridden through administrative commands.

Figure 1 shows the local metadata collection id of
`1b96495f-82d3-4224-9fdd-31bcb84c224c` in the server configuration.
A new local metadata collection id is assigned when the local repository is set up.

![Figure 1: local metadata collection id in server configuration](local-metadata-collection-id-in-config.png)
> Figure 1: local metadata collection id in server configuration

It also appears in an audit log message written at start up.

```
OMRS-AUDIT-0001 The Open Metadata Repository Services (OMRS) is initializing
        :                                 :                              :
OMRS-AUDIT-0003 The local repository is initializing with metadata collection id 1b96495f-82d3-4224-9fdd-31bcb84c224c
```

If the server is connected to a cohort, the local **[cohort registry](component-descriptions/cohort-registry.md)**
sends the local metadata collection id and 

```json
{
  "localRegistration": {
    "metadataCollectionId": "1b96495f-82d3-4224-9fdd-31bcb84c224c",
    "serverName": "cocoMDS1",
    "serverType": "Open Metadata and Governance Server",
    "registrationTime": 1531820378765,
    "repositoryConnection": {
      "class": "Connection",
      "type": {
        "type": "ElementType",
        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
        "elementTypeName": "Connection",
        "elementTypeVersion": 1,
        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
        "elementOrigin": "CONFIGURATION"
      },
      "guid": "858be98b-49d2-4ccf-9b23-01085a5f473f",
      "qualifiedName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
      "name": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
      "description": "OMRS default repository REST API connection.",
      "connectorType": {
        "type": {
          "type": "ElementType",
          "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
          "elementTypeName": "ConnectorType",
          "elementTypeVersion": 1,
          "elementTypeDescription": "A set of properties describing a type of connector.",
          "elementOrigin": "CONFIGURATION"
        },
        "guid": "64e67923-8190-45ea-8f96-39320d638c02",
        "qualifiedName": "DefaultRepositoryRESTAPI.ConnectorType.cocoMDS1",
        "name": "DefaultRepositoryRESTAPI.ConnectorType.cocoMDS1",
        "description": "OMRS default repository REST API connector type.",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider"
      },
      "endpoint": {
        "type": {
          "type": "ElementType",
          "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
          "elementTypeName": "Endpoint",
          "elementTypeVersion": 1,
          "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
          "elementOrigin": "CONFIGURATION"
        },
        "guid": "cee85898-43aa-4af5-9bbd-2bed809d1acb",
        "qualifiedName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
        "name": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
        "description": "OMRS default repository REST API endpoint.",
        "address": "https://localhost:9443/openmetadata/repositoryservices/"
      }
    }
  }
}
```


----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.