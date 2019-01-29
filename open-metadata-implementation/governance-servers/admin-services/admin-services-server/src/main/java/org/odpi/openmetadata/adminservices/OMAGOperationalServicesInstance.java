/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.securitysyncservices.configuration.registration.SecuritySyncOperationalServices;

import java.util.ArrayList;
import java.util.List;

/**
 * OMAGOperationalServicesInstance provides the references to the active services for an instance of an OMAG Server.
 */
public class OMAGOperationalServicesInstance
{
    private OMAGServerConfig         operationalConfiguration               = null;
    private OMRSOperationalServices  operationalRepositoryServices          = null;
    private List<AccessServiceAdmin> operationalAccessServiceAdminList      = new ArrayList<>();
    private SecuritySyncOperationalServices securitySyncOperationalServices = null;

    /**
     * Default constructor
     */
    public OMAGOperationalServicesInstance()
    {
    }


    /**
     * Return the configuration document that was used to start the current running server.
     * This value is null if the server has not been started.
     *
     * @return OMAGServerConfig object
     */
    public OMAGServerConfig getOperationalConfiguration()
    {
        return operationalConfiguration;
    }


    /**
     * Set up the configuration document that was used to start the current running server.
     *
     * @param operationalConfiguration OMAGServerConfig object
     */
    public void setOperationalConfiguration(OMAGServerConfig operationalConfiguration)
    {
        this.operationalConfiguration = operationalConfiguration;
    }


    /**
     * Return the running instance of the Open Metadata Repository Services (OMRS).
     *
     * @return OMRSOperationalServices object
     */
    public OMRSOperationalServices getOperationalRepositoryServices()
    {
        return operationalRepositoryServices;
    }


    /**
     * Set up the running instance of the Open Metadata Repository Services (OMRS).
     *
     * @param operationalRepositoryServices OMRSOperationalServices object
     */
    public void setOperationalRepositoryServices(OMRSOperationalServices operationalRepositoryServices)
    {
        this.operationalRepositoryServices = operationalRepositoryServices;
    }


    /**
     * Return the list of references to the admin object for each active Open Metadata Access Service (OMAS).
     *
     * @return list of AccessServiceAdmin objects
     */
    public List<AccessServiceAdmin> getOperationalAccessServiceAdminList()
    {
        return operationalAccessServiceAdminList;
    }


    /**
     * Set up the list of references to the admin object for each active Open Metadata Access Service (OMAS).
     *
     * @param operationalAccessServiceAdminList list of AccessServiceAdmin objects
     */
    public void setOperationalAccessServiceAdminList(List<AccessServiceAdmin> operationalAccessServiceAdminList)
    {
        this.operationalAccessServiceAdminList = operationalAccessServiceAdminList;
    }

    /**
     * Return the running instance of the Security Sync
     *
     * @return SecuritySyncOperationalServices object
     */
    public SecuritySyncOperationalServices getSecuritySyncOperationalServices()
    {
        return securitySyncOperationalServices;
    }

    /**
     * Set up the running instance of the Security Sync
     *
     * @param securitySyncOperationalServices SecuritySyncOperationalServices object
     */
    public void setSecuritySyncOperationalServices(SecuritySyncOperationalServices securitySyncOperationalServices)
    {
        this.securitySyncOperationalServices = securitySyncOperationalServices;
    }
}
