/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.Asset;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * AssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Asset bean.
 */
public class AssetConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    private static final int OWNER_TYPE_OTHER = 99;

    public AssetConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * The initial set of values come from the entity properties.
     * The super class properties are removed from a copy of the entities properties, leaving any subclass properties to
     * be stored in extended properties.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B> beanClass, EntityDetail entity, String methodName) throws PropertyServerException {
        try {
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof Asset) {
                Asset bean = (Asset) returnBean;
                InstanceType type = entity.getType();

                bean.setTypeGUID(type.getTypeDefGUID());
                bean.setTypeName(type.getTypeDefName());
                bean.setGUID(entity.getGUID());

                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                Map<String, String> originMap = this.getOtherOriginValues(instanceProperties);

                String orgOriginValue = this.getOriginOrganizationGUID(instanceProperties);
                String bizOriginValue = this.getOriginBusinessCapabilityGUID(instanceProperties);

                if ((orgOriginValue != null) || (bizOriginValue != null)) {
                    if (originMap == null) {
                        originMap = new HashMap<>();
                    }

                    if (orgOriginValue != null) {
                        originMap.put(OpenMetadataAPIMapper.ORGANIZATION_GUID_PROPERTY_NAME, orgOriginValue);
                    }

                    if (bizOriginValue != null) {
                        originMap.put(OpenMetadataAPIMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME, bizOriginValue);
                    }
                }

                bean.setOrigin(originMap);

                bean.setName(removeName(instanceProperties));
                bean.setQualifiedName(removeQualifiedName(instanceProperties));
                bean.setDisplayName(removeDisplayName(instanceProperties));
                bean.setDescription(removeDescription(instanceProperties));

                bean.setOwner(removeOwner(instanceProperties));
                bean.setOwnerType(removeOwnerTypeFromProperties(instanceProperties));
                bean.setZoneMembership(removeZoneMembership(instanceProperties));

                bean.setAdditionalProperties(removeAdditionalProperties(instanceProperties));
                bean.setExtendedProperties(getRemainingExtendedProperties(instanceProperties));
            }

            return returnBean;
        } catch (IllegalAccessException | InstantiationException | ClassCastException error) {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }

    int removeOwnerTypeFromProperties(InstanceProperties properties) {
        int ownerType = this.getOwnerTypeFromProperties(properties);

        if (properties != null) {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null) {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }
            properties.setInstanceProperties(instancePropertiesMap);
        }
        return ownerType;
    }

    int getOwnerTypeFromProperties(InstanceProperties properties) {
        int ownerType = OWNER_TYPE_OTHER;

        if (properties != null) {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null) {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue) {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    ownerType = enumPropertyValue.getOrdinal();
                }
            }
        }
        return ownerType;
    }
}
