<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Lookup registration

Lookup registration submitted by an external tool.

```
POST {{url-omas}}/servers/{{server-id-omas}}/open-metadata/access-services/information-view/users/{{user-id}}/registration/lookup
```

```json
{
   "class": "RegistrationRequestBody",
   "softwareServerCapability": {
             "class": "SoftwareServerCapabilitySource",
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

`RegistrationResponse` - response containing the registration details or
`VoidReponse` with error message and status.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.







