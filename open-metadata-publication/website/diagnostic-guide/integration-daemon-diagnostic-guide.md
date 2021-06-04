<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Trouble shooting issues with the integration daemon

The [integration daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md)
is designed to run as a background process.  It hosts 
[integration connectors](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md)
that exchange metadata with third party technologies.  It also
make calls to a metadata server in order to retrieve and maintain metadata in the open metadata ecosystem.

Most of the time an integration daemon operates without intervention.  However, because digital landscapes are
dynamic, the integration daemon outputs messages to the audit log when it is starting, stopping,
refreshing the connectors and also if one of the connectors throws a exception.
This allows a remote operator to monitor for issues.
The integration daemon also has simple REST API for manually restarting and refreshing the connectors
to enable the integration daemon to recover from errors.

If a connector is incorrectly configured, the integration daemon's configuration document needs to be changed
using [administration commands](../../../open-metadata-implementation/admin-services/docs/user/configuring-the-integration-services.md)
and the integration daemon [restarted](../../../open-metadata-implementation/admin-services/docs/user/starting-and-stopping-omag-server.md).

## Sources of errors

There are three likely sources of error if an integration daemon is not working properly:

* An integration connector is incorrectly configured or misbehaving
* The third party technology is not available or misbehaving
* The metadata server is not available or misbehaving

The first step is work out which of these is occurring (remembering that there may be multiple failures occurring)
and then the likely cause.  For example:

* Is the configuration correct?
* Are the servers running?
* Do the components have appropriate security access?
* Is the code working properly?

## Diagnostics tools for the integration daemon

The integration daemon has a number of tools to help you diagnose problems.

### Audit Log

The integration daemon (and potentially the connectors) are writing to the audit log destinations
configured for the integration daemon.  This includes details of both the successful metadata exchange
and any errors or failures.

#### Server start up messages

The start up messages confirm that the integration daemon started successfully.
Here is an example of the start up of a server:

```
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the Integration Daemon called exchangeDL01
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMAG-ADMIN-0001 The exchangeDL01 server is configured with a max page size of 100
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMRS-AUDIT-0001 The Open Metadata Repository Services (OMRS) is initializing the subsystems to support a new server
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMRS-AUDIT-0007 The Open Metadata Repository Services (OMRS) has initialized
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OPEN-METADATA-SECURITY-0003 The Open Metadata Security Service org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityConnector for server exchangeDL01 is initializing
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMAG-ADMIN-0100 The governance services subsystem for the Integration Daemon called exchangeDL01 is about to start
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0001 The integration daemon services are initializing in server exchangeDL01
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMIS-FILES-INTEGRATOR-0001 The files integrator context manager is being initialized for calls to server cocoMDS1 on platform https://localhost:9444
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0008 A new integration connector named OakDeneLandingAreaFilesMonitor is initializing in integration service Files Integrator OMIS running in integration daemon exchangeDL01, permitted synchronization is: From Third Party
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMIS-FILES-INTEGRATOR-0002 Creating context for integration connector OakDeneLandingAreaFilesMonitor (41244d6b-2054-49a5-bd24-328a82dc8552) connecting to third party technology HospitalLandingArea with permitted synchronization of From Third Party and service options of null
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Exception INTEGRATION-DAEMON-SERVICES-0031 The integration connector OakDeneLandingAreaFilesMonitor method initialize has returned with a org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception containing message OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user exchangeDL01npa
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0008 A new integration connector named OldMarketLandingAreaFilesMonitor is initializing in integration service Files Integrator OMIS running in integration daemon exchangeDL01, permitted synchronization is: From Third Party
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMIS-FILES-INTEGRATOR-0002 Creating context for integration connector OldMarketLandingAreaFilesMonitor (723f449e-4229-4197-ba84-500bc467d39a) connecting to third party technology HospitalLandingArea with permitted synchronization of From Third Party and service options of null
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Exception INTEGRATION-DAEMON-SERVICES-0031 The integration connector OldMarketLandingAreaFilesMonitor method initialize has returned with a org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception containing message OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user exchangeDL01npa
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0008 A new integration connector named DropFootClinicalTrialResultsFilesMonitor is initializing in integration service Files Integrator OMIS running in integration daemon exchangeDL01, permitted synchronization is: From Third Party
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMIS-FILES-INTEGRATOR-0002 Creating context for integration connector DropFootClinicalTrialResultsFilesMonitor (57192bbd-6221-4770-8833-7da48e295be3) connecting to third party technology DropFootClinicalTrialResults with permitted synchronization of From Third Party and service options of null
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Exception INTEGRATION-DAEMON-SERVICES-0031 The integration connector DropFootClinicalTrialResultsFilesMonitor method initialize has returned with a org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception containing message OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user exchangeDL01npa
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMIS-DATABASE-INTEGRATOR-0001 The database integrator context manager is being initialized for calls to server cocoMDS1 on platform https://localhost:9444
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0008 A new integration connector named OakDeneLandingAreaDatabaseMonitor is initializing in integration service Database Integrator OMIS running in integration daemon exchangeDL01, permitted synchronization is: From Third Party
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Exception INTEGRATION-DAEMON-SERVICES-0031 The integration connector OakDeneLandingAreaDatabaseMonitor method initialize has returned with a org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception containing message OMIS-DATABASE-INTEGRATOR-400-001 Integration connector OakDeneLandingAreaDatabaseMonitor is not of the correct type to run in the Database Integrator OMIS integration service.  It must inherit from org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0040 The integration daemon thread for integration daemon exchangeDL01 has started
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Information INTEGRATION-DAEMON-SERVICES-0041 The integration daemon thread is refreshing integration connector OakDeneLandingAreaFilesMonitor for the first time in the exchangeDL01 integration daemon instance
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Information INTEGRATION-DAEMON-SERVICES-0041 The integration daemon thread is refreshing integration connector OldMarketLandingAreaFilesMonitor for the first time in the exchangeDL01 integration daemon instance
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Information INTEGRATION-DAEMON-SERVICES-0041 The integration daemon thread is refreshing integration connector DropFootClinicalTrialResultsFilesMonitor for the first time in the exchangeDL01 integration daemon instance
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Information INTEGRATION-DAEMON-SERVICES-0041 The integration daemon thread is refreshing integration connector OakDeneLandingAreaDatabaseMonitor for the first time in the exchangeDL01 integration daemon instance
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup INTEGRATION-DAEMON-SERVICES-0013 The integration daemon exchangeDL01 has initialized
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMAG-ADMIN-0101 The governance services subsystem for the Integration Daemon called exchangeDL01 has completed start up
Tue Feb 02 20:57:50 GMT 2021 exchangeDL01 Startup OMAG-ADMIN-0004 The exchangeDL01 server has successfully completed start up.  The following services are running: [Open Metadata Repository Services (OMRS), Files Integrator OMIS, Database Integrator OMIS, Integration Daemon Services]
```

