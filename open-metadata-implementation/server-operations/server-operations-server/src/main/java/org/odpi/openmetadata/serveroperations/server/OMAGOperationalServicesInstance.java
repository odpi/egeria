/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.server;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.registration.ViewServerGenericServiceAdmin;
import org.odpi.openmetadata.adminservices.registration.ViewServiceAdmin;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.server.EngineHostOperationalServices;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonOperationalServices;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGOperationalServicesInstance provides the references to the active services for an instance of an OMAG Server.
 */

public class OMAGOperationalServicesInstance extends OMAGServerServiceInstance
{
    private       ServerActiveStatus              serverActiveStatus = ServerActiveStatus.INACTIVE;
    private final Map<String, ServerActiveStatus> serviceStatusMap   = new HashMap<>();
    private final ServerTypeClassification        serverTypeClassification;

    private OMAGServerConfig                     operationalConfiguration                     = null;
    private OMRSOperationalServices              operationalRepositoryServices                = null;
    private List<AccessServiceAdmin>             operationalAccessServiceAdminList            = new ArrayList<>();
    private List<ViewServiceAdmin>               operationalViewServiceAdminList              = new ArrayList<>();
    private List<ViewServerGenericServiceAdmin>  operationalViewServerGenericServiceAdminList = new ArrayList<>();
    private ConformanceSuiteOperationalServices  operationalConformanceSuiteServices          = null;
    private EngineHostOperationalServices        operationalEngineHost                        = null;
    private IntegrationDaemonOperationalServices operationalIntegrationDaemon                 = null;
    private AuditLog                             auditLog                                     = null;


    /**
     * Default constructor
     *
     * @param serverName name of the new server
     * @param serviceName name of the new service instance
     * @param maxPageSize maximum number of results that can be returned
     */
    OMAGOperationalServicesInstance(String                   serverName,
                                    ServerTypeClassification serverTypeClassification,
                                    String                   serviceName,
                                    int                      maxPageSize)
    {
        super(serverName, serverTypeClassification.getServerTypeName(), serviceName, maxPageSize);

        this.serverTypeClassification = serverTypeClassification;
    }


    /**
     * Set the status of the server.
     *
     * @param serverActiveStatus new status
     */
    public synchronized void setServerActiveStatus(ServerActiveStatus serverActiveStatus)
    {
        this.serverActiveStatus = serverActiveStatus;
    }


    /**
     * Set the status of a particular service.
     *
     * @param serviceName name of service
     * @param activeStatus new status
     */
    public synchronized void setServerServiceActiveStatus(String serviceName, ServerActiveStatus activeStatus)
    {
        serviceStatusMap.put(serviceName, activeStatus);
    }


    /**
     * Return a summary of the status of this server and the services within it.
     *
     * @return server status
     */
    public synchronized ServerServicesStatus getServerStatus()
    {
        ServerServicesStatus serverServicesStatus = new ServerServicesStatus();

        serverServicesStatus.setServerName(serverName);
        serverServicesStatus.setServerType(serverTypeClassification.getServerTypeName());
        serverServicesStatus.setServerActiveStatus(serverActiveStatus);

        List<OMAGServerServiceStatus> serviceStatuses = new ArrayList<>();

        for (String serviceName : serviceStatusMap.keySet())
        {
            OMAGServerServiceStatus serviceStatus = new OMAGServerServiceStatus();

            serviceStatus.setServiceName(serviceName);
            serviceStatus.setServiceStatus(serviceStatusMap.get(serviceName));

            serviceStatuses.add(serviceStatus);
        }

        if (operationalEngineHost != null)
        {
            List<OMAGServerServiceStatus> engineServices = operationalEngineHost.getServiceStatuses();

            if (engineServices != null)
            {
                serviceStatuses.addAll(engineServices);
            }
        }

        serverServicesStatus.setServices(serviceStatuses);

        return serverServicesStatus;
    }


    /**
     * Return the list of services that are active in the server.
     *
     * @return list of services names
     */
    public synchronized  List<String> getActiveServiceListForServer()
    {
        if (! serviceStatusMap.isEmpty())
        {
            List<String> activeServices = new ArrayList<>();

            for (String serviceName : serviceStatusMap.keySet())
            {
                if (serviceStatusMap.get(serviceName) == ServerActiveStatus.RUNNING)
                {
                    activeServices.add(serviceName);
                }
            }

            return activeServices;
        }

        return null;
    }


    /**
     * Return the configuration document that was used to start the current running server.
     * This value is null if the server has not been started.
     *
     * @return OMAGServerConfig object
     */
    OMAGServerConfig getOperationalConfiguration()
    {
        return operationalConfiguration;
    }


    /**
     * Set up the configuration document that was used to start the current running server.
     *
     * @param operationalConfiguration OMAGServerConfig object
     */
    void setOperationalConfiguration(OMAGServerConfig operationalConfiguration)
    {
        this.operationalConfiguration = operationalConfiguration;
    }


