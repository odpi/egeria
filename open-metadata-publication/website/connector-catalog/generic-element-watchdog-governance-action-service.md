<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Generic Element Watchdog Governance Action Service

* Connector Category: [Watchdog Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/watchdog-governance-service.md)
* Hosting Service: [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
* Hosting Server: [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
* Source Module: [governance-action-connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)
* Jar File Name: `governance-action-connectors.jar`
* ConnectorProviderClassName: `org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericElementWatchdogGovernanceActionProvider`


The **Generic Element Watchdog** Governance Action Service detects changes to requested elements and initiates a governance action process when they
occur.  It has two modes of operation: 

- listening for a single event and then terminating when it occurs or 
- continuously listening for multiple events.

It is possible to listen for:

- specific types of elements
- specific instances
- specific types of events


![Figure 1](generic-element-watchdog-governance-action-service.png#pagewidth)
> **Figure 1:** Operation of generic element watchdog governance action service


## Configuration

This connector uses the [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
running in the [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).

The following properties that can be set up
in its connection's configuration properties and overridden by the request parameters.

The **interestingTypeName** property takes the name of an element type.  If set, it determines which types of elements are to be
monitored.  This monitoring includes all subtypes of this interesting type.  If interestingTypeName is not set
the default value is OpenMetadataRoot - effectively all elements with an open metadata type.

The **instanceToMonitor** property takes the unique identifier of a metadata element.  If set, this service will
only consider events for this instance.  If it is not set then all elements of the interesting type are
monitored unless there are one or more action targets that are labelled with instanceToMonitor when this service starts.
If the action targets are set up then these are the instances that are monitored.

The rest of the properties are the governance action processes to call for specific types of events.  The property is set to the
qualified name of the process to run if the type of event occurs on the metadata instance(s) being monitored.  If the property is not
set, the type of event it refers to is ignored.

- **newElementProcessName**: listen for new or refreshed elements
- **updatedElementProcessName**: listen for changes to the properties in the element
- **deletedElementProcessName**: listen for elements that have been deleted
- **classifiedElementProcessName**: listen for elements that have had a new classification attached
- **reclassifiedElementProcessName**: listen for elements that have had the properties in one of their classifications changed
- **declassifiedElementProcessName**: listen for elements that have had a classification removed
- **newRelationshipProcessName**: listen for new relationships linking these elements to other elements
- **updatedRelationshipProcessName**: listen for changes to the properties of relationships that are attached to these elements
- **deletedRelationshipProcessName**: listen for the removal of relationships attached to these elements

This is its connection definition to use when
creating the definition of the governance action service
using the [Governance Engine OMAS](../../../open-metadata-implementation/access-services/governance-engine).
Remove the configuration properties that are not required.
Replace `{typeName}`, `{guid}` and `{processQualifiedName}` as required. 


```json
{
   "connection" : { 
                      "class" : "Connection",
                      "connectorType" : 
                      {
                           "class" : "ConnectorType",
                           "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericElementWatchdogGovernanceActionProvider"           
                      },
                      "configurationProperties": 
                      {
                              "interestingTypeName": "{typeName}",
                              "instanceToMonitor": "{guid}",
                              "newElementProcessName": "{processQualifiedName}",
                              "updatedElementProcessName": "{processQualifiedName}",
                              "deletedElementProcessName": "{processQualifiedName}",
                              "classifiedElementProcessName": "{processQualifiedName}",
                              "reclassifiedElementProcessName": "{processQualifiedName}",
                              "declassifiedElementProcessName": "{processQualifiedName}",
                              "newRelationshipProcessName": "{processQualifiedName}",
                              "updatedRelationshipProcessName": "{processQualifiedName}",
                              "deletedRelationshipProcessName": "{processQualifiedName}"
                      }
                  }
}

```

## Governance Action Settings

When this governance action service is called through a [GovernanceAction](../open-metadata-types/0463-Governance-Actions.md)
it supports the following options.

#### Request Types and Parameters

There are two request types that control its modes of operation:

* **process-single-event** to request it monitors for a single specific event and then completes.
* **process-multiple-events** to request it continuously monitors for events until it fails.
If the engine host server where it is running is restarted, this governance action service is also restarted.

Any of the configuration properties can be overridden by request parameters of the same name.

#### Action Targets

The **instanceToMonitor** property can be supplied as a name action target.  Using action targets allows the
instance to be dynamically controlled and for multiple instances to be monitored.

#### Completion Status and Guards

This service will only complete and produce a guard if it encounters an unrecoverable error or 
it is set up to listen for a single event and that event occurs.

On completion, this governance action service uses:

* `CompletionStatus.ACTIONED` with guard `monitoring-complete` - requested single event occurred, or
* `CompletionStatus.FAILED` with guard `monitoring-failed` - monitor not configured correctly or failed 


## Examples of use

* [Open Metadata Labs](../../../open-metadata-resources/open-metadata-labs): this connector is configured
in the `governDL01` engine host server as part of the **automated curation** asset management lab.

## Related Information

* [Designing a Watchdog Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/watchdog-governance-service.md)


----
* Return to the [Connector Catalog](.)
* Return to the [Governance Action Connectors Overview](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.