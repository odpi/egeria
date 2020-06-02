<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Data Manager Open Metadata Access Service (OMAS)

The Data Manager OMAS provides APIs for technologies wishing to register
new data assets, connections and related schema from data resources located
in database servers, file systems, file managers and content managers.
The caller of this interface may be the data manager itself, or an
[integration daemon](../../admin-services/docs/concepts/governance-server-types.md) if the
data manager does not support open metadata directly.

There are specific APIs for different types of data managers and assets.  These reflect
the terminology typically associated with the specific type of data manager to make it easier
for people to map the Data Manager OMAS APIs and events to the actual technology.
However, the specific implementation objects supported by these APIs all inherit from common
open metadata types so it is possible to work with the resulting metadata in a technology
agnostic manner using services such as the [Asset Consumer OMAS](../asset-consumer).

The Data Manager OMAS APIs needs to accommodate slight variations between different vendor
implementations of data managers, along with information relating to local deployment standards.
As such there is provision in these interfaces to support:

* `VendorProperties` for properties unique to a specific vendor implementation, and
* `AdditionalProperties` for properties that the metadata team wish to add to the metadata.

The module structure for the Data Manager OMAS is as follows:

* [data-manager-client](data-manager-client) supports the client library.
* [data-manager-api](data-manager-api) supports the common Java classes that are used both by the client and the server.
* [data-manager-server](data-manager-server) supports in implementation of the access service and its related event management.
* [data-manager-spring](data-manager-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
* [data-manager-topic-connectors](data-manager-topic-connectors) supports the connectors used to access the InTopic and OutTopic
events from the Data Manager OMAS.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

