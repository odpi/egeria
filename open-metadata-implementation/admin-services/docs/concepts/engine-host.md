<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Governance Engine Hosting Servers

An **Engine Host** is an [OMAG Server](omag-server.md) that hosts one or more governance engines.

Governance engines provide collections of services used to support the governance of assets and the metadata
that describes them.  They are managed by the [engine services](../../../engine-services) (or Open Metadata Engine Services (OMES)
to give them their full name) which, in turn run in the Engine Host OMAG Server.

The engine services are:

* **Asset Analysis** - Analyses the content of an asset's real world counterpart, generates annotations
                   in an open discovery analysis report that is attached to the asset in the open metadata repositories.
* **Metadata Watchdog** - Monitors changes in the metadata and initiates updates as a result.  One example of a
                      watchdog service is duplicate detection. Another example is to monitor the addition of
                      open discovery reports and take action on their content.  Examples of updates include
                      creating RequestForAction instances.  
* **Request Triage** - Monitors for new/changed RequestForAction instances and runs triage rules to determine
                   how to manage the request.  This could be to initiate an external workflow, wait for manual
                   decision or initiate a remediation request.
* **Issue Remediation** - Monitors for remediation requests and runs the requested remediation service.
                      Examples of remediation services are duplicate linking and consolidating.
* **Action Scheduler** - Maintains a calendar of events and creates RequestForAction instances at the requested
                     time.  For example, it may move assets between zones when a particular date is reached.
* **Asset Provisioning** - Invokes a provisioning service whenever a provisioning request is made.  Typically the
                       provisioning service is an external service.  It may also create lineage metadata to
                       describe the work of the provisioning engine.                

An engine service is paired with a specific [access service](../../../access-services) running in either a 
[Metadata Access Point](metadata-access-point.md) or a [metadata server](metadata-server.md).
The name and URL root of the server where the access service is running
is needed to configure an engine service.  The specific access services
are:

* [Discovery Engine OMAS](../../../access-services/discovery-engine) for Asset Analysis OMES.
* [Stewardship Action OMAS](../../../access-services/stewardship-action) for Request Triage OMES.
* [Asset Manager OMAS](../../../access-services/asset-manager) for all of the others.

Engine services typically have an external API to control and query the work of the engine
and may well be called by a view server as part of the support for a user interface.


![Figure 1](engine-host.png#pagewidth)
> Figure 1: Engine Host in OMAG server ecosystem


The services running the governance engines may access third party technology to
perform their responsibilities.


## Configuring the Engine Host Server


Each [type of OMAG Server](omag-server.md) is configured by creating
a [configuration document](configuration-document.md).  The contents
of the configuration document identify the type of server and
the options on the services it runs.

Figure 2 shows the structure of the configuration document for an engine host.

![Figure 2](engine-host-config.png#pagewidth)
> Figure 2: Configuration Document for an Engine Host

The tasks for configuring an engine host are as follows.

* [Setting basic properties for an OMAG server](../user/configuring-omag-server-basic-properties.md)
* [Configuring the audit log destinations](../user/configuring-the-audit-log.md)
* [Configuring the server security connector](../user/configuring-the-server-security-connector.md)
* [Configuring the engine services](../user/configuring-the-engine-services.md)

Below is an example of the configuration for a minimal engine host server.  It has
a single engine service (asset-analysis) and the default audit log.

```json

{
    "class": "OMAGServerConfigResponse",
    "relatedHTTPCode": 200,
    "omagserverConfig": {
        "class": "OMAGServerConfig",
        "versionId": "V2.0",
        "localServerId": "8b745d03-5ffc-4978-81ab-bd3d5156eebe",
        "localServerName": "myserver",
        "localServerType": "Open Metadata and Governance Server",
        "localServerURL": "https://localhost:9443",
        "localServerUserId": "OMAGServer",
        "maxPageSize": 1000,
        "engineServicesConfig": [
            {
                "class": "EngineServiceConfig",
                "engineId": 6000,
                "engineQualifiedName": "Asset Analysis",
                "engineServiceFullName": "Asset Analysis OMES",
                "engineServiceURLMarker": "asset-analysis",
                "engineServiceDescription": "Analyses the content of an asset's real world counterpart, generates annotations in an open discovery report that is attached to the asset in the open metadata repositories .",
                "engineServiceWiki": "https://egeria.odpi.org/open-metadata-implementation/engine-services/asset-analysis/",
                "engines" : [ {"engineId" : "daff1dca-984b-4b8a-8a8f-febaf72b82a8",
                               "engineName" : "engine1", 
                               "engineUserId" : "engine1UserId"},
                              {"engineId" : "a80aa0f8-2ea0-4f84-b613-d68becba2693",
                               "engineName" : "engine2", 
                               "engineUserId" : "engine2UserId"} ],
                "engineServiceOperationalStatus": "ENABLED",
                "engineServiceAdminClass": "org.odpi.openmetadata.engineservices.assetanalysis.admin.AssetAnalysisAdmin",
                "omagserverPlatformRootURL": "https://localhost:9443",
                "omagserverName": "myMetadataServer"
            }
        ],
        "repositoryServicesConfig": {
            "class": "RepositoryServicesConfig",
            "auditLogConnections": [
                {
                    "class": "Connection",
                    "headerVersion": 0,
                    "displayName": "Console",
                    "connectorType": {
                        "class": "ConnectorType",
                        "headerVersion": 0,
                        "type": {
                            "class": "ElementType",
                            "headerVersion": 0,
                            "elementOrigin": "LOCAL_COHORT",
                            "elementVersion": 0,
                            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                            "elementTypeName": "ConnectorType",
                            "elementTypeVersion": 1,
                            "elementTypeDescription": "A set of properties describing a type of connector."
                        },
                        "guid": "4afac741-3dcc-4c60-a4ca-a6dede994e3f",
                        "qualifiedName": "Console Audit Log Store Connector",
                        "displayName": "Console Audit Log Store Connector",
                        "description": "Connector supports logging of audit log messages to stdout.",
                        "connectorProviderClassName": "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider"
                    },
                    "configurationProperties": {
                        "supportedSeverities": [
                            "<Unknown>",
                            "Information",
                            "Event",
                            "Decision",
                            "Action",
                            "Error",
                            "Exception",
                            "Security",
                            "Startup",
                            "Shutdown",
                            "Asset",
                            "Types",
                            "Cohort"
                        ]
                    }
                }
            ]
        },
        "auditTrail": [
            "Tue Dec 08 18:38:32 GMT 2020 me updated configuration for engine service asset-analysis.",
            "Tue Dec 08 18:43:47 GMT 2020 me set up default audit log destinations."
        ]
    }
}

```

## Further information

More information on the governance engines are available below:

* The capabilities of each of the engine
services are described in the [engine services](../../../engine-services) module.

* Details of how to configure the discovery engines and discovery services are 
  described in the [Discovery Engine OMAS](../../../access-services/discovery-engine) documentation.
  
* Details of how to configure the governance action engines and services are
  described in the [Stewardship Action OMAS](../../../access-services/stewardship-action)  

----
Return to the [Governance Server Types](governance-server-types.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.