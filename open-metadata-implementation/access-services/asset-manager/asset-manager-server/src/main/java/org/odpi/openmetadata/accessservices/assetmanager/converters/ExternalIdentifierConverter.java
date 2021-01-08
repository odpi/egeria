/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * GlossaryConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GlossaryElement bean.
 */
public class ExternalIdentifierConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ExternalIdentifierConverter(OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName,
                                       String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that os a connected relationship.
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
        return this.getNewComplexBean(beanClass, entity, null, methodName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that os a connected relationship.
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
        List<Relationship> relationships = null;

        if (relationship != null)
        {
            relationships = new ArrayList<>();

            relationships.add(relationship);
        }

        return this.getNewComplexBean(beanClass, entity, relationships, methodName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the entity and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof MetadataCorrelationHeader)
            {
                MetadataCorrelationHeader bean = (MetadataCorrelationHeader) returnBean;

                if (primaryEntity != null)
                {
                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    bean.setExternalIdentifier(this.removeIdentifier(instanceProperties));
                    bean.setKeyPattern(this.removeKeyPattern(instanceProperties));
                    bean.setMappingProperties(this.removeMappingProperties(instanceProperties));

                    if (relationships != null)
                    {
                        for (Relationship relationship : relationships)
                        {
                            if ((relationship != null) &&
                                        (relationship.getType() != null) &&
                                        (relationship.getProperties() != null) &&
                                        (relationship.getEntityOneProxy() != null))
                            {
                                if (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataAPIMapper.EXTERNAL_ID_SCOPE_TYPE_NAME))
                                {
                                    instanceProperties = new InstanceProperties(relationship.getProperties());

                                    bean.setSynchronizationDirection(this.removePermittedSynchronization(instanceProperties));
                                    bean.setSynchronizationDescription(this.removeDescription(instanceProperties));

                                    bean.setAssetManagerGUID(relationship.getEntityOneProxy().getGUID());

                                    instanceProperties = new InstanceProperties(relationship.getEntityOneProxy().getUniqueProperties());

                                    bean.setAssetManagerName(this.removeQualifiedName(instanceProperties));
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   relationship.getType().getTypeDefName(),
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TO_EXTERNAL_ID_TYPE_NAME))
                                {
                                    instanceProperties = new InstanceProperties(relationship.getProperties());

                                    bean.setExternalIdentifierName(this.removeDescription(instanceProperties));
                                    bean.setExternalIdentifierUsage(this.removeUsage(instanceProperties));
                                    bean.setExternalIdentifierSource(this.removeSource(instanceProperties));
                                    bean.setLastSynchronized(this.removeLastSynchronized(instanceProperties));
                                }
                            }
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        return this.getNewComplexBean(beanClass, primaryEntity, relationships, methodName);
    }
}
