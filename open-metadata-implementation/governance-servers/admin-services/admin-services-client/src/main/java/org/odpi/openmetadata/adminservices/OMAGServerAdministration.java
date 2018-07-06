/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices;


import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;
import java.util.Map;

/**
 * OMAGServerAdministration defines the administrative interface for an Open Metadata and Governance (OMAG) Server.
 * It is used to create both the Java client and the RESTful server-side implementation.  It provides all of the
 * configuration properties for the Open Metadata Access Services (OMASs) and delegates administration requests
 * to the Open Metadata Repository Services (OMRS).
 *
 * <p>
 *     There are four types of operations supported by OMAGServerAdministration:
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
public interface OMAGServerAdministration
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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    void setServerURLRoot(String    userId,
                          String    serverName,
                          String    serverURLRoot) throws OMAGNotAuthorizedException,
                                                          OMAGInvalidParameterException;


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverType  short description for the type of server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    void setServerType(String    userId,
                       String    serverName,
                       String    serverType) throws OMAGNotAuthorizedException,
                                                    OMAGInvalidParameterException;


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param organizationName  String name of the organization.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    void setOrganizationName(String    userId,
                             String    serverName,
                             String    organizationName) throws OMAGNotAuthorizedException,
                                                                OMAGInvalidParameterException;


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param id  String user is for the server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    void setServerUserId(String userId,
                         String serverName,
                         String id) throws OMAGNotAuthorizedException,
                                           OMAGInvalidParameterException;


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param maxPageSize  max number of elements that can be returned on a request.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    void setMaxPageSize(String  userId,
                        String  serverName,
                        int     maxPageSize) throws OMAGNotAuthorizedException,
                                                    OMAGInvalidParameterException;


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repositories event mapper.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param additionalProperties  property name/value pairs used to configure the connection to the event bus connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists.
     * @throws OMAGInvalidParameterException invalid serverName or serviceMode parameter.
     */
    void setEventBus(String              userId,
                     String              serverName,
                     String              connectorProvider,
                     String              topicURLRoot,
                     Map<String, Object> additionalProperties) throws OMAGNotAuthorizedException,
                                                                      OMAGInvalidParameterException,
                                                                      OMAGConfigurationErrorException;


    /**
     * Enable all access services that are installed into this server.   The configuration properties
     * for each access service can be changed from their default using setAccessServicesConfig operation.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGConfigurationErrorException the event bus has not been configured or
     * @throws OMAGInvalidParameterException invalid serverName parameter.
     */
    void enableAccessServices(String              userId,
                              String              serverName,
                              Map<String, Object> accessServiceOptions) throws OMAGNotAuthorizedException,
                                                                               OMAGInvalidParameterException,
                                                                               OMAGConfigurationErrorException;


    /**
     * Disable the access services.  This removes all configuration for the access services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName  parameter.
     */
    void disableAccessServices(String          userId,
                               String          serverName) throws OMAGNotAuthorizedException,
                                                                  OMAGInvalidParameterException;


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    void setInMemLocalRepository(String userId,
                                 String serverName) throws OMAGNotAuthorizedException,
                                                           OMAGInvalidParameterException;



    /**
     * Set up a graph store as the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGConfigurationErrorException the event bus has not been configured or
     * @throws OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    void setGraphLocalRepository(String userId,
                                 String serverName) throws OMAGNotAuthorizedException,
                                                           OMAGConfigurationErrorException,
                                                           OMAGInvalidParameterException;



    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    void setNoRepositoryMode(String userId,
                             String serverName) throws OMAGNotAuthorizedException,
                                                       OMAGInvalidParameterException;


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param repositoryProxyConnection  connection to the OMRS repository connector.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or organizationName parameter.
     * @throws OMAGConfigurationErrorException the local repository mode has not been set
     */
    void setRepositoryProxyConnection(String     userId,
                                      String     serverName,
                                      Connection repositoryProxyConnection) throws OMAGNotAuthorizedException,
                                                                                   OMAGInvalidParameterException,
                                                                                   OMAGConfigurationErrorException;


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param userId   user that is issuing the request.
     * @param serverName    local server name.
     * @param connectorProvider    connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or organizationName parameter.
     * @throws OMAGConfigurationErrorException the local repository mode has not been set.
     */
    void setRepositoryProxyConnection(String               userId,
                                      String               serverName,
                                      String               connectorProvider,
                                      Map<String, Object>  additionalProperties) throws OMAGNotAuthorizedException,
                                                                                        OMAGInvalidParameterException,
                                                                                        OMAGConfigurationErrorException;


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param localRepositoryEventMapper  connection to the OMRS repository event mapper.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or organizationName parameter.
     * @throws OMAGConfigurationErrorException the local repository mode has not been set
     */
    void setLocalRepositoryEventMapper(String      userId,
                                       String      serverName,
                                       Connection  localRepositoryEventMapper) throws OMAGNotAuthorizedException,
                                                                                      OMAGInvalidParameterException,
                                                                                      OMAGConfigurationErrorException;


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param connectorProvider           Java class name of the connector provider for the OMRS repository event mapper.
     * @param eventSource                 topic name or URL to the native event source.
     * @param additionalProperties        additional properties for the event mapper connection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or organizationName parameter.
     * @throws OMAGConfigurationErrorException the local repository mode has not been set
     */
    void setLocalRepositoryEventMapper(String               userId,
                                       String               serverName,
                                       String               connectorProvider,
                                       String               eventSource,
                                       Map<String, Object>  additionalProperties) throws OMAGNotAuthorizedException,
                                                                                         OMAGInvalidParameterException,
                                                                                         OMAGConfigurationErrorException;


    /**
     * Enable registration of server to an open metadata repository cohort.  This is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param additionalProperties additional properties for the event bus connection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * @throws OMAGConfigurationErrorException the event bus is not set.
     */
    void enableCohortRegistration(String               userId,
                                  String               serverName,
                                  String               cohortName,
                                  Map<String, Object>  additionalProperties) throws OMAGNotAuthorizedException,
                                                                                    OMAGInvalidParameterException,
                                                                                    OMAGConfigurationErrorException;


    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     */
    void disableCohortRegistration(String          userId,
                                   String          serverName,
                                   String          cohortName) throws OMAGNotAuthorizedException,
                                                                      OMAGInvalidParameterException;


    /*
     * =============================================================
     * Configure server - advanced options overriding defaults
     */


    /**
     * Set up the configuration for all of the open metadata access services (OMASs).  This overrides
     * the current values.
     *
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param accessServicesConfig - list of configuration properties for each access service.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    void setAccessServicesConfig(String                    userId,
                                 String                    serverName,
                                 List<AccessServiceConfig> accessServicesConfig) throws OMAGNotAuthorizedException,
                                                                                        OMAGInvalidParameterException;


    /**
     * Set up the configuration for the local repository.  This overrides the current values.
     *
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param localRepositoryConfig - configuration properties for the local repository.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    void setLocalRepositoryConfig(String                userId,
                                  String                serverName,
                                  LocalRepositoryConfig localRepositoryConfig) throws OMAGNotAuthorizedException,
                                                                                      OMAGInvalidParameterException;


    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.
     *
     * @param userId - user that is issuing the request
     * @param serverName - local server name
     * @param enterpriseAccessConfig - enterprise repository services configuration properties.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName or enterpriseAccessConfig parameter.
     */
    void setEnterpriseAccessConfig(String                 userId,
                                   String                 serverName,
                                   EnterpriseAccessConfig enterpriseAccessConfig) throws OMAGNotAuthorizedException,
                                                                                         OMAGInvalidParameterException;


    /**
     * Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a
     * cohort.  Use setCohortMode to delete a cohort.
     *
     * @param userId - user that is issuing the request
     * @param serverName - local server name
     * @param cohortName - name of the cohort
     * @param cohortConfig - configuration for the cohort
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName, cohortName or cohortConfig parameter.
     */
    void setCohortConfig(String        userId,
                         String        serverName,
                         String        cohortName,
                         CohortConfig  cohortConfig) throws OMAGNotAuthorizedException,
                                                            OMAGInvalidParameterException;


    /*
     * =============================================================
     * Query current configuration
     */

    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId - user that is issuing the request
     * @param serverName - local server name
     * @return OMAGServerConfig properties or
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException invalid serverName parameter.
     */
    OMAGServerConfig getCurrentConfiguration(String     userId,
                                             String     serverName) throws OMAGNotAuthorizedException,
                                                                           OMAGInvalidParameterException;


    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId - user that is issuing the request
     * @param serverName - local server name
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException the server name is invalid or
     * @throws OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    void activateWithStoredConfig(String        userId,
                                  String        serverName) throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException;


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId - user that is issuing the request
     * @param configuration - properties used to initialize the services
     * @param serverName - local server name
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException the server name is invalid or
     * @throws OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    void activateWithSuppliedConfig(String             userId,
                                    String             serverName,
                                    OMAGServerConfig   configuration) throws OMAGNotAuthorizedException,
                                                                             OMAGInvalidParameterException,
                                                                             OMAGConfigurationErrorException;


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException the serverName is invalid.
     */
    void deactivateTemporarily(String  userId,
                               String  serverName) throws OMAGNotAuthorizedException,
                                                          OMAGInvalidParameterException;


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * @throws OMAGInvalidParameterException the serverName is invalid.
     */
    void deactivatePermanently(String  userId,
                               String  serverName) throws OMAGNotAuthorizedException,
                                                          OMAGInvalidParameterException;


    /*
     * =============================================================
     * Operational status and control
     */

    /* placeholder */
}
