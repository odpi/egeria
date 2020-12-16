<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.2 (September 2020)

Below are the highlights of this release:

 * Additional connectors are now placed in our assembly under server/lib without dependencies. If you need to use a connector that requires additional dependencies that are not already part of the server chassis, you will need to add those libraries here.
 * The [docker](https://hub.docker.com/repository/docker/odpi/egeria/tags?page=1&name=2.2) image has been updated to use a later openjdk alpine base image due to incompatibilities with our JanusGraph support in the old images for java 8.
 * In the VDC chart, 2 new values have been added, 'ibmigc.connectorversion' and 'atlas.connectorversion'. In this release these are set to use the 2.1 connectors, since the connectors run to a different release cycle than the main Egeria code. Once new connectors are released you can update these values to get the latest connectors 
 * Further code to support lineage has been added, but in this release it remains in development and is not yet ready for use in production.
 * User interface improvements.
 * Ongoing bug fixes and refactoring especially in subject-area omas.

## Known Issues
 * In the VDC helm chart, the Apache Atlas initialization job fails to complete. This is due to a problem with the Apache Atlas server and Apache SOLR. See https://github.com/odpi/egeria/issues/3587 for more information.

## Dependencies
 * Spring Boot is updated to 2.3.3.
 
## Egeria Implementation Status at Release 2.2
 
 ![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.2.png#pagewidth)
 
 Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
 Open Metadata and Governance vision, strategy and content.
 
----
 * Return to [Release Notes](.)
     
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
