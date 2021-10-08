/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * AssetManagerConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a SoftwareServerCapabilityElement bean.
 */
public class AssetManagerConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetManagerConverter(OMRSRepositoryHelper repositoryHelper,
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
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof SoftwareServerCapabilityElement)
            {
                SoftwareServerCapabilityElement bean = (SoftwareServerCapabilityElement) returnBean;
                AssetManagerProperties databaseManagerProperties = new AssetManagerProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    databaseManagerProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    databaseManagerProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    databaseManagerProperties.setDisplayName(this.removeName(instanceProperties));
                    databaseManagerProperties.setDescription(this.removeDescription(instanceProperties));
                    databaseManagerProperties.setTypeDescription(this.removeDeployedImplementationType(instanceProperties));
                    databaseManagerProperties.setVersion(this.removeCapabilityVersion(instanceProperties));
                    databaseManagerProperties.setPatchLevel(this.removePatchLevel(instanceProperties));
                    databaseManagerProperties.setSource(this.removeSource(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    databaseManagerProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    databaseManagerProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setSoftwareServerCapabilitiesProperties(databaseManagerProperties);
                }
                else
                {
                    handleMissingMetadataInstance(AssetManagerProperties.class.getName(),
                                                  TypeDefCategory.ENTITY_DEF,
                                                  methodName);
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
        return getNewBean(beanClass, entity, methodName);
    }
}
