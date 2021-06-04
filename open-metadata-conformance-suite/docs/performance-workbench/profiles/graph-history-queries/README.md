<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Graph History Queries Profile

The performance of programmatically retrieving inter-related instances across potentially multiple degrees of separation,
for a given point in time.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines methods for retrieving inter-related metadata instances across several degrees at a given
point in time in the past.
These methods are called:

- `getRelationshipsForEntity` - retrieves the first-degree relationships linked to an entity
- `getEntityNeighborhood` - retrieves the relationships and entities linked to an entity, up to a specified number of degrees of separation
- `getRelatedEntities` - retrieves the entities linked to an entity, both directly and indirectly
- `getLinkingEntities` - retrieves the relationships and entities that exist between (ultimately connecting) two entities

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entity GUIDs of that type. (This uses `findEntities` and its performance is recorded
   as part of the Entity Search profile.)
1. For each of these entity GUIDs, `getRelationshipsForEntity` is called with an `asOfTime` using the timestamp captured
   prior to the execution of the Entity Update profile, to retrieve first-degree relationships.
1. For each of these entity GUIDs, `getEntityNeighborhood` is then called with an `asOfTime` using the timestamp captured
   prior to the execution of the Entity Update profile, 3 times to retrieve that entity's 1st, 2nd and 3rd degree relationships.
1. For each of these entity GUIDs, `getRelatedEntities` is then called with an `asOfTime` using the timestamp captured
   prior to the execution of the Entity Update profile, to retrieve _all_ the entities that are either directly or
   indirectly related to this entity. These results, per entity, are stored in a Map for the next step.
1. For each of these entity GUIDs, `getLinkingEntities` is then called with an `asOfTime` using the timestamp captured
   prior to the execution of the Entity Update profile, up to 3 times: using the first related entity from the test
   above, the last related entity from the test above, and an entity from the middle of the list of the results from the
   test above.

So, for example, if the technology under test supports 50 entity types, and the `instancesPerType` parameter is
set to 100, then this profile will run 50 (types) x 100 (instances per type) x 8 (maximum operation variations) = 40 000
(at most) queries. (And it will run the initial `findEntities` 50 times.)

Note the following caveats:

- The same GUIDs are used for each retrieval, but each method is run against every GUID before moving onto the next method.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.