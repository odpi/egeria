<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Update Profile

The performance of programmatically updating existing relationship instances.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for updating relationship instances.  These methods are called:

- `updateRelationshipProperties` - changes one or more values of the properties on an existing relationship
- `updateRelationshipStatus` - changes the status of an existing relationship

Prior to this profile running, a timestamp is captured by the performance workbench to denote a specific date and time
prior to any updates having been made to the relationships.

For every entity type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` relationships of that type. (This uses `findRelationshipsByProperty` with a condition on
   `metadataCollectionId` and its performance is recorded as part of the Entity Search profile.)
1. For each of these relationship instances, `updateRelationshipProperties` is called to update the existing property values of the
   relationship, using a new generated set of properties.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will update 50 (types) x 100 (instances per type) = 5000
relationships. (And it will run `findRelationshipsByProperty` 50 times.)

The properties of each of these instances will be fully-populated with:

- Any string properties containing a value representative of the property name itself (and where unique,
  they will be made unique through appending a unique sequence)
- Any non-string properties will be randomly generated, in a simple attempt to represent data that is not entirely
  uniform

Note the following caveats:

- `updateRelationshipStatus` is currently not tested.
- Relationship type definitions that have no properties will not be updated, since there are no properties to update.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.