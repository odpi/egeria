/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.dynamicarchivers.glossary;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.dynamicarchivers.DynamicArchiveProvider;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * GlossaryDynamicArchiverProvider is the OCF connector provider for the Glossary Dynamic Archiving Service.
 * This is an Archive Service as defined by the Repository Governance OMES.
 */
public class GlossaryDynamicArchiverProvider extends DynamicArchiveProvider
{
    private static final String connectorClassName = GlossaryDynamicArchiverConnector.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation as well as declare the parameters supported by the archive service.
     */
    public GlossaryDynamicArchiverProvider()
    {
        super(EgeriaOpenConnectorDefinition.GLOSSARY_DYNAMIC_ARCHIVER_CONNECTOR,
              connectorClassName,
              List.of(DynamicGlossaryArchiveRequestParameter.GLOSSARY_NAME.getName()));


        supportedRequestParameters = DynamicGlossaryArchiveRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(DynamicGlossaryArchiveRequestParameter.GLOSSARY_NAME.getName());
        actionTargetType.setDescription(DynamicGlossaryArchiveRequestParameter.GLOSSARY_NAME.getDescription());
        actionTargetType.setOpenMetadataTypeName(OpenMetadataType.GLOSSARY.typeName);

        super.supportedActionTargetTypes.add(actionTargetType);
    }
}
