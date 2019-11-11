<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository graph queries test case

This test validates that a repository connector supports graph queries on graphs of instances of the types supported by the repository.

## Operation

The graph query tests extract the type definitions for entity and relationship types and create an instance graph consisting of (a subset of)
those types. The graph is then queries using eah of the graph query APIs in the metadata collection interface. This includes the following
API methods:
* getEntityNeighborhood - this is invoked for every entitiy in the graph with different values for 'level'
* getRelatedEntities - this is invoked for every entitiy in the graph
* getLinkingEntities - this is invoked for every pair of entities in the graph (including the case where start == end)

The response from each API call is compared to the expected result which is computed from an in-memory set of indexes that are built alonsgide
the instance graph stored by the repository.

Finally, the instances created by this test are deleted.


## Assertions

* **repository-graph-queries-01** - getEntityNeighborhood returned a result.
* **repository-graph-queries-02** - getEntityNeighborhood returned the expected number of entities.
* **repository-graph-queries-03** - getEntityNeighborhood returned the expected set of entities.
* **repository-graph-queries-04** - getEntityNeighborhood returned the expected number of relationships.
* **repository-graph-queries-05** - getEntityNeighborhood returned the expected set of relationships.
* **repository-graph-queries-06** - getRelatedEntities returned the expected number of entities.
* **repository-graph-queries-07** - getRelatedEntities returned the expected set of entities.
* **repository-graph-queries-08** - getLinkingEntities returned the expected number of entities.
* **repository-graph-queries-09** - getLinkingEntities returned the expected set of entities.
* **repository-graph-queries-10** - getLinkingEntities returned the expected number of relationships.
* **repository-graph-queries-11** - getLinkingEntities returned the expected set of relationships.


## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **graph query support** : Enabled/Disabled - indicates whether the optional support for the graph queries command is enabled or not.

## Sample Output

This is the sample output this testcase. Graph query tests are not specific to a particular type.

```json
     {
         "class": "OpenMetadataTestCaseResult",
         "discoveredProperties": {
             "Graph query support": "Enabled"
         },
         "notSupportAssertions": [],
         "successMessage": "Graph queries can be performed",
         "successfulAssertions": [
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
                 " lines omitted... ",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-graph-queries-01: <null> graph query returned a result.",
             "repository-graph-queries-02: <null> graph query returned the expected number of entities.",
             "repository-graph-queries-03: <null> graph query returned all the expected entities.",
             "repository-graph-queries-04: <null> graph query returned the expected number of relationships.",
             "repository-graph-queries-01: <null> graph query returned a result.",
             "repository-graph-queries-02: <null> graph query returned the expected number of entities.",
             "repository-graph-queries-03: <null> graph query returned all the expected entities.",
                 " lines omitted... ",
             "repository-graph-queries-10: <null> graph query returned the expected number of relationships.",
             "repository-graph-queries-11: <null> graph query returned all the expected relationships."
         ],
         "testCaseDescriptionURL": "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/test-cases/repository-graph-queries-test-case.md",
         "testCaseId": "repository-graph-queries-<null>",
         "testCaseName": "Repository graph query test case",
         "unsuccessfulAssertions": []
     }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.