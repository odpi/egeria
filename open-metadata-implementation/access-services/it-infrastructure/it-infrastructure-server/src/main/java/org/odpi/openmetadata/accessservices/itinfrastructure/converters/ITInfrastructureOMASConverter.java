/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.converters;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementClassification;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementOrigin;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementOriginCategory;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementType;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementVersions;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ContactMethodType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * DataManagerOMASConverter provides the generic methods for the Data Manager beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public class ITInfrastructureOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public ITInfrastructureOMASConverter(OMRSRepositoryHelper   repositoryHelper,
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
     * Extract the properties from the instance - called for entities, relationships and entity proxies.
     *
     * @param beanClass name of the class to create
     * @param header header from the entity containing the properties
     * @param entityClassifications classifications associated with the header
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
            elementHeader.setClassifications(this.getEntityClassifications(entityClassifications));
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
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entityProxy entityProxy from the relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>    beanClass,
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
     * Extract the properties from the entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>     beanClass,
                                      EntityDetail entity,
                                      String       methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, entity, methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                         entity.getProperties(),
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
     * Extract the properties from the relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>     beanClass,
                                      Relationship relationship,
                                      String       methodName) throws PropertyServerException
    {
        if (relationship != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, relationship, null, methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            return elementStub;
        }
        else
        {
            super.handleMissingMetadataInstance(beanClass.getName(),
                                                TypeDefCategory.RELATIONSHIP_DEF,
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
            return this.getEntityClassifications(entity.getClassifications());
        }

        return null;
    }

    /**
     * Extract the requested classification from the entity.
     *
     * @param classificationName name of classification
     * @param entity entity containing the classifications
     * @return list of bean classifications
     */
    public Classification getEntityClassification(String       classificationName,
                                                  EntityDetail entity)
    {
        if (entity != null)
        {
            List<Classification> classifications = entity.getClassifications();

            if (classifications != null)
            {
                return this.getEntityClassification(classificationName, classifications);
            }
        }

        return null;
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entityClassifications classifications direct from the entity
     * @return list of bean classifications
     */
    private List<ElementClassification> getEntityClassifications(List<Classification> entityClassifications)
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
     * Retrieve a specific named classification.
     *
     * @param classificationName name of classification
     * @param classifications list of classifications retrieved from the repositories
     * @return null or the requested classification
     */
    private Classification getEntityClassification(String               classificationName,
                                                   List<Classification> classifications)
    {
        if ((classificationName != null) && (classifications != null))
        {
            for (Classification classification : classifications)
            {
                if (classification != null)
                {
                    if (classification.getName().equals(classificationName))
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
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    ContactMethodType getContactMethodTypeFromProperties(InstanceProperties properties)
    {
        final String methodName = "getContactMethodTypeFromProperties";

        ContactMethodType contactMethodType = ContactMethodType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataAPIMapper.CONTACT_METHOD_TYPE_PROPERTY_NAME, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    contactMethodType = ContactMethodType.EMAIL;
                    break;

                case 1:
                    contactMethodType = ContactMethodType.PHONE;
                    break;

                case 2:
                    contactMethodType = ContactMethodType.CHAT;
                    break;

                case 3:
                    contactMethodType = ContactMethodType.PROFILE;
                    break;

                case 4:
                    contactMethodType = ContactMethodType.ACCOUNT;
                    break;

                case 99:
                    contactMethodType = ContactMethodType.OTHER;
                    break;
            }
        }

        return contactMethodType;
    }


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    ServerAssetUseType getServerAssetUseTypeFromProperties(InstanceProperties   properties)
    {
        final String methodName = "getServerAssetUseTypeFromProperties";

        ServerAssetUseType serverAssetUseType = ServerAssetUseType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_TYPE_NAME, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    serverAssetUseType = ServerAssetUseType.OWNS;
                    break;

                case 1:
                    serverAssetUseType = ServerAssetUseType.GOVERNS;
                    break;

                case 2:
                    serverAssetUseType = ServerAssetUseType.MAINTAINS;
                    break;

                case 3:
                    serverAssetUseType = ServerAssetUseType.USES;
                    break;

                case 99:
                    serverAssetUseType = ServerAssetUseType.OTHER;
                    break;
            }
        }

        return serverAssetUseType;
    }
}
