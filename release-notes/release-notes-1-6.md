<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.6 (March 2020)

Release 1.6 adds support for:
   * Audit Log Framework (ALF) technical preview
   * Repository Explorer (TEX) 
    
Below are the highlights:

* There is a new framework:
   * The [Audit Log Framework](../open-metadata-implementation/frameworks/audit-log-framework) (ALF) provides interface definitions and classes to enable connectors to support natural language enabled diagnostics such as exception messages and audit log messages.

* There is a new user interface module:
    * The [Repository Explorer](../open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/docs/RepositoryExplorer/RepositoryExplorerGuide.md) (Rex) can help you explore and visualize the metadata in a repository. It retrieves entities and relationships from the repository and displays them. A details panel also shows the properties and other information about an object. Each entity or relationship is added to a network diagram, which shows how they are connected.

* The Swagger-based API documentation for the Egeria server chassis has been reorganized to align with our modules structure & to provide links into our other documentation which also will clarify if the module is released, in Tech Preview, or still in development. The docs can be found at `http://<server>:<port>/swagger-ui.htm`. Further enhancements will follow in future releases.

* Many dependencies have been updated including:
    * Kafka client upgraded to 2.4.1
    * Spring updated to 5.2.4, spring boot to 2.2.5 & other spring components accordingly.
  
  For a full list refer to the git commit logs.
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
