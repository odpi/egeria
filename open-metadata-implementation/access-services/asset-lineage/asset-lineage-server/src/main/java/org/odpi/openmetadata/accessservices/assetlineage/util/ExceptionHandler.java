/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class ExceptionHandler {

    private OMRSAuditLog auditLog;

    public ExceptionHandler(OMRSAuditLog auditLog){
        this.auditLog = auditLog;
    }

    public void captureOMRSCheckedExceptionBase(AssetContext response, OMRSCheckedExceptionBase e) {

        String actionDescription = "Retrieve asset context from metadata repository";
        AssetLineageErrorCode auditCode = AssetLineageErrorCode.PUBLISH_EVENT_EXCEPTION;

        auditLog.logException(actionDescription,
                auditCode.getErrorMessageId(),
                OMRSAuditLogRecordSeverity.EXCEPTION,
                auditCode.getFormattedErrorMessage(e.getErrorMessage()),
                "event {" + response.toString() + "}",
                e.getReportedSystemAction(),
                e.getReportedUserAction(),
                e);
    }

    public void captureAssetLineageExeption(AssetContext response, AssetLineageException e) {
        String actionDescription = "Retrieve asset context from metadata repository";
        AssetLineageErrorCode auditCode = AssetLineageErrorCode.PUBLISH_EVENT_EXCEPTION;

        auditLog.logException(actionDescription,
                auditCode.getErrorMessageId(),
                OMRSAuditLogRecordSeverity.EXCEPTION,
                auditCode.getFormattedErrorMessage(e.getErrorMessage()),
                "event {" + response.toString() + "}",
                e.getReportedSystemAction(),
                e.getReportedUserAction(),
                e);
    }

}
