<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Digital Service Open Metadata Access Service (OMAS)

The Digital Service OMAS is responsible for supporting a digital service manager in the management of [digital services and digital products](https://egeria-project.org/types/7/0710-Digital-Service/).
The documentation for this service is on the main [Egeria documentation site](https://egeria-project.org/services/omas/digital-service/overview).

## Design

The module structure for the Digital Service OMAS is as follows:

* [digital-service-api](digital-service-api) supports the common Java classes that are used both by the client and the server.
* [digital-service-client](digital-service-client) supports the client library.
* [digital-service-server](digital-service-server) supports in implementation of the access service and its related event management.
* [digital-service-spring](digital-service-spring) supports the REST API using the [Spring](https://egeria-project.org/guides/contributor/runtime/) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

