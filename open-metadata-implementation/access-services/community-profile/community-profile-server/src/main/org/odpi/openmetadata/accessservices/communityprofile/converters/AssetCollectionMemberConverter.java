/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.AssetCollectionMemberMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.OwnerType;
import org.odpi.openmetadata.accessservices.communityprofile.properties.WatchStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * AssetCollectionMemberConverter generates an AssetCollectionMember bean from an Asset entity
 * and a ResourceList relationship to it.
 */
public class AssetCollectionMemberConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(AssetCollectionMemberConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    AssetCollectionMemberConverter(EntityDetail         entity,
                                   Relationship         relationship,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               componentName)
    {
        super(entity, relationship, repositoryHelper, componentName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public AssetCollectionMember getBean()
    {
        final String methodName = "getBean";

        AssetCollectionMember  bean = new AssetCollectionMember();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * resource properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, AssetCollectionMemberMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, AssetCollectionMemberMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, AssetCollectionMemberMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setOwner(repositoryHelper.removeStringProperty(serviceName, AssetCollectionMemberMapper.OWNER_PROPERTY_NAME, instanceProperties, methodName));
                bean.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));
                bean.setDateAssetCreated(entity.getCreateTime());
                bean.setDateAssetLastUpdated(entity.getUpdateTime());
                bean.setLastChange(repositoryHelper.removeStringProperty(serviceName, AssetCollectionMemberMapper.LATEST_CHANGE_PROPERTY_NAME, instanceProperties, methodName));
                bean.setZoneMembership(repositoryHelper.removeStringArrayProperty(serviceName, AssetCollectionMemberMapper.ZONE_MEMBERSHIP_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, AssetCollectionMemberMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setDateAddedToCollection(relationship.getCreateTime());
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setMembershipRationale(repositoryHelper.getStringProperty(serviceName, AssetCollectionMemberMapper.MEMBERSHIP_RATIONALE_PROPERTY_NAME, instanceProperties, methodName));

                if (instanceProperties.getPropertyValue(AssetCollectionMemberMapper.WATCH_STATUS_PROPERTY_NAME) != null)
                {
                    if (repositoryHelper.getBooleanProperty(serviceName, AssetCollectionMemberMapper.WATCH_STATUS_PROPERTY_NAME, instanceProperties, methodName))
                    {
                        bean.setWatchStatus(WatchStatus.WATCHED);
                    }
                    else
                    {
                        bean.setWatchStatus(WatchStatus.NOT_WATCHED);
                    }
                }
                else
                {
                    bean.setWatchStatus(WatchStatus.USE_DEFAULT);
                }
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }


    /**
     * Retrieve the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    private OwnerType getOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType   ownerType = null;

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(AssetCollectionMemberMapper.OWNER_TYPE_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        ownerType = OwnerType.USER_ID;
                        break;

                    case 1:
                        ownerType = OwnerType.PROFILE_ID;
                        break;

                    case 99:
                        ownerType = OwnerType.OTHER;
                        break;
                }
            }

            Map<String, InstancePropertyValue> instancePropertyValueMap = properties.getInstanceProperties();
            instancePropertyValueMap.remove(AssetCollectionMemberMapper.OWNER_TYPE_PROPERTY_NAME);
            properties.setInstanceProperties(instancePropertyValueMap);
        }

        log.debug("OwnerType: " + ownerType.getName());

        return ownerType;
    }
}