The integration daemon successfully completes start up running the
[Files Integrator OMIS](../../../open-metadata-implementation/integration-services/files-integrator)
with three integration connectors and the
[Database Integrator OMIS](../../../open-metadata-implementation/integration-services/database-integrator)
with one connector.
It then begins its periodic refresh of the connectors (set to every 10 minutes in this configuration).

However, there is a sign of trouble in that each connector is reporting an exception at start up.
Message `INTEGRATION-DAEMON-SERVICES-0031` is reporting
```
INTEGRATION-DAEMON-SERVICES-0031 The integration connector OldMarketLandingAreaFilesMonitor method initialize 
has returned with a org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception 
containing message OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request 
from user exchangeDL01npa
```
So the metadata server `cocoMDS1` is down and the connectors can not begin processing until it is restarted.

There is also an error with the `OakDeneLandingAreaDatabaseMonitor`
in that it is configured to work with the
[Database Integrator OMIS](../../../open-metadata-implementation/integration-services/database-integrator)
but the connector does not implement the right interface for this service.

```
INTEGRATION-DAEMON-SERVICES-0031 The integration connector OakDeneLandingAreaDatabaseMonitor method initialize
has returned with a org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception 
containing message OMIS-DATABASE-INTEGRATOR-400-001 Integration connector OakDeneLandingAreaDatabaseMonitor is not 
of the correct type to run in the Database Integrator OMIS integration service.  
It must inherit from org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector
```

This is will not be resolved until either the code is corrected to implement the Database Integrator OMIS connector interface, 
or the connector is reconfigured to run in the integration service that matches its implementation.

### Retrieving status of the connectors

It is also possible to retrieve the status of the connectors in the
integration daemon through the following command:
```
GET {serverRootURL}/servers/{serverName}/open-metadata/integration-daemon/users/{userId}/status
```
Provided the integration daemon is running, it returns a list of connectors with their statuses.
You can see the same errors that where recorded in the start up messages.

