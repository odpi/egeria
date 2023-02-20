<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity property search test case

This test validates that a repository connector supports `findEntitiesByProperty` and `findEntitiesByPropertyValue` where the
search criteria (either property values or searchCriteria search string) are specified as exact match expressions.

The metadata collection interface 'find' methods accept regular expressions, but for many purposes a caller may be satisfied
with a exact match semantics. To achieve this, it is recommended that the caller invokes on the repository helper methods, such
as `getExactMatchRegex()`. This testcase is concerned with this type of use of the metadata collection interface.

## Operation

The entity property search testcase extracts the entity type definitions supported by the repository under test. Depending on the number and
type of type-defined-attributes of the type, the testcase will either create one set or multiple sets of entity instances. The decision is
based on there being sufficient number of attributes to be able to differentiate the instances by property value. If there are insufficient
attributes, a single set test is performed. If there are sufficient attributes, a multi-set test is performed.
In the latter case, the testcase creates sets of instances that are differentiated by the property values assigned to the entity properties.
There are three sets of entity instances:
 * set_0 - this is a disjoint set of entities, in which the property values are unique to this instance set
 * set_1 - this set of entities is disjoint from set_0, but shares one property value with the set_2 (below)
 * set_2 - this set of entities is disjoint from set_0, but shares one property value with the set_1 (above)

The testcase sets of match properties that are intended to match either set_0 instances, or set_1, set_2 instances. It then calls the
`findEntitiesByProperty` methods passing different combinations of matchProperties and matchCriteria. On some calls the entity type
filter parameter is used to restrict the set of entities returned.


The response from each API call is compared to the expected result computed from the known instance sets that are saved in-memory and built alonsgide
the instance sets stored by the repository. Results from calls with type filtering are compared against the result of an identical request with wildcard
entity type, by comparing the number of instances of the specified type and its subtypes with the number calculated by post-filtering the wildcard query.

Finally, the sets of instances created by this test are deleted.


## Assertions

Assertions for multi-set tests

* **repository-entity-property-search-01** - findEntitiesByProperty for set_0 ALL returned a result.
* **repository-entity-property-search-02** - findEntitiesByProperty for set_0 ALL returned the expected entities.
* **repository-entity-property-search-03** - findEntitiesByProperty for set_0 ALL returned no unexpected entities.
* **repository-entity-property-search-04** - findEntitiesByProperty for set_0 ANY returned a result.
* **repository-entity-property-search-05** - findEntitiesByProperty for set_0 ANY returned the expected entities.
* **repository-entity-property-search-06** - findEntitiesByProperty for set_0 ANY returned no unexpected entities.
* **repository-entity-property-search-07** - findEntitiesByProperty for set_0 NONE returned a result.
* **repository-entity-property-search-08** - findEntitiesByProperty for set_0 NONE returned the expected entities.
* **repository-entity-property-search-09** - findEntitiesByProperty for set_0 NONE returned no unexpected entities.
* **repository-entity-property-search-10** - findEntitiesByProperty for set_1 ALL returned a result.
* **repository-entity-property-search-11** - findEntitiesByProperty for set_1 ALL returned the expected entities.
* **repository-entity-property-search-12** - findEntitiesByProperty for set_1 ALL returned no unexpected entities.
* **repository-entity-property-search-13** - findEntitiesByProperty for set_1 ANY returned a result.
* **repository-entity-property-search-14** - findEntitiesByProperty for set_1 ANY returned the expected entities.
* **repository-entity-property-search-15** - findEntitiesByProperty for set_1 ANY returned no unexpected entities.
* **repository-entity-property-search-16** - findEntitiesByProperty for set_1 NONE returned a result.
* **repository-entity-property-search-17** - findEntitiesByProperty for set_1 NONE returned the expected entities.
* **repository-entity-property-search-18** - findEntitiesByProperty for set_1 NONE returned no unexpected entities.
* **repository-entity-property-search-19** - findEntitiesByProperty for set_1 NONE returned a result.
* **repository-entity-property-search-20** - findEntitiesByProperty for set_1 NONE with type filtering returned the expected number of entities.
* **repository-entity-property-search-21** - findEntitiesByPropertyValue for set_0 returned a result.
* **repository-entity-property-search-22** - findEntitiesByPropertyValue for set_0 returned the expected entities.
* **repository-entity-property-search-23** - findEntitiesByPropertyValue for set_0 returned no unexpected entities.
* **repository-entity-property-search-24** - findEntitiesByPropertyValue for set_1 n set_2 returned a result.
* **repository-entity-property-search-25** - findEntitiesByPropertyValue for set_1 n set_2 returned the expected entities.
* **repository-entity-property-search-26** - findEntitiesByPropertyValue for set_1 n set_2 returned no unexpected entities.

Assertions for single-set tests

* **repository-entity-property-search-27** - findEntitiesByProperty for set_0 ALL returned a result.
* **repository-entity-property-search-28** - findEntitiesByProperty for set_0 ALL returned the expected entities.
* **repository-entity-property-search-29** - findEntitiesByProperty for set_0 ALL returned no unexpected entities.
* **repository-entity-property-search-30** - findEntitiesByProperty for set_0 ANY returned a result.
* **repository-entity-property-search-31** - findEntitiesByProperty for set_0 ANY returned the expected entities.
* **repository-entity-property-search-32** - findEntitiesByProperty for set_0 ANY returned no unexpected entities.
* **repository-entity-property-search-33** - findEntitiesByProperty for set_0 NONE returned no unexpected entities.





## Discovered Properties

There are no discovered properties for this test - the search capabiliites are part of the mandatory profile.


## Sample Output

This is the sample output for a multi-set test run (for the Document type).

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
             " lines omitted ",
             "repository-entity-property-search-01: Document search returned results.",
             "repository-entity-property-search-02: Document search contained all expected results.",
             "repository-entity-property-search-03: Document search contained only valid results.",
             "repository-entity-property-search-04: Document search returned results.",
             "repository-entity-property-search-05: Document search contained all expected results.",
             "repository-entity-property-search-06: Document search contained only valid results.",
             "repository-entity-property-search-07: Document search returned results.",
             "repository-entity-property-search-08: Document search contained all expected results.",
             "repository-entity-property-search-09: Document search contained only valid results.",
             "repository-entity-property-search-10: Document search returned results.",
             "repository-entity-property-search-11: Document search contained all expected results.",
             "repository-entity-property-search-12: Document search contained only valid results.",
             "repository-entity-property-search-13: Document search returned results.",
             "repository-entity-property-search-14: Document search contained all expected results.",
             "repository-entity-property-search-15: Document search contained only valid results.",
             "repository-entity-property-search-16: Document search returned results.",
             "repository-entity-property-search-17: Document search contained all expected results.",
             "repository-entity-property-search-18: Document search contained only valid results.",
             "repository-entity-property-search-19: Document search with type filter returned results.",
             "repository-entity-property-search-20: Document search with type filter returned expected number of results.",
             "repository-entity-property-search-21: Document value search returned results.",
             "repository-entity-property-search-22: Document value search contained all expected results.",
             "repository-entity-property-search-23: Document value search contained only valid results.",
             "repository-entity-property-search-24: Document value search returned results.",
             "repository-entity-property-search-25: Document value search contained all expected results.",
             "repository-entity-property-search-26: Document value search contained no unexpected results.",
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
             "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite."
         ],
         "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-entity-property-search-test-case.md",
         "testCaseId": "repository-entity-property-search-Document",
         "testCaseName": "Repository entity property search test case",
         "unsuccessfulAssertions": []
     }

```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.