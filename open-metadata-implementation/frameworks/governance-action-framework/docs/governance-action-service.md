<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Governance Action Service

A governance action service is a specialized [connector](../../open-connector-framework/docs/concepts/connector.md)
that performs triage, assessment and/or remediation activities on request.

There are five types of governance action services:
  
* **Watchdog Governance Service** listens for changes to metadata.   One example of a
  governance action service is duplicate detection. Another example is to monitor the addition of
  open discovery reports and take action on their content. 
  
* ** 
  Alterruns triage rules to determine
  how to manage the request.  This could be to initiate an external workflow, wait for manual
  decision or initiate a remediation request.
  
* **Remediation Governance Service** - Hosts [OpenRemediationEngines](../../../frameworks/governance-action-framework/docs/g)
  that monitor for remediation requests and runs the requested remediation service.
  Examples of remediation services are duplicate linking and consolidating.
  
* **Action Scheduler** - Hosts [OpenSchedulingEngines](verification-governance-service.md)
  that maintains a calendar of events and creates RequestForAction instances at the requested
  time.  For example, it may move assets between zones when a particular date is reached.

* **Asset Provisioning** - Hosts [OpenProvisioningEngines](../../../frameworks/governance-action-framework/docs/open-provisioning-engine.md)
  that invokes a provisioning service whenever a provisioning request is made.  Typically the
  provisioning service is an external service.  It may also create lineage metadata to
  describe the work of the provisioning engine.         


----
* Return the [Governance Action Framework Overview](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.