```json
{
    "class": "IntegrationDaemonStatusResponse",
    "relatedHTTPCode": 200,
    "integrationServiceSummaries": [
        {
            "integrationServiceId": 4005,
            "integrationServiceFullName": "Files Integrator OMIS",
            "integrationServiceURLMarker": "files-integrator",
            "integrationServiceDescription": "Extract metadata about files stored in a file system or file manager.",
            "integrationServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/",
            "integrationConnectorReports": [
                {
                    "connectorName": "OakDeneLandingAreaFilesMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T20:57:50.647+00:00",
                    "lastRefreshTime": "2021-02-02T21:01:11.399+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user exchangeDL01npa"
                },
                {
                    "connectorName": "OldMarketLandingAreaFilesMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T20:57:50.657+00:00",
                    "lastRefreshTime": "2021-02-02T21:01:11.399+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user exchangeDL01npa"
                },
                {
                    "connectorName": "DropFootClinicalTrialResultsFilesMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T20:57:50.668+00:00",
                    "lastRefreshTime": "2021-02-02T21:01:11.399+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "OMAG-MULTI-TENANT-404-001 The OMAG Server cocoMDS1 is not available to service a request from user exchangeDL01npa"
                }
            ]
        },
        {
            "integrationServiceId": 4004,
            "integrationServiceFullName": "Database Integrator OMIS",
            "integrationServiceURLMarker": "database-integrator",
            "integrationServiceDescription": "Extract metadata such as schema, tables and columns from database managers.",
            "integrationServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/",
            "integrationConnectorReports": [
                {
                    "connectorName": "OakDeneLandingAreaDatabaseMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T20:57:50.726+00:00",
                    "lastRefreshTime": "2021-02-02T21:01:11.399+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "OMIS-DATABASE-INTEGRATOR-400-001 Integration connector OakDeneLandingAreaDatabaseMonitor is not of the correct type to run in the Database Integrator OMIS integration service.  It must inherit from org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector"
                }
            ]
        }
    ]
}
```
If the `cocoMDS1` metadata server is restarted, the status of the connectors does not change.
This is because once a connector goes into **FAILED**
status (caused by the connector throwing an exception on one of its standard methods), it needs to be restarted before the integration daemon will start calling it again.  It is possible to restart all connectors
registered with an integration service with the following command:

```
GET {serverRootURL}/servers/{serverName}/open-metadata/integration-daemon/users/{userId}/integration-service/{service-url-marker}/restart
```
If you only want to restart a single connector, add the connector name to the request body.
The `service-url-marker` for each integration services can be located using the following command:

```
GET {serverRootURL}/servers/{serverName}/platform-services/users/{userId}/server-platform/registered-services/integration-services
```
which returns a list of the valid integration services:
```json
{
    "class": "RegisteredOMAGServicesResponse",
    "relatedHTTPCode": 200,
    "services": [
        {
            "serviceName": "Files Integrator OMIS",
            "serviceURLMarker": "files-integrator",
            "serviceDescription": "Extract metadata about files stored in a file system or file manager.",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/"
        },
        {
            "serviceName": "Security Integrator OMIS",
            "serviceURLMarker": "security-integrator",
            "serviceDescription": "Distribute security properties to security enforcement points.",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/security-integrator/"
        },
        {
            "serviceName": "Database Integrator OMIS",
            "serviceURLMarker": "database-integrator",
            "serviceDescription": "Extract metadata such as schema, tables and columns from database managers.",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/"
        },
        {
            "serviceName": "Lineage Integrator OMIS",
            "serviceURLMarker": "lineage-integrator",
            "serviceDescription": "Manage capture of lineage from a third party tool.",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/lineage-integrator/"
        },
        {
            "serviceName": "Catalog Integrator OMIS",
            "serviceURLMarker": "catalog-integrator",
            "serviceDescription": "Exchange metadata with third party data catalogs.",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/catalog-integrator/"
        },
        {
            "serviceName": "Organization Integrator OMIS",
            "serviceURLMarker": "organization-integrator",
            "serviceDescription": "Load information about the teams and people in an organization and return collaboration activity.",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/organization-integrator/"
        }
    ]
}
```
So the `service-url-marker` for the Files Integrator OMIS is `files-integrator`.


