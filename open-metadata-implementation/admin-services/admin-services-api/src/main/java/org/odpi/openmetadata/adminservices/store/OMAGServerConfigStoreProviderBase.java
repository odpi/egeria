/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;

/**
 * The OMAGServerConfigStoreProviderBase provides a base class for the connector provider supporting the OMAG
 * server configuration stores.  OpenConnectorProviderBase, which does the creation of connector instances.
 * The subclasses of OMAGServerConfigStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the registry store connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class OMAGServerConfigStoreProviderBase extends OpenConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OMAGServerConfigStoreProviderBase()
    {
        super();
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of recognized configuration property names
     */
    public OMAGServerConfigStoreProviderBase(OpenConnectorDefinition openConnectorDescription,
                                             String                  connectorClassName,
                                             List<String>            recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);
    }
}
