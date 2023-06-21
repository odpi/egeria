/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import org.odpi.openmetadata.accessservices.assetcatalog.auditlog.AssetCatalogAuditCode;
import org.odpi.openmetadata.accessservices.assetcatalog.listeners.AssetCatalogOMRSTopicListener;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.Collections;
import java.util.List;

/**
 * AssetCatalogAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Asset Catalog OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class AssetCatalogAdmin extends AccessServiceAdmin {

    private static final String SUPPORTED_TYPES_FOR_SEARCH   = "SupportedTypesForSearch";
    private static final String ENABLE_INDEX_EVENTS          = "EnableIndexingEvents";
    private AuditLog auditLog;
    private String serverName;
    private AssetCatalogServicesInstance instance;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector                  connector for querying the cohort repositories
     * @param auditLog                             audit log component for logging messages.
     * @param serverUserName                       user id to use on OMRS calls where there is no end user.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           AuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, AssetCatalogAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try {
            this.auditLog = auditLog;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfigurationProperties.getAccessServiceOptions(),
                    accessServiceConfigurationProperties.getAccessServiceName(),
                    auditLog);

            List<String> supportedTypesForSearch = getSupportedTypesForSearchOption(accessServiceConfigurationProperties);

            instance = new AssetCatalogServicesInstance(repositoryConnector, supportedZones, auditLog, serverUserName,
                    accessServiceConfigurationProperties.getAccessServiceName(), supportedTypesForSearch,
                    accessServiceConfigurationProperties.getAccessServiceInTopic(),
                    accessServiceConfigurationProperties.getAccessServiceOutTopic());


            boolean indexingEnabled = this.isIndexingEnabled(accessServiceConfigurationProperties);

            if(indexingEnabled) {
                registerListener(accessServiceConfigurationProperties,
                        enterpriseOMRSTopicConnector,
                        repositoryConnector,
                        auditLog);
            }

            this.serverName = instance.getServerName();

            auditLog.logMessage(actionDescription, AssetCatalogAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));

        } catch (Exception error) {
            auditLog.logException(actionDescription, AssetCatalogAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(), serverName), error);

            super.throwUnexpectedInitializationException(actionDescription, AccessServiceDescription.ASSET_CATALOG_OMAS.getAccessServiceFullName(), error);
        }
    }

    private void registerListener(AccessServiceConfig accessServiceConfigurationProperties,
                                  OMRSTopicConnector enterpriseOMRSTopicConnector,
                                  OMRSRepositoryConnector repositoryConnector,
                                  AuditLog auditLog) throws OMAGConfigurationErrorException {

        Connection outTopicConnection = accessServiceConfigurationProperties.getAccessServiceOutTopic();

        String serviceName = accessServiceConfigurationProperties.getAccessServiceName();

        OpenMetadataTopicConnector outTopicConnector = super.getOutTopicEventBusConnector(
                outTopicConnection,
                accessServiceConfigurationProperties.getAccessServiceName(),
                auditLog);

        List<String> supportedZones = this.extractSupportedZones(
                accessServiceConfigurationProperties.getAccessServiceOptions(),
                serviceName,
                auditLog);

        List<String> supportedTypesForSearch = getSupportedTypesForSearchOption(accessServiceConfigurationProperties);

        AssetCatalogOMRSTopicListener omrsTopicListener = new AssetCatalogOMRSTopicListener(
                serviceName,
                auditLog,
                outTopicConnector,
                repositoryConnector.getRepositoryHelper(),
                repositoryConnector.getRepositoryValidator(),
                serverName,
                supportedZones,
                supportedTypesForSearch
                );

        super.registerWithEnterpriseTopic(
                serviceName,
                serverName,
                enterpriseOMRSTopicConnector,
                omrsTopicListener,
                auditLog);
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

            auditLog.logMessage(actionDescription, AssetCatalogAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
        }
    }

    private boolean isIndexingEnabled(AccessServiceConfig accessServiceConfigurationProperties) {
        if (accessServiceConfigurationProperties.getAccessServiceOptions() != null) {
            return (Boolean) accessServiceConfigurationProperties.getAccessServiceOptions().getOrDefault(ENABLE_INDEX_EVENTS, Boolean.FALSE);
        }
        return false;
    }


    /**
     *
     * @param accessServiceConfigurationProperties service configuration object
     * @return the list of supported types for search
     */
    private List<String> getSupportedTypesForSearchOption(AccessServiceConfig accessServiceConfigurationProperties) {
        if (accessServiceConfigurationProperties.getAccessServiceOptions() != null) {
            Object supportedTypesProperty = accessServiceConfigurationProperties.getAccessServiceOptions().get(SUPPORTED_TYPES_FOR_SEARCH);
            if (supportedTypesProperty instanceof List) {
                return (List<String>) supportedTypesProperty;
            }
        }

        return Collections.emptyList();
    }
}