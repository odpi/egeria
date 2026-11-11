
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Wait For Steward Watchdog Action Service


The *Wait For Steward* [watchdog action service](https://egeria-project.org/concepts/watchdog-action-service) waits for stewards to finish the [to dos](https://egeria-project.org/concepts/to-do) supplied as its action targets.  It lets a [governance action process](https://egeria-project.org/concepts/governance-action-process) hand work over to people and then carry on once they have finished it.

For example, it can follow:

- the *Evaluate Annotations* governance action service, which opens a to do for each request for action annotation in a survey report and passes them on as `stewardAction` action targets, or
- the *Steward Informer* governance action service, which creates a to do from a template and passes it on as the `newAsset` action target.

## Configuration

This connector uses the [Watchdog Action OMES](https://egeria-project.org/services/omes/watchdog-action/overview) running in the [engine host](https://egeria-project.org/concepts/engine-host).  It is registered in the *EgeriaWatchdog* [watchdog action engine](https://egeria-project.org/concepts/watchdog-action-engine) that is defined in the Core Content Pack.

It has no configuration properties.

### Example Connection configuration

```json linenums="1"
{
  "connection":
  {
    "class": "Connection",
    "connectorType":
    {
      "class": "ConnectorType",
      "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.WaitForStewardGovernanceActionProvider"
    }
  }
}
```

This is its connection definition to use when creating the definition of the watchdog action service using the [Governance Configuration Services](https://egeria-project.org/services/governance-configuration-services).

## Watchdog action settings

When this watchdog action service is called through an [engine action](https://egeria-project.org/concepts/engine-action) it supports the following options:

### Request types and parameters

The EgeriaWatchdog engine calls this service through the `wait-for-steward` governance request type.  The service has no request parameters.

### Action targets

The to dos to wait for are:

- the action targets named `stewardAction`, and
- any other action targets whose element is a `ToDo`, or one of its subtypes.

Other action targets are ignored.

### How it waits

The service listens for updates to, and deletions of, these to dos.  A to do has finished when either:

- its `activityStatus` is `COMPLETED`, `INVALID`, `IGNORED`, `FAILED`, `CANCELLED` or `ABANDONED`, or
- it is deleted.

The listener is registered before the status of each to do is checked, so to dos that finished before the service started are found straight away.  If the engine host server where it is running is restarted, this watchdog action service is also restarted, and it checks the to dos again.

### Completion status and guards

The service completes once every to do has finished.  On completion, it uses:

- `CompletionStatus.ACTIONED` with guard `to-dos-completed` - every to do was `COMPLETED`,
- `CompletionStatus.ACTIONED` with guard `to-dos-not-completed` - every to do has finished, but at least one finished with another status or was deleted,
- `CompletionStatus.ACTIONED` with guard `no-to-dos` - there were no to dos in the action targets, so there was nothing to wait for, or
- `CompletionStatus.FAILED` with guard `wait-failed` - the service could not register its listener or read the to dos.

The completion message lists the unique identifiers of the to dos that were, or were not, completed.



--8<-- "snippets/abbr.md"
