<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity Retrieval Profile

The performance of programmatically retrieving existing entity instances based on their ID.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines methods for retrieving entity instances by their ID.  These methods are called:

- `isEntityKnown` - retrieves an entity instance's details, if available, or returns null if not
- `getEntitySummary` - retrieves a summary of an entity instance, without its properties
- `getEntityDetail` - retrieves an entity instance's details, if available, or throws an exception if not

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entity GUIDs of that type. (This uses `findEntities` and its performance is recorded
   as part of the Entity Search profile.)
1. For each of these entity GUIDs, `isEntityKnown` is called to retrieve it.
1. For each of these entity GUIDs, `getEntitySummary` is then called to retrieve its summary.
1. For each of these entity GUIDs, `getEntityDetail` is then called to retrieve its details.

So, for example, if the technology under test supports 50 entity types, and the `instancesPerType` parameter is
set to 100, then this profile will retrieve 50 (types) x 100 (instances per type) x 3 (operations) = 15 000
entities. (And it will run `findEntities` 50 times.)

Note the following caveats:

- The same GUIDs are used for each retrieval, but each method is run against every GUID before moving onto the next method.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.