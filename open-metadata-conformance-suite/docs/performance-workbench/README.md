<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Conformance Performance Workbench

The open metadata conformance performance workbench is responsible for testing
the performance of various APIs supported by an Open Metadata and Governance (OMAG) Server Repository.

**Note that the workbench is focused purely on measuring performance, and will not extensively test for correctness of
results across a variety of edge cases, etc. For that, use the normal Repository Workbench.**

This workbench runs the following profiles, in the following order:

1. **[Entity Creation](profiles/entity-creation)** tests the performance of `addEntity` and `saveEntityReferenceCopy` methods
1. **[Entity Search](profiles/entity-search)** tests the performance of `findEntities`, `findEntitiesByProperty` and `findEntitiesByPropertyValue` methods
1. **[Relationship Creation](profiles/relationship-creation)** tests the performance of `addRelationship` and `saveRelationshipReferenceCopy` methods
1. **[Relationship Search](profiles/relationship-search)** tests the performance of `findRelationships`, `findRelationshipsByProperty` and `findRelationshipsByPropertyValue` methods
1. **[Entity Classification](profiles/entity-classification)** tests the performance of `classifyEntity` and `saveClassificationReferenceCopy` methods
1. **[Classification Search](profiles/classification-search)** tests the performance of `findEntitiesByClassification` method
1. **[Entity Update](profiles/entity-update)** tests the performance of `updateEntityProperties` method
1. **[Relationship Update](profiles/relationship-update)** tests the performance of `updateRelationshipProperties` method
1. **[Classification Update](profiles/classification-update)** tests the performance of `updateEntityClassification` method
1. **[Entity Undo](profiles/entity-undo)** tests the performance of `undoEntityUpdate` method
1. **[Relationship Undo](profiles/relationship-undo)** tests the performance of `undoRelationshipUpdate` method
1. **[Entity Retrieval](profiles/entity-retrieval)** tests the performance of `isEntityKnown`, `getEntitySummary` and `getEntityDetail` methods
1. **[Entity History Retrieval](profiles/entity-history-retrieval)** tests the performance of `getEntityDetail` (with non-null `asOfTime`) and `getEntityDetailHistory` methods
1. **[Relationship Retrieval](profiles/relationship-retrieval)** tests the performance of `isRelationshipKnown` and `getRelationship` methods
1. **[Relationship History Retrieval](profiles/relationship-history-retrieval)** tests the performance of `getRelationship` (with non-null `asOfTime`) and `getRelationshipHistory` methods
1. **[Entity History Search](profiles/entity-history-search)** tests the performance of the same search operations as Entity Search, but in each case with a non-null `asOfTime`
1. **[Relationship History Search](profiles/relationship-history-search)** tests the performance of the same search operations as Relationship Search, but in each case with a non-null `asOfTime`
1. **[Graph Queries](profiles/graph-queries)** tests the performance of `getRelationshipsForEntity`, `getEntityNeighborhood`, `getRelatedEntities` and `getLinkingEntities` methods
1. **[Graph History Queries](profiles/graph-history-queries)** tests the performance of the same operations as Graph Queries, but in each case with a non-null `asOfTime`
1. **[Entity Re-Home](profiles/entity-re-home)** tests the performance of `reHomeEntity` method
1. **[Relationship Re-Home](profiles/relationship-re-home)** tests the performance of `reHomeRelationship` method
1. **[Entity Declassify](profiles/entity-declassify)** tests the performance of `declassifyEntity` and `purgeClassificationReferenceCopy` methods
1. **[Entity Re-Type](profiles/entity-retype)** tests the performance of `reTypeEntity` method
1. **[Relationship Re-Type](profiles/relationship-retype)** tests the performance of `reTypeRelationship` method
1. **[Entity Re-Identify](profiles/entity-re-identify)** tests the performance of `reIdentifyEntity` method
1. **[Relationship Re-Identify](profiles/relationship-re-identify)** tests the performance of `reIdentifyRelationship` method
1. **[Relationship Delete](profiles/relationship-delete)** tests the performance of `deleteRelationship` method
1. **[Entity Delete](profiles/entity-delete)** tests the performance of `deleteEntity` method
1. **[Entity Restore](profiles/entity-restore)** tests the performance of `restoreEntity` method
1. **[Relationship Restore](profiles/relationship-restore)** tests the performance of `restoreRelationship` method
1. **[Relationship Purge](profiles/relationship-purge)** tests the performance of `purgeRelationship` and `purgeRelationshipReferenceCopy` methods
1. **[Entity Purge](profiles/entity-purge)** tests the performance of `purgeEntity` and `purgeEntityReferenceCopy` methods
1. **[Environment](profiles/environment)** does not actually perform any tests, but rather gives statistics about the environment in which the tests were performed (instance counts, etc)

In each profile, the methods being tested will be executed a number of times and the elapsed time of each execution captured.
These elapsed times are available through the detailed profile results of the Conformance Test Suite reports, and can be
extracted to calculate more detailed statistics (min, max, median, mean, etc).

Configuration of the performance test can be done through the properties passed in to the admin services prior to executing
it:

- `instancesPerType` controls how many instances of metadata to create (per type supported by the repository) (defaults to `50`)
- `maxSearchResults` controls how many results to retrieve per page for any search operations (defaults to `10`)
- `waitBetweenScenarios` controls an optional wait-point between write and read scenarios, in case you are testing a
  repository that has an eventually-consistent index (defaults to `0` to avoid any wait)
- `profilesToSkip` is an optional array of strings of the profile names that should be skipped during performance
  testing (for example, to skip very long-running profiles like the graph queries at the larger scales, where thousands
  or more relationships and entities could be returned by each query)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


  