/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * ExternalReferenceConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ExternalReferenceElement bean.
 */
public class ExternalReferenceLinkConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ExternalReferenceLinkConverter(OMRSRepositoryHelper repositoryHelper,
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
        return getNewBean(beanClass, entity, null, methodName);
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

            if (returnBean instanceof ExternalReferenceLinkElement)
            {
                ExternalReferenceLinkElement    bean                        = (ExternalReferenceLinkElement) returnBean;
                ExternalReferenceProperties     externalReferenceProperties = new ExternalReferenceProperties();
                ExternalReferenceLinkProperties linkProperties              = new ExternalReferenceLinkProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    externalReferenceProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    externalReferenceProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    externalReferenceProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    externalReferenceProperties.setDescription(this.removeDescription(instanceProperties));
                    externalReferenceProperties.setUrl(this.removeURL(instanceProperties));
                    externalReferenceProperties.setVersion(this.removeReferenceVersion(instanceProperties));
                    externalReferenceProperties.setOrganization(this.removeOrganization(instanceProperties));

                    bean.setProperties(externalReferenceProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                if (relationship != null)
                {
                    InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

                    linkProperties.setLinkId(this.removeReferenceId(instanceProperties));
                    linkProperties.setLinkDescription(this.removeDescription(instanceProperties));

                    bean.setLink(linkProperties);
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
