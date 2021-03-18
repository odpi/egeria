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
1. **[Relationship Search](profiles/relationship-search)** tests the performance of `findEtlationships`, `findRelationshipsByProperty` and `findRelationshipsByPropertyValue` methods
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

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


  