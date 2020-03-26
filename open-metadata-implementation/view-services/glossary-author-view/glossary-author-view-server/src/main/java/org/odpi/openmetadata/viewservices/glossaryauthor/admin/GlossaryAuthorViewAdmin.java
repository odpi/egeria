/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.admin;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.admin.serviceinstances.GlossaryAuthorViewServicesInstance;
import org.odpi.openmetadata.viewservices.glossaryauthor.auditlog.GlossaryAuthorViewAuditCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * GlossaryAuthorViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Subject Area OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class GlossaryAuthorViewAdmin extends ViewServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(GlossaryAuthorViewAdmin.class);

    private ViewServiceConfig viewServiceConfig = null;
    private AuditLog          auditLog          = null;
    private String            serverUserName    = null;

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
    public void initialize(String serverName, ViewServiceConfig viewServiceConfigurationProperties, AuditLog auditLog, String serverUserName, int maxPageSize) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        final String methodName = actionDescription;
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid=" + serverUserName);
        }
        //TODO validate the configuration and when invalid, throw OMAGConfigurationErrorException

        auditLog.logMessage(actionDescription,
                           GlossaryAuthorViewAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

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
            writeAuditLogPassingErrorMessage(auditLog, actionDescription, GlossaryAuthorViewAuditCode.SERVICE_INITIALIZED, serverName);

            if (log.isDebugEnabled()) {
                log.debug("<== Method: " + methodName + ",userid=" + serverUserName);
            }
            // todo - not valid to use private exception from SubjectArea OMAS
        } catch (InvalidParameterException iae) {
            writeAuditLogPassingErrorMessage(auditLog, actionDescription, GlossaryAuthorViewAuditCode.SERVICE_INSTANCE_FAILURE, iae.getMessage());
            throw new OMAGConfigurationErrorException(iae.getReportedHTTPCode(), iae.getReportingClassName(), iae.getReportingActionDescription(), iae.getErrorMessage(), iae.getReportedSystemAction(), iae.getReportedUserAction());
        }
    }

    private void writeAuditLogPassingErrorMessage(AuditLog auditLog, String actionDescription, GlossaryAuthorViewAuditCode auditCode, String message) {
        auditLog.logMessage(actionDescription, auditCode.getMessageDefinition(message));
    }

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