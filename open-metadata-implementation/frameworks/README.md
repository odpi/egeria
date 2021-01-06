<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Open Metadata and Governance Frameworks

The open metadata and governance frameworks define the interfaces for pluggable components.  These
components provide much of the customization and technology integration points offered by the open metadata and governance
implementation.  

The open metadata and governance frameworks supported by Egeria are as follows:
  
* **[Audit Log Framework (ALF)](audit-log-framework)** provides interfaces and classes to
enable connectors of all types to support natural language diagnostic messages for both
exceptions and the audit log.

* **[Open Connector Framework (OCF)](open-connector-framework)** provides the interfaces and base implementations for components
(called [connectors](open-connector-framework/docs/concepts/connector.md)) that access data-related assets.
OCF connectors also provide detailed metadata about the assets they access.

* **[Governance Action Framework (GAF)](governance-action-framework)** provides the interfaces and base implementations for components
(called governance action services) that take action to assess and correct a situation that is harmful to the data,
or the organization in some way.

* **[Open Discovery Framework (ODF)](open-discovery-framework)** provides the interfaces and base implementations for components
(called discovery services) that access data-related assets and extract characteristics 
about the data that can be stored in an open metadata repository.

**Note:** Both the discovery services and the governance actions are specialized OCF connectors, making the OCF the
**only** plug-in mechanism in the open metadata and governance technology.

----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.