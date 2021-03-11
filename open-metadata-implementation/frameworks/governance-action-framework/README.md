<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Governance Action Framework (GAF)
  
The Governance Action Framework (GAF) provides the interfaces
and base implementations for components (called [Governance Action Services](docs/governance-action-service.md)) that
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

There are five types of Governance Action Services.  Each type of service supports a specialist governance activity:

* **Watchdog Governance Service** - monitors for specific types of instances, or changes to open metadata and initiates
  appropriate follow on governance actions either by creating a 
  [governance action](docs/governance-action.md), a
  [governance action process](docs/governance-action-process.md) or an [incident report](docs/incident-report.md).
  For example, a watchdog service may detect a new 
  [asset](../../access-services/docs/concepts/assets) and kick off an asset onboarding governance action process.
    
* **Verification Governance Service** - performs a series of tests around a specific metadata instance and depending on what it
  finds, it publishes [guards](docs/guard.md) to report on any errors it finds.

* **Triage Governance Service** - evaluates a reported situation and makes a choice
  on the right action to proceed.  This may involve a human decision maker.
 
* **Remediation Governance Service** - performs a change to the open metadata (or
  its counterpart in the real-world).

* **Provisioning Governance Service** - performs an action that makes a new resource available
  along with the appropriate open metadata.

These are often used in conjunction with the
[Open Discovery Services](../open-discovery-framework/docs/discovery-service.md)
from the [Open Discovery Framework (ODF)](../open-discovery-framework).
Collectively they are called the **Governance Services** and they
can be linked together into [governance action processes](docs/governance-action-process.md).

Some governance action services invoke functions in external engines that are working with data and related assets.
The GAF offers embeddable functions and APIs to simplify the implementation of governance action services,
and their integration into the broader digital landscape, 
whilst being resilient and with good performance.

## Implementing governance action services

Governance action services are open connectors
(see [Open Connector Framework (OCF)](../open-connector-framework))
that support the interfaces defined by the GAF.
They may produce [audit log records](../audit-log-framework) and exceptions
and they may make changes to metadata through the
Open Metadata Access Services (OMASs).

A governance action service is passed a context as it is started.
This provides access to the request type and associated parameters used to
invoke the governance action service, along with a client to access open metadata through
the [Governance Engine OMAS](../../access-services/governance-engine).

![Figure 1](docs/governance-context.png)
> **Figure 1:** Structure of the governance context 

This context is then specialized for each type of governance action service.
Details of the specific context for each service can be found by following the links:

* **[Watchdog Governance Service](docs/watchdog-governance-service.md)** 
* **[Verification Governance Service](docs/verification-governance-service.md)**
* **[Triage Governance Service](docs/triage-governance-service.md)**
* **[Remediation Governance Service](docs/remediation-governance-service.md)** 
* **[Provisioning Governance Service](docs/provisioning-governance-service.md)** 

## Configuring the governance action services in open metadata

A collection of related governance action services are grouped into
[Governance Action Engines](docs/governance-action-engine.md) for deployment.
The governance action engine maps [governance action request
types](docs/governance-action-request-type.md) to the 
governance action service that should be invoked along with 

![Figure 2](docs/governance-action-engine-definitions.png)
> **Figure 2:** Structure of a governance engine definition

These definitions are created through the [Governance Engine OMAS](../../access-services/governance-engine)
and are stored in the open metadata repositories.


## Running governance action services

Governance action engines are hosted by the 
[Governance Action Open Metadata Engine Service (OMES)](../../engine-services/governance-action).

The engine services run in dedicated OMAG Server called the **Engine Host**.
Instructions for configuring the engine services in the Engine Host
are found in the [Administration Guide](../../admin-services/docs/concepts/engine-host.md).

The [Governance Engine OMAS](../../access-services/governance-engine)
provides the services for:

* setting up the definitions of a [governance action engine](docs/governance-action-engine.md).
* configuring [governance action processes](docs/governance-action-process.md).
* managing [governance actions](docs/governance-action.md) and [incident reports](docs/incident-report.md).

----
Return to [frameworks](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