Once the Files Integrator OMIS connectors are restarted, the status changes.  Disappointingly, there are still problems.
```json
{
    "class": "IntegrationDaemonStatusResponse",
    "relatedHTTPCode": 200,
    "integrationServiceSummaries": [
        {
            "integrationServiceId": 4005,
            "integrationServiceFullName": "Files Integrator OMIS",
            "integrationServiceURLMarker": "files-integrator",
            "integrationServiceDescription": "Extract metadata about files stored in a file system or file manager.",
            "integrationServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/",
            "integrationConnectorReports": [
                {
                    "connectorName": "OakDeneLandingAreaFilesMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T21:13:01.840+00:00",
                    "lastRefreshTime": "2021-02-02T21:14:02.042+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "BASIC-FILES-INTEGRATION-CONNECTORS-404-001 The directory named data/landing-area/hospitals/oak-dene/clinical-trials/drop-foot in the Connection object Connection{displayName='null', description='null', connectorType=ConnectorType{displayName='null', description='null', connectorProviderClassName='org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider', recognizedAdditionalProperties=null, recognizedSecuredProperties=null, recognizedConfigurationProperties=null, qualifiedName='null', additionalProperties=null, type=null, guid='null', url='null', classifications=null}, endpoint=Endpoint{displayName='null', description='null', address='data/landing-area/hospitals/oak-dene/clinical-trials/drop-foot', protocol='null', encryptionMethod='null', qualifiedName='null', additionalProperties=null, type=null, guid='null', url='null', classifications=null}, userId='null', encryptedPassword='null', clearPassword='null', configurationProperties=null, securedProperties=null, assetSummary='null', qualifiedName='null', additionalProperties=null, meanings=null, securityTags=null, searchKeywords=null, confidentialityGovernanceClassification=null, confidenceGovernanceClassification=null, criticalityGovernanceClassification=null, retentionGovernanceClassification=null, type=null, GUID='null', URL='null', classifications=null, extendedProperties=null, headerVersion=0} does not exist"
                },
                {
                    "connectorName": "OldMarketLandingAreaFilesMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T21:13:01.841+00:00",
                    "lastRefreshTime": "2021-02-02T21:14:02.042+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "BASIC-FILES-INTEGRATION-CONNECTORS-404-001 The directory named data/landing-area/hospitals/old-market/clinical-trials/drop-foot in the Connection object Connection{displayName='null', description='null', connectorType=ConnectorType{displayName='null', description='null', connectorProviderClassName='org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider', recognizedAdditionalProperties=null, recognizedSecuredProperties=null, recognizedConfigurationProperties=null, qualifiedName='null', additionalProperties=null, type=null, guid='null', url='null', classifications=null}, endpoint=Endpoint{displayName='null', description='null', address='data/landing-area/hospitals/old-market/clinical-trials/drop-foot', protocol='null', encryptionMethod='null', qualifiedName='null', additionalProperties=null, type=null, guid='null', url='null', classifications=null}, userId='null', encryptedPassword='null', clearPassword='null', configurationProperties=null, securedProperties=null, assetSummary='null', qualifiedName='null', additionalProperties=null, meanings=null, securityTags=null, searchKeywords=null, confidentialityGovernanceClassification=null, confidenceGovernanceClassification=null, criticalityGovernanceClassification=null, retentionGovernanceClassification=null, type=null, GUID='null', URL='null', classifications=null, extendedProperties=null, headerVersion=0} does not exist"
                },
                {
                    "connectorName": "DropFootClinicalTrialResultsFilesMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T21:13:01.843+00:00",
                    "lastRefreshTime": "2021-02-02T21:14:02.042+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "BASIC-FILES-INTEGRATION-CONNECTORS-404-001 The directory named data/data-lake/research/clinical-trials/drop-foot/weekly-measurements in the Connection object Connection{displayName='null', description='null', connectorType=ConnectorType{displayName='null', description='null', connectorProviderClassName='org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider', recognizedAdditionalProperties=null, recognizedSecuredProperties=null, recognizedConfigurationProperties=null, qualifiedName='null', additionalProperties=null, type=null, guid='null', url='null', classifications=null}, endpoint=Endpoint{displayName='null', description='null', address='data/data-lake/research/clinical-trials/drop-foot/weekly-measurements', protocol='null', encryptionMethod='null', qualifiedName='null', additionalProperties=null, type=null, guid='null', url='null', classifications=null}, userId='null', encryptedPassword='null', clearPassword='null', configurationProperties=null, securedProperties=null, assetSummary='null', qualifiedName='null', additionalProperties=null, meanings=null, securityTags=null, searchKeywords=null, confidentialityGovernanceClassification=null, confidenceGovernanceClassification=null, criticalityGovernanceClassification=null, retentionGovernanceClassification=null, type=null, GUID='null', URL='null', classifications=null, extendedProperties=null, headerVersion=0} does not exist"
                }
            ]
        },
        {
            "integrationServiceId": 4004,
            "integrationServiceFullName": "Database Integrator OMIS",
            "integrationServiceURLMarker": "database-integrator",
            "integrationServiceDescription": "Extract metadata such as schema, tables and columns from database managers.",
            "integrationServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/",
            "integrationConnectorReports": [
                {
                    "connectorName": "OakDeneLandingAreaDatabaseMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T20:57:50.726+00:00",
                    "lastRefreshTime": "2021-02-02T21:14:04.052+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "OMIS-DATABASE-INTEGRATOR-400-001 Integration connector OakDeneLandingAreaDatabaseMonitor is not of the correct type to run in the Database Integrator OMIS integration service.  It must inherit from org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector"
                }
            ]
        }
    ]
}
```
However, it is clear from the message identifier `BASIC-FILES-INTEGRATION-CONNECTORS-404-001` that the problem is
being reported by the connector itself.  This means the integration service is now working ok even if the connectors are not.

