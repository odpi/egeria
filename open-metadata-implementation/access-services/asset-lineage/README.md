<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Asset Lineage Open Metadata Access Service (OMAS)

The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.
This access service is used to build **Vertical Lineage** and **Horizontal Lineage** functionality.
On the output topic, it publishes out events that contains full context of data assets and glossary terms involved in lineage.
These events are consumed by the external tools that build the lineage graph in a specific format.

Also, the Asset Lineage OMAS provides an endpoint that publishes the [lineage events](asset-lineage-api/docs/events/lineage-event.md)
associated with the entities involved in lineage.

The module structure for the Asset Lineage OMAS is as follows:

* [asset-lineage-client](asset-lineage-client) supports the client library.
* [asset-lineage-api](asset-lineage-api) supports the common Java classes that are used both by the client and the server.
* [asset-lineage-server](asset-lineage-server) supports in implementation of the access service and its related event management.
* [asset-lineage-spring](asset-lineage-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


## Digging Deeper

* [User Documentation](docs/user)
* [Design Documentation](docs/design)

----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

