<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Technical Preview](../../../images/egeria-content-status-tech-preview.png)

# Repository Explorer View Service

The Repository Explorer (Rex) View Service is an Integration View Service that supports Repository Explorer UI operations
that retrieve metadata type and instance information from metadata repositories.

The purpose of this interface is to enable a technical user (such as an Enterprise Architect) to 
retrieve metadata instances from repositories and explore the connectivity of those instances to other metadata objects. 
The interface enables retrieval and search and the construction of a visualization of a graph of connected objects. 

This view service calls a remote server using the [repository services client](../../repository-services/repository-services-client/README.md).


The module structure for the Repository Explorer OMVS is as follows:

* [rex-view-api](rex-view-api) defines the interface to the view service.
* [rex-view-server](rex-view-server) supports an implementation of the view service.
* [rex-view-spring](rex-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


Return to [open-metadata-implementation](../..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.