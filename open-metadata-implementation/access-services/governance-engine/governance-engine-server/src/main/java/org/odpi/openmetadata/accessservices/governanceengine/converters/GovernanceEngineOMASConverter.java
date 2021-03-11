/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.*;
import org.odpi.openmetadata.accessservices.governanceengine.properties.OwnerCategory;
import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * GovernanceEngineOMASConverter provides the generic methods for the Governance Engine beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
abstract class GovernanceEngineOMASConverter<B> extends OCFConverter<B>
{
    protected PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    GovernanceEngineOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                  String                 serviceName,
                                  String                 serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
            elementHeader.setClassifications(this.getElementClassifications(entityClassifications));
            elementHeader.setType(this.getElementType(header));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(header.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(header.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(header.getMetadataCollectionName());
            elementOrigin.setLicense(header.getInstanceLicense());

            elementHeader.setOrigin(elementOrigin);

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
     * Extract the classifications from the entity.
     *
     * @param entity entity containing the classifications
     * @return list of bean classifications
     */
    List<ElementClassification> getElementClassifications(EntityDetail entity)
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

                    fillElementControlHeader(beanClassification, entityClassification);

                    beanClassification.setClassificationName(entityClassification.getName());

                    if (entityClassification.getProperties() != null)
                    {
                        Map<String, Object> classificationPropertyMap = repositoryHelper.getInstancePropertiesAsMap(
                                entityClassification.getProperties());

                        beanClassification.setClassificationProperties(propertyHelper.addPropertyMap(null, classificationPropertyMap));
                        beanClassification.setEffectiveFromTime(entityClassification.getProperties().getEffectiveFromTime());
                        beanClassification.setEffectiveToTime(entityClassification.getProperties().getEffectiveToTime());
                    }

                    beanClassifications.add(beanClassification);
                }
            }

        }

        return beanClassifications;
    }


    /**
     * Convert information from a repository instance into an ElementType.
     *
     * @param instanceHeader values from the server
     * @return ElementType object
     */
    ElementType getElementType(InstanceHeader instanceHeader)
    {
        ElementType  elementType = new ElementType();

        InstanceType instanceType = instanceHeader.getType();

        if (instanceType != null)
        {
            elementType.setElementTypeId(instanceType.getTypeDefGUID());
            elementType.setElementTypeName(instanceType.getTypeDefName());
            elementType.setElementTypeVersion(instanceType.getTypeDefVersion());
            elementType.setElementTypeDescription(instanceType.getTypeDefDescription());

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
                    elementType.setElementSuperTypeNames(superTypes);
                }
            }
        }

        return elementType;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    private ElementOriginCategory getElementOriginCategory(InstanceProvenanceType instanceProvenanceType)
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
     * Retrieve and delete the OwnerCategory enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    OwnerCategory removeOwnerCategoryFromProperties(InstanceProperties   properties)
    {
        OwnerCategory ownerCategory = this.getOwnerCategoryFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerCategory;
    }


    /**
     * Retrieve the OwnerCategory enum property from the instance properties of a classification
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    OwnerCategory getOwnerCategoryFromProperties(InstanceProperties   properties)
    {
        OwnerCategory ownerCategory = OwnerCategory.OTHER;

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
                            ownerCategory = OwnerCategory.USER_ID;
                            break;

                        case 1:
                            ownerCategory = OwnerCategory.PROFILE_ID;
                            break;

                        case 99:
                            ownerCategory = OwnerCategory.OTHER;
                            break;
                    }
                }
            }
        }

        return ownerCategory;
    }


    /**
     * Retrieve and delete the OwnerCategory enum property from the instance properties of an entity
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
     * Build an open metadata element bean from a matching entity.
     *
     * @param entityGUID unique identifier of desired entity
     * @param entities list of retrieved entities
     * @return new bean or null if guid is not in list
     */
    OpenMetadataElement getOpenMetadataElement(String              entityGUID,
                                               List<EntityDetail>  entities)
    {
        OpenMetadataElement metadataElement = new OpenMetadataElement();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    if (entityGUID.equals(entity.getGUID()))
                    {
                        this.fillOpenMetadataElement(metadataElement, entity);

                        return metadataElement;
                    }
                }
            }
        }

        /*
         * The entity was not retrieved - however, returning the guid allows the caller to retrieve the element separately.
         */
        metadataElement.setElementGUID(entityGUID);

        return metadataElement;
    }


    /**
     * Fill a GAF control header from the information in a repository services element header.
     *
     * @param elementControlHeader GAF object control header
     * @param header OMRS element header
     */
    void fillElementControlHeader(ElementControlHeader elementControlHeader,
                                  InstanceAuditHeader  header)
    {
        if (header != null)
        {
            elementControlHeader.setElementSourceServer(serverName);
            elementControlHeader.setElementOriginCategory(this.getElementOriginCategory(header.getInstanceProvenanceType()));
            elementControlHeader.setElementMetadataCollectionId(header.getMetadataCollectionId());
            elementControlHeader.setElementMetadataCollectionName(header.getMetadataCollectionName());
            elementControlHeader.setElementLicense(header.getInstanceLicense());
            elementControlHeader.setElementCreatedBy(header.getCreatedBy());
            elementControlHeader.setElementUpdatedBy(header.getUpdatedBy());
            elementControlHeader.setElementMaintainedBy(header.getMaintainedBy());
            elementControlHeader.setElementCreateTime(header.getCreateTime());
            elementControlHeader.setElementUpdateTime(header.getUpdateTime());
            elementControlHeader.setElementVersion(header.getVersion());
            elementControlHeader.setStatus(this.getElementStatus(header.getStatus()));
            elementControlHeader.setMappingProperties(header.getMappingProperties());
        }
    }


    /**
     * Fill out the properties for the GAF Open Metadata Element bean with values from an OMRS entity.
     *
     * @param bean bean to fill
     * @param entity values from repositories
     */
    void fillOpenMetadataElement(OpenMetadataElement bean,
                                 EntityDetail        entity)
    {
        fillElementControlHeader(bean, entity);

        bean.setElementGUID(entity.getGUID());
        bean.setElementType(this.getElementType(entity));
        bean.setClassifications(this.getElementClassifications(entity));

        InstanceProperties instanceProperties = entity.getProperties();

        if (instanceProperties != null)
        {
            bean.setEffectiveFromTime(instanceProperties.getEffectiveFromTime());
            bean.setEffectiveToTime(instanceProperties.getEffectiveToTime());

            Map<String, Object> propertyMap = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);

            bean.setElementProperties(propertyHelper.addPropertyMap(new ElementProperties(), propertyMap));
        }
    }
}
