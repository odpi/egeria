/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.mermaid.GlossaryMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ChildCategoryElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GlossaryConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GlossaryElement bean.
 */
public class GlossaryConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GlossaryConverter(OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationship relationship used to access the entity
     * @param relatedEntities relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexRelatedEntityBean(Class<B>            beanClass,
                                            EntityDetail        primaryEntity,
                                            Relationship        relationship,
                                            List<RelatedEntity> relatedEntities,
                                            String              methodName) throws PropertyServerException

    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof GlossaryElement bean)
            {

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));
                    GlossaryProperties glossaryProperties = new GlossaryProperties();

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    glossaryProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    glossaryProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    glossaryProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    glossaryProperties.setDescription(this.removeDescription(instanceProperties));
                    glossaryProperties.setLanguage(this.removeLanguage(instanceProperties));
                    glossaryProperties.setUsage(this.removeUsage(instanceProperties));

                    bean.setGlossaryProperties(glossaryProperties);

                    if (relatedEntities != null)
                    {
                        List<RelatedMetadataElementSummary> externalReferences     = new ArrayList<>();
                        Map<String, ChildCategoryElement>   categories             = new HashMap<>();
                        List<RelatedEntity>                 categoryHierarchyLinks = new ArrayList<>();
                        List<String>                        childCategories        = new ArrayList<>();
                        List<RelatedMetadataElementSummary> otherRelatedElements   = new ArrayList<>();

                        for (RelatedEntity relatedEntity : relatedEntities)
                        {
                            if ((relatedEntity != null) && (relatedEntity.relationship() != null) && (relatedEntity.entityDetail() != null))
                            {
                                if (repositoryHelper.isTypeOf(serviceName,
                                                              relatedEntity.relationship().getType().getTypeDefName(),
                                                              OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeName))
                                {
                                    ChildCategoryElement childCategoryElement = new ChildCategoryElement(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));

                                    categories.put(childCategoryElement.getRelatedElement().getElementHeader().getGUID(), childCategoryElement);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName))
                                {
                                    categoryHierarchyLinks.add(relatedEntity);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (! repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.TERM_ANCHOR_RELATIONSHIP.typeName))
                                {
                                    otherRelatedElements.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                            }
                        }

                        if (! categoryHierarchyLinks.isEmpty())
                        {
                            for (RelatedEntity relatedEntity : categoryHierarchyLinks)
                            {
                                ChildCategoryElement parentCategory = categories.get(relatedEntity.relationship().getEntityOneProxy().getGUID());
                                ChildCategoryElement childCategory  = categories.get(relatedEntity.relationship().getEntityTwoProxy().getGUID());

                                if ((parentCategory != null) && (childCategory != null))
                                {
                                    List<ChildCategoryElement> currentChildCategories = parentCategory.getChildCategories();

                                    if (currentChildCategories == null)
                                    {
                                        currentChildCategories = new ArrayList<>();
                                    }

                                    currentChildCategories.add(childCategory);
                                    parentCategory.setChildCategories(currentChildCategories);
                                    childCategories.add(childCategory.getRelatedElement().getElementHeader().getGUID());
                                }
                            }
                        }

                        if (! externalReferences.isEmpty())
                        {
                            bean.setExternalReferences(externalReferences);
                        }
                        if (! categories.isEmpty())
                        {
                            /*
                             * Only need to return the top-level categories - all other
                             * categories are nested.
                             */
                            List<ChildCategoryElement> parentCategories = new ArrayList<>();

                            for (ChildCategoryElement categoryElement : categories.values())
                            {
                                if (! childCategories.contains(categoryElement.getRelatedElement().getElementHeader().getGUID()))
                                {
                                    parentCategories.add(categoryElement);
                                }
                            }

                            bean.setCategories(parentCategories);
                        }
                        if (! otherRelatedElements.isEmpty())
                        {
                            bean.setOtherRelatedElements(otherRelatedElements);
                        }
                    }

                    GlossaryMermaidGraphBuilder graphBuilder = new GlossaryMermaidGraphBuilder(bean);

                    bean.setMermaidGraph(graphBuilder.getMermaidGraph());
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
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
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        return getNewComplexRelatedEntityBean(beanClass, entity, (Relationship) null, null, methodName);
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
        return getNewComplexRelatedEntityBean(beanClass, entity, relationship, null, methodName);
    }
}
