<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity Re-Home Profile

The performance of programmatically changing the home repository of an existing entity instance.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for changing the home repository of entity instances, called:

- `reHomeEntity` - changes the home repository of an entity

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` reference copy entity GUIDs of that type. (This uses `findEntitiesByProperty`
   with a condition on `metadataCollectionId` and its performance is recorded as part of the Entity Search profile.)
1. For each of these entity GUIDs, `reHomeEntity` is called to change the home repository to the technology under test's
   `metadataCollectionId`.

So, for example, if the technology under test supports 50 entity types, and the `instancesPerType` parameter is
set to 100, then this profile will re-home 50 (types) x 100 (instances per type) = 5000
entities. (And it will run `findEntitiesByProperty` 50 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.