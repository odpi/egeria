/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server;


import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.cognos.contentmanager.OMEntityDao;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * CognosServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AnalyticsModelingServicesInstance extends OCFOMASServiceInstance
{
    private DatabaseContextHandler databaseContextHandler;

    /**
     * Set up the local repository connector that will service the REST Calls
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls
     * @param supportedZones      list of zones that OMAS is allowed to serve Assets from
     * @param auditLog            logging destination
     * @param localServerUserId   userId used for server initiated actions
     * @param maxPageSize         max number of results to return on single request
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AnalyticsModelingServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones,
                               AuditLog auditLog, String localServerUserId, int maxPageSize) throws NewInstanceException {
    	
        super(AccessServiceDescription.COGNOS_OMAS.getAccessServiceFullName(), repositoryConnector, supportedZones, null, auditLog,
                localServerUserId, maxPageSize);
        
        OMEntityDao omEntityDao = new OMEntityDao(repositoryConnector, supportedZones, auditLog);

        databaseContextHandler = new DatabaseContextHandler(omEntityDao);
    }


    /**
     * Get the handler that does major work this OMAS is designed for.
     * @return Database Context Handler
     */
    public DatabaseContextHandler getContextBuilder() {
        return databaseContextHandler;
    }
}
