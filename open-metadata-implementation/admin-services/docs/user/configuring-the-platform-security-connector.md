<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuring the platform security connector

The [OMAG Server Platform](../concepts/omag-server-platform.md)
provides both configuration and diagnostic services
for [OMAG Servers](../concepts/omag-server.md) which in themselves
provide access to a wide variety of information and control points.

Therefore it is necessary to provide authorization services
relating to the use of the platform services.

Egeria provides [a platform security authorization capability](../../../common-services/metadata-security).
It is implemented in a platform security connector
that is called whenever requests are made for to the server platform services.

Security is configured for a specific platform once it is running by
using the following command.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/platform/security/connection
```
This passes in a connection  used to create the platform security connector and the `platformURLRoot` of the platform
in the request body.  For example, this is the request body that would
set up the [sample platform security connector](../../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) provided for the Coco Pharmaceuticals case study:
```json
{
    "class": "PlatformSecurityRequestBody",
    "urlRoot": "{{platformURLRoot}}",
    "platformSecurityConnection" : {
	    "class": "Connection",
	    "connectorType": {
	        "class": "ConnectorType",
	        "connectorProviderClassName": "org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaPlatformSecurityProvider"
	    }
    }
}
```

# Querying which connector is in use in an OMAG Server Platform

It is possible to query the setting of the platform security connector
using the following command:

```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/platform/security/connection
```

If the response is:
```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200
}
```
then no connector is set up and then no connector is set up and no authorization checks are occurring.

If the response looks more like the JSON below, a connector is configured.  The
`connectorProviderClassName` tells you which connector is being used.

```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200,
    "connection": {
        "class": "Connection",
        "connectorType": {
            "class": "ConnectorType",
            "connectorProviderClassName": "org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaPlatformSecurityProvider"
        }
    }
}
```

# Removing the configured configuration document store connector

It is possible to remove the configuration for the connector using
the following command:

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/platform/security/connection
```

This removes all authorization checking from the platform services.

----
Return to [Configuring the OMAG Server Platform](configuring-the-omag-server-platform.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.