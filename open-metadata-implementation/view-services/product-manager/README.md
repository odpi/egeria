<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Product Manager OMVS 

The Product Manager Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the definition and maintenance of digital products and digital product families.

## Key Features

The Product Manager API supports the following key features:

* **Digital Product Creation**: Create a digital product and link it, in one call, to its product manager, community, owning collections (folders and product families), guiding questions, product asset, governance definitions (such as the subscriber's license) and data specification.
* **Subscription Types**: Add one-time, periodic and ongoing update subscription types to a digital product.  Each is a notification type, registered with the subscription manager (Baudot by default), plus the governance action process that a subscriber runs to take out a subscription of that type.
* **Digital Product Dependency Management**: Link and unlink dependent digital products to represent product hierarchies and usage.
* **Product Manager Assignment**: Assign and detach product managers to digital products to define responsibility and ownership.
* **Bitol Documents**: Publish, import and generate Open Data Contract Standard (ODCS) and Open Data Product Standard (ODPS) documents.

## Further information

* [Product Manager API Overview](https://egeria-project.org/services/omvs/product-manager/overview/)
* [Digital Product Concept](https://egeria-project.org/concepts/digital-product/)

Sample requests for the REST API can be found in [Egeria-api-product-manager.http](Egeria-api-product-manager.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.