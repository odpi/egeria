<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Digital Service Open Metadata Access Service (OMAS)

The Digital Service OMAS provides APIs and events for tools that play a role in the lifecycle of an egeria Digital Service.  It enables these tools to query information about the assets it
is deploying, the infrastructure options and any governance actions that need
to be performed.

The module structure for the Digital Service OMAS is as follows:

* [digital-service-client](digital-service-client) supports the client library.
* [digital-service-api](digital-service-api) supports the common Java classes that are used both by the client and the server.
* [digital-service-server](digital-service-server) supports in implementation of the access service and its related event management.
* [digital-service-spring](digital-service-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.



----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

