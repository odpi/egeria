/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.userinterface.adminservices.configuration.registration;


import org.odpi.openmetadata.adminservices.configuration.auditlog.OMAGAuditCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.userinterface.adminservices.configuration.auditlog.ViewServiceAuditCode;

import java.util.Map;

/**
 * ViewServiceAdmin is the interface that an view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the ViewServiceDescription enumeration.
 */
public abstract class ViewServiceAdmin {
    protected OMRSAuditLog auditLog = null;
    protected String serverName = null;

    /**
     * Initialize the view service.
     *
     * @param viewServiceConfig  specific configuration properties for this view service. This is not null.
     * @param auditLog           audit log component for logging messages.
     * @param serverUserName     user id to use on OMRS calls where there is no end user.
     * @param maxPageSize        maximum page size
     * @param serverName         local server name
     * @param metadataServerName metadata server name
     * @param metadataServerURL  metadata server URL
     * @return ViewServiceConfig updated ViewServiceConfig containing all the view service fields filled in.
     */

    public ViewServiceConfig initialize(ViewServiceConfig viewServiceConfig,
                                        OMRSAuditLog auditLog,
                                        String serverUserName,
                                        int maxPageSize,
                                        String serverName,
                                        String metadataServerName,
                                        String metadataServerURL) {

        final String actionDescription = "initialize";
        ViewServiceAuditCode auditCode;

        auditCode = ViewServiceAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName,viewServiceConfig.getViewServiceName()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        try {
            this.auditLog = auditLog;

            createViewServicesInstance(serverName,
                    viewServiceConfig,
                    auditLog,
                    serverUserName,
                    maxPageSize,
                    metadataServerName,
                    metadataServerURL,
                    viewServiceConfig.getViewServiceOptions());

            auditCode = ViewServiceAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName,viewServiceConfig.getViewServiceName()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } catch (Throwable error) {
            auditCode = ViewServiceAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(viewServiceConfig.getViewServiceName(),error.getMessage()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
        return viewServiceConfig;
    }

    /**
     * Validate and expand the view service configuration
     * @param viewServiceConfigurationProperties configuration properties
     * @return ViewServiceConfig a populated objected containing the view configuration
     * @throws OMAGConfigurationErrorException
     */
    abstract protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties) throws OMAGConfigurationErrorException;

    /**
     * Validate that the supplied the view service properties are consistent with the view service.
     * The minimal configuration that can be supplied is the admin class. This method will fill in any unspecified fields.
     *
     * @param viewServiceConfigurationProperties the view service configuration to validate
     * @return updated ViewServiceConfig the updated view service config
     * @throws OMAGConfigurationErrorException configuration error - a property of the the supplied ViewServiceConfig did not match
     *                                         what was expected for this view service.
     */
    protected ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties, ViewServiceDescription viewServiceDescription) throws OMAGConfigurationErrorException {
        String methodName = "validateAndExpandViewServicesConfigurationProperties";
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

    /**
     * This method takes the view configuration and creates an view services instance.
     * The view service instance is an instance specific to a particular view that is created
     * from the configuration. This method also adds the server view instance to the views server map.
     * @param serverName                         serverName
     * @param viewServiceConfigurationProperties view configuration
     * @param auditLog                           audit log
     * @param serverUserName                     server user name
     * @param maxPageSize                        max page size
     * @param metadataServerName                 metadata server name
     * @param metadataServerURL                  metadata server URL
     * @param options                            OMVS specific configuration
     * @throws OMAGConfigurationErrorException   OMAG configuration error
     */
    protected abstract void createViewServicesInstance(String serverName,
                                                       ViewServiceConfig viewServiceConfigurationProperties,
                                                       OMRSAuditLog auditLog,
                                                       String serverUserName,
                                                       int maxPageSize,
                                                       String metadataServerName,
                                                       String metadataServerURL,
                                                       Map<String, Object> options) throws OMAGConfigurationErrorException;


    /**
     * Shutdown the view service. This includes removing the server instance from the views instance Map.
     */
    public abstract void shutdown();

    /**
     * Log that a property value is incorrect.
     *
     * @param viewServiceName name of the calling view service
     * @param propertyName    name of the property in error
     * @param propertyValue   value of the property that is in error
     * @param auditLog        log to write message to
     * @param methodName      calling method
     * @throws OMAGConfigurationErrorException exception documenting the error
     */
    protected void logBadConfigProperties(String viewServiceName,
                                          String propertyName,
                                          String propertyValue,
                                          OMRSAuditLog auditLog,
                                          String methodName) throws OMAGConfigurationErrorException {
        OMAGAuditCode auditCode = OMAGAuditCode.BAD_CONFIG_PROPERTY;
        auditLog.logRecord(methodName,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(viewServiceName, propertyValue, propertyName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        UIAdminErrorCode errorCode = UIAdminErrorCode.BAD_CONFIG_PROPERTIES;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(viewServiceName,
                propertyValue,
                propertyName);

        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Extract ta named property from the view services option.
     *
     * @param viewServiceOptions options passed to the view service.
     * @param viewServiceName    name of calling service
     * @param propertyName       name of the String property to extract from the view service options
     * @param auditLog           audit log for error messages
     * @return the property value
     * @throws OMAGConfigurationErrorException the viewServerOptions are null or the property value is not specified in the viewServerOptions
     */
    protected String extractPropertyFromViewServiceOptions(Map<String, Object> viewServiceOptions,
                                                           String viewServiceName,
                                                           String propertyName,
                                                           OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        final String methodName = "extractMetadataServerName";
        String property = null;

        if (viewServiceOptions == null) {
            logBadConfigProperties(viewServiceName,
                    "viewServiceOptions",
                    null,
                    auditLog,
                    methodName);
        } else {
            property = (String) viewServiceOptions.get(propertyName);

            if (property == null) {
                logBadConfigProperties(viewServiceName,
                        propertyName,
                        null,
                        auditLog,
                        methodName);

            }
        }
        return property;
    }

}
