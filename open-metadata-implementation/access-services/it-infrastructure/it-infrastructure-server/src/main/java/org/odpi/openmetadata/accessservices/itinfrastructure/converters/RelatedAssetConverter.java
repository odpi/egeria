/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.converters;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * RelatedAssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * Relationship and EntityDetail object into a bean that inherits from RelatedAssetElement.
 */
public class RelatedAssetConverter<B> extends ITInfrastructureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public RelatedAssetConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof RelatedAssetElement bean)
            {
                if (relationship != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));

                    if (relationship.getProperties() != null)
                    {
                        bean.setProperties(repositoryHelper.getInstancePropertiesAsMap(relationship.getProperties()));
                        bean.setEffectiveFrom(relationship.getProperties().getEffectiveFromTime());
                        bean.setEffectiveTo(relationship.getProperties().getEffectiveToTime());
                    }
                }

                if (entity != null)
                {
                    AssetElement assetElement = new AssetElement();
                    AssetProperties assetProperties = new AssetProperties();

                    assetElement.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    assetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    assetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    assetProperties.setName(this.removeName(instanceProperties));
                    assetProperties.setDeployedImplementationType(assetProperties.getDeployedImplementationType());
                    assetProperties.setDescription(this.removeDescription(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    assetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    assetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    assetElement.setProperties(assetProperties);

                    bean.setRelatedAsset(assetElement);
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
