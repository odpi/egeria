/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;

/**
 * MoveCopyFileGovernanceActionProvider is the OCF connector provider for the Move or Copy File Provisioning Governance Action Service.
 * This is a Provisioning Governance Action Service.
 */
public class MoveCopyFileGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID          = "e2a14ca8-57b1-48d7-9cc4-d0b44983ca79";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Provisioning:MoveCopyDeleteFile";
    private static final String  connectorTypeDisplayName   = "Move, Copy or Delete File Governance Action Service";
    private static final String  connectorTypeDescription   = "Works with files.  The request type defines which action is taken.  " +
        "The request parameters define the source file and destination folder, along with lineage options";

    static final String DEFAULT_TOP_LEVEL_PROCESS_NAME_PROPERTY = "Egeria:MoveCopyDeleteFileGovernanceActionService";

    static final String SOURCE_FILE_PROPERTY                    = "sourceFile";
    static final String SOURCE_FILE_PROPERTY_DESCRIPTION        = "The full path name of the source file.";
    static final String DESTINATION_FOLDER_PROPERTY             = "destinationFolder";
    static final String DESTINATION_FOLDER_PROPERTY_DESCRIPTION = "The full path name of the destination directory.";

    private static final String connectorClassName = MoveCopyFileGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public MoveCopyFileGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = MoveCopyFileRequestType.getRequestTypeTypes();

        supportedRequestParameters = MoveCopyFileRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();

        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(SOURCE_FILE_PROPERTY);
        actionTargetType.setDescription(SOURCE_FILE_PROPERTY_DESCRIPTION);
        actionTargetType.setOpenMetadataTypeName(DeployedImplementationType.FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE.getDeployedImplementationType());

        supportedActionTargetTypes.add(actionTargetType);

        actionTargetType = new ActionTargetType();

        actionTargetType.setName(DESTINATION_FOLDER_PROPERTY);
        actionTargetType.setDescription(DESTINATION_FOLDER_PROPERTY_DESCRIPTION);
        actionTargetType.setOpenMetadataTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        supportedActionTargetTypes.add(actionTargetType);

        producedActionTargetTypes = new ArrayList<>();
        super.producedActionTargetTypes.add(ActionTarget.NEW_ASSET.getActionTargetType());

        producedGuards = MoveCopyFileGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.TARGET_FILE_NAME_PATTERN.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.NO_LINEAGE.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.TOP_LEVEL_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.TOP_LEVEL_PROCESS_TEMPLATE_NAME.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.DESTINATION_TEMPLATE_NAME.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.TOP_LEVEL_PROCESS_ONLY_LINEAGE.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.LINEAGE_FROM_SOURCE_FOLDER_ONLY.getName());
        recognizedConfigurationProperties.add(MoveCopyFileRequestParameter.LINEAGE_TO_DESTINATION_FOLDER_ONLY.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
