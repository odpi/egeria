<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.4 (November 2020)

* The [Integration Daemon](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md)
  now makes it simpler to exchange metadata with third party technology such as catalogs and databases.
* The Repository Explorer User Interface ('REX') must now be configured with a list of available 
  platforms and servers to improve security and usability. 
* The latest version of Repository Explorer, Type Explorer and Dino, is found 
  in the 'Presentation Server' UI.
  * The Repository Explorer User Interface ('REX') must now be configured with a list of available
    platforms and servers to improve security and usability. See
    [Presentation Server component documentation](https://github.com/odpi/egeria/tree/master/open-metadata-implementation/user-interfaces/presentation-server)
    and
    [Configuring the Presentation Server](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/admin-services/docs/user/configuring-the-presentation-server.md).
* The [UI Server Chassis](https://github.com/odpi/egeria/tree/master/open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring)
  no longer includes static content. It is now required to deploy the [egeria-ui]( https://github.com/odpi/egeria-ui)
  project in addition to the spring application.
  A [docker image](https://hub.docker.com/r/odpi/egeria-uistatic) is available, 
  and the [docker-compose](https://github.com/odpi/egeria/blob/master/open-metadata-resources/open-metadata-tutorials/lab-infrastructure-guide/running-docker-compose.md) 
  & [Kubernetes](https://github.com/odpi/egeria/blob/master/open-metadata-resources/open-metadata-tutorials/lab-infrastructure-guide/running-kubernetes.md) 
  lab environments include this pre-configured.
* Additional [Access Services Functional Verification tests](https://github.com/odpi/egeria/tree/master/open-metadata-test/open-metadata-fvt/access-services-fvt) have been added to improve code quality
* Bug fixes
* Dependency updates
  * Spring has been updated to 5.2.9
  * Spring Security has been updated to 5.4.1
  * Spring Boot has been updated to 2.3.3
  * For a full list run 'mvn dependency:tree' against top level directory and/or review the top level pom.xml

## Known Issues

* The docker-compose based lab environment is incorrectly pulling docker images from the wrong repository.  To correct this change `open-metadata-resources/open-metadata-deployment/compose/tutorials/.env` to `egeria_repo=odpi`. The original repository
has however been updated to include 2.4 images, so will now work even if unchanged.



## Egeria Implementation Status at Release 2.4

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.4.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.

----
* Return to [Release Notes](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
