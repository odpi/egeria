/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CommentMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LikeMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * CommentConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Comment bean.
 */
public class CommentConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public CommentConverter(EntityDetail         entity,
                            Relationship         relationship,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public Comment getBean()
    {
        final String  methodName = "getBean";

        Comment  bean = null;

        if (entity != null)
        {
            bean = new Comment();

            super.updateBean(bean);
            bean.setUser(entity.getCreatedBy());

            InstanceProperties instanceProperties;

            if (relationship != null)
            {
                instanceProperties = relationship.getProperties();

                if (instanceProperties != null)
                {
                    bean.setPublic(repositoryHelper.getBooleanProperty(serviceName,
                                                                       LikeMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));
                }
            }

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setCommentText(repositoryHelper.removeStringProperty(serviceName, CommentMapper.TEXT_PROPERTY_NAME, instanceProperties, methodName));
                bean.setCommentType(this.removeCommentTypeFromProperties(instanceProperties));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }



    /**
     * Retrieve the CommentType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return   enum value
     */
    private CommentType removeCommentTypeFromProperties(InstanceProperties   properties)
    {
        CommentType commentType = CommentType.STANDARD_COMMENT;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(CommentMapper.TYPE_PROPERTY_NAME);

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

                    default:
                        commentType = CommentType.OTHER;
                        break;
                }

                instancePropertiesMap.remove(CommentMapper.TYPE_PROPERTY_NAME);

                properties.setInstanceProperties(instancePropertiesMap);
            }
        }

        return commentType;
    }

}
