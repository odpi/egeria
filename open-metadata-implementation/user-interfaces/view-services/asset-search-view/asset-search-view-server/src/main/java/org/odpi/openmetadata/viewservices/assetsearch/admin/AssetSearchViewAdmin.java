/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetsearch.admin;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.openmetadata.viewservices.assetsearch.admin.serviceinstances.AssetSearchViewServicesInstance;
import org.odpi.userinterface.adminservices.configuration.auditlog.ViewServiceAuditCode;
import org.odpi.userinterface.adminservices.configuration.registration.ViewServiceAdmin;
import java.util.Map;

/**
 * AssetSearchViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Asset Search OMVS. The initialization call provides this OMVS with the Audit log and configuration.
 */
public class AssetSearchViewAdmin extends ViewServiceAdmin
{
    private AssetSearchViewServicesInstance instance            = null;

    @Override
    protected void createViewServicesInstance(String serverName,
                                              ViewServiceConfig viewServiceConfigurationProperties,
                                              OMRSAuditLog auditLog,
                                              String serverUserName,
                                              int maxPageSize,
                                              String metadataServerName,
                                              String metadataServerURL,
                                              Map<String, Object> options) throws OMAGConfigurationErrorException{
        String methodName = "createViewServicesInstance";

        try {
            this.instance = new AssetSearchViewServicesInstance(serverName,
                    auditLog,
                    serverUserName,
                    maxPageSize,
                    metadataServerName,
                    metadataServerURL);
        } catch (InvalidParameterException e) {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.CREATE_VIEW_SERVICE_INSTANCE_FAILED;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(ViewServiceDescription.ASSET_SEARCH.getViewServiceName(), serverName, e.getParameterName());

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        this.serverName = instance.getServerName();
    }
    @Override
    protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties) throws OMAGConfigurationErrorException {
        return  validateAndExpandViewServicesConfigurationProperties(viewServiceConfigurationProperties, ViewServiceDescription.ASSET_SEARCH);
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