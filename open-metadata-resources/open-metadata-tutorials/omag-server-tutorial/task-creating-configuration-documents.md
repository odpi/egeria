<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Creating configuration documents for the OMAG Server Platform

The [OMAG server platform](../../../open-metadata-publication/website/omag-server)
provides a software platform for running
[OMAG Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md).

Each OMAG Server supports selected open metadata and governance services based on its
configuration. 

When [the OMAG server platform is first started](task-starting-the-omag-server-platform.md),
there are no OMAG Servers running inside it.
However there are three sets of Administration Service APIs active.

* **Server Origin** - for discovering the source of the OMAG server platform.  
(This was used in the previous [task](task-starting-the-omag-server-platform.md).)
* **Configuration Services** - for creating configuration documents.
* **Operational Services** - for starting and stopping [OMAG Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md)
in the OMAG server platform using the configuration documents.

## What is a configuration document?

A [configuration document](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document.md)
provides the configuration properties for an OMAG server.

It includes:
* Basic properties of the OMAG server.
* Defaults to use when configuring the [subsystems](../../../open-metadata-implementation/access-services/docs/concepts/it-infrastructure/subsystem.md)
of the OMAG server.
* Configuration for selected subsystems within the OMAG server.  The subsystems selected and configured
determine which open metadata and governance services are supported by the OMAG server.

Configuration documents are created using the OMAG server platform
configuration services.  In order to experiment with these services,
this tutorial uses the [Postman](task-working-with-postman.md) test tool.
This is a tool that enables you to type in REST API calls and execute them
against the OMAG server platform.

There is also a postman collection located at:

