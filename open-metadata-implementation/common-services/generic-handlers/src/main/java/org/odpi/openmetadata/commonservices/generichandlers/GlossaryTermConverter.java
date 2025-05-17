/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.mermaid.GlossaryTermMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedBy;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
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
 * GlossaryTermConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GlossaryTermElement bean.
 */
public class GlossaryTermConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GlossaryTermConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof GlossaryTermElement bean)
            {
                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));
                    GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    glossaryTermProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    glossaryTermProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    glossaryTermProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    glossaryTermProperties.setAliases(this.removeAliases(instanceProperties));
                    glossaryTermProperties.setDescription(this.removeDescription(instanceProperties));
                    glossaryTermProperties.setAbbreviation(this.removeAbbreviation(instanceProperties));
                    glossaryTermProperties.setUsage(this.removeUsage(instanceProperties));
                    glossaryTermProperties.setExamples(this.removeExamples(instanceProperties));
                    glossaryTermProperties.setSummary(this.removeSummary(instanceProperties));
                    glossaryTermProperties.setPublishVersionIdentifier(this.removePublishVersionIdentifier(instanceProperties));


                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    glossaryTermProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    glossaryTermProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setGlossaryTermProperties(glossaryTermProperties);

                    if (relationship != null)
                    {
                        RelatedBy relatedBy = new RelatedBy();

                        relatedBy.setRelationshipHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));

                        if (relationship.getProperties() != null)
                        {
                            instanceProperties = new InstanceProperties(relationship.getProperties());

                            RelationshipProperties relationshipProperties = new RelationshipProperties();
                            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
                            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                            relatedBy.setRelationshipProperties(relationshipProperties);
                        }

                        bean.setRelatedElement(relatedBy);
                    }

                    if (relatedEntities != null)
                    {
                        List<RelatedMetadataElementSummary> externalReferences     = new ArrayList<>();
                        List<RelatedMetadataElementSummary> categoryMembership     = new ArrayList<>();
                        List<RelatedMetadataElementSummary> relatedTerms           = new ArrayList<>();
                        List<RelatedMetadataElementSummary> relatedDefinitions     = new ArrayList<>();
                        List<RelatedMetadataElementSummary> semanticAssignments    = new ArrayList<>();
                        List<RelatedMetadataElementSummary> otherRelatedElements   = new ArrayList<>();

                        for (RelatedEntity relatedEntity: relatedEntities)
                        {
                            if ((relatedEntity != null) && (relatedEntity.relationship() != null) && (relatedEntity.entityDetail() != null))
                            {
                                if (repositoryHelper.isTypeOf(serviceName,
                                                              relatedEntity.relationship().getType().getTypeDefName(),
                                                              OpenMetadataType.TERM_ANCHOR_RELATIONSHIP.typeName))
                                {
                                    bean.setParentGlossary(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.TERM_CATEGORIZATION.typeName))
                                {
                                    categoryMembership.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName))
                                {
                                    relatedDefinitions.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName))
                                {
                                    semanticAssignments.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relatedEntity.relationship().getType().getTypeDefName(),
                                                                   OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                }
                                else
                                {
                                    if (repositoryHelper.isTypeOf(serviceName,
                                                                  relatedEntity.entityDetail().getType().getTypeDefName(),
                                                                  OpenMetadataType.GLOSSARY_TERM.typeName))
                                    {
                                        relatedTerms.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                    }
                                    else
                                    {
                                        otherRelatedElements.add(super.getRelatedMetadataElementSummary(beanClass, relatedEntity, methodName));
                                    }
                                }
                            }
                        }

                        if (! externalReferences.isEmpty())
                        {
                            bean.setExternalReferences(externalReferences);
                        }
                        if (! categoryMembership.isEmpty())
                        {
                            bean.setCategoryMembership(categoryMembership);
                        }
                        if (! relatedTerms.isEmpty())
                        {
                            bean.setRelatedToTerms(relatedTerms);
                        }
                        if (! relatedDefinitions.isEmpty())
                        {
                            bean.setRelatedDefinitions(relatedDefinitions);
                        }
                        if (! semanticAssignments.isEmpty())
                        {
                            bean.setSemanticAssignments(semanticAssignments);
                        }
                        if (! otherRelatedElements.isEmpty())
                        {
                            bean.setOtherRelatedElements(otherRelatedElements);
                        }
                    }

                    GlossaryTermMermaidGraphBuilder graphBuilder = new GlossaryTermMermaidGraphBuilder(bean);

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
