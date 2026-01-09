/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * DynamicOpenMetadataDataSetProviderBase provides a base class for a connector that supplies its own
 * product definition.  This means that its existence is discovered dynamically based on the content of
 * the open metadata repositories.  The identifier property value for the starting element is
 * supplied in the configuration properties.
 */
public abstract class DynamicOpenMetadataDataSetProviderBase extends OpenConnectorProviderBase
{
    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of property names that the connector supports
     *                                             in the configuration properties.
     */
    public DynamicOpenMetadataDataSetProviderBase(OpenConnectorDefinition openConnectorDescription,
                                                  String                  connectorClassName,
                                                  List<String> recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of property names that the connector supports
     *                                             in the configuration properties.
     */
    public DynamicOpenMetadataDataSetProviderBase(OpenConnectorDefinition openConnectorDescription,
                                                  String                  connectorClassName,
                                                  List<String>            recognizedConfigurationPropertyNames,
                                                  List<String>            connectorInterfaces,
                                                  String                  expectedDataFormat)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames, connectorInterfaces, expectedDataFormat);
    }
}
