<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Reference Data OMVS

The Reference Data Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the capture and maintenance of reference data such as code tables and valid metadata values.

## Key Features

The Reference Data API supports the following key features:

* **Valid Value Definition Management**: Creation, retrieval, update and deletion of valid value definitions.
* **Valid Value Set Management**: Organizing valid values into sets and hierarchies to represent code tables and taxonomies.
* **Reference Data Mapping**: Linking valid values to their equivalents in different systems to support synchronization and mapping.
* **Implementation and Consumption Tracking**: Identifying which systems and data fields use specific reference data.
* **Discovery and Exploration**: Tools for finding valid value definitions by name, search string, or unique identifier.

## Further information

* [Reference Data API Overview](https://egeria-project.org/services/omvs/reference-data/overview/)
* [Valid Value Concept](https://egeria-project.org/concepts/valid-value-definition/)
* [Reference Data Management](https://egeria-project.org/features/reference-data-management/overview)

Sample requests for the REST API can be found in `Egeria-api-reference-data.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.