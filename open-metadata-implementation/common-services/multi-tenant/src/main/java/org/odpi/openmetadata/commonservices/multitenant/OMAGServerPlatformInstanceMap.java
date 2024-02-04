/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.adminservices.registration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.registration.OMAGViewServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerInstanceHistory;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;

import java.util.*;

/**
 * OMAGServerPlatformInstanceMap provides part of the mapping for inbound REST requests to the appropriate
 * service instances for the requested server.  It manages the server name to server instance mapping.
 * The map is maintained in a static, so it is scoped to the class loader.
 * <br><br>
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class OMAGServerPlatformInstanceMap
{
    private static final Map<String, OMAGServerInstance> activeServerInstanceMap   = new HashMap<>();
    private static final Map<String, OMAGServerInstance> inActiveServerInstanceMap = new HashMap<>();


    /**
     * Verify that the calling user is allowed to query the platform services.
     *
     * @param userId calling user
     * @throws UserNotAuthorizedException calling user not allowed to use these services
     */
    public static void validateUserAsInvestigatorForPlatform(String userId) throws UserNotAuthorizedException
    {
        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    public static synchronized void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error);
        }
    }


    /**
     * Create a service description object.
     *
     * @param serviceId component identifier for the service
     * @param serviceName name of the service
     * @param serviceDevelopmentStatus development status
     * @param serviceURLMarker name use in URLs
     * @param serviceDescription short description
     * @param serviceWiki link to more info
     * @param serverType name of server where this service resides
     * @param partnerServiceName service in remote server that this service depends on
     * @param partnerServerType type of server where the partner service resides
     * @return service description object
     */
    private static RegisteredOMAGService getServiceDescription(int                        serviceId,
                                                               String                     serviceName,
                                                               ComponentDevelopmentStatus serviceDevelopmentStatus,
                                                               String                     serviceURLMarker,
                                                               String                     serviceDescription,
                                                               String                     serviceWiki,
                                                               String                     serverType,
                                                               String                     partnerServiceName,
                                                               String                     partnerServerType)
    {
        RegisteredOMAGService service = new RegisteredOMAGService();

        service.setServiceId(serviceId);
        service.setServiceName(serviceName);
        service.setServiceDevelopmentStatus(serviceDevelopmentStatus);
        service.setServiceURLMarker(serviceURLMarker);
        service.setServiceDescription(serviceDescription);
        service.setServiceWiki(serviceWiki);
        service.setServerType(serverType);
        service.setPartnerServiceName(partnerServiceName);
        service.setPartnerServerType(partnerServerType);

        return service;
    }


    /**
     * Return the list of access services that are registered (supported) in this OMAG Server Platform
     * and can be configured in a metadata access point or metadata server.
     *
     * @param userId calling user
     * @return list of access service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getRegisteredAccessServices(String userId) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        List<RegisteredOMAGService> response = new ArrayList<>();

        /*
         * Get the list of Access Services implemented in this server.
         */
        List<AccessServiceRegistrationEntry> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

        /*
         * Set up the available access services.
         */
        if (! accessServiceRegistrationList.isEmpty())
        {
            for (AccessServiceRegistrationEntry registration : accessServiceRegistrationList)
            {
                if (registration != null)
                {
                    if (registration.getAccessServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                    {
                        response.add(getServiceDescription(registration.getAccessServiceCode(),
                                                           registration.getAccessServiceFullName(),
                                                           registration.getAccessServiceDevelopmentStatus(),
                                                           registration.getAccessServiceURLMarker(),
                                                           registration.getAccessServiceDescription(),
                                                           registration.getAccessServiceWiki(),
                                                           ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName(),
                                                           null,
                                                           null));
                    }
                }
            }

        }

        if (response.isEmpty())
        {
            return null;
        }

        return response;
    }


    /**
     * Return the list of engine services that are registered (supported) in this OMAG Server Platform
     * and can be configured in an engine hosting OMAG server.
     *
     * @param userId calling user
     * @return list of engine service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getRegisteredEngineServices(String userId) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        List<RegisteredOMAGService> response = new ArrayList<>();

        /*
         * Get the list of Engine Services implemented in this server.
         */
        List<EngineServiceRegistrationEntry> engineServiceRegistrationList = OMAGEngineServiceRegistration.getEngineServiceRegistrationList();

        /*
         * Set up the available engine services.
         */
        if (! engineServiceRegistrationList.isEmpty())
        {
            for (EngineServiceRegistrationEntry registration : engineServiceRegistrationList)
            {
                if (registration != null)
                {
                    if (registration.getEngineServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                    {
                        response.add(getServiceDescription(registration.getEngineServiceId(),
                                                           registration.getEngineServiceFullName(),
                                                           registration.getEngineServiceDevelopmentStatus(),
                                                           registration.getEngineServiceURLMarker(),
                                                           registration.getEngineServiceDescription(),
                                                           registration.getEngineServiceWiki(),
                                                           ServerTypeClassification.ENGINE_HOST.getServerTypeName(),
                                                           registration.getEngineServicePartnerOMAS(),
                                                           ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName()));
                    }
                }
            }

        }

        if (response.isEmpty())
        {
            return null;
        }

        return response;
    }


    /**
     * Return the list of access services that are registered (supported) in this OMAG Server Platform
     * and can be configured in an integration daemon.
     *
     * @param userId calling user
     * @return list of access service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getRegisteredIntegrationServices(String userId) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        return IntegrationServiceRegistry.getRegisteredIntegrationServices();
    }


    /**
     * Return the list of view services that are registered (supported) in this OMAG Server Platform
     * and can be configured in a view server.
     *
     * @param userId calling user
     * @return list of view service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getRegisteredViewServices(String userId) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        List<RegisteredOMAGService> response = new ArrayList<>();

        /*
         * Get the list of View Services implemented in this server.
         */
        List<ViewServiceRegistrationEntry> viewServiceRegistrationList = OMAGViewServiceRegistration.getViewServiceRegistrationList();

        /*
         * Set up the available view services.
         */
        if (! viewServiceRegistrationList.isEmpty())
        {
            for (ViewServiceRegistrationEntry registration : viewServiceRegistrationList)
            {
                if (registration != null)
                {
                    if (registration.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                    {
                        response.add(getServiceDescription(registration.getViewServiceCode(),
                                                           registration.getViewServiceFullName(),
                                                           registration.getViewServiceDevelopmentStatus(),
                                                           registration.getViewServiceURLMarker(),
                                                           registration.getViewServiceDescription(),
                                                           registration.getViewServiceWiki(),
                                                           ServerTypeClassification.VIEW_SERVER.getServerTypeName(),
                                                           registration.getViewServicePartnerService(),
                                                           ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName()));
                    }
                }
            }

        }

        if (response.isEmpty())
        {
            return null;
        }

        return response;
    }


    /**
     * Return the list of governance services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of a governance server.
     *
     * @param userId calling user
     * @return list of governance service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getRegisteredGovernanceServices(String userId) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        List<RegisteredOMAGService> response = new ArrayList<>();


        /*
         * Set up the available governance services.
         */
        for (GovernanceServicesDescription registration : GovernanceServicesDescription.values())
        {
            if (registration != null)
            {
                response.add(getServiceDescription(registration.getServiceCode(),
                                                   registration.getServiceName(),
                                                   registration.getServiceDevelopmentStatus(),
                                                   registration.getServiceURLMarker(),
                                                   registration.getServiceDescription(),
                                                   registration.getServiceWiki(),
                                                   registration.getServerType(),
                                                   registration.getPartnerServiceName(),
                                                   registration.getPartnerServerType()));
            }
        }

        if (response.isEmpty())
        {
            return null;
        }

        return response;
    }


    /**
     * Return the list of common services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of a server.
     *
     * @param userId calling user
     * @return list of service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getRegisteredCommonServices(String userId) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        List<RegisteredOMAGService> response = new ArrayList<>();

        /*
         * Set up the available governance services.
         */
        for (CommonServicesDescription registration : CommonServicesDescription.values())
        {
            if (registration != null)
            {
                response.add(getServiceDescription(registration.getServiceCode(),
                                                   registration.getServiceName(),
                                                   registration.getServiceDevelopmentStatus(),
                                                   registration.getServiceURLMarker(),
                                                   registration.getServiceDescription(),
                                                   registration.getServiceWiki(),
                                                   registration.getServerType(),
                                                   registration.getPartnerServiceName(),
                                                   registration.getPartnerServerType()));
            }
        }

        if (response.isEmpty())
        {
            return null;
        }

        return response;
    }


    /**
     * Return the list of  services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of a server.
     *
     * @param userId calling user
     * @return list of service descriptions
     * @throws UserNotAuthorizedException user not authorized
     */
    public List<RegisteredOMAGService> getAllRegisteredServices(String userId) throws UserNotAuthorizedException
    {
        List<RegisteredOMAGService> response = new ArrayList<>();

        List<RegisteredOMAGService> services = getRegisteredCommonServices(userId);

        if ((services != null) && (! services.isEmpty()))
        {
            response.addAll(services);
        }

        services = getRegisteredAccessServices(userId);

        if ((services != null) && (! services.isEmpty()))
        {
            response.addAll(services);
        }

        services = getRegisteredIntegrationServices(userId);

        if ((services != null) && (! services.isEmpty()))
        {
            response.addAll(services);
        }

        services = getRegisteredViewServices(userId);

        if ((services != null) && (! services.isEmpty()))
        {
            response.addAll(services);
        }

        services = getRegisteredGovernanceServices(userId);

        if ((services != null) && (! services.isEmpty()))
        {
            response.addAll(services);
        }

        return response;
    }


    /**
     * Return an active server instance object for the requested service.  The server instance
     * may be new, already active, or known but inactive.
     *
     * @param serverName name of the server
     * @return active OMAGServerInstance object
     */
    private static synchronized OMAGServerInstance getActiveServerInstance(String serverName)
    {
        /*
         * Is this a server that is currently running?
         */
        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance == null)
        {
            /*
             * Is this a known server that is currently inactive?
             */
            serverInstance = inActiveServerInstanceMap.get(serverName);

            if (serverInstance == null)
            {
                /*
                 * New server for this platform
                 */
                serverInstance = new OMAGServerInstance(serverName);
                activeServerInstanceMap.put(serverName, serverInstance);
            }
            else
            {
                /*
                 * Move the inactive server to active
                 */
                activeServerInstanceMap.put(serverName, serverInstance);
                inActiveServerInstanceMap.remove(serverName);
            }
        }

        return serverInstance;
    }


    /**
     * Add a new service instance to the server map.
     *
     * @param serverName name of the server
     * @param serverType type of server (or null if the registering service does not know)
     * @param serviceName name of the service running on the server
     * @param instance instance object
     */
    private static synchronized void  setInstanceForPlatform(String                    serverName,
                                                             String                    serverType,
                                                             String                    serviceName,
                                                             OMAGServerServiceInstance instance)
    {
        OMAGServerInstance  serverInstance = getActiveServerInstance(serverName);

        serverInstance.registerService(serviceName, instance);
        if (serverType != null)
        {
            serverInstance.setServerType(serverType);
        }
    }


    /**
     * Add a new server security connector to the server map.
     *
     * @param localServerUserId server's userId
     * @param serverName name of the server
     * @param auditLog logging destination
     * @param connection connection for the server's security validator
     * @return OpenMetadataServerSecurityVerifier object
     * @throws InvalidParameterException the connector is not valid.
     */
    private static synchronized OpenMetadataServerSecurityVerifier setServerActiveWithSecurity(String       localServerUserId,
                                                                                               String       serverName,
                                                                                               AuditLog     auditLog,
                                                                                               Connection   connection) throws InvalidParameterException
    {
        OMAGServerInstance  serverInstance = getActiveServerInstance(serverName);

        serverInstance.initialize();
        return serverInstance.registerSecurityValidator(localServerUserId, auditLog, connection);
    }


    /**
     * Return the type of server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @param serviceOperationName calling method
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws InvalidParameterException the server name is not known
     */
    private static synchronized String getServerInstanceType(String  userId,
                                                             String  serverName,
                                                             String  serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        OMAGServerInstance serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance != null)
        {
            return serverInstance.getServerType();
        }
        else
        {
            handleBadServerName(userId, serverName, serviceOperationName);

            /*
             * Note, this return is unreachable because handleBadServerName always throws an exception.
             */
            return null;
        }
    }


    /**
     * Return the type of server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @param serviceOperationName calling method
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws InvalidParameterException the server name is not known
     */
    private static synchronized ServerStatus getServerInstanceStatus(String  userId,
                                                                     String  serverName,
                                                                     String  serviceOperationName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        OMAGServerInstance serverInstance = activeServerInstanceMap.get(serverName);
        boolean            isActive = true;

        if (serverInstance == null)
        {
            serverInstance = inActiveServerInstanceMap.get(serverName);
            isActive = false;
        }

        if (serverInstance != null)
        {
            ServerStatus serverStatus = new ServerStatus();

            serverStatus.setServerName(serverName);
            serverStatus.setServerType(serverInstance.getServerType());
            serverStatus.setIsActive(isActive);
            serverStatus.setServerStartTime(serverInstance.getServerStartTime());
            serverStatus.setServerEndTime(serverInstance.getServerEndTime());
            serverStatus.setServerHistory(serverInstance.getServerHistory());

            return serverStatus;
        }
        else
        {
            handleBadServerName(userId, serverName, serviceOperationName);

            /*
             * Note, this return is unreachable because handleBadServerName always throws an exception.
             */
            return null;
        }
    }


    /**
     * Return whether a particular service is registered with this platform.
     * This is used by the admin services when no instance is not an error.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized boolean isServerInstanceActive(String  userId,
                                                               String  serverName) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        return (activeServerInstanceMap.get(serverName) != null);
    }



    /**
     * Return whether a particular service is registered with this platform.
     * This is used by the admin services when no instance is not an error.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized boolean isServerInstanceKnown(String  userId,
                                                              String  serverName) throws UserNotAuthorizedException
    {
        validateUserAsInvestigatorForPlatform(userId);

        return ((activeServerInstanceMap.get(serverName) != null) ||
                (inActiveServerInstanceMap.get(serverName) != null));
    }


    /**
     * Return the instance of this service for this server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     * @param serviceOperationName calling method
     *
     * @return OMAGServerServiceInstance object
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not know - indicating a logic error
     */
    private static synchronized OMAGServerServiceInstance getInstanceForPlatform(String  userId,
                                                                                 String  serverName,
                                                                                 String  serviceName,
                                                                                 String  serviceOperationName) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance != null)
        {
            OpenMetadataServerSecurityVerifier serverSecurityVerifier = serverInstance.getSecurityVerifier();

            try
            {
                serverSecurityVerifier.validateUserForServer(userId);
                serverSecurityVerifier.validateUserForService(userId, serviceName);
                serverSecurityVerifier.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }

            return serverInstance.getRegisteredService(userId, serviceName, serviceOperationName);
        }
        else
        {
            handleBadServerName(userId, serverName, serviceOperationName);

            /*
             * Note, this return is unreachable because handleBadServerName always throws an exception.
             */
            return null;
        }
    }


    /**
     * Return the list of OMAG Servers running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized List<String> getActiveServerListForPlatform(String userId) throws UserNotAuthorizedException
    {
        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error);
        }

        Set<String>  activeServerSet = activeServerInstanceMap.keySet();

        if (activeServerSet.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(activeServerSet);
        }
    }


    /**
     * Return the list of OMAG Servers running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized List<String> getKnownServerListForPlatform(String userId) throws UserNotAuthorizedException
    {
        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error);
        }

        List<String> knownServerList = new ArrayList<>(activeServerInstanceMap.keySet());
        knownServerList.addAll(inActiveServerInstanceMap.keySet());

        if (knownServerList.isEmpty())
        {
            return null;
        }
        else
        {
            return knownServerList;
        }
    }


    /**
     * Check that the user is allowed to query the active services.
     *
     * @param userId calling user
     * @param serverInstance instance for the server
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized void validateUserAsServerInvestigator(String              userId,
                                                                      OMAGServerInstance  serverInstance) throws UserNotAuthorizedException
    {
        if (serverInstance != null)
        {
            OpenMetadataServerSecurityVerifier serverSecurityVerifier = serverInstance.getSecurityVerifier();

            try
            {
                serverSecurityVerifier.validateUserForServer(userId);
                serverSecurityVerifier.validateUserAsServerInvestigator(userId);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }
    }


    /**
     * Return the time this server instance last started.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @return start time
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized  Date getServerStartTimeFromPlatform(String  userId,
                                                                     String  serverName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException
    {
        final String  methodName = "getServerStartTimeFromPlatform";

        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance == null)
        {
            serverInstance = inActiveServerInstanceMap.get(serverName);
        }

        if (serverInstance != null)
        {
            validateUserAsServerInvestigator(userId, serverInstance);

            return serverInstance.getServerStartTime();
        }
        else
        {
            handleBadServerName(userId, serverName, methodName);
        }

        return null;
    }


    /**
     * Return the time this server instance last ended (or null if it is still running).
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @return end time or null
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized  Date getServerEndTimeFromPlatform(String  userId,
                                                                   String  serverName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException
    {
        final String  methodName = "getServerEndTimeFromPlatform";

        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance == null)
        {
            serverInstance = inActiveServerInstanceMap.get(serverName);
        }

        if (serverInstance != null)
        {
            validateUserAsServerInvestigator(userId, serverInstance);

            return serverInstance.getServerEndTime();
        }
        else
        {
            handleBadServerName(userId, serverName, methodName);
        }

        return null;
    }


    /**
     * Return the time this server instance last started.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @return start time
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized  List<OMAGServerInstanceHistory> getServerHistoryFromPlatform(String  userId,
                                                                                              String  serverName) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException
    {
        final String  methodName = "getServerHistoryFromPlatform";

        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance == null)
        {
            serverInstance = inActiveServerInstanceMap.get(serverName);
        }

        if (serverInstance != null)
        {
            validateUserAsServerInvestigator(userId, serverInstance);

            return serverInstance.getServerHistory();
        }
        else
        {
            handleBadServerName(userId, serverName, methodName);
        }

        return null;
    }

    /**
     * Return the list of services running in an OMAG Server that is running on this OMAG Server Platform.
     *
     * @param userId calling user
     * @param serverName name of the server
     * @return list on OMAG Services or null if the server is not
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized List<String> getActiveServicesForServerOnPlatform(String userId,
                                                                                  String serverName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException
    {
        final String  methodName = "getActiveServicesForServerOnPlatform";

        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance != null)
        {
            validateUserAsServerInvestigator(userId, serverInstance);

            return serverInstance.getConfiguredServices();
        }
        else /* server is not active */
        {
            serverInstance = inActiveServerInstanceMap.get(serverName);

            if (serverInstance != null)
            {
                validateUserAsServerInvestigator(userId, serverInstance);
            }
            else
            {
                handleBadServerName(userId, serverName, methodName);
            }
        }

        return null;
    }


    /**
     * Remove the service instance for this server.
     *
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     */
    private static synchronized void removeInstanceForPlatform(String   serverName,
                                                               String   serviceName)
    {
        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance != null)
        {
             serverInstance.unRegisterService(serviceName);
        }
    }


    /**
     * Shutdown the server instance.  This is called once all services have been shutdown.
     *
     * @param userId calling user
     * @param serverName name of the unknown server
     * @param methodName calling method
     * @throws InvalidParameterException server name is not known
     * @throws PropertyServerException server still has active services - logic error
     */
    private static synchronized void setServerInActive(String   userId,
                                                       String   serverName,
                                                       String   methodName) throws InvalidParameterException,
                                                                                   PropertyServerException
    {
        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance == null)
        {
            handleBadServerName(userId, serverName, methodName);
        }
        else
        {
            try
            {
                serverInstance.shutdown(methodName);
                inActiveServerInstanceMap.put(serverName, serverInstance);
                activeServerInstanceMap.remove(serverName);
            }
            catch (Exception error)
            {
                inActiveServerInstanceMap.put(serverName, serverInstance);
                activeServerInstanceMap.remove(serverName);
                throw error;
            }
        }
    }


    /**
     * Return the security verifier for the server.
     *
     * @param userId calling user
     * @param serverName name of the server
     *
     * @return OpenMetadataServerSecurityVerifier object - never null
     * @throws InvalidParameterException the server name is not known
     */
    private static synchronized OpenMetadataServerSecurityVerifier getServerSecurityVerifierForPlatform(String    userId,
                                                                                                        String    serverName) throws InvalidParameterException
    {
        final String  methodName = "getServerSecurityVerifierForPlatform";

        OMAGServerInstance  serverInstance = activeServerInstanceMap.get(serverName);

        if (serverInstance != null)
        {
            return serverInstance.getSecurityVerifier();
        }

        /*
         * Null will never be returned since handlerBadServerName always throws an exception (but the
         * compiler is not sure :)).
         */
        handleBadServerName(userId, serverName, methodName);
        return null;
    }


    /**
     * Throw a standard exception for when the server name is not known.
     *
     * @param userId calling user
     * @param serverName name of the unknown server
     * @param methodName calling method
     * @throws InvalidParameterException requested exception
     */
    private static void handleBadServerName(String    userId,
                                            String    serverName,
                                            String    methodName) throws InvalidParameterException
    {
        Map<String, Object>         debugProperties = new HashMap<>();

        final String  serverNameProperty = "serverName";
        debugProperties.put(serverNameProperty, serverName);

        throw new InvalidParameterException(OMAGServerInstanceErrorCode.SERVER_NOT_AVAILABLE.getMessageDefinition(serverName, userId),
                                            OMAGServerPlatformInstanceMap.class.getName(),
                                            methodName,
                                            serverNameProperty,
                                            debugProperties);
    }



    /**
     * Constructor for instances - used by service instances to get access to the platform map
     */
    public OMAGServerPlatformInstanceMap()
    {
    }


    /**
     * Return the type of server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @param serviceOperationName name of the calling method/request
     *
     * @return string name
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public String getServerType(String  userId,
                                String  serverName,
                                String  serviceOperationName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getServerInstanceType(userId, serverName, serviceOperationName);
    }


    /**
     * Return the type of server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @param serviceOperationName name of the calling method/request
     *
     * @return server status
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public ServerStatus getServerStatus(String  userId,
                                        String  serverName,
                                        String  serviceOperationName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getServerInstanceStatus(userId, serverName, serviceOperationName);
    }


    /**
     * Return whether a particular server is active (running) in the platform.
     * This is used by the admin services when no instance is not an error.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public boolean isServerActive(String  userId,
                                  String  serverName) throws UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.isServerInstanceActive(userId, serverName);
    }


    /**
     * Return whether a particular server is registered with the platform.
     * This is used by the admin services when no instance is not an error.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public boolean isServerKnown(String  userId,
                                 String  serverName) throws UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.isServerInstanceKnown(userId, serverName);
    }


    /**
     * Return the time this server instance last started.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @return start time
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public  Date getServerStartTime(String  userId,
                                    String  serverName) throws InvalidParameterException,
                                                               UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getServerStartTimeFromPlatform(userId, serverName);
    }


    /**
     * Return the time this server instance last ended (or null if it is still running).
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @return end time or null
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public  Date getServerEndTime(String  userId,
                                  String  serverName) throws InvalidParameterException,
                                                             UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getServerEndTimeFromPlatform(userId, serverName);
    }


    /**
     * Return the time this server instance last started.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @return start time
     * @throws InvalidParameterException the serverName is not known.
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public  List<OMAGServerInstanceHistory> getServerHistory(String  userId,
                                                             String  serverName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getServerHistoryFromPlatform(userId, serverName);
    }


    /**
     * Add a new service instance to the server map.
     *
     * @param serverName name of the server
     * @param serverType type of server (or null if the registering service does not know)
     * @param serviceName name of the service running on the server
     * @param instance instance object
     */
    void  addServiceInstanceToPlatform(String                    serverName,
                                       String                    serverType,
                                       String                    serviceName,
                                       OMAGServerServiceInstance instance)
    {
        OMAGServerPlatformInstanceMap.setInstanceForPlatform(serverName, serverType, serviceName, instance);
    }


    /**
     * Start up the server with the requested security.
     *
     * @param localServerUserId userId for local server
     * @param serverName name of the server
     * @param auditLog logging destination
     * @param connection connection properties for open metadata security connector for server (can be null for no security)
     * @return OpenMetadataServerSecurityVerifier object
     * @throws InvalidParameterException the connection is invalid
     */
    public OpenMetadataServerSecurityVerifier startUpServerInstance(String       localServerUserId,
                                                                    String       serverName,
                                                                    AuditLog     auditLog,
                                                                    Connection   connection) throws InvalidParameterException
    {
        return OMAGServerPlatformInstanceMap.setServerActiveWithSecurity(localServerUserId, serverName, auditLog, connection);
    }


    /**
     * Shutdown the server instance.  This is called once all services have been shutdown.
     *
     * @param userId calling user
     * @param serverName name of the unknown server
     * @param methodName calling method
     * @throws InvalidParameterException server name is not known
     * @throws PropertyServerException server still has active services - logic error
     */
    public void shutdownServerInstance(String userId,
                                       String serverName,
                                       String methodName) throws InvalidParameterException,
                                                                 PropertyServerException
    {
        OMAGServerPlatformInstanceMap.setServerInActive(userId, serverName, methodName);
    }


    /**
     * Return the instance for this server.
     *
     * @param userId calling user
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     * @param serviceOperationName  name of the calling method (relates to REST operation call)
     * @return OMAGServerServiceInstance object
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    OMAGServerServiceInstance getServiceInstance(String    userId,
                                                 String    serverName,
                                                 String    serviceName,
                                                 String    serviceOperationName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return OMAGServerPlatformInstanceMap.getInstanceForPlatform(userId, serverName, serviceName, serviceOperationName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     */
    void removeServiceInstanceFromPlatform(String   serverName,
                                           String   serviceName)
    {
        OMAGServerPlatformInstanceMap.removeInstanceForPlatform(serverName, serviceName);
    }


    /**
     * Return the security verifier for the server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return OpenMetadataServerSecurityVerifier object - never null
     * @throws InvalidParameterException the server name is not known
     */
    OpenMetadataServerSecurityVerifier getServerSecurityVerifier(String    userId,
                                                                 String    serverName) throws InvalidParameterException
    {
        return OMAGServerPlatformInstanceMap.getServerSecurityVerifierForPlatform(userId, serverName);
    }


    /**
     * Return the list of OMAG Servers running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public List<String>   getActiveServerList(String userId) throws UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getActiveServerListForPlatform(userId);
    }


    /**
     * Return the list of OMAG Servers that have run or are running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public List<String>   getKnownServerList(String userId) throws UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getKnownServerListForPlatform(userId);
    }


    /**
     * Return the list of services running in an OMAG Server that is running on this OMAG Server Platform.
     *
     * @param userId calling user
     * @param serverName name of the server
     * @return list on OMAG Services
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public List<String> getActiveServicesForServer(String  userId,
                                                   String  serverName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getActiveServicesForServerOnPlatform(userId, serverName);
    }
}
