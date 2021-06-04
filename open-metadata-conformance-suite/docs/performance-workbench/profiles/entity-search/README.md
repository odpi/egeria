<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity Search Profile

The performance of programmatically searching for existing entity instances.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for searching for entity instances.  These methods are called:

- `findEntities` - arbitrarily complex combinations of search criteria including ranges, nested conditions, etc
- `findEntitiesByProperty` - searches for entity instances based on specific properties with specific values
- `findEntitiesByPropertyValue` - searches for entity instances based on text that matches any textual property

This profile begins by interrogating the technology under test in its entirety, to discover every existing entity
instance of every type known to Egeria. The total count of these is tallied to report later under the Environment
profile. In this way, even for repositories that do not support write operations, we can still calculate some metrics
about read performance (including search) while being able to understand that information in light of the volumes of
metadata in the repository while the test was executed.

These initial tests are also performed with basic sorting of results, and all pages of results are retrieved: thereby
also exercising the efficiency of the technology under test to both sort and cycle through pages of results.

The tests then proceed to exercise other search variations, but only ever retrieve the first page of results
for each:

- `findEntitiesByPropertyValue` for each entity type with the following variations:
    - exact match to a value
    - starts with a value
    - contains a value
    - ends with a value
    - arbitrary regular expression match to a value

- `findEntitiesByProperty` for each entity type, with the following variations -- in each case preferring non-string
  properties (if any exist for the type), given the tests above heavily exercise string-based queries:
    - match a single value
    - match both of two values
    - match either of two values
    - match neither of two values

In addition, these tests will record into the Environment profile the `totalEntitiesFound`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.