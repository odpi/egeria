<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository relationship property search test case

This test validates that a repository connector supports `findRelationshipsByProperty` and `findRelationshipsByPropertyValue` where the
search criteria (either property values or searchCriteria search string) are specified as exact match expressions.

The metadata collection interface 'find' methods accept regular expressions, but for many purposes a caller may be satisfied
with a exact match semantics. To achieve this, it is recommended that the caller invokes on the repository helper methods, such
as `getExactMatchRegex()`. This testcase is concerned with this type of use of the metadata collection interface.

## Operation

The relationship property search testcase extracts the relationship type definitions supported by the repository under test. Depending on the number and
type of type-defined-attributes of the type, the testcase will either create one set or multiple sets of relationship instances. The decision is
based on there being sufficient number of attributes to be able to differentiate the instances by property value. If there are insufficient
attributes, a single set test is performed. If there are sufficient attributes, a multi-set test is performed.
In the latter case, the testcase creates sets of instances that are differentiated by the property values assigned to the relationship properties.
There are three sets of relationship instances:
 * set_0 - this is a disjoint set of relationships, in which the property values are unique to this instance set
 * set_1 - this set of relationships is disjoint from set_0, but shares one property value with the set_2 (below)
 * set_2 - this set of relationships is disjoint from set_0, but shares one property value with the set_1 (above)

The testcase sets of match properties that are intended to match either set_0 instances, or set_1, set_2 instances. It then calls the
`findRelationshipsByProperty` methods passing different combinations of matchProperties and matchCriteria. On some calls the relationship type
filter parameter is used to restrict the set of relationships returned.


The response from each API call is compared to the expected result computed from the known instance sets that are saved in-memory and built alonsgide
the instance sets stored by the repository. Results from calls with type filtering are compared against the result of an identical request with wildcard
relationship type, by comparing the number of instances of the specified type and its subtypes with the number calculated by post-filtering the wildcard query.

Finally, the sets of instances created by this test are deleted.


## Assertions

Assertions for multi-set tests

* **repository-relationship-property-search-01** - findRelationshipsByProperty for set_0 ALL returned a result.
* **repository-relationship-property-search-02** - findRelationshipsByProperty for set_0 ALL returned the expected relationships.
* **repository-relationship-property-search-03** - findRelationshipsByProperty for set_0 ALL returned no unexpected relationships.
* **repository-relationship-property-search-04** - findRelationshipsByProperty for set_0 ANY returned a result.
* **repository-relationship-property-search-05** - findRelationshipsByProperty for set_0 ANY returned the expected relationships.
* **repository-relationship-property-search-06** - findRelationshipsByProperty for set_0 ANY returned no unexpected relationships.
* **repository-relationship-property-search-07** - findRelationshipsByProperty for set_0 NONE returned a result.
* **repository-relationship-property-search-08** - findRelationshipsByProperty for set_0 NONE returned the expected relationships.
* **repository-relationship-property-search-09** - findRelationshipsByProperty for set_0 NONE returned no unexpected relationships.
* **repository-relationship-property-search-10** - findRelationshipsByProperty for set_1 ALL returned a result.
* **repository-relationship-property-search-11** - findRelationshipsByProperty for set_1 ALL returned the expected relationships.
* **repository-relationship-property-search-12** - findRelationshipsByProperty for set_1 ALL returned no unexpected relationships.
* **repository-relationship-property-search-13** - findRelationshipsByProperty for set_1 ANY returned a result.
* **repository-relationship-property-search-14** - findRelationshipsByProperty for set_1 ANY returned the expected relationships.
* **repository-relationship-property-search-15** - findRelationshipsByProperty for set_1 ANY returned no unexpected relationships.
* **repository-relationship-property-search-16** - findRelationshipsByProperty for set_1 NONE returned a result.
* **repository-relationship-property-search-17** - findRelationshipsByProperty for set_1 NONE returned the expected relationships.
* **repository-relationship-property-search-18** - findRelationshipsByProperty for set_1 NONE returned no unexpected relationships.
* **repository-relationship-property-search-19** - findRelationshipsByProperty for set_1 NONE returned a result.
* **repository-relationship-property-search-20** - findRelationshipsByProperty for set_1 NONE with type filtering returned the expected number of relationships.
* **repository-relationship-property-search-21** - findRelationshipsByPropertyValue for set_0 returned a result.
* **repository-relationship-property-search-22** - findRelationshipsByPropertyValue for set_0 returned the expected relationships.
* **repository-relationship-property-search-23** - findRelationshipsByPropertyValue for set_0 returned no unexpected relationships.
* **repository-relationship-property-search-24** - findRelationshipsByPropertyValue for set_1 n set_2 returned a result.
* **repository-relationship-property-search-25** - findRelationshipsByPropertyValue for set_1 n set_2 returned the expected relationships.
* **repository-relationship-property-search-26** - findRelationshipsByPropertyValue for set_1 n set_2 returned no unexpected relationships.

