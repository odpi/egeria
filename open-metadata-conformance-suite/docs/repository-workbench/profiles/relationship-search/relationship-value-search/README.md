<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->




# Relationship Value Search

The ability to search for relationship instances using the findRelationshipsByPropertyValue() method.

## Description

These tests validate that a repository connector supports the relationship-value-search requirement which is part of the relationship-search profile.

If instances already exist in the repository under test, these instances are used for the test. If no instances already exist, test instances are
created and are cleaned up at the end of the test. If there are no existing instances and it is not possible to create instances, the test
reports unknown-status.

The tests exercise the repository connector under test by different serch strings for the searchCriteria parameter with values known to be present in the
repository's instances. The tests then perform the searches.

The expected set of instances is determined in advance of the search, and compared to the set of relationships returned by the search. Due to paging
limits, additional instances that were not previously seen may be returned by the method under test and these are validated against the specified
match properties. The test succeeds provided all expected instances are found and any unexpected instances are valid matches for the search criteria.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.