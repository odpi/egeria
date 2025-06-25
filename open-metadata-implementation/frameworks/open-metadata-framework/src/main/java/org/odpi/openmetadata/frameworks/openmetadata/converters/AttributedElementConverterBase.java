/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AttributedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * AttributedElementConverterBase provides common methods for transferring relevant properties from an Open Metadata Element
 * object into a bean that inherits from AttributedMetadataElement.
 */
public class AttributedElementConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AttributedElementConverterBase(PropertyHelper propertyHelper,
                                          String         serviceName,
                                          String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }



    /**
     * Summarize the relationships that have no special processing by the subtype.
     *
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @param processedRelationshipTypes list of relationships that have already been processed by the subtype
     * @throws PropertyServerException problem in converter
     */
    protected void addRelationshipsToBean(Class<B>                     beanClass,
                                          List<RelatedMetadataElement> relatedMetadataElements,
                                          List<String>                 processedRelationshipTypes,
                                          AttributedMetadataElement    attributedMetadataElement) throws PropertyServerException
    {
        final String methodName = "addRelationshipToBean";

        if (relatedMetadataElements != null)
        {
            /*
             * These are the relationships for the attributed element
             */
            List<RelatedMetadataElementSummary> externalReferences = new ArrayList<>();
            List<RelatedMetadataElementSummary> alsoKnownAs        = new ArrayList<>();
            List<RelatedMetadataElementSummary> memberOfCollection = new ArrayList<>();
            List<RelatedMetadataElementSummary> semanticAssignment = new ArrayList<>();
            List<RelatedMetadataElementSummary> attachedLikes      = new ArrayList<>();
            List<RelatedMetadataElementSummary> attachedTags       = new ArrayList<>();
            List<RelatedMetadataElementSummary> attachedKeywords   = new ArrayList<>();
            List<RelatedMetadataElementSummary> attachedComments   = new ArrayList<>();
            List<RelatedMetadataElementSummary> attachedReviews    = new ArrayList<>();
            List<RelatedMetadataElementSummary> others             = new ArrayList<>();

            /*
             * Step through the relationships processing those that relate directly to attributed elements
             */
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        externalReferences.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        alsoKnownAs.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName)) && (relatedMetadataElement.getElementAtEnd1()))
                    {
                        memberOfCollection.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        semanticAssignment.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        attachedLikes.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        attachedTags.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        attachedKeywords.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        attachedComments.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        attachedReviews.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else
                    {
                        others.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                }
            }

            if (! externalReferences.isEmpty())
            {
                attributedMetadataElement.setExternalReferences(externalReferences);
            }

            if (! alsoKnownAs.isEmpty())
            {
                attributedMetadataElement.setAlsoKnownAs(alsoKnownAs);
            }

            if (! memberOfCollection.isEmpty())
            {
                attributedMetadataElement.setMemberOfCollections(memberOfCollection);
            }

            if (! semanticAssignment.isEmpty())
            {
                attributedMetadataElement.setSemanticAssignments(semanticAssignment);
            }

            if (! attachedLikes.isEmpty())
            {
                attributedMetadataElement.setAttachedLikes(attachedLikes);
            }

            if (! attachedTags.isEmpty())
            {
                attributedMetadataElement.setAttachedTags(attachedTags);
            }

            if (! attachedKeywords.isEmpty())
            {
                attributedMetadataElement.setAttachedKeywords(attachedKeywords);
            }

            if (! attachedComments.isEmpty())
            {
                attributedMetadataElement.setAttachedComments(attachedComments);
            }

            if (! attachedReviews.isEmpty())
            {
                attributedMetadataElement.setAttachedReviews(attachedReviews);
            }

            if (! others.isEmpty())
            {
                /*
                 * All of the related elements processed by this class are stripped out of "others".  Since it is only extracting relationships in one direction,
                 * relationships in the other direction will be added to otherRelatedElements.
                 */
                attributedMetadataElement.setOtherRelatedElements(super.getOtherRelatedElements(beanClass, relatedMetadataElements, processedRelationshipTypes));
            }
        }
    }


    /**
     * Using the supplied relatedMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement relatedMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the relatedMetadataElement supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(relatedMetadataElement)";

        B returnBean = this.getNewBean(beanClass, relatedMetadataElement.getElement(), methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relatedMetadataElement, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an element and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>                 beanClass,
                        OpenMetadataElement      element,
                        OpenMetadataRelationship relationship,
                        String                   methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity, relationship)";

        B returnBean = this.getNewBean(beanClass, element, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relationship, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(beanClass, relationships, null, bean);
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>                     beanClass,
                               RelatedMetadataElement       primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(beanClass, relationships, null, bean);
        }

        return returnBean;
    }
}
