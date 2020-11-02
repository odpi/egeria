<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.4 (October 2020)

Release 2.4 adds:

* The [Integration Daemon](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md) now makes is simpler to exchange metadata with third party technology 
  such as catalogs and databases.
* The Repository explorer User Interface ('REX') can now be configured with a list of available 
  platforms and servers to improve security and usability. 
* The latest version of Repository Explorer, Type Explorer and Dino, is found 
  in the 'Presentation Server' UI. See 
  [Presentation Server component documentation](https://github.com/odpi/egeria/tree/master/open-metadata-implementation/user-interfaces/presentation-server) 
  and 
  [Configuring the Presentation Server](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/admin-services/docs/user/configuring-the-presentation-server.md)
  .
* Additional [Access Services Functional Verification tests](https://github.com/odpi/egeria/tree/master/open-metadata-test/open-metadata-fvt/access-services-fvt) have been added to improve code quality
* Bug fixes
* Dependency updates
  * Spring has been updated to 5.2.9
  * Spring Security has been updated to 5.4.1
  * Spring Boot has been updated to 2.3.3
  * For a full list run 'mvn dependency:tree' against top level directory and/or review the top level pom.xml

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
