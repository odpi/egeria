/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.processor;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GovernanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEventProcessor.class);
    private OMRSAuditLog auditLog;

    public GovernanceEventProcessor(OMRSRepositoryConnector enterpriseConnector, OMRSAuditLog auditLog) {
        this.auditLog = auditLog;
    }

    public void processGovernedEvent(GovernedAsset governedAsset) {
        //TODO: processing will be added here
    }
}
