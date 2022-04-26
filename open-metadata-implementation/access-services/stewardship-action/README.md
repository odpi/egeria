<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Stewardship Action Open Metadata Access Service (OMAS)

The Stewardship Action OMAS provides APIs and events for tools and applications
focused on resolving issues detected in the data landscape.

* [Documentation](https://egeria-project.org/services/omas/stewardship-action/overview)

## Design Information

The module structure for the Stewardship Action OMAS is as follows:

* [stewardship-action-client](stewardship-action-client) supports the client library.
* [stewardship-action-api](stewardship-action-api) supports the common Java classes that are used both by the client and the server.
* [stewardship-action-server](stewardship-action-server) supports in implementation of the access service and its related event management.
* [stewardship-action-spring](stewardship-action-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.

----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
