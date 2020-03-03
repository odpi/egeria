/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.admin.OCFMetadataOperationalServices;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.dataplatformservices.admin.DataPlatformOperationalServices;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.server.DiscoveryServerOperationalServices;
import org.odpi.openmetadata.governanceservers.dataengineproxy.admin.DataEngineProxyOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageServerOperationalServices;
import org.odpi.openmetadata.governanceservers.stewardshipservices.admin.StewardshipOperationalServices;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.securityofficerservices.registration.SecurityOfficerOperationalServices;
import org.odpi.openmetadata.securitysyncservices.registration.SecuritySyncOperationalServices;
import org.odpi.openmetadata.governanceservers.virtualizationservices.admin.VirtualizationOperationalServices;

import java.util.ArrayList;
import java.util.List;

/**
 * OMAGOperationalServicesInstance provides the references to the active services for an instance of an OMAG Server.
 */

public class OMAGOperationalServicesInstance extends OMAGServerServiceInstance
{
    private OMAGServerConfig                     operationalConfiguration            = null;
    private OMRSOperationalServices              operationalRepositoryServices       = null;
    private OCFMetadataOperationalServices       operationalOCFMetadataServices      = null;
    private List<AccessServiceAdmin>             operationalAccessServiceAdminList   = new ArrayList<>();
    private List<ViewServiceAdmin>               operationalViewServiceAdminList     = new ArrayList<>();
    private ConformanceSuiteOperationalServices  operationalConformanceSuiteServices = null;
    private DiscoveryServerOperationalServices   operationalDiscoveryServer          = null;
    private OpenLineageServerOperationalServices openLineageOperationalServices      = null;
    private StewardshipOperationalServices       operationalStewardshipServices      = null;
    private SecuritySyncOperationalServices      operationalSecuritySyncServices     = null;
    private SecurityOfficerOperationalServices   operationalSecurityOfficerService   = null;
    private VirtualizationOperationalServices    operationalVirtualizationServices   = null;
    private DataEngineProxyOperationalServices   operationalDataEngineProxyServices  = null;
    private DataPlatformOperationalServices      operationalDataPlatformServices     = null;
    private OMRSAuditLog                         auditLog                            = null;


    /**
     * Default constructor
     *
     * @param serverName name of the new server
     * @param serviceName name of the new service instance
     * @param maxPageSize maximum number of results that can be returned
     */
    public OMAGOperationalServicesInstance(String   serverName,
                                           String   serviceName,
                                           int      maxPageSize)
    {
        super(serverName, serviceName, maxPageSize);
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
    OMRSOperationalServices getOperationalRepositoryServices() {
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
    public OCFMetadataOperationalServices getOperationalOCFMetadataServices()
    {
        return operationalOCFMetadataServices;
    }


    /**
     * Set up the running instance of the Open Connector Framework (OCF) metadata services.
     *
     * @param operationalOCFMetadataServices OCFMetadataOperationalServices object
     */
    public void setOperationalOCFMetadataServices(OCFMetadataOperationalServices operationalOCFMetadataServices)
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
     * Return the running instance of the discovery engine.
     *
     * @return DiscoveryServerOperationalServices object
     */
    DiscoveryServerOperationalServices getOperationalDiscoveryServer() {
        return operationalDiscoveryServer;
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
     * Set up the running instance of the discovery engine.
     *
     * @param operationalDiscoveryServer DiscoveryServerOperationalServices object
     */
    void setOperationalDiscoveryServer(DiscoveryServerOperationalServices operationalDiscoveryServer)
    {
        this.operationalDiscoveryServer = operationalDiscoveryServer;
    }


    /**
     * Return the running instance of the stewardship services.
     *
     * @return StewardshipOperationalServices object
     */
    StewardshipOperationalServices getOperationalStewardshipServices() {
        return operationalStewardshipServices;
    }


    /**
     * Set up running instance of the stewardship services.
     *
     * @param operationalStewardshipServices StewardshipOperationalServices object
     */
    void setOperationalStewardshipServices(StewardshipOperationalServices operationalStewardshipServices)
    {
        this.operationalStewardshipServices = operationalStewardshipServices;
    }


    /**
     * Return the running instance of the Security Sync
     *
     * @return SecuritySyncOperationalServices object
     */
    SecuritySyncOperationalServices getOperationalSecuritySyncServices()
    {
        return operationalSecuritySyncServices;
    }


    /**
     * Set up the running instance of the Security Sync
     *
     * @param operationalSecuritySyncServices SecuritySyncOperationalServices object
     */
    void setOperationalSecuritySyncServices(SecuritySyncOperationalServices operationalSecuritySyncServices)
    {
        this.operationalSecuritySyncServices = operationalSecuritySyncServices;
    }

    /**
     * Return the running instance of the Security Officer Server
     *
     * @return SecurityOfficerOperationalServices object
     */
    SecurityOfficerOperationalServices getOperationalSecurityOfficerService()
    {
        return operationalSecurityOfficerService;
    }

    /**
     * Set up the running instance of the Security Officer Server
     *
     * @param operationalSecurityOfficerService SecurityOfficerOperationalServices object
     */
    void setOperationalSecurityOfficerService(SecurityOfficerOperationalServices operationalSecurityOfficerService)
    {
        this.operationalSecurityOfficerService = operationalSecurityOfficerService;
    }

    /**
     * Return the running instance of Virtualizer
     *
     * @return VirtualizationOperationalServices object
     */
    VirtualizationOperationalServices getOperationalVirtualizationServices()
    {
        return operationalVirtualizationServices;
    }


    /**
     * Set up the running instance of Virtualizer
     *
     * @param operationalVirtualizationServices VirtualizationOperationalServices object
     */
    void setOperationalVirtualizationServices(VirtualizationOperationalServices operationalVirtualizationServices)
    {
        this.operationalVirtualizationServices = operationalVirtualizationServices;
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
     * Return the running instance of Data Platform Service
     *
     * @return DataPlatformOperationalServices
     */
    DataPlatformOperationalServices getOperationalDataPlatformServices() {
        return operationalDataPlatformServices;
    }


    /**
     * Set up the running instance of Data Platform Service
     *
     * @param operationalDataPlatformServices DataPlatformOperationalServices
     */
    void setOperationalDataPlatformServices(DataPlatformOperationalServices operationalDataPlatformServices)
    {
        this.operationalDataPlatformServices = operationalDataPlatformServices;
    }


    /**
     * Retrieve the operational admin services' audit log.
     *
     * @return logging destination
     */
    public OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Set up the operational admin services' audit log.
     *
     * @param auditLog logging destination
     */
    public void setAuditLog(OMRSAuditLog auditLog)
    {
        this.auditLog = auditLog;
    }
}