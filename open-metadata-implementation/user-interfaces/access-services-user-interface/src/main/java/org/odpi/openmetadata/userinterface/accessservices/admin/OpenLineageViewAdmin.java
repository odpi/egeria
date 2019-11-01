/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.admin;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.accessservices.admin.serviceinstances.OpenLineageViewServicesInstance;
import org.odpi.openmetadata.userinterface.accessservices.auditlog.ViewServiceAuditCode;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterfaces.adminservices.UIServerErrorHandler;

import java.util.Map;

/**
 * OpenLineageViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Open Lineage OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class OpenLineageViewAdmin extends AuditableViewServiceAdmin
{

    private String   openLineageServerNamePropertyName  = "openLineageServerName";
    private String   openLineageServerURLPropertyName   = "openLineageServerURL";
    private OpenLineageViewServicesInstance instance    = null;

    @Override
    protected void createViewServicesInstance(ViewServiceConfig viewServiceConfigurationProperties,
                                              OMRSAuditLog auditLog,
                                              String serverUserName,
                                              int maxPageSize,
                                              String metadataServerName,
                                              String metadataServerURL,
                                              Map<String, Object> options) throws OMAGConfigurationErrorException {
        String methodName = "createViewServicesInstance";
        String viewServiceName = viewServiceConfigurationProperties.getViewServiceName();
        String openLineageServerName = extractPropertyFromViewServiceOptions(options, viewServiceName, openLineageServerNamePropertyName, auditLog);
        String openLineageServerURL = extractPropertyFromViewServiceOptions(options, viewServiceName, openLineageServerURLPropertyName, auditLog);

        UIServerErrorHandler errorHandler = new UIServerErrorHandler();
        errorHandler.validateLineageServerName(serverName, methodName, openLineageServerName);
        errorHandler.validateLineageServerURL(serverName ,methodName, openLineageServerURL);

        this.instance = new OpenLineageViewServicesInstance(serverName,
                auditLog,
                serverUserName,
                maxPageSize,
                metadataServerName,
                metadataServerURL,
                openLineageServerName,
                openLineageServerURL);
        this.serverName = instance.getServerName();
    }
    @Override
    protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties) throws OMAGConfigurationErrorException {
        String methodName ="validateAndExpandViewServicesConfigurationProperties";
        ViewServiceConfig updatedViewServiceConfig = new ViewServiceConfig();
        // to have got here the supplied class and the admin class must be correct.

        // check with anything else has been supplied, if so - check it is correct, if not update with the correct value from the view service description,

        // check service name
        if (viewServiceConfigurationProperties.getViewServiceName() != null && !(viewServiceConfigurationProperties.getViewServiceName().equals(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName()))) {
            logBadConfigProperties(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName(),
                    "viewServiceName",
                    viewServiceConfigurationProperties.getViewServiceName(),
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceName(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName());

        // check description
        if (viewServiceConfigurationProperties.getViewServiceDescription() != null && !(viewServiceConfigurationProperties.getViewServiceDescription().equals(ViewServiceDescription.OPEN_LINEAGE.getViewServiceDescription()))) {
            logBadConfigProperties(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName(),
                    "viewServiceDescription",
                    viewServiceConfigurationProperties.getViewServiceDescription(),
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceDescription(ViewServiceDescription.OPEN_LINEAGE.getViewServiceDescription());

        // check id
        if (viewServiceConfigurationProperties.getViewServiceId() != ViewServiceDescription.OPEN_LINEAGE.getViewServiceCode()) {
            logBadConfigProperties(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName(),
                    "viewServiceId",
                    viewServiceConfigurationProperties.getViewServiceId() + "",
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceId(ViewServiceDescription.OPEN_LINEAGE.getViewServiceCode());

        // check wiki
        if (viewServiceConfigurationProperties.getViewServiceWiki() != null && !(viewServiceConfigurationProperties.getViewServiceWiki().equals(ViewServiceDescription.OPEN_LINEAGE.getViewServiceWiki()))) {
            logBadConfigProperties(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName(),
                    "viewServiceWiki",
                    viewServiceConfigurationProperties.getViewServiceWiki(),
                    auditLog,
                    methodName);
        }

        // make sure the admin class is still specified.
        updatedViewServiceConfig.setViewServiceAdminClass(viewServiceConfigurationProperties.getViewServiceAdminClass());
        viewServiceConfigurationProperties.setViewServiceWiki(ViewServiceDescription.OPEN_LINEAGE.getViewServiceWiki());
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