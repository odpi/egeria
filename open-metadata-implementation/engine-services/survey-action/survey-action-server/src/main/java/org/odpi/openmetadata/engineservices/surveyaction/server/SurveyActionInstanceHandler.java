/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;

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
}
