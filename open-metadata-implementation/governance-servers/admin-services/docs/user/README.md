<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform Administration Services User Guide

An [Open Metadata and Governance (OMAG) Server Platform](../../../../../../open-metadata-publication/website/omag-server)
hosts one or more logical **[OMAG servers](../concepts/logical-omag-server.md)**, each supporting a variety of open metadata
and governance capabilities.

The capabilities that are enabled in a specific instance of a logical OMAG Server
are defined in a JSON **[configuration document](../concepts/configuration-document.md)**.
When the configuration document is loaded to the OMAG server platform, the logical OMAG server
is started, and the capabilities defined in the configuration document are activated.

In an open metadata landscape, it is anticipated that there may be multiple
instances of the logical OMAG Server running, each performing a different role.
The capabilities of each of these instances would be defined in a different configuration document.
They could all, however, be loaded into the same OMAG server platform, or distributed across
different OMAG server platforms.

The configuration document for a specific logical OMAG server is identified by the server's name.
This is passed on the URL of every admin services API request along with the user
id of the administrator.  By default, the configuration is stored in a file called:

```

omag.server.{serverName}.config

```

The administration services that set up this file all begin with a URL like this:

```

.../open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/...

```

The **serverName** specified on these calls determines which configuration
document is used, and hence which of the logical OMAG server's configuration it is working with.

The OMAG server platform starts up without any open metadata capabilities enabled.
Once it is running, it can be used to set up the configuration documents
that describe the open metadata capabilities needed for each server instance.

Once the configuration document is in place, the open metadata services
can be activated and deactivated multiple times, across multiple
restarts of the server platform.

## Building a configuration document for a server

The configuration document for the logical OMAG Server determines the types of open
metadata and governance services that should be activated in the logical OMAG server.
For example:

* Basic descriptive properties of the server that are used in logging and events
originating from the server.
* What type of local repository to use.
* Whether the Open Metadata Access Services (OMASs) should be started.
* Which cohorts to connect to.

Each of the configuration commands builds up sections in the configuration document.
This document is stored in the configuration file after each configuration request so
it is immediately available for use each time the open metadata services are activated
in the OMAG Server.

In the descriptions of the configuration commands the following values are used as examples:

