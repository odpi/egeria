<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Metadata Store Service

The **Metadata Store Service** supports the implementation of a [governance program](../governance-program)
by providing the metadata services for running
**[governance engines](https://egeria-project.org/concepts/governance-engine)**.

* [Documentation](https://egeria-project.org/services/omf-metadata-management)


## Design Information

The module structure for the Metadata Store Service is as follows:

* [omf-metadata-client](omf-metadata-client) supports the client library.
* [omf-metadata-api](omf-metadata-api) supports the common Java classes that are used both by the client and the server.
* [omf-metadata-server](omf-metadata-server) supports in implementation of the access service and its related event management.
* [omf-metadata-spring](omf-metadata-spring) supports the REST API using the [Spring](https://egeria-project.org/guides/contributor/runtime/#spring) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

