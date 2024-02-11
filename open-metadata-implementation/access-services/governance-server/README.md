<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Governance Server Open Metadata Access Service (OMAS)

The **Governance Server OMAS** supports the operation of a [governance server](https://egeria-project.org/concepts/governance-server/)
by providing the metadata services for retrieving and maintaining [Governance Server definitions](https://egeria-project.org/concepts/governance-engine-definition/) and [Integration Groups](https://egeria-project.org/concepts/integration-group/). 

* [Documentation](https://egeria-project.org/services/omas/governance-server/overview)


## Design Information

The module structure for the Governance Server OMAS is as follows:

* [governance-server-client](governance-server-client) supports the client library.
* [governance-server-api](governance-server-api) supports the common Java classes that are used both by the client and the server.
* [governance-server-topic-connectors](governance-server-topic-connectors) provides access to this modules In and Out Topics.
* [governance-server-server](governance-server-server) supports in implementation of the access service and its related event management.
* [governance-server-spring](governance-server-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

