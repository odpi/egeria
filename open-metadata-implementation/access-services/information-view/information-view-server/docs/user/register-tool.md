<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Register tool

Register an external tool.

```

POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/register
```

```json
{
  "class": "RegistrationRequestBody",
  "softwareServerCapability": {
    "class": "SoftwareServerCapabilitySource",
    "qualifiedName":"internal id",
    "author": "owner-test",
    "userId": "cognosToolId",
    "lastModifiedTime": 1547838663347,
    "lastModifier": "owner",
    "name": "cognos reporting tool",
    "version": "1",
    "type": "reporting"
  }
}
```
RegistrationResponse for success containing details of the software server capability entity create or error response otherwise

```
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.