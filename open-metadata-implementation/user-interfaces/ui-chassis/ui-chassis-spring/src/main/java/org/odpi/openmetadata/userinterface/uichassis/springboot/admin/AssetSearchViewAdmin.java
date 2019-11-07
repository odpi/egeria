/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.admin;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.uichassis.springboot.admin.serviceinstances.AssetSearchViewServicesInstance;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auditlog.ViewServiceAuditCode;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;

import java.util.Map;

/**
 * AssetSearchViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Asset Search OMVS. The initialization call provides this OMVS with the Audit log and configuration.
 */
public class AssetSearchViewAdmin extends AuditableViewServiceAdmin
{

    private AssetSearchViewServicesInstance instance = null;

    @Override
    protected void createViewServicesInstance(ViewServiceConfig viewServiceConfigurationProperties,
                                              OMRSAuditLog auditLog,
                                              String serverUserName,
                                              int maxPageSize,
                                              String metadataServerName,
                                              String metadataServerURL,
                                              Map<String, Object> options) {
        this.instance = new AssetSearchViewServicesInstance(serverName,
                auditLog,
                serverUserName,
                maxPageSize,
                metadataServerName,
                metadataServerURL);
        this.serverName = instance.getServerName();


    }
    @Override
    protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties) throws OMAGConfigurationErrorException {
        ViewServiceConfig updatedViewServiceConfig = validateAndExpandViewServicesConfigurationProperties(viewServiceConfigurationProperties, ViewServiceDescription.ASSET_SEARCH);
        return updatedViewServiceConfig;
    }
    /**
     * Shutdown the view service.
     */
    public void shutdown()
    {
        if (instance != null)
        {
            instance.shutdown();
        }

        if (auditLog != null)
        {
            final String actionDescription = "shutdown";

            ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }

    /**
     * Get the audit log
     * @return OMRSAuditLog the audit log
     */
    public OMRSAuditLog getAuditLog() {
        return auditLog;
    }
}