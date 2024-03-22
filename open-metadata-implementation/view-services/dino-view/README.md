<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Technical Preview](../../../images/egeria-content-status-tech-preview.png)

# Open Metadata View Services (OMVS)


Develop a user interface to support the Dino UI, which enables a technical user (such as an Egeria Operator) to 
retrieve information about servers, services, cohorts and platforms. 
The interface supports the exploration of an Egeria topology and displays it both visually, as graphs and using  
detailed textual description of each type of artefact. The textual display provides summary and detailed views.

This OMVS calls a remote server using the [repository services client](../../repository-services/repository-services-client/README.md).


The module structure for the Dino OMVS is as follows:

* [dino-view-api](dino-view-api) defines the interface to the view service.
* [dino-view-server](dino-view-server) supports an implementation of the view service.
* [dino-view-spring](dino-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


Return to [open-metadata-implementation](../..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.