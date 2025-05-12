/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CommentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * CommentConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Comment bean.
 */
public class CommentConverter<B> extends OMFConverter<B>
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
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof Comment bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataType.COMMENT.typeName, methodName);

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
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                if (relationship != null)
                {
                    instanceProperties = new InstanceProperties(relationship.getProperties());

                    bean.setIsPublic(this.getIsPublic(instanceProperties));
                }

                /*
                 * This cast should not fail due to the checking we have done above, but ClassCastException is caught just in case.
                 */
                return returnBean;
            }
            else
            {
                super.handleUnexpectedBeanClass(beanClass.getName(), Comment.class.getName(), methodName);
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Retrieve and delete the CommentType enum property from the instance properties of an entity
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
                instancePropertiesMap.remove(OpenMetadataProperty.COMMENT_TYPE.name);
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
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataProperty.COMMENT_TYPE.name);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    commentType = switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> CommentType.STANDARD_COMMENT;
                        case 1 -> CommentType.QUESTION;
                        case 2 -> CommentType.ANSWER;
                        case 3 -> CommentType.SUGGESTION;
                        case 4 -> CommentType.USAGE_EXPERIENCE;
                        case 5 -> CommentType.REQUIREMENT;
                        case 99 -> CommentType.OTHER;
                        default -> commentType;
                    };
                }
            }
        }

        return commentType;
    }
}
