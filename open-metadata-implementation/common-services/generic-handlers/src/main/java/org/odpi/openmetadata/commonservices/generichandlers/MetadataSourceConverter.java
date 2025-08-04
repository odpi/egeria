/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * MetadataSourceConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a MetadataSourceElement bean.
 */
public class MetadataSourceConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public MetadataSourceConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof MetadataSourceElement bean)
            {
                MetadataSourceProperties metadataSourceProperties = new MetadataSourceProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    metadataSourceProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    metadataSourceProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    metadataSourceProperties.setDisplayName(this.removeName(instanceProperties));
                    metadataSourceProperties.setDescription(this.removeDescription(instanceProperties));
                    metadataSourceProperties.setDeployedImplementationType(this.removeCapabilityType(instanceProperties));
                    metadataSourceProperties.setVersionIdentifier(this.removeCapabilityVersion(instanceProperties));
                    metadataSourceProperties.setPatchLevel(this.removePatchLevel(instanceProperties));
                    metadataSourceProperties.setSource(this.removeSource(instanceProperties));

                    metadataSourceProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    metadataSourceProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    metadataSourceProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    metadataSourceProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(metadataSourceProperties);
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
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, methodName);
    }
}
