<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Lineage Event

A **Lineage Event** is sent each time an entity involved in lineage is updated or deleted.

The lineage entity types processed are: 
* Glossary Term
* Glossary Category
* Process  
* Relational Column
* Relational Table
* Data File
* Tabular Column

The **Vertical Lineage** is built using the Glossary Terms context and relationships and the **Horizontal Lineage** is built around Process entities. 


For Glossary Terms, the lineage event is sent if there are Semantic Assignment and/or Term Categorization relationships where the term is assigned.
The Glossary Term Context Event contains the description of the term plus the full context of the 
Glossary Category or Schema Elements that are involved in lineage relationship with the processed term. 
This is send only when the Semantic Assigment or Term Categorization relationships are created.

The event sent for Process entities includes information about the lineage process, the lineage mappings and context of the Schema Elements. 
The full context of the Process is first time sent when the status of the entity is changed from DRAFT to ACTIVE, for the rest of the lineage entity types 
the Lineage Event sent contains only the entity that has been changed.

An event is sent for the lineage entities if a classification has been assigned, changed or deleted. 
The default list of classifications that are relevant for lineage is:
* Confidentiality
* Asset Zone Membership
* Subject Area
* Asset Ownership

This list of classifications can be changed using the Asset Lineage OMAS configuration.

## Related information

* [Configuring the lineage classifications](../../../asset-lineage-server/docs/configuration/configuring-the-lineage-classifications.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.