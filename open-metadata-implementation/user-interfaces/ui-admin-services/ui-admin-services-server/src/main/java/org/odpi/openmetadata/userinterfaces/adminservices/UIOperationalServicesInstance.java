/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.adminservices.OMAGOperationalServicesInstance;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.userinterface.adminservices.configuration.registration.ViewServiceAdmin;

import java.util.ArrayList;
import java.util.List;

/**
 * UIOperationalServicesInstance provides the references to the active services for an instance of a UI Server.
 */

class UIOperationalServicesInstance extends OMAGOperationalServicesInstance
{
    private UIServerConfig operationalConfiguration = null;
    private List<ViewServiceAdmin> operationalViewServiceAdminList   = new ArrayList<>();


    /**
     * Default constructor
     *
     * @param serviceName name of the new service instance
     */
    public UIOperationalServicesInstance(String   serverName,
                                         String   serviceName,
                                         int      maxPageSize)
    {
        super(serverName, serviceName, maxPageSize);
    }


    /**
     * Return the configuration document that was used to start the current running server.
     * This value is null if the server has not been started.
     *
     * @return UIServerConfig object
     */
    UIServerConfig getOperationalConfiguration() {
        return operationalConfiguration;
    }


    /**
     * Set up the configuration document that was used to start the current running server.
     *
     * @param operationalConfiguration UIServerConfig object
     */
    void setOperationalConfiguration(UIServerConfig operationalConfiguration)
    {
        this.operationalConfiguration = operationalConfiguration;
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
}
