<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# pasteur-fvt

Functional verification tests for the **Pasteur Action History Cleanup**, the nanny integration connector that
deletes finished actions once the number of days configured for their type has passed since they completed,
using the LOOK_FOR_LINEAGE delete method by default (archived if they have lineage relationships, otherwise
soft-deleted), or the one named in its `deleteMethod` configuration property.
See the [nanny connectors README](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/README.md#pasteur-action-history-cleanup)
for what Pasteur does.

## What the suite stands up

`OMAGPlatformExtension` starts an OMAG Server Platform in-process, on a free port, with:

* **pasteurFvtMetadataStore** - a metadata access store with a PostgreSQL repository (schema
  `repository_pasteurFvtMetadataStore`), its access services publishing to Apache Kafka, and the open metadata
  types and Core Content Pack loaded at start-up;
* **pasteurFvtIntegrationDaemon** - an integration daemon running the `Egeria:IntegrationGroup:Pasteur`
  integration group, so Pasteur runs with exactly the configuration the Core Content Pack gives it.

## What it checks

`PasteurActionHistoryCleanupFVT` creates actions with their `completionTime` set into the past, asks the
integration daemon to refresh Pasteur, and checks which are still readable:

| Action | Status | Completed | Expected |
| :--- | :--- | :--- | :--- |
| ToDo | COMPLETED | 100 days ago | soft-deleted - it has no lineage (and found as a deleted element) |
| ToDo | COMPLETED | 10 days ago | kept |
| ToDo | IN_PROGRESS | 100 days ago | kept |
| ToDo | CANCELLED | 100 days ago | kept |
| DataSharingRequest | FAILED | 100 days ago | deleted - inherits ToDo's 90 days |
| Meeting | COMPLETED | 1000 days ago | kept - not configured |
| EngineAction | COMPLETED | 8 days ago | deleted |
| EngineAction | INVALID | 6 days ago | kept |
| GovernanceActionProcessInstance + anchored EngineAction | COMPLETED | 15 days ago | both deleted |
| GovernanceActionProcessInstance + anchored EngineAction | COMPLETED | 10 days ago | both kept - the engine action waits for its process instance |

A second test sets Pasteur's `deleteMethod` configuration property to PURGE, which restarts and refreshes it,
and checks that a to do completed 100 days ago has been purged - it cannot even be found as a deleted element.
The property is set back to LOOK_FOR_LINEAGE at the end of that test.

Every qualified name includes the run's start time, and the actions are purged at the end of the run.

## Running it

The suite is not part of the default build.  It needs PostgreSQL on `localhost:5442` and Kafka on
`localhost:9194` (both set in `src/test/resources/application.properties`):

```
./gradlew :open-metadata-test:open-metadata-fvt:pasteur-fvt:test -PrunPasteurFvt
```
