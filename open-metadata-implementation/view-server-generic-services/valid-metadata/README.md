<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Valid Metadata OMVS 

The Valid Metadata OMVS enables the caller to maintain and query valid values for metadata.
[Valid values](https://egeria-project.org/concepts/valid-value/) are used to ensure that metadata 
is consistent and follows established standards, such as code tables or allowed property values.

## Key Features

The Valid Metadata OMVS provides the following capabilities:

- **Valid Metadata Value Management**: Support for setting up, clearing, and retrieving valid metadata values.
  This includes mapping values for different contexts and providing consistent values across different types.
- **Metadata Validation**: Tools for validating that actual metadata values conform to the defined valid values.
- **Open Metadata Type Exploration**: Retrieving definitions for entity types, relationship types, 
  classification types, and attribute types from the open metadata ecosystem.
- **Specification Property Management**: Managing properties used to define specifications, 
  including finding properties by type, name, or GUID.

## Further information

- [Valid Metadata OMVS Overview](https://egeria-project.org/services/omvs/valid-metadata/overview/)
- [Valid Value Concept](https://egeria-project.org/concepts/valid-value/)

Sample REST API requests can be found in:
- [Egeria-api-valid-metadata.http](Egeria-api-valid-metadata.http)
- [Egeria-valid-metadata-lists.http](Egeria-valid-metadata-lists.http)
- [Egeria-valid-type-lists.http](Egeria-valid-type-lists.http)
- [Egeria-specification-properties.http](Egeria-specification-properties.http)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.