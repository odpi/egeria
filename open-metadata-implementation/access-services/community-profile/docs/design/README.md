<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Community Profile Open Metadata Access Service (OMAS) Design Documentation

The [Community Profile OMAS](../..) is implemented in four modules:

* **community-profile-api** supports the common Java classes that are used both by the client and the server.
This includes the Java API, events, beans, events and REST API structures.
* **community-profile-client** supports the Java client library that allows applications and tools to call the remote REST APIs.
* **community-profile-server** supports the server side implementation of the access service.
  This includes the
  * interaction with the [administration services](../../../../admin-services) for
    registration, configuration, initialization and termination of the access service.
  * interaction with the [repository services](../../../../repository-services) to work with open metadata from the
    [cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md).
  * support for the access service's API and its related event management.
* **community-profile-spring** supports the REST API using the [Spring](../../../../../developer-resources/Spring.md) libraries.

Further documentation can be found:

* [Java API](../../community-profile-client)
* [Beans](../../community-profile-api)
* [Exceptions](../../community-profile-api)
* [Event Payloads](../../community-profile-api)
* [REST API Operations](../../community-profile-server)
* [Client-side design](../../community-profile-client)
* [Server-side design](../../community-profile-server)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

