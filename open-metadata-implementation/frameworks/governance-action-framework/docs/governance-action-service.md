<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Governance Action Service

A governance action service is a specialized [connector](../../open-connector-framework/docs/concepts/connector.md)
that performs monitoring of metadata changes, validation of metadata, triage of issues, assessment and/or remediation activities on request.

There are five types of governance action services:
  
* **[Watchdog Governance Service Service](watchdog-governance-service.md)** listens for changes to metadata and initiates
  [governance actions](governance-action.md), [governance action processes](governance-action-process.md)
  or an [incident report](incident-report.md).
  
* **[Verification Governance Action Service](verification-governance-service.md)** validates that the metadata elements, relationships and
  classification are set up as they should be.  For example, it may check that a new asset has an owner, is set up
  with zones and includes a connection and a schema there possible.  It produces [guards](guard.md)
  that define what needs to be done.
  
* **[Triage Governance Action Service](triage-governance-service.md)** runs triage rules to determine how to manage a situation or request.
  Often this involves a human decision maker.   It may initiate an external workflow, wait for manual
  decision or create a **To Do** for a specific person.
  
* **[Remediation Governance Action Service](remediation-governance-service.md)** makes updates to metadata elements, relationships between them
  and classifications. Examples of remediation governance action services are duplicate linking and consolidating.

* **[Provisioning Governance Action Service](provisioning-governance-service.md)**  invokes a provisioning service whenever a provisioning request is made. 
  Typically the provisioning service is an external service.  It may also create lineage metadata to
  describe the work of the provisioning engine.         


----
* Return the [Governance Action Framework Overview](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.