/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client.converters;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ActionTargetElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ToDoElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActionTargetProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * CollectionConverter generates a CollectionElement from a Collection entity
 */
public class ToDoConverter<B> extends CommunityProfileConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ToDoConverter(PropertyHelper propertyHelper,
                         String         serviceName,
                         String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException

    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ToDoElement bean)
            {
                ToDoProperties toDoProperties = new ToDoProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the toDoMetadataElement.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    toDoProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    toDoProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    toDoProperties.setName(this.removeName(elementProperties));
                    toDoProperties.setDescription(this.removeDescription(elementProperties));
                    toDoProperties.setCreationTime(this.removeCreationTime(elementProperties));
                    toDoProperties.setPriority(this.removeIntPriority(elementProperties));
                    toDoProperties.setStatus(super.removeToDoStatus(elementProperties));
                    toDoProperties.setLastReviewTime(this.removeLastReviewTime(elementProperties));
                    toDoProperties.setDueTime(this.removeDueTime(elementProperties));
                    toDoProperties.setCompletionTime(this.removeCompletionTime(elementProperties));
                    toDoProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    toDoProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    toDoProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    toDoProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(toDoProperties);

                    if (relationships != null)
                    {
                        List<ElementStub>         assignedActors = new ArrayList<>();
                        List<ActionTargetElement> actionTargets  = new ArrayList<>();

                        for (RelatedMetadataElement relatedMetadataElement : relationships)
                        {
                            if (relatedMetadataElement != null)
                            {
                                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP_TYPE_NAME))
                                {
                                    bean.setRelatedElement(super.getRelatedElement(beanClass, relatedMetadataElement, methodName));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP_TYPE_NAME))
                                {
                                    assignedActors.add(super.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ACTION_TARGET_RELATIONSHIP_TYPE_NAME))
                                {
                                    ActionTargetElement actionTargetElement = new ActionTargetElement();

                                    actionTargetElement.setTargetElement(relatedMetadataElement.getElement());
                                    actionTargetElement.setRelationshipHeader(super.getMetadataElementHeader(beanClass,
                                                                                                             relatedMetadataElement,
                                                                                                             relatedMetadataElement.getRelationshipGUID(),
                                                                                                             null,
                                                                                                             methodName));

                                    ActionTargetProperties actionTargetProperties = new ActionTargetProperties();
                                    ElementProperties      relationshipProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                                    actionTargetProperties.setActionTargetName(removeActionTargetName(relationshipProperties));
                                    actionTargetProperties.setStatus(removeToDoStatus(relationshipProperties));
                                    actionTargetProperties.setStartDate(removeStartDate(relationshipProperties));
                                    actionTargetProperties.setCompletionDate(removeCompletionDate(relationshipProperties));
                                    actionTargetProperties.setCompletionMessage(removeCompletionMessage(relationshipProperties));

                                    actionTargetElement.setRelationshipProperties(actionTargetProperties);

                                    actionTargets.add(actionTargetElement);
                                }
                            }
                        }

                        if (! assignedActors.isEmpty())
                        {
                            bean.setAssignedActors(assignedActors);
                        }

                        if (! actionTargets.isEmpty())
                        {
                            bean.setActionTargets(actionTargets);
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(toDoProperties);
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }



    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param toDoRelatedMetadataElement the properties of an open metadata element plus details of the relationship used to navigate to it
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement toDoRelatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof CollectionElement bean)
            {
                CollectionProperties collectionProperties = new CollectionProperties();
                OpenMetadataElement  openMetadataElement  = toDoRelatedMetadataElement.getElement();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    collectionProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    collectionProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    collectionProperties.setName(this.removeName(elementProperties));
                    collectionProperties.setDescription(this.removeDescription(elementProperties));
                    collectionProperties.setCollectionType(this.removeCollectionType(elementProperties));
                    collectionProperties.setEffectiveFrom(openMetadataElement.getEffectiveFromTime());
                    collectionProperties.setEffectiveTo(openMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    collectionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    collectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(collectionProperties);

                bean.setRelatedElement(super.getRelatedElement(beanClass, toDoRelatedMetadataElement, methodName));
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
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
    public B getNewBean(Class<B>                beanClass,
                        OpenMetadataElement     element,
                        RelatedMetadataElements relationship,
                        String                  methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, element, methodName);

        if (returnBean instanceof CollectionElement bean)
        {
            bean.setRelatedElement(super.getRelatedElement(beanClass, element, relationship, methodName));
        }

        return returnBean;
    }
}
