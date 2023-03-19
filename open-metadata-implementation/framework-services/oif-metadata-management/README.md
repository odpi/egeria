<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Open Integration Service

The *Open Integration Service* supports the governance of [integration connectors](https://egeria-project.org/concepts/integration-connector).

* [Documentation](https://egeria-project.org/services/oif-metadata-management)


## Design Information

The module structure for the Metadata Store Service is as follows:

* [oif-metadata-client](oif-metadata-client) supports the client library.
* [oif-metadata-api](oif-metadata-api) supports the common Java classes that are used both by the client and the server.
* [oif-metadata-server](oif-metadata-server) supports in implementation of the access service and its related event management.
* [oif-metadata-spring](oif-metadata-spring) supports the REST API using the [Spring](https://egeria-project.org/guides/contributor/runtime/#spring) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

