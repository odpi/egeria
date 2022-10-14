<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# DevOps Open Metadata Access Service (OMAS)

The DevOps OMAS provides APIs and events for tools that play a role in a
DevOps pipeline.  It enables these tools to query information about the assets it
is deploying, the infrastructure options and any governance actions that need
to be performed.

* [Documentation](https://egeria-project.org/services/omas/dev-ops/overview)

## Design Information

The module structure for the DevOps OMAS is as follows:

* [dev-ops-client](dev-ops-client) supports the client library.
* [dev-ops-api](dev-ops-api) supports the common Java classes that are used both by the client and the server.
* [dev-ops-server](dev-ops-server) supports in implementation of the access service and its related event management.
* [dev-ops-spring](dev-ops-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.



----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

