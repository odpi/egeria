<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Service

A governance service is a specialized
[connector](../../../../frameworks/open-connector-framework/docs/concepts/connector.md)
that implements a specific governance activity.

There are six types of governance services:

* [Open Discovery Service](../../../../frameworks/open-discovery-framework/docs/discovery-service.md) for
  analysing the content of an [Asset's](../../../docs/concepts/assets) real-world counterpart
  in the digital landscape. (For example, if the asset describes a file, the discovery service
  analyses the data stored in the file).
  
* [Open Watchdog Service](../../../../frameworks/governance-action-framework/docs/open-watchdog-service.md) for
  monitoring changes to open metadata elements and when certain changes occur
  (such as the creation of a new [Asset](../../../docs/concepts/assets))
  the watchdog service requests action from
  other governance services by creating either a
  [Governance Action](../../../../frameworks/governance-action-framework/docs/governance-action.md),
  a [Governance Action Process](../../../../frameworks/governance-action-framework/docs/governance-action-process.md)
  or an [Incident Report](../../../../frameworks/governance-action-framework/docs/incident-report.md).
  
* [Open Verification Service](../../../../frameworks/governance-action-framework/docs/open-verification-service.md)
  for testing the properties of specific open metadata elements  
  to ensure they are set up correctly or
  do not indicate a situation where governance activity is required.
  The [results](../../../../frameworks/governance-action-framework/docs/guard.md) returned from the verification service
  can be used to trigger other governance services as part of a
  [Governance Action Process](../../../../frameworks/governance-action-framework/docs/governance-action-process.md).
  
* [Open Triage Service](../../../../frameworks/governance-action-framework/docs/open-triage-service.md) for making
  decisions on how to handle a specific situation or incident.  Often this involves
  a human decision maker.
  
* [Open Remediation Service](../../../../frameworks/governance-action-framework/docs/open-remediation-service.md) for
  correcting errors in open metadata or the digital landscape it represents.
   
* [Open Provisioning Service](../../../../frameworks/governance-action-framework/docs/open-provisioning-service.md) for
  configuring, enabling, provisioning resources in the digital landscape.  Often these provisioning
  services manage the cataloguing of new assets and the lineage between them.

There is an [Open Metadata Engine Service (OMES)](../../../../engine-services)
for each type of governance service.  The engine services support
the specialist REST APIs and event handling needed for the specific
type of governance service.


| Governance Service | Engine Service |
| :----------------- | :------------- | 
| Open Discovery Service | [Asset Analysis OMES](../../../../engine-services/asset-analysis) |
| Open Watchdog Service | [Metadata Watchdog OMES](../../../../engine-services/metadata-watchdog) |
| Open Verification Service | [Rule Verifier OMES](../../../../engine-services/rule-verifier) |
| Open Triage Service | [Request Triage OMES](../../../../engine-services/request-triage) |
| Open Remediation Service | [Issue Remediation OMES](../../../../engine-services/issue-remediation) |
| Open Provisioning Service | [Asset Provisioning OMES](../../../../engine-services/asset-provisioning) |


## Support for implementing governance services

The interface for the Open Discovery Service is defined by
the [Open Discovery Framework (ODF)](../../../../frameworks/open-discovery-framework)
and the rest are defined by the [Governance Action Framework (GAF)](../../../../frameworks/governance-action-framework).

These frameworks provide the guidance to developers of new governance services.

## Support for running governance services

Related governance services are configured together as a
[governance engine](governance-engine.md) and they run in
the appropriate [Open Metadata Engine Service (OMES)](../../../../engine-services).

The [Governance Engine OMAS](..) provides:
* The API to create [governance engine definitions](governance-engine.md) for the governance services.
* The API to link governance services together into [governance action processes](governance-action-process.md).
* The metadata support for the [Engine Host Services](../../../../governance-servers/engine-host-services)
  to drive the governance services in an [Engine Host](../../../../admin-services/docs/concepts/engine-host.md)
  OMAG Server.


----

* [Return to Governance Engine OMAS Concepts](.)
* [Return to Governance Engine OMAS Overview](../..)




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.