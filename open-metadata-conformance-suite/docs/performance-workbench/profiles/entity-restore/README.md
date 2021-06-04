<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity Restore Profile

The performance of programmatically reversing the latest soft-delete of an existing entity instance.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines an optional method for reverting soft-deletes on entity instances, called:

- `restoreEntity` - reverts the last soft-delete that was made to an entity

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entity GUIDs of that type that have been soft-deleted. (This uses `findEntitiesByProperty`
   with a condition to limit to the status `DELETED` only and its performance is recorded as part of the Entity Search
   profile.)
1. For each of these entity GUIDs, `restoreEntity` is called to revert the soft-delete and make the entity active again.

So, for example, if the technology under test supports 50 entity types, and the `instancesPerType` parameter is
set to 100, then this profile will re-activate 50 (types) x 100 (instances per type) = 5000
entities. (And it will run `findEntitiesByProperty` 50 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.