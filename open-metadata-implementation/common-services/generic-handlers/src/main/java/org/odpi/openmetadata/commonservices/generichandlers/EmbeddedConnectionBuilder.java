/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * EmbeddedConnectionBuilder is able to build the properties for the relationship between
 * a connection and its embedded connections.
 */
public class EmbeddedConnectionBuilder extends OpenMetadataAPIGenericBuilder
{
    private final Map<String, Object>  arguments;
    private final String               displayName;
    private final int                  position;

    /**
     * Constructor when only the qualified name is known.
     *
     * @param position position of this embedded connection in the list of connections
     * @param arguments unique name
     * @param displayName display name for the embedded connection
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of this server instance
     */
    EmbeddedConnectionBuilder(int                  position,
                              Map<String, Object>  arguments,
                              String               displayName,
                              OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeGUID,
              OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName,
              null,
              InstanceStatus.ACTIVE,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.position = position;
        this.arguments = arguments;
        this.displayName = displayName;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               null,
                                                               OpenMetadataProperty.POSITION.name,
                                                               position,
                                                               methodName);

        properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.ARGUMENTS.name,
                                                               arguments,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }
}
