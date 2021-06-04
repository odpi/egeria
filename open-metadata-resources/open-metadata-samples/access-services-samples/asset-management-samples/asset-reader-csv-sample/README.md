<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# CSV File Reader Sample

CSVFileReaderSample illustrates the use of the Asset Consumer OMAS API to create connectors to CSV files.
It also contrasts the use of the 
[Open Connector Framework (OCF) Connector Broker](../../../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connector-broker.md)
with the use of the Access Consumer OMAS java client for creating connectors.

The `main()` method controls the operation of the sample.
The parameters are passed space separated.

1. file name 
2. server name
3. URL root for the server 
4. client userId

The parameters are used to override the sample's default values.

```
        String  fileName = "open-metadata-resources/open-metadata-samples/access-services-samples/asset-management-samples/ContactList.csv";
        String  serverName = "cocoMDS4";
        String  serverURLRoot = "https://localhost:9444";
        String  clientUserId = "erinoverview";
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.