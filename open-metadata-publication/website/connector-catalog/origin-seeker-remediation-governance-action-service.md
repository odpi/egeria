<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Origin Seeker Remediation Governance Action Service

* Connector Category: [Remediation Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/remediation-governance-service.md)
* Hosting Service: [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
* Hosting Server: [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
* Source Module: [governance-action-connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)
* Jar File Name: `governance-action-connectors.jar`
* ConnectorProviderClassName: `org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGovernanceActionProvider`

An asset may have an origin classification attached that documents various properties
about its origin.
The **Origin Seeker** Remediation Governance Action Service sets up the origin classification on an asset
by navigating backwards through
the lineage relationships to locate origin classifications attached to the assets in
the lineage graph.  


![Figure 1](origin-seeker-remediation-governance-action-service.png#pagewidth)
> **Figure 1:** Operation of the origin seeker remediation governance action service

Origin seeker is only able to assign an origin to the asset if at least on of the
assets in its lineage has an origin classification, and where there are multiple assets
If one (and only one) is found, it is assigned to the action target asset.

Figure 2 shows a simple example of origin seeker finding an origin
in a no-branching lineage graph.

![Figure 2](origin-seeker-remediation-governance-action-service-1.png#pagewidth)
> **Figure 2:** Origin assigned because one origin classification found

Figure 3 shows the origin coming from multiple branches of the lineage
graph, but they are all the same, so the same values can be assigned.

![Figure 3](origin-seeker-remediation-governance-action-service-2.png#pagewidth)
> **Figure 3:** Origin assigned because consistent origin classifications found

Figure 4 shows a case where origin seeker fails because the asset's lineage shows it
to have been derived from multiple origins.

![Figure 4](origin-seeker-remediation-governance-action-service-3.png#pagewidth)
> **Figure 4:** Origin not assigned because inconsistent origin classifications found

Finally the origin is not assigned because there are no origin classifications
in the lineage graph.

![Figure 5](origin-seeker-remediation-governance-action-service-4.png#pagewidth)
> **Figure 5:** Origin not assigned because no origin classifications found

## Configuration

This connector uses the [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
running in the [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).

This is its connection definition to use when
creating the definition of the governance action service
using the [Governance Engine OMAS](../../../open-metadata-implementation/access-services/governance-engine).
Note that is does not use configuration properties. 


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

When this governance action service is called through a [GovernanceAction](../open-metadata-types/0463-Governance-Actions.md)
it supports the following options.

#### Request Types and Parameters

Origin seeker does not specifically recognize any request types or request parameters.

#### Action Targets

The asset that needs an origin classification is linked to the
governance action as an action target.  The unique identifier (guid)
can be passed as a parameter when the governance action or
governance action process is initiated.

#### Completion Status and Guards

These are the responses that origin seeker produces:

* `CompletionStatus.ACTIONED` with guard `origin-assigned` if a single origin is detected in all branches of the lineage graph.
* `CompletionStatus.ACTIONED` with guard `origin-already-assigned` if the asset already has an origin set up.
* `CompletionStatus.INVALID` with guard `multiple-origins-detected` if multiple, different origins are detected in the lineage graph.
* `CompletionStatus.INVALID` with guard `no-origins-detected` if no origin classifications are found in the lineage graph. 
* `CompletionStatus.FAILED` with guard `no-targets-detected` if there is no asset set up as an action target.
* `CompletionStatus.FAILED` with guard `multiple-targets-detected` if there are multiple assets set up as action targets.
* `CompletionStatus.FAILED` with guard `origin-seeking-failed` for an unrecoverable error such as a
not authorized response from the metadata repository.

## Examples of use

* [Open Metadata Labs](../../../open-metadata-resources/open-metadata-labs): this connector is configured
in the `governDL01` engine host server as part of the **automated curation** asset management lab.

## Related Information

* See the **Asset Lineage** section in [Asset Catalog Contents](../cataloging-assets/asset-catalog-contents.md).
* [Designing a Remediation Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/remediation-governance-service.md).

## Suggested Enhancements

This governance action service could be enhanced as follows:

* Support a default origin through the configuration properties/request parameters to use if no origin is found in the
  lineage graph.
  
* Provide logic to blend the origin properties when multiple origins are detected.
  This type of change would need knowledge of the origin properties used in the installation
  to know which are valid to combine.

----
* Return to the [Connector Catalog](.)
* Return to the [Governance Action Connectors Overview](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.