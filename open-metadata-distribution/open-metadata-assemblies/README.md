<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Assemblies

The assemblies provide independently deployable executables for servers and tool kits.

* **Open Metadata and Governance (OMAG) Server** - provides a server runtime
for Open Metadata and Governance Components that can be selectively configured
to support different integration patterns for tools using the Caller and Adapter
Integration Patterns.

## Future assemblies

* **[Open Connector Toolkit](https://jira.odpi.org/browse/EGERIA-33)** - provides a toolkit
for developers to build connectors to data and related assets.
It includes the APIs for the connectors and some sample connectors
to act as a pattern
as well as guidance on building connectors.

* **[Open Metadata and Governance Discovery Server](https://jira.odpi.org/browse/EGERIA-27)** - provides a server
runtime for discovery services that analyze data resources to augment
the metadata about them, or report exceptions to quality or protection standards.
These discovery services plug into the Open Discovery Framework (ODF). 

* **[Open Discovery Toolkit](https://jira.odpi.org/browse/EGERIA-27)** provides the interfaces for building
**[open discovery framework (ODF)](../../open-metadata-implementation/frameworks/open-discovery-framework/README.md)**
compliant discovery services that run in the discovery server.

* **[Open Metadata and Governance Stewardship Server](https://jira.odpi.org/browse/EGERIA-32)** - provides a server
runtime for exception management and remediation services to enable data
stewards to understand and fix errors in data.
This server supports audit log management, the triage of exceptions generated
through the Governance Action Framework (GAF) and remediation and coordination
workflows associated with governance.

* **[Open Stewardship Toolkit](https://jira.odpi.org/browse/EGERIA-32)** provides the interfaces for building
Governance Action Framework (GAF) compliant stewardship services that run
in the stewardship server.



 