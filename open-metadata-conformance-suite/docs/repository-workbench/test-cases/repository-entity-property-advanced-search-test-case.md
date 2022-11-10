<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity property advanced search test case

This test validates that a repository connector supports `findEntitiesByProperty` and `findEntitiesByPropertyValue` where the
search criteria (either property values or searchCriteria search string) are passed as regular expressions.

The metadata collection interface 'find' methods may optionally accept general regular expressions. Unlike the mandatory tests in
`TestSupportedEntityPropertySearch` in which values are literalised to exact match expressions by the repository helper methods,
such as `getExactMatchRegex()`, this 'advanced' testcase is concerned with the more general cases of arbitrary regular expressions,
that have NOT been literalised.

## Operation

The entity property advanced search testcase extracts the entity type definitions supported by the repository under test.
Depending on the number and type of type-defined-attributes of the type, the testcase will create multiple sets of entity instances.
The decision is based on there is at least one type-defined-attribute that is a string - otherwise regex testing is not possible.
The testcase creates sets of instances that are differentiated by the property values assigned to the entity properties.
There are three sets of entity instances:
 * set_0 - this is a disjoint set of entities, in which the property values are unique to this instance set
 * set_1 - this set of entities is disjoint from set_0, but shares one property value with the set_2 (below)
 * set_2 - this set of entities is disjoint from set_0, but shares one property value with the set_1 (above)

The testcase sets of match properties that are intended to match either set_0 instances, or set_1, set_2 instances. It then calls the
`findEntitiesByProperty` methods passing different combinations of matchProperties and matchCriteria. On some calls the entity type
filter parameter is used to restrict the set of entities returned.

The response from each API call is compared to the expected result computed from the known instance sets that are saved in-memory and built alonsgide
the instance sets stored by the repository.

Finally, the sets of instances created by this test are deleted.


## Assertions

Assertions for multi-set tests

* **repository-entity-property-search-01** - findEntitiesByProperty with generic regex to match sets 0, 1 & 2 ALL returned a result.
* **repository-entity-property-search-02** - findEntitiesByProperty with generic regex to match sets 0, 1 & 2  ALL returned the expected entities.
* **repository-entity-property-search-03** - findEntitiesByProperty with generic regex to match sets 0, 1 & 2  ALL returned no unexpected entities.
* **repository-entity-property-search-04** - findEntitiesByProperty with generic regex to match sets 0, 1 & 2  NONE returned no unexpected entities.
* **repository-entity-property-search-05** - findEntitiesByProperty with regex to match set 0 ALL returned a result.
* **repository-entity-property-search-06** - findEntitiesByProperty with regex to match set 0 ALL returned the expected entities.
* **repository-entity-property-search-07** - findEntitiesByProperty with regex to match set 0 ALL returned no unexpected entities.
* **repository-entity-property-search-08** - findEntitiesByProperty with regex to match set 0 NONE returned a result.
* **repository-entity-property-search-09** - findEntitiesByProperty with regex to match set 0 NONE returned the expected entities.
* **repository-entity-property-search-10** - findEntitiesByProperty with regex to match set 0 NONE returned no unexpected entities.
* **repository-entity-property-search-11** - findEntitiesByProperty with regex to match sets other than 1 & 2 ALL returned a result.
* **repository-entity-property-search-12** - findEntitiesByProperty with regex t match sets other than 1 & 2 ALL  returned the expected entities.
* **repository-entity-property-search-13** - findEntitiesByProperty with regex to match sets other than 1 & 2 ALL returned no unexpected entities.
* **repository-entity-property-search-14** - findEntitiesByProperty with regex to match sets other than 1 & 2 NONE returned a result.
* **repository-entity-property-search-15** - findEntitiesByProperty with regex to match sets other than 1 & 2 NONE returned the expected entities.
* **repository-entity-property-search-16** - findEntitiesByProperty with regex to match sets other than 1 & 2 NONE returned no unexpected entities.
* **repository-entity-property-search-17** - findEntitiesByPropertyValue with regex to match set 0 returned a result.
* **repository-entity-property-search-18** - findEntitiesByPropertyValue with regex to match set 0  returned the expected entities.
* **repository-entity-property-search-19** - findEntitiesByPropertyValue with regex to match set 0 returned no unexpected entities.
* **repository-entity-property-search-20** - findEntitiesByPropertyValue with regex for set_1 n set_2 returned a result.
* **repository-entity-property-search-21** - findEntitiesByPropertyValue with regex for set_1 n set_2 returned the expected entities.
* **repository-entity-property-search-22** - findEntitiesByPropertyValue with regex for set_1 n set_2 returned no unexpected entities.



## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` advanced search support** : Enabled/Disabled - indicates whether the optional support for the regex processing is enabled or not.


## Sample Output

This is the sample output for a multi-set test run (for the Database type).

```json
     {
         "class": "OpenMetadataTestCaseResult",
         "notSupportAssertions": [],
         "successMessage": "Entities can be searched by property and property value",
         "successfulAssertions": [
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-entity-property-advanced-search-01: Database search returned results.",
             "repository-entity-property-advanced-search-02: Database search contained all expected results.",
             "repository-entity-property-advanced-search-03: Database search contained only valid results.",
             "repository-entity-property-advanced-search-04: Database search contained only valid results.",
             "repository-entity-property-advanced-search-05: Database search returned results.",
             "repository-entity-property-advanced-search-06: Database search contained all expected results.",
             "repository-entity-property-advanced-search-07: Database search contained only valid results.",
             "repository-entity-property-advanced-search-08: Database search returned results.",
             "repository-entity-property-advanced-search-09: Database search contained all expected results.",
             "repository-entity-property-advanced-search-10: Database search contained only valid results.",
             "repository-entity-property-advanced-search-11: Database search returned results.",
             "repository-entity-property-advanced-search-12: Database search contained all expected results.",
             "repository-entity-property-advanced-search-13: Database search contained only valid results.",
             "repository-entity-property-advanced-search-14: Database search returned results.",
             "repository-entity-property-advanced-search-15: Database search contained all expected results.",
             "repository-entity-property-advanced-search-16: Database search contained only valid results.",
             "repository-entity-property-advanced-search-17: Database value search returned results.",
             "repository-entity-property-advanced-search-18: Database value search contained all expected results.",
             "repository-entity-property-advanced-search-19: Database value search contained only valid results.",
             "repository-entity-property-advanced-search-20: Database value search returned results.",
             "repository-entity-property-advanced-search-21: Database value search contained all expected results.",
             "repository-entity-property-advanced-search-22: Database value search contained only valid results.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
             "repository-test-case-base-01: Repository connector supplied to conformance suite.",
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite."
         ],
         "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-entity-property-advanced-search-test-case.md",
         "testCaseId": "repository-entity-property-advanced-search-Database",
         "testCaseName": "Repository entity property advanced search test case",
         "unsuccessfulAssertions": []
     }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.