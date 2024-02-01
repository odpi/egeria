/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

import java.util.ArrayList;
import java.util.HashMap;

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

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public CSVSurveyServiceProvider()
    {
        super();

        super.setConnectorClassName(connectorClass);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(FILE_TARGET_PROPERTY);

        actionTargetTypes = new HashMap<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.actionTargetTypes.put(FILE_TARGET_PROPERTY, actionTargetType);

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorTypeName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
