<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Governance Action Framework (GAF)
  
The Governance Action Framework (GAF) provides the interfaces
and base implementations for components (called [Governance Action Services](docs/goverance-action-service.md)) that
take action to:

* detect, report and eventually correct a situation that is harmful to the data
or the organization in some way or 
* to enhance the metadata to improve its use.

The Governance Action Framework can be used for three purposes:

* Provide the complete implementation and orchestration of a governance process.
* Provide coordination between processes run by specialized governance systems.  For example, coordinating
  a DevOps pipeline with a data movement and quality process and security incident management.
* Provide contextual metadata plus an audit trail of actions managed by an external governance process.
  

## Governance Action Services

There are five types of Governance Action Services:

* **Watchdog Governance Service** - monitors for specific types of instances, or changes to open metadata and initiates
  appropriate follow on governance actions either by creating a 
  [governance action](docs/governance-action.md), a
  [governance action process](docs/governance-action-process.md) or an [incident report](docs/incident-report.md).
  For example, a watchdog service may detect a new 
  [asset](../../access-services/docs/concepts/assets) and kick off an asset onboarding governance action process.
    
* **Verification Governance Service** - performs a series of tests around a specific metadata instance.

* **Triage Governance Service** - evaluates a reported situation and makes a choice
  on the right action to proceed.  This may involve a human decision maker.
 
* **Remediation Governance Service** - performs a change to the open metadata (or
  its counterpart in the real-world).

* **Provisioning Governance Service** - performs an action that makes a new resource available
  along with the appropriate open metadata.

These are often used in conjunction with the
[Open Discovery Services](../open-discovery-framework/docs/discovery-service.md)
from the [Open Discovery Framework (ODF)](../open-discovery-framework).

Collectively they are called the **Governance Services**.



Each type of service supports a specialist governance activity.
The governance action services are linked together into


Some governance action services invoke functions in external engines that are working with data and related assets.
The GAF offers embeddable functions and APIs to simplify the implementation of governance action services,
and their integration into the broader digital landscape, 
whilst being resilient and with good performance.

## Implementing governance action services

Governance action services are open connectors
(see [Open Connector Framework (OCF)](../open-connector-framework))
that support the interfaces defined by the GAF.
They may produce audit log records and exceptions
and they may make changes to metadata through the
Open Metadata Access Services (OMASs).

## Configuring the governance action services in open metadata

[Governance Action Engines](docs/goverance-action-engine.md) are
defined in open metadata as a collection of [governance action request
types](docs/governance-action-request-type.md) linked to descriptions of
governance action services.






## Running governance action services

Governance action engines are hosted by [Open Metadata Engine Services (OMES)](../../engine-services).
There is a specific engine service for each type of governance action engine
defined in the open metadata.

* [Asset Analysis OMES](../../engine-services/asset-analysis) - supports Open Discovery Engines from the ODF.
* [Metadata Watchdog OMES](../../engine-services/metadata-watchdog) - supports the Open Watchdog Engine.
* [Request Triage OMES](../../engine-services/request-triage) - supports this Open Triage Engine.
* [Issue Remediation OMES](../../engine-services/issue-remediation) - supports the Open Remediation Engine.
* [Action Scheduler OMES](../../engine-services/action-scheduler) - supports the Open Scheduling Engine.
* [Asset Provisioning OMES](../../engine-services/asset-provisioning) - supports the Open Provisioning Engine.

The engine services run in dedicated OMAG Server called the **Engine Host**.
Instructions for configuring the engine services in the Engine Host
are found in the [Administration Guide](../../admin-services/docs/concepts/engine-host.md).

----
Return to [frameworks](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

