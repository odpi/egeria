/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.admin;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.admin.serviceinstances.GlossaryAuthorViewServicesInstance;
import org.odpi.openmetadata.viewservices.glossaryauthor.auditlog.GlossaryAuthorViewAuditCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * GlossaryAuthorViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Subject Area OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class GlossaryAuthorViewAdmin extends ViewServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(GlossaryAuthorViewAdmin.class);

    private ViewServiceConfig viewServiceConfig = null;
    private OMRSAuditLog auditLog = null;
    private String serverUserName = null;

    private GlossaryAuthorViewServicesInstance instance = null;
    private String serverName = null;

    /**
     * Default constructor
     */
    public GlossaryAuthorViewAdmin() {
    }

    /**
     * Initialize the subject area access service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfigurationProperties specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use to issue calls to the remote server.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(String serverName, ViewServiceConfig viewServiceConfigurationProperties, OMRSAuditLog auditLog, String serverUserName, int maxPageSize) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        final String methodName = actionDescription;
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid=" + serverUserName);
        }
        //TODO validate the configuration and when invalid, throw OMAGConfigurationErrorException

        GlossaryAuthorViewAuditCode auditCode = GlossaryAuthorViewAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        try {
            this.viewServiceConfig = viewServiceConfigurationProperties;
            this.auditLog = auditLog;
            this.serverUserName = serverUserName;
            this.serverName = serverName;
            this.instance = new GlossaryAuthorViewServicesInstance(this.serverName,
                                                                   auditLog,
                                                                   serverUserName,
                                                                   maxPageSize,
                                                                   this.viewServiceConfig.getOMAGServerName(),
                                                                   this.viewServiceConfig.getOMAGServerPlatformRootURL());
            auditCode = GlossaryAuthorViewAuditCode.SERVICE_INITIALIZED;
            writeAuditLogPassingErrorMessage(auditLog, actionDescription, auditCode, serverName);

            if (log.isDebugEnabled()) {
                log.debug("<== Method: " + methodName + ",userid=" + serverUserName);
            }
        } catch (InvalidParameterException iae) {
            auditCode = GlossaryAuthorViewAuditCode.SERVICE_INSTANCE_FAILURE;
            writeAuditLogPassingErrorMessage(auditLog, actionDescription, auditCode, iae.getMessage());
            throw new OMAGConfigurationErrorException(iae.getReportedHTTPCode(), iae.getReportingClassName(), iae.getReportingActionDescription(), iae.getErrorMessage(), iae.getReportedSystemAction(), iae.getReportedUserAction());
        }
    }

    private void writeAuditLogPassingErrorMessage(OMRSAuditLog auditLog, String actionDescription, GlossaryAuthorViewAuditCode auditCode, String message) {
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),

                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(message),
                           null,
                           auditCode.getSystemAction(),

                           auditCode.getUserAction());
    }

//    private String extractRemoteServerURL(Map<String, Object> viewServiceOptions) {
//        return (String)viewServiceOptions.get(remoteServerURL);
//    }
//
//    private String extractRemoteServerName(Map<String, Object> viewServiceOptions) {
//        return (String)viewServiceOptions.get(remoteServerName);
//    }

    /**
     * Shutdown the subject area view service.
     */
    @Override
    public void shutdown() {
        final String actionDescription = "shutdown";

        log.debug(">>" + actionDescription);

        GlossaryAuthorViewAuditCode auditCode;

        auditCode = GlossaryAuthorViewAuditCode.SERVICE_TERMINATING;
        writeAuditLogPassingErrorMessage(auditLog, actionDescription, auditCode, serverName);

        if (instance != null) {
            this.instance.shutdown();
        }

        auditCode = GlossaryAuthorViewAuditCode.SERVICE_SHUTDOWN;
        writeAuditLogPassingErrorMessage(auditLog, actionDescription, auditCode, serverName);

        log.debug("<<" + actionDescription);
    }
}