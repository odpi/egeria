<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Governance Engine Open Metadata Access Service (OMAS)

The **Governance Engine OMAS** provides the metadata services for running
**[governance engines](docs/concepts/governance-engine.md)**.

A governance engine is a collection of related 
**[governance services](docs/concepts/governance-service.md)** that provide pluggable
governance functions.  The governance services are implemented as 
specialist [connectors](../../frameworks/open-connector-framework) that are defined by:

 * [Open Discovery Framework (ODF)](../../frameworks/open-discovery-framework) for Open Discovery Services
   that analyse the content of resources in the digital landscape.
 * [Governance Action Framework (GAF)](../../frameworks/governance-action-framework) of Governance Action Services
   that monitor, assess and maintain metadata.

The governance services run in the [Engine Host OMAG Server](../../admin-services/docs/concepts/engine-host.md)
supported by the [Open Metadata Engine Services (OMES)](../../engine-services).
   
The Governance Engine OMAS has the following capabilities:

* Creating the definitions for [governance engines](docs/concepts/governance-engine.md) and
  their [governance services](docs/concepts/governance-service.md).
* Providing the APIs and events that enable the Engine Host OMAG Server to retrieve the definitions
  of the governance engines and services and be notified of any changes to them.
* Creating the definitions for [governance action processes](docs/concepts/governance-action-process.md)
  that control the sequencing of [governance actions](docs/concepts/governance-action.md).
* Providing APIs to create [governance actions](docs/concepts/governance-action.md) explicitly and
  [incident reports](docs/concepts/incident-report.md).
* Initiation and choreography of governance actions based on the template provided by 
  a [governance actions process](docs/concepts/governance-action-process.md).
* Notification of new [governance actions](docs/concepts/governance-action.md) to the 
  Engine Host OMAG Servers that then invoke the appropriate governance services to action them.
* Supporting the metadata requirements for many of the [engine services](../../engine-services).



## Documentation

Governance Engine OMAS has a [User Guide](docs/user) that covers the Governance Engine OMAS's APIs and
events.  

The documentation for writing governance services is located:

* [Open Discovery Framework (ODF)](../../frameworks/open-discovery-framework) for Open Discovery Services.
* [Governance Action Framework (GAF)](../../frameworks/governance-action-framework) for the
Governance Action Services: Watchdog Governance Services, Triage Governance Services, Verification Governance Services,
Remediation Governance Services
and Provisioning Governance Services. 


## Internals

The module structure for the Governance Engine OMAS is as follows:

* [governance-engine-client](governance-engine-client) supports the client library.
* [governance-engine-api](governance-engine-api) supports the common Java classes that are used both by the client and the server.
* [governance-engine-topic-connectors](governance-engine-topic-connectors) provides access to this modules In and Out Topics.
* [governance-engine-server](governance-engine-server) supports in implementation of the access service and its related event management.
* [governance-engine-spring](governance-engine-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

