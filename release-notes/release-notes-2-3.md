<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.3 (October 2020)

## New capabilities & Major changes
 * A new Presentation Server User interface has been added, making use of [React](https://reactjs.org/) & [Carbon](https://www.carbondesignsystem.com/) 
   - Presentation Server is still in development
   - For developers not contributing to Presentation Server, running in a container under Kubernetes or docker-compose is the easiest way to get started
   - See the last section of the [Presentation Server README](https://github.com/odpi/egeria/tree/egeria-release-2.3/open-metadata-implementation/user-interfaces/presentation-server) for instructions on running Presentation Server
   - For contributors, The [Presentation Server README](https://github.com/odpi/egeria/tree/egeria-release-2.3/open-metadata-implementation/user-interfaces/presentation-server) also documents 'Configuring the Presentation Server' - this is done automatically in our k8s/compose environment. However
     if doing this manually note (4th point) that the environment variable is called `EGERIA_PRESENTATIONSERVER_SERVER_<ui server name>` where
     the `<ui server name>` is the tenant's serverName.. The examples in the document are correct.
 * The Dino User Interface for presentation server now allows an Egeria operations user to display a graph and details of Egeria resources including
   platforms, servers, services and cohort memberships.
 * Type Explorer & Repository Explorer, previously found in the Polymer based UI, are now available
   in Presentation Server.
 * Raise a github issue or Contact the Egeria team via slack  at slack.odpi.com if you experience issues or have questions.
 * The Egeria Docker image is now based on Redhat's UBI-8 openjdk-11 image, to improve security & operational support. See issue #3580
 * Bug Fixes & ongoing feature work

## Known Issues
 * Several maven artifacts have not been published to maven central/JCenter. See issue #3675 They can be retrieved from JFrog Artifactory at 'https://odpi.jfrog.io/odpi/egeria-release-local' if needed and it is not possible to build locally.
   - org.odpi.egeria:presentation-server
   - org.odpi.egeria:subject-area-fvt
   - org.odpi.egeria:dev-ops-api
   - org.odpi.egeria:digital-service-spring
   
## Dependencies
 * Spring has been updated to 2.3.9
 * Spring Security has been updated to 5.4.0
 * For a full list run 'mvn dependency:tree' against top level directory and/or review the top level pom.xml
 
 ## Egeria Implementation Status at Release 2.3
 
![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.3.png#pagewidth)
 
 Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
 Open Metadata and Governance vision, strategy and content.
 
----
 * Return to [Release Notes](.)
    
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
