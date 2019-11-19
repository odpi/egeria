/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.admin;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auditlog.ViewServiceAuditCode;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterfaces.adminservices.ViewServiceAdmin;

/**
 * AuditableViewServiceAdmin is the interface that a view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.
 */
public abstract class AuditableViewServiceAdmin extends ViewServiceAdmin
{
    private final String          actionDescription = "initialize";

    @Override
    protected void auditServiceInitializing(){

        ViewServiceAuditCode auditCode = ViewServiceAuditCode.UNKNOWN_SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;
    }
    @Override
    protected void auditServiceInitializing(String serviceName){

        ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName,serviceName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;
    }
    @Override
    protected void auditServiceInitialized(String serviceName) {
        ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName,serviceName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
    @Override
    protected void auditServiceInitializingError(Exception  error) {
        ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_INSTANCE_FAILURE;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(error.getMessage()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

    }

    /**
     *
     * @param viewServiceConfigurationProperties supplied view configuration to validate
     * @param viewServiceDescription view service description to vlidate against
     * @return updated view service configuration properties
     * @throws OMAGConfigurationErrorException configuration error
     */
    protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties, ViewServiceDescription viewServiceDescription ) throws OMAGConfigurationErrorException {
        String methodName ="validateAndExpandViewServicesConfigurationProperties";
        ViewServiceConfig updatedViewServiceConfig = new ViewServiceConfig();
        // to have got here the supplied class and the admin class must be correct.

        // check with anything else has been supplied, if so - check it is correct, if not update with the correct value from the view service description,

        // check service name
        if (viewServiceConfigurationProperties.getViewServiceName() != null && !(viewServiceConfigurationProperties.getViewServiceName().equals(viewServiceDescription.getViewServiceName()))) {
            logBadConfigProperties(viewServiceDescription.getViewServiceName(),
                    "viewServiceName",
                    viewServiceConfigurationProperties.getViewServiceName(),
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceName(viewServiceDescription.getViewServiceName());

        // check description
        if (viewServiceConfigurationProperties.getViewServiceDescription() != null && !(viewServiceConfigurationProperties.getViewServiceDescription().equals(viewServiceDescription.getViewServiceDescription()))) {
            logBadConfigProperties(viewServiceDescription.getViewServiceName(),
                    "viewServiceDescription",
                    viewServiceConfigurationProperties.getViewServiceDescription(),
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceDescription(viewServiceDescription.getViewServiceDescription());

        // check id
        if (viewServiceConfigurationProperties.getViewServiceId() != viewServiceDescription.getViewServiceCode()) {
            logBadConfigProperties(viewServiceDescription.getViewServiceName(),
                    "viewServiceId",
                    viewServiceConfigurationProperties.getViewServiceId() + "",
                    auditLog,
                    methodName);
        }
        updatedViewServiceConfig.setViewServiceId(viewServiceDescription.getViewServiceCode());

        // check wiki
        if (viewServiceConfigurationProperties.getViewServiceWiki() != null && !(viewServiceConfigurationProperties.getViewServiceWiki().equals(viewServiceDescription.getViewServiceWiki()))) {
            logBadConfigProperties(viewServiceDescription.getViewServiceName(),
                    "viewServiceWiki",
                    viewServiceConfigurationProperties.getViewServiceWiki(),
                    auditLog,
                    methodName);
        }

        // make sure the admin class is still specified.
        updatedViewServiceConfig.setViewServiceAdminClass(viewServiceConfigurationProperties.getViewServiceAdminClass());
        viewServiceConfigurationProperties.setViewServiceWiki(viewServiceDescription.getViewServiceWiki());
        return updatedViewServiceConfig;
    }
}
