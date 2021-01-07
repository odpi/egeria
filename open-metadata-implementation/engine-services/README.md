<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Open Metadata Engine Services (OMES)

The engine services are each able to host a specific
type of governance engine. 

Broadly there are two types of Governance Engines:
* [Discovery Engines](../frameworks/open-discovery-framework/docs/discovery-engine.md) that run automatic metadata
discovery requests to analyse the content of an [asset's](../access-services/docs/concepts/assets) real-world counterpart.
* [Governance Action Engines](../frameworks/governance-action-framework/docs/goverance-action-engine.md) that
 manage the processing supporting governance processing such as the the resolution of issues reported in the open
metadata ecosystem or the assets it supports.


## Engine Services Descriptions

* [Asset Analysis OMES](asset-analysis) - Analyses the content of an asset's real world counterpart, generates annotations
  in an open discovery report that is attached to the asset in the open metadata repositories.
   * Loads [Open Discovery Engines](../frameworks/open-discovery-framework/docs/discovery-engine.md),
   * runs [Open Discovery Services](../frameworks/open-discovery-framework/docs/discovery-service.md) and 
   * calls the [Discovery Engine OMAS](../access-services/discovery-engine).
  
* [Metadata Watchdog OMES](metadata-watchdog) - Monitors changes in the metadata and initiates updates as a result.  
  One example of a watchdog service is duplicate detection. Another example is to monitor the addition of 
  open discovery reports and take action on their content.  
  Examples of updates include creating RequestForAction instances.
   * Loads **Open WatchDog Engines**,
   * runs **Open WatchDog Services** and
   * calls the [Governance Engine OMAS](../access-services/governance-engine).
  
* [Request Triage OMES](request-triage) - Monitors for new/changed RequestForAction instances and runs triage rules to 
  determine how to manage the request.  This could be to initiate an external workflow, wait for manual decision or 
  initiate a remediation request.
   * Loads **Open Triage Engines**,
   * runs **Open Triage Services** and
   * calls the [Governance Engine OMAS](../access-services/governance-engine).
  
* [Issue Remediation OMES](issue-remediation) - Monitors for RemediationRequest instances and runs the requested 
  remediation service. Examples of remediation services are duplicate linking and consolidating.
   * Loads **Open Remediation Engines**,
   * runs **Open Remediation Services** and  
   * calls the [Governance Engine OMAS](../access-services/governance-engine).
  
* [Rule Verifier OMES](rule-verifier) - Validates that specific metadata elements are set up correctly. For example, 
  it could detect assets that are not allocated to a governance zone.
   * Loads **Open Verification Engines**,
   * runs **Open Verification Services** and
   * calls the [Governance Engine OMAS](../access-services/governance-engine).
  
* [Asset Provisioning OMES](asset-provisioning) - Invokes a provisioning service whenever a provisioning 
  request is made.  Typically the provisioning service is an external service.  It may also create lineage metadata 
  to describe the work of the provisioning engine.
   * Loads **Open Provisioning Engines**,
   * runs **Open Provisioning Services** and
   * calls the [Governance Engine OMAS](../access-services/governance-engine).


----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.