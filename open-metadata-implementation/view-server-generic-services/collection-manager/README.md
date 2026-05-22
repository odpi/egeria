<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Collection Manager OMVS

The Collection Manager OMVS enables the caller to manage collections of assets and other types of elements.

### Key Features

* **Collection Management**: Support for creating, retrieving, updating, and deleting collections of metadata elements.
* **Collection Hierarchies**: Building and navigating hierarchies of nested collections to organize metadata.
* **Membership Management**: Adding and removing elements (assets, schema elements, etc.) from collections, with support for specific membership properties.
* **Specialized Collections**:
    * **Digital Products**: Managing collections representing digital products and their deployment status.
    * **Agreements**: Linking actors and items to agreements, including support for external contract references.
    * **Skill Sets**: Associating required skill sets with actors and other elements.
* **Collection Classification**: Applying classifications like *Editing*, *Scoping*, or *Staging* to collections to indicate their purpose and lifecycle state.
* **Data Descriptions**: Linking data descriptions to collections to provide context on the data being managed.

### Further information

* [Collection Manager OMVS Overview](https://egeria-project.org/services/omvs/collection-manager/overview/)
* [Collection Concept](https://egeria-project.org/concepts/collection/)
* [Digital Product Concept](https://egeria-project.org/concepts/digital-product/)

----
Sample REST API requests can be found in [Egeria-api-collection-manager.http](Egeria-api-collection-manager.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.