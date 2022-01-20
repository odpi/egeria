/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.server;

import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstance;
import org.odpi.openmetadata.engineservices.archivemanager.ffdc.ArchiveManagerErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.archivemanager.handlers.ArchiveEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * ArchiveManagerInstance maintains the instance information needed to execute requests on behalf of
 * a engine host server.
 */
public class ArchiveManagerInstance extends OMESServiceInstance
{
    private Map<String, ArchiveEngineHandler> archiveEngineInstances;


    /**
     * Constructor where REST Services used.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls.
     * @param localServerUserId userId to use for local server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param accessServiceRootURL URL root for server platform where the access service is running.
     * @param accessServiceServerName name of the server where the access service is running.
     * @param archiveEngineInstances active archive engines in this server.
     */
    public ArchiveManagerInstance(String                              serverName,
                                 String                              serviceName,
                                 AuditLog                            auditLog,
                                 String                              localServerUserId,
                                 int                                 maxPageSize,
                                 String                              accessServiceRootURL,
                                 String                              accessServiceServerName,
                                 Map<String, ArchiveEngineHandler> archiveEngineInstances)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);

        this.archiveEngineInstances = archiveEngineInstances;
    }



    /**
     * Return the archive engine instance requested on an archive engine services request.
     *
     * @param archiveEngineName unique name of the archive engine
     * @return archive engine instance.
     * @throws InvalidParameterException the archive engine name is not recognized
     */
    synchronized ArchiveEngineHandler getArchiveEngine(String   archiveEngineName) throws InvalidParameterException
    {
        final String  methodName        = "getArchiveEngine";
        final String  guidParameterName = "archiveEngineName";

        ArchiveEngineHandler instance = archiveEngineInstances.get(archiveEngineName);

        if (instance == null)
        {
            throw new InvalidParameterException(ArchiveManagerErrorCode.UNKNOWN_ARCHIVE_ENGINE.getMessageDefinition(serverName,
                                                                                                                     archiveEngineName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }

        return instance;
    }
}
