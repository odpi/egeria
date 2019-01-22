/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;

public interface SecuritySyncAdmin {
    /**
     * Initialize the security sync
     *
     * @param securitySyncConfig           specific configuration properties for the security sync
     * @param enterpriseOMRSTopicConnector connector for receiving OMRS Events from the cohorts
     * @param auditLog                     audit log component for logging messages.
     * @param serverUserName               user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    void initialize(SecuritySyncConfig securitySyncConfig,
                    OMRSTopicConnector enterpriseOMRSTopicConnector,
                    OMRSAuditLog auditLog,
                    String serverUserName) throws OMAGConfigurationErrorException;


    /**
     * Shutdown the access service.
     */
    void shutdown();
}
