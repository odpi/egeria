<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Server Author View Service


The Server Author Open Metadata View Service (OMVS) is an Integration View Service that supports Server Author UI operations
that retrieve, author and deploy server configurations.

The purpose of this interface is to enable an IT Infrastructure Architect to author servers in the Egeria eco-system.


This view service offers authoring capabilies; OMAG server configurations are accumulated on the
platform root url that is identified in the Server Author View configuration by the 'omagServerPlatformRootURL'.

While authoring servers, the caller is  not aware of where the server configurations are being accumulated.

In addition to server authoring capabilities, this view service exposes a query that returns the known platforms,
as defined in the view, each platform that is returned contines the servers are stored on them. The caller can
then deploy a server that has been configured onto a particular platform.

In this view server API, platforms and servers are refered to by name, to ease consumption by a user interface.

This view service calls a remote platform using the [admin client](../../admin-services/admin-services-client/README.md).

The module structure for the Server Author OMVS is as follows:

* [server-author-view-api](server-author-view-api) defines the interface to the view service.
* [server-author-view-server](server-author-view-server) supports an implementation of the view service.
* [server-author-view-spring](server-author-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


Return to [open-metadata-implementation](../..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.