<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.8 (June 2020)

Below are the highlights of Release 1.8:

* New tutorial information has been added in the form of the [Egeria Dojo](../open-metadata-resources/open-metadata-tutorials/egeria-dojo/README.md)
* Usability & Capability improvements to [Repository Explorer](../open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/docs/RepositoryExplorer/RepositoryExplorerGuide.md)
* Samples & utilities are now also packaged into jars with dependencies to make them easier to use (java -jar)
* Connections to kafka will now retry to improve availability. See 'Bring up Issues' in the [connector documentation](../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector/README.md)\

* New dependencies has been included:
    * [Spring Boot Actuator](../open-metadata-implementation/server-chassis/server-chassis-spring/README.md) - Provides features to help you monitor and manage your application when you push it to production
    * micrometer-registry-prometheus - Exposes metrics in a format that can be scraped by a Prometheus server
    
* Many dependencies have been updated. The most relevant include:
    * Spring has been updated to 5.2.6
    * Spring Boot, Spring Security, Spring Security, Spring Data have been updated to 2.3.0

## Egeria Implementation Status at Release 1.8
 
![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-1.8.png#pagewidth)
 
 Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
 Open Metadata and Governance vision, strategy and content.
 
----
 * Return to [Release Notes](.)
        
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
