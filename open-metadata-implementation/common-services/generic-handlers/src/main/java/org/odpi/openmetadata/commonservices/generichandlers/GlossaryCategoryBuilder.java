/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GlossaryCategoryBuilder creates the parts for an entity that represents a glossary category.
 */
public class GlossaryCategoryBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the glossary category
     * @param displayName short display name for the glossary category
     * @param description description of the glossary category
     * @param additionalProperties additional properties for a glossary category
     * @param extendedProperties  properties for a glossary category subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryCategoryBuilder(String               qualifiedName,
                            String               displayName,
                            String               description,
                            Map<String, String>  additionalProperties,
                            Map<String, Object>  extendedProperties,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the glossary category
     * @param displayName short display name for the glossary category
     * @param description description of the glossary category
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GlossaryCategoryBuilder(String        qualifiedName,
                            String               displayName,
                            String               description,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryCategoryBuilder(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);

        return properties;
    }
}
