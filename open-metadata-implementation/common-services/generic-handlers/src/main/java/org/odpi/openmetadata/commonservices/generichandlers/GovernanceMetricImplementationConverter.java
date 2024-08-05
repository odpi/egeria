/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceMetricImplementation;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMetricProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * GovernanceMetricConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from GovernanceMetricImplementation.
 */
public class GovernanceMetricImplementationConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceMetricImplementationConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof GovernanceMetricImplementation bean)
            {

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, entity.getClassifications(), methodName));
                    GovernanceMetricProperties governanceMetricProperties = new GovernanceMetricProperties();

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    governanceMetricProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    governanceMetricProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    governanceMetricProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    governanceMetricProperties.setDescription(this.removeDescription(instanceProperties));
                    governanceMetricProperties.setMeasurement(this.removeMeasurement(instanceProperties));
                    governanceMetricProperties.setTarget(this.removeTarget(instanceProperties));
                    governanceMetricProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceMetricProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceMetricProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(governanceMetricProperties);
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
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, entity, methodName);

        if (returnBean instanceof GovernanceMetricImplementation bean)
        {

            bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
        }

        return returnBean;
    }
    

    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the entity and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof GovernanceMetricImplementation bean)
            {

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    GovernanceMetricProperties governanceMetricProperties = new GovernanceMetricProperties();

                    governanceMetricProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    governanceMetricProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    governanceMetricProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    governanceMetricProperties.setDescription(this.removeDescription(instanceProperties));
                    governanceMetricProperties.setMeasurement(this.removeMeasurement(instanceProperties));
                    governanceMetricProperties.setTarget(this.removeTarget(instanceProperties));
                    governanceMetricProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceMetricProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceMetricProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(governanceMetricProperties);

                    List<RelatedElement> implementations = new ArrayList<>();

                    if (relationships != null)
                    {
                        for (Relationship relationship : relationships)
                        {
                            if (relationship != null)
                            {
                                if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_RESULTS_TYPE_NAME))
                                {
                                    RelatedElement element = super.getRelatedElement(beanClass, relationship, relationship.getEntityTwoProxy(), methodName);

                                    implementations.add(element);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_NAME))
                                {
                                    RelatedElement element = super.getRelatedElement(beanClass, relationship, relationship.getEntityOneProxy(), methodName);

                                    bean.setRelatedElement(element);
                                }
                            }
                        }
                    }

                    if (! implementations.isEmpty())
                    {
                        bean.setImplementations(implementations);
                    }
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
}
