/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;

import org.odpi.openmetadata.accessservices.assetconsumer.elements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.CommentType;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.OwnerType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * CommentConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from CommentProperties.
 */
public class CommentConverter<B> extends AssetConsumerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CommentConverter(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }



    /**
     * Extract the properties from the entity.  Each DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param metadataElement output bean
     * @param entity entity containing the properties
     * @param relationship optional relationship containing the properties
     */
    public void updateMetadataElement(MetadataElement metadataElement, EntityDetail entity, Relationship relationship)
    {
        metadataElement.setElementHeader(this.getMetadataElementHeader(entity));

        if (metadataElement instanceof CommentProperties)
        {
            CommentProperties bean = (CommentProperties) metadataElement;

            /*
             * The initial set of values come from the entity.
             */
            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
            bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
            bean.setCommentType(this.removeCommentTypeFromProperties(instanceProperties));
            bean.setCommentText(this.removeCommentText(instanceProperties));
            bean.setUser(entity.getCreatedBy());

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            bean.setTypeName(typeName);
            bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            if (relationship != null)
            {
                instanceProperties = new InstanceProperties(relationship.getProperties());

                bean.setPublic(this.getIsPublic(instanceProperties));
            }
        }
    }


    /**
     * Retrieve and delete the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return CommentType  enum value
     */
    private CommentType removeCommentTypeFromProperties(InstanceProperties   properties)
    {
        CommentType commentType = this.getCommentTypeFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.COMMENT_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return commentType;
    }


    /**
     * Retrieve the CommentType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return CommentType  enum value
     */
    private CommentType getCommentTypeFromProperties(InstanceProperties   properties)
    {
        CommentType commentType = CommentType.STANDARD_COMMENT;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            commentType = CommentType.STANDARD_COMMENT;
                            break;

                        case 1:
                            commentType = CommentType.QUESTION;
                            break;

                        case 2:
                            commentType = CommentType.ANSWER;
                            break;

                        case 3:
                            commentType = CommentType.SUGGESTION;
                            break;

                        case 4:
                            commentType = CommentType.USAGE_EXPERIENCE;
                            break;

                        case 99:
                            commentType = CommentType.OTHER;
                            break;
                    }
                }
            }
        }

        return commentType;
    }
}
