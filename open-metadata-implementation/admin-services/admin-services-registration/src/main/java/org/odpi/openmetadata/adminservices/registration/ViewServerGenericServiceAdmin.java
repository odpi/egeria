/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.List;
import java.util.Map;


/**
 * ViewServiceAdmin is the interface that a view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the ViewServiceDescription enumeration.
 */
public abstract class ViewServerGenericServiceAdmin
{
    /*
     * These are standard property names that a view service may support.  They are passed in the
     * ViewServiceConfig as the viewServicesOptions.  Individual access services may support
     * additional properties.
     */
    protected final String   supportedTypesForSearch         = "SupportedTypesForSearch"; /* Asset Catalog OMVS */

    /*
     * Properties needed by all view services.
     */
    protected final String remoteServerName  = "remoteServerName";        /* Common */
    protected final String remoteServerURL   = "remoteServerURL";         /* Common */

    private String     fullServiceName = null;

    /**
     * Initialize the view service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfig                  specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use on OMRS calls where there is no end user.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @param activeViewServices                 list of view services active in this server
     * @throws OMAGConfigurationErrorException   invalid parameters in the configuration properties.
     */
    public abstract void initialize(String                  serverName,
                                    ViewServiceConfig       viewServiceConfig,
                                    AuditLog                auditLog,
                                    String                  serverUserName,
                                    int                     maxPageSize,
                                    List<ViewServiceConfig> activeViewServices) throws OMAGConfigurationErrorException;


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
     * Retrieve the option from the configuration that overrides default list of assets
     * that a caller can search for.
     *
     * @param viewServiceOptions service configuration options
     * @return the list of supported asset types for search or null
     * @throws OMAGConfigurationErrorException error in the view options
     */
    @SuppressWarnings(value = "unchecked")
    protected List<String> getSupportedTypesForSearchOption(Map<String, Object> viewServiceOptions,
                                                            String              viewServiceName,
                                                            AuditLog            auditLog) throws OMAGConfigurationErrorException
    {
        final String methodName = "getSupportedTypesForSearchOption";

        if (viewServiceOptions != null)
        {
            Object supportedTypesObject = viewServiceOptions.get(supportedTypesForSearch);

            if (supportedTypesObject == null)
            {
                auditLog.logMessage(methodName, OMAGAdminAuditCode.ALL_SEARCH_TYPES.getMessageDefinition(viewServiceName));
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String> typeList = (List<String>) supportedTypesObject;

                    auditLog.logMessage(methodName, OMAGAdminAuditCode.SUPPORTED_SEARCH_TYPES.getMessageDefinition(viewServiceName, typeList.toString()));
                    return typeList;
                }
                catch (Exception error)
                {
                    logBadConfigProperties(viewServiceName,
                                           supportedTypesForSearch,
                                           supportedTypesObject.toString(),
                                           auditLog,
                                           methodName,
                                           error);

                    /* unreachable */
                    return null;
                }
            }
        }

        return null;
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
