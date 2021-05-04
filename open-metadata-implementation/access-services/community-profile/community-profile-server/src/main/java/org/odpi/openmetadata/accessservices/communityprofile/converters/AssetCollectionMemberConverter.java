/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.AssetCollectionMemberMapper;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * AssetCollectionMemberConverter generates an AssetCollectionMember bean from an Asset entity
 * and a CollectionMembership relationship to it.
 */
public class AssetCollectionMemberConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetCollectionMemberConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
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

            if (returnBean instanceof AssetCollectionMember)
            {
                AssetCollectionMember bean             = (AssetCollectionMember) returnBean;
                AssetProperties       assetProperties = new AssetProperties();

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    assetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    assetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    assetProperties.setName(this.removeName(instanceProperties));
                    assetProperties.setDescription(this.removeDescription(instanceProperties));

                    /* Note this value should be in the classification */
                    assetProperties.setOwner(this.removeOwner(instanceProperties));
                    /* Note this value should be in the classification */
                    assetProperties.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
                    /* Note this value should be in the classification */
                    assetProperties.setZoneMembership(this.removeZoneMembership(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    assetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    assetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    /*
                     * The values in the classifications override the values in the main properties of the Asset's entity.
                     * Having these properties in the main entity is deprecated.
                     */
                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                    assetProperties.setZoneMembership(this.getZoneMembership(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                    assetProperties.setOwner(this.getOwner(instanceProperties));
                    assetProperties.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                if (relationship != null)
                {
                    instanceProperties = new InstanceProperties(relationship.getProperties());

                    bean.setMembershipRationale(this.removeMembershipRationale(instanceProperties));
                }

                bean.setProperties(assetProperties);
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
}
