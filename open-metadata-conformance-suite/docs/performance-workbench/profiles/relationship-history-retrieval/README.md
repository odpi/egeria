<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship History Retrieval Profile

The performance of programmatically retrieving the history of existing relationship instances based on their ID.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines methods for retrieving the history of relationship instances by their ID.  These methods are called:

- `getRelationship` - retrieves a relationship instance's details at a given point in time, if the relationship was
  known at that point in time, or throws an exception if not
- `getRelationshipHistory` - retrieves the full history of a relationship instance's details

For every relationship type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entity GUIDs of that type. (This uses `findRelationships` and its performance is recorded
   as part of the Relationship Search profile.)
1. For each of these relationship GUIDs, `getRelationship` is then called with an `asOfTime` using the timestamp captured
   prior to the execution of the Relationship Update profile, to retrieve its historical details from that point in time
   (prior to any updates).
1. For each of these relationship GUIDs, `getRelationshipHistory` is then called to retrieve its full history (including
   current version and all historical versions of the instance).

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will retrieve 50 (types) x 100 (instances per type) x 2 (operations) = 10 000
relationships. (And it will run `findRelationships` 50 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.