/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;


import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * VerifyAssetGovernanceActionProvider is the OCF connector provider for the "Verify Asset" Governance Action Service.
 * This is a Verification Governance Action Service.
 */
public class VerifyAssetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "26df5406-8c4b-4738-96f6-1aea5dfd71de";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Verification:VerifyAsset";
    private static final String  connectorTypeDisplayName = "Move or Copy File Governance Action Service";
    private static final String  connectorTypeDescription = "Provisioning Governance Action Service that moves or copies files on request.";

    static final String PROVISION_UNCATALOGUED_FILES_CONFIGURATION_PROPERTY       = "provisionUncataloguedFiles";
    static final String NO_LINEAGE_CONFIGURATION_PROPERTY                         = "noLineage";
    static final String LINEAGE_PROCESS_NAME_CONFIGURATION_PROPERTY               = "processName";
    static final String TOP_LEVEL_PROCESS_ONLY_CONFIGURATION_PROPERTY             = "topLevelProcessOnly";
    static final String LINEAGE_TO_DESTINATION_FOLDER_ONLY_CONFIGURATION_PROPERTY = "lineageToDestinationFolderOnly";
    static final String TARGET_FILE_NAME_PATTERN_CONFIGURATION_PROPERTY           = "targetFileNamePattern";

    static final String NEW_REQUEST_TYPE = "new-asset";
    static final String MOVE_REQUEST_TYPE = "move-file";
    static final String DELETE_REQUEST_TYPE = "delete-file";

    static final String SOURCE_FILE_PARAMETER        = "source-file";
    static final String DESTINATION_FOLDER_PARAMETER = "destination-folder";

    static final String SOURCE_FILE_ACTION_TARGET        = "source-file";
    static final String DESTINATION_FOLDER_ACTION_TARGET = "destination-folder";

    static final String PROVISIONING_COMPLETE_GUARD = "provisioning-complete";
    static final String PROVISIONING_FAILED_GUARD   = "provisioning-failed";

    private static final String connectorClassName = MoveCopyFileGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public VerifyAssetGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = new ArrayList<>();
        supportedRequestTypes.add(NEW_REQUEST_TYPE);
        supportedRequestTypes.add(MOVE_REQUEST_TYPE);
        supportedRequestTypes.add(DELETE_REQUEST_TYPE);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(SOURCE_FILE_PARAMETER);
        supportedRequestParameters.add(DESTINATION_FOLDER_PARAMETER);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(SOURCE_FILE_ACTION_TARGET);
        supportedTargetActionNames.add(DESTINATION_FOLDER_ACTION_TARGET);

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
        recognizedConfigurationProperties.add(PROVISION_UNCATALOGUED_FILES_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(NO_LINEAGE_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(LINEAGE_PROCESS_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(TOP_LEVEL_PROCESS_ONLY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(LINEAGE_TO_DESTINATION_FOLDER_ONLY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(TARGET_FILE_NAME_PATTERN_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
