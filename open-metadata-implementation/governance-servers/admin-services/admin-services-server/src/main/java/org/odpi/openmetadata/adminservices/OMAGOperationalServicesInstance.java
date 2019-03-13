/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.governanceservers.discoveryengine.admin.DiscoveryEngineOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices;
import org.odpi.openmetadata.governanceservers.stewardshipservices.admin.StewardshipOperationalServices;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.securitysyncservices.configuration.registration.SecuritySyncOperationalServices;
import org.odpi.openmetadata.governanceservers.virtualizationservices.admin.VirtualizationOperationalServices;

import java.util.ArrayList;
import java.util.List;

/**
 * OMAGOperationalServicesInstance provides the references to the active services for an instance of an OMAG Server.
 */

public class OMAGOperationalServicesInstance
{
    private OMAGServerConfig                   operationalConfiguration          = null;
    private OMRSOperationalServices            operationalRepositoryServices     = null;
    private List<AccessServiceAdmin>           operationalAccessServiceAdminList = new ArrayList<>();
    private DiscoveryEngineOperationalServices operationalDiscoveryEngine        = null;
    private OpenLineageOperationalServices openLineageOperationalServices = null;
    private StewardshipOperationalServices     operationalStewardshipServices    = null;
    private SecuritySyncOperationalServices    operationalSecuritySyncServices   = null;
    private VirtualizationOperationalServices  operationalVirtualizationServices = null;

    /**
     * Default constructor
     */
    public OMAGOperationalServicesInstance() {
    }


    /**
     * Return the configuration document that was used to start the current running server.
     * This value is null if the server has not been started.
     *
     * @return OMAGServerConfig object
     */
    public OMAGServerConfig getOperationalConfiguration() {
        return operationalConfiguration;
    }


    /**
     * Set up the configuration document that was used to start the current running server.
     *
     * @param operationalConfiguration OMAGServerConfig object
     */
    public void setOperationalConfiguration(OMAGServerConfig operationalConfiguration) {
        this.operationalConfiguration = operationalConfiguration;
    }


    /**
     * Return the running instance of the Open Metadata Repository Services (OMRS).
     *
     * @return OMRSOperationalServices object
     */
    public OMRSOperationalServices getOperationalRepositoryServices() {
        return operationalRepositoryServices;
    }


    /**
     * Set up the running instance of the Open Metadata Repository Services (OMRS).
     *
     * @param operationalRepositoryServices OMRSOperationalServices object
     */
    public void setOperationalRepositoryServices(OMRSOperationalServices operationalRepositoryServices) {
        this.operationalRepositoryServices = operationalRepositoryServices;
    }


    /**
     * Return the list of references to the admin object for each active Open Metadata Access Service (OMAS).
     *
     * @return list of AccessServiceAdmin objects
     */
    public List<AccessServiceAdmin> getOperationalAccessServiceAdminList() {
        return operationalAccessServiceAdminList;
    }


    /**
     * Set up the list of references to the admin object for each active Open Metadata Access Service (OMAS).
     *
     * @param operationalAccessServiceAdminList list of AccessServiceAdmin objects
     */
    public void setOperationalAccessServiceAdminList(List<AccessServiceAdmin> operationalAccessServiceAdminList) {
        this.operationalAccessServiceAdminList = operationalAccessServiceAdminList;
    }


    /**
     * Return the running instance of the discovery engine.
     *
     * @return DiscoveryEngineOperationalServices object
     */
    public DiscoveryEngineOperationalServices getOperationalDiscoveryEngine() {
        return operationalDiscoveryEngine;
    }


    /**
     * Set up the running instance of the discovery engine.
     *
     * @param openLineageOperationalServices OpenLineageOperationalServices object
     */
    public void setOpenLineageOperationalServices(OpenLineageOperationalServices openLineageOperationalServices) {
        this.openLineageOperationalServices = openLineageOperationalServices;
    }

    /**
     * Return the running instance of the discovery engine.
     *
     * @return DiscoveryEngineOperationalServices object
     */
    public OpenLineageOperationalServices getOpenLineageOperationalServices() {
        return openLineageOperationalServices;
    }


    /**
     * Set up the running instance of the discovery engine.
     *
     * @param operationalDiscoveryEngine DiscoveryEngineOperationalServices object
     */
    public void setOperationalDiscoveryEngine(DiscoveryEngineOperationalServices operationalDiscoveryEngine) {
        this.operationalDiscoveryEngine = operationalDiscoveryEngine;
    }


    /**
     * Return the running instance of the stewardship services.
     *
     * @return StewardshipOperationalServices object
     */
    public StewardshipOperationalServices getOperationalStewardshipServices() {
        return operationalStewardshipServices;
    }


    /**
     * Set up running instance of the stewardship services.
     *
     * @param operationalStewardshipServices StewardshipOperationalServices object
     */
    public void setOperationalStewardshipServices(StewardshipOperationalServices operationalStewardshipServices) {
        this.operationalStewardshipServices = operationalStewardshipServices;
    }


    /**
     * Return the running instance of the Security Sync
     *
     * @return SecuritySyncOperationalServices object
     */
    public SecuritySyncOperationalServices getOperationalSecuritySyncServices() {
        return operationalSecuritySyncServices;
    }

    /**
     * Set up the running instance of the Security Sync
     *
     * @param operationalSecuritySyncServices SecuritySyncOperationalServices object
     */
    public void setOperationalSecuritySyncServices(SecuritySyncOperationalServices operationalSecuritySyncServices) {
        this.operationalSecuritySyncServices = operationalSecuritySyncServices;
    }

    /**
     * Return the running instance of Virtualizer
     *
     * @return VirtualizationOperationalServices object
     */
    public VirtualizationOperationalServices getOperationalVirtualizationServices() {
        return operationalVirtualizationServices;
    }

    /**
     * Set up the running instance of Virtualizer
     *
     * @param operationalVirtualizationServices VirtualizationOperationalServices object
     */
    public void setOperationalVirtualizationServices(VirtualizationOperationalServices operationalVirtualizationServices) {
        this.operationalVirtualizationServices = operationalVirtualizationServices;
    }
}
