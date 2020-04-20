/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * SchemaLinkBuilder creates instance properties for a schema link.
 */
public class SchemaLinkBuilder extends ReferenceableBuilder
{
    private String              displayName;
    private String              description    = null;
    private boolean             isDeprecated   = false;
    private String              linkName       = null;
    private Map<String, String> linkProperties = null;

    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaLinkBuilder(String               qualifiedName,
                             String               displayName,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
    }



    /**
     * Constructor supporting all properties for a schema link element entity.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param isDeprecated is this element deprecated?
     * @param linkName name of implementation class for Java
     * @param linkProperties aliases for the field
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaLinkBuilder(String               qualifiedName,
                             String               displayName,
                             String               description,
                             boolean              isDeprecated,
                             String               linkName,
                             Map<String, String>  linkProperties,
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

        this.displayName           = displayName;
        this.description           = description;
        this.isDeprecated          = isDeprecated;
        this.linkName              = linkName;
        this.linkProperties        = linkProperties;
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

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        if (linkName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.LINK_NAME_PROPERTY_NAME,
                                                                      linkName,
                                                                      methodName);
        }

        if ((linkProperties != null) && (!linkProperties.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         SchemaElementMapper.LINK_PROPERTIES_PROPERTY_NAME,
                                                                         linkProperties,
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

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

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


    /**
     * Set up a property value for the CommentType enum property.
     *
     * @param properties   current properties
     * @param sortOrder  enum value
     * @param methodName   calling method
     *
     * @return  InstanceProperties object with the enum value added
     */
    private InstanceProperties addSortOrderPropertyToInstance(InstanceProperties  properties,
                                                              DataItemSortOrder   sortOrder,
                                                              String              methodName)
    {
        int                ordinal = 99;
        String             symbolicName = null;
        String             description = null;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Ascending";
        final String element1Description     = "Sort the data values so that they increase in value.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "Descending";
        final String element2Description     = "Sort the data values so that they decrease in value.";

        final int    element3Ordinal         = 99;
        final String element3Value           = "Ignore";
        final String element3Description     = "No specific sort order.";

        switch (sortOrder)
        {
            case ASCENDING:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case DESCENDING:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case UNSORTED:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          SchemaElementMapper.SORT_ORDER_PROPERTY_NAME,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }
}
