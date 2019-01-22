/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.processor;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecuritySyncProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecuritySyncProcessor.class);
    private SecuritySyncConfig securitySyncConfig;
    private OMRSAuditLog auditLog;

    public SecuritySyncProcessor(SecuritySyncConfig securitySyncConfig, OMRSAuditLog auditLog) {
        this.securitySyncConfig = securitySyncConfig;
        this.auditLog = auditLog;
    }

    public void processGovernedEvent(GovernedAsset governedAsset) {
        //TODO: processing will be added here
    }
}
