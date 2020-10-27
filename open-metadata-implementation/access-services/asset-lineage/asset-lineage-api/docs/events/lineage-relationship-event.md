<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Lineage Relationship Event

The Asset Lineage OMAS is publishing a **Lineage Relationship Event** each time a lineage relationship is created, updated or deleted.

The relationships types used to build the lineage use case are: 
* Semantic Assignment
* Term Categorization
* Process Hierarchy
* Process Port
* Port Delegation
* Port Schema
* Asset Schema Type
* Connection To Asset
* Connection Endpoint
* Data content For Data Set
* Attribute for Schema
* Nested File
* Folder Hierarchy 

A relationship event received from the Cohort Topic is processed if the relationship type is in the list mentioned above and if 
at least of the type of the ends type of the relationship is a [lineage entity type](lineage-event.md).

The Lineage Relationship Event contains the description of the relationship, a summary of the lineage entities involved, and the event type.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.