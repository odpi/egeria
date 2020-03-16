<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Initial load use case

[Initial load](../samples/initial-load/data-stage) use case shows the integration between 
Data Engine OMAS and IBM Data Stage.

The [processes request body](../samples/initial-load/data-engine/payloads/create-processes.json) describes 
the open metadata entities corresponding to the Data Stage ETL job.

>Note: Data Engine OMAS must have access to the IGC entities. 
Check [egeria-connector-ibm-information-server](https://github.com/odpi/egeria-connector-ibm-information-server#ibm-infosphere-information-server-connectors)
for more details.

Request to use is: 

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}/processes
```

`ProcessListResponse` - response containing the guids of the created processes 
and the guids of the failed processes with error message and status.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.