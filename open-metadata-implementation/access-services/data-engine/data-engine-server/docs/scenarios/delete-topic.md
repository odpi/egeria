<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Delete topic

Delete a Topic with event types

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-topics-lineage_examples.postman_collection.json)

```
DELETE {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/topics/

{
    "guid": "topicGUID",
    "qualifiedName": "(topic)=test-topic",
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.<br>
`guid` - optional property describing the unique identifier of the topic to be deleted
`qualifiedName` - optional property describing the qualifiedName of the topic to be deleted<br>
Note that you must provide either the qualifiedName, or the guid of the topic <br>
`VoidRespone` - void response with status and error message if failing


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







