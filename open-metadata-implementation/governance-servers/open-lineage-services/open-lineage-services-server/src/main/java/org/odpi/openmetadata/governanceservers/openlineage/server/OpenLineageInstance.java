/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstance;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.Map;

/**
 * OpenLineageInstance maintains the instance information needed to execute queries on a Open lineage server.
 */
public class OpenLineageInstance extends GovernanceServerServiceInstance {


    private Map<String, OpenLineageHandler> openLineageInstances;

    /**
     * Constructor where REST Services used.
     *
     * @param serverName              name of this server
     * @param serviceName             name of this service
     * @param auditLog                link to the repository responsible for servicing the REST calls.
     * @param localServerUserId       userId to use for local server initiated actions
     * @param maxPageSize             max number of results to return on single request.
     * @param accessServiceRootURL    URL root for server platform where the access service is running.
     * @param accessServiceServerName name of the server where the access service is running.
     * @param openLineageInstances    active discovery engines in this server.
     */
    public OpenLineageInstance(String serverName,
                               String serviceName,
                               OMRSAuditLog auditLog,
                               String localServerUserId,
                               int maxPageSize,
                               String accessServiceRootURL,
                               String accessServiceServerName,
                               Map<String, OpenLineageHandler> openLineageInstances) {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);
        this.openLineageInstances = openLineageInstances;
    }


    /**
     * Return the discovery engine instance requested on an open lineage services request.
     *
     * @param openLineageGUID unique identifier of the discovery engine
     * @return discovery engine instance.
     * @throws InvalidParameterException the discovery engine guid is not recognized
     */
    synchronized OpenLineageHandler getOpenLineage(String   openLineageGUID) throws InvalidParameterException
    {
        final String  methodName        = "getOpenLineage";
        final String  guidParameterName = "openLineageGUID";

        OpenLineageHandler instance = openLineageInstances.get(openLineageGUID);

        if (instance == null)
        {
            OpenLineageServerErrorCode errorCode    = OpenLineageServerErrorCode.UNKNOWN_DISCOVERY_ENGINE;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,
                    openLineageGUID);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    guidParameterName);
        }

        return instance;
    }
    /**
     * Shutdown the engines
     */
    @Override
    public void shutdown()
    {
        if (openLineageInstances != null)
        {
            for (OpenLineageHandler handler : openLineageInstances.values())
            {
                if (handler != null)
                {
                    handler.terminate();
                }
            }
        }

        super.shutdown();
    }
}