* The OMAG server platform is running on the localhost, at port 8080 (ie *http://localhost:8080*).
* The user id of the administrator is **garygeeke**.
* The name of the logical OMAG server (serverName) is **cocoMDS1**.

## Common Configuration Tasks

* [Setting basic properties for a logical OMAG server](configuring-omag-server-basic-properties.md)

## Advanced Topics

* [Configuring the storage mechanism to use for configuration documents](configuring-configuration-file-store.md)

##

### Setting up the default event bus

An OMAG server uses an event bus to exchange events with other
tools.  The open metadata code manages the specific topic names;
however, it needs to know where the event bus implementation is and
any properties needed to configure it.

The following command creates information about the event bus.
This information is used on the subsequent configuration of the OMAG server subsystems.
It does not affect any subsystems that have already been configured in the configuration document.

It is possible to add arbitrary name/value pairs as JSON in the
request body.  The correct properties to use are defined in the connector type.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/event-bus

```

For example, when using Kafka as your event bus you may want to configure properties such as:

```json
{
	"producer": {
		"bootstrap.servers":"localhost:9092",
		"acks":"all",
		"retries":"0",
		"batch.size":"16384",
		"linger.ms":"1",
		"buffer.memory":"33554432",
		"max.request.size":"10485760",
		"key.serializer":"org.apache.kafka.common.serialization.StringSerializer",
		"value.serializer":"org.apache.kafka.common.serialization.StringSerializer",
		"kafka.omrs.topic.id":"cocoCohort"
	},
	"consumer": {
   		"bootstrap.servers":"localhost:9092",
   		"zookeeper.session.timeout.ms":"400",
   		"zookeeper.sync.time.ms":"200",
   		"fetch.message.max.bytes":"10485760",
   		"max.partition.fetch.bytes":"10485760",
   		"key.deserializer":"org.apache.kafka.common.serialization.StringDeserializer",
   		"value.deserializer":"org.apache.kafka.common.serialization.StringDeserializer",
   		"kafka.omrs.topic.id":"cocoCohort"
	}
}

```

### Managing the access services

The open metadata access services provide the domain-specific
APIs for metadata management and governance.

#### Enable the access services

To enable the open metadata access services (and the enterprise
repository services that support them) use the following command.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/access-services

```

#### Disable the access services


The access services can be disabled with the following command.
This also disables the enterprise repository services since they
are not being used.

```
DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/access-services

```

### Setting up the local repository

A local repository is optional.
The administration services can be used to enable one of the built-in
local repositories.

#### Enable the graph repository

This command is a placeholder for an Egeria graph repository.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/local-graph-repository

```

#### Enable the in-memory repository

The in-memory repository is useful for demos and testing.
No metadata is kept if the open metadata services are deactivated,
or the server is shutdown.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/in-memory-repository

```

#### Enable the IBM Information Governance Catalog repository proxy

The OMAG Server can act as a [repository proxy](docs/concepts/repository-proxy.md)
to an IBM Information Governance Catalog ("IGC") environment.
This is done by POSTing the IGC environment details:

- `igcBaseURL` specifies the https host and port on which to access the IGC REST API
- `igcAuthorization` provides a basic-encoded username and password

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/ibm-igc/details
{
    "igcBaseURL": "https://infosvr.vagrant.ibm.com:9446",
    "igcAuthorization": "aXNhZG1pbjppc2FkbWlu",
}

```

The specific version of IBM Information Governance Catalog the environment is running will be detected as part of the initialization process.

#### Enable OMAG Server as a repository proxy

The OMAG Server can act as a proxy to a vendor's repository.
This is done by adding the connection
for the repository proxy as the local repository.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/proxy-details?connectorProvider={javaClassName}

```

#### Add the local repository's event mapper

Any open metadata repository that supports its own API needs an
event mapper to ensure the
Open Metadata Repository Services (OMRS) is notified when
metadata is added
to the repository without going through the open metadata APIs.

The event mapper is a connector that listens for proprietary events
from the repository and converts them into calls to the OMRS.
The OMRS then distributes this new metadata.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/event-mapper-details?connectorProvider={javaClassName}&eventSource={resourceName}

```

For example, to enable the IBM Information Governance Catalog event mapper,
POST the following (where `igc.hostname.somewhere.com` is the hostname of the
domain (services) tier of the environment, and `59092` is the port on which
its Kafka bus can be accessed):

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/event-mapper-details?connectorProvider=org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.IGCOMRSRepositoryEventMapperProvider&eventSource=igc.hostname.somewhere.com:59092

```

#### Remove the local repository

This command removes all configuration for the local repository.
This includes the local metadata collection id.  If a new local repository is
added, it will have a new local metadata collection id and will
not be able to automatically re-register with its cohort(s).

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository

```

### Cohort registration

Open metadata repository cohorts are a set of metadata servers
that are sharing metadata using the open metadata services.
They use a peer-to-peer protocol coordinated through an event bus topic
(typically this is an Apache Kafka topic).

#### Enable access to a cohort

The following command registers the server with cohort called `cocoCohort`.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cocoCohort

```

#### Disconnect from a cohort

This command unregisters a server from a cohort called `cocoCohort`.

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cocoCohort

```

### Querying the contents of a configuration document

It is possible to query the configuration document for a specific
server using the following command.
This command can be issued any time the OMAG Server is running.

```

GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/configuration

```

It is also possible to query the origin of the server supporting the open metadata services.  For the Egeria OMAG Server Platform, the response is "ODPI Egeria OMAG Server Platform".

```

GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/server-origin

```

## Activation/Deactivation API

### Activate open metadata services

The following command activates the open metadata capability defined in
the configuration document for the named server.
Once this call completes, the server begins processing events and the open metadata REST APIs are enabled.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance

```

### Deactivate open metadata services

The following command deactivates the open metadata capability
defined in the configuration document for the named server.
The server will stop processing open metadata events and calls to the REST interfaces will throw an error.

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance

```

### Deactivate open metadata services and unregister from all cohorts

The following command unregisters the named server from the cohorts it
is connected to, disables the open metadata REST APIs and event
processing and deletes the configuration file.
Only use this command if the server is being permanently removed.

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1

```

# Examples of configuration calls

The following calls illustrate the services running.  They are generated
using the following postman configuration: 
[admin-services-configuration.postman_collection.json](admin-services-configuration.postman_collection.json)


## Building the configuration document

Before there is a configuration document, requesting the server configuration
returns a default document:

```

GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/configuration

```

returns

```JSON
{
    "type": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "localServerName": "cocoMDS1",
        "localServerType": "Open Metadata and Governance Server",
        "localServerURL": "http://localhost:8080",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 1000
    }
}
```

### Update server configuration

It is possible to update these values and add in the organization name:

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/server-url-root?url=https://cocoMDS1:8080
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/server-user-id?id=cocoMDS
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/max-page-size?limit=500
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/server-type?typeName=OMAG Test Server
http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/organization-name?name=Coco Pharmaceuticals

```

results in

```JSON
{
    "type": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "localServerName": "cocoMDS1",
        "localServerType": "OMAG Test Server",
        "organizationName": "Coco Pharmaceuticals",
        "localServerURL": "https://cocoMDS1:8080",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 500
    }
}
```

### Update event bus configuration

Next the event bus is set up.  The request body is set up with the common
parameters needed to connect to the event bus

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/event-bus

```