Assertions for single-set tests

* **repository-relationship-property-search-27** - findRelationshipsByProperty for set_0 ALL returned a result.
* **repository-relationship-property-search-28** - findRelationshipsByProperty for set_0 ALL returned the expected relationships.
* **repository-relationship-property-search-29** - findRelationshipsByProperty for set_0 ALL returned no unexpected relationships.
* **repository-relationship-property-search-30** - findRelationshipsByProperty for set_0 ANY returned a result.
* **repository-relationship-property-search-31** - findRelationshipsByProperty for set_0 ANY returned the expected relationships.
* **repository-relationship-property-search-32** - findRelationshipsByProperty for set_0 ANY returned no unexpected relationships.
* **repository-relationship-property-search-33** - findRelationshipsByProperty for set_0 NONE returned no unexpected relationships.





## Discovered Properties

There are no discovered properties for this test - the search capabiliites are part of the mandatory profile.


## Sample Output

This is the sample output for a multi-set test run (for the ExternalReferenceLink type).

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
             " lines omitted ",
             "repository-relationship-property-search-01: ExternalReferenceLink search returned results.",
             "repository-relationship-property-search-02: ExternalReferenceLink search contained all expected results.",
             "repository-relationship-property-search-03: ExternalReferenceLink search contained only valid results.",
             "repository-relationship-property-search-04: ExternalReferenceLink search returned results.",
             "repository-relationship-property-search-05: ExternalReferenceLink search contained all expected results.",
             "repository-relationship-property-search-06: ExternalReferenceLink search contained only valid results.",
             "repository-relationship-property-search-07: ExternalReferenceLink search returned results.",
             "repository-relationship-property-search-08: ExternalReferenceLink search contained all expected results.",
             "repository-relationship-property-search-09: ExternalReferenceLink search contained only valid results.",
             "repository-relationship-property-search-10: ExternalReferenceLink search returned results.",
             "repository-relationship-property-search-11: ExternalReferenceLink search contained all expected results.",
             "repository-relationship-property-search-12: ExternalReferenceLink search contained only valid results.",
             "repository-relationship-property-search-13: ExternalReferenceLink search returned results.",
             "repository-relationship-property-search-14: ExternalReferenceLink search contained all expected results.",
             "repository-relationship-property-search-15: ExternalReferenceLink search contained only valid results.",
             "repository-relationship-property-search-16: ExternalReferenceLink search returned results.",
             "repository-relationship-property-search-17: ExternalReferenceLink search contained all expected results.",
             "repository-relationship-property-search-18: ExternalReferenceLink search contained only valid results.",
             "repository-relationship-property-search-19: ExternalReferenceLink search with type filter returned results.",
             "repository-relationship-property-search-20: ExternalReferenceLink search with type filter returned expected number of results.",
             "repository-relationship-property-search-21: ExternalReferenceLink value search returned results.",
             "repository-relationship-property-search-22: ExternalReferenceLink value search contained all expected results.",
             "repository-relationship-property-search-23: ExternalReferenceLink value search contained only valid results.",
             "repository-relationship-property-search-24: ExternalReferenceLink value search returned results.",
             "repository-relationship-property-search-25: ExternalReferenceLink value search contained all expected results.",
             "repository-relationship-property-search-26: ExternalReferenceLink value search contained no unexpected results.",
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
         "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-relationship-property-search-test-case.md",
         "testCaseId": "repository-relationship-property-search-ExternalReferenceLink",
         "testCaseName": "Repository relationship property search test case",
         "unsuccessfulAssertions": []
     }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.