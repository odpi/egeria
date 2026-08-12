<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![Stable](../../../../images/egeria-content-status-released.png#pagewidth)

# Governance Action Connectors

This module contains the implementation of the [governance action services](https://egeria-project.org/concepts/governance-action-service/) that run in the
[Governance Action Open Metadata Engine Service (OMES)](../../../engine-services/governance-action)
hosted by the [Engine Host OMAG Server](https://egeria-project.org/concepts/engine-host).

The governance action services with detailed documentation are:

* [Generic Element Watchdog Governance Action Service](docs/generic-element-watchdog-governance-action-service.md)
* [Generic Folder Watchdog Governance Action Service](docs/generic-folder-watchdog-governance-action-service.md)
* [Move Copy File Provisioning Governance Action Service](docs/move-copy-file-provisioning-governance-action-service.md)
* [Origin Seeker Remediation Governance Action Service](docs/origin-seeker-remediation-governance-action-service.md)
* [Zone Publisher Governance Action Service](docs/zone-publisher-governance-action-service.md)

The remaining governance action services, grouped by category, are:

* **Provisioning**
  * **ProvisionTabularDataSetGovernanceActionConnector** - copies data from one tabular data set to another.
* **Remediation**
  * **QualifiedNamePeerDuplicateGovernanceActionConnector** - checks the qualified name of the entity passed as
    an action target to determine its duplicates.
  * **RetentionClassifierGovernanceActionConnector** - sets a Retention classification on the elements supplied
    as action targets.
* **Stewardship**
  * **CatalogTargetAssetGovernanceActionConnector** - creates a catalog target between the supplied integration
    connector and the supplied asset.
  * **CreateAssetGovernanceActionConnector** - creates an asset and passes its GUID as an action target for
    follow-on work.
  * **DaysOfWeekGovernanceActionConnector** - uses the current time to output the day of the week as a guard.
  * **DeleteAssetGovernanceActionConnector** - deletes an asset and passes its GUID as an action target for
    follow-on work.
  * **EvaluateAnnotationsGovernanceActionConnector** - a placeholder for a service that will look through the
    annotations from a survey report and set up guards to drive actions that process the different types.
  * **WaitForStewardGovernanceActionConnector** - a placeholder for a service that will wait for a steward to
    complete a to do.
  * **WriteAuditLogMessageGovernanceActionConnector** - writes requested messages to the audit log.
* **Verification**
  * **VerifyAssetGovernanceActionConnector** - evaluates an asset to be sure it has zones, an origin and an owner.


----
Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.