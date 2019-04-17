/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.builders;

import org.odpi.openmetadata.accessservices.discoveryengine.mappers.DiscoveryEnginePropertiesMapper;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * DiscoveryEngineBuilder creates instance properties based on the bean properties
 * provided in the constructor.
 */
public class DiscoveryEngineBuilder extends ReferenceableBuilder
{
    private String displayName     = null;
    private String description     = null;
    private String typeDescription = null;
    private String version         = null;
    private String patchLevel      = null;
    private String source          = null;


    /**
     * Constructor when only the qualified name is known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryEngineBuilder(String               qualifiedName,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of discovery engine
     * @param description description of discovery engine
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryEngineBuilder(String               qualifiedName,
                                  String               displayName,
                                  String               description,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param typeDescription new description of the type of discovery engine.
     * @param version new version number for the discovery engine implementation.
     * @param patchLevel new patch level for the discovery engine implementation.
     * @param source new source description for the implementation of the discovery engine.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryEngineBuilder(String               qualifiedName,
                                  String               displayName,
                                  String               description,
                                  String               typeDescription,
                                  String               version,
                                  String               patchLevel,
                                  String               source,
                                  Map<String, String>  additionalProperties,
                                  Map<String, Object>  extendedProperties,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.typeDescription = typeDescription;
        this.version = version;
        this.patchLevel = patchLevel;
        this.source = source;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (typeDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.TYPE_DESCRIPTION_PROPERTY_NAME,
                                                                      typeDescription,
                                                                      methodName);
        }

        if (version != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.VERSION_PROPERTY_NAME,
                                                                      version,
                                                                      methodName);
        }

        if (patchLevel != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                      patchLevel,
                                                                      methodName);
        }

        if (source != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.SOURCE_PROPERTY_NAME,
                                                                      source,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryEnginePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        return properties;
    }
}
