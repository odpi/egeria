<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Deprecated](../../../open-metadata-publication/website/images/egeria-content-status-deprecated.png#pagewidth)

# Virtualizer Server

The Virtualizer communicates with Information View OMAS and virtualization tool which is currently Gaian.
The design of the server allows for other data virtualization platforms to be
plugged in by changing the view generation connector.

Virtualizer has three main functions:
1. listen to Information View OMAS Out topic(specified by property information-view-out-topic) and retrieve InformationViewEvent event (json structure);
2. create Business Logical View(business terms are used as column names) and Technical Logical View(source table column names are used as view column names), containing only the columns with business terms assigned
3. create Business View json file and Technical View json file, notify Information View OMAS through publishing on Information View OMAS In topic (specified by property information-view-in-topic).

## OMAG server configuration

The Virtualizer is a server that runs on the [OMAG Server Platform](../../../open-metadata-publication/website/omag-server).
In this case, the service should be configured and initialized by the restful APIs provided by the platform.

Here are the steps to run Virtualizer
1. Start an [OMAG Server Platform](../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial)

- Configure event bus

**POST** following JSON object 

```json
{
  "consumer":
  {
    "bootstrap.servers" :  "{kafkaServerAddress}"
  },
  "producer":
  {
    "bootstrap.servers" :  "{kafkaServerAddress}",
    "key.serializer": "org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer": "org.apache.kafka.common.serialization.StringSerializer"
  }
}
```

to the following address

```
{serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/event-bus?topicURLRoot={topicRoot}
```

- Set up virtualizer configuration

**POST** following JSON object 

```json
{
  "class": "VirtualizationConfig",
  "virtualizationProvider": "{fullyQualifiedJavaClassName}",
  "virtualizerOutboundTopicName": "{iv-in-topic-name}",
  "virtualizerInboundTopicName": "{iv-out-topic-name}",
  "virtualizationSolutionConfig":
  {
    "frontendName": "",
    "serverAddress": "",
    "databaseName": "",
    "schema": "",
    "username": "",
    "password": "",
    "timeoutInSecond": 5,
    "create": true,
    "derbyDriver": "org.apache.derby.jdbc.ClientDriver",
    "gdbNode": "GDB_NODE",
    "logicTableName": "LTNAME",
    "logicTableDefinition": "LTDEF",
    "getLogicTables": "call listlts()"
  }
}
```

to the following address

```
{serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/virtualization-service/configuration
```

The object *virtualizationSolutionConfig* is the information required to implement the specific connector to the virtualization solutions. The keys should be modified based on the information needed by the connector.

- Start the instance of the OMAG Server

**POST** to the following address

```
{serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/instance
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.