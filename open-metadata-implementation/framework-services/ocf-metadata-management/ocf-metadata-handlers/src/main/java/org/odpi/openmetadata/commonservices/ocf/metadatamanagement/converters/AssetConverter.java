/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * AssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Asset bean.
 */
public class AssetConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param assetEntity properties to convert
     * @param connectionToAssetRelationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public AssetConverter(EntityDetail         assetEntity,
                          Relationship         connectionToAssetRelationship,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(assetEntity, connectionToAssetRelationship, repositoryHelper, serviceName, serverName);
    }

    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param assetEntity properties to convert
     * @param connectionToAssetRelationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public AssetConverter(EntityDetail         assetEntity,
                          Relationship         connectionToAssetRelationship,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName)
    {
        super(assetEntity, connectionToAssetRelationship, repositoryHelper, serviceName, null);
    }


    /**
     * This method is overridable by the subclasses.
     *
     * @return empty bean
     */
    protected Asset  getNewBean()
    {
       return new Asset();
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public Asset getAssetBean()
    {
        Asset  bean = null;

        if (entity != null)
        {
            bean = getNewBean();

            updateBean(bean);
        }

        return bean;
    }


    /**
     * Return the new audit header for the asset.  These are values stored in the repository and
     * made available to open metadata security connectors and asset owners.
     *
     * @return audit header
     */
    public AssetAuditHeader getAssetAuditHeader()
    {
        if (entity != null)
        {
            AssetAuditHeader auditHeader = new AssetAuditHeader();

            auditHeader.setCreatedBy(entity.getCreatedBy());
            auditHeader.setCreateTime(entity.getCreateTime());
            auditHeader.setMaintainedBy(entity.getMaintainedBy());
            auditHeader.setUpdatedBy(entity.getUpdatedBy());
            auditHeader.setUpdateTime(entity.getUpdateTime());
            auditHeader.setVersion(entity.getVersion());

            return auditHeader;
        }

        return null;
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @param bean  output bean
     */
    protected void updateBean(Asset bean)
    {
        final String  methodName = "getBean";

        if (entity != null)
        {
            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                          AssetMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          AssetMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                /* Note this value should be in the classification */
                bean.setOwner(repositoryHelper.removeStringProperty(serviceName,
                                                                    AssetMapper.OWNER_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
                /* Note this value should be in the classification */
                bean.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
                /* Note this value should be in the classification */
                bean.setZoneMembership(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                                  AssetMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                  instanceProperties,
                                                                                  methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }

            if (relationship != null)
            {
                instanceProperties = relationship.getProperties();

                if (instanceProperties != null)
                {
                    bean.setShortDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                AssetMapper.SHORT_DESCRIPTION_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName));
                }
            }

            /*
             * The values in the classifications override the values in the main properties of the Asset's entity.
             * Having these properties in the main entity is deprecated.
             */
            instanceProperties = super.getClassificationProperties(AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME);

            if (instanceProperties != null)
            {
                bean.setZoneMembership(repositoryHelper.getStringArrayProperty(serviceName,
                                                                               AssetMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                               instanceProperties,
                                                                               methodName));
            }

            instanceProperties = super.getClassificationProperties(AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME);

            if (instanceProperties != null)
            {
                bean.setOwner(repositoryHelper.getStringProperty(serviceName,
                                                                 AssetMapper.OWNER_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName));
                bean.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));
            }

            instanceProperties = super.getClassificationProperties(AssetMapper.OWNERSHIP_CLASSIFICATION_TYPE_NAME);

            if (instanceProperties != null)
            {
                bean.setOwner(repositoryHelper.getStringProperty(serviceName,
                                                                 AssetMapper.OWNER_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName));
                bean.setOwnerTypeName(repositoryHelper.getStringProperty(serviceName,
                                                                         AssetMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setOwnerPropertyName(repositoryHelper.getStringProperty(serviceName,
                                                                             AssetMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
            }

            instanceProperties = super.getClassificationProperties(AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME);

            if (instanceProperties != null)
            {
                Map<String, String>  origins = new HashMap<>();
                String               propertyValue = repositoryHelper.getStringProperty(serviceName,
                                                                                        AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME,
                                                                                        instanceProperties,
                                                                                        methodName);

                if (propertyValue != null)
                {
                    origins.put(AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME, propertyValue);
                }

                propertyValue = repositoryHelper.getStringProperty(serviceName,
                                                                   AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName);

                if (propertyValue != null)
                {
                    origins.put(AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME, propertyValue);
                }

                Map<String, String> propertyMap = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                    AssetMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                                    instanceProperties,
                                                                                    methodName);

                if (propertyMap != null)
                {
                    for (String propertyName : propertyMap.keySet())
                    {
                        if (propertyName != null)
                        {
                            origins.put(propertyName, propertyMap.get(propertyName));
                        }
                    }
                }

                if (! origins.isEmpty())
                {
                    bean.setAssetOrigin(origins);
                }
            }
        }
    }


    /**
     * Retrieve the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    private OwnerType getOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = OwnerType.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(AssetMapper.OWNER_TYPE_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

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
            }
        }

        return ownerType;
    }


    /**
     * Retrieve the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    private OwnerType removeOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = this.getOwnerTypeFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(AssetMapper.OWNER_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerType;
    }
}
