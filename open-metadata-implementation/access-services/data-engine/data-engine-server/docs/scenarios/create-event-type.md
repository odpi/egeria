<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create an event type

Create an event type, with the associated event schema attributes.
More examples with all available properties for an event type can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-topics-lineage_examples.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/event-types

{
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform",
    "topicQualifiedName": "(topic)=test-topic",
    "eventType": {
        "qualifiedName": "(topic)=test-topic::(eventType)=test-event",
        "displayName": "test-event",
        "eventSchemaAttributes": [
            {
                "qualifiedName": "(topic)=test-topic::(eventType)=test-event::(eventAttribute)=event-attribute",
                "displayName": "event-attribute"
            }
        ]
    }
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process
`GUIDResponse` - response containing the topic GUID, with status and error message if failing  

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







