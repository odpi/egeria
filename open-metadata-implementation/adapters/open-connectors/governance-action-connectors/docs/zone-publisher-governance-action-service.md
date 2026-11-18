<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Zone Publisher Governance Action Service
    
An asset's [governance zones](https://egeria-project.org/concepts/governance-zone) determines its visibility to callers. Its zone membership is maintained in the [Asset Zone Membership](https://egeria-project.org/types/4/0424-Governance-Zones) classification.  

The *Zone Publisher* Governance Action Service sets the supplied governance zone names into the assets supplied as `newAsset` action targets.

* If there is at least one asset, their zones are updated, and the output guard is set to `zones-assigned`.
* If no assets are passed as `newAsset` action targets the output guard is `no-targets-detected`.

![Figure 1](zone-publisher-governance-action-service.svg)
> **Figure 1:** Operation of the Zone Publisher governance action service


## Configuration

This connector uses the [Governance Action OMES](https://egeria-project.org/services/omes/governance-action/overview) running in the [Engine Host](https://egeria-project.org/concepts/engine-host).

This is its connection definition to use when creating the definition of the governance action service. 
Note that `publishZones` can be specified in either the configuration properties or the request parameters.  The value in the request parameters overrides the configuration properties value, even when it is empty.
In the configuration properties, `publishZones` may be a comma separated list of zone names, or a list of zone names such as `["zone1", "zone2"]`.


```json
{
   "connection" : { 
                      "class" : "Connection",
                      "connectorType" : 
                      {
                           "class" : "ConnectorType",
                           "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.ZonePublisherGovernanceActionProvider"           
                      },
                      "configurationProperties" : 
                      {
                           "publishZones" : "zone1,zone2"
                      }
                  }
}

```

## Governance Action Settings

When this governance action service is called through an [engine action](https://egeria-project.org/concepts/engine-action) it supports the following options.

#### Request Types and Parameters

The `publishZones` request parameter/configuration property provides Zone Publisher with the list of zones to set in the action target assets.  These zones replace any zones that were previously set up.  In the request parameters, the zones are a comma separated list, for example `zone1,zone2`.

#### Action Targets

The assets that need their zones updated are linked to the governance action as action targets named `newAsset`.  Action targets named `*` are also updated.  Action targets with any other name are ignored, so a governance action process can pass other elements, such as the source file of a copied asset, without changing their zones.

#### Completion Status and Guards

These are the responses that Zone Publisher produces:

- `CompletionStatus.ACTIONED` with guard `zones-assigned` if the zones are successfully assigned to the assets.
- `CompletionStatus.ACTIONED` with guard `no-zones-detected` if the `publishZones` property is not set up in either the configuration properties or the request parameters, or contains no zone names.  Any *ZoneMembership* classification is removed from the action target asset(s).
- `CompletionStatus.INVALID` with guard `no-targets-detected` if no assets are passed as `newAsset` action targets.  Nothing is changed.
- `CompletionStatus.FAILED` with guard `zone-publishing-failed` if the zones of an asset can not be updated.


## Related Information

* See the  [Governance Zoning](https://egeria-project.org/features/governance-zoning/overview/) feature for more information on how to use governance zones.
* [Designing a Remediation Governance Action Service](https://egeria-project.org/guides/developer/governance-action-services/overview/#remediation-context-methods).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