Once the expected directories (folders) are created, the files integration connectors and be restarted and begin to work:

```json
{
    "class": "IntegrationDaemonStatusResponse",
    "relatedHTTPCode": 200,
    "integrationServiceSummaries": [
        {
            "integrationServiceId": 4005,
            "integrationServiceFullName": "Files Integrator OMIS",
            "integrationServiceURLMarker": "files-integrator",
            "integrationServiceDescription": "Extract metadata about files stored in a file system or file manager.",
            "integrationServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/",
            "integrationConnectorReports": [
                {
                    "connectorName": "OakDeneLandingAreaFilesMonitor",
                    "connectorStatus": "RUNNING",
                    "lastStatusChange": "2021-02-02T21:39:31.496+00:00",
                    "lastRefreshTime": "2021-02-02T21:40:02.280+00:00",
                    "minMinutesBetweenRefresh": 10
                },
                {
                    "connectorName": "OldMarketLandingAreaFilesMonitor",
                    "connectorStatus": "RUNNING",
                    "lastStatusChange": "2021-02-02T21:39:31.662+00:00",
                    "lastRefreshTime": "2021-02-02T21:40:02.336+00:00",
                    "minMinutesBetweenRefresh": 10
                },
                {
                    "connectorName": "DropFootClinicalTrialResultsFilesMonitor",
                    "connectorStatus": "RUNNING",
                    "lastStatusChange": "2021-02-02T21:39:31.742+00:00",
                    "lastRefreshTime": "2021-02-02T21:40:02.392+00:00",
                    "minMinutesBetweenRefresh": 10
                }
            ]
        },
        {
            "integrationServiceId": 4004,
            "integrationServiceFullName": "Database Integrator OMIS",
            "integrationServiceURLMarker": "database-integrator",
            "integrationServiceDescription": "Extract metadata such as schema, tables and columns from database managers.",
            "integrationServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/",
            "integrationConnectorReports": [
                {
                    "connectorName": "OakDeneLandingAreaDatabaseMonitor",
                    "connectorStatus": "FAILED",
                    "lastStatusChange": "2021-02-02T20:57:50.726+00:00",
                    "lastRefreshTime": "2021-02-02T21:40:00.220+00:00",
                    "minMinutesBetweenRefresh": 10,
                    "failingExceptionMessage": "OMIS-DATABASE-INTEGRATOR-400-001 Integration connector OakDeneLandingAreaDatabaseMonitor is not of the correct type to run in the Database Integrator OMIS integration service.  It must inherit from org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector"
                }
            ]
        }
    ]
}
```

## Resynchronizing after a failure

If a connector has been unavailable for a while, it is possible it has missed some metadata changes
either in the third party technology or in the open metadata ecosystem.  The integration daemon
has a REST API to call `refresh` on:
* All connectors
* All connectors in an integration service
* A single connector

The refresh command requests that the connector does a comparision of the metadata in its
third party technology and the open metadata ecosystem and make good any discrepancies it finds.

These are the three forms of the refresh call in the order listed above.
The first refreshes all connectors running in the integration daemon.

```
POST {serverRootURL}/servers/{serverName}/open-metadata/integration-daemon/users/{userId}/refresh
```

This second command refreshes all connectors running under an integration service.
If the name of a connector is included in the request body then just that connector is rrefreshed.

```
POST {serverRootURL}/servers/{serverName}/open-metadata/integration-daemon/users/{userId}/integration-service/{service-url-marker}/refresh
```

The refresh processing can be an expensive operation, so it should only be requested when there has been an outage.

## Further information

* Configuring the [Integration daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md)
* Design of the [Integration daemon](../../../open-metadata-implementation/governance-servers/integration-daemon-services)
* Understanding [Integration connectors](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md)
* Understanding [Open Metadata Integration Services (OMIS)](../../../open-metadata-implementation/integration-services)

----
* Return to the [diagnostic guide](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.