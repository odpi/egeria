<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository relationship property advanced search test case

This test validates that a repository connector supports `findRelationshipsByProperty` and `findRelationshipsByPropertyValue` where the
search criteria (either property values or searchCriteria search string) are passed as regular expressions.

The metadata collection interface 'find' methods may optionally accept general regular expressions. Unlike the mandatory tests in
`TestSupportedRelationshipPropertySearch` in which values are literalised to exact match expressions by the repository helper methods,
such as `getExactMatchRegex()`, this 'advanced' testcase is concerned with the more general cases of arbitrary regular expressions,
that have NOT been literalised.

## Operation

The relationship property advanced search testcase extracts the relationship type definitions supported by the repository under test.
Depending on the number and type of type-defined-attributes of the type, the testcase will create multiple sets of relationship instances.
The decision is based on there is at least one type-defined-attribute that is a string - otherwise regex testing is not possible.
The testcase creates sets of instances that are differentiated by the property values assigned to the relationship properties.
There are three sets of relationship instances:
 * set_0 - this is a disjoint set of relationships, in which the property values are unique to this instance set
 * set_1 - this set of relationships is disjoint from set_0, but shares one property value with the set_2 (below)
 * set_2 - this set of relationships is disjoint from set_0, but shares one property value with the set_1 (above)

The testcase sets of match properties that are intended to match either set_0 instances, or set_1, set_2 instances. It then calls the
`findRelationshipsByProperty` methods passing different combinations of matchProperties and matchCriteria. On some calls the relationship type
filter parameter is used to restrict the set of relationships returned.

The response from each API call is compared to the expected result computed from the known instance sets that are saved in-memory and built alonsgide
the instance sets stored by the repository.

Finally, the sets of instances created by this test are deleted.


## Assertions

Assertions for multi-set tests

* **repository-relationship-property-search-01** - findRelationshipsByProperty with generic regex to match sets 0, 1 & 2 ALL returned a result.
* **repository-relationship-property-search-02** - findRelationshipsByProperty with generic regex to match sets 0, 1 & 2  ALL returned the expected relationships.
* **repository-relationship-property-search-03** - findRelationshipsByProperty with generic regex to match sets 0, 1 & 2  ALL returned no unexpected relationships.
* **repository-relationship-property-search-04** - findRelationshipsByProperty with generic regex to match sets 0, 1 & 2  NONE returned no unexpected relationships.
* **repository-relationship-property-search-05** - findRelationshipsByProperty with regex to match set 0 ALL returned a result.
* **repository-relationship-property-search-06** - findRelationshipsByProperty with regex to match set 0 ALL returned the expected relationships.
* **repository-relationship-property-search-07** - findRelationshipsByProperty with regex to match set 0 ALL returned no unexpected relationships.
* **repository-relationship-property-search-08** - findRelationshipsByProperty with regex to match set 0 NONE returned a result.
* **repository-relationship-property-search-09** - findRelationshipsByProperty with regex to match set 0 NONE returned the expected relationships.
* **repository-relationship-property-search-10** - findRelationshipsByProperty with regex to match set 0 NONE returned no unexpected relationships.
* **repository-relationship-property-search-11** - findRelationshipsByProperty with regex to match sets other than 1 & 2 ALL returned a result.
* **repository-relationship-property-search-12** - findRelationshipsByProperty with regex t match sets other than 1 & 2 ALL  returned the expected relationships.
* **repository-relationship-property-search-13** - findRelationshipsByProperty with regex to match sets other than 1 & 2 ALL returned no unexpected relationships.
* **repository-relationship-property-search-14** - findRelationshipsByProperty with regex to match sets other than 1 & 2 NONE returned a result.
* **repository-relationship-property-search-15** - findRelationshipsByProperty with regex to match sets other than 1 & 2 NONE returned the expected relationships.
* **repository-relationship-property-search-16** - findRelationshipsByProperty with regex to match sets other than 1 & 2 NONE returned no unexpected relationships.
* **repository-relationship-property-search-17** - findRelationshipsByPropertyValue with regex to match set 0 returned a result.
* **repository-relationship-property-search-18** - findRelationshipsByPropertyValue with regex to match set 0  returned the expected relationships.
* **repository-relationship-property-search-19** - findRelationshipsByPropertyValue with regex to match set 0 returned no unexpected relationships.
* **repository-relationship-property-search-20** - findRelationshipsByPropertyValue with regex for set_1 n set_2 returned a result.
* **repository-relationship-property-search-21** - findRelationshipsByPropertyValue with regex for set_1 n set_2 returned the expected relationships.
* **repository-relationship-property-search-22** - findRelationshipsByPropertyValue with regex for set_1 n set_2 returned no unexpected relationships.



## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` advanced search support** : Enabled/Disabled - indicates whether the optional support for the regex processing is enabled or not.


## Sample Output

This is the sample output for a multi-set test run (for the GovernanceControlLink type).

```json
     {
         "class": "OpenMetadataTestCaseResult",
         "notSupportAssertions": [],
         "successMessage": "Relationships can be searched by property and property value",
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
             "repository-relationship-property-advanced-search-01: GovernanceControlLink search returned results.",
             "repository-relationship-property-advanced-search-02: GovernanceControlLink search contained all expected results.",
             "repository-relationship-property-advanced-search-03: GovernanceControlLink search contained only valid results.",
             "repository-relationship-property-advanced-search-04: GovernanceControlLink search contained only valid results.",
             "repository-relationship-property-advanced-search-05: GovernanceControlLink search returned results.",
             "repository-relationship-property-advanced-search-06: GovernanceControlLink search contained all expected results.",
             "repository-relationship-property-advanced-search-07: GovernanceControlLink search contained only valid results.",
             "repository-relationship-property-advanced-search-08: GovernanceControlLink search returned results.",
             "repository-relationship-property-advanced-search-09: GovernanceControlLink search contained all expected results.",
             "repository-relationship-property-advanced-search-10: GovernanceControlLink search contained only valid results.",
             "repository-relationship-property-advanced-search-11: GovernanceControlLink search returned results.",
             "repository-relationship-property-advanced-search-12: GovernanceControlLink search contained all expected results.",
             "repository-relationship-property-advanced-search-13: GovernanceControlLink search contained only valid results.",
             "repository-relationship-property-advanced-search-14: GovernanceControlLink search returned results.",
             "repository-relationship-property-advanced-search-15: GovernanceControlLink search contained all expected results.",
             "repository-relationship-property-advanced-search-16: GovernanceControlLink search contained only valid results.",
             "repository-relationship-property-advanced-search-17: GovernanceControlLink value search returned results.",
             "repository-relationship-property-advanced-search-18: GovernanceControlLink value search contained all expected results.",
             "repository-relationship-property-advanced-search-19: GovernanceControlLink value search contained only valid results.",
             "repository-relationship-property-advanced-search-20: GovernanceControlLink value search returned results.",
             "repository-relationship-property-advanced-search-21: GovernanceControlLink value search contained all expected results.",
             "repository-relationship-property-advanced-search-22: GovernanceControlLink value search contained only valid results.",
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
         "testCaseDescriptionURL": "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/test-cases/repository-relationship-property-advanced-search-test-case.md",
         "testCaseId": "repository-relationship-property-advanced-search-GovernanceControlLink",
         "testCaseName": "Repository relationship property advanced search test case",
         "unsuccessfulAssertions": []
     }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.