/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ElementClassification;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
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
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    ElementHeader getMetadataElementHeader(Class<B>     beanClass,
                                           EntityDetail entity,
                                           String       methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            return getMetadataElementHeader(beanClass,
                                            entity,
                                            entity.getClassifications(),
                                            methodName);
        }
        else
        {
            super.handleMissingMetadataInstance(beanClass.getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }

        return null;
    }


    /**
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param header header from the entity containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    ElementHeader getMetadataElementHeader(Class<B>             beanClass,
                                           InstanceHeader       header,
                                           List<Classification> entityClassifications,
                                           String               methodName) throws PropertyServerException
    {
        if (header != null)
        {
            ElementHeader elementHeader = new ElementHeader();

            elementHeader.setGUID(header.getGUID());
            elementHeader.setStatus(this.getElementStatus(header.getStatus()));
            elementHeader.setClassifications(this.getElementClassifications(entityClassifications));
            elementHeader.setType(this.getElementType(header));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(header.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(header.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(header.getMetadataCollectionName());
            elementOrigin.setLicense(header.getInstanceLicense());

            elementHeader.setOrigin(elementOrigin);

            elementHeader.setVersions(this.getElementVersions(header));

            return elementHeader;
        }
        else
        {
            super.handleMissingMetadataInstance(beanClass.getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }

        return null;
    }


    /**
     * Translate the repository services' InstanceStatus to an ElementStatus.
     *
     * @param instanceStatus value from the repository services
     * @return ElementStatus enum
     */
    ElementStatus getElementStatus(InstanceStatus instanceStatus)
    {
        if (instanceStatus != null)
        {
            switch (instanceStatus)
            {
                case UNKNOWN:
                    return ElementStatus.UNKNOWN;

                case DRAFT:
                    return ElementStatus.DRAFT;

                case PREPARED:
                    return ElementStatus.PREPARED;

                case PROPOSED:
                    return ElementStatus.PROPOSED;

                case APPROVED:
                    return ElementStatus.APPROVED;

                case REJECTED:
                    return ElementStatus.REJECTED;

                case APPROVED_CONCEPT:
                    return ElementStatus.APPROVED_CONCEPT;

                case UNDER_DEVELOPMENT:
                    return ElementStatus.UNDER_DEVELOPMENT;

                case DEVELOPMENT_COMPLETE:
                    return ElementStatus.DEVELOPMENT_COMPLETE;

                case APPROVED_FOR_DEPLOYMENT:
                    return ElementStatus.APPROVED_FOR_DEPLOYMENT;

                case STANDBY:
                    return ElementStatus.STANDBY;

                case ACTIVE:
                    return ElementStatus.ACTIVE;

                case FAILED:
                    return ElementStatus.FAILED;

                case DISABLED:
                    return ElementStatus.DISABLED;

                case COMPLETE:
                    return ElementStatus.COMPLETE;

                case DEPRECATED:
                    return ElementStatus.DEPRECATED;

                case OTHER:
                    return ElementStatus.OTHER;
            }
        }

        return ElementStatus.UNKNOWN;
    }


    /**
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entityProxy entityProxy from the relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    ElementStub getElementStub(Class<B>    beanClass,
                               EntityProxy entityProxy,
                               String      methodName) throws PropertyServerException
    {
        if (entityProxy != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                         entityProxy.getUniqueProperties(),
                                                                         methodName));

            return elementStub;
        }
        else
        {
            super.handleMissingMetadataInstance(beanClass.getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }

        return null;
    }



    /**
     * Extract the classifications from the entity.
     *
     * @param entity entity containing the classifications
     * @return list of bean classifications
     */
    private List<ElementClassification> getEntityClassifications(EntityDetail entity)
    {
        if (entity != null)
        {
            return this.getElementClassifications(entity.getClassifications());
        }

        return null;
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entityClassifications classifications direct from the entity
     * @return list of bean classifications
     */
    private List<ElementClassification> getElementClassifications(List<Classification> entityClassifications)
    {
        List<ElementClassification> beanClassifications = null;

        if (entityClassifications != null)
        {
            beanClassifications = new ArrayList<>();

            for (Classification entityClassification : entityClassifications)
            {
                if (entityClassification != null)
                {
                    ElementClassification beanClassification = new ElementClassification();

                    beanClassification.setClassificationName(entityClassification.getName());
                    beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));

                    beanClassifications.add(beanClassification);
                }
            }

        }

        return beanClassifications;
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceHeader values from the server
     * @return OCF ElementType object
     */
    ElementType getElementType(InstanceAuditHeader instanceHeader)
    {
        ElementType  elementType = new ElementType();

        InstanceType instanceType = instanceHeader.getType();

        if (instanceType != null)
        {
            elementType.setTypeId(instanceType.getTypeDefGUID());
            elementType.setTypeName(instanceType.getTypeDefName());
            elementType.setTypeVersion(instanceType.getTypeDefVersion());
            elementType.setTypeDescription(instanceType.getTypeDefDescription());

            List<TypeDefLink> typeDefSuperTypes = instanceType.getTypeDefSuperTypes();

            if ((typeDefSuperTypes != null) && (! typeDefSuperTypes.isEmpty()))
            {
                List<String>   superTypes = new ArrayList<>();

                for (TypeDefLink typeDefLink : typeDefSuperTypes)
                {
                    if (typeDefLink != null)
                    {
                        superTypes.add(typeDefLink.getName());
                    }
                }

                if (! superTypes.isEmpty())
                {
                    elementType.setSuperTypeNames(superTypes);
                }
            }
        }

        return elementType;
    }


    /**
     * Extract detail of the version of the element and the user's maintaining it.
     *
     * @param header audit header from the repository
     * @return ElementVersions object
     */
    ElementVersions getElementVersions(InstanceAuditHeader header)
    {
        ElementVersions elementVersions = new ElementVersions();

        elementVersions.setCreatedBy(header.getCreatedBy());
        elementVersions.setCreateTime(header.getCreateTime());
        elementVersions.setUpdatedBy(header.getUpdatedBy());
        elementVersions.setUpdateTime(header.getUpdateTime());
        elementVersions.setMaintainedBy(header.getMaintainedBy());
        elementVersions.setVersion(header.getVersion());

        return elementVersions;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    ElementOriginCategory getElementOriginCategory(InstanceProvenanceType   instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOriginCategory.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOriginCategory.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOriginCategory.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOriginCategory.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOriginCategory.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOriginCategory.CONFIGURATION;

                case UNKNOWN:
                    return ElementOriginCategory.UNKNOWN;
            }
        }

        return ElementOriginCategory.UNKNOWN;
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
     * Retrieve and delete the GovernanceActionStatus enum property from the instance properties of an entity
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
