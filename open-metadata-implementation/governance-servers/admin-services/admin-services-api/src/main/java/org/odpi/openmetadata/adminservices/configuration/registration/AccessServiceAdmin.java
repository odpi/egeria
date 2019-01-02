/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * AccessServiceAdmin is the interface that an access service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the AccessServiceDescription enumeration.
 */
public interface AccessServiceAdmin
{
    /*
     * These are standard property names that an access service may support.  They are passed in the
     * AccessServiceConfig as the accessServicesOptions.  Individual access services may support
     * additional properties.
     */
    String            supportedZonesPropertyName      = "SupportedZones";      /* All */
    String            karmaPointThresholdPropertyName = "KarmaPointThreshold"; /* Community Profile OMAS */

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                    OMRSTopicConnector      enterpriseOMRSTopicConnector,
                    OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                    OMRSAuditLog            auditLog,
                    String                  serverUserName) throws OMAGConfigurationErrorException;


    /**
     * Shutdown the access service.
     */
    void shutdown();
}
