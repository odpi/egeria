/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import org.odpi.openmetadata.accessservices.assetcatalog.auditlog.AssetCatalogAuditCode;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetCatalogAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Asset Catalog OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class AssetCatalogAdmin extends AccessServiceAdmin {

    public static final String SUPPORTED_TYPES_FOR_SEARCH = "SupportedTypesForSearch";
    private OMRSAuditLog auditLog;
    private AssetCatalogServicesInstance instance;
    private String serverName;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector                  connector for querying the cohort repositories
     * @param auditLog                             audit log component for logging messages.
     * @param serverUserName                       user id to use on OMRS calls where there is no end user.
     */
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        AssetCatalogAuditCode auditCode;

        try {
            auditCode = AssetCatalogAuditCode.SERVICE_INITIALIZING;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            this.auditLog = auditLog;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfigurationProperties.getAccessServiceOptions(),
                    accessServiceConfigurationProperties.getAccessServiceName(),
                    auditLog);

            List<String> supportedTypesForSearch = getSupportedTypesForSearchOption(accessServiceConfigurationProperties);

            instance = new AssetCatalogServicesInstance(repositoryConnector, supportedZones, auditLog, serverName, supportedTypesForSearch);
            this.serverName = instance.getServerName();

            auditCode = AssetCatalogAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    accessServiceConfigurationProperties.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (OMAGConfigurationErrorException error) {
            throw error;
        } catch (Exception error) {
            auditCode = AssetCatalogAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getMessage()),
                    accessServiceConfigurationProperties.toString(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        if (instance != null) {
            instance.shutdown();
        }

        if (auditLog != null) {
            final String actionDescription = "shutdown";

            AssetCatalogAuditCode auditCode = AssetCatalogAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }

    private List<String> getSupportedTypesForSearchOption(AccessServiceConfig accessServiceConfigurationProperties) {
        List<String> supportedTypesForSearch = null;
        if (accessServiceConfigurationProperties.getAccessServiceOptions() != null) {
            Object supportedTypesProperty = accessServiceConfigurationProperties.getAccessServiceOptions().get(SUPPORTED_TYPES_FOR_SEARCH);
            if (supportedTypesProperty != null)
                supportedTypesForSearch = (List<String>) supportedTypesProperty;
        }
        return supportedTypesForSearch;
    }
}