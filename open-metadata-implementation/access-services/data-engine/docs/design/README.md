<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Engine OMAS Design

The module structure for the Data Engine OMAS is as follows:

* [data-engine-api](../../data-engine-api) supports the common Java classes that are used both by the client and the server. This includes the Java API, beans and REST API structures.
* [data-engine-client](../../data-engine-client) supports the Java client library that allows applications and tools to call the remote REST APIs.
* [data-engine-server](../../data-engine-server) supports the server side implementation of the access service.This includes the
  * interaction with the [administration services](../../../../admin-services) for
    registration, configuration, initialization and termination of the access service.
  * interaction with the [repository services](../../../../repository-services) to work with open metadata from the
    [cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md).
  * support for the access service's API and its related event management.
* [data-engine-spring](../../data-engine-spring) supports the REST API using the [Spring](../../../../../developer-resources/Spring.md) libraries.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.