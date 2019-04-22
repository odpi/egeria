/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * OMRSTopicProvider provides implementation of the connector provider for the OMRSTopicConnector.
 */
public abstract class OpenMetadataTopicProvider extends ConnectorProviderBase
{
    protected static final String  sleepTimeProperty = "sleepTime";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public OpenMetadataTopicProvider()
    {
        Class        connectorClass = OpenMetadataTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}