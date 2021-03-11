/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.admin.OCFMetadataOperationalServices;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.governanceservers.dataengineproxy.admin.DataEngineProxyOperationalServices;
import org.odpi.openmetadata.governanceservers.enginehostservices.server.EngineHostOperationalServices;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageServerOperationalServices;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.ArrayList;
import java.util.List;

/**
 * OMAGOperationalServicesInstance provides the references to the active services for an instance of an OMAG Server.
 */

class OMAGOperationalServicesInstance extends OMAGServerServiceInstance
{
    private ServerTypeClassification             serverTypeClassification            = null;
    private OMAGServerConfig                     operationalConfiguration            = null;
    private OMRSOperationalServices              operationalRepositoryServices       = null;
    private OCFMetadataOperationalServices       operationalOCFMetadataServices      = null;
    private List<AccessServiceAdmin>             operationalAccessServiceAdminList   = new ArrayList<>();
    private List<ViewServiceAdmin>               operationalViewServiceAdminList     = new ArrayList<>();
    private ConformanceSuiteOperationalServices  operationalConformanceSuiteServices = null;
    private EngineHostOperationalServices        operationalEngineHost               = null;
    private IntegrationDaemonOperationalServices operationalIntegrationDaemon        = null;
    private OpenLineageServerOperationalServices openLineageOperationalServices      = null;
    private DataEngineProxyOperationalServices   operationalDataEngineProxyServices  = null;
    private OMRSAuditLog                         auditLog                            = null;


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
     * Return the configuration document that was used to start the current running server.
     * This value is null if the server has not been started.
     *
     * @return OMAGServerConfig object
     */
    OMAGServerConfig getOperationalConfiguration() {
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
     * Return the running instance of the Open Connector Framework (OCF) metadata services.
     *
     * @return OCFMetadataOperationalServices object
     */
    OCFMetadataOperationalServices getOperationalOCFMetadataServices()
    {
        return operationalOCFMetadataServices;
    }


    /**
     * Set up the running instance of the Open Connector Framework (OCF) metadata services.
     *
     * @param operationalOCFMetadataServices OCFMetadataOperationalServices object
     */
    void setOperationalOCFMetadataServices(OCFMetadataOperationalServices operationalOCFMetadataServices)
    {
        this.operationalOCFMetadataServices = operationalOCFMetadataServices;
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
    EngineHostOperationalServices getOperationalEngineHost() {
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
    IntegrationDaemonOperationalServices getOperationalIntegrationDaemon() {
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
     * Set up the running instance of the Open Lineage Services server.
     *
     * @param openLineageOperationalServices OpenLineageOperationalServices object
     */
    void setOpenLineageOperationalServices(OpenLineageServerOperationalServices openLineageOperationalServices)
    {
        this.openLineageOperationalServices = openLineageOperationalServices;
    }


    /**
     * Return the running instance of the Open Lineage Services server.
     *
     * @return DiscoveryServerOperationalServices object
     */
    OpenLineageServerOperationalServices getOpenLineageOperationalServices() {
        return openLineageOperationalServices;
    }


    /**
     * Return the running instance of Data Engine proxy
     *
     * @return DataEngineProxyOperationalServices
     */
    DataEngineProxyOperationalServices getOperationalDataEngineProxyServices()
    {
        return operationalDataEngineProxyServices;
    }


    /**
     * Set up the running instance of Data Engine proxy
     *
     * @param operationalDataEngineProxyServices DataEngineProxyOperationalServices
     */
    void setOperationalDataEngineProxyServices(DataEngineProxyOperationalServices operationalDataEngineProxyServices)
    {
        this.operationalDataEngineProxyServices = operationalDataEngineProxyServices;
    }


    /**
     * Retrieve the operational admin services' audit log.
     *
     * @return logging destination
     */
    OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Set up the operational admin services' audit log.
     *
     * @param auditLog logging destination
     */
    void setAuditLog(OMRSAuditLog auditLog)
    {
        this.auditLog = auditLog;
    }
}