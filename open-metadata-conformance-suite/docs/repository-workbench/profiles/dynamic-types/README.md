<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Dynamic Types Profile

The ability to programmatically create and maintain type definitions.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for creating, patching and deleting
type definitions.  These methods are called:

* `addTypeDefGallery()` - add a collection of new types
* `addTypeDef()` - add a definition of a new entity, relationship or classification type
* `addAttributeTypeDef()` - add a new definition for a new property type that can be
used within a TypeDef.  Typically these are CollectionDefs and EnumDefs.
* `updateTypeDef()` - change the definition of a type
* `deleteTypeDef()` - delete a type
* `deleteAttributeTypeDef()` - delete an attribute type
* `reIdentifyTypeDef()` - change the name of a type
* `reIdentifyAttributeTypeDef()` - change the name of an attribute type

This profile tests that either the metadata repository throws a
`FunctionNotSupportedException` for these methods or they work properly.

## Requirements

The requirements for this profile are as follows:

* **[TypeDef Creation](type-def-add)** - adding new definitions for attributes,
entities, relationships and classifications.

* **[TypeDef Maintenance](type-def-maintenance)** - making changes to existing
types.  Note that this is only possible if there are not instances stored for the
type.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.