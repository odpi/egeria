<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Governance Engine Open Metadata Access Service (OMAS)

The **Governance Engine OMAS** supports the implementation of a [governance program](../governance-program)
by providing the metadata services for running
**[governance engines](https://egeria-project.org/concepts/governance-engine)**.

* [Documentation](https://egeria-project.org/services/omas/governance-engine/overview)


## Design Information

The module structure for the Governance Engine OMAS is as follows:

* [governance-engine-client](governance-engine-client) supports the client library.
* [governance-engine-api](governance-engine-api) supports the common Java classes that are used both by the client and the server.
* [governance-engine-topic-connectors](governance-engine-topic-connectors) provides access to this modules In and Out Topics.
* [governance-engine-server](governance-engine-server) supports in implementation of the access service and its related event management.
* [governance-engine-spring](governance-engine-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

