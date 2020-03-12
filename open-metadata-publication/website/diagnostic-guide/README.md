<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria diagnostic guide

**NOTE: work in progress**


## An example of a response from a REST API call
```json
{
    "class": "GovernanceOfficerListResponse",
    "relatedHTTPCode": 404,
    "exceptionClassName": "org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException",
    "exceptionErrorMessage": "OMAG-PLATFORM-404-001 The OMAG Server cocoMDS2 is not available to service a request from user peterprofile",
    "exceptionSystemAction": "The system is unable to process the request.",
    "exceptionUserAction": "Retry the request when the OMAG Server is available.",
    "exceptionProperties": {
        "parameterName": "serverName"
    }
}
```
## Example of an audit log message

```json
{
    "guid": "bfc4ebe9-0550-4c33-b3d6-aa760401b400",
    "timeStamp": 1583442856062,
    "originatorProperties": {
        "Server Name": "findItDL01",
        "Organization Name": "Coco Pharmaceuticals",
        "Server Type": "Open Metadata and Governance Server"
    },
    "originatorComponent": {
        "componentId": 2000,
        "componentName": "Discovery Engine Services",
        "componentWikiURL": "https://egeria.odpi.org/open-metadata-implementation/governance-servers/discovery-engine-services/",
        "componentType": "Run automated discovery services"
    },
    "actionDescription": "Register configuration listener",
    "threadId": 116,
    "threadName": "org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryConfigurationRefreshHandler",
    "severityCode": 5,
    "severity": "Error",
    "messageId": "DISCOVERY-ENGINE-SERVICES-0028",
    "messageText": "Failed to refresh configuration for discovery engine AssetQuality.  The exception was org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException with error message DISCOVERY-ENGINE-SERVICES-400-014 Properties for discovery engine called AssetQuality have not been returned by open metadata server cocoMDS1 to discovery server cocoMDS1",
    "messageParameters": [
        "AssetQuality",
        "org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException",
        "DISCOVERY-ENGINE-SERVICES-400-014 Properties for discovery engine called AssetQuality have not been returned by open metadata server cocoMDS1 to discovery server cocoMDS1"
    ],
    "additionalInformation": [
        "PropertyServerException{reportedHTTPCode=400, reportingClassName='org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryEngineHandler', reportingActionDescription='refreshConfig', errorMessage='DISCOVERY-ENGINE-SERVICES-400-014 Properties for discovery engine called AssetQuality have not been returned by open metadata server cocoMDS1 to discovery server cocoMDS1', reportedSystemAction='The discovery server is not able to initialize the discovery engine and so it will not de able to support discovery requests targeted to this discovery engine.', reportedUserAction='This may be a configuration error or the metadata server may be down.  Look for other error messages and review the configuration of the discovery server.  Once the cause is resolved, restart the discovery server.', reportedCaughtException=null, relatedProperties=null}"
    ],
    "systemAction": "The discovery engine is unable to process any discovery requests until its configuration can be retrieved.",
    "userAction": "Review the error messages and resolve the cause of the problem.  Either wait for the discovery server to refresh the configuration, or issue the refreshConfigcall to request that the discovery engine calls the Discovery Engine OMAS to refresh the configuration for the discovery service.",
    "exceptionClassName": "org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException",
    "exceptionMessage": "DISCOVERY-ENGINE-SERVICES-400-014 Properties for discovery engine called AssetQuality have not been returned by open metadata server cocoMDS1 to discovery server cocoMDS1",
    "exceptionStackTrace": "PropertyServerException{reportedHTTPCode=400, reportingClassName='org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryEngineHandler', reportingActionDescription='refreshConfig', errorMessage='DISCOVERY-ENGINE-SERVICES-400-014 Properties for discovery engine called AssetQuality have not been returned by open metadata server cocoMDS1 to discovery server cocoMDS1', reportedSystemAction='The discovery server is not able to initialize the discovery engine and so it will not de able to support discovery requests targeted to this discovery engine.', reportedUserAction='This may be a configuration error or the metadata server may be down.  Look for other error messages and review the configuration of the discovery server.  Once the cause is resolved, restart the discovery server.', reportedCaughtException=null, relatedProperties=null}\n\tat org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryEngineHandler.refreshConfig(DiscoveryEngineHandler.java:154)\n\tat org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryConfigurationRefreshHandler.run(DiscoveryConfigurationRefreshHandler.java:140)\n\tat java.lang.Thread.run(Thread.java:748)\n",
    "originator": {
        "serverName": "findItDL01",
        "serverType": "Open Metadata and Governance Server",
        "organizationName": "Coco Pharmaceuticals"
    },
    "reportingComponent": {
        "componentId": 2000,
        "componentName": "Discovery Engine Services",
        "componentWikiURL": "https://egeria.odpi.org/open-metadata-implementation/governance-servers/discovery-engine-services/",
        "componentType": "Run automated discovery services"
    }
}
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.