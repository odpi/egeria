/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;

/**
 * GenericFolderWatchdogGovernanceActionProvider is the OCF connector provider for the Generic Folder Watchdog Governance Action Service.
 * This is a Watchdog Governance Action Service.
 */
public class GenericFolderWatchdogGovernanceActionProvider extends GenericWatchdogGovernanceActionProvider
{
    private static final String  connectorTypeGUID = "b4629f05-710c-492b-bc9c-6e3f89e002df";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Watchdog:GenericFolder";
    private static final String  connectorTypeDisplayName = "Generic Folder Watchdog Governance Action Service";
    private static final String  connectorTypeDescription = "Watchdog Governance Action Service that listens for events relating to file assets located in a particular folder.";

    /*
     * The folder to monitor can be passed as a GUID in the configuration properties or
     * request parameters.  Alternatively as an action target.
     */
    static final String FOLDER_NAME_PROPERTY   = "folderName";
    static final String FOLDER_TARGET_PROPERTY = "folderTarget";

    /*
     * Does the asset need to be directly within the folder or can it be in a nested folder?
     */
    static final String DIRECT_REQUEST_TYPE   = "member-of-folder";
    static final String NESTED_REQUEST_TYPE   = "nested-in-folder";

    private static final String connectorClassName = GenericFolderWatchdogGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public GenericFolderWatchdogGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = new ArrayList<>();
        supportedRequestTypes.add(DIRECT_REQUEST_TYPE);
        supportedRequestTypes.add(NESTED_REQUEST_TYPE);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(CHANGED_PROPERTY_NAMES);
        supportedRequestParameters.add(ACTION_TARGET_NAME_PROPERTY);
        supportedRequestParameters.add(ACTION_TARGET_TWO_NAME_PROPERTY);
        supportedRequestParameters.add(FOLDER_NAME_PROPERTY);
        supportedRequestParameters.add(NEW_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(UPDATED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(DELETED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(CLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(RECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(DECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(FOLDER_TARGET_PROPERTY);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(MONITORING_COMPLETE);
        supportedGuards.add(MONITORING_FAILED);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(FOLDER_NAME_PROPERTY);
        recognizedConfigurationProperties.add(ACTION_TARGET_NAME_PROPERTY);
        recognizedConfigurationProperties.add(ACTION_TARGET_TWO_NAME_PROPERTY);
        recognizedConfigurationProperties.add(NEW_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(UPDATED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DELETED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(CLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(RECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
