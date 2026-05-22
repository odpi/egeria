<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Metadata Expert OMVS

The Metadata Expert OMVS provides a comprehensive and advanced API for managing open metadata elements, classifications, and relationships. It is designed for user interfaces and tools that require direct, fine-grained control over metadata and for users who have a deep understanding of the [Open Metadata Types](https://egeria-project.org/types/).

Unlike other view services that focus on specific personas or use cases (such as the Glossary Manager or Asset Catalog), the Metadata Expert OMVS provides a generic interface that can be used to work with any type of metadata supported by the open metadata ecosystem.

### Key Features

The service supports a wide range of operations across the metadata lifecycle:

- **Metadata Element Management**: Full lifecycle support for open metadata elements, including creation (directly or from templates), updates, effectivity date management, archiving, and deletion. It also supports publishing and withdrawing elements.
- **Classification Management**: Capability to classify metadata elements, update classification properties and effectivity dates, and declassify elements.
- **Relationship Management**: Tools for creating and maintaining relationships between metadata elements, including updating relationship properties and detaching elements.
- **Advanced Search and Discovery**:
    - **Search**: Finding elements using search strings, regular expressions, or complex property-based queries.
    - **History**: Retrieving the historical versions and changes for elements, classifications, and relationships.
    - **Anchored Elements**: Exploring elements that are anchored to a specific root element (e.g., all parts of a complex asset).
- **Graph and Relationship Exploration**:
    - **Graph Retrieval**: Retrieving a graph of anchored elements to visualize complex structures.
    - **Relationship Discovery**: Finding relationships between specific elements or groups of elements.
- **Metadata Type Support**: Direct interaction with metadata based on their open metadata types, supporting the full richness of the Egeria type system.

### Further information

- [Metadata Expert OMVS Overview](https://egeria-project.org/services/omvs/metadata-expert/overview/)
- [Open Metadata Types](https://egeria-project.org/types/)
- [Metadata Management Concepts](https://egeria-project.org/concepts/metadata-management/)

----
Sample REST API requests can be found in [Egeria-api-metadata-expert.http](Egeria-api-metadata-expert.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.