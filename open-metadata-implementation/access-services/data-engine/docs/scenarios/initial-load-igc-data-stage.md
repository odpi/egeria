<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Initial load use case

[Initial load](../samples/initial-load/data-stage) use case shows the integration between 
Data Engine OMAS and IBM Data Stage.

The [process request body](../samples/initial-load/data-engine/payloads/process.json) describes 
the open metadata entities corresponding to the Data Stage ETL job.

Request to use is: 
```

POST {{omas-url}}/servers/{{server-id-omas}}/open-metadata/access-services/data-engine/users/{{user-id}}/processes


ProcessListResponse - response containing the guids of the created processes 
and the guids of the failed processes with error message and status

```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.