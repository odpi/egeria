/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import java.util.List;


/**
 * OpenMetadataTopicProvider provides implementation of the connector provider for the OpenMetadataTopicConnector.  This connector provides
 * a generic interface for sending and receiving string-based events.
 */
public abstract class OpenMetadataTopicProvider extends OpenConnectorProviderBase
{
    public static final String EVENT_DIRECTION_PROPERTY_NAME = "eventDirection";
    public static final String EVENT_DIRECTION_INOUT         = "inOut";
    public static final String EVENT_DIRECTION_OUT_ONLY      = "outOnly";
    public static final String EVENT_DIRECTION_IN_ONLY       = "inOnly";


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of property names that the connector supports
     *                                             in the configuration properties.
     */
    public OpenMetadataTopicProvider(OpenConnectorDefinition openConnectorDescription,
                                     String                  connectorClassName,
                                     List<String>            recognizedConfigurationPropertyNames)
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
     * @param connectorInterfaces                  list of interfaces that the connector supports.
     * @param expectedDataFormat                   description of the data format that the connector expects.
     */
    public OpenMetadataTopicProvider(OpenConnectorDefinition openConnectorDescription,
                                     String                  connectorClassName,
                                     List<String>            recognizedConfigurationPropertyNames,
                                     List<String>            connectorInterfaces,
                                     String                  expectedDataFormat)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames, connectorInterfaces, expectedDataFormat);
    }
}