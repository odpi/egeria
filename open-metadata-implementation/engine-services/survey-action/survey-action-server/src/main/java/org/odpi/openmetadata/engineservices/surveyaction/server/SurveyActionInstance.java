/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.server;

import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * SurveyActionInstance maintains the instance information needed to execute requests on behalf of
 * a engine host server.
 */
public class SurveyActionInstance extends OMESServiceInstance
{
    /**
     * Constructor where REST Services used.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls.
     * @param localServerUserId userId to use for local server initiated actions
     * @param maxPageSize max number of results to return on single request.

     */
    public SurveyActionInstance(String     serverName,
                                 String    serviceName,
                                 AuditLog  auditLog,
                                 String    localServerUserId,
                                 int       maxPageSize)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);
    }
}
