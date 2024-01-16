/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;
import org.odpi.openmetadata.engineservices.surveyaction.handlers.SurveyActionEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * SurveyActionInstanceHandler retrieves information from the instance map for the
 * survey action engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SurveyActionAdmin class.
 */
class SurveyActionInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    SurveyActionInstanceHandler()
    {
        super(EngineServiceDescription.SURVEY_ACTION_OMES.getEngineServiceName());

        SurveyActionRegistration.registerEngineService();
    }


    /**
     * Retrieve the specific handler for the survey action engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param surveyActionEngineName unique name of the survey action engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    SurveyActionEngineHandler getSurveyActionEngineHandler(String userId,
                                                           String serverName,
                                                           String surveyActionEngineName,
                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        SurveyActionInstance instance = (SurveyActionInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSurveyActionEngine(surveyActionEngineName);
        }

        return null;
    }
}
