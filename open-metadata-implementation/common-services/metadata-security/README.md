<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security

Metadata Security defines the base classes and interfaces for the
open metadata security connectors that
validate access to specific Open Metadata services and instances
for individual users.

There are two types of connector:

* Open metadata platform security connector - secures access to the
platform services that are not specific to an OMAG Server.
This connector is optional and is set up using the 

* 

The connector implements one of more of the following open metadata
interfaces what should be called at the appropriate times
by any [OMAG Server](../../../open-metadata-publication/website/omag-server)
where this connector is installed.

*  **OpenMetadataPlatformSecurity** - provides the interface for a plugin connector that validates whether a calling
   user can access any service on an OMAG Server Platform.  It is called within the context of a specific
   OMAG Server Platform request.
   Each OMAG Server Platform can define its own plugin connector implementation and will have its own instance
   of the connector. 
   
   * **validateUserForPlatform** - Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
       *
       * @param userId calling user
       *
       * @throws UserNotAuthorizedException the user is not authorized to access this platform
       */
      void  validateUserForPlatform(String   userId) throws UserNotAuthorizedException;
  
  
      /**
       * Check that the calling user is authorized to issue administration requests to the OMAG Server Platform.
       *
       * @param userId calling user
       *
       * @throws UserNotAuthorizedException the user is not authorized to change configuration on this platform
       */
      void  validateUserAsAdminForPlatform(String   userId) throws UserNotAuthorizedException;
  
  
  
      /**
       * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
       *
       * @param userId calling user
       *
       * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
       */
      void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException;
  
  
      /**
       * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
       *
       * @param userId calling user
       *
       * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
       */
      void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException;

* OpenMetadataConnectionSecurity - defines the interface of a connector that is validating whether a specific
  user should be given access to a specific Connection object.  This connection information has been retrieved
  from an open metadata repository.  It is used to create a Connector to an Asset.  It may include user
  credentials that could enhance the access to data and function within the Asset that is far above
  the specific user's approval.  This is why this optional check is performed by any open metadata service
  that is returning a Connection object (or a Connector created with the Connection object) to an external party.

Return to [**Common Services**](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.