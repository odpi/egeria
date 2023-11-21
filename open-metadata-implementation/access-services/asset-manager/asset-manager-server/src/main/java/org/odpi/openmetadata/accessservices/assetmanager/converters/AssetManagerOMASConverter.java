/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * AssetManagerOMASConverter provides the generic methods for the Asset Manager beans' converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing an Asset Manager bean.
 */
public abstract class AssetManagerOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public AssetManagerOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                     String                 serviceName,
                                     String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */

    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B>     beanClass,
                                            EntityDetail entity,
                                            Relationship relationship,
                                            String       methodName) throws PropertyServerException
    {
        RelatedElement relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entity != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entity, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }



    /**
     * Extract and delete the sortOrder property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return DataItemSortOrder enum
     */
    DataItemSortOrder removeSortOrder(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSortOrder";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.SORT_ORDER_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (DataItemSortOrder dataItemSortOrder : DataItemSortOrder.values())
            {
                if (dataItemSortOrder.getOpenTypeOrdinal() == ordinal)
                {
                    return dataItemSortOrder;
                }
            }
        }

        return DataItemSortOrder.UNKNOWN;
    }


    /**
     * Extract and delete the keyPattern property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return KeyPattern enum
     */
    KeyPattern removeKeyPattern(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeKeyPattern";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.KEY_PATTERN_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (KeyPattern keyPattern : KeyPattern.values())
            {
                if (keyPattern.getOpenTypeOrdinal() == ordinal)
                {
                    return keyPattern;
                }
            }
        }

        return KeyPattern.LOCAL_KEY;
    }


    /**
     * Extract and delete the permittedSynchronization property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return SynchronizationDirection enum
     */
    SynchronizationDirection removePermittedSynchronization(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePermittedSynchronization";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.PERMITTED_SYNC_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (SynchronizationDirection synchronizationDirection : SynchronizationDirection.values())
            {
                if (synchronizationDirection.getOpenTypeOrdinal() == ordinal)
                {
                    return synchronizationDirection;
                }
            }
        }

        return SynchronizationDirection.BOTH_DIRECTIONS;
    }


    /**
     * Extract and delete the portType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PortType enum
     */
    PortType removePortType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePortType";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.PORT_TYPE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (PortType portType : PortType.values())
            {
                if (portType.getOpenTypeOrdinal() == ordinal)
                {
                    return portType;
                }
            }
        }

        return PortType.OTHER;
    }


    /**
     * Extract and delete the processContainmentType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return ProcessContainmentType enum
     */
    ProcessContainmentType removeProcessContainmentType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeProcessContainmentType";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.CONTAINMENT_TYPE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (ProcessContainmentType containmentType : ProcessContainmentType.values())
            {
                if (containmentType.getOpenTypeOrdinal() == ordinal)
                {
                    return containmentType;
                }
            }
        }

        return ProcessContainmentType.OTHER;
    }


    /**
     * Retrieve and delete the EngineActionStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    GovernanceActionStatus removeActionStatus(String               propertyName,
                                              InstanceProperties   properties)
    {
        GovernanceActionStatus ownerCategory = this.getActionStatus(propertyName, properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(propertyName);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerCategory;
    }


    /**
     * Retrieve the ActionStatus enum property from the instance properties of a Governance Action.
     *
     * @param propertyName name ot property to extract the enum from
     * @param properties  entity properties
     * @return ActionStatus  enum value
     */
    private GovernanceActionStatus getActionStatus(String               propertyName,
                                                   InstanceProperties   properties)
    {
        GovernanceActionStatus governanceActionStatus = GovernanceActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            governanceActionStatus = GovernanceActionStatus.REQUESTED;
                            break;

                        case 1:
                            governanceActionStatus = GovernanceActionStatus.APPROVED;
                            break;

                        case 2:
                            governanceActionStatus = GovernanceActionStatus.WAITING;
                            break;

                        case 3:
                            governanceActionStatus = GovernanceActionStatus.ACTIVATING;
                            break;

                        case 4:
                            governanceActionStatus = GovernanceActionStatus.IN_PROGRESS;
                            break;

                        case 10:
                            governanceActionStatus = GovernanceActionStatus.ACTIONED;
                            break;

                        case 11:
                            governanceActionStatus = GovernanceActionStatus.INVALID;
                            break;

                        case 12:
                            governanceActionStatus = GovernanceActionStatus.IGNORED;
                            break;

                        case 13:
                            governanceActionStatus = GovernanceActionStatus.FAILED;
                            break;

                        case 99:
                            governanceActionStatus = GovernanceActionStatus.OTHER;
                            break;
                    }
                }
            }
        }

        return governanceActionStatus;
    }
}
