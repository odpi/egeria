/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIConfigurationErrorException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIInvalidParameterException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UINotAuthorizedException;

/**
 * UIServerAdministration defines the administrative interface for an User Interface (UI) Server.
 * It is used to create both the Java client and the RESTful server-side implementation.  It provides all of the
 * configuration properties for User Interface configuration .
 *
 * <p>
 *     There are four types of operations supported by UIServerAdministration:
 * </p>
 * <ul>
 *     <li>
 *         Basic configuration - these methods use the minimum of configuration information to run the
 *         server using default properties.
 *     </li>
 *     <li>
 *         Advanced Configuration - provides access to all configuration properties to provide
 *         fine-grained control of the server.
 *     </li>
 *     <li>
 *         Initialization and shutdown - these methods control the initialization and shutdown of the
 *         open metadata and governance services based on the supplied configuration.
 *     </li>
 *     <li>
 *         Operational status and control - these methods query the status of the open metadata and governance
 *         services as well as the audit log.
 *     </li>
 * </ul>
 */
public interface UIServerAdministration
{
    /*
     * =============================================================
     * Help the client discover the type of the server
     */

    /**
     * Return the origin of this server implementation.
     *
     * @return OMAG Server Origin
     */
    String getServerOrigin();


    /*
     * =============================================================
     * Configure server - basic options using defaults
     */

    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  The default value is "localhost:8080".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverURLRoot  String url.
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    void setServerURLRoot(String userId,
                          String serverName,
                          String serverURLRoot) throws UINotAuthorizedException,
            UIInvalidParameterException;


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverType  short description for the type of server.
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException invalid serverName or serverType parameter.
     */
    void setServerType(String userId,
                       String serverName,
                       String serverType) throws UINotAuthorizedException,
                                                    UIInvalidParameterException;


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param organizationName  String name of the organization.
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException invalid serverName or organizationName parameter.
     */
    void setOrganizationName(String userId,
                             String serverName,
                             String organizationName) throws UINotAuthorizedException,
                                                                UIInvalidParameterException;


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param id  String user is for the server.
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws UIInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    void setServerUserId(String userId,
                         String serverName,
                         String id) throws UINotAuthorizedException,
                                           UIInvalidParameterException;


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param maxPageSize  max number of elements that can be returned on a request.
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException invalid serverName or organizationName parameter.
     */
    void setMaxPageSize(String userId,
                        String serverName,
                        int maxPageSize) throws UINotAuthorizedException,
                                                    UIInvalidParameterException;

    /*
     * =============================================================
     * Configure server - advanced options overriding defaults
     */



    /*
     * =============================================================
     * Query current configuration
     */

    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId - user that is issuing the request
     * @param serverName - local server name
     * @return UIServerConfig properties or
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException invalid serverName parameter.
     */
    UIServerConfig getCurrentConfiguration(String userId,
                                           String serverName) throws UINotAuthorizedException,
                                                                           UIInvalidParameterException;


    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId - user that is issuing the request
     * @param serverName - local server name
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException the server name is invalid or
     * @throws UIConfigurationErrorException there is a problem using the supplied configuration.
     */
    void activateWithStoredConfig(String userId,
                                  String serverName) throws UINotAuthorizedException,
                                                                   UIInvalidParameterException,
                                                                   UIConfigurationErrorException;


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId - user that is issuing the request
     * @param configuration - properties used to initialize the services
     * @param serverName - local server name
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException the server name is invalid or
     * @throws UIConfigurationErrorException there is a problem using the supplied configuration.
     */
    void activateWithSuppliedConfig(String userId,
                                    String serverName,
                                    UIServerConfig configuration) throws UINotAuthorizedException,
                                                                             UIInvalidParameterException,
                                                                             UIConfigurationErrorException;


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException the serverName is invalid.
     */
    void deactivateTemporarily(String userId,
                               String serverName) throws UINotAuthorizedException,
                                                          UIInvalidParameterException;


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @throws UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws UIInvalidParameterException the serverName is invalid.
     */
    void deactivatePermanently(String userId,
                               String serverName) throws UINotAuthorizedException,
                                                          UIInvalidParameterException;


    /*
     * =============================================================
     * Operational status and control
     */

    /* placeholder */
}
