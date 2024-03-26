/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

import java.util.ArrayList;

/**
 * FolderSurveyServiceProvider provides the connector provider for the Folder Survey Action Service
 */
public class FolderSurveyServiceProvider extends SurveyActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 670;

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String  connectorTypeGUID = "297ede10-a004-4aa6-9af3-55e400551531";
    private static final String  connectorTypeQualifiedName = "Egeria:SurveyActionService:FolderSurveyService";
    private static final String  connectorTypeName = "Folder Survey Action Service Connector";
    private static final String  connectorTypeDescription = "Connector supports the surveying of file in a directory (folder) and the directories beneath it.";
    private static final String  connectorWikiPage  = "https://egeria-project.org/connectors/survey-action/folder-survey-action-service/";

    /*
     * Class of the connector.
     */
    private static final String connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyService";

    static final String  FILE_TARGET_PROPERTY = "fileToSurvey";
    static final String  FILE_TARGET_PROPERTY_DESCRIPTION = "A FileFolder asset representing the directory on the file system to survey.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * survey action implementation.
     */
    public FolderSurveyServiceProvider()
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
        connectorType.setSupportedAssetTypeName(SurveyActionServiceProvider.supportedAssetTypeName);
        connectorType.setDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(FILE_TARGET_PROPERTY);
        actionTargetType.setDescription(FILE_TARGET_PROPERTY_DESCRIPTION);
        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);
        super.producedAnnotationTypes = SurveyFolderAnnotationType.getAnnotationTypeTypes();


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
