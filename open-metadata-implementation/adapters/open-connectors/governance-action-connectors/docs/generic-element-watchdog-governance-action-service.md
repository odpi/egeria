
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Generic Element Watchdog Action Service


The *Generic Element Watchdog* [watchdog action service](https://egeria-project.org/concepts/watchdog-action-service) detects changes to requested elements and initiates a governance action process when they occur. It has two modes of operation: 

- listening for a single event and then terminating when it occurs, or
- continuously listening for multiple events.

It is possible to listen for:

- specific types of elements
- specific instances
- specific types of events

![Operation of generic element watchdog action service](generic-element-watchdog-governance-action-service.svg)

## Configuration

This connector uses the [Watchdog Action OMES](https://egeria-project.org/services/omes/watchdog-action) running in the [engine host](https://egeria-project.org/concepts/engine-host).  It is registered in the *EgeriaWatchdog* [watchdog action engine](https://egeria-project.org/concepts/watchdog-action-engine) that is defined in the Core Content Pack.

The following properties can be set up in its connection's configuration properties and overridden by the request parameters:

- The `interestingTypeName` property takes the name of an element type. If set, it determines which types of elements are to be monitored. This monitoring includes all subtypes of this interesting type. If `interestingTypeName` is not set the default value is `OpenMetadataRoot` - effectively all elements with an open metadata type.
- The `instanceToMonitor` property takes the unique identifier of a metadata element. If set, this service will only consider events for this instance (when it is also of the interesting type) and for relationships attached to it. If it is not set then all elements of the interesting type are monitored unless there are one or more action targets that are labelled with `instanceToMonitor` when this service starts. If the action targets are set up then these are the instances that are monitored.

The rest of the properties are the governance action processes to call for specific types of events. The property is set to the qualified name of the process to run if the type of event occurs on the metadata instance(s) being monitored. If the property is not set, the type of event it refers to is ignored.

| Property | Description |
|---|---|
| `newElementProcessName` | listen for new or refreshed elements |
| `updatedElementProcessName` | listen for changes to the properties in the element |
| `deletedElementProcessName` | listen for elements that have been deleted |
| `classifiedElementProcessName` | listen for elements that have had a new classification attached |
| `reclassifiedElementProcessName` | listen for elements that have had the properties in one of their classifications changed |
| `declassifiedElementProcessName` | listen for elements that have had a classification removed |
| `newRelationshipProcessName` | listen for new relationships linking these elements to other elements |
| `updatedRelationshipProcessName` | listen for changes to the properties of relationships that are attached to these elements |
| `deletedRelationshipProcessName` | listen for the removal of relationships attached to these elements |

### What the started process receives

| Event | Action targets | Request parameters |
|---|---|---|
| new element | `receivedElement` | |
| updated element | `receivedElement` | `ChangedProperties` - comma-separated names of the properties that were added, changed or removed |
| deleted element | none - the element no longer exists | `ElementGUID` and `ElementTypeName` of the deleted element |
| classified, reclassified, declassified element | `receivedElement` | `ClassificationName`; `ChangedProperties` for a reclassification |
| new, updated, deleted relationship | `receivedElement` (end 1) and `receivedElementTwo` (end 2) | `RelationshipGUID`, `RelationshipTypeName`; `ChangedProperties` for an update |

The action target names can be changed with the `actionTargetName` and `actionTargetNameTwo` properties.

### Example Connection configuration

```json linenums="1"
{
  "connection":
  { 
    "class": "Connection",
    "connectorType": 
    {
      "class": "ConnectorType",
      "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericElementWatchdogGovernanceActionProvider"           
    },
    "configurationProperties": 
    {
      "interestingTypeName": "{{typeName}}",
      "instanceToMonitor": "{{guid}}",
      "newElementProcessName": "{{processQualifiedName}}",
      "updatedElementProcessName": "{{processQualifiedName}}",
      "deletedElementProcessName": "{{processQualifiedName}}",
      "classifiedElementProcessName": "{{processQualifiedName}}",
      "reclassifiedElementProcessName": "{{processQualifiedName}}",
      "declassifiedElementProcessName": "{{processQualifiedName}}",
      "newRelationshipProcessName": "{{processQualifiedName}}",
      "updatedRelationshipProcessName": "{{processQualifiedName}}",
      "deletedRelationshipProcessName": "{{processQualifiedName}}"
    }
  }
}
```

This is its connection definition to use when creating the definition of the watchdog action service using the [Governance Configuration Services](https://egeria-project.org/services/governance-configuration-services). Remove the configuration properties that are not required. Replace `{{typeName}}`, `{{guid}}` and `{{processQualifiedName}}` as required.

## Watchdog action settings

When this watchdog action service is called through an [engine action](https://egeria-project.org/concepts/engine-action) it supports the following options:

### Request types and parameters

There are two request types that control its modes of operation:

- `process-single-event` to request it monitors for a single specific event and then completes.  The single event is the first one that starts a process.
- `process-multiple-events` to request it continuously monitors for events until it is cancelled.  An event whose process cannot be started is logged and the monitoring continues.

The EgeriaWatchdog engine maps the `watch-for-element-change` governance request type to `process-single-event` and `watch-for-element-changes` to `process-multiple-events`.

If the engine host server where it is running is restarted, this watchdog action service is also restarted.

Any of the configuration properties can be overridden by request parameters of the same name.

### Action targets

The `instanceToMonitor` property can be supplied as a named action target. Using action targets allows the instance to be dynamically controlled and for multiple instances to be monitored.

### Completion status and guards

This service will only complete and produce a guard if it is set up to listen for a single event and that event occurs, or if it fails - when it starts, or, for a single event, when it cannot start the process.

On completion, this watchdog action service uses:

- `CompletionStatus.ACTIONED` with guard `monitoring-completed` - requested single event occurred, or
- `CompletionStatus.FAILED` with guard `monitoring-failed` - monitor not configured correctly or failed



--8<-- "snippets/abbr.md"
