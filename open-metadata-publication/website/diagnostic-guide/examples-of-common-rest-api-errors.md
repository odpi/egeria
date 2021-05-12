<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Examples of common errors that occur when using Egeria's REST APIs



## The platform is not running

If the OMAG Server Platform that should be hosting a server is not running when a request is made to an OMAG Server,
then there is a `ECONNREFUSED` network error.  For example:

```
GET https://localhost:9446/servers/cocoMDS1/open-metadata/repository-services/users/erinoverview/enterprise/instances/entity/d4d64735-4ddc-4a1a-a899-7c917ff5f763
Error: connect ECONNREFUSED 127.0.0.1:9446
```

## A server is not running on the called platform

If the OMAG Server Platform is running but the requested server is not running then the following error is returned.

```json
{
    "class": "EntityDetailResponse",
    "relatedHTTPCode": 404,
    "actionDescription": "getEntityDetail",
    "exceptionClassName": "org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException",
    "exceptionErrorMessage": "OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user erinoverview",
    "exceptionErrorMessageId": "OMAG-MULTI-TENANT-404-001",
    "exceptionErrorMessageParameters": [
        "cocoMDS1",
        "erinoverview"
    ],
    "exceptionSystemAction": "The system is unable to process the request because the server is not running on the called platform.",
    "exceptionUserAction": "Verify that the correct server is being called on the correct platform and that this server is running. Retry the request when the server is available.",
    "exceptionProperties": {
        "parameterName": "serverName"
    }
}
```

## The requested service is not active on the called server

If the ser

```json
{
    "class": "VoidResponse",
    "relatedHTTPCode": 404,
    "exceptionClassName": "org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException",
    "actionDescription": "refreshService",
    "exceptionErrorMessage": "OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user garygeeke",
    "exceptionErrorMessageId": "OMAG-MULTI-TENANT-404-001",
    "exceptionErrorMessageParameters": [
        "cocoMDS1",
        "garygeeke"
    ],
    "exceptionSystemAction": "The system is unable to process the request because the server is not running on the called platform.",
    "exceptionUserAction": "Verify that the correct server is being called on the correct platform and that this server is running. Retry the request when the server is available.",
    "exceptionProperties": {
        "serverName": "cocoMDS1",
        "parameterName": "serverName"
    }
}
```

----

* Return to [diagnostic guide](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.