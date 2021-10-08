<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0530 Tabular Schemas

Model 0530 shows the the structure of a single table.
This could be a file that is organized into columns such as a [CSV file](0220-Files-and-Folders.md).

Note that for such a simple structure, the type information for each column
can be directly embedded on the **TabularColumn** through the [TypeEmbeddedAttribute](0505-Schema-Attributes.md)
classification.

![UML](0530-Tabular-Schemas.png#pagewidth)


## Further Information

* [Files Integrator OMIS](../../../open-metadata-implementation/integration-services/files-integrator)
* [Data Manager OMAS](../../../open-metadata-implementation/access-services/data-manager)



## Deprecated Types

The **TabularColumnType** entity has been deprecated because it restricts tabular columns to primitive types
and some technologies will be able to support more types.


---

* Return to [Area 5](Area-5-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.