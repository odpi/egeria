<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Server Author View Service

![In Development](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png)

The Server Author Open Metadata View Service (OMVS) is an Integration View Service that supports Server Author UI operations
that retrieve, author and deploy server configurations

The purpose of this interface is to enable an IT Infrastructure Architect to author servers in the Egeria eco-system.

This view service calls a remote server using the [admin client](../../admin-services/admin-services-client/README.md).


The module structure for the Server author OMVS is as follows:

* [server-author-view-api](server-author-view-api) defines the interface to the view service.
* [server-author-view-server](server-author-view-server) supports an implementation of the view service.
* [server-author-view-spring](server-author-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


Return to [open-metadata-implementation](../..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.