<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Governance Officer OMVS

The Governance Officer Open Metadata View Service (OMVS) provides the ability to define and maintain
governance definitions, regulations, metrics, and manage licenses and certifications for metadata elements.
It also supports the tracking of implementation resources back to their design elements and exploring
governance action processes.

## Key features

* **Governance Definition Management**: Support for creating, updating, and deleting governance definitions, including the ability to create definitions from templates.
* **Relationship Management**: Capabilities for linking and detaching peer and supporting governance definitions, and associating definitions with metadata elements using the *GovernedBy* relationship.
* **Regulatory Compliance**: Linking regulator organizations to regulation governance definitions.
* **Governance Results Tracking**: Associating governance metrics with the data stores where measurements are located.
* **Licenses and Certifications**: Managing the lifecycle of licenses and certifications for metadata elements, including linking to license/certification types and updating properties.
* **Governance Action Process Exploration**: Visualizing and exploring the structure of governance action processes through process graphs.
* **Implementation and Design Tracking**: Linking design elements to implementation resources and implementation elements to track how governance requirements are realized.

## Further information

* [Governance Officer OMVS Overview](https://egeria-project.org/services/omvs/governance-officer/overview/)
* [Governance Definition Concept](https://egeria-project.org/concepts/governance-definition/)
* [Governance Action Concept](https://egeria-project.org/concepts/governance-action/)
* [Certification Concept](https://egeria-project.org/concepts/certification/)
* [License Concept](https://egeria-project.org/concepts/license/)

----
Sample REST API requests can be found in [Egeria-api-governance-officer.http](Egeria-api-governance-officer.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.