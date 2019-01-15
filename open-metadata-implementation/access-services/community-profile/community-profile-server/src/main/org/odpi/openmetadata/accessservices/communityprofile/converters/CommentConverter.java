/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.CommentMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Comment;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentConverter generates an Comment bean from an Comment entity and its attachment to a Referenceable.
 */
public class CommentConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(CommentConverter.class);

    private List<Relationship>  acceptedAnswersRelationships;
    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param attachedCommentRelationship properties to convert
     * @param acceptedAnswersRelationships properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    CommentConverter(EntityDetail         entity,
                     Relationship         attachedCommentRelationship,
                     List<Relationship>   acceptedAnswersRelationships,
                     OMRSRepositoryHelper repositoryHelper,
                     String               componentName)
    {
        super(entity, attachedCommentRelationship, repositoryHelper, componentName);
        this.acceptedAnswersRelationships = acceptedAnswersRelationships;
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public Comment getBean()
    {
        final String methodName = "getBean";

        Comment  bean = new Comment();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * user identity properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, CommentMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setText(repositoryHelper.removeStringProperty(serviceName, CommentMapper.TEXT_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, CommentMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setUserId(relationship.getCreatedBy());
        }

        if (acceptedAnswersRelationships != null)
        {
            List<String>  answeredBy = new ArrayList<>();
            List<String>  answers = new ArrayList<>();
            String        myGUID = entity.getGUID();

            if (myGUID != null)
            {
                for (Relationship relationship : acceptedAnswersRelationships)
                {
                    if (relationship != null)
                    {
                        InstanceType instanceType = relationship.getType();

                        if (instanceType != null)
                        {
                            String answeredQuestionGUID = repositoryHelper.getEnd1EntityGUID(relationship);
                            String acceptedAnswerGUID   = repositoryHelper.getEnd2EntityGUID(relationship);

                            if (myGUID.equals(answeredQuestionGUID))
                            {
                                answeredBy.add(acceptedAnswerGUID);
                            }
                            else if (myGUID.equals(acceptedAnswerGUID))
                            {
                                answers.add(answeredQuestionGUID);
                            }
                        }
                    }
                }
            }

            if (! answeredBy.isEmpty())
            {
                bean.setAnsweredBy(answeredBy);
            }

            if (! answers.isEmpty())
            {
                bean.setAnswers(answers);
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
