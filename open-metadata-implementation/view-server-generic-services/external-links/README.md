<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# External Links OMVS

The External Links API enables the caller to manage the definitions of external references and external identifiers.

### Key Features

* **External Reference Management**: Support for creating, retrieving, updating, and deleting external references (links to resources outside of the open metadata ecosystem).
* **External Identifier Management**: Support for adding, updating, and deleting external identifiers that link an open metadata element to its equivalent in an external system.
* **Relationship Management**:
    * Linking external references to any metadata element.
    * Managing specialized external references such as media references and cited document references.
    * Managing the synchronization status and confirmation of external identifiers.
* **Discovery and Search**: Finding external references and external identifiers by name, search strings, or through their unique identifiers (GUIDs).

### Further information

* [External Links API Overview](https://egeria-project.org/services/omvs/external-links/overview/)
* [External Reference Concept](https://egeria-project.org/concepts/external-reference/)
* [External Identifier Concept](https://egeria-project.org/features/external-identifiers/overview/)

----
Sample REST API requests can be found in [Egeria-api-external-links.http](Egeria-api-external-links.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.