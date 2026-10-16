<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Governance Engine OMVS

The Governance Engine OMVS is a REST API for governance engines that run engine actions outside of Egeria.
Egeria still coordinates the engine actions - for example, when an engine action is a step in a governance
action process, Egeria starts the next steps of the process when the engine action completes.

The external governance engine:

1. Retrieves the engine actions that are approved to run on it (`getApprovedEngineActions`).
2. Claims each engine action it is going to run (`claimEngineAction`).  Only one caller can claim an engine action.
3. Optionally reports its progress (`updateEngineActionStatus` and `updateActionTargetStatus`).
4. Records the completion of the engine action, along with the output guards that select the next steps
   of a governance action process (`recordCompletionStatus`).

After a restart, it can retrieve the engine actions it has claimed but not finished (`getActiveClaimedEngineActions`).
The governance engine authenticates with its own userId, and only that userId can update an engine action once
it has claimed it.

These are the same calls that Egeria's engine hosts make (the `GovernanceCompletionInterface` from the
Open Governance Framework).

These operations used to be part of the [Automated Curation OMVS](../../view-server-generic-services/automated-curation).
The paths, request bodies and responses are unchanged, so a caller moves to this service by replacing the
`automated-curation` URL marker with `governance-engine`.  For example:

```
/servers/{viewServer}/api/open-metadata/automated-curation/engine-actions/{engineActionGUID}/claim
```

becomes

```
/servers/{viewServer}/api/open-metadata/governance-engine/engine-actions/{engineActionGUID}/claim
```

`getApprovedEngineActions` is new in this service.

Link to documentation: [https://egeria-project.org/services/omvs/governance-engine/overview/](https://egeria-project.org/services/omvs/governance-engine/overview/)

Sample REST API requests can be found in [Egeria-api-governance-engine.http](Egeria-api-governance-engine.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
