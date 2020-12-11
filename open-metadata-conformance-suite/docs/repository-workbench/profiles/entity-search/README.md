<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Entity Search

The ability to search for entity instances.

## Description

There are several methods in the metadata collection API for searching for entities:
* findEntitiesByProperty - this method accepts a match properties parameter that can be used to specify a individual match value for a subset of properties
* findEntitiesByPropertyValue - this method accepts a search string parameter that is compared to each of the string properties of an instance
* findEntities - this method accepts a SearchProperties parameter that can be used to express logical combinations of property conditions using a variety of operators
* findEntitiesByClassification - this method accepts a specification of the classifications that an instance must possess in order to match

Support for these methods is optional for a repository connector.

The requirements listed below correspond to the first three methods. There are no CTS tests yet for the last method in the list.

The conformance test suite has a separate entity-advanced-search profile for search tests that use general regular expressions.

## Requirements

The requirements for this profile are as follows:

* [Entity Property Search](entity-property-search)
* [Entity Value Search](entity-value-search)
* [Entity Condition Search](entity-condition-search)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.