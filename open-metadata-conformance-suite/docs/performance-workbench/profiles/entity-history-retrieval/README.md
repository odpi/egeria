<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity History Retrieval Profile

The performance of programmatically retrieving the history of existing entity instances based on their ID.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines methods for retrieving the history of entity instances by their ID.  These methods are called:

- `getEntityDetail` - retrieves an entity instance's details at a given point in time, if the entity was known at that
  point in time, or throws an exception if not
- `getEntityDetailHistory` - retrieves the full history of an entity instance's details

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entity GUIDs of that type. (This uses `findEntities` and its performance is recorded
   as part of the Entity Search profile.)
1. For each of these entity GUIDs, `getEntityDetail` is then called with an `asOfTime` using the timestamp captured
   prior to the execution of the Entity Update profile, to retrieve its historical details from that point in time
   (prior to any updates).
1. For each of these entity GUIDs, `getEntityDetailHistory` is then called to retrieve its full history (including
   current version and all historical versions of the instance).

So, for example, if the technology under test supports 50 entity types, and the `instancesPerType` parameter is
set to 100, then this profile will retrieve 50 (types) x 100 (instances per type) x 2 (operations) = 10 000
entities. (And it will run `findEntities` 50 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.