/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFileAnnotationType;

import java.util.ArrayList;

/**
 * CSVSurveyServiceProvider provides the connector provider for the CSV Survey Action Service
 */
public class CSVSurveyServiceProvider extends SurveyActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 668;

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String  connectorTypeGUID = "2a844ac9-bb86-4765-9f3c-04df148c05a5";
    private static final String  connectorTypeQualifiedName = "Egeria:SurveyActionService:CSVFileSurveyService";
    private static final String  connectorTypeName = "CSV File Survey Action Service Connector";
    private static final String  connectorTypeDescription = "Connector supports the schema extraction and profiling of data in a CSV file.";
    private static final String  connectorWikiPage  = "https://egeria-project.org/connectors/survey-action/csv-survey-action-service/";

    /*
     * Class of the connector.
     */
    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyService";


    static final String  FILE_TARGET_PROPERTY = "fileToSurvey";
    static final String  FILE_TARGET_PROPERTY_DESCRIPTION = "The CSVFile asset that describes the physical file to survey";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public CSVSurveyServiceProvider()
    {
        super();

        super.setConnectorClassName(connectorClass);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;



        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(FILE_TARGET_PROPERTY);
        actionTargetType.setDescription(FILE_TARGET_PROPERTY_DESCRIPTION);
        actionTargetType.setTypeName(DeployedImplementationType.CSV_FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.CSV_FILE.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_FILE});

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorTypeName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.MEASURE_RESOURCE, AnalysisStep.SCHEMA_EXTRACTION, AnalysisStep.PROFILE_DATA});
        super.producedAnnotationTypes    = SurveyFileAnnotationType.getCSVSurveyAnnotationTypeTypes();
    }
}