This event bus information is then used in the configuration of the In and Out Topics
for each access service, the cohort OMRS topics and the local event mapper.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/access-services
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/in-memory-repository
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cohort1

```

These calls build up the configuration document as follows:

```JSON
{
    "type": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "localServerName": "cocoMDS1",
        "localServerType": "OMAG Test Server",
        "organizationName": "Coco Pharmaceuticals",
        "localServerURL": "https://cocoMDS1:8080",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 500,
        "accessServicesConfig": [
            {
                "accessServiceId": 1004,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.connectedasset.admin.ConnectedAssetAdmin",
                "serviceName": "ConnectedAsset",
                "serviceDescription": "Understand an asset",
                "serviceWiki": "https://cwiki.apache.org/confluence/display/ATLAS/Connected+Asset+OMAS",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "type": {
                        "type": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "8ea2d820-2368-4554-899f-c1fa014971c5",
                    "qualifiedName": "InTopic",
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "type": {
                            "type": "ElementType",
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
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
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
                        "guid": "c1ffa217-9e94-498e-96e7-b6facb5baa35",
                        "qualifiedName": "open-metadata/access-services/ConnectedAsset/inTopic",
                        "displayName": "open-metadata/access-services/ConnectedAsset/inTopic",
                        "description": "InTopic",
                        "address": "open-metadata/access-services/ConnectedAsset/inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "type": {
                        "type": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "0333478c-d5c8-41ea-836c-fc528a91a876",
                    "qualifiedName": "OutTopic",
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "type": {
                            "type": "ElementType",
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
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
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
                        "guid": "9bb198b0-93cd-4a3a-aee7-6ed6f0022481",
                        "qualifiedName": "open-metadata/access-services/ConnectedAsset/outTopic",
                        "displayName": "open-metadata/access-services/ConnectedAsset/outTopic",
                        "description": "OutTopic",
                        "address": "open-metadata/access-services/ConnectedAsset/outTopic"
                    }
                }
            },
            {
                "accessServiceId": 1001,
                "accessServiceAdminClass": "org.odpi.openmetadata.accessservices.assetconsumer.admin.AssetConsumerAdmin",
                "serviceName": "AssetConsumer",
                "serviceDescription": "Access assets through connectors",
                "serviceWiki": "https://cwiki.apache.org/confluence/display/ATLAS/Asset+Consumer+OMAS",
                "accessServiceOperationalStatus": "ENABLED",
                "accessServiceInTopic": {
                    "type": {
                        "type": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "2a454c0a-f270-4d5f-a595-4c472976aad5",
                    "qualifiedName": "InTopic",
                    "displayName": "InTopic",
                    "description": "InTopic",
                    "connectorType": {
                        "type": {
                            "type": "ElementType",
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
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
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
                        "guid": "c0499a1b-faf6-4a49-b0b0-56801cd89669",
                        "qualifiedName": "open-metadata/access-services/AssetConsumer/inTopic",
                        "displayName": "open-metadata/access-services/AssetConsumer/inTopic",
                        "description": "InTopic",
                        "address": "open-metadata/access-services/AssetConsumer/inTopic"
                    }
                },
                "accessServiceOutTopic": {
                    "type": {
                        "type": "ElementType",
                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                        "elementTypeName": "Connection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "37d4d189-83ae-40f5-b088-ebaa08847fa9",
                    "qualifiedName": "OutTopic",
                    "displayName": "OutTopic",
                    "description": "OutTopic",
                    "connectorType": {
                        "type": {
                            "type": "ElementType",
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
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
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
                        "guid": "494909e2-f2aa-44f5-8d71-4a12fa3e09de",
                        "qualifiedName": "open-metadata/access-services/AssetConsumer/outTopic",
                        "displayName": "open-metadata/access-services/AssetConsumer/outTopic",
                        "description": "OutTopic",
                        "address": "open-metadata/access-services/AssetConsumer/outTopic"
                    }
                }
            }
        ],
        "repositoryServicesConfig": {
            "auditLogConnections": [
                {
                    "type": {
                        "type": "ElementType",
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
                        "type": {
                            "type": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "f8a24f09-9183-4d5c-8408-aa1c8852a7d6",
                        "qualifiedName": "DefaultAuditLog.ConnectorType.cocoMDS1",
                        "displayName": "DefaultAuditLog.ConnectorType.cocoMDS1",
                        "description": "OMRS default audit log connector type.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider"
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
                        "guid": "836efeae-ab34-4425-89f0-6adf2faa1f2e",
                        "qualifiedName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "displayName": "DefaultAuditLog.Endpoint.cocoMDS1.auditlog",
                        "description": "OMRS default audit log endpoint.",
                        "address": "cocoMDS1.auditlog"
                    }
                }
            ],
            "openMetadataArchiveConnections": [],
            "localRepositoryConfig": {
                "metadataCollectionId": "239343dc-ff3f-4a01-8935-ad5882a7c7d1",
                "localRepositoryLocalConnection": {
                    "type": {
                        "type": "ElementType",
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
                        "type": {
                            "type": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "21422eb9-c6c1-4071-b96b-0572c9680260",
                        "qualifiedName": "DefaultInMemoryRepository.ConnectorType.cocoMDS1",
                        "displayName": "DefaultInMemoryRepository.ConnectorType.cocoMDS1",
                        "description": "OMRS default in memory local repository connector type.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider"
                    }
                },
                "localRepositoryRemoteConnection": {
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
                    "displayName": "DefaultRepositoryRESTAPI.Connection.cocoMDS1",
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
                        "displayName": "DefaultRepositoryRESTAPI.ConnectorType.cocoMDS1",
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
                        "displayName": "DefaultRepositoryRESTAPI.Endpoint.cocoMDS1",
                        "description": "OMRS default repository REST API endpoint.",
                        "address": "https://cocoMDS1:8080/openmetadata/repositoryservices/"
                    }
                },
                "eventsToSaveRule": "ALL",
                "eventsToSendRule": "ALL"
            },
            "enterpriseAccessConfig": {
                "enterpriseMetadataCollectionName": "cocoMDS1 Enterprise Metadata Collection",
                "enterpriseMetadataCollectionId": "ab5d9ea8-2dc3-4bd3-9523-b8113f9c4bd5",
                "enterpriseOMRSTopicConnection": {
                    "type": {
                        "type": "ElementType",
                        "elementTypeId": "82f9c664-e59d-484c-a8f3-17088c23a2f3",
                        "elementTypeName": "VirtualConnection",
                        "elementTypeVersion": 1,
                        "elementTypeDescription": "A connector for a virtual resource that needs to retrieve data from multiple places.",
                        "elementOrigin": "CONFIGURATION"
                    },
                    "guid": "2084ee90-717b-49a1-938e-8f9d49567b8e",
                    "qualifiedName": "DefaultEnterpriseTopic.Connection.cocoMDS1",
                    "displayName": "DefaultEnterpriseTopic.Connection.cocoMDS1",
                    "description": "OMRS default enterprise topic connection.",
                    "connectorType": {
                        "type": {
                            "type": "ElementType",
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "6536cb46-61f0-4f2d-abb4-2dadede30520",
                        "qualifiedName": "DefaultEnterpriseTopic.ConnectorType.cocoMDS1",
                        "displayName": "DefaultEnterpriseTopic.ConnectorType.cocoMDS1",
                        "description": "OMRS default enterprise connector type.",
                        "connectorProviderClassName": "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider"
                    },
                    "embeddedConnections": [
                        {
                            "type": "EmbeddedConnection",
                            "displayName": "Enterprise OMRS Events",
                            "embeddedConnection": {
                                "type": {
                                    "type": "ElementType",
                                    "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                                    "elementTypeName": "Connection",
                                    "elementTypeVersion": 1,
                                    "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                                    "elementOrigin": "CONFIGURATION"
                                },
                                "guid": "449f88f6-90d8-459b-885a-eba0ec1350e1",
                                "qualifiedName": "Enterprise OMRS Events",
                                "displayName": "Enterprise OMRS Events",
                                "description": "Enterprise OMRS Events",
                                "connectorType": {
                                    "type": {
                                        "type": "ElementType",
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
                                    "type": {
                                        "type": "ElementType",
                                        "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
                                        "elementTypeName": "Endpoint",
                                        "elementTypeVersion": 1,
                                        "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
                                        "elementOrigin": "CONFIGURATION"
                                    },
                                    "guid": "022fa859-663b-4dfd-97f6-00d7a4861ee3",
                                    "qualifiedName": "/open-metadata/repository-services/enterprise/cocoMDS1/OMRSTopic",
                                    "displayName": "/open-metadata/repository-services/enterprise/cocoMDS1/OMRSTopic",
                                    "description": "Enterprise OMRS Events",
                                    "address": "/open-metadata/repository-services/enterprise/cocoMDS1/OMRSTopic"
                                }
                            }
                        }
                    ]
                },
                "enterpriseOMRSTopicProtocolVersion": "V1"
            },
            "cohortConfigList": [
                {
                    "cohortName": "cohort1",
                    "cohortRegistryConnection": {
                        "type": {
                            "type": "ElementType",
                            "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                            "elementTypeName": "Connection",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "b9af734f-f005-4085-9975-bf46c67a099a",
                        "qualifiedName": "DefaultCohortRegistry.Connection.cocoMDS1.cohort1",
                        "displayName": "DefaultCohortRegistry.Connection.cocoMDS1.cohort1",
                        "description": "OMRS default cohort registry connection.",
                        "connectorType": {
                            "type": {
                                "type": "ElementType",
                                "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                                "elementTypeName": "ConnectorType",
                                "elementTypeVersion": 1,
                                "elementTypeDescription": "A set of properties describing a type of connector.",
                                "elementOrigin": "CONFIGURATION"
                            },
                            "guid": "2e1556a3-908f-4303-812d-d81b48b19bab",
                            "qualifiedName": "DefaultCohortRegistry.ConnectorType.cocoMDS1.cohort1",
                            "displayName": "DefaultCohortRegistry.ConnectorType.cocoMDS1.cohort1",
                            "description": "OMRS default cohort registry connector type.",
                            "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider"
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
                            "guid": "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0",
                            "qualifiedName": "DefaultCohortRegistry.Endpoint.cocoMDS1.cohort1.registrystore",
                            "displayName": "DefaultCohortRegistry.Endpoint.cocoMDS1.cohort1.registrystore",
                            "description": "OMRS default cohort registry endpoint.",
                            "address": "cocoMDS1.cohort1.registrystore"
                        }
                    },
                    "cohortOMRSTopicConnection": {
                        "type": {
                            "type": "ElementType",
                            "elementTypeId": "82f9c664-e59d-484c-a8f3-17088c23a2f3",
                            "elementTypeName": "VirtualConnection",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A connector for a virtual resource that needs to retrieve data from multiple places.",
                            "elementOrigin": "CONFIGURATION"
                        },
                        "guid": "023bb1f3-03dd-47ae-b3bc-dce62e9c11cb",
                        "qualifiedName": "DefaultCohortTopic.Connection.cohort1",
                        "displayName": "DefaultCohortTopic.Connection.cohort1",
                        "description": "OMRS default cohort topic connection.",
                        "connectorType": {
                            "type": {
                                "type": "ElementType",
                                "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                                "elementTypeName": "ConnectorType",
                                "elementTypeVersion": 1,
                                "elementTypeDescription": "A set of properties describing a type of connector.",
                                "elementOrigin": "CONFIGURATION"
                            },
                            "guid": "32843dd8-2597-4296-831c-674af0d8b837",
                            "qualifiedName": "DefaultCohortTopic.ConnectorType.cohort1",
                            "displayName": "DefaultCohortTopic.ConnectorType.cohort1",
                            "description": "OMRS default cohort topic connector type.",
                            "connectorProviderClassName": "org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider"
                        },
                        "embeddedConnections": [
                            {
                                "type": "EmbeddedConnection",
                                "displayName": "cohort1 OMRS Topic",
                                "embeddedConnection": {
                                    "type": {
                                        "type": "ElementType",
                                        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
                                        "elementTypeName": "Connection",
                                        "elementTypeVersion": 1,
                                        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
                                        "elementOrigin": "CONFIGURATION"
                                    },
                                    "guid": "fb2c4671-17e8-4d85-80fa-11ce8b100496",
                                    "qualifiedName": "cohort1 OMRS Topic",
                                    "displayName": "cohort1 OMRS Topic",
                                    "description": "cohort1 OMRS Topic",
                                    "connectorType": {
                                        "type": {
                                            "type": "ElementType",
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
                                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
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
                                        "guid": "80855716-44f7-40c3-80ab-c30717294a3d",
                                        "qualifiedName": "/open-metadata/repository-services/cohort/cohort1/OMRSTopic",
                                        "displayName": "/open-metadata/repository-services/cohort/cohort1/OMRSTopic",
                                        "description": "cohort1 OMRS Topic",
                                        "address": "/open-metadata/repository-services/cohort/cohort1/OMRSTopic"
                                    }
                                }
                            }
                        ]
                    },
                    "cohortOMRSTopicProtocolVersion": "V1",
                    "eventsToProcessRule": "ALL"
                }
            ]
        }
    }
}

```

Most of the configuration is made up of connection objects that are used to
configure the repository services connectors.

## Activating and deactivating the open metadata services

With the configuration document in place, the services can be activated as
follows:

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance

```

and deactivated, as follows:

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance

```

The configuration document is not changed by these calls.

If you want to delete the server's configuration document then issue:

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1

```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.