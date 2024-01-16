/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.surveyaction.api.SurveyActionAPI;
import org.odpi.openmetadata.engineservices.surveyaction.client.rest.SurveyActionRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * SurveyActionClient is a client-side library for calling a specific survey action engine with an engine host server.
 */
public class SurveyActionClient implements SurveyActionAPI
{
    private final String                  serverName;               /* Initialized in constructor */
    private final String                  serverPlatformRootURL;    /* Initialized in constructor */
    private final String                  surveyActionEngineName;      /* Initialized in constructor */
    private final SurveyActionRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the survey action engine is running.
     * @param serverName the name of the engine host server where the survey action engine is running
     * @param surveyActionEngineName the unique name of the survey action engine.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public SurveyActionClient(String serverPlatformRootURL,
                              String serverName,
                              String surveyActionEngineName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.surveyActionEngineName   = surveyActionEngineName;

        this.restClient = new SurveyActionRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the survey action engine is running.
     * @param serverName the name of the engine host server where the survey action engine is running
     * @param surveyActionEngineName the unique name of the survey action engine.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public SurveyActionClient(String serverPlatformRootURL,
                              String serverName,
                              String surveyActionEngineName,
                              String userId,
                              String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL  = serverPlatformRootURL;
        this.serverName             = serverName;
        this.surveyActionEngineName = surveyActionEngineName;

        this.restClient = new SurveyActionRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorReport validateConnector(String userId,
                                             String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "validateConnector";
        final String   nameParameter = "connectorProviderClassName";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/survey-action/users/{1}/validate-connector";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          userId,
                                                                                          connectorProviderClassName);

        return restResult.getConnectorReport();
    }
}
