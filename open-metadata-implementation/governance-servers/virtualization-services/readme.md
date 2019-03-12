<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Virtualizer
Virtualizer communicates with Information View OMAS and virtualization tool which is currently Gaian.

Virtualizer has three main functions:
1. listen to Information View OMAS Out topic(specified by property information-view-out-topic) and retrieve InformationViewEvent event (json structure);
2. create Business Logical View(business terms are used as column names) and Technical Logical View(source table column names are used as view column names), containing only the columns with business terms assigned
3. create Business View json file and Technical View json file, notify Information View OMAS through publishing on Information View OMAS In topic (specified by property information-view-in-topic).

## OMAG server configuration

Virtualizer is now running as a service for [OMAG Server Platform](../server-chassis). In this case, the service should be configured and initialized by the restful APIs provided by the platform.

Here are the steps to run Vitualizer
- Start [OMAG Server Platform](../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial)

- Configure event bus

**POST** following JSON object 
````json
{"consumer": {
	"bootstrap.servers" :  "{{kafka-server-address}}"
},
"producer": {
	"bootstrap.servers" :  "{{kafka-server-address}}",
	"key.serializer": "org.apache.kafka.common.serialization.StringSerializer",
	"value.serializer": "org.apache.kafka.common.serialization.StringSerializer"
}
}
````
to the following address
```
{{url-virtualizer}}/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/event-bus?topicURLRoot={{topic-root}}
```

- Set up virtualizer configuration

**POST** following JSON object 
````json
{
	"class": "VirtualizationConfig",
	"virtualizationProvider": "{{connector-class-name}}",
	"ivInTopicName": "{{iv-in-topic-name}}",
	"ivOutTopicName": "{{iv-out-topic-name}}"
}
````
to the following address
```
{{url-virtualizer}}/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/virtualization-service/configuration
```

- Start the instance of the OMAG Server

**POST** to the following address
```
{{url-virtualizer}}/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/instance
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.