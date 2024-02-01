/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * VerifyFileFolderGovernanceActionProvider is the OCF connector provider for the "Verify FileFolder"
 * Verification Governance Action Service.
 * This is a Verification Governance Action Service.
 */
public class VerifyFileFolderGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "1fc4f300-5003-4b1e-8669-953ade4a3742";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Verification:VerifyFileFolder";
    private static final String  connectorTypeDisplayName = "Verify File Folder Governance Action Service";
    private static final String  connectorTypeDescription = "Verification Governance Action Service that checks whether a file asset is located in a particular folder.";


    static final String ACTION_TARGET_NAME = "elementGUID";

    static final String FILE_MATCHED_GUARD        = "file-matched";
    static final String FILE_NOT_MATCHED_GUARD    = "file-not-matched";
    static final String VERIFICATION_FAILED_GUARD = "verification-failed";

    private static final String connectorClassName = VerifyFileFolderGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public VerifyFileFolderGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);


        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(ACTION_TARGET_NAME);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(ACTION_TARGET_NAME);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(FILE_MATCHED_GUARD);
        supportedGuards.add(FILE_NOT_MATCHED_GUARD);
        supportedGuards.add(VERIFICATION_FAILED_GUARD);

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
        recognizedConfigurationProperties.add(ACTION_TARGET_NAME);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

        actionTargetTypes = new HashMap<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.actionTargetTypes.put(ACTION_TARGET_NAME, actionTargetType);
    }
}
