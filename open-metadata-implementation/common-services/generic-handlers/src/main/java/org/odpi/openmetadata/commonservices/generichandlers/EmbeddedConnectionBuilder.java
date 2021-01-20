/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

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
    private Map<String, Object>  arguments;
    private String               displayName;
    private int                  position;

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
        super(OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_GUID,
              OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME,
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
    public InstanceProperties getInstanceProperties(String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.POSITION_PROPERTY_NAME,
                                                                                  position,
                                                                                  methodName);

        if (arguments != null)
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.ARGUMENTS_PROPERTY_NAME,
                                                                   arguments,
                                                                   methodName);
        }

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        return properties;
    }
}
