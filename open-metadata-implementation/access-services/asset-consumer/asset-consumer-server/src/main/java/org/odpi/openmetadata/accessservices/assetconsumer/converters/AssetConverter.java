/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;

import org.odpi.openmetadata.accessservices.assetconsumer.elements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.OwnerType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * AssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from AssetProperties.
 */
public class AssetConverter<B> extends AssetConsumerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetConverter(OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }



    /**
     * Extract the properties from the entity.  Each DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param metadataElement output bean
     * @param entity entity containing the properties
     * @param relationship optional relationship containing the properties
     */
    public void updateMetadataElement(MetadataElement metadataElement, EntityDetail entity, Relationship relationship)
    {
        metadataElement.setElementHeader(this.getMetadataElementHeader(entity));

        if (metadataElement instanceof AssetProperties)
        {
            AssetProperties bean = (AssetProperties) metadataElement;

            /*
             * The initial set of values come from the entity.
             */
            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
            bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
            bean.setDisplayName(this.removeDisplayName(instanceProperties));
            bean.setDescription(this.removeDescription(instanceProperties));

            /* Note this value should be in the classification */
            bean.setOwner(this.removeOwner(instanceProperties));
            /* Note this value should be in the classification */
            bean.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
            /* Note this value should be in the classification */
            bean.setZoneMembership(this.removeZoneMembership(instanceProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            bean.setTypeName(typeName);
            bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            /*
             * The values in the classifications override the values in the main properties of the Asset's entity.
             * Having these properties in the main entity is deprecated.
             */
            instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

            bean.setZoneMembership(this.getZoneMembership(instanceProperties));

            instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

            bean.setOwner(this.getOwner(instanceProperties));
            bean.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));

            instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

            bean.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
            bean.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
            bean.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));
        }
    }


    /**
     * Retrieve and delete the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    OwnerType removeOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = this.getOwnerTypeFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerType;
    }


    /**
     * Retrieve the OwnerType enum property from the instance properties of a classification
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    OwnerType getOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = OwnerType.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

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
}
