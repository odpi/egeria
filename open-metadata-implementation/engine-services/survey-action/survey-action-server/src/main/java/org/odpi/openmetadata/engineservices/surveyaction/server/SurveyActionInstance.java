/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.server;

import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstance;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.engineservices.surveyaction.handlers.SurveyActionEngineHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * SurveyActionInstance maintains the instance information needed to execute requests on behalf of
 * a engine host server.
 */
public class SurveyActionInstance extends OMESServiceInstance
{
    private final Map<String, SurveyActionEngineHandler> surveyActionEngineInstances;


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
     * @param surveyActionEngineInstances active survey action engines in this server.
     */
    public SurveyActionInstance(String                                  serverName,
                                 String                                 serviceName,
                                 AuditLog                               auditLog,
                                 String                                 localServerUserId,
                                 int                                    maxPageSize,
                                 String                                 accessServiceRootURL,
                                 String                                 accessServiceServerName,
                                 Map<String, SurveyActionEngineHandler> surveyActionEngineInstances)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);

        this.surveyActionEngineInstances = surveyActionEngineInstances;
    }



    /**
     * Return the survey action engine instance requested on an survey action engine services request.
     *
     * @param surveyActionEngineName unique name of the survey action engine
     * @return survey action engine instance.
     * @throws InvalidParameterException the survey action engine name is not recognized
     */
    synchronized SurveyActionEngineHandler getSurveyActionEngine(String   surveyActionEngineName) throws InvalidParameterException
    {
        final String  methodName        = "getSurveyActionEngine";
        final String  guidParameterName = "surveyActionEngineName";

        SurveyActionEngineHandler instance = surveyActionEngineInstances.get(surveyActionEngineName);

        if (instance == null)
        {
            throw new InvalidParameterException(SurveyActionErrorCode.UNKNOWN_SURVEY_ACTION_ENGINE.getMessageDefinition(serverName,
                                                                                                                        surveyActionEngineName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }

        return instance;
    }
}