[https://raw.githubusercontent.com/odpi/egeria/master/open-metadata-resources/open-metadata-tutorials/omag-server-tutorial/resources/omag-server-platform-tutorial.postman_collection.json](https://raw.githubusercontent.com/odpi/egeria/master/open-metadata-resources/open-metadata-tutorials/omag-server-tutorial/resources/omag-server-platform-tutorial.postman_collection.json)

It can be downloaded and imported into postman to support this tutorial.
(see **Import** button top left of the Postman user interface).

This tutorial will also use **curl** commands to illustrate calls to the administration services
as well as refer to the pre-canned calls in the postman collection.


## Creating the configuration document

Before there is a configuration document, requesting the server configuration
creates and returns a default document.  The command is:

```
GET {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/configuration
```

where:
* `{serverURLRoot}` is the host name and port number of the OMAG server platform (eg https://localhost:9443).
* `{adminUserId}` is the user id of the administrator making the calls.
* `{serverName}` is the name of the OMAG server that is being configured.

Try the following command (this is request **2.** in Postman): 

```
GET https://localhost:9443/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/configuration
```

The response is in JSON format and contains the following information:

```json
{
    "class":"OMAGServerConfigResponse",
    "relatedHTTPCode":200,
    "omagserverConfig":
    {
       "class":"OMAGServerConfig",
       "localServerId":"28aeb916-5029-4d6a-aa96-392196859916",
       "localServerName":"cocoMDS1",
       "localServerType":"Open Metadata and Governance Server",
       "localServerURL":"https://localhost:9443",
       "localServerUserId":"OMAGServer",
       "maxPageSize":1000
    }
}
```

The **localServerId** property is a unique identifier given to the server and is used
internally to improve the performance of its interaction with external components such as Apache Kafka.

The **localServerName** is the name of the OMAG server supplied on the command.

The **localServerType**, **localServerURL**, **localServerUserId** and **maxPageSize**
are set to their default values and can be changed.

For example, try the following command (this is request **3.** in Postman):

```
POST https://localhost:9443/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/server-type?typeName="Standalone Metadata Repository"
```

Then query the configuration again (this is request **4.** in Postman):

```json
{
    "class": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "class": "OMAGServerConfig",
        "localServerId": "2a73902e-e691-43cc-b422-23b6b42992e2",
        "localServerName": "cocoMDS1",
        "localServerType": "Standalone Metadata Repository",
        "localServerURL": "https://localhost:9443",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 1000,
        "auditTrail": [
            "Tue Feb 05 13:12:50 GMT 2019 garygeeke updated configuration for local server type name to Standalone Metadata Repository."
        ]
    }
}
```

Notice that the localServerType has changed and an audit trail has also appeared.  This allows you to keep track of
the changes being made to the configuration document.

The next command configures in type of metadata repository (this is request **5.** in Postman).  In this example, we are using a simple in-memory repository
which is useful for testing.

```
POST https://localhost:9443/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/in-memory-repository
```

This has added the configuration for the local repository using default values.
If you query the configuration again (this is request **6.** in Postman) you see:

```json
{
    "class": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "class": "OMAGServerConfig",
        "localServerId": "2a73902e-e691-43cc-b422-23b6b42992e2",
        "localServerName": "cocoMDS1",
        "localServerType": "Standalone Metadata Repository",
        "localServerURL": "https://localhost:9443",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 1000,
        "repositoryServicesConfig": {
            "class": "RepositoryServicesConfig",
            "auditLogConnections": [
                {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "5390bf3e-6b38-4eda-b34a-de55ac4252a7",
                    "qualifiedName": "DefaultAuditLog.Connection.cocoMDS1",
                    "displayName": "DefaultAuditLog.Connection.cocoMDS1",
                    "description": "OMRS default audit log connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "4afac741-3dcc-4c60-a4ca-a6dede994e3f",
                        "qualifiedName": "Console Audit Log Store Connector",
                        "displayName": "Console Audit Log Store Connector",
                        "description": "Connector supports logging of audit log messages to stdout.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider"
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "836efeae-ab34-4425-89f0-6adf2faa1f2e",
                        "qualifiedName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "displayName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "description": "OMRS default audit log endpoint.",
                        "address": "cocoMDS1.auditlog"
                    }
                }
            ],
            "localRepositoryConfig": {
                "class": "LocalRepositoryConfig",
                "metadataCollectionId": "ad405dc2-1361-48f8-9ea2-538bd43db1b0",
                "localRepositoryLocalConnection": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "6a3c07b0-0e04-42dc-bcc6-392609bf1d02",
                    "qualifiedName": "DefaultInMemoryRepository.Connection.cocoMDS1",
                    "displayName": "DefaultInMemoryRepository.Connection.cocoMDS1",
                    "description": "OMRS default in memory local repository connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "65cc9091-757f-4bcd-b937-426160be8bc2",
                        "qualifiedName": "OMRS In Memory Repository Connector",
                        "displayName": "OMRS In Memory Repository Connector",
                        "description": "OMRS Repository Connector that uses an in-memory store.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider"
                    }
                },
                "localRepositoryRemoteConnection": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "858be98b-49d2-4ccf-9b23-01085a5f473f",
                    "qualifiedName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
                    "displayName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
                    "description": "OMRS default repository REST API connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "75ea56d1-656c-43fb-bc0c-9d35c5553b9e",
                        "qualifiedName": "OMRS REST API Repository Connector",
                        "displayName": "OMRS REST API Repository Connector",
                        "description": "OMRS Repository Connector that calls the repository services REST API of a remote server.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider"
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "cee85898-43aa-4af5-9bbd-2bed809d1acb",
                        "qualifiedName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "displayName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "description": "OMRS default repository REST API endpoint.",
                        "address": "https://localhost:9443/servers/cocoMDS1"
                    }
                },
                "eventsToSaveRule": "ALL",
                "eventsToSendRule": "ALL"
            }
        },
        "auditTrail": [
            "Tue Feb 05 13:12:50 GMT 2019 garygeeke updated configuration for local server type name to Standalone Metadata Repository.",
            "Tue Feb 05 14:58:28 GMT 2019 garygeeke updated configuration for the local repository."
        ]
    }
}
```

Finally in this exercise, use the following command to enable the
[Open Metadata Access Services (OMASs)](../../../open-metadata-implementation/access-services) that provide
the domain specific open metadata and governance APIs.

The access services provide both REST APIs and event-based interaction.
As such they need information about an event bus.
The command below (this is request **7.** in Postman) sets up configuration properties about the event bus.  These properties are
embedded in the configuration for each access service.

```
POST https://localhost:9443/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/event-bus?topicURLRoot=egeriaTopics
```

When the configuration is next queried (this is request **8.** in Postman), the event bus details are stored in the configuration document.

```json
{
    "class": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "class": "OMAGServerConfig",
        "localServerId": "2a73902e-e691-43cc-b422-23b6b42992e2",
        "localServerName": "cocoMDS1",
        "localServerType": "Standalone Metadata Repository",
        "localServerURL": "https://localhost:9443",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 1000,
        "eventBusConfig": {
            "class": "EventBusConfig",
            "topicURLRoot": "egeriaTopics"
        },
        "repositoryServicesConfig": {
            "class": "RepositoryServicesConfig",
            "auditLogConnections": [
                {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "5390bf3e-6b38-4eda-b34a-de55ac4252a7",
                    "qualifiedName": "DefaultAuditLog.Connection.cocoMDS1",
                    "displayName": "DefaultAuditLog.Connection.cocoMDS1",
                    "description": "OMRS default audit log connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "4afac741-3dcc-4c60-a4ca-a6dede994e3f",
                        "qualifiedName": "Console Audit Log Store Connector",
                        "displayName": "Console Audit Log Store Connector",
                        "description": "Connector supports logging of audit log messages to stdout.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider"
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "836efeae-ab34-4425-89f0-6adf2faa1f2e",
                        "qualifiedName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "displayName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "description": "OMRS default audit log endpoint.",
                        "address": "cocoMDS1.auditlog"
                    }
                }
            ],
            "localRepositoryConfig": {
                "class": "LocalRepositoryConfig",
                "metadataCollectionId": "ad405dc2-1361-48f8-9ea2-538bd43db1b0",
                "localRepositoryLocalConnection": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "6a3c07b0-0e04-42dc-bcc6-392609bf1d02",
                    "qualifiedName": "DefaultInMemoryRepository.Connection.cocoMDS1",
                    "displayName": "DefaultInMemoryRepository.Connection.cocoMDS1",
                    "description": "OMRS default in memory local repository connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "65cc9091-757f-4bcd-b937-426160be8bc2",
                        "qualifiedName": "OMRS In Memory Repository Connector",
                        "displayName": "OMRS In Memory Repository Connector",
                        "description": "OMRS Repository Connector that uses an in-memory store.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider"
                    }
                },
                "localRepositoryRemoteConnection": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "858be98b-49d2-4ccf-9b23-01085a5f473f",
                    "qualifiedName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
                    "displayName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
                    "description": "OMRS default repository REST API connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "75ea56d1-656c-43fb-bc0c-9d35c5553b9e",
                        "qualifiedName": "OMRS REST API Repository Connector",
                        "displayName": "OMRS REST API Repository Connector",
                        "description": "OMRS Repository Connector that calls the repository services REST API of a remote server.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider"
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "cee85898-43aa-4af5-9bbd-2bed809d1acb",
                        "qualifiedName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "displayName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "description": "OMRS default repository REST API endpoint.",
                        "address": "https://localhost:9443/servers/cocoMDS1"
                    }
                },
                "eventsToSaveRule": "ALL",
                "eventsToSendRule": "ALL"
            }
        },
        "auditTrail": [
            "Tue Feb 05 13:12:50 GMT 2019 garygeeke updated configuration for local server type name to Standalone Metadata Repository.",
            "Tue Feb 05 14:58:28 GMT 2019 garygeeke updated configuration for the local repository.",
            "Tue Feb 05 15:13:45 GMT 2019 garygeeke updated configuration for default event bus."
        ]
    }
}
```

The last command creates the configuration for the access services (this is request **9.** in Postman):

```
POST https://localhost:9443/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/access-services
```

which results in the final configuration (this is request **10.** in Postman).

```json
{
    "class": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "class": "OMAGServerConfig",
        "localServerId": "2a73902e-e691-43cc-b422-23b6b42992e2",
        "localServerName": "cocoMDS1",
        "localServerType": "Standalone Metadata Repository",
        "localServerURL": "https://localhost:9443",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 1000,
        "eventBusConfig": {
            "class": "EventBusConfig",
            "topicURLRoot": "egeriaTopics"
        },
        "accessServicesConfig": [
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1021,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineAdmin",
                "accessServiceName": "Data Engine",
                "accessServiceDescription": "Create processes for lineage",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/data-engine/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "14475820-7356-4b14-a560-128b4f6bdf87",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "25629bf5-e8f0-4f47-ac43-2dee3ba2439c",
                        "qualifiedName": "open-metadata.access-services.Data Engine.inTopic",
                        "displayName": "open-metadata.access-services.Data Engine.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Data Engine.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "84689e40-b83a-4a8e-b172-e628ded4c77b",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "0b2e8a73-0c58-44fe-8b8e-e07b3f78592d",
                        "qualifiedName": "open-metadata.access-services.Data Engine.outTopic",
                        "displayName": "open-metadata.access-services.Data Engine.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Data Engine.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1020,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.subjectarea.admin.SubjectAreaAdmin",
                "accessServiceName": "Subject Area",
                "accessServiceDescription": "Document knowledge about a subject area",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "96780526-2d8e-4334-9bfa-1dd86896913c",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "c2643840-8225-4c9b-84b6-4ab0b2a9a80a",
                        "qualifiedName": "open-metadata.access-services.Subject Area.inTopic",
                        "displayName": "open-metadata.access-services.Subject Area.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Subject Area.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "82279a05-9310-4600-a03a-9dc70a41600f",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "2235fdb0-84d6-4efe-b914-9035dbe74083",
                        "qualifiedName": "open-metadata.access-services.Subject Area.outTopic",
                        "displayName": "open-metadata.access-services.Subject Area.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Subject Area.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1008,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.governanceengine.admin.GovernanceEngineAdmin",
                "accessServiceName": "Governance Engine",
                "accessServiceDescription": "Set up an operational governance engine",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/governance-engine/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "4f83087c-bd1c-455f-b809-1938cab03ecd",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "a0ba0eea-738f-4b0c-adc2-3d832e43ca72",
                        "qualifiedName": "open-metadata.access-services.Governance Engine.inTopic",
                        "displayName": "open-metadata.access-services.Governance Engine.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Governance Engine.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "84036aea-5055-427a-aa3d-ad3fa0aef255",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "98f58323-b97c-4e7b-82f6-d804449a4d81",
                        "qualifiedName": "open-metadata.access-services.Governance Engine.outTopic",
                        "displayName": "open-metadata.access-services.Governance Engine.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Governance Engine.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1009,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.governanceprogram.admin.GovernanceProgramAdmin",
                "accessServiceName": "Governance Program",
                "accessServiceDescription": "Manage the governance program",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/governance-program/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "6bba6aa3-308e-41a3-bf01-51ef65028e60",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "aad59e1e-20e4-4a9b-982a-f8b939dc580a",
                        "qualifiedName": "open-metadata.access-services.Governance Program.inTopic",
                        "displayName": "open-metadata.access-services.Governance Program.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Governance Program.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "6d5a04c3-e216-42b0-9479-71ce4461e86d",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "dccb1ef2-ae92-4ed9-9619-537c9cf4e05d",
                        "qualifiedName": "open-metadata.access-services.Governance Program.outTopic",
                        "displayName": "open-metadata.access-services.Governance Program.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Governance Program.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1014,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.informationview.admin.InformationViewAdmin",
                "accessServiceName": "Information View",
                "accessServiceDescription": "Support information virtualization and data set definitions",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/information-view/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "b7718cc6-da6a-4a9f-8f76-c5d4579f1c34",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "87628694-8c85-46eb-aae2-0c834e915e00",
                        "qualifiedName": "open-metadata.access-services.Information View.inTopic",
                        "displayName": "open-metadata.access-services.Information View.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Information View.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "826c314e-856d-4b2b-bcf1-0d11285a7610",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "fccd511a-86f1-4748-b580-78afd9a0b166",
                        "qualifiedName": "open-metadata.access-services.Information View.outTopic",
                        "displayName": "open-metadata.access-services.Information View.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Information View.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1001,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.assetconsumer.admin.AssetConsumerAdmin",
                "accessServiceName": "Asset Consumer",
                "accessServiceDescription": "Access assets through connectors",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-consumer/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "5d034bb8-133b-4dc8-b0db-712ae8a032d6",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "148e6940-9dff-47c6-9762-662559b3bec7",
                        "qualifiedName": "open-metadata.access-services.Asset Consumer.inTopic",
                        "displayName": "open-metadata.access-services.Asset Consumer.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Asset Consumer.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "c46fd6be-4377-4ef8-8ce4-82aa30a9f3bd",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "6ca034c7-523e-4b30-a7dc-f1359c287ea0",
                        "qualifiedName": "open-metadata.access-services.Asset Consumer.outTopic",
                        "displayName": "open-metadata.access-services.Asset Consumer.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Asset Consumer.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1004,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.connectedasset.admin.ConnectedAssetAdmin",
                "accessServiceName": "Connected Asset",
                "accessServiceDescription": "Understand an asset",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/connected-asset/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "2214b4ad-d1b8-4fd4-942c-4a5552688e91",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "fabfa203-ca60-4036-90e5-de5d695ef8f1",
                        "qualifiedName": "open-metadata.access-services.Connected Asset.inTopic",
                        "displayName": "open-metadata.access-services.Connected Asset.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Connected Asset.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "9c3332ce-b065-49dc-b22e-432277627cde",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "e7bf76b4-4214-460f-ad8f-12a0ecb63e44",
                        "qualifiedName": "open-metadata.access-services.Connected Asset.outTopic",
                        "displayName": "open-metadata.access-services.Connected Asset.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Connected Asset.outTopic"
                    }
                }
            },
            {
                "class": "AccessServiceConfig",
                "accessServiceId": 1000,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin",
                "accessServiceName": "Asset Catalog",
                "accessServiceDescription": "Search and understand your assets",
                "accessServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-catalog/",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "5f6dc495-2b31-4ca0-8196-bb34a14e020b",
                    "qualifiedName": "InTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "338b6ea6-cd1e-4896-ab1a-568da8477903",
                        "qualifiedName": "open-metadata.access-services.Asset Catalog.inTopic",
                        "displayName": "open-metadata.access-services.Asset Catalog.inTopic",
                        "description": "InTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Asset Catalog.inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "55a9dbb0-f756-4180-82d5-9b8c35af5fb0",
                    "qualifiedName": "OutTopic",
                    "configurationProperties": {
                        "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                    },
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "3851e8d0-e343-400c-82cb-3918fed81da6",
                        "qualifiedName": "Kafka Open Metadata Topic Connector",
                        "displayName": "Kafka Open Metadata Topic Connector",
                        "description": "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                        "recognizedAdditionalProperties": [
                            "producer",
                            "consumer",
                            "local.server.id"
                        ]
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "dab48064-6204-49bf-bf30-a66b0e353d36",
                        "qualifiedName": "open-metadata.access-services.Asset Catalog.outTopic",
                        "displayName": "open-metadata.access-services.Asset Catalog.outTopic",
                        "description": "OutTopic",
                        "address": "egeriaTopics.server.cocoMDS1.open-metadata.access-services.Asset Catalog.outTopic"
                    }
                }
            }
        ],
        "repositoryServicesConfig": {
            "class": "RepositoryServicesConfig",
            "auditLogConnections": [
                {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "5390bf3e-6b38-4eda-b34a-de55ac4252a7",
                    "qualifiedName": "DefaultAuditLog.Connection.cocoMDS1",
                    "displayName": "DefaultAuditLog.Connection.cocoMDS1",
                    "description": "OMRS default audit log connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "4afac741-3dcc-4c60-a4ca-a6dede994e3f",
                        "qualifiedName": "Console Audit Log Store Connector",
                        "displayName": "Console Audit Log Store Connector",
                        "description": "Connector supports logging of audit log messages to stdout.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider"
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "836efeae-ab34-4425-89f0-6adf2faa1f2e",
                        "qualifiedName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "displayName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "description": "OMRS default audit log endpoint.",
                        "address": "cocoMDS1.auditlog"
                    }
                }
            ],
            "localRepositoryConfig": {
                "class": "LocalRepositoryConfig",
                "metadataCollectionId": "ad405dc2-1361-48f8-9ea2-538bd43db1b0",
                "localRepositoryLocalConnection": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "6a3c07b0-0e04-42dc-bcc6-392609bf1d02",
                    "qualifiedName": "DefaultInMemoryRepository.Connection.cocoMDS1",
                    "displayName": "DefaultInMemoryRepository.Connection.cocoMDS1",
                    "description": "OMRS default in memory local repository connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "65cc9091-757f-4bcd-b937-426160be8bc2",
                        "qualifiedName": "OMRS In Memory Repository Connector",
                        "displayName": "OMRS In Memory Repository Connector",
                        "description": "OMRS Repository Connector that uses an in-memory store.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider"
                    }
                },
                "localRepositoryRemoteConnection": {
                    "class": "Connection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "858be98b-49d2-4ccf-9b23-01085a5f473f",
                    "qualifiedName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
                    "displayName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
                    "description": "OMRS default repository REST API connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "LOCAL_COHORT"
                        },
                        "guid": "75ea56d1-656c-43fb-bc0c-9d35c5553b9e",
                        "qualifiedName": "OMRS REST API Repository Connector",
                        "displayName": "OMRS REST API Repository Connector",
                        "description": "OMRS Repository Connector that calls the repository services REST API of a remote server.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider"
                    },
                    "endpoint": {
                        "class": "Endpoint",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                            "elementTypeName": "Endpoint",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "cee85898-43aa-4af5-9bbd-2bed809d1acb",
                        "qualifiedName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "displayName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "description": "OMRS default repository REST API endpoint.",
                        "address": "https://localhost:9443/servers/cocoMDS1"
                    }
                },
                "eventsToSaveRule": "ALL",
                "eventsToSendRule": "ALL"
            },
            "enterpriseAccessConfig": {
                "class": "EnterpriseAccessConfig",
                "enterpriseMetadataCollectionName": "cocoMDS1 Enterprise Metadata Collection",
                "enterpriseMetadataCollectionId": "95af03a9-ac18-4dbb-8b3a-4782429e5f77",
                "enterpriseOMRSTopicConnection": {
                    "class": "VirtualConnection",
                    "type": {
                        "class": "ElementType",
                        "elementTypeId": "82f9c664-e59d-484c-a8f3-17088c23a2f3",
                        "elementTypeName": "VirtualConnection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A connector for a virtual resource that needs to retrieve data from multiple places.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "2084ee90-717b-49a1-938e-8f9d49567b8e",
                    "qualifiedName": "EnterpriseTopicConnector.Server.cocoMDS1",
                    "displayName": "EnterpriseTopicConnector.Server.cocoMDS1",
                    "description": "OMRS default enterprise topic connection.",
                    "connectorType": {
                        "class": "ConnectorType",
                        "type": {
                            "class": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "c3cc7a9c-4fe2-4383-85c3-1e94df45e2da",
                        "qualifiedName": "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider",
                        "displayName": "OMRSTopicProvider",
                        "description": "ConnectorType for OMRSTopicProvider",
                        "connectorProviderClassName": "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider"
                    },
                    "embeddedConnections": [
                        {
                            "class": "EmbeddedConnection",
                            "displayName": "Enterprise OMRS Events",
                            "embeddedConnection": {
                                "class": "Connection",
                                "type": {
                                    "class": "ElementType",
                                    "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                                    "elementTypeName": "Connection",
                                    "elementTypeVersion": 1,
                                    "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                                    "elementOrigin": "CONFIGURATION"
                                },
                                "guid": "d2224d17-d55d-4029-b841-7b37f2fa3df3",
                                "qualifiedName": "Enterprise OMRS Events",
                                "configurationProperties": {
                                    "local.server.id": "2a73902e-e691-43cc-b422-23b6b42992e2"
                                },
                                "displayName": "Enterprise OMRS Events",
                                "description": "Enterprise OMRS Events",
                                "connectorType": {
                                    "class": "ConnectorType",
                                    "type": {
                                        "class": "ElementType",
                                        "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                                        "elementTypeName": "ConnectorType",
                                        "elementTypeVersion": 1,
                                        "elementTypeDescription": "A set of properties describing a type of connector.",
                                        "elementOrigin": "LOCAL_COHORT"
                                    },
                                    "guid": "ed8e682b-2fec-4403-b551-02f8c46322ef",
                                    "qualifiedName": "In Memory Open Metadata Topic Connector",
                                    "displayName": "In Memory Open Metadata Topic Connector",
                                    "description": "In Memory Open Metadata Topic Connector supports string based events over an in memory event bus.",
                                    "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider"
                                },
                                "endpoint": {
                                    "class": "Endpoint",
                                    "type": {
                                        "class": "ElementType",
                                        "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                                        "elementTypeName": "Endpoint",
                                        "elementTypeVersion": 1,
                                        "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                                        "elementOrigin": "CONFIGURATION"
                                    },
                                    "guid": "2f858351-eb06-4824-805c-6f0bb56a4923",
                                    "qualifiedName": "open-metadata.repository-services.enterprise.cocoMDS1.OMRSTopic",
                                    "displayName": "open-metadata.repository-services.enterprise.cocoMDS1.OMRSTopic",
                                    "description": "Enterprise OMRS Events",
                                    "address": "cocoMDS1.open-metadata.repository-services.enterprise.cocoMDS1.OMRSTopic"
                                }
                            }
                        }
                    ]
                },
                "enterpriseOMRSTopicProtocolVersion": "V1"
            }
        },
        "auditTrail": [
            "Tue Feb 05 13:12:50 GMT 2019 garygeeke updated configuration for local server type name to Standalone Metadata Repository.",
            "Tue Feb 05 14:58:28 GMT 2019 garygeeke updated configuration for the local repository.",
            "Tue Feb 05 15:13:45 GMT 2019 garygeeke updated configuration for default event bus.",
            "Tue Feb 05 15:49:07 GMT 2019 garygeeke updated configuration for access services.",
            "Tue Feb 05 15:49:07 GMT 2019 garygeeke updated configuration for enterprise repository services (used by access services)."
        ]
    }
}
```

You have probably noticed how quickly the configuration document grew into a complex structure.
The commands you used made use of all of the configuration default values.
There are other configuration services that enable you to customize the configuration document to
adapt it to specific environment.  However, the defaults provide a good starting point.

## Further reading

The contents of this tutorial cover a very simple OMAG server configuration.
For guidance on configuring more complex OMAG servers see:

* [Administration Services User Guide](../../../open-metadata-implementation/admin-services/docs/user)

For instructions on how to set up two OMAG Servers using in memory repositories that are exchanging metadata
over [Apache Kafka](http://kafka.apache.org/), see:

* [In Memory Repository Demo](https://github.com/odpi/egeria-samples/tree/master/demos/in-memory-repository)

## Next steps

With the configuration document in place, you are ready to [start the OMAG Server](task-starting-omag-server.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
