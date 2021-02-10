/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionProcessElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionProcessProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * GovernanceActionProcessConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from GovernanceActionProcessElement.
 */
public class GovernanceActionProcessConverter<B> extends GovernanceEngineOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceActionProcessConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof GovernanceActionProcessElement)
            {
                GovernanceActionProcessElement    bean              = (GovernanceActionProcessElement) returnBean;
                GovernanceActionProcessProperties processProperties = new GovernanceActionProcessProperties();

                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    processProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    processProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    processProperties.setDisplayName(this.removeName(instanceProperties));
                    processProperties.setDescription(this.removeDescription(instanceProperties));

                    /* Note this value should be in the classification */
                    processProperties.setOwner(this.removeOwner(instanceProperties));
                    /* Note this value should be in the classification */
                    processProperties.setOwnerCategory(this.removeOwnerCategoryFromProperties(instanceProperties));
                    /* Note this value should be in the classification */
                    processProperties.setZoneMembership(this.removeZoneMembership(instanceProperties));

                    /*
                     * The values in the classifications override the values in the main properties of the Asset's entity.
                     * Having these properties in the main entity is deprecated.
                     */
                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                    processProperties.setZoneMembership(this.getZoneMembership(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                    processProperties.setOwner(this.getOwner(instanceProperties));
                    processProperties.setOwnerCategory(this.getOwnerCategoryFromProperties(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

                    processProperties.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
                    processProperties.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
                    processProperties.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));

                    bean.setProcessProperties(processProperties);
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
