/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum AssetLineageAuditCode {

    SERVICE_INITIALIZING("OMAS-ASSET-LINEAGE-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Asset Lineage Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Asset Lineage OMAS.",
            "No action is required.  This is part of the normal operation of the server."),


    SERVICE_INITIALIZED("OMAS-ASSET-LINEAGE-0005",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Asset Lineage Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Asset Lineage OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-ASSET-LINEAGE-0006",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Asset Lineage Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Asset Lineage OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),


    SERVICE_INSTANCE_FAILURE("OMAS-ASSET-LINEAGE-0009",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Asset Lineage Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    ;

    private static final Logger log = LoggerFactory.getLogger(AssetLineageAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    AssetLineageAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
        this.logMessageId = logMessageId;
        this.severity = severity;
        this.logMessage = logMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    public String getLogMessageId() {
        return logMessageId;
    }

    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public String getUserAction() {
        return userAction;
    }

    public String getFormattedLogMessage(String... params) {
        log.debug(String.format("<== OMRS Audit Code.getMessage(%s)", Arrays.toString(params)));

        String result = MessageFormat.format(logMessage, params);

        log.debug(String.format("==> OMRS Audit Code.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }

}
