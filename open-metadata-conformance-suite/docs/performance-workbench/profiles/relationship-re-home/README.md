<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Re-Home Profile

The performance of programmatically changing the home repository of an existing relationship instance.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for changing the home repository of relationship instances, called:

- `reHomeRelationship` - changes the home repository of a relationship

For every relationship type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` reference copy relationship GUIDs of that type. (This uses `findRelationshipsByProperty`
   with a condition on `metadataCollectionId` and its performance is recorded as part of the Relationship Search profile.)
1. For each of these relationship GUIDs, `reHomeEntity` is called to change the home repository to the technology under test's
   `metadataCollectionId`.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will re-home 50 (types) x 100 (instances per type) = 5000
relationships. (And it will run `findRelationshipsByProperty` 50 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.