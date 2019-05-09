/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectionMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * EmbeddedConnectionBuilder is able to build the properties for the relationship between
 * a connection and its embedded connections.
 */
public class EmbeddedConnectionBuilder
{
    private Map<String, Object>  arguments;
    private OMRSRepositoryHelper repositoryHelper;
    private String               displayName;
    private String               serviceName;

    /**
     * Constructor when only the qualified name is known.
     *
     * @param arguments unique name
     * @param displayName display name for the embedded connection
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     */
    public EmbeddedConnectionBuilder(Map<String, Object>  arguments,
                                     String               displayName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName)
    {
        this.arguments = arguments;
        this.displayName = displayName;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (arguments != null)
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   null,
                                                                   ConnectionMapper.ARGUMENTS_PROPERTY_NAME,
                                                                   arguments,
                                                                   methodName);
        }

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        return properties;
    }
}
