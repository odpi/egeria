/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ALOutTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(ALOutTopicListener.class);

    private final OMRSAuditLog auditLog;

    public ALOutTopicListener(OMRSAuditLog auditLog) {

        this.auditLog = auditLog;

    }

    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {

        log.info(eventAsString);
    }


}
