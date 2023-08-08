/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SoftwareCapabilitiesProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * SoftwareCapabilityConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a SoftwareCapabilityElement bean.
 */
public class SoftwareCapabilityConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SoftwareCapabilityConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof SoftwareCapabilityElement bean)
            {
                SoftwareCapabilitiesProperties softwareCapabilitiesProperties = new SoftwareCapabilitiesProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    softwareCapabilitiesProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    softwareCapabilitiesProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    softwareCapabilitiesProperties.setTechnicalName(this.removeName(instanceProperties));
                    softwareCapabilitiesProperties.setTechnicalDescription(this.removeDescription(instanceProperties));
                    softwareCapabilitiesProperties.setTypeDescription(this.removeCapabilityType(instanceProperties));
                    softwareCapabilitiesProperties.setVersion(this.removeCapabilityVersion(instanceProperties));
                    softwareCapabilitiesProperties.setPatchLevel(this.removePatchLevel(instanceProperties));
                    softwareCapabilitiesProperties.setSource(this.removeSource(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    softwareCapabilitiesProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    softwareCapabilitiesProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setSoftwareCapabilitiesProperties(softwareCapabilitiesProperties);
                }
                else
                {
                    handleMissingMetadataInstance(SoftwareCapabilitiesProperties.class.getName(),
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
        return getNewBean(beanClass, entity, methodName);
    }
}
