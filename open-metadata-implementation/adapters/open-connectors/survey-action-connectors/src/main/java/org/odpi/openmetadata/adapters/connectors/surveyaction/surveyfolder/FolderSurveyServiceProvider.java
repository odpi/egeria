/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FolderSurveyServiceProvider provides the connector provider for the Folder Survey Action Service
 */
public class FolderSurveyServiceProvider extends SurveyActionServiceProvider
{
    static final String  connectorTypeGUID = "297ede10-a004-4aa6-9af3-55e400551531";
    static final String  connectorTypeName = "Folder Survey Action Service Connector";
    static final String  connectorTypeDescription = "Connector supports the surveying of file in a directory (folder) and the directories beneath it.";

    static final String  FILE_TARGET_PROPERTY = "fileToSurvey";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public FolderSurveyServiceProvider()
    {
        super();

        String   connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyService";

        super.setConnectorClassName(connectorClass);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(FILE_TARGET_PROPERTY);

        actionTargetTypes = new HashMap<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.actionTargetTypes.put(FILE_TARGET_PROPERTY, actionTargetType);
    }
}
