<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Getting a connection by name with REST

This operation retrieves a [connection](../../../../../frameworks/open-connector-framework/docs/concepts/connection.md) object
from the [open metadata repositories](../../../../../repository-services/docs/open-metadata-repository.md)
and returns it to the caller.


This rest call is used by the [get a connector by connection name](../../../asset-consumer-client/docs/user/java-client/get-connector-by-name-with-java.md)

```
GET {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/connections/by-name/{connectionName}
```

```java
ConnectionResponse 
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.