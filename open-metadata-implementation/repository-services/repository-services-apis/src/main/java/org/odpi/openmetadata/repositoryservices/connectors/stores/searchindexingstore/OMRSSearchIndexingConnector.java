package org.odpi.openmetadata.repositoryservices.connectors.stores.searchindexingstore;
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;

public class OMRSSearchIndexingConnector extends ConnectorBase {

    private OMRSAuditLog auditLog;
    private OMRSTopicConnector enterpriseOMRSTopicConnector;

    public OMRSAuditLog getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(OMRSAuditLog auditLog) {
        this.auditLog = auditLog;
    }

    public OMRSTopicConnector getEnterpriseOMRSTopicConnector() {
        return enterpriseOMRSTopicConnector;
    }

    public void setEnterpriseOMRSTopicConnector(OMRSTopicConnector enterpriseOMRSTopicConnector) {
        this.enterpriseOMRSTopicConnector = enterpriseOMRSTopicConnector;
    }
}