    /**
     * Return the running instance of the Open Metadata Repository Services (OMRS).
     *
     * @return OMRSOperationalServices object
     */
    OMRSOperationalServices getOperationalRepositoryServices()
    {
        return operationalRepositoryServices;
    }


    /**
     * Set up the running instance of the Open Metadata Repository Services (OMRS).
     *
     * @param operationalRepositoryServices OMRSOperationalServices object
     */
    void setOperationalRepositoryServices(OMRSOperationalServices operationalRepositoryServices)
    {
        this.operationalRepositoryServices = operationalRepositoryServices;
    }


    /**
     * Return the list of references to the admin object for each active Open Metadata Access Service (OMAS).
     *
     * @return list of AccessServiceAdmin objects
     */
    List<AccessServiceAdmin> getOperationalAccessServiceAdminList()
    {
        return operationalAccessServiceAdminList;
    }


    /**
     * Set up the list of references to the admin object for each active Open Metadata Access Service (OMAS).
     *
     * @param operationalAccessServiceAdminList list of AccessServiceAdmin objects
     */
    void setOperationalAccessServiceAdminList(List<AccessServiceAdmin> operationalAccessServiceAdminList)
    {
        this.operationalAccessServiceAdminList = operationalAccessServiceAdminList;
    }


    /**
     * Return the list of references to the admin object for each active Open Metadata View Service (OMVS).
     *
     * @return list of ViewServiceAdmin objects
     */
    List<ViewServiceAdmin> getOperationalViewServiceAdminList()
    {
        return operationalViewServiceAdminList;
    }


    /**
     * Set up the list of references to the admin object for each active Open Metadata View Service (OMVS).
     *
     * @param operationalViewServiceAdminList list of ViewServiceAdmin objects
     */
    void setOperationalViewServiceAdminList(List<ViewServiceAdmin> operationalViewServiceAdminList)
    {
        this.operationalViewServiceAdminList = operationalViewServiceAdminList;
    }


    /**
     * Return the list of references to the admin object for each active Open Metadata View Service (OMVS).
     *
     * @return list of ViewServiceAdmin objects
     */
    public List<ViewServerGenericServiceAdmin> getOperationalViewServerGenericServiceAdminList()
    {
        return operationalViewServerGenericServiceAdminList;
    }


    /**
     * Set up the list of references to the admin object for each active Open Metadata View Service (OMVS).
     *
     * @param operationalViewServerGenericServiceAdminList list of ViewServiceAdmin objects
     */
    public void setOperationalViewServerGenericServiceAdminList(List<ViewServerGenericServiceAdmin> operationalViewServerGenericServiceAdminList)
    {
        this.operationalViewServerGenericServiceAdminList = operationalViewServerGenericServiceAdminList;
    }


    /**
     * Return the running instance of the conformance suite operational services for this server.
     *
     * @return ConformanceSuiteOperationalServices object
     */
    ConformanceSuiteOperationalServices getOperationalConformanceSuiteServices()
    {
        return operationalConformanceSuiteServices;
    }


    /**
     * Set up the running instance of the conformance suite operational services for this server.
     *
     * @param operationalConformanceSuiteServices ConformanceSuiteOperationalServices object
     */
    void setOperationalConformanceSuiteServices(ConformanceSuiteOperationalServices operationalConformanceSuiteServices)
    {
        this.operationalConformanceSuiteServices = operationalConformanceSuiteServices;
    }
    

    /**
     * Return the running instance of the integration daemon.
     *
     * @return IntegrationDaemonOperationalServices object
     */
    EngineHostOperationalServices getOperationalEngineHost()
    {
        return operationalEngineHost;
    }


    /**
     * Set up the running instance of the integration daemon.
     *
     * @param operationalEngineHost IntegrationDaemonOperationalServices object
     */
    void setOperationalEngineHost(EngineHostOperationalServices operationalEngineHost)
    {
        this.operationalEngineHost = operationalEngineHost;
    }


    /**
     * Return the running instance of the integration daemon.
     *
     * @return IntegrationDaemonOperationalServices object
     */
    IntegrationDaemonOperationalServices getOperationalIntegrationDaemon()
    {
        return operationalIntegrationDaemon;
    }


    /**
     * Set up the running instance of the integration daemon.
     *
     * @param operationalIntegrationDaemon IntegrationDaemonOperationalServices object
     */
    void setOperationalIntegrationDaemon(IntegrationDaemonOperationalServices operationalIntegrationDaemon)
    {
        this.operationalIntegrationDaemon = operationalIntegrationDaemon;
    }


    /**
     * Retrieve the operational admin services' audit log.
     *
     * @return logging destination
     */
    AuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Set up the operational admin services' audit log.
     *
     * @param auditLog logging destination
     */
    void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }
}
