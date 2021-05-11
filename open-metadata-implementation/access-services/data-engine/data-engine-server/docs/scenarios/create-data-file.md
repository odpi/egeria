<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Create data file

Create a data file, with the associated schema, columns, folder hierarchy and a connection. The schema is generated
automatically, the columns according to the payload, the folder structure is based on the value of file.pathName and 
the connection, which also includes an endpoint, is based on the value of optional fields file.protocol and
file.networkAddress

A csv file can be created by changing the value of field file.type from "DataFile" into "CSVFile"

More examples with all available properties for a data file can be found in the 
[sample collection](../../../docs/samples/collections/DataEngine-technical-assets.postman_collection.json) 
 

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/data-files

{
    "externalSourceName" : "Company::Project::ExternalDataEngine",
    "file" :{
        "type" : "DataFile", 
        "qualifiedName" : "(file)=DATAFILE.DAT",
        "displayName" : "dataFile.dat",
        "pathName" : "/home/files/dataFile.dat",
        "protocol" : "ftp",
        "networkAddress" : "localhost",
        "columns" : [ 
            {
                "qualifiedName" : "(file)=DATAFILE.DAT::(column)=COLUMN-A",
                "displayName" : "column-a"
            },
            {
                "qualifiedName" : "(file)=DATAFILE.DAT::(column)=COLUMN-B",
                "displayName" : "column-b"
            }
        ]
    }
}
```

`externalSourceName` - qualifiedName of the external data engine tool.
 Note that you need to register the data engine tool with [register-data-engine-tool](register-data-engine-tool.md) 
 before creating any process  
`GUIDResponse` - response containing the data file GUID, with status and error message if failing  
`type` - type of file to be created, either a DataFile or a CSVFile  
`pathName` - file system location of the data file  
`protocol` - used to create connection and endpoint  
`networkAddress` - used to create connection and endpoint  


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







