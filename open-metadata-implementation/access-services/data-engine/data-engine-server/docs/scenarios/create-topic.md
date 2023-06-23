<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create topic

Create a topic, with the associated event types. For each event type it creates the associated event schema attributes
More examples with all available properties for a topic can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-topics-lineage_examples.postman_collection.json)

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/topics

{
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform",
    "topic": {
        "qualifiedName": "(topic)=test-topic",
        "displayName": "test-topic",
        "eventTypes": [
            {
                "qualifiedName": "(topic)=test-topic::(eventType)=test-event",
                "displayName": "test-event",
                "eventSchemaAttributes": [
                    {
                        "qualifiedName": "(topic)=test-topic::(eventType)=test-event::(eventAttribute)=event-attribute",
                        "displayName": "event-attribute"
                    }
                ]
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







