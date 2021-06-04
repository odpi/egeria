<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Retrieval Profile

The performance of programmatically retrieving existing relationship instances based on their ID.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines methods for retrieving relationship instances by their ID.  These methods are called:

- `isRelationshipKnown` - retrieves a relationship instance's details, if available, or returns null if not
- `getRelationship` - retrieves a relationship instance's details, if available, or throws an exception if not

For every relationship type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` relationship GUIDs of that type. (This uses `findRelationships` and its performance
   is recorded as part of the Relationship Search profile.)
1. For each of these relationship GUIDs, `isRelationshipKnown` is called to retrieve it.
1. For each of these relationship GUIDs, `getRelationship` is then called to retrieve its details.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will retrieve 50 (types) x 100 (instances per type) x 2 (operations) = 10 000
relationships. (And it will run `findRelationships` 50 times.)

Note the following caveats:

- The same GUIDs are used for each retrieval, but each method is run against every GUID before moving onto the next method.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.