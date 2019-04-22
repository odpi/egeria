/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.accessservices.discoveryengine.mappers.DiscoveryServicePropertiesMapper;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * DiscoveryEnginePropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DiscoveryEngineProperties bean.
 */
public class DiscoveryServicePropertiesConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param discoveryServiceEntity properties to convert
     * @param connectionToAssetRelationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public DiscoveryServicePropertiesConverter(EntityDetail         discoveryServiceEntity,
                                               Relationship         connectionToAssetRelationship,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName)
    {
        super(discoveryServiceEntity, connectionToAssetRelationship, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public DiscoveryServiceProperties getBean()
    {
        final String  methodName = "getBean";

        DiscoveryServiceProperties  bean = null;

        if (entity != null)
        {
            bean = new DiscoveryServiceProperties();

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
                                                                          DiscoveryServicePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          DiscoveryServicePropertiesMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setOwner(repositoryHelper.removeStringProperty(serviceName,
                                                                    DiscoveryServicePropertiesMapper.OWNER_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
                bean.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
                bean.setZoneMembership(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                                  DiscoveryServicePropertiesMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                  instanceProperties,
                                                                                  methodName));
                bean.setLatestChange(repositoryHelper.removeStringProperty(serviceName,
                                                                           DiscoveryServicePropertiesMapper.LATEST_CHANGE_PROPERTY_NAME,
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
                                                                                DiscoveryServicePropertiesMapper.SHORT_DESCRIPTION_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName));
                }
            }
        }

        return bean;
    }


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    private OwnerType removeOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = OwnerType.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(DiscoveryServicePropertiesMapper.OWNER_TYPE_PROPERTY_NAME);

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

                instancePropertiesMap.remove(DiscoveryServicePropertiesMapper.OWNER_TYPE_PROPERTY_NAME);

                properties.setInstanceProperties(instancePropertiesMap);
            }
        }

        return ownerType;
    }
}
