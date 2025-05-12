/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.AnnotationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.surveyaction.properties.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
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
public abstract class SAFConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public SAFConverter(OMRSRepositoryHelper   repositoryHelper,
                        String                 serviceName,
                        String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the Connection bean. It may be a Connection or a VirtualConnection.
     *
     * @param beanClass class of bean that has been requested
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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

            if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.CLASSIFICATION_ANNOTATION.typeName))
            {
                return getNewClassificationAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.DATA_CLASS_ANNOTATION.typeName))
            {
                return getNewDataClassAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName))
            {
                return getNewResourceProfileAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName))
            {
                return getNewDataProfileLogAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName))
            {
                return getNewDataSourcePhysicalStatusAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName))
            {
                return getNewDataSourceMeasurementAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.QUALITY_ANNOTATION.typeName))
            {
                return getNewQualityAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName))
            {
                return getNewRelationshipAdviceAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
            {
                return getNewRequestForActionAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName))
            {
                return getNewSchemaAnalysisAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.SEMANTIC_ANNOTATION.typeName))
            {
                return getNewSemanticAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.ANNOTATION.typeName))
            {
                return getNewAnnotation(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else
            {
                /*
                 * This will throw an exception
                 */
                super.validateInstanceType(OpenMetadataType.ANNOTATION.typeName,
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getNewResourceProfileAnnotation(Class<B>           beanClass,
                                                       EntityDetail       primaryEntity,
                                                       List<EntityDetail> supplementaryEntities,
                                                       List<Relationship> relationships,
                                                       String             methodName) throws PropertyServerException
    {
        try
        {
            ResourceProfileAnnotation annotation = new ResourceProfileAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setProfilePropertyNames(this.removeProfilePropertyNames(remainingProperties));
            annotation.setLength(this.removeLength(remainingProperties));
            annotation.setInferredDataType(this.removeInferredDataType(remainingProperties));
            annotation.setInferredFormat(this.removeInferredFormat(remainingProperties));
            annotation.setInferredLength(this.removeInferredLength(remainingProperties));
            annotation.setInferredPrecision(this.removeInferredPrecision(remainingProperties));
            annotation.setInferredScale(this.removeInferredScale(remainingProperties));
            annotation.setProfileStartDate(this.removeProfileStartDate(remainingProperties));
            annotation.setProfileEndDate(this.removeProfileEndDate(remainingProperties));
            annotation.setProfileProperties(this.removeProfileProperties(remainingProperties));
            annotation.setProfileFlags(this.removeProfileFlags(remainingProperties));
            annotation.setProfileDates(this.removeProfileDates(remainingProperties));
            annotation.setProfileCounts(this.removeProfileCounts(remainingProperties));
            annotation.setProfileDoubles(this.removeProfileDoubles(remainingProperties));
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
            ResourceProfileLogAnnotation annotation = new ResourceProfileLogAnnotation();

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
                        if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName))
                        {
                            EntityProxy endTwo = relationship.getEntityTwoProxy();

                            if (endTwo != null)
                            {
                                String name = repositoryHelper.getStringProperty(serviceName,
                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
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

            annotation.setResourceProfileLogGUIDs(dataProfileLogNames);

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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
            ResourceMeasureAnnotation annotation = new ResourceMeasureAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setResourceProperties(this.removeResourceProperties(remainingProperties));

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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
            ResourcePhysicalStatusAnnotation annotation = new ResourcePhysicalStatusAnnotation();

            InstanceProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                      annotation,
                                                                                      primaryEntity,
                                                                                      supplementaryEntities,
                                                                                      relationships,
                                                                                      methodName);

            annotation.setResourceProperties(this.removeResourceProperties(remainingProperties));
            annotation.setCreateTime(this.removeResourceCreateTime(remainingProperties));
            annotation.setModifiedTime(this.removeResourceUpdateTime(remainingProperties));
            annotation.setLastAccessedTime(this.removeResourceLastAccessedTime(remainingProperties));
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
     * Using the supplied instances, return a new instance of the QualityAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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

            annotation.setSurveyActivity(this.removeSurveyActivity(remainingProperties));
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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
     * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
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

                    if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.ANNOTATION_REVIEW.typeName))
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
                    if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP.typeName))
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

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataProperty.ANNOTATION_STATUS.name);

            if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
            {
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
