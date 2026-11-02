---
hide:
- toc
---

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Generic Folder Watchdog Action Service

The *generic folder watchdog* [watchdog action service](https://egeria-project.org/concepts/watchdog-action-service) detects changes to the assets in a specific folder and initiates a governance action process when they occur. It listens continuously for events.

It is possible to listen for:

- assets directly in the folder - and optionally assets in any nested folder
- specific types of events

![Operation of generic folder watchdog action service](generic-folder-watchdog-governance-action-service.svg)

## Configuration

This connector uses the [Watchdog Action OMES](https://egeria-project.org/services/omes/watchdog-action) running in the [engine host](https://egeria-project.org/concepts/engine-host).  It is registered in the *EgeriaWatchdog* [watchdog action engine](https://egeria-project.org/concepts/watchdog-action-engine) under the `watch-for-new-files-in-folder` governance request type, which is defined in the Files Content Pack.

The following properties that can be set up in its connection's configuration properties and overridden by the request parameters.

- The `watchedFolderName` property takes the path name of the folder to watch.  It is used to locate the folder's element if the `watchedFolder` action target is not supplied.  If neither is supplied, files in every folder are monitored.
- The `interestingTypeName` property takes the name of a `DataFile` type. If set, it determines which types of assets are to be monitored. This monitoring includes all subtypes of this interesting type. If `interestingTypeName` is not set the default value is `DataFile` - effectively all files with an open metadata type.

The rest of the properties are the governance action processes to call for specific types of events. The property is set to the qualified name of the process to run if the type of event occurs on the metadata instance(s) being monitored. If the property is not set, the type of event it refers to is ignored.

| Property | Description |
|---|---|
| `newElementProcessName` | listen for new or refreshed elements |
| `updatedElementProcessName` | listen for changes to the properties in the element |
| `deletedElementProcessName` | listen for elements that have been deleted - the deleted file is not an action target; its GUID and type name are passed in the `ElementGUID` and `ElementTypeName` request parameters |
| `classifiedElementProcessName` | listen for elements that have had a new classification attached |
| `reclassifiedElementProcessName` | listen for elements that have had the properties in one of their classifications changed |
| `declassifiedElementProcessName` | listen for elements that have had a classification removed |

!!! example "Connection configuration"
    ```json linenums="1"
    {
      "connection": { 
        "class": "Connection",
        "connectorType": {
          "class": "ConnectorType",
          "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider"           
        },
        "configurationProperties": {
          "watchedFolderName": "{{pathName}}",
          "interestingTypeName": "{{typeName}}",
          "newElementProcessName": "{{processQualifiedName}}",
          "updatedElementProcessName": "{{processQualifiedName}}",
          "deletedElementProcessName": "{{processQualifiedName}}",
          "classifiedElementProcessName": "{{processQualifiedName}}",
          "reclassifiedElementProcessName": "{{processQualifiedName}}",
          "declassifiedElementProcessName": "{{processQualifiedName}}"
        }
      }
    }
    ```

    This is its connection definition to use when creating the definition of the watchdog action service. Remove the configuration properties that are not required. Replace `{{pathName}}`, `{{typeName}}` and `{{processQualifiedName}}` as required.

## Watchdog action settings

When this watchdog action service is called through an [engine action](https://egeria-project.org/concepts/engine-action) it supports the following options:

### Request types and parameters

There are two request types that control its modes of operation:

- `watch-member-of-folder` to request it monitors for file assets that are directly in the named folder.
- `watch-nested-in-folder` to request it monitors for file assets that are either directly in the named folder or any sub-folder.

The EgeriaWatchdog engine's `watch-for-new-files-in-folder` governance request type maps to `watch-nested-in-folder`.

Any of the configuration properties can be overridden by request parameters of the same name.

### Action targets

The folder to watch can be supplied as an action target named `watchedFolder`.  This takes precedence over the `watchedFolderName` property.

### Completion status and guards

This service will only complete and produce a guard if it cannot start monitoring.  Once it is running, an event whose process cannot be started is logged and the monitoring continues.

In which case, this watchdog action service uses:

- `CompletionStatus.INVALID` with guard `monitoring-invalid` - no folder element has the path name in `watchedFolderName`, or
- `CompletionStatus.FAILED` with guard `monitoring-failed` - the monitoring could not be started.

The service can be stopped by cancelling its engine action.

--8<-- "snippets/abbr.md"
