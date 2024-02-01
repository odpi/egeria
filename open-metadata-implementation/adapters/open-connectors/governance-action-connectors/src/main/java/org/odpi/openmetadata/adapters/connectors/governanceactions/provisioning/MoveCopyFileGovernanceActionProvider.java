/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MoveCopyFileGovernanceActionProvider is the OCF connector provider for the Move or Copy File Provisioning Governance Action Service.
 * This is a Provisioning Governance Action Service.
 */
public class MoveCopyFileGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "e2a14ca8-57b1-48d7-9cc4-d0b44983ca79";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Provisioning:MoveCopyFile";
    private static final String  connectorTypeDisplayName = "Move or Copy File Governance Action Service";
    private static final String  connectorTypeDescription = "Provisioning Governance Action Service that moves or copies files on request.";

    static final String TARGET_FILE_NAME_PATTERN_PROPERTY                          = "targetFileNamePattern";

    static final String NO_LINEAGE_PROPERTY                                        = "noLineage";
    static final String TOP_LEVEL_PROCESS_NAME_PROPERTY                            = "topLevelProcessQualifiedName";
    static final String TOP_LEVEL_PROCESS_TEMPLATE_NAME_PROPERTY                   = "topLevelProcessTemplateQualifiedName";
    static final String DESTINATION_TEMPLATE_NAME_PROPERTY                         = "destinationFileTemplateQualifiedName";
    static final String TOP_LEVEL_PROCESS_ONLY_LINEAGE_PROPERTY                    = "topLevelProcessLineageOnly";
    static final String LINEAGE_TO_DESTINATION_FOLDER_ONLY_PROPERTY                = "lineageToDestinationFolderOnly";
    static final String LINEAGE_FROM_SOURCE_FOLDER_ONLY_PROPERTY                   = "lineageFromSourceFolderOnly";

    static final String COPY_REQUEST_TYPE   = "copy-file";
    static final String MOVE_REQUEST_TYPE   = "move-file";
    static final String DELETE_REQUEST_TYPE = "delete-file";

    static final String SOURCE_FILE_PROPERTY        = "sourceFile";
    static final String DESTINATION_FOLDER_PROPERTY = "destinationFolder";
    static final String NEW_ASSET_GUID_PROPERTY     = "newAssetGUID";

    static final String PROVISIONING_COMPLETE_GUARD             = "provisioning-complete";
    static final String PROVISIONING_FAILED_NO_FILE_NAMES_GUARD = "provisioning-failed-no-file-names";
    static final String PROVISIONING_FAILED_EXCEPTION_GUARD     = "provisioning-failed-exception";

    private static final String connectorClassName = MoveCopyFileGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public MoveCopyFileGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = new ArrayList<>();
        supportedRequestTypes.add(COPY_REQUEST_TYPE);
        supportedRequestTypes.add(MOVE_REQUEST_TYPE);
        supportedRequestTypes.add(DELETE_REQUEST_TYPE);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(SOURCE_FILE_PROPERTY);
        supportedRequestParameters.add(DESTINATION_FOLDER_PROPERTY);
        supportedRequestParameters.add(DESTINATION_TEMPLATE_NAME_PROPERTY);
        supportedRequestParameters.add(TARGET_FILE_NAME_PATTERN_PROPERTY);
        supportedRequestParameters.add(NO_LINEAGE_PROPERTY);
        supportedRequestParameters.add(TOP_LEVEL_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(TOP_LEVEL_PROCESS_TEMPLATE_NAME_PROPERTY);
        supportedRequestParameters.add(TOP_LEVEL_PROCESS_ONLY_LINEAGE_PROPERTY);
        supportedRequestParameters.add(LINEAGE_FROM_SOURCE_FOLDER_ONLY_PROPERTY);
        supportedRequestParameters.add(LINEAGE_TO_DESTINATION_FOLDER_ONLY_PROPERTY);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(SOURCE_FILE_PROPERTY);
        supportedTargetActionNames.add(DESTINATION_FOLDER_PROPERTY);
        supportedTargetActionNames.add(NEW_ASSET_GUID_PROPERTY);

        actionTargetTypes = new HashMap<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(DeployedImplementationType.FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE.getDeployedImplementationType());

        super.actionTargetTypes.put(SOURCE_FILE_PROPERTY, actionTargetType);

        actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.actionTargetTypes.put(DESTINATION_FOLDER_PROPERTY, actionTargetType);

        actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(DeployedImplementationType.DATA_ASSET.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.DATA_ASSET.getDeployedImplementationType());

        super.actionTargetTypes.put(NEW_ASSET_GUID_PROPERTY, actionTargetType);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(PROVISIONING_COMPLETE_GUARD);
        supportedGuards.add(PROVISIONING_FAILED_NO_FILE_NAMES_GUARD);
        supportedGuards.add(PROVISIONING_FAILED_EXCEPTION_GUARD);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TARGET_FILE_NAME_PATTERN_PROPERTY);
        recognizedConfigurationProperties.add(NO_LINEAGE_PROPERTY);
        recognizedConfigurationProperties.add(TOP_LEVEL_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(TOP_LEVEL_PROCESS_TEMPLATE_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DESTINATION_TEMPLATE_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DESTINATION_FOLDER_PROPERTY);
        recognizedConfigurationProperties.add(TOP_LEVEL_PROCESS_ONLY_LINEAGE_PROPERTY);
        recognizedConfigurationProperties.add(LINEAGE_FROM_SOURCE_FOLDER_ONLY_PROPERTY);
        recognizedConfigurationProperties.add(LINEAGE_TO_DESTINATION_FOLDER_ONLY_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

    }
}
