/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceEngineElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * GovernanceEnginePropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GovernanceEngineProperties bean.
 */
public class RelatedElementConverter<B> extends GovernanceEngineOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName this server
     */
    public RelatedElementConverter(OMRSRepositoryHelper repositoryHelper,
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof GovernanceEngineElement)
            {
                GovernanceEngineElement    bean = (GovernanceEngineElement) returnBean;
                GovernanceEngineProperties properties = new GovernanceEngineProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties = null;

                /*
                 * The initial set of values come from the entity definition.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());
                }

                properties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                properties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                properties.setDisplayName(this.removeName(instanceProperties));
                properties.setDescription(this.removeDescription(instanceProperties));
                properties.setTypeDescription(this.removeCapabilityType(instanceProperties));
                properties.setVersion(this.removeVersion(instanceProperties));
                properties.setPatchLevel(this.removePatchLevel(instanceProperties));
                properties.setSource(this.removeSource(instanceProperties));

                bean.setProperties(properties);
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
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof RelatedMetadataElement)
            {
                RelatedMetadataElement bean = (RelatedMetadataElement) returnBean;

                fillElementControlHeader(bean, relationship);

                bean.setRelationshipGUID(relationship.getGUID());
                bean.setRelationshipType(super.getElementType(relationship));

                InstanceProperties instanceProperties = relationship.getProperties();

                if (instanceProperties != null)
                {
                    bean.setEffectiveFromTime(instanceProperties.getEffectiveFromTime());
                    bean.setEffectiveToTime(instanceProperties.getEffectiveToTime());

                    Map<String, Object> propertyMap = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);

                    bean.setRelationshipProperties(propertyHelper.addPropertyMap(new ElementProperties(), propertyMap));
                }

                OpenMetadataElement relatedBean = new OpenMetadataElement();

                super.fillOpenMetadataElement(relatedBean, entity);

                bean.setElementProperties(relatedBean);
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
