/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * VerifyFileFolderGovernanceActionProvider is the OCF connector provider for the Move or Copy File Provisioning Governance Action Service.
 * This is a Provisioning Governance Action Service.
 */
public class VerifyFileFolderGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "1fc4f300-5003-4b1e-8669-953ade4a3742";
    private static final String  connectorTypeQualifiedName = "Egeria:GeneralGovernanceActionService:Verification:VerifyFileFolder";
    private static final String  connectorTypeDisplayName = "Verify File Folder Governance Action Service";
    private static final String  connectorTypeDescription = "Verification Governance Action Service that checks whether a file asset is located in a particular folder.";

    static final String DIRECT_REQUEST_TYPE   = "member-of-folder";
    static final String NESTED_REQUEST_TYPE   = "nested-in-folder";

    static final String FOLDER_GUID_PROPERTY  = "folderGUID";
    static final String FILE_GUID_PROPERTY    = "fileGUID";

    static final String FOLDER_TARGET_PROPERTY = "folderGUID";
    static final String FILE_TARGET_PROPERTY   = "fileGUID";

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

        supportedRequestTypes = new ArrayList<>();
        supportedRequestTypes.add(DIRECT_REQUEST_TYPE);
        supportedRequestTypes.add(NESTED_REQUEST_TYPE);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(FOLDER_GUID_PROPERTY);
        supportedRequestParameters.add(FILE_GUID_PROPERTY);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(FOLDER_TARGET_PROPERTY);
        supportedTargetActionNames.add(FILE_TARGET_PROPERTY);

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

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(FOLDER_GUID_PROPERTY);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
