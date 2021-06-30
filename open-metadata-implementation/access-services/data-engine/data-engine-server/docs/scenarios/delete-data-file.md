<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Delete data file

Delete a DataFile with columns and relationships

More examples can be found in the
[sample collection](../../../docs/samples/collections/DataEngine-asset_endpoints.postman_collection.json)

```
DELETE {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/data-files/

{
    "guid": "dataFileGUID",
    "qualifiedName": "(host)=HOST::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv",
    "externalSourceName": "(organization)=Company::(project)=ExternalDataPlatform"
}
```
`externalSourceName` - qualifiedName of the external data engine tool.<br>
`guid` - optional property describing the unique identifier of the data file to be deleted
`qualifiedName` - optional property describing the qualifiedName of the data files to be deleted<br>
Note that you must provide either the qualifiedName, or the guid of the data file <br>
`VoidRespone` - void response with status and error message if failing


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







