package org.odpi.openmetadata.adapters.repositoryservices.searchindexing.repositoryconnector;
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;

public class SearchIndexingOMRSTopicListener extends OMRSTopicListenerBase {

    public SearchIndexingOMRSTopicListener(String serviceName) {
        super(serviceName);
    }

    public SearchIndexingOMRSTopicListener(String serviceName, OMRSAuditLog auditLog) {
        super(serviceName, auditLog);
    }
}
