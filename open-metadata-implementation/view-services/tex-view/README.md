<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata View Services (OMVS)

![In Development](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png)

Develop an explorer interface to support the Type Explorer UI, which enables a technical user (such as an Enterprise Architect) to 
retrieve open metadata type information from repositories and explore the attributes of each type and the possible combinations of 
entities, relationships and classifications. The interface retrieves the type information and display it both visually, as graphs
of entity type inheritance and the possible relationships in which each entity type can participate; the display also includes a 
detailed textual description of the attributes and possible relationships and classification for each entity type. The textual
display also shows the attributes and entity types for each relationship.

This OMVS calls a remote server using the [repository services client](../../repository-services/repository-services-client/README.md).


The module structure for the Type Explorer OMVS is as follows:

* [tex-view-api](tex-view-api) defines the interface to the view service.
* [tex-view-server](tex-view-server) supports an implementation of the view service.
* [tex-view-spring](tex-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


Return to [open-metadata-implementation](../..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.