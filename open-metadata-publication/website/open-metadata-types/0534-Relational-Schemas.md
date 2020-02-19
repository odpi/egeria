<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0534 Relational Schemas

Model 0534 describes the parts of a relational database schema.
These are used in relational databases.
There are multiple tables and views defined within the relational schema.
The columns are within both the tables and views.

Note that the type information for each column can be directly embedded on the **RelationalColumn** through the
[TypeEmbeddedAttribute](0505-Schema-Attributes.md) classification, as both [TabularColumn](0530-Tabluar-Schemas.md) and
[DerivedSchemaAttribute](0512-Derived-Schema-Elements.md) extend [SchemaAttribute](0505-Schema-Attributes.md).

Also recall that the [NestedSchemaAttribute](0505-Schema-Attributes.md) relationship can be used to link directly
between **RelationalTable** (or **RelationalView**) and the **RelationalColumn**s contained within the table (or view).

![UML](0534-Relational-Schemas.png#pagewidth)


Return to [Area 5](Area-5-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.