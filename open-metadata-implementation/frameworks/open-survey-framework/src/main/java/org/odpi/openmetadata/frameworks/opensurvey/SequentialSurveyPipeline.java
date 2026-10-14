/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * SequentialSurveyPipeline is a survey action pipeline that provides an inline sequential invocation of the
 * supplied survey action services.
 */
public class SequentialSurveyPipeline extends SurveyActionPipelineConnector
{
    /**
     * This implementation provides an inline sequential invocation of the supplied survey action services.
     *
     * @throws ConnectorCheckedException a problem within the survey action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    protected void runSurveyPipeline() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        for (SurveyActionServiceConnector embeddedSurveyService : super.embeddedSurveyActionServices)
        {
            if (embeddedSurveyService != null)
            {
                embeddedSurveyService.setSurveyContext(super.surveyContext);
                embeddedSurveyService.start();
                embeddedSurveyService.disconnect();

                /*
                 * The embedded services share the survey context, and so the finalAnalysisStep request parameter.
                 * Once one of them completes the final analysis step, the rest of the pipeline is not run.
                 */
                if (embeddedSurveyService.hasReachedFinalAnalysisStep())
                {
                    break;
                }
            }
        }
    }
}
