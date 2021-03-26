<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Retype Profile

The performance of programmatically changing the type of an existing relationship instance.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for changing the type of relationship instances, called:

- `reTypeRelationship` - changes the type of an existing relationship

For every relationship type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` homed relationship GUIDs of that type. (This uses `findRelationshipsByProperty`
   with a condition on `metadataCollectionId` and its performance is recorded as part of the Relationship Search profile.)
1. For each of these relationship GUIDs, `updateRelationshipProperties` is called to remove all the relationship's properties (so it can be easily retyped).
1. For each of these relationship GUIDs, `reTypeRelationship` is called to change the type of the relationship to one of its subtypes.
1. For each of these relationship GUIDs, `reTypeRelationship` is then called to change the type of the relationship back to its original type.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will retype 50 (types) x 100 (instances per type) x 2 (operations) = 10 000
relationships at most. (And it will run `findRelationshipsByProperty` 50 times.)

Note the following caveats:

- Instances of a given type will only be retyped if that type has any subtypes: if it has no subtypes, then all
  retyping operations will be skipped for that type's instances.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.