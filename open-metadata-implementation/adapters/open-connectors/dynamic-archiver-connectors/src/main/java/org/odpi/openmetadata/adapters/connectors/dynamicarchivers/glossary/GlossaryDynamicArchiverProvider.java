/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.dynamicarchivers.glossary;


import org.odpi.openmetadata.adapters.connectors.dynamicarchivers.DynamicArchiveProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;

/**
 * GlossaryDynamicArchiverProvider is the OCF connector provider for the Glossary Dynamic Archiving Service.
 * This is an Archive Service as defined by the Archive Manager OMES.
 */
public class GlossaryDynamicArchiverProvider extends DynamicArchiveProvider
{
    private static final String  connectorTypeGUID          = "02cfb290-43cb-497c-928e-267bd3d69324";
    private static final String  connectorTypeQualifiedName = "Egeria:ArchiveService:Glossary";
    private static final String  connectorTypeDisplayName   = "Glossary Dynamic Archive Service";
    private static final String  connectorTypeDescription   = "Archive Service that writes a glossary to an archive as the glossary is developed.";


    private static final String connectorClassName = GlossaryDynamicArchiverConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation as well as declare the parameters supported by the archive service.
     */
    public GlossaryDynamicArchiverProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = DynamicGlossaryArchiveRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(DynamicGlossaryArchiveRequestParameter.GLOSSARY_NAME.getName());
        actionTargetType.setDescription(DynamicGlossaryArchiveRequestParameter.GLOSSARY_NAME.getDescription());
        actionTargetType.setTypeName(OpenMetadataType.GLOSSARY_TYPE_NAME);

        super.supportedActionTargetTypes.add(actionTargetType);

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

        if (recognizedConfigurationProperties == null)
        {
            recognizedConfigurationProperties = new ArrayList<>();
        }
        recognizedConfigurationProperties.add(DynamicGlossaryArchiveRequestParameter.GLOSSARY_NAME.getName());

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
