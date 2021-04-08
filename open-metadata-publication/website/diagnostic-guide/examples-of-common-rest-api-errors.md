<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Examples of common errors that occur when using Egeria's REST APIs


## A server is not running on the called platform

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

## The requested service is not active on the called server

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