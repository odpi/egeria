/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * MoveCopyFileGovernanceActionProvider is the OCF connector provider for the Move or Copy File Provisioning Governance Action Service.
 * This is a Provisioning Governance Action Service.
 */
public class MoveCopyFileGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "e2a14ca8-57b1-48d7-9cc4-d0b44983ca79";
    private static final String  connectorTypeQualifiedName = "Egeria:GeneralGovernanceActionService:Provisioning:MoveCopyFile";
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

    static final String PROVISIONING_COMPLETE_GUARD = "provisioning-complete";
    static final String PROVISIONING_FAILED_GUARD   = "provisioning-failed";

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

        supportedGuards = new ArrayList<>();
        supportedGuards.add(PROVISIONING_COMPLETE_GUARD);
        supportedGuards.add(PROVISIONING_FAILED_GUARD);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

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
