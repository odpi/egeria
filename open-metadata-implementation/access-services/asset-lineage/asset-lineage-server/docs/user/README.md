<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Lineage OMAS REST Interfaces

The Asset Lineage OMAS REST services use a URL that begins

```
<serverRootURL>/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}
```

where:
* **serverName** is the name of the server
* **userId** is the userId of the person (or server/engine/process) making the call.

The specific REST APIs for the Asset Lineage OMAS extend this basic URL.

## publish entities

* [publish lineage](publish-lineage-with-rest.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.