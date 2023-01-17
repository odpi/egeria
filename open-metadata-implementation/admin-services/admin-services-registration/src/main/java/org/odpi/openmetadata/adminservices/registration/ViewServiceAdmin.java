/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.registration;

import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;


/**
 * ViewServiceAdmin is the interface that an view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the ViewServiceDescription enumeration.
 */
public abstract class ViewServiceAdmin
{
    final protected String remoteServerName  = "remoteServerName";        /* Common */
    final protected String remoteServerURL   = "remoteServerURL";         /* Common */

    private String     fullServiceName = null;

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
    public abstract void initialize(String            serverName,
                                    ViewServiceConfig viewServiceConfigurationProperties,
                                    AuditLog          auditLog,
                                    String            serverUserName,
                                    int               maxPageSize) throws OMAGConfigurationErrorException;


    /**
     * Shutdown the view service.
     */
    public abstract void shutdown();


    /**
     * Return the cached service name.
     *
     * @return string name
     */
    public String getFullServiceName()
    {
        return fullServiceName;
    }


    /**
     * Set up the cached service name.
     *
     * @param fullServiceName string name
     */
    public void setFullServiceName(String fullServiceName)
    {
        this.fullServiceName = fullServiceName;
    }


    /**
     * Log that the configuration is not valid
     *
     * @param viewServiceFullName name of the calling view service
     * @param propertyName        name of the property in error
     * @param propertyValue       value of the property that is in error
     * @param auditLog            log to write message to
     * @param methodName          calling method
     * @throws OMAGConfigurationErrorException exception documenting the error
     */
    protected void logBadConfiguration(String       viewServiceFullName,
                                       String       propertyName,
                                       String       propertyValue,
                                       AuditLog     auditLog,
                                       String       methodName) throws OMAGConfigurationErrorException
    {
        auditLog.logMessage(methodName,
                            OMAGAdminAuditCode.BAD_CONFIG_PROPERTY.getMessageDefinition(viewServiceFullName, propertyValue, propertyName));

        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.VIEW_SERVICE_CONFIG.getMessageDefinition(viewServiceFullName,
                                                                                                                propertyValue,
                                                                                                                propertyName),
                                                  this.getClass().getName(),
                                                  methodName);
    }

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
    protected void logBadConfigProperties(String       viewServiceFullName,
                                          String       propertyName,
                                          String       propertyValue,
                                          AuditLog     auditLog,
                                          String       methodName,
                                          Throwable    error) throws OMAGConfigurationErrorException
    {
        auditLog.logMessage(methodName,
                            OMAGAdminAuditCode.BAD_CONFIG_PROPERTY.getMessageDefinition(viewServiceFullName, propertyValue, propertyName));

        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_CONFIG_PROPERTIES.getMessageDefinition(viewServiceFullName,
                                                                                                                propertyValue,
                                                                                                                propertyName,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName,
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
    protected void throwUnexpectedInitializationException(String    actionDescription,
                                                          String    fullViewServiceName,
                                                          Throwable error) throws OMAGConfigurationErrorException
    {
        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(fullViewServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                  this.getClass().getName(),
                                                  actionDescription,
                                                  error);
    }
}
