/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.adminservices.configuration.auditlog.OMAGAuditCode;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.UUID;

/**
 * ViewServiceAdmin is the interface that an view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the ViewServiceDescription enumeration.
 */
public abstract class ViewServiceAdmin {

    final protected String remoteServerName  = "remoteServerName";        /* Common */
    final protected String remoteServerURL   = "remoteServerURL";         /* Common */

    /**
     * Initialize the view service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfigurationProperties specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use on OMRS calls where there is no end user.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @throws OMAGConfigurationErrorException   invalid parameters in the configuration properties.
     */
    public abstract void initialize(String serverName,
                                    ViewServiceConfig viewServiceConfigurationProperties,
                                    OMRSAuditLog auditLog,
                                    String serverUserName,
                                    int maxPageSize) throws OMAGConfigurationErrorException;


    /**
     * Shutdown the view service.
     */
    public abstract void shutdown();


    /**
     * Log that a property value is incorrect.
     *
     * @param viewServiceFullName name of the calling view service
     * @param propertyName        name of the property in error
     * @param propertyValue       value of the property that is in error
     * @param auditLog            log to write message to
     * @param methodName          calling method
     * @param error               resulting exception
     * @throws OMAGConfigurationErrorException exception documenting the error
     */
    private void logBadConfigProperties(String viewServiceFullName,
                                        String propertyName,
                                        String propertyValue,
                                        OMRSAuditLog auditLog,
                                        String methodName,
                                        Throwable error) throws OMAGConfigurationErrorException {
        OMAGAuditCode auditCode = OMAGAuditCode.BAD_CONFIG_PROPERTY;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(viewServiceFullName, propertyValue, propertyName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.BAD_CONFIG_PROPERTIES;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(viewServiceFullName,
                                                                                                 propertyValue,
                                                                                                 propertyName,
                                                                                                 error.getClass().getName(),
                                                                                                 error.getMessage());

        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction(),
                                                  error);
    }

    /**
     * Log that an unexpected exception was received during start up.
     *
     * @param actionDescription   calling method
     * @param fullViewServiceName name of the view service
     * @param error               exception that was caught
     * @throws OMAGConfigurationErrorException wrapped exception
     */
    protected void throwUnexpectedInitializationException(String actionDescription,
                                                          String fullViewServiceName,
                                                          Throwable error) throws OMAGConfigurationErrorException {
        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION;

        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  actionDescription,
                                                  errorCode.getFormattedErrorMessage(fullViewServiceName,
                                                                                     error.getClass().getName(),
                                                                                     error.getMessage()),
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction(),
                                                  error);
    }


}
