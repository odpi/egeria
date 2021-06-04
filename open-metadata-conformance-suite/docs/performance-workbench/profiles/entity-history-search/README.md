<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity History Search Profile

The performance of programmatically searching for entity instances as they existed in the past.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for searching for entity instances based on their details in the past.
These methods are called:

- `findEntities` - arbitrarily complex combinations of search criteria including ranges, nested conditions, etc
- `findEntitiesByProperty` - searches for entity instances based on specific properties with specific values
- `findEntitiesByPropertyValue` - searches for entity instances based on text that matches any textual property

This profile begins by interrogating the technology under test for each entity type, by calling `findEntities` for that
type GUID with an `asOfTime` using the timestamp captured prior to the execution of the Entity Update profile.

This initial tests are performed with basic sorting of results, and all pages of results are retrieved: thereby
also exercising the efficiency of the technology under test to both sort and cycle through pages of results.

The tests then proceed to exercise other search variations, but only ever retrieve the first page of results
for each:

- `findEntitiesByPropertyValue` for each entity type with the following variations, called with an `asOfTime` using
  the timestamp captured prior to the execution of the Entity Update profile:
    - exact match to a value
    - starts with a value
    - contains a value
    - ends with a value
    - arbitrary regular expression match to a value

- `findEntitiesByProperty` for each entity type, with the following variations -- in each case preferring non-string
  properties (if any exist for the type), given the tests above heavily exercise string-based queries. Each time, the
  method is called with an `asOfTime` using the timestamp captured prior to the execution of the Entity Update profile:
    - match a single value
    - match both of two values
    - match either of two values
    - match neither of two values

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.