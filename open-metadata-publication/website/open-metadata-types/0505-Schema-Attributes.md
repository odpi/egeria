<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0505 Schema Attributes

Schemas typically have a hierarchical structure.

Model 0505 provides for a structure of complex schema types that have their own internal structure.
This structure is defined through one or more nested attributes each with their own type.

For simple types, the **TypeEmbeddedAttribute** classification can be applied directly
to the **SchemaAttribute** to provide its type information. For example, if a schema has a simple string
attribute this can be captured as a SchemaAttribute (giving the name of the attribute) with a TypeEmbeddedAttribute
classification (whose `dataType` property indicates `string`).

For nested types, the **TypeEmbeddedAttribute** classification can be applied directly to simple nested
**SchemaAttribute**s to provide their type information, and these can be nested within other **SchemaAttribute**s
using the **NestedSchemaAttribute** relationship. See [0534 Relational Schemas](0534-Relational-Schemas.md) for an
example of this between RelationalTables and RelationalColumns.

The **SchemaAttributeType** relationship should be used only for more complex types such as
[0511 Map Schema Elements](0511-Map-Schema-Elements.md).

The combined properties of **SchemaAttribute** can be used to represent simple bounded types
like sets and arrays:

- Array: `minCardinality = 0`, `maxCardinality = -1`, `allowsDuplicateValues = true`, `orderedValues = true`
- Set: `minCardinality = 0`, `maxCardinality = -1`, `allowsDuplicateValues = false`, `orderedValues = false`
- Bag: `minCardinality = 0`, `maxCardinality = -1`, `allowsDuplicateValues = true`, `orderedValues = false`

![UML](0505-Schema-Attributes.png#pagewidth)

Return to [Area 5](Area-5-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.