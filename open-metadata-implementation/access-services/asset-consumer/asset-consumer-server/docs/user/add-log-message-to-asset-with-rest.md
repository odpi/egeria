<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Add log message to asset

Creates an Audit log record for the asset.  This log record is stored in the local server's Audit Log.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/assets/{requestType}/log-records
```

```java
LogRecordRequestBody  requestBody = new LogRecordRequestBody();
requestBody.setConnectorInstanceId(connectorInstanceId);
requestBody.setConnectionName(connectionName);
requestBody.setConnectorType(connectorType);
requestBody.setContextId(contextId);
requestBody.setMessage(message);

VoidResponse restResult 
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.