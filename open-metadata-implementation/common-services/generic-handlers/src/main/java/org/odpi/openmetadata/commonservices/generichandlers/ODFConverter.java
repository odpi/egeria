/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.discovery.properties.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * ODFConverter provides the generic methods for the Data Manager beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public abstract class ODFConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public ODFConverter(OMRSRepositoryHelper   repositoryHelper,
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
    protected ElementHeader getMetadataElementHeader(Class<B> beanClass,
                                                     EntityDetail entity,
                                                     String methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            ElementHeader elementHeader = new ElementHeader();

            elementHeader.setElementSourceServer(serverName);
            elementHeader.setElementOrigin(this.getElementOrigin(entity.getInstanceProvenanceType()));
            elementHeader.setElementMetadataCollectionId(entity.getMetadataCollectionId());
            elementHeader.setElementMetadataCollectionName(entity.getMetadataCollectionName());
            elementHeader.setElementLicense(entity.getInstanceLicense());
            elementHeader.setElementCreatedBy(entity.getCreatedBy());
            elementHeader.setElementUpdatedBy(entity.getUpdatedBy());
            elementHeader.setElementMaintainedBy(entity.getMaintainedBy());
            elementHeader.setElementCreateTime(entity.getCreateTime());
            elementHeader.setElementUpdateTime(entity.getUpdateTime());
            elementHeader.setElementVersion(entity.getVersion());
            elementHeader.setStatus(this.getElementStatus(entity.getStatus()));
            elementHeader.setGUID(entity.getGUID());
            elementHeader.setClassifications(this.getEntityClassifications(entity));
            elementHeader.setType(this.getElementType(entity));
            elementHeader.setMappingProperties(entity.getMappingProperties());

            elementHeader.setGUID(entity.getGUID());
            elementHeader.setType(this.getElementType(entity));
            elementHeader.setURL(entity.getInstanceURL());
            elementHeader.setClassifications(this.getEntityClassifications(entity));

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
    private List<ElementClassification> getEntityClassifications(EntityDetail entity)
    {
        List<ElementClassification> beanClassifications = null;

        if (entity != null)
        {
            List<Classification> entityClassifications = entity.getClassifications();

            if (entityClassifications != null)
            {
                beanClassifications = new ArrayList<>();

                for (Classification entityClassification : entityClassifications)
                {
                    if (entityClassification != null)
                    {
                        ElementClassification beanClassification = new ElementClassification();

                        beanClassification.setElementSourceServer(serverName);
                        beanClassification.setElementOrigin(this.getElementOrigin(entityClassification.getInstanceProvenanceType()));
                        beanClassification.setElementMetadataCollectionId(entityClassification.getMetadataCollectionId());
                        beanClassification.setElementMetadataCollectionName(entityClassification.getMetadataCollectionName());
                        beanClassification.setElementLicense(entityClassification.getInstanceLicense());
                        beanClassification.setElementCreatedBy(entityClassification.getCreatedBy());
                        beanClassification.setElementUpdatedBy( entityClassification.getUpdatedBy());
                        beanClassification.setElementMaintainedBy(entityClassification.getMaintainedBy());
                        beanClassification.setElementCreateTime(entityClassification.getCreateTime());
                        beanClassification.setElementUpdateTime(entityClassification.getUpdateTime());
                        beanClassification.setElementVersion(entityClassification.getVersion());
                        beanClassification.setStatus(this.getElementStatus(entityClassification.getStatus()));

                        beanClassification.setClassificationName(entityClassification.getName());
                        beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));

                        beanClassifications.add(beanClassification);
                    }
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
    private ElementType getElementType(InstanceHeader instanceHeader)
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
    private ElementOrigin getElementOrigin(InstanceProvenanceType instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOrigin.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOrigin.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOrigin.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOrigin.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOrigin.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOrigin.CONFIGURATION;

                case UNKNOWN:
                    return ElementOrigin.UNKNOWN;
            }
        }

        return ElementOrigin.UNKNOWN;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceStatus value from the repository services
     * @return ElementOrigin enum
     */
    private ElementStatus getElementStatus(InstanceStatus instanceStatus)
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
     * Using the supplied instances, return a new instance of the Connection bean. It may be a Connection or a VirtualConnection.
     *
     * @param beanClass class of bean that has been requested
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    protected Annotation getAnnotationBean(Class<B>           beanClass,
                                           EntityDetail       primaryEntity,
                                           List<EntityDetail> supplementaryEntities,
                                           List<Relationship> relationships,
                                           String             methodName) throws PropertyServerException
    {
        /*
         * The entities and relationships may describe either a Connection or a VirtualConnection.
         * This next piece of logic sorts out which type of bean to create.
         */
        if ((primaryEntity != null) && (primaryEntity.getType() != null))
        {
            String actualTypeName = primaryEntity.getType().getTypeDefName();

            if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.CLASSIFICATION_ANNOTATION_TYPE_NAME))
            {
                return getNewClassificationAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DATA_CLASS_ANNOTATION_TYPE_NAME))
            {
                return getNewDataClassAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DATA_PROFILE_ANNOTATION_TYPE_NAME))
            {
                return getNewDataProfileAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DATA_PROFILE_LOG_ANNOTATION_TYPE_NAME))
            {
                return getNewDataProfileLogAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DS_PHYSICAL_STATUS_ANNOTATION_TYPE_NAME))
            {
                return getNewDataSourcePhysicalStatusAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DATA_SOURCE_MEASUREMENT_ANNOTATION_TYPE_NAME))
            {
                return getNewDataSourceMeasurementAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DIVERGENT_VALUE_ANNOTATION_TYPE_NAME))
            {
                return getNewDivergentValueAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DIVERGENT_CLASSIFICATION_ANNOTATION_TYPE_NAME))
            {
                return getNewDivergentClassificationAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DIVERGENT_RELATIONSHIP_ANNOTATION_TYPE_NAME))
            {
                return getNewDivergentRelationshipAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DIVERGENT_ATTACHMENT_ANNOTATION_TYPE_NAME))
            {
                return getNewDivergentAttachmentValueAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DIVERGENT_ATTACHMENT_CLASS_ANNOTATION_TYPE_NAME))
            {
                return getNewDivergentAttachmentClassificationAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.DIVERGENT_ATTACHMENT_REL_ANNOTATION_TYPE_NAME))
            {
                return getNewDivergentAttachmentRelationshipAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.QUALITY_ANNOTATION_TYPE_NAME))
            {
                return getNewQualityAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.RELATIONSHIP_ADVICE_ANNOTATION_TYPE_NAME))
            {
                return getNewRelationshipAdviceAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.REQUEST_FOR_ACTION_ANNOTATION_TYPE_NAME))
            {
                return getNewRequestForActionAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.SCHEMA_ANALYSIS_ANNOTATION_TYPE_NAME))
            {
                return getNewSchemaAnalysisAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.SEMANTIC_ANNOTATION_TYPE_NAME))
            {
                return getNewSemanticAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.SUSPECT_DUPLICATE_ANNOTATION_TYPE_NAME))
            {
                return getNewSuspectDuplicateAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME))
            {
                return getNewAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else
            {
                /*
                 * This will throw an exception
                 */
                super.validateInstanceType(OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME,
                                           beanClass.getName(),
                                           primaryEntity,
                                           methodName);
            }
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DataClassAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewClassificationAnnotation(Class<B>           beanClass,
                                                      EntityDetail       primaryEntity,
                                                      List<EntityDetail> supplementaryEntities,
                                                      List<Relationship> relationships,
                                                      String             methodName) throws PropertyServerException
    {
        try
        {
            ClassificationAnnotation annotation = new ClassificationAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setCandidateClassifications(this.removeCandidateClassifications(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDataClassAnnotation(Class<B>           beanClass,
                                                 EntityDetail       primaryEntity,
                                                 List<EntityDetail> supplementaryEntities,
                                                 List<Relationship> relationships,
                                                 String             methodName) throws PropertyServerException
    {
        try
        {
            DataClassAnnotation annotation = new DataClassAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setCandidateDataClassGUIDs(this.removeCandidateDataClassGUIDs(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DataProfileAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDataProfileAnnotation(Class<B>           beanClass,
                                                   EntityDetail       primaryEntity,
                                                   List<EntityDetail> supplementaryEntities,
                                                   List<Relationship> relationships,
                                                   String             methodName) throws PropertyServerException
    {
        try
        {
            DataProfileAnnotation annotation = new DataProfileAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setLength(this.removeLength(remainingProperties));
            annotation.setInferredDataType(this.removeInferredDataType(remainingProperties));
            annotation.setInferredFormat(this.removeInferredFormat(remainingProperties));
            annotation.setInferredLength(this.removeInferredLength(remainingProperties));
            annotation.setInferredPrecision(this.removeInferredPrecision(remainingProperties));
            annotation.setInferredScale(this.removeInferredScale(remainingProperties));
            annotation.setProfileProperties(this.removeProfileProperties(remainingProperties));
            annotation.setProfileFlags(this.removeProfileFlags(remainingProperties));
            annotation.setProfileCounts(this.removeProfileCounts(remainingProperties));
            annotation.setValueList(this.removeValueList(remainingProperties));
            annotation.setValueCount(this.removeValueCount(remainingProperties));
            annotation.setValueRangeFrom(this.removeValueRangeFrom(remainingProperties));
            annotation.setValueRangeTo(this.removeValueRangeTo(remainingProperties));
            annotation.setAverageValue(this.removeAverageValue(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DataProfileLogAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDataProfileLogAnnotation(Class<B>           beanClass,
                                                      EntityDetail       primaryEntity,
                                                      List<EntityDetail> supplementaryEntities,
                                                      List<Relationship> relationships,
                                                      String             methodName) throws PropertyServerException
    {
        try
        {
            DataProfileLogAnnotation annotation = new DataProfileLogAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);


            List<String> dataProfileLogNames = new ArrayList<>();

            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.DATA_PROFILE_LOG_FILE_TYPE_NAME))
                        {
                            EntityProxy endTwo = relationship.getEntityTwoProxy();

                            if (endTwo != null)
                            {
                                String name = repositoryHelper.getStringProperty(serviceName,
                                                                                 OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                 endTwo.getUniqueProperties(),
                                                                                 methodName);
                                if (name == null)
                                {
                                    name = endTwo.getGUID();
                                }

                                dataProfileLogNames.add(name);
                            }
                        }
                    }
                }
            }

            annotation.setDataProfileLogFileNames(dataProfileLogNames);

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DataSourceMeasurementAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDataSourceMeasurementAnnotation(Class<B>           beanClass,
                                                             EntityDetail       primaryEntity,
                                                             List<EntityDetail> supplementaryEntities,
                                                             List<Relationship> relationships,
                                                             String             methodName) throws PropertyServerException
    {
        try
        {
            DataSourceMeasurementAnnotation annotation = new DataSourceMeasurementAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDataSourceProperties(this.removeDataSourceProperties(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DataSourcePhysicalStatusAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDataSourcePhysicalStatusAnnotation(Class<B>           beanClass,
                                                                EntityDetail       primaryEntity,
                                                                List<EntityDetail> supplementaryEntities,
                                                                List<Relationship> relationships,
                                                                String             methodName) throws PropertyServerException
    {
        try
        {
            DataSourcePhysicalStatusAnnotation annotation = new DataSourcePhysicalStatusAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDataSourceProperties(this.removeDataSourceProperties(remainingProperties));
            annotation.setCreateTime(this.removeSourceCreateTime(remainingProperties));
            annotation.setModifiedTime(this.removeSourceUpdateTime(remainingProperties));
            annotation.setSize(this.removeSize(remainingProperties));
            annotation.setEncoding(this.removeEncoding(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DivergentValueAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDivergentValueAnnotation(Class<B>           beanClass,
                                                      EntityDetail       primaryEntity,
                                                      List<EntityDetail> supplementaryEntities,
                                                      List<Relationship> relationships,
                                                      String             methodName) throws PropertyServerException
    {
        try
        {
            DivergentValueAnnotation annotation = new DivergentValueAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDuplicateAnchorGUID(this.removeDuplicateAnchorGUID(remainingProperties));
            annotation.setDivergentPropertyNames(this.removeDivergentPropertyNames(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DivergentClassificationAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDivergentClassificationAnnotation(Class<B>           beanClass,
                                                               EntityDetail       primaryEntity,
                                                               List<EntityDetail> supplementaryEntities,
                                                               List<Relationship> relationships,
                                                               String             methodName) throws PropertyServerException
    {
        try
        {
            DivergentClassificationAnnotation annotation = new DivergentClassificationAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDuplicateAnchorGUID(this.removeDuplicateAnchorGUID(remainingProperties));
            annotation.setDivergentClassificationName(this.removeDivergentClassificationName(remainingProperties));
            annotation.setDivergentClassificationPropertyNames(this.removeDivergentClassificationPropertyNames(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DivergentRelationshipAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDivergentRelationshipAnnotation(Class<B>           beanClass,
                                                             EntityDetail       primaryEntity,
                                                             List<EntityDetail> supplementaryEntities,
                                                             List<Relationship> relationships,
                                                             String             methodName) throws PropertyServerException
    {
        try
        {
            DivergentRelationshipAnnotation annotation = new DivergentRelationshipAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDuplicateAnchorGUID(this.removeDuplicateAnchorGUID(remainingProperties));
            annotation.setDivergentRelationshipGUID(this.removeDivergentRelationshipGUID(remainingProperties));
            annotation.setDivergentRelationshipPropertyNames(this.removeDivergentRelationshipPropertyNames(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DivergentValueAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDivergentAttachmentValueAnnotation(Class<B>           beanClass,
                                                                EntityDetail       primaryEntity,
                                                                List<EntityDetail> supplementaryEntities,
                                                                List<Relationship> relationships,
                                                                String             methodName) throws PropertyServerException
    {
        try
        {
            DivergentAttachmentValueAnnotation annotation = new DivergentAttachmentValueAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setAttachmentGUID(this.removeAttachmentGUID(remainingProperties));
            annotation.setDuplicateAttachmentGUID(this.removeDuplicateAttachmentGUID(remainingProperties));
            annotation.setDuplicateAnchorGUID(this.removeDuplicateAnchorGUID(remainingProperties));
            annotation.setDivergentPropertyNames(this.removeDivergentPropertyNames(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DivergentClassificationAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDivergentAttachmentClassificationAnnotation(Class<B>           beanClass,
                                                                         EntityDetail       primaryEntity,
                                                                         List<EntityDetail> supplementaryEntities,
                                                                         List<Relationship> relationships,
                                                                         String             methodName) throws PropertyServerException
    {
        try
        {
            DivergentAttachmentClassificationAnnotation annotation = new DivergentAttachmentClassificationAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setAttachmentGUID(this.removeAttachmentGUID(remainingProperties));
            annotation.setDuplicateAttachmentGUID(this.removeDuplicateAttachmentGUID(remainingProperties));
            annotation.setDuplicateAnchorGUID(this.removeDuplicateAnchorGUID(remainingProperties));
            annotation.setDivergentClassificationName(this.removeDivergentClassificationName(remainingProperties));
            annotation.setDivergentClassificationPropertyNames(this.removeDivergentClassificationPropertyNames(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the DivergentRelationshipAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewDivergentAttachmentRelationshipAnnotation(Class<B>           beanClass,
                                                                       EntityDetail       primaryEntity,
                                                                       List<EntityDetail> supplementaryEntities,
                                                                       List<Relationship> relationships,
                                                                       String             methodName) throws PropertyServerException
    {
        try
        {
            DivergentAttachmentRelationshipAnnotation annotation = new DivergentAttachmentRelationshipAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setAttachmentGUID(this.removeAttachmentGUID(remainingProperties));
            annotation.setDuplicateAttachmentGUID(this.removeDuplicateAttachmentGUID(remainingProperties));
            annotation.setDuplicateAnchorGUID(this.removeDuplicateAnchorGUID(remainingProperties));
            annotation.setDivergentRelationshipGUID(this.removeDivergentRelationshipGUID(remainingProperties));
            annotation.setDivergentRelationshipPropertyNames(this.removeDivergentClassificationPropertyNames(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the QualityAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewQualityAnnotation(Class<B>           beanClass,
                                               EntityDetail       primaryEntity,
                                               List<EntityDetail> supplementaryEntities,
                                               List<Relationship> relationships,
                                               String             methodName) throws PropertyServerException
    {
        try
        {
            QualityAnnotation annotation = new QualityAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setQualityDimension(this.removeQualityDimension(remainingProperties));
            annotation.setQualityScore(this.removeQualityScore(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the QualityAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewRelationshipAdviceAnnotation(Class<B>           beanClass,
                                                          EntityDetail       primaryEntity,
                                                          List<EntityDetail> supplementaryEntities,
                                                          List<Relationship> relationships,
                                                          String             methodName) throws PropertyServerException
    {
        try
        {
            RelationshipAdviceAnnotation annotation = new RelationshipAdviceAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setRelatedEntityGUID(this.removeRelatedEntityGUID(remainingProperties));
            annotation.setRelationshipTypeName(this.removeRelationshipTypeName(remainingProperties));
            annotation.setRelationshipProperties(this.removeRelationshipProperties(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the RequestForActionAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewRequestForActionAnnotation(Class<B>           beanClass,
                                                        EntityDetail       primaryEntity,
                                                        List<EntityDetail> supplementaryEntities,
                                                        List<Relationship> relationships,
                                                        String             methodName) throws PropertyServerException
    {
        try
        {
            RequestForActionAnnotation annotation = new RequestForActionAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDiscoveryActivity(this.removeDiscoveryActivity(remainingProperties));
            annotation.setActionRequested(this.removeActionRequested(remainingProperties));
            annotation.setActionProperties(this.removeActionProperties(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewSchemaAnalysisAnnotation(Class<B>           beanClass,
                                                      EntityDetail       primaryEntity,
                                                      List<EntityDetail> supplementaryEntities,
                                                      List<Relationship> relationships,
                                                      String             methodName) throws PropertyServerException
    {
        try
        {
            SchemaAnalysisAnnotation annotation = new SchemaAnalysisAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setSchemaName(this.removeSchemaName(remainingProperties));
            annotation.setSchemaTypeName(this.removeSchemaType(remainingProperties));

            int dataFieldCount = 0;

            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_NAME))
                        {
                            dataFieldCount++;
                        }
                    }
                }
            }

            annotation.setDiscoveredDataFields(dataFieldCount);

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewSemanticAnnotation(Class<B>           beanClass,
                                                EntityDetail       primaryEntity,
                                                List<EntityDetail> supplementaryEntities,
                                                List<Relationship> relationships,
                                                String             methodName) throws PropertyServerException
    {
        try
        {
            SemanticAnnotation annotation = new SemanticAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setInformalTerm(this.removeInformalTerm(remainingProperties));
            annotation.setCandidateGlossaryTermGUIDs(this.removeCandidateGlossaryTermGUIDs(remainingProperties));
            annotation.setInformalTopic(this.removeInformalTopic(remainingProperties));
            annotation.setCandidateGlossaryCategoryGUIDs(this.removeCandidateGlossaryCategoryGUIDs(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the SuspectDuplicateAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewSuspectDuplicateAnnotation(Class<B>           beanClass,
                                                        EntityDetail       primaryEntity,
                                                        List<EntityDetail> supplementaryEntities,
                                                        List<Relationship> relationships,
                                                        String             methodName) throws PropertyServerException
    {
        try
        {
            SuspectDuplicateAnnotation annotation = new SuspectDuplicateAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setDuplicateAnchorGUIDs(this.removeDuplicateAnchorGUIDs(remainingProperties));
            annotation.setMatchingPropertyNames(this.removeMatchingPropertyNames(remainingProperties));
            annotation.setMatchingClassificationNames(this.removeMatchingClassificationNames(remainingProperties));
            annotation.setMatchingAttachmentGUIDs(this.removeMatchingAttachmentGUIDs(remainingProperties));
            annotation.setMatchingRelationshipGUIDs(this.removeMatchingRelationshipGUIDs(remainingProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewAnnotation(Class<B>           beanClass,
                                        EntityDetail       primaryEntity,
                                        List<EntityDetail> supplementaryEntities,
                                        List<Relationship> relationships,
                                        String             methodName) throws PropertyServerException
    {
        try
        {
            Annotation annotation = new Annotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

            return annotation;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Retrieve the annotation properties from an entity and save them in the supplied bean
     *
     * @param annotation bean to fill
     * @param primaryEntity entity to trawl for values
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private InstanceProperties fillInCommonAnnotationProperties(Class<B>           beanClass,
                                                                Annotation         annotation,
                                                                EntityDetail       primaryEntity,
                                                                List<EntityDetail> supplementaryEntities,
                                                                List<Relationship> relationships,
                                                                String             methodName) throws PropertyServerException
    {
        annotation.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

        /*
         * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
         * properties, leaving any subclass properties to be stored in extended properties.
         */
        InstanceProperties instanceProperties = null;
        if (primaryEntity.getProperties() != null)
        {
            instanceProperties = new InstanceProperties(primaryEntity.getProperties());
        }

        annotation.setAnnotationType(this.removeAnnotationType(instanceProperties));
        annotation.setSummary(this.removeSummary(instanceProperties));
        annotation.setConfidenceLevel(this.removeConfidenceLevel(instanceProperties));
        annotation.setExpression(this.removeExpression(instanceProperties));
        annotation.setExplanation(this.removeExplanation(instanceProperties));
        annotation.setAnalysisStep(this.removeAnalysisStep(instanceProperties));
        annotation.setJsonProperties(this.removeJsonProperties(instanceProperties));
        annotation.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

        /*
         * The other entities should include the ConnectorType and Endpoint
         */
        if (supplementaryEntities != null)
        {
            for (EntityDetail entity : supplementaryEntities)
            {
                if ((entity != null) && (entity.getType() != null))
                {
                    String actualTypeName = entity.getType().getTypeDefName();

                    if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.ANNOTATION_REVIEW_TYPE_NAME))
                    {
                        InstanceProperties properties = new InstanceProperties(entity.getProperties());

                        annotation.setReviewDate(this.removeReviewDate(properties));
                        annotation.setSteward(this.removeSteward(properties));
                        annotation.setReviewComment(this.removeComment(properties));
                    }
                }
            }
        }

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.ANNOTATION_REVIEW_LINK_TYPE_NAME))
                    {
                        annotation.setAnnotationStatus(this.getAnnotationStatusFromProperties(instanceProperties));
                    }
                }
            }
        }

        return instanceProperties;
    }


    /**
     * Retrieve the AnnotationStatus enum property from the instance properties of the Annotation Review Link relationship.
     *
     * @param properties  entity properties
     * @return   enum value
     */
    private AnnotationStatus getAnnotationStatusFromProperties(InstanceProperties   properties)
    {
        AnnotationStatus requestStatus = AnnotationStatus.UNKNOWN_STATUS;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.ANNOTATION_STATUS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        requestStatus = AnnotationStatus.NEW_ANNOTATION;
                        break;

                    case 1:
                        requestStatus = AnnotationStatus.REVIEWED_ANNOTATION;
                        break;

                    case 2:
                        requestStatus = AnnotationStatus.APPROVED_ANNOTATION;
                        break;

                    case 3:
                        requestStatus = AnnotationStatus.ACTIONED_ANNOTATION;
                        break;

                    case 4:
                        requestStatus = AnnotationStatus.INVALID_ANNOTATION;
                        break;

                    case 5:
                        requestStatus = AnnotationStatus.IGNORE_ANNOTATION;
                        break;

                    case 98:
                        requestStatus = AnnotationStatus.OTHER_STATUS;
                        break;

                    default:
                        requestStatus = AnnotationStatus.UNKNOWN_STATUS;
                        break;
                }
            }
        }

        return requestStatus;
    }
}
