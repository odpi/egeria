/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * MoveCopyFileGovernanceActionProvider is the OCF connector provider for the Move or Copy File Provisioning Governance Action Service.
 * This is is a Provisioning Governance Service.
 */
public class MoveCopyFileGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "e2a14ca8-57b1-48d7-9cc4-d0b44983ca79";
    private static final String  connectorTypeName = "Move or Copy File Governance Action Service";
    private static final String  connectorTypeDescription = "Provisioning Governance Action Service that moves or copies files on request.";

    static final String PROVISION_UNCATALOGUED_FILES_CONFIGURATION_PROPERTY = "provisionUncataloguedFiles";
    static final String NO_LINEAGE_CONFIGURATION_PROPERTY                   = "createLineage";
    static final String LINEAGE_PROCESS_NAME_CONFIGURATION_PROPERTY         = "processName";
    static final String TARGET_FILE_NAME_PATTERN_CONFIGURATION_PROPERTY     = "targetFileNamePattern";

    static final String COPY_REQUEST_TYPE = "copy-file";
    static final String MOVE_REQUEST_TYPE = "move-file";

    static final String SOURCE_FILE_PARAMETER        = "source-file";
    static final String DESTINATION_FOLDER_PARAMETER = "destination-folder";

    static final String SOURCE_FILE_ACTION_TARGET        = "source-file";
    static final String DESTINATION_FOLDER_ACTION_TARGET = "destination-folder";

    static final String PROVISIONING_COMPLETE_GUARD = "provisioning-complete";
    static final String PROVISIONING_FAILED_GUARD   = "provisioning-failed";

    private static final String connectorClassName = MoveCopyFileGovernanceActionConnector.class.getName();

    private List<String> supportedRequestTypes       = new ArrayList<>();
    private List<String> supportedRequestParameters  = new ArrayList<>();
    private List<String> supportedTargetActionNames  = new ArrayList<>();
    private List<String> supportedGuards             = new ArrayList<>();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public MoveCopyFileGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes.add(COPY_REQUEST_TYPE);
        supportedRequestTypes.add(MOVE_REQUEST_TYPE);

        supportedRequestParameters.add(SOURCE_FILE_PARAMETER);
        supportedRequestParameters.add(DESTINATION_FOLDER_PARAMETER);

        supportedTargetActionNames.add(SOURCE_FILE_ACTION_TARGET);
        supportedTargetActionNames.add(DESTINATION_FOLDER_ACTION_TARGET);

        supportedGuards.add(PROVISIONING_COMPLETE_GUARD);
        supportedGuards.add(PROVISIONING_FAILED_GUARD);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(PROVISION_UNCATALOGUED_FILES_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(NO_LINEAGE_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(LINEAGE_PROCESS_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(TARGET_FILE_NAME_PATTERN_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }


    /**
     * The request types returned are those that affect the governance action service's behaviour.  Other request types may be used
     * to call the governance action service but they result in default behaviour.
     *
     * @return list of request types
     */
    @Override
    public List<String> supportedRequestTypes()
    {
        return supportedRequestTypes;
    }


    /**
     * The request parameters returned are used by the governance action service to control its behaviour.
     *
     * @return list of parameter names used if the connector is provisioning
     */
    @Override
    public List<String> supportedRequestParameters()
    {
        return supportedRequestParameters;
    }


    /**
     * The request source names returned are the request source names that affect the governance action service's behaviour.  Other request
     * source names may be used in a call the governance action service but they result in default behaviour.
     *
     * @return null since request sources are ignored
     */
    @Override
    public List<String> supportedRequestSourceNames()
    {
        return null;
    }


    /**
     * The action target names returned are those that affect the governance action service's behaviour.  Other action target names may be used
     * in a call the governance action service but they result in default behaviour.
     *
     * @return list of action target names with special meaning
     */
    @Override
    public List<String> supportedActionTargetNames()
    {
        return supportedTargetActionNames;
    }


    /**
     * The guards describe the output assessment from the governance action service.  The list returned is the complete list of
     * guards to expect from the governance action service.  They are used when defining governance action processes that choreograph
     * the execution of governance action services using the guards to determine the path in the process to take.
     *
     * @return list of guards
     */
    @Override
    public  List<String> supportedGuards()
    {
        return supportedGuards;
    }
}
