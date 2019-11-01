/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.admin;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.accessservices.admin.serviceinstances.TypeExplorerViewServicesInstance;
import org.odpi.openmetadata.userinterface.accessservices.auditlog.ViewServiceAuditCode;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;

import java.util.Map;

/**
 * TypeExplorerViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Type Explorer OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class TypeExplorerViewAdmin extends AuditableViewServiceAdmin
{

    private TypeExplorerViewServicesInstance instance = null;


    @Override
    protected void createViewServicesInstance(ViewServiceConfig viewServiceConfigurationProperties,
                                              OMRSAuditLog auditLog,
                                              String serverUserName,
                                              int maxPageSize,
                                              String metadataServerName,
                                              String metadataServerURL,
                                              Map<String, Object> options) {
        this.instance = new TypeExplorerViewServicesInstance(serverName,
                auditLog,
                serverUserName,
                maxPageSize,
                metadataServerName,
                metadataServerURL);
        this.serverName = instance.getServerName();
    }
    @Override
    protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties) throws OMAGConfigurationErrorException {
        String methodName ="validateAndExpandViewServicesConfigurationProperties";
        ViewServiceConfig updatedViewServiceConfig = new ViewServiceConfig();
        // to have got here the supplied class and the admin class must be correct.

        // check with anything else has been supplied, if so - check it is correct, if not update with the correct value from the view service description,

        // check service name
        if (viewServiceConfigurationProperties.getViewServiceName() != null && !(viewServiceConfigurationProperties.getViewServiceName().equals(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName()))) {
            logBadConfigProperties(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName(),
                    "viewServiceName",
                    viewServiceConfigurationProperties.getViewServiceName(),
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceName(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName());

        // check description
        if (viewServiceConfigurationProperties.getViewServiceDescription() != null && !(viewServiceConfigurationProperties.getViewServiceDescription().equals(ViewServiceDescription.TYPE_EXPLORER.getViewServiceDescription()))) {
            logBadConfigProperties(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName(),
                    "viewServiceDescription",
                    viewServiceConfigurationProperties.getViewServiceDescription(),
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceDescription(ViewServiceDescription.TYPE_EXPLORER.getViewServiceDescription());

        // check id
        if (viewServiceConfigurationProperties.getViewServiceId() != ViewServiceDescription.TYPE_EXPLORER.getViewServiceCode()) {
            logBadConfigProperties(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName(),
                    "viewServiceId",
                    viewServiceConfigurationProperties.getViewServiceId() + "",
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceId(ViewServiceDescription.TYPE_EXPLORER.getViewServiceCode());

        // check wiki
        if (viewServiceConfigurationProperties.getViewServiceWiki() != null && !(viewServiceConfigurationProperties.getViewServiceWiki().equals(ViewServiceDescription.TYPE_EXPLORER.getViewServiceWiki()))) {
            logBadConfigProperties(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName(),
                    "viewServiceWiki",
                    viewServiceConfigurationProperties.getViewServiceWiki(),
                    auditLog,
                    methodName);
        }

        // make sure the admin class is still specified.
        updatedViewServiceConfig.setViewServiceAdminClass(viewServiceConfigurationProperties.getViewServiceAdminClass());
        viewServiceConfigurationProperties.setViewServiceWiki(ViewServiceDescription.TYPE_EXPLORER.getViewServiceWiki());
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