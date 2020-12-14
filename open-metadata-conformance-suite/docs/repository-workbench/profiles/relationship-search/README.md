<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Relationship Search

The ability to search for relationship instances.

## Description

There are several methods in the metadata collection API for searching for relationships:
* findRelationshipByProperty - this method accepts a match properties parameter that can be used to specify a individual match value for a subset of properties
* findRelationshipsByPropertyValue - this method accepts a search string parameter that is compared to each of the string properties of an instance
* findRelationships - this method accepts a SearchProperties parameter that can be used to express logical combinations of property conditions using a variety of operators


Support for these methods is optional for a repository connector.

The requirements listed below correspond to the above three methods.

The conformance test suite has a separate relationship-advanced-search profile for search tests that use general regular expressions.

## Requirements

The requirements for this profile are as follows:

* [Relationship Property Search](relationship-property-search)
* [Relationship Value Search](relationship-value-search)
* [Relationship Condition Search](relationship-condition-search)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.