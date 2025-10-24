/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.*;

import java.util.List;


/**
 * SurveyActionServiceProvider implements the base class for the connector provider for a survey action service.
 */
public abstract class SurveyActionServiceProvider extends GovernanceServiceProviderBase
{
    static
    {
        supportedAssetTypeName = DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName();
        supportedDeployedImplementationType = DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType();
    }

    protected List<AnalysisStepType>   supportedAnalysisSteps  = null;
    protected List<AnnotationTypeType> producedAnnotationTypes = null;


    /**
     * Base provider for all survey action services.
     */
    public SurveyActionServiceProvider()
    {
        super.supportedRequestParameters = SurveyRequestParameter.getRequestParameterTypes();
        super.producedGuards = SurveyActionGuard.getSimpleSurveyGuardTypes();
        super.producedActionTargetTypes = SurveyActionTarget.getActionTargetTypes();
    }


    /**
     * Return the analysis steps that the service supports.
     *
     * @return list of analysis steps
     */
    public List<AnalysisStepType> getSupportedAnalysisSteps()
    {
        return supportedAnalysisSteps;
    }


    /**
     * Return the supported annotation types.
     *
     * @return list of annotation types
     */
    public List<AnnotationTypeType> getProducedAnnotationTypes()
    {
        return producedAnnotationTypes;
    }
}
