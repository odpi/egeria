/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CommentMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * SchemaAttributeBuilder creates repository entities and relationships from properties for a schema attribute.
 */
public class SchemaAttributeBuilder extends ReferenceableBuilder
{
    private String attributeName;
    private int    elementPosition;
    private String cardinality;
    private String defaultValueOverride;
    private String anchorGUID;


    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param attributeName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               attributeName,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.attributeName = attributeName;
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param attributeName new value for the display name.
     * @param elementPosition position of the attribute in the parent schemaType.
     * @param cardinality does the attribute repeat?
     * @param defaultValueOverride override for the Type's default value.
     * @param additionalProperties additional properties
     * @param anchorGUID unique identifier of the anchor entity
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               attributeName,
                                  int                  elementPosition,
                                  String               cardinality,
                                  String               defaultValueOverride,
                                  String               anchorGUID,
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

        this.attributeName = attributeName;
        this.elementPosition = elementPosition;
        this.cardinality = cardinality;
        this.defaultValueOverride = defaultValueOverride;
        this.anchorGUID = anchorGUID;
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

        if (attributeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.ATTRIBUTE_NAME_PROPERTY_NAME,
                                                                      attributeName,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.ELEMENT_POSITION_PROPERTY_NAME,
                                                               elementPosition,
                                                               methodName);

        if (cardinality != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.CARDINALITY_PROPERTY_NAME,
                                                                      cardinality,
                                                                      methodName);
        }

        if (defaultValueOverride != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
                                                                      defaultValueOverride,
                                                                      methodName);
        }

        if (anchorGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                      anchorGUID,
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
    @Override
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (attributeName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(attributeName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      literalName,
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
    @Override
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return super.getNameInstanceProperties(methodName);
    }

}
