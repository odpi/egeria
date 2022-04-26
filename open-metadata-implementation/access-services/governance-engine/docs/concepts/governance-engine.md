<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Governance Engine

A governance engine is responsible for executing requests to a collection of related
[governance services](governance-service.md).

The implementation of a governance engine is
handled by an [Open Metadata Engine Service (OMES)](../../../../engine-services)
running in an [Engine Host](https://egeria-project.org/concepts/engine-host) OMAG Server.

There is a specific engine service for each type of governance engine/service pair.

## Governance Engines

Governance engines define a collection of related governance
services.  Governance services are specialized connectors that
implement a single specialized governance activity.
There are six types of governance service:

* [Open Discovery Service](https://egeria-project.org/concepts/open-discovery-service) for
  analysing the content of an [Asset's](https://egeria-project.org/concepts/asset) real-world counterpart
  in the digital landscape called a [digital resource](https://egeria-project.org/concepts/resource). (For example, if the asset describes a file, the discovery service
  analyses the data stored in the file).
  
* [Open Watchdog Service](../../../../frameworks/governance-action-framework/docs/open-watchdog-service.md) for
  monitoring changes to open metadata elements and when certain changes occur
  (such as the creation of a new [Asset](https://egeria-project.org/concepts/asset))
  the watchdog service requests action from
  other governance services by creating either a
  [Governance Action](https://egeria-project.org/concepts/governance-action),
  a [Governance Action Process](https://egeria-project.org/conceepts/governance-action-process)
  or an [Incident Report](../../../../frameworks/governance-action-framework/docs/incident-report.md).
  
* [Open Verification Service](../../../../frameworks/governance-action-framework/docs/open-verification-service.md)
  for testing the properties of specific open metadata elements  
  to ensure they are set up correctly or
  do not indicate a situation where governance activity is required.
  The [results](https://egeria-project.org/concepts/guard) returned from the verification service
  can be used to trigger other governance services as part of a
  [Governance Action Process](https://egeria-project.org/concepts/governance-action-process).
  
* [Open Triage Service](../../../../frameworks/governance-action-framework/docs/open-triage-service.md) for making
  decisions on how to handle a specific situation or incident.  Often this involves
  a human decision maker.
  
* [Open Remediation Service](../../../../frameworks/governance-action-framework/docs/open-remediation-service.md) for
  correcting errors in open metadata or the digital landscape it represents.
   
* [Open Provisioning Service](../../../../frameworks/governance-action-framework/docs/open-provisioning-service.md) for
  configuring, enabling, provisioning resources in the digital landscape.  Often these provisioning
  services manage the cataloguing of new assets and the lineage between them.

There is a different [Open Metadata Engine Service (OMES)](../../../../engine-services)
depending on the type of governance service.  The engine services support
the specialist REST APIs and event handling needed for the specific
type of governance service.


| Governance Service | Engine Service |
| :----------------- | :------------- | 
| Open Discovery Service | [Asset Analysis OMES](../../../../engine-services/asset-analysis) |
| Watchdog Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Verification Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Triage Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Remediation Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Provisioning Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |


Each governance engine has a unique name. 
A **governance engine definition** for this unique name
is created using the
[Governance Engine OMAS API](../user).

Figure 1 shows the structure of a governance engine definition.
The open metadata types for this definition are in
model [0461 - Governance Engines](https://egeria-project.org/types/4/0461-Governance-Engines.md)
(see **Governance Engine**, **GovernanceService** linked by the **SupportedGovernanceService** relationship.


![Figure 1](../governance-request-type.png)
> **Figure 1:** The structure of a governance engine definition


When a governance engine is called, it is passed a request type
and request parameters.  This is mapped to a call to a [governance service](governance-service.md).


----

* [Return to Governance Engine OMAS Concepts](.)
* [Return to Governance Engine OMAS Overview](../..)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.