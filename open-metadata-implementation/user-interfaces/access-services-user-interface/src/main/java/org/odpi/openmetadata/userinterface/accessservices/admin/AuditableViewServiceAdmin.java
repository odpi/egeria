/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.admin;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.accessservices.auditlog.ViewServiceAuditCode;
import org.odpi.openmetadata.userinterfaces.adminservices.ViewServiceAdmin;

/**
 * AuditableViewServiceAdmin is the interface that an view service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.  It is configured in the ViewServiceDescription enumeration.
 */
public abstract class AuditableViewServiceAdmin extends ViewServiceAdmin
{
    private final String          actionDescription = "initialize";

    @Override
    protected void auditServiceInitializing(){

        ViewServiceAuditCode auditCode = ViewServiceAuditCode.UNKNOWN_SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;
    }
    @Override
    protected void auditServiceInitializing(String serviceName){

        ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName,serviceName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;
    }
    @Override
    protected void auditServiceInitialized(String serviceName) {
        ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName,serviceName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
    @Override
    protected void auditServiceInitializingError(Exception  error) {
        ViewServiceAuditCode auditCode = ViewServiceAuditCode.SERVICE_INSTANCE_FAILURE;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(error.getMessage()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

    }

}
