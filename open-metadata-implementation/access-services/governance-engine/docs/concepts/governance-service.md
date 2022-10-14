<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Service

A governance service is a specialized [connector](https://egeria-project.org/concepts/connector)
that implements a specific governance activity.

There are six types of governance services:

* [Open Discovery Service](https://egeria-project.org/concepts/open-discovery-service) for
  analysing the content of an [Asset's](https://egeria-project.org/concepts/asset) real-world counterpart
  in the digital landscape. (For example, if the asset describes a file, the discovery service
  analyses the data stored in the file).
  
* [Watchdog Governance Service](../../../../frameworks/governance-action-framework/docs/watchdog-governance-service.md) for
  monitoring changes to open metadata elements and when certain changes occur
  (such as the creation of a new [Asset](https://egeria-project.org/concepts/asset))
  the watchdog service requests action from
  other governance services by creating either a
  [Governance Action](../../../../frameworks/governance-action-framework/docs/governance-action.md),
  a [Governance Action Process](../../../../frameworks/governance-action-framework/docs/governance-action-process.md)
  or an [Incident Report](../../../../frameworks/governance-action-framework/docs/incident-report.md).
  
* [Verification Governance Service](../../../../frameworks/governance-action-framework/docs/verification-governance-service.md)
  for testing the properties of specific open metadata elements  
  to ensure they are set up correctly or
  do not indicate a situation where governance activity is required.
  The [results](../../../../frameworks/governance-action-framework/docs/guard.md) returned from the verification service
  can be used to trigger other governance services as part of a
  [Governance Action Process](../../../../frameworks/governance-action-framework/docs/governance-action-process.md).
  
* [Triage Governance Service](../../../../frameworks/governance-action-framework/docs/triage-governance-service.md) for making
  decisions on how to handle a specific situation or incident.  Often this involves a human decision maker.
  
* [Remediation Governance Service](../../../../frameworks/governance-action-framework/docs/remediation-governance-service.md) for
  correcting errors in open metadata or the digital landscape it represents.
   
* [Provisioning Governance Service](../../../../frameworks/governance-action-framework/docs/provisioning-governance-service.md) for
  configuring, enabling, provisioning resources in the digital landscape.  Often these provisioning
  services manage the cataloguing of new assets and the lineage between them.

The [Governance Action Open Metadata Engine Service (OMES)](../../../../engine-services/governance-action)
supports the execution of the governance action service.
It supports the specialist REST APIs and event handling needed for the specific type of governance action service.


| Governance Service | Engine Service |
| :----------------- | :------------- | 
| Open Discovery Service | [Asset Analysis OMES](../../../../engine-services/asset-analysis) |
| Watchdog Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Verification Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Triage Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Remediation Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |
| Provisioning Governance Service | [Governance Action OMES](../../../../engine-services/governance-action) |


## Support for implementing governance services

The interface for the Open Discovery Service is defined by
the [Open Discovery Framework (ODF)](../../../../frameworks/open-discovery-framework)
and the rest are defined by the [Governance Action Framework (GAF)](../../../../frameworks/governance-action-framework).

These frameworks provide the guidance to developers of new governance services.

## Support for running governance services

Related governance services are configured together as a
[governance engine](https://egeria-project.org/concepts/governance-engine) and they run in
the appropriate [Open Metadata Engine Service (OMES)](../../../../engine-services).

The [Governance Engine OMAS](..) provides:
* The API to create [governance engine definitions](governance-engine.md) for the governance services.
* The API to link governance services together into [governance action processes](https://egeria-project.org/concepts/governance-action-process).
* The metadata support for the [Engine Host Services](../../../../governance-servers/engine-host-services)
  to drive the governance services in an [Engine Host](https://egeria-project.org/concepts/engine-host)
  OMAG Server.


----

* [Return to Governance Engine OMAS Concepts](.)
* [Return to Governance Engine OMAS Overview](../..)




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.