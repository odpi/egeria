<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository relationship sharing property search test case

This test validates that a repository connector supports `findRelationshipsByProperty` using match properties
and `findRelationshipsByPropertyValue` using search criteria, where the repository does not support the
creation of metadat instances through the metadata collection interface. Such a repository does not
support methods like addEntity() or addRelationship(). This testcase relies on there being existing instances
in the repository; it performs a broad search to retrieve an initial set of instances, and then performs
finer-grain searches and verifies that the results are consistent with the initial instance set and the
query that was performed.

The metadata collection interface 'find' methods accept regular expressions, but for many purposes a caller may be satisfied
with exact match semantics. To achieve this, it is recommended that the caller invokes on the repository helper methods, such
as `getExactMatchRegex()`. This testcase is concerned with this type of use of the metadata collection interface. String
property values and search criteria (a string that is tested against all string properties) are specified
as exact match regular expressions as produced by the repository helper's exact match regex method.

## Operation

The relationship sharing property search testcase extracts the relationship type definitions supported by the repository under test.
For each supported relationship type it performs a broad search using `findRelationshipsByProperty()`, in which no match properties
are specified. This initial search will return up to a page worth of instances. The testcase analyses the returned instances
and constructs follow-on queries based on the property values found in the initial set.

The follow-on queries use property values from the initial set together with different settings for matchCriteria, to
retrieve further result sets. Each result set is verified against what would be expected in terms of the initial set,
the query parameters and the fact that these searches may return hitherto unseen relationships that were not in the initial
set due to paging, but which are nevertheless valid results.


## Assertions

The following assertions are used in this testcase

* **repository-relationship-sharing-property-search-01** - findRelationshipsByProperty returned a result.
* **repository-relationship-sharing-property-search-02** - findRelationshipsByProperty search contained the expected number of relationships.
* **repository-relationship-sharing-property-search-03** - findRelationshipsByProperty search contained expected relationships.
* **repository-relationship-sharing-property-search-04** - findRelationshipsByProperty match criteria ANY returned a result.
* **repository-relationship-sharing-property-search-05** - findRelationshipsByProperty match criteria ANY search contained the expected number of relationships.
* **repository-relationship-sharing-property-search-06** - findRelationshipsByProperty match criteria ANY search contained expected relationships.
* **repository-relationship-sharing-property-search-07** - findRelationshipsByProperty match criteria ALL returned a result.
* **repository-relationship-sharing-property-search-08** - findRelationshipsByProperty match criteria ALL search contained the expected number of relationships.
* **repository-relationship-sharing-property-search-09** - findRelationshipsByProperty match criteria ALL search contained expected relationships.
* **repository-relationship-sharing-property-search-10** - findRelationshipsByProperty match criteria NONE returned a result.
* **repository-relationship-sharing-property-search-11** - findRelationshipsByProperty match criteria NONE search contained the expected number of relationships.
* **repository-relationship-sharing-property-search-12** - findRelationshipsByProperty match criteria NONE search contained expected relationships.
* **repository-relationship-sharing-property-search-13** - findRelationshipsByPropertyValue returned a result.
* **repository-relationship-sharing-property-search-14** - findRelationshipsByPropertyValue search contained the expected number of relationships.
* **repository-relationship-sharing-property-search-15** - findRelationshipsByPropertyValue search contained expected relationships.




## Discovered Properties

There are no discovered properties for this test - the search capabilities are part of the mandatory profile.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.