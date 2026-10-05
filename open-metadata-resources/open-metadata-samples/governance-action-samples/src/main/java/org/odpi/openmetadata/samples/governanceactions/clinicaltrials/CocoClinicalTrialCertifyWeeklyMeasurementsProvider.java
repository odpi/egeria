/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionGuard;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.CocoOpenConnectorDefinition;

public class CocoClinicalTrialCertifyWeeklyMeasurementsProvider extends SurveyActionServiceProvider
{
    private static final String connectorClassName = CocoClinicalTrialCertifyWeeklyMeasurementsService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialCertifyWeeklyMeasurementsProvider()
    {
        super(CocoOpenConnectorDefinition.CERTIFY_WEEKLY_MEASUREMENTS, connectorClassName, null);

        supportedRequestParameters = null;
        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getCertifyWeeklyMeasurementsActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ACTION_TARGETS, AnalysisStep.CHECK_ASSET, AnalysisStep.SCHEMA_VALIDATION, AnalysisStep.DATA_VALIDATION, AnalysisStep.PRODUCE_ACTIONS});

        producedGuards = SurveyActionGuard.getDataValidationSurveyGuardTypes();
        producedGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getGuardType());
        producedAnnotationTypes = CocoClinicalTrialsAnnotationType.getCertifyWeeklyMeasurementsSurveyAnnotationTypeTypes();
    }
}
