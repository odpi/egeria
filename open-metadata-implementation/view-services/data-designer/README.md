<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Data Designer OMVS

The Data Designer Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the definition of data structures, data fields and data value specifications.
These can be assembled into data specifications to describe the data requirements for a project or digital product.

## Key Concepts

The Data Designer API supports the following key concepts:

* **Data Structures**: Reusable definitions of the structure of data (e.g., a record or a message).
* **Data Fields**: Individual fields within a data structure.
* **Data Value Specifications**: Definitions of the valid values and formats for data, including data classes and data grains.
* **Data Specifications**: Assembly of data structures and fields with semantic definitions (glossary terms) 
  and data value specifications to provide a complete description of data requirements.

These definitions can also be linked to certification types to guide automated data quality checks.

## Further information

* [Data Designer API Overview](https://egeria-project.org/services/omvs/data-designer/overview/)
* [Data Structure Concept](https://egeria-project.org/concepts/data-structure)
* [Data Field Concept](https://egeria-project.org/concepts/data-field)
* [Data Value Specification Concept](https://egeria-project.org/concepts/data-value-specification)
* [Data Specification Concept](https://egeria-project.org/concepts/data-specification)

Sample requests for the REST API can be found in `Egeria-api-data-designer.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.