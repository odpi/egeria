/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionGuard;

public class CocoClinicalTrialCertifyWeeklyMeasurementsProvider extends SurveyActionServiceProvider
{
    private static final String  connectorTypeGUID = "dd01ede5-ccfc-48db-8315-788b7de5b55f";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:CertifyHospital";
    private static final String  connectorTypeDisplayName = "Clinical Trial Certify Hospital Governance Action Service";
    private static final String  connectorTypeDescription = "Adds a certification to the Hospital organization so that its data can be included in a clinical trial.";

    private static final String connectorClassName = CocoClinicalTrialCertifyWeeklyMeasurementsService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialCertifyWeeklyMeasurementsProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = null;
        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getCertifyWeeklyMeasurementsActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ACTION_TARGETS, AnalysisStep.CHECK_ASSET, AnalysisStep.SCHEMA_VALIDATION, AnalysisStep.DATA_VALIDATION, AnalysisStep.PRODUCE_ACTIONS});

        producedGuards = SurveyActionGuard.getDataValidationSurveyGuardTypes();
        producedAnnotationTypes = CocoClinicalTrialsAnnotationType.getCertifyWeeklyMeasurementsSurveyAnnotationTypeTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;
    }
}
