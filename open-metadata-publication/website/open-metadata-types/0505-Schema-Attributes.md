<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0505 Schema Attributes

Schemas typically have a hierarchical structure.

Model 0505 provides for a structure of complex schema types that have their own internal structure.
This structure is defined through one or more nested attributes each with their own type.

The **TypeEmbeddedAttribute** classification is applied directly
to the **SchemaAttribute** to provide its type information.
For example, if a ComplexSchemaType has a simple string
attribute this can be captured as a SchemaAttribute (giving the name of the attribute) with a TypeEmbeddedAttribute
classification (whose `dataType` property indicates `string`).

TypeEmbeddedAttribute can represent any of the standard schema types.
Where a schema type is described using multiple schema type objects,
(such as [MapSchemaType](0511-Map-Schema-Elements.md),
[SchemaOptionChoice](0501-Schema-Elements.md) and [ExternalSchemaType](0507-External-Schema-Type.md))
the schema relationships for that type begin with **SchemaElement** and then map to more detailed SchemaTypes.
This is so they can be connected to a SchemaAttribute or a SchemaType.

**SchemaAttribute**s can be nested within other **SchemaAttribute**s
using the **NestedSchemaAttribute** relationship. See [0534 Relational Schemas](0534-Relational-Schemas.md) for an
example of this between RelationalTables and RelationalColumns.

The **SchemaAttributeType** relationship only should be used if the same schema type
is used in multiple attributes within the same schema.
(To use a standard, pre-defined type for a schema attribute's type, set an
[ExternalSchemaType](0507-External-Schema-Type.md) in the TypeEmbeddedAttribute and link to the
standard, pre-defined type using the **LinkedExternalSchemaType** relationship.)

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