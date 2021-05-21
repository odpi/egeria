/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.admin;


import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.listeners.AssetLineageOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageServicesInstance;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AssetLineageAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Asset Lineage OMAS. The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class AssetLineageAdmin extends AccessServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageAdmin.class);
    private AuditLog auditLog;
    private AssetLineageServicesInstance instance;
    private String serverName;

    /**
     * Default constructor
     */
    public AssetLineageAdmin() {
    }

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector                  connector for querying the cohort repositories
     * @param auditLog                             audit log component for logging messages.
     * @param serverUserName                       user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           AuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        auditLog.logMessage(actionDescription, AssetLineageAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try {
            this.auditLog = auditLog;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfigurationProperties.getAccessServiceOptions(),
                    accessServiceConfigurationProperties.getAccessServiceName(),
                    auditLog);

            Set<String> lineageClassificationTypes = getLineageClassificationTypes(accessServiceConfigurationProperties);
            this.instance = new AssetLineageServicesInstance(repositoryConnector,
                    supportedZones, lineageClassificationTypes, serverUserName, auditLog);
            this.serverName = instance.getServerName();

            Connection outTopicConnection = accessServiceConfigurationProperties.getAccessServiceOutTopic();
            Map<String, Object> accessServiceOptions = accessServiceConfigurationProperties.getAccessServiceOptions();
            if (outTopicConnection != null) {
                OpenMetadataTopicConnector outTopicConnector = super.getOutTopicEventBusConnector(outTopicConnection,
                        accessServiceConfigurationProperties.getAccessServiceName(), auditLog);

                AssetLineageOMRSTopicListener omrsTopicListener = new AssetLineageOMRSTopicListener(
                        repositoryConnector.getRepositoryHelper(), outTopicConnector, serverName,
                        serverUserName,
                        lineageClassificationTypes,
                        auditLog,
                        accessServiceOptions);

                super.registerWithEnterpriseTopic(accessServiceConfigurationProperties.getAccessServiceName(),
                        serverName,
                        enterpriseOMRSTopicConnector,
                        omrsTopicListener,
                        auditLog);
                this.instance.setAssetLineagePublisher(omrsTopicListener.getPublisher());
            }

            auditLog.logMessage(actionDescription, AssetLineageAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
        } catch (OMAGConfigurationErrorException error) {
            log.error("The Asset Lineage OMAS could not be started", error);
            throw error;
        } catch (Throwable error) {
            log.error("The Asset Lineage OMAS could not be started", error);
            auditLog.logException(actionDescription, AssetLineageAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(), serverName), error);

            super.throwUnexpectedInitializationException(actionDescription, AccessServiceDescription.ASSET_LINEAGE_OMAS.getAccessServiceFullName(), error);
        }
    }

    /**
     * Returns the list of lineage classifications
     *
     * @param accessServiceConfig Asset Lineage Configuration
     * @return the list of the lineage classifications
     */
    private Set<String> getLineageClassificationTypes(AccessServiceConfig accessServiceConfig) {

        if (accessServiceConfig.getAccessServiceOptions() != null) {
            Object lineageClassificationTypesProperty = accessServiceConfig
                    .getAccessServiceOptions()
                    .get(AssetLineageConstants.LINEAGE_CLASSIFICATION_TYPES_KEY);
            if (lineageClassificationTypesProperty != null) {
                return new HashSet<>((List<String>)lineageClassificationTypesProperty);
            }
        }

        return AssetLineageConstants.immutableDefaultLineageClassifications;
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

            auditLog.logMessage(actionDescription, AssetLineageAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
        }
    }
}
