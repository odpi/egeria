<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Lookup registration

Lookup registration submitted by an external tool.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/registration/lookup
```

```json
{
   "class": "RegistrationRequestBody",
   "softwareServerCapability": {
             "class": "SoftwareServerCapabilitySource",
             "qualifiedName":"internal id"
   }
 }
```

`RegistrationResponse` - response containing the registration details or
`VoidReponse` with error message and status.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







