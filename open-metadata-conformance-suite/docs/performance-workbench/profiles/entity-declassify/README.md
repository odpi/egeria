<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Entity Declassification Profile

The performance of programmatically declassifying existing entity instances from existing classifications.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for declassifying entity instances.  These methods are called:

- `declassifyEntity` - removes a classification from an existing entity instance, where the technology under test is the home repository for that classification
- `purgeClassificationReferenceCopy` - removes a classification from an existing entity instance, where the technology under test is _not_ the home repository for that classification

For every classification type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` entities homed in the technology under test with the classification. (This uses
   `findEntitiesByClassification` with a condition on `metadataCollectionId` and its performance is recorded as part of
   the Classification Search profile.)
1. For each of these entity instances, `declassifyEntity` is called to remove the classification of this type from that
   instance.
1. Searches for `instancesPerType` reference copy entities with the classification. (This uses `findEntitiesByClassification`
   with a condition on `metadataCollectionId` and its performance is recorded as part of the Classification Search profile.)
1. For each of these reference copy entity instances, `purgeClassificationReferenceCopy` is called to remove the reference copy
   classification of this type from that instance.

So, for example, if the technology under test supports 50 classification types, and the `instancesPerType` parameter is
set to 100, then this profile will remove 50 (types) x 100 (instances per type) x 2 (home + reference copy methods) = 10 000
classifications. (And it will run `findEntitiesByClassification` 100 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.