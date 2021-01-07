<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Open Metadata Engine Services (OMES)

The engine services (or Open Metadata Engine Services (OMESs) to give them
their full name)
run in an [Engine Host](../concepts/engine-host.md) OMAG Server.
They are part of the [Engine Host Services](configuring-the-engine-host-services.md).

Each [engine service](../../../engine-services) provides support for a particular
type of **Governance Engine**, that is the
[Open Discovery Engines](../../../frameworks/open-discovery-framework/docs/discovery-engine.md)
and [Governance Action Engines](../../../frameworks/governance-action-framework).

Each engine service hosts one or more governance engines. A governance engine is a collection
of governance services of a specific type.:

* **Asset Analysis** - Hosts  [Open Discovery Services](../../../frameworks/open-discovery-framework/docs/discovery-engine.md) 
  that analyse the content of an asset's real world counterpart, 
  generates [annotations](../../../frameworks/open-discovery-framework/docs/discovery-annotation.md)
  in an [open discovery analysis report](../../../frameworks/open-discovery-framework/docs/discovery-analysis-report.md)
  that is attached to the asset in the open metadata repositories.

* **Metadata Watchdog** - Hosts [OpenWatchdogEngines](../../../frameworks/governance-action-framework/docs/open-watchdog-engine.md)
  that monitor changes in the metadata and initiates updates as a result.  One example of a
  watchdog service is duplicate detection. Another example is to monitor the addition of
  open discovery reports and take action on their content.  Examples of updates include
  creating RequestForAction instances.  
  
* **Request Triage** - Hosts [OpenTriageEngines](../../../frameworks/governance-action-framework/docs/open-triage-engine.md)
  that monitor for new/changed RequestForAction instances and runs triage rules to determine
  how to manage the request.  This could be to initiate an external workflow, wait for manual
  decision or initiate a remediation request.
  
* **Issue Remediation** - Hosts [OpenRemediationEngines](../../../frameworks/governance-action-framework/docs/open-remediation-engine.md)
  that monitor for remediation requests and runs the requested remediation service.
  Examples of remediation services are duplicate linking and consolidating.
  
* **Action Scheduler** - Hosts [OpenSchedulingEngines](../../../frameworks/governance-action-framework/docs/open-verification-service.md)
  that maintains a calendar of events and creates RequestForAction instances at the requested
  time.  For example, it may move assets between zones when a particular date is reached.

* **Asset Provisioning** - Hosts [OpenProvisioningEngines](../../../frameworks/governance-action-framework/docs/open-provisioning-engine.md)
  that invokes a provisioning service whenever a provisioning request is made.  Typically the
  provisioning service is an external service.  It may also create lineage metadata to
  describe the work of the provisioning engine.                


It is possible to get a description of each of the registered
engine services using the following command:

```
GET {serverURLRoot}/open-metadata/platform-services/users/{userId}/server-platform/registered-services/engine-services
```
Note the `engineServiceURLMarker` for the engine service that you want to configure.

Figure 1 shows the structure of the configuration for an individual engine service.

![Figure 1](../concepts/engine-service-config.png#pagewidth)
> **Figure 1:** The configuration document contents for an integration service

The descriptive information and operational status are filled out automatically by the
administration services based on the `engineServiceURLMarker` value that you supply.
The other values are supplied on the configuration call.

Each engine service is configured with the network location of the
[Metadata Access Point](../concepts/metadata-access-point.md) /
[Metadata Server](../concepts/metadata-server.md)
running the appropriate [Open Metadata Access Service](../../../access-services).
There are a set of options that the engine service supports
along with the list of configuration properties for the governance engines that will be run in the
engine service.
The governance engine's configuration properties identifies which governance engine to
to run.  The governance engine's definition, including the services it supports
are retrieved from the metadata access point / metadata server when the
engine service starts up.

```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/engine-services/{engineServiceURLMarker}
{
        "class": "EngineServiceRequestBody",
        "omagserverPlatformRootURL": {MDServerURLRoot},
        "omagserverName" : "{MDServerName}",
        [ {
             "class": "EngineConfig",
             "engineQualifiedName" : " ... "             
             "engineUserId" : " ... "
        } ]      
}
```
Where:
* **engineQualifiedName** - Set up the qualified name of the governance engine stored in the metadata servers.
* **connectorUserId** - Set up the user id for the engine - if this is null, the engine host's userId is used
  on requests to the Open Metadata Access Service (OMAS). 



## Further Information

The definition of the governance services
that are supported by these governance engines are retrieved from
the open metadata server when the engine host server starts up.

Maintaining these definitions is described:

   * For discovery engines and services see [Discovery Engine OMAS](../../../access-services/discovery-engine)
   * For governance action engines and services see [Governance Engine OMAS](../../../access-services/governance-engine)


----
* Return to [the Engine Host Server](../concepts/engine-host.md)
* Return to [configuration document structure](../concepts/configuration-document.md)
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.