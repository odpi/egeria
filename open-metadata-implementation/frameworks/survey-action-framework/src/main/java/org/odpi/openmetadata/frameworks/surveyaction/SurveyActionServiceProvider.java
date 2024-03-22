/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStepType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionGuard;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionTarget;

import java.util.ArrayList;
import java.util.List;


/**
 * SurveyActionServiceProvider implements the base class for the connector provider for a survey action service.
 */
public abstract class SurveyActionServiceProvider extends GovernanceServiceProviderBase
{
    static
    {
        supportedAssetTypeName = OpenMetadataType.SURVEY_ACTION_SERVICE.typeName;
    }

    protected List<AnalysisStepType>   supportedAnalysisSteps   = null;
    protected List<AnnotationTypeType> supportedAnnotationTypes = null;


    /**
     * Base provider for all survey action services.
     */
    public SurveyActionServiceProvider()
    {
        producedGuards = new ArrayList<>();
        producedGuards.add(SurveyActionGuard.SURVEY_COMPLETED.getGuardType());
        producedGuards.add(SurveyActionGuard.SURVEY_FAILED.getGuardType());

        producedActionTargetTypes = new ArrayList<>();
        producedActionTargetTypes.add(SurveyActionTarget.SURVEY_REPORT.getActionTargetType());
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
    public List<AnnotationTypeType> getSupportedAnnotationTypes()
    {
        return supportedAnnotationTypes;
    }
}
