<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Generic Folder Watchdog Governance Action Service

* Connector Category: [Watchdog Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/watchdog-governance-service.md)
* Hosting Service: [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
* Hosting Server: [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
* Source Module: [governance-action-connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)
* Jar File Name: `governance-action-connectors.jar`
* ConnectorProviderClassName: `org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider`


The **Generic Folder Watchdog** Governance Action Service detects changes to the assets in a specific folder and initiates a governance action process when they
occur.  It has two modes of operation: 

- listening for a single event and then terminating when it occurs or 
- continuously listening for multiple events.

It is possible to listen for:

- assets directly in the folder - and optionally assets in any nested folder
- specific types of events


![Figure 1](generic-folder-watchdog-governance-action-service.png#pagewidth)
> **Figure 1:** Operation of generic folder watchdog governance action service


## Configuration

This connector uses the [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
running in the [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).

The following properties that can be set up
in its connection's configuration properties and overridden by the request parameters.

The **interestingTypeName** property takes the name of an DataFile type.  If set, it determines which types of assets are to be
monitored.  This monitoring includes all subtypes of this interesting type.  If interestingTypeName is not set
the default value is DataFile - effectively all files with an open metadata type.


The rest of the properties are the governance action processes to call for specific types of events.  The property is set to the
qualified name of the process to run if the type of event occurs on the metadata instance(s) being monitored.  If the property is not
set, the type of event it refers to is ignored.

- **newElementProcessName**: listen for new or refreshed elements
- **updatedElementProcessName**: listen for changes to the properties in the element
- **deletedElementProcessName**: listen for elements that have been deleted
- **classifiedElementProcessName**: listen for elements that have had a new classification attached
- **reclassifiedElementProcessName**: listen for elements that have had the properties in one of their classifications changed
- **declassifiedElementProcessName**: listen for elements that have had a classification removed

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
                           "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider"           
                      },
                      "configurationProperties": 
                      {
                              "interestingTypeName": "{typeName}",
                              "newElementProcessName": "{processQualifiedName}",
                              "updatedElementProcessName": "{processQualifiedName}",
                              "deletedElementProcessName": "{processQualifiedName}",
                              "classifiedElementProcessName": "{processQualifiedName}",
                              "reclassifiedElementProcessName": "{processQualifiedName}",
                              "declassifiedElementProcessName": "{processQualifiedName}"
                      }
                  }
}

```

## Governance Action Settings

When this governance action service is called through a [GovernanceAction](../open-metadata-types/0463-Governance-Actions.md)
it supports the following options.

#### Request Types and Parameters

There are two request types that control its modes of operation:

* **member-of-folder** to request it monitors for file assets that are directly in the named folder.
* **nested-in-folder** to request it monitors for file assets that are either directly in the named folder or any sub-folder.

Any of the configuration properties can be overridden by request parameters of the same name.

#### Action Targets

The **folderTarget** property can be supplied as a named action target.  Using action targets allows the
instance to be dynamically controlled and for multiple instances to be monitored.

#### Completion Status and Guards

This service will only complete and produce a guard if it encounters an unrecoverable error.

In which case, this governance action service uses:

* `CompletionStatus.FAILED` with guard `monitoring-failed` 

It is also possible to shutdown the governance action service by setting

* `CompletionStatus.ACTIONED` with guard `monitoring-completed` 

in the governance action.

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