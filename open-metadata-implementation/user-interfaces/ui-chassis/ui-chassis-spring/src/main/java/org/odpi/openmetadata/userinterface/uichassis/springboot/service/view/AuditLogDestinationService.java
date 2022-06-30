/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.view;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogDestination;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditLogDestinationService extends AuditLogDestination {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogDestinationService.class);

    @Override
    public void addLogRecord(AuditLogRecord logRecord) {

            if ((OMRSAuditLogRecordSeverity.ERROR.getName().equals(logRecord.getSeverity())) ||
                    (OMRSAuditLogRecordSeverity.EXCEPTION.getName().equals(logRecord.getSeverity())))
            {

                if (logRecord.getExceptionClassName() != null)
                {
                    Logger log = LoggerFactory.getLogger(logRecord.getExceptionClassName());
                    log.error( logRecord.getMessageText() );
                } else {
                    LOGGER.error(logRecord.getExceptionStackTrace());
                }
            }
            else
            {
                LOGGER.info(logRecord.getMessageText());
            }

    }
}
