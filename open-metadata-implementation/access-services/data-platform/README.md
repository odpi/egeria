<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Data Platform Open Metadata Access Service (OMAS)

The Data Platform OMAS provides APIs for tools and applications wishing to register
new data assets.  It provides the ability to register the host, platform and location of the
data platform itself along with the data assets it hosts.

There are specific APIs for different types of data platforms and assets.  These reflect
the terminology typically associated with the specific type of data platform to make it easier
for people to map the Data Platform OMAS APIs and events to the actual technology.
However, the specific implementation objects supported by these APIs all inherit from common
classes so it is possible to use this interface in a technology-agnostic mode.

The Data Platform OMAS APIs need to accommodate slight variations between different vendor
implementations of data platforms, along with information relating to local deployment standards.
As such there is provision in these interfaces to support:

* `VendorProperties` for properties unique to a specific vendor implementation, and
* `AdditionalProperties` for properties that the infrastructure team wish to add to the metadata.

The module structure for the Data Platform OMAS is as follows:

* [data-platform-client](data-platform-client) supports the client library.
* [data-platform-api](data-platform-api) supports the common Java classes that are used both by the client and the server.
* [data-platform-server](data-platform-server) supports in implementation of the access service and its related event management.
* [data-platform-spring](data-platform-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

