<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Glossary Manager OMVS

The Glossary Manager Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the definition and maintenance of glossaries and their terms.

## Key Features

The Glossary Manager API supports the following key features:

* **Glossary Management**: Create and maintain glossaries. A glossary can be classified as a 
  **taxonomy** (organized into a strict hierarchy) or a **canonical vocabulary** (where each term has a unique name).
* **Glossary Term Management**: Create, update, and delete glossary terms. Terms can be categorized and 
  classified as abstract concepts, data values, questions, activities, or context definitions.
* **Relationship Management**: Define various relationships between glossary terms to show how they are 
  related (e.g., synonyms, related terms, etc.).
* **Controlled Workflow**: Support for a controlled workflow process, allowing for review and approval 
  cycles for glossary content.

## Further information

* [Glossary Manager API Overview](https://egeria-project.org/services/omvs/glossary-manager/overview/)
* [Glossary Concept](https://egeria-project.org/concepts/glossary/)
* [Glossary Term Concept](https://egeria-project.org/concepts/glossary-term/)

Sample requests for the REST API can be found in `Egeria-api-glossary-manager.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.