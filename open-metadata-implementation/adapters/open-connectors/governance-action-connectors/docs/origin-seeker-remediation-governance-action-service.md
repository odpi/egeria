<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Origin Seeker Governance Action Service
    
An asset may have an origin classification attached that documents various properties about its origin.  The *Origin Seeker* Governance Action Service sets up the origin classification on an asset by navigating backwards through the lineage relationships to locate origin classifications attached to the assets in the lineage graph.  
It follows the *LineageMapping*, *DataFlow*, *ControlFlow* and *ProcessCall* relationships that are attached directly to the asset and to each element upstream of it.  Lineage between the ports and schema elements of the assets is not followed.

![Figure 1](origin-seeker-remediation-governance-action-service.svg)
> **Figure 1:** Operation of the Origin Seeker governance action service

*Origin Seeker* is only able to assign an origin to the asset if at least on of the assets in its lineage has an origin classification, and where there are multiple assets.  If one (and only one) is found, it is assigned to the action target asset.

Figure 2 shows a simple example of origin seeker finding an origin in a no-branching lineage graph.

![Figure 2](origin-seeker-remediation-governance-action-service-1.svg)
> **Figure 2:** Origin assigned because one origin classification found

Figure 3 shows the origin coming from multiple branches of the lineage graph, but they are all the same, so the same values can be assigned.

![Figure 3](origin-seeker-remediation-governance-action-service-2.svg)
> **Figure 3:** Origin assigned because consistent origin classifications found

Figure 4 shows a case where origin seeker fails because the asset's lineage shows it to have been derived from multiple origins.

![Figure 4](origin-seeker-remediation-governance-action-service-3.svg)
> **Figure 4:** Origin not assigned because inconsistent origin classifications found

Finally the origin is not assigned because there are no origin classifications in the lineage graph.

![Figure 5](origin-seeker-remediation-governance-action-service-4.svg)
> **Figure 5:** Origin not assigned because no origin classifications found

## Configuration

This is *Origin Seeker*'s connection definition to use when creating the definition of the governance action service using the [Open Governance Services](https://egeria-project.org/services/gaf-metadata-management). Note that is does not use configuration properties. 


```json
{
   "connection" : { 
                      "class" : "Connection",
                      "connectorType" : 
                      {
                           "class" : "ConnectorType",
                           "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGovernanceActionProvider"           
                      }
                  }
}

```

## Governance Action Settings

When this governance action service is called through an [engine action](https://egeria-project.org/concepts/engine-action) it supports the following options.

#### Request Types and Parameters

Origin seeker does not specifically recognize any request types or request parameters.

#### Action Targets

The asset that needs an origin classification is linked to the governance action as the `newAsset` action target.  The unique identifier (guid) can be passed as a parameter when the governance action or governance action process is initiated.  Action targets with other names are ignored.

#### Completion Status and Guards

These are the responses that origin seeker produces:

- `CompletionStatus.ACTIONED` with guard `origin-assigned` if a single origin is detected in all branches of the lineage graph.
- `CompletionStatus.ACTIONED` with guard `origin-already-assigned` if the asset already has an origin set up.
- `CompletionStatus.INVALID` with guard `multiple-origins-detected` if multiple, different origins are detected in the lineage graph.  The origins that were found are passed on as a JSON array in the `detectedOrigins` request parameter so that a steward can choose between them.
- `CompletionStatus.INVALID` with guard `no-origins-detected` if no origin classifications are found in the lineage graph. 
- `CompletionStatus.INVALID` with guard `no-targets-detected` if there is no `newAsset` action target.
- `CompletionStatus.INVALID` with guard `multiple-targets-detected` if there are multiple `newAsset` action targets.
- `CompletionStatus.INVALID` with guard `target-not-asset` if the `newAsset` action target is not an asset.
- `CompletionStatus.FAILED` with guard `origin-seeking-failed` for an unrecoverable error such as a not authorized response from the metadata repository.


## Related Information

* See the *Asset Lineage* section in [Asset Catalog Contents](https://egeria-project.org/patterns/metadata-manager/overview/#asset-lineage).
* [Designing a Remediation Governance Action Service](https://egeria-project.org/guides/developer/governance-action-services/overview/#remediation-context-methods).

## Suggested Enhancements

This governance action service could be enhanced as follows:

* Support a default origin through the configuration properties/request parameters to use if no origin is found in the
  lineage graph.
  
* Provide logic to blend the origin properties when multiple origins are detected.
  This type of change would need knowledge of the origin properties used in the installation
  to know which are valid to combine.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
