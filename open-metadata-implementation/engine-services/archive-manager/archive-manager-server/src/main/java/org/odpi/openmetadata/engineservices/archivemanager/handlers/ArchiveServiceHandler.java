/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.engineservices.archivemanager.connector.ArchiveContext;
import org.odpi.openmetadata.engineservices.archivemanager.connector.ArchiveService;
import org.odpi.openmetadata.engineservices.archivemanager.ffdc.ArchiveManagerErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.archivemanager.ffdc.ArchiveManagerAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.Date;

/**
 * ArchiveServiceHandler provides the support to run a archive service.  A new instance is created for each request and it is assigned its
 * own thread.
 */
public class ArchiveServiceHandler extends GovernanceServiceHandler
{
    private ArchiveService archiveService;
    private ArchiveContext archiveContext;


    /**
     * Constructor sets up the key parameters for running the archive service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param archiveEngineProperties properties of the archive engine - used for message logging
     * @param archiveEngineGUID unique Identifier of the archive engine - used for message logging
     * @param engineHostUserId userId for making updates to the governance actions
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param governanceActionClient client for processing governance actions
     * @param requestType requestType - used for message logging
     * @param archiveServiceGUID name of this archive service - used for message logging
     * @param archiveServiceName name of this archive service - used for message logging
     * @param archiveServiceConnector connector that does the work
     * @param archiveContext context for the connector
     * @param auditLog destination for log messages
     */
    ArchiveServiceHandler(GovernanceEngineProperties archiveEngineProperties,
                          String                     archiveEngineGUID,
                          String                     engineHostUserId,
                          String                     governanceActionGUID,
                          GovernanceEngineClient     governanceActionClient,
                          String                     requestType,
                          String                     archiveServiceGUID,
                          String                     archiveServiceName,
                          Connector                  archiveServiceConnector,
                          ArchiveContext             archiveContext,
                          AuditLog                   auditLog) throws InvalidParameterException
    {
        super(archiveEngineProperties,
              archiveEngineGUID,
              engineHostUserId,
              governanceActionGUID,
              governanceActionClient,
              requestType,
              archiveServiceGUID,
              archiveServiceName,
              archiveServiceConnector,
              auditLog);


        this.requestType    = requestType;
        this.archiveContext = archiveContext;
        this.auditLog       = auditLog;

        try
        {
            this.archiveService = (ArchiveService) archiveServiceConnector;
        }
        catch (Exception error)
        {
            final String archiveServiceConnectorParameterName = "archiveServiceConnector";
            final String actionDescription = "Cast connector to ArchiveService";

            auditLog.logException(actionDescription,
                                  ArchiveManagerAuditCode.INVALID_ARCHIVE_SERVICE.getMessageDefinition(archiveServiceName,
                                                                                                        requestType,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                  error);
            throw new InvalidParameterException(ArchiveManagerErrorCode.INVALID_ARCHIVE_SERVICE.getMessageDefinition(archiveServiceName,
                                                                                                                      requestType,
                                                                                                                      error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                archiveServiceConnectorParameterName);
        }
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        Date startTime;
        Date endTime;

        final String actionDescription = "Maintain an archive";

        try
        {
            auditLog.logMessage(actionDescription,
                                ArchiveManagerAuditCode.ARCHIVE_SERVICE_STARTING.getMessageDefinition(governanceServiceName,
                                                                                                      requestType,
                                                                                                      governanceEngineProperties.getQualifiedName(),
                                                                                                      governanceEngineGUID));



            archiveService.setArchiveContext(archiveContext);
            archiveService.setArchiveServiceName(governanceServiceName);

            startTime = new Date();
            archiveService.start();
            endTime = new Date();

            CompletionStatus completionStatus = archiveContext.getCompletionStatus();

            if (completionStatus == null)
            {
                auditLog.logMessage(actionDescription,
                                    ArchiveManagerAuditCode.ARCHIVE_SERVICE_RETURNED.getMessageDefinition(governanceServiceName,
                                                                                                          requestType,
                                                                                                          Long.toString(endTime.getTime() - startTime.getTime())));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    ArchiveManagerAuditCode.ARCHIVE_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                          requestType,
                                                                                                          completionStatus.getName(),
                                                                                                          Long.toString(endTime.getTime() - startTime.getTime())));
                super.disconnect();
                archiveService.setArchiveContext(null);
            }
        }
        catch (Exception  error)
        {
            auditLog.logException(actionDescription,
                                  ArchiveManagerAuditCode.ARCHIVE_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                      error.getClass().getName(),
                                                                                                      requestType,
                                                                                                      governanceEngineProperties.getQualifiedName(),
                                                                                                      governanceEngineGUID,
                                                                                                      error.getMessage()),
                                  error.toString(),
                                  error);
            try
            {
                CompletionStatus completionStatus = archiveContext.getCompletionStatus();

                if (completionStatus == null)
                {
                    archiveContext.recordCompletionStatus(CompletionStatus.FAILED, null, null, null);
                    super.disconnect();
                }
            }
            catch (Exception statusError)
            {
                auditLog.logException(actionDescription,
                                      ArchiveManagerAuditCode.EXC_ON_ERROR_STATUS_UPDATE.getMessageDefinition(governanceEngineProperties.getDisplayName(),
                                                                                                                governanceServiceName,
                                                                                                                statusError.getClass().getName(),
                                                                                                                statusError.getMessage()),
                                      statusError.toString(),
                                      statusError);
            }
        }
    }
}
