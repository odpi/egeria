/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;

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
    private static final String  connectorTypeDescription   = "Provisioning Governance Action Service that moves, copies or deletes files on request.";

    static final String DEFAULT_TOP_LEVEL_PROCESS_NAME_PROPERTY                    = "Egeria:MoveCopyDeleteFileGovernanceActionService";

    static final String SOURCE_FILE_PROPERTY                    = "sourceFile";
    static final String SOURCE_FILE_PROPERTY_DESCRIPTION        = "The full path name of the source file.";
    static final String DESTINATION_FOLDER_PROPERTY             = "destinationFolder";
    static final String DESTINATION_FOLDER_PROPERTY_DESCRIPTION = "The full path name of the destination directory.";
    static final String NEW_ASSET_PROPERTY                      = "newAsset";
    static final String NEW_ASSET_PROPERTY_DESCRIPTION          = "This is the asset for the destination file.";

    private static final String connectorClassName = MoveCopyFileGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public MoveCopyFileGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = RequestType.getRequestTypeTypes();

        supportedRequestParameters = RequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();

        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(SOURCE_FILE_PROPERTY);
        actionTargetType.setDescription(SOURCE_FILE_PROPERTY_DESCRIPTION);
        actionTargetType.setTypeName(DeployedImplementationType.FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE.getDeployedImplementationType());

        supportedActionTargetTypes.add(actionTargetType);

        actionTargetType = new ActionTargetType();

        actionTargetType.setName(DESTINATION_FOLDER_PROPERTY);
        actionTargetType.setDescription(DESTINATION_FOLDER_PROPERTY_DESCRIPTION);
        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        supportedActionTargetTypes.add(actionTargetType);

        producedActionTargetTypes = new ArrayList<>();

        actionTargetType = new ActionTargetType();

        actionTargetType.setName(NEW_ASSET_PROPERTY);
        actionTargetType.setDescription(NEW_ASSET_PROPERTY_DESCRIPTION);
        actionTargetType.setTypeName(DeployedImplementationType.FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE.getDeployedImplementationType());

        super.producedActionTargetTypes.add(actionTargetType);


        producedGuards = MoveCopyFileGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(RequestParameter.TARGET_FILE_NAME_PATTERN.getName());
        recognizedConfigurationProperties.add(RequestParameter.NO_LINEAGE.getName());
        recognizedConfigurationProperties.add(RequestParameter.TOP_LEVEL_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(RequestParameter.TOP_LEVEL_PROCESS_TEMPLATE_NAME.getName());
        recognizedConfigurationProperties.add(RequestParameter.DESTINATION_TEMPLATE_NAME.getName());
        recognizedConfigurationProperties.add(RequestParameter.DESTINATION_DIRECTORY.getName());
        recognizedConfigurationProperties.add(RequestParameter.TOP_LEVEL_PROCESS_ONLY_LINEAGE.getName());
        recognizedConfigurationProperties.add(RequestParameter.LINEAGE_FROM_SOURCE_FOLDER_ONLY.getName());
        recognizedConfigurationProperties.add(RequestParameter.LINEAGE_TO_DESTINATION_FOLDER_ONLY.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
