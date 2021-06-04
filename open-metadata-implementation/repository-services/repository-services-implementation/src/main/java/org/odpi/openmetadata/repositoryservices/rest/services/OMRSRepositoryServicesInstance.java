/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.services;

import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager;


/**
 * OMRSRepositoryServicesInstance caches references to OMRS objects for a specific server
 */
public class OMRSRepositoryServicesInstance extends OMAGServerServiceInstance
{
    private OMRSAuditLog                 masterAuditLog;
    private OMRSMetadataCollection       localMetadataCollection;
    private OMRSMetadataCollection       enterpriseMetadataCollection;
    private OMRSMetadataHighwayManager   metadataHighwayManager;
    private String                       localServerURL;
    private AuditLog                     auditLog;


    /**
     * Set up the repository connectors that will service the REST Calls.  If the requested repository connector
     * is null when a REST calls is received, the request is rejected.
     *
     * @param localServerName name of this server
     * @param masterAuditLog audit log at the top of the tree
     * @param localRepositoryConnector link to the repository responsible for servicing the REST calls to the local repository.
     * @param enterpriseRepositoryConnector link to the repository responsible for servicing the REST calls to the enterprise.
     * @param metadataHighwayManager manager of the cohort managers
     * @param localServerURL URL of the local server
     * @param serviceName name of this service
     * @param auditLog logging destination
     * @param maxPageSize max number of results to return on single request.
     */
    public OMRSRepositoryServicesInstance(String                       localServerName,
                                          OMRSAuditLog                 masterAuditLog,
                                          OMRSRepositoryConnector      localRepositoryConnector,
                                          OMRSRepositoryConnector      enterpriseRepositoryConnector,
                                          OMRSMetadataHighwayManager   metadataHighwayManager,
                                          String                       localServerURL,
                                          String                       serviceName,
                                          AuditLog                     auditLog,
                                          int                          maxPageSize)
    {
        super(localServerName, serviceName, maxPageSize);

        this.masterAuditLog = masterAuditLog;
        this.auditLog = auditLog;
        this.localServerURL = localServerURL;
        this.metadataHighwayManager = metadataHighwayManager;

        /*
         * The local repository connector is null in governance servers, view servers and metadata access points.
         */
        if (localRepositoryConnector == null)
        {
            this.localMetadataCollection = null;
        }
        else
        {
            try
            {
                this.localMetadataCollection = localRepositoryConnector.getMetadataCollection();
            }
            catch (Exception error)
            {
                /*
                 * This is unexpected but if the local metadata collection is not set up, the error has already been recorded.
                 */
                this.localMetadataCollection = null;
            }
        }

        /*
         * The enterprise repository connector is null in governance servers, view servers and repository proxies.
         */
        if (enterpriseRepositoryConnector == null)
        {
            this.enterpriseMetadataCollection = null;
        }
        else
        {
            try
            {
                this.enterpriseMetadataCollection = enterpriseRepositoryConnector.getMetadataCollection();
            }
            catch (Exception error)
            {
                /*
                 * This is unexpected but if the enterprise metadata collection is not set up, the error has already been recorded.
                 */
                this.enterpriseMetadataCollection = null;
            }
        }
    }


    /**
     * Return the master audit log for audit log services.
     *
     * @return audit log at the top of the tree
     */
    public OMRSAuditLog getMasterAuditLog()
    {
        return masterAuditLog;
    }


    /**
     * Return the local metadata collection for this instance.
     *
     * @return OMRSMetadataCollection object
     */
    public OMRSMetadataCollection getLocalMetadataCollection()
    {
        return localMetadataCollection;
    }


    /**
     * Return the enterprise metadata collection for this instance.
     *
     * @return OMRSMetadataCollection object
     */
    public OMRSMetadataCollection getEnterpriseMetadataCollection()
    {
        return enterpriseMetadataCollection;
    }


    /**
     * Return the metadata highway manager
     *
     * @return OMRSMetadataHighwayManager object
     */
    public OMRSMetadataHighwayManager getMetadataHighwayManager()
    {
        return metadataHighwayManager;
    }


    /**
     * Return the URL root for this server.
     *
     * @return URL
     */
    public String getLocalServerURL()
    {
        return localServerURL;
    }


    /**
     * Return the audit log destination for this server.
     *
     * @return auditLog
     */
    public AuditLog getAuditLog()
    {
        return auditLog;
    }
}
