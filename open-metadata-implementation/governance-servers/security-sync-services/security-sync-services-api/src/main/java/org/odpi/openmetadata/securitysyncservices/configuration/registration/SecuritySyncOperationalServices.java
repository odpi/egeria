/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.configuration.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.admin.RangerConnector;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class SecuritySyncOperationalServices {

    private String                         localServerName;               /* Initialized in constructor */
    private String                         localServerType;               /* Initialized in constructor */
    private String                         localMetadataCollectionName;   /* Initialized in constructor */
    private String                         localOrganizationName;         /* Initialized in constructor */
    private String                         localServerUserId;             /* Initialized in constructor */
    private String                         localServerURL;                /* Initialized in constructor */
    private int                            maxPageSize;                   /* Initialized in constructor */


    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerType type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId user id for this server to use if processing inbound messages.
     * @param localServerURL URL root for this server.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public SecuritySyncOperationalServices(String localServerName,
                                           String localServerType,
                                           String localOrganizationName,
                                           String localServerUserId,
                                           String localServerURL,
                                           int maxPageSize) {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localServerUserId = localServerUserId;
        this.localServerURL = localServerURL;
        this.maxPageSize = maxPageSize;
    }

    public void initialize(SecuritySyncConfig securitySyncConfig, OMRSAuditLog auditLog)
    {
        if (securitySyncConfig != null)
        {
            RangerConnector rangerConnector = new RangerConnector();
            rangerConnector.initialize(securitySyncConfig, auditLog);
        }
    }

    /**
     * Shutdown the Security Sync Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        return false;
    }
}
