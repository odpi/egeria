<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata and Governance Frameworks

The open metadata and governance frameworks define the interfaces for pluggable components.  These
components provide much of the customization offered by the open metadata and governance
implementation.

The open metadata and governance frameworks supported by egeria are as follows:
  
* **[Open Connector Framework (OCF)](open-connector-framework)** provides the interfaces and base implementations for components
(called connectors) that access data-related assets.
OCF connectors also provide detailed metadata about the assets they access.

* **[Open Discovery Framework (ODF)](open-discovery-framework)** provides the interfaces and base implementations for components
(called discovery services) that access data-related assets and extract characteristics 
about the data that can be stored in an open metadata repository.

* **[Governance Action Framework (GAF)](governance-action-framework)** provides the interfaces and base implementations for components
(called governance actions) that take action to correct a situation that is harmful the data,
or the organization in some way.