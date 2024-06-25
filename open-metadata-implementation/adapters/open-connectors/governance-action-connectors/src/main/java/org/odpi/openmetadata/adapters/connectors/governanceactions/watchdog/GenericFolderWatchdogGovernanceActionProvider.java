/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;

/**
 * GenericFolderWatchdogGovernanceActionProvider is the OCF connector provider for the Generic Folder Watchdog Governance Action Service.
 * This is a Watchdog Governance Action Service.
 */
public class GenericFolderWatchdogGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "b4629f05-710c-492b-bc9c-6e3f89e002df";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Watchdog:GenericFolder";
    private static final String  connectorTypeDisplayName = "Generic Folder Watchdog Governance Action Service";
    private static final String  connectorTypeDescription = "Watchdog Governance Action Service that listens for events relating to file assets located in a particular folder.";

    /*
     * The folder to monitor can be passed as a GUID in the configuration properties or
     * request parameters.  Alternatively as an action target.
     */
    static final String FOLDER_TARGET_PROPERTY = "watchedFolder";
    static final String FOLDER_TARGET_PROPERTY_DESCRIPTION = "This action target identifies the folder asset to watch.";


    private static final String connectorClassName = GenericFolderWatchdogGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public GenericFolderWatchdogGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = GenericFolderRequestType.getRequestTypeTypes();

        supportedRequestParameters = GenericFolderRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(FOLDER_TARGET_PROPERTY);
        actionTargetType.setDescription(FOLDER_TARGET_PROPERTY_DESCRIPTION);
        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);

        producedGuards = GenericWatchdogGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.FOLDER_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.ACTION_TARGET_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.ACTION_TARGET_TWO_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.NEW_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.UPDATED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.DELETED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.CLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.RECLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericFolderRequestParameter.DECLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
