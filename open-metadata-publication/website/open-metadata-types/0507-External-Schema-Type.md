<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0507 External Schema Type

External schema types allow a schema to reference another schema type
defined outside of the scope of its asset.

The schema type that is linked to is often defined as part of a standard,
of a set of types supported by a specific type of technology.
It is typically reused in assets' schemas where the asset is supporting
a standard schema.  It may also be used as types for schema attributes
where the schema is defined for a specific technology that has a fixed set of types.
For example, a relational database column may be defined with
external types that represent the defined types of the database platform
where the database schema resides.

![UML](0507-External-Schema-Type.png#pagewidth)

**LinkedExternalSchemaType** is linked to a SchemaElement to
enable it to be linked to both an **ExternalSchemaType** and a **SchemaAttribute**.

Return to [Area 5](Area-5-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.