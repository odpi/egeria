<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Data Manager Open Metadata Access Service (OMAS)

The Data Manager OMAS provides APIs for technologies wishing to register
new data assets, connections and related schema from data resources located
in database servers, file systems, event brokers, API gateways and file managers and content managers.

* [Documentation](https://egeria-project.org/services/omas/data-manager/overview)

## Design information

The module structure for the Data Manager OMAS is as follows:

* [data-manager-client](data-manager-client) supports the client library.
* [data-manager-api](data-manager-api) supports the common Java classes that are used both by the client and the server.
* [data-manager-server](data-manager-server) supports in implementation of the access service and its related event management.
* [data-manager-spring](data-manager-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
* [data-manager-topic-connectors](data-manager-topic-connectors) supports the connectors used to access the OutTopic
events from the Data Manager OMAS.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

