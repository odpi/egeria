/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.mermaid.GlossaryCategoryMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryCategoryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * GlossaryCategoryConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GlossaryCategoryElement bean.
 */
public class GlossaryCategoryConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GlossaryCategoryConverter(OMRSRepositoryHelper repositoryHelper,
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
    public B getNewComplexBean(Class<B>            beanClass,
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

            if (returnBean instanceof GlossaryCategoryElement bean)
            {
                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));
                    GlossaryCategoryProperties glossaryCategoryProperties = new GlossaryCategoryProperties();

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    glossaryCategoryProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    glossaryCategoryProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    glossaryCategoryProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    glossaryCategoryProperties.setDescription(this.removeDescription(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    glossaryCategoryProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    glossaryCategoryProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setGlossaryCategoryProperties(glossaryCategoryProperties);

                    if (relatedEntities != null)
                    {
                        List<RelatedMetadataElementSummary> externalReferences     = new ArrayList<>();
                        List<RelatedMetadataElementSummary> terms                  = new ArrayList<>();
                        List<RelatedMetadataElementSummary> childCategories        = new ArrayList<>();
                        List<RelatedMetadataElementSummary> otherRelatedElements   = new ArrayList<>();

                        for (RelatedEntity relatedEntity: relatedEntities)
                        {
                            if ((relatedEntity != null) && (relatedEntity.relationship() != null) && (relatedEntity.entityDetail() != null))
                            {
                                if (repositoryHelper.isTypeOf(serviceName,
                                                              relatedEntity.relationship().getType().getTypeDefName(),
                                                              OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeName))
                                {
                                    bean.setParentGlossary(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.TERM_CATEGORIZATION.typeName))
                                {
                                    terms.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName))
                                {

                                    if (relatedEntity.relationship().getEntityOneProxy().getGUID().equals(primaryEntity.getGUID()))
                                    {
                                        childCategories.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                    }
                                    else
                                    {
                                        bean.setParentCategory(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                    }
                                }
                                else
                                {
                                    otherRelatedElements.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }

                            }
                        }

                        if (! externalReferences.isEmpty())
                        {
                            bean.setExternalReferences(externalReferences);
                        }
                        if (! terms.isEmpty())
                        {
                            bean.setTerms(terms);
                        }
                        if (! childCategories.isEmpty())
                        {
                            bean.setChildCategories(childCategories);
                        }
                        if (! otherRelatedElements.isEmpty())
                        {
                            bean.setOtherRelatedElements(otherRelatedElements);
                        }
                    }

                    GlossaryCategoryMermaidGraphBuilder graphBuilder = new GlossaryCategoryMermaidGraphBuilder(bean);

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
        return getNewComplexBean(beanClass, entity, (Relationship) null, null, methodName);
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
        return getNewComplexBean(beanClass, entity, relationship, null, methodName);
    }
}
