<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Undo Profile

The performance of programmatically reversing the latest update to an existing relationship instance.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for reverting updates on relationship instances, called:

- `undoRelationshipUpdate` - reverts the last update that was made to a relationship

For every relationship type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` relationships of that type that have at least one change. (This uses
   `findRelationships` with a condition on both `metadataCollectionId` and `version` being greater than `1`, and its
   performance is recorded as part of the Relationship Search profile.)
1. For each of these relationship instances, `undoRelationshipUpdate` is called to revert the last change.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will update 50 (types) x 100 (instances per type) = 5000
relationships. (And it will run `findRelationships` 50 times.)

Note the following caveats:

- Relationship type definitions that have no properties will not be reverted: since there are no properties to update,
  there will not have been any updated version (and thus nothing to revert).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.