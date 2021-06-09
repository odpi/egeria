/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;


import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AssetOwnerOMASConverter provides the generic methods for the Data Manager beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public class GovernanceProgramOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public GovernanceProgramOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                          String                 serviceName,
                                          String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /**
     * Extract the properties from the entity or relationship.
     *
     * @param beanClass name of the class to create
     * @param header header from the entity containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementHeader getMetadataElementHeader(Class<B>       beanClass,
                                                  InstanceHeader header,
                                                  String         methodName) throws PropertyServerException
    {
        if (header != null)
        {
            ElementHeader elementHeader = new ElementHeader();

            elementHeader.setGUID(header.getGUID());
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
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, entityProxy, methodName);
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
            List<String> assetZoneMembership = null;
            String       assetOwnerProperty  = null;
            int          ownerCategory       = 0;

            if (entity.getProperties() != null)
            {
                /*
                 * Asset ownership and zone membership may be stored in a couple of deprecated properties
                 */
                assetZoneMembership = this.getZoneMembership(entity.getProperties());
                assetOwnerProperty = this.getOwner(entity.getProperties());
                ownerCategory = this.getOwnerTypeOrdinal(entity.getProperties());
            }

            List<ElementClassification> beanClassifications = null;

            if (entity.getClassifications() != null)
            {
                beanClassifications = new ArrayList<>();

                for (Classification entityClassification : entity.getClassifications())
                {
                    if (entityClassification != null)
                    {
                        if (OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME.equals(entityClassification.getName()))
                        {
                            /*
                             * Asset ownership may be stored in a deprecated classification (which takes precedence over the deprecated properties
                             */
                            assetOwnerProperty = this.getOwner(entityClassification.getProperties());
                            ownerCategory = this.getOwnerTypeOrdinal(entityClassification.getProperties());
                        }
                        else
                        {
                            ElementClassification beanClassification = new ElementClassification();

                            beanClassification.setClassificationName(entityClassification.getName());
                            beanClassification.setClassificationProperties(
                                    repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));

                            beanClassifications.add(beanClassification);
                        }
                    }
                }

                if (assetOwnerProperty != null)
                {
                    /*
                     * The ownership classification takes priority
                     */
                    if (getClassification(OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_NAME, beanClassifications) == null)
                    {
                        /*
                         * If the ownership classification is not available then one is constructed using the information
                         * from the deprecated mechanisms.
                         */
                        ElementClassification beanClassification = new ElementClassification();
                        Map<String, Object>   beanProperties     = new HashMap<>();

                        beanProperties.put(OpenMetadataAPIMapper.OWNER_PROPERTY_NAME, assetOwnerProperty);

                        if (ownerCategory == 0)
                        {
                            beanProperties.put(OpenMetadataAPIMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
                                               OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME);
                            beanProperties.put(OpenMetadataAPIMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
                                               OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
                        }
                        else if (ownerCategory == 1)
                        {
                            beanProperties.put(OpenMetadataAPIMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
                                               OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME);
                            beanProperties.put(OpenMetadataAPIMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
                                               OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
                        }


                        beanClassification.setClassificationName(OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_NAME);
                        beanClassification.setClassificationProperties(beanProperties);

                        beanClassifications.add(beanClassification);
                    }
                }

                if (assetZoneMembership != null)
                {
                    /*
                     * The AssetZoneMembership classification takes priority
                     */
                    if (getClassification(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, beanClassifications) == null)
                    {
                        /*
                         * If the AssetZoneMembership classification is not available then one is constructed using the information
                         * from the deprecated properties.
                         */
                        ElementClassification beanClassification = new ElementClassification();
                        Map<String, Object>   beanProperties     = new HashMap<>();

                        beanProperties.put(OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME, assetZoneMembership);

                        beanClassification.setClassificationName(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME);
                        beanClassification.setClassificationProperties(beanProperties);

                        beanClassifications.add(beanClassification);
                    }
                }
            }

            return beanClassifications;
        }

        return null;
    }


    /**
     * Retrieve a specific named classification.
     *
     * @param classificationName name of classification
     * @param beanClassifications list of classifications retrieved from the repositories
     * @return null or the requested classification
     */
    protected ElementClassification getClassification(String                      classificationName,
                                                      List<ElementClassification> beanClassifications)
    {
        if ((classificationName != null) && (beanClassifications != null))
        {
            for (ElementClassification classification : beanClassifications)
            {
                if (classification != null)
                {
                    if (classification.getClassificationName().equals(classificationName))
                    {
                        return classification;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Remove the requested classification from the bean classifications and return the resulting list.
     *
     * @param classificationName name of the classification
     * @param beanClassifications list of classifications retrieved from the repositories
     * @return null or a list of classifications
     */
    protected List<ElementClassification> removeClassification(String                      classificationName,
                                                               List<ElementClassification> beanClassifications)
    {
        if ((classificationName != null) && (beanClassifications != null))
        {
            List<ElementClassification> results = new ArrayList<>();

            for (ElementClassification classification : beanClassifications)
            {
                if (classification != null)
                {
                    if (! classification.getClassificationName().equals(classificationName))
                    {
                        results.add(classification);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceHeader values from the server
     * @return OCF ElementType object
     */
    ElementType getElementType(InstanceHeader instanceHeader)
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

}
