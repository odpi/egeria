/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.AssetProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * AssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an AssetElement bean.
 */
public class AssetConverter<B> extends GovernanceProgramOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetConverter(OMRSRepositoryHelper repositoryHelper,
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
        return getNewComplexBean(beanClass, entity,null, null, methodName);
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
        return getNewComplexBean(beanClass, entity,null, null, methodName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof AssetElement)
            {
                AssetElement    bean            = (AssetElement) returnBean;
                AssetProperties assetProperties = new AssetProperties();

                if (primaryEntity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    assetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    assetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    assetProperties.setResourceName(this.removeName(instanceProperties));
                    assetProperties.setResourceDescription(this.removeDescription(instanceProperties));

                    /* Note this value should be in the classification and have been incorporated into the element header already */
                    this.removeOwner(instanceProperties);
                    /* Note this value should be in the classification and have been incorporated into the element header already */
                    this.removeOwnerTypeOrdinal(instanceProperties);
                    /* Note this value should be in the classification and have been incorporated into the element header already */
                    this.removeZoneMembership(instanceProperties);

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    assetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    assetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    if ((relationships != null) && (supplementaryEntities != null))
                    {
                        for (Relationship relationship : relationships)
                        {
                            if (relationship != null)
                            {
                                if (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataAPIMapper.SUPPLEMENTARY_PROPERTIES_TYPE_NAME))
                                {
                                    EntityProxy termProxy = relationship.getEntityTwoProxy();

                                    if (termProxy != null)
                                    {
                                        for (EntityDetail entity : supplementaryEntities)
                                        {
                                            if (entity != null)
                                            {
                                                if (termProxy.getGUID().equals(entity.getGUID()))
                                                {
                                                    instanceProperties = entity.getProperties();
                                                    assetProperties.setDisplayName(this.getDisplayName(instanceProperties));
                                                    assetProperties.setSummary(this.getSummary(instanceProperties));
                                                    assetProperties.setDescription(this.getDescription(instanceProperties));
                                                    assetProperties.setAbbreviation(this.getAbbreviation(instanceProperties));
                                                    assetProperties.setUsage(this.getUsage(instanceProperties));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    bean.setProperties(assetProperties);
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
