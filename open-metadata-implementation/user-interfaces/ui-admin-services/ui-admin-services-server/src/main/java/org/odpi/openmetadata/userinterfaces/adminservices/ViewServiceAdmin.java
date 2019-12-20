/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.adminservices.configuration.auditlog.OMAGAuditCode;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;

import java.util.Map;

/**
 * ViewServiceAdmin is the interface that an view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the ViewServiceDescription enumeration.
 */
public abstract class ViewServiceAdmin
{
    protected OMRSAuditLog                 auditLog    = null;
    protected String                       serverName = null;
    /**
     * Initialize the view service.
     *
     * @param viewServiceConfigurationProperties  specific configuration properties for this view service. This is not null.
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @param maxPageSize  maximum page size
     * @param serverName local server name
     * @param metadataServerName metadata server name
     * @param metadataServerURL metadata server URL
     * @return ViewServiceConfig updated ViewServiceConfig containing all the view service fields filled in.
     */

    public ViewServiceConfig initialize(ViewServiceConfig  viewServiceConfigurationProperties,
                           OMRSAuditLog                    auditLog,
                           String                          serverUserName,
                           int                             maxPageSize,
                           String                          serverName,
                           String                          metadataServerName,
                           String                          metadataServerURL)
    {
        ViewServiceConfig updatedViewServiceConfig = null;
        this.auditLog = auditLog;
        this.serverName = serverName;
        try {
            if ( viewServiceConfigurationProperties == null || viewServiceConfigurationProperties.getViewServiceName() == null) {
                auditServiceInitializing();
            } else {
                auditServiceInitializing(viewServiceConfigurationProperties.getViewServiceName());
            }

            updatedViewServiceConfig = validateAndExpandViewServicesConfigurationProperties(viewServiceConfigurationProperties);

            createViewServicesInstance(viewServiceConfigurationProperties,
                    auditLog,
                    serverUserName,
                    maxPageSize,
                    metadataServerName,
                    metadataServerURL,
                    viewServiceConfigurationProperties.getViewServiceOptions()
               );
            auditServiceInitialized(updatedViewServiceConfig.getViewServiceName());
        }
        catch (Exception  error)
        {
            auditServiceInitializingError(error);
        }
        return updatedViewServiceConfig;
    }

    /**
     * Validate that the supplied the view service properties are consistent with the view service.
     * The minimal configuration that can be supplied is the admin class. This method will fill in any unspecified fields.
     * @param viewServiceConfigurationProperties the view service configuration to validate
     * @throws OMAGConfigurationErrorException configuration error - a property of the the supplied ViewServiceConfig did not match
     *        what was expected for this view service.
     * @return updated ViewServiceConfig the updated view service config
     */
    protected abstract ViewServiceConfig validateAndExpandViewServicesConfigurationProperties(ViewServiceConfig viewServiceConfigurationProperties) throws OMAGConfigurationErrorException;

    /**
     * Audit log the view service initializing
     * @param serviceName name of the service initializing
     */
    protected abstract void auditServiceInitializing(String serviceName);

    /**
     * Audit log the view service initializing
     */
    protected abstract void auditServiceInitializing();

    /**
     * Audit log the view service initialized
     * @param serviceName name of the service for audit message
     */
    protected abstract void auditServiceInitialized(String serviceName);

    /**
     * Audit log error during initialization of the view service
     * @param error error during initialization
     */
    protected abstract void auditServiceInitializingError(Exception  error);

    /**
     * This method takes the view configuration and creates an view services instance.
     * The view service instance is an instance specific to a particular view that is created
     * from the configuration. This method also adds the server view instance to the views server map.
     *
     * @param viewServiceConfigurationProperties view configuration
     * @param auditLog audit log
     * @param serverUserName server user name
     * @param maxPageSize  max page size
     * @param metadataServerName metadata server name
     * @param metadataServerURL metadata server URL
     * @param options OMVS specific configuration
     * @throws OMAGConfigurationErrorException OMAG configuration error
     */
    protected abstract void createViewServicesInstance(ViewServiceConfig viewServiceConfigurationProperties,
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
     * @param propertyName name of the property in error
     * @param propertyValue value of the property that is in error
     * @param auditLog log to write message to
     * @param methodName calling method
     *
     * @throws OMAGConfigurationErrorException exception documenting the error
     */
    protected void logBadConfigProperties(String viewServiceName,
                                          String propertyName,
                                          String propertyValue,
                                          OMRSAuditLog auditLog,
                                          String methodName) throws OMAGConfigurationErrorException
    {
        OMAGAuditCode auditCode = OMAGAuditCode.BAD_CONFIG_PROPERTY;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(viewServiceName, propertyValue, propertyName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.BAD_CONFIG_PROPERTIES;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(viewServiceName,
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
     * @param viewServiceName name of calling service
     * @param propertyName name of the String property to extract from the view service options
     * @param auditLog audit log for error messages
     * @return the property value
     * @throws OMAGConfigurationErrorException the viewServerOptions are null or the property value is not specified in the viewServerOptions
     */
    protected String extractPropertyFromViewServiceOptions(Map<String, Object> viewServiceOptions,
                                               String              viewServiceName,
                                               String              propertyName,
                                               OMRSAuditLog        auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "extractMetadataServerName";
        String   property = null;

        if (viewServiceOptions == null)
        {
            logBadConfigProperties(viewServiceName,
                    "viewServiceOptions",
                    null,
                    auditLog,
                    methodName);
        }
        else {
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
