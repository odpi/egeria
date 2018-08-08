/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.admin;

import org.odpi.openmetadata.accessservice.assetcatalog.auditlog.AssetCatalogAuditCode;
import org.odpi.openmetadata.accessservice.assetcatalog.service.AssetCatalogAssetService;
import org.odpi.openmetadata.accessservice.assetcatalog.service.AssetCatalogRelationshipService;
import org.odpi.openmetadata.accessservice.assetcatalog.service.OMASCatalogRESTServices;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * AssetCatalogAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Asset Catalog OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class AssetCatalogAdmin implements AccessServiceAdmin {

    private OMRSAuditLog auditLog;
    
    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         - connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector                  - connector for querying the cohort repositories
     * @param auditLog                             - audit log component for logging messages.
     * @param serverUserName                       - user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException - invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {

        final String actionDescription = "initialize";
        AssetCatalogAuditCode auditCode = AssetCatalogAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        AssetCatalogAssetService.setRepositoryConnector(repositoryConnector);
        AssetCatalogRelationshipService.setRepositoryConnector(repositoryConnector);
        OMASCatalogRESTServices.setRepositoryConnector(repositoryConnector);

        this.auditLog = auditLog;

        auditCode = AssetCatalogAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }

    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        final String actionDescription = "shutdown";
        AssetCatalogAuditCode auditCode;

        auditCode = AssetCatalogAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}