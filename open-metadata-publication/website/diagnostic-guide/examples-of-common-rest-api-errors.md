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

If the server is not running when a request is made, but the platform is running then this is the type of response returned.

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

## A request is using the wrong metadata provenance

The following exception is from an integration daemon that has been set up with a null
[metadataSourceQualifiedName property](../../../open-metadata-implementation/admin-services/docs/user/configuring-the-integration-services.md)
making it different to that used when the metadata it is managing was created.

The nested message `OMAG-REPOSITORY-HANDLER-400-008` can also occur through other services if metadata is managed with
the wrong provenance values.

```
Wed May 12 20:27:47 BST 2021 postgresConnectorServer Exception POSTGRES-CONNECTOR-0002 The method updateView generated a UserNotAuthorized. org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException OMAG-REPOSITORY-HANDLER-400-008 Method updateDatabaseView is unable to modify ClassificationDef instance 947016c6-b812-4bc8-a0f4-2e44bb640603 because it is has metadata provenance of External Source with an externalSourceGUID of 6abf01f2-75d0-489b-94e6-df704fc6753b and an externalSourceName of myDBServer and user OMAGServer issued a request with the Local Cohort metadata provenance set
Wed May 12 20:27:47 BST 2021 postgresConnectorServer Exception POSTGRES-CONNECTOR-0002 Supplementary information: log record id 618af417-9c97-45a1-bdfb-af7906a8b40c org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException returned message of OMAG-REPOSITORY-HANDLER-400-008 Method updateDatabaseView is unable to modify ClassificationDef instance 947016c6-b812-4bc8-a0f4-2e44bb640603 because it is has metadata provenance of External Source with an externalSourceGUID of 6abf01f2-75d0-489b-94e6-df704fc6753b and an externalSourceName of myDBServer because user OMAGServer is using a local cohort interface and stacktrace of
UserNotAuthorizedException{userId='OMAGServer', reportedHTTPCode=400, reportingClassName='org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler', reportingActionDescription='updateDatabaseView', reportedErrorMessageId='OMAG-REPOSITORY-HANDLER-400-008', reportedErrorMessageParameters=[updateDatabaseView, ClassificationDef, 947016c6-b812-4bc8-a0f4-2e44bb640603, External Source, 6abf01f2-75d0-489b-94e6-df704fc6753b, myDBServer, OMAGServer], reportedSystemAction='The system is unable to modify the requested instance because it does not have ownership rights to the instance.', reportedUserAction='Route the request through a process that is set up to use the correct external source identifiers.', reportedCaughtException=null, relatedProperties={userId=OMAGServer}}
	at org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler.throwUserNotAuthorizedException(RESTExceptionHandler.java:338)
	at org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler.detectAndThrowStandardExceptions(RESTExceptionHandler.java:122)
	at org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient.callVoidPostRESTCall(FFDCRESTClient.java:309)
	at org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient.updateDatabaseView(DatabaseManagerClient.java:1552)
	at org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext.updateDatabaseView(DatabaseIntegratorContext.java:737)
	at org.odpi.openmetadata.adapters.connectors.integration.postgres.PostgresDatabaseConnector.updateView(PostgresDatabaseConnector.java:745)
	at org.odpi.openmetadata.adapters.connectors.integration.postgres.PostgresDatabaseConnector.updateViews(PostgresDatabaseConnector.java:673)
	at org.odpi.openmetadata.adapters.connectors.integration.postgres.PostgresDatabaseConnector.updateSchema(PostgresDatabaseConnector.java:394)
	at org.odpi.openmetadata.adapters.connectors.integration.postgres.PostgresDatabaseConnector.updateSchemas(PostgresDatabaseConnector.java:312)
	at org.odpi.openmetadata.adapters.connectors.integration.postgres.PostgresDatabaseConnector.updateDatabase(PostgresDatabaseConnector.java:219)
	at org.odpi.openmetadata.adapters.connectors.integration.postgres.PostgresDatabaseConnector.refresh(PostgresDatabaseConnector.java:90)
	at org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler.refreshConnector(IntegrationConnectorHandler.java:420)
	at org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.IntegrationDaemonThread.run(IntegrationDaemonThread.java:106)
	at java.base/java.lang.Thread.run(Thread.java:829)
```

There is more information on [metadata provenance here](../metadata-provenance).

----

* Return to [diagnostic guide](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.