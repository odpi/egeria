/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * EndpointConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a EndpointElement bean.
 */
public class EndpointConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public EndpointConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof EndpointElement bean)
            {
                EndpointProperties endpointProperties = new EndpointProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    endpointProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    endpointProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    endpointProperties.setName(this.removeName(instanceProperties));
                    endpointProperties.setResourceDescription(this.removeDescription(instanceProperties));
                    endpointProperties.setAddress(this.removeNetworkAddress(instanceProperties));
                    endpointProperties.setProtocol(this.removeProtocol(instanceProperties));
                    endpointProperties.setEncryptionMethod(this.removeEncryptionMethod(instanceProperties));


                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    endpointProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    endpointProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setEndpointProperties(endpointProperties);
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
