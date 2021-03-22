<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity Undo Profile

The performance of programmatically reversing the latest update to an existing entity instance.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for reverting updates on entity instances, called:

- `undoEntityUpdate` - reverts the last update that was made to an entity

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entities of that type that have at least one change. (This uses `findEntities` with a
   condition on both `metadataCollectionId` and `version` being greater than `1`, and its performance is recorded as
   part of the Entity Search profile.)
1. For each of these entity instances, `undoEntityUpdate` is called to revert the last change.

So, for example, if the technology under test supports 50 entity types, and the `instancesPerType` parameter is
set to 100, then this profile will update 50 (types) x 100 (instances per type) = 5000
entities. (And it will run `findEntities` 50 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.