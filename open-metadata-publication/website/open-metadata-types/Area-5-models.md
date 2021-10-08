<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Area 5 Models - Schema, Models and Reference Data

Area 5 is where standards are established.
This includes data models, schema fragments and reference data
that are used to assist developers and architects in using best
practice data structures and valid values as they develop new
capability around the data assets.

![UML Packages](area-5-models-and-schema-overview.png#pagewidth)

The first set of models describe the structure of data as it is deployed.
These models describe the abstract definition of structured data.

* **[0501 Schema Elements](0501-Schema-Elements.md)**
* **[0505 Schema Attributes](0505-Schema-Attributes.md)**
* **[0507 External Schema Types](0507-External-Schema-Type.md)**
* **[0511 Map Schema Element](0511-Map-Schema-Elements.md)**
* **[0512 Derived Schema Elements](0512-Derived-Schema-Elements.md)**

These next models show how the schema is attached to assets and ports on a process.

* **[0503 Asset Schema](0503-Asset-Schema.md)**
* **[0520 Process Schemas](0520-Process-Schemas.md)**
* **[0525 Process Variables](0525-Process-Variables.md)**

This next model provides the ability to capture a template for generating schema artifacts for
direct inclusion in an implementation.

* **[0504 Implementation Snippets](0504-Implementation-Snippets.md)**

The next set of models are specializations of the abstract schema elements
described aimed at different types of technology.

* **[0530 Tabular Schema](0530-Tabular-Schemas.md)**
* **[0531 Document Schemas](0531-Document-Schemas.md)**
* **[0532 Object Schemas](0532-Object-Schemas.md)**
* **[0533 Graph Schema](0533-Graph-Schemas.md)**
* **[0534 Relational Schema](0534-Relational-Schemas.md)**
* **[0535 Event Schema](0535-Event-Schemas.md)**
* **[0536 API Schemas](0536-API-Schemas.md)**
* **[0537 Display Schemas](0537-Display-Schemas.md)**

Data classes describe specify types of data - such as data, address, credit card, social security number.
A data class is identified for a specific schema attribute by looking at the data values stored in it.

* **[0540 Data Classes](0540-Data-Classes.md)**

Reference data describes standard sets of data values - such as a list of country codes.

* **[0545 Reference Data](0545-Reference-Data.md)**

Instance metadata identifies schema attributes that contain metadata, rather than business data.

* **[0550 Instance Metadata](0550-Instance-Metadata.md)**

The next set of models describe the content of different types of data models

* **[0565 Design Model Elements](0565-Design-Model-Elements.md)**
* **[0566 Design Model Organization](0566-Design-Model-Organization.md)**
* **[0568 Design Model Scoping](0568-Design-Model-Scoping.md)**
* **[0569 Design Model Implementation](0569-Design-Model-Implementation.md)**
* **[0570 Metamodels](0570-Metamodels.md)**
* **[0571 Concept Models](0571-Concept-Models.md)**

The final set of models capture architectures and patterns.

* **[0580 Solution Blueprints](0580-Solution-Blueprints.md)**
* **[0581 Solution Ports and Wires](0581-Solution-Ports-and-Wires.md)**
* **[0595 Design Patterns](0595-Design-Patterns.md)**

## Further reading

* [Modelling schemas](../modelling-technology/modelling-schemas.md)

* Specific schema structures supported by the integration services
   * For API Schemas - [API Integrator OMIS](../../../open-metadata-implementation/integration-services/api-integrator)
   * For File Schemas - [Files Integrator OMIS](../../../open-metadata-implementation/integration-services/files-integrator)
   * For Database Schemas - [Database Integrator OMIS](../../../open-metadata-implementation/integration-services/database-integrator)
   * For Event Schemas - [Topic Integrator OMIS](../../../open-metadata-implementation/integration-services/topic-integrator)
   * For Forms and Report Schemas - [Display Integrator OMIS](../../../open-metadata-implementation/integration-services/display-integrator)

Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.