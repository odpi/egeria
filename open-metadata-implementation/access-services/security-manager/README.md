<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Security Manager Open Metadata Access Service (OMAS)

The Security Manager OMAS provides APIs for technologies wishing to register
new data assets, connections and related schema from data resources located
in database servers, file systems, file managers and content managers.
The caller of this interface may be the security manager itself, or an
[integration daemon](../../admin-services/docs/concepts/governance-server-types.md) if the
security manager does not support open metadata directly.

There are specific APIs for different types of security managers and assets.  These reflect
the terminology typically associated with the specific type of security manager to make it easier
for people to map the Security Manager OMAS APIs and events to the actual technology.
However, the specific implementation objects supported by these APIs all inherit from common
open metadata types so it is possible to work with the resulting metadata in a technology
agnostic manner using services such as the [Asset Consumer OMAS](../asset-consumer).

The Security Manager OMAS APIs needs to accommodate slight variations between different vendor
implementations of security managers, along with information relating to local deployment standards.
As such there is provision in these interfaces to support:

* `VendorProperties` for properties unique to a specific vendor implementation, and
* `AdditionalProperties` for properties that the metadata team wish to add to the metadata.

The module structure for the Security Manager OMAS is as follows:

* [security-manager-client](security-manager-client) supports the client library.
* [security-manager-api](security-manager-api) supports the common Java classes that are used both by the client and the server.
* [security-manager-server](security-manager-server) supports in implementation of the access service and its related event management.
* [security-manager-spring](security-manager-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
* [security-manager-topic-connectors](security-manager-topic-connectors) supports the connectors used to access the InTopic and OutTopic
events from the Security Manager OMAS.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

