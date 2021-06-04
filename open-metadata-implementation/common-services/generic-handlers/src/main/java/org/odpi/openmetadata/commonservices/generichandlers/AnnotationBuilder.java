/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AnnotationBuilder supports the creation of the entities and
 * relationships that describe an Open Discovery Framework (ODF) Annotation.
 * It takes the properties as native types so that it can be used whether the ODF beans are used or the
 * OMAS has defined its own.
 */
public class AnnotationBuilder extends OpenMetadataAPIGenericBuilder
{
    /*
     * Attributes supported by all annotations
     */
    private String               annotationType;
    private String               summary;
    private int                  confidenceLevel;
    private String               expression;
    private String               explanation;
    private String               analysisStep;
    private String               jsonProperties;
    private Map<String, String>  additionalProperties;

    /*
     * Attributes for the ClassificationAnnotation
     */
    private Map<String, String> candidateClassifications = null;

    /*
     * Attributes for the DataClassAnnotation
     */
    private List<String> candidateDataClassGUIDs = null;
    private long         matchingValues = 0;
    private long         nonMatchingValues = 0;


    /*
     * Attributes for the DataProfileAnnotation
     */
    private int                  length            = 0;
    private String               inferredDataType  = null;
    private String               inferredFormat    = null;
    private int                  inferredLength    = 0;
    private int                  inferredPrecision = 0;
    private int                  inferredScale     = 0;
    private Map<String, String>  profileProperties = null;
    private Map<String, Boolean> profileFlags      = null;
    private Map<String, Long>    profileCounts     = null;
    private List<String>         valueList         = null;
    private Map<String, Integer> valueCount        = null;
    private String               valueRangeFrom    = null;
    private String               valueRangeTo      = null;
    private String               averageValue      = null;

    /*
     * Attributes for the DataSourceMeasurementAnnotation and DataSourcePhysicalStatusAnnotation
     */
    private Map<String, String> dataSourceProperties = null;

    private Date   createTime     = null;
    private Date   modifiedTime   = null;
    private int    size           = 0;
    private String encoding       = null;

    /*
     * Attributes for the QualityAnnotation
     */
    private String qualityDimension = null;
    private int    qualityScore     = 0;


    /*
     * Attributes for RelationshipAdviceAnnotation
     */
    private String              relatedEntityGUID = null;
    private String              relationshipTypeName = null;
    private Map<String, String> relationshipProperties = null;

    /*
     * Attributes for RequestForActionAnnotation
     */
    private String              discoveryActivity = null;
    private String              actionRequested   = null;
    private Map<String, String> actionProperties  = null;

    /*
     * Attributes for the SchemaAnalysisAnnotation
     */
    private  String schemaName     = null;
    private  String schemaTypeName = null;

    /*
     * Attributes for SemanticAnnotation
     */
    private String       informalTerm = null;
    private String       informalTopic = null;
    private List<String> candidateGlossaryTermGUIDs = null;
    private List<String> candidateGlossaryCategoryGUIDs = null;

    /*
     * Attributes for the suspect duplicate annotation
     */
    private List<String> duplicateAnchorGUIDs = null;
    private List<String> matchingPropertyNames = null;
    private List<String> matchingClassificationNames = null;
    private List<String> matchingAttachmentGUIDs = null;
    private List<String> matchingRelationshipGUIDs = null;



    /**
     * Create a builder to convert the properties of the annotation bean into repository services instances.
     * All the common properties of the annotation is passed to the builder.  Properties from the known subtypes are
     * passed on subsequent method calls.
     *
     * @param annotationType type of annotation
     * @param summary summary of the annotation
     * @param confidenceLevel how confident was discovery service that the results are correct
     * @param expression expression that summarizes the results
     * @param explanation explanation of the results
     * @param analysisStep analysis step in the discovery service that produced this annotation
     * @param jsonProperties JSON properties passed to discovery service
     * @param additionalProperties additional properties
     * @param typeName             type name to use for the entity
     * @param typeGUID             type GUID to use for the entity
     * @param extendedProperties   properties from the subtype
     * @param existingClassifications   classifications from the entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    AnnotationBuilder(String               annotationType,
                      String               summary,
                      int                  confidenceLevel,
                      String               expression,
                      String               explanation,
                      String               analysisStep,
                      String               jsonProperties,
                      Map<String, String>  additionalProperties,
                      String               typeGUID,
                      String               typeName,
                      Map<String, Object>  extendedProperties,
                      List<Classification> existingClassifications,
                      OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(typeGUID,
              typeName,
              extendedProperties,
              InstanceStatus.ACTIVE,
              existingClassifications,
              repositoryHelper,
              serviceName,
              serverName);

        this.annotationType         = annotationType;
        this.summary                = summary;
        this.confidenceLevel        = confidenceLevel;
        this.expression             = expression;
        this.explanation            = explanation;
        this.analysisStep           = analysisStep;
        this.jsonProperties         = jsonProperties;
        this.additionalProperties   = additionalProperties;
        this.repositoryHelper       = repositoryHelper;
        this.serviceName            = serviceName;
        this.serverName             = serverName;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param candidateClassifications discovered classifications
     */
    void setClassificationSubtypeProperties(Map<String, String> candidateClassifications)
    {
        this.candidateClassifications = candidateClassifications;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param candidateDataClassGUIDs discovered data class candidates
     * @param matchingValues number of values in the field that match the data class
     * @param nonMatchingValues number of values in the field that do not match the data class
     */
    void setDataClassSubtypeProperties(List<String> candidateDataClassGUIDs,
                                       long         matchingValues,
                                       long         nonMatchingValues)
    {
        this.candidateDataClassGUIDs = candidateDataClassGUIDs;
        this.matchingValues          = matchingValues;
        this.nonMatchingValues       = nonMatchingValues;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param length length of the data field.  Assumes static predefined lengths
     * @param inferredDataType name of the data type that the discovery service believes the field is
     * @param inferredFormat name of the data format that the discovery service believes the field is
     * @param inferredLength length of the data field that has been deduced from the data stored
     * @param inferredPrecision precision of the data field that has been deduced from the data stored
     * @param inferredScale inferred scale used in other properties
     * @param profileProperties the map of properties that make up the profile
     * @param profileFlags a set of boolean flags describing different aspects of the data
     * @param profileCounts the map of different profiling counts that have been calculated
     * @param valueList the list of values found in the data field
     * @param valueCount  a map of values to value count for the data field
     * @param valueRangeFrom the lowest value of the data stored in this data field
     * @param valueRangeTo the upper value of the data stored in this data field
     * @param averageValue the average (mean) value of the values stored in the data field
     */
    void setDataProfileSubtypeProperties(int                  length,
                                         String               inferredDataType,
                                         String               inferredFormat,
                                         int                  inferredLength,
                                         int                  inferredPrecision,
                                         int                  inferredScale,
                                         Map<String, String>  profileProperties,
                                         Map<String, Boolean> profileFlags,
                                         Map<String, Long>    profileCounts,
                                         List<String>         valueList,
                                         Map<String, Integer> valueCount,
                                         String               valueRangeFrom,
                                         String               valueRangeTo,
                                         String               averageValue)
    {
        this.length            = length;
        this.inferredDataType  = inferredDataType;
        this.inferredFormat    = inferredFormat;
        this.inferredLength    = inferredLength;
        this.inferredPrecision = inferredPrecision;
        this.inferredScale     = inferredScale;
        this.profileProperties = profileProperties;
        this.profileFlags      = profileFlags;
        this.profileCounts     = profileCounts;
        this.valueList         = valueList;
        this.valueCount        = valueCount;
        this.valueRangeFrom    = valueRangeFrom;
        this.valueRangeTo      = valueRangeTo;
        this.averageValue      = averageValue;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param dataSourceProperties properties of the data source
     */
    void setDataSourceMeasurementSubtypeProperties(Map<String, String> dataSourceProperties)
    {
        this.dataSourceProperties = dataSourceProperties;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param dataSourceProperties properties of the data source
     * @param createTime the date and time that the data source was created
     * @param modifiedTime the time that the file was last modified.
     * @param size the size in bytes of the data source
     * @param encoding the encoding of the data source
     */
    void setDataSourcePhysicalStatusSubtypeProperties(Map<String, String> dataSourceProperties,
                                                      Date                createTime,
                                                      Date                modifiedTime,
                                                      int                 size,
                                                      String              encoding)
    {
        this.dataSourceProperties = dataSourceProperties;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
        this.size = size;
        this.encoding = encoding;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param qualityDimension the type of quality being measured
     * @param qualityScore a quality score between 0 and 100 - 100 is the best
     */
    void setQualitySubtypeProperties(String qualityDimension,
                                     int    qualityScore)
    {
        this.qualityDimension = qualityDimension;
        this.qualityScore = qualityScore;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param relatedEntityGUID the unique identifier of the object to connect to
     * @param relationshipTypeName the type of relationship to create
     * @param relationshipProperties the properties that should be stored in the relationship
     */
    void setRelationshipAdviceSubtypeProperties(String              relatedEntityGUID,
                                                String              relationshipTypeName,
                                                Map<String, String> relationshipProperties)
    {
        this.relatedEntityGUID = relatedEntityGUID;
        this.relationshipTypeName = relationshipTypeName;
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param discoveryActivity the unique name of the discovery activity
     * @param actionRequested the identifier of the type of action that needs to be run
     * @param actionProperties the properties that will guide the governance action
     */
    void setRequestForActionSubtypeProperties(String              discoveryActivity,
                                              String              actionRequested,
                                              Map<String, String> actionProperties)
    {
        this.discoveryActivity = discoveryActivity;
        this.actionRequested = actionRequested;
        this.actionProperties = actionProperties;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param schemaName name of schema
     * @param schemaTypeName type of schema
     */
    void setSchemaAnalysisSubTypeProperties(String schemaName,
                                            String schemaTypeName)
    {
        this.schemaName     = schemaName;
        this.schemaTypeName = schemaTypeName;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param informalTerm a string that describes the meaning of this data
     * @param informalTopic a string that describes the topic that this data is about
     * @param candidateGlossaryTermGUIDs a list of unique identifiers of glossary terms that describe the meaning of the data
     * @param candidateGlossaryCategoryGUIDs a list of unique identifiers of glossary categories that describe the topic of the data
     */
    void setSemanticSubTypeProperties(String       informalTerm,
                                      String       informalTopic,
                                      List<String> candidateGlossaryTermGUIDs,
                                      List<String> candidateGlossaryCategoryGUIDs)
    {
        this.informalTerm = informalTerm;
        this.informalTopic = informalTopic;
        this.candidateGlossaryTermGUIDs = candidateGlossaryTermGUIDs;
        this.candidateGlossaryCategoryGUIDs = candidateGlossaryCategoryGUIDs;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param duplicateAnchorGUIDs the unique identifiers of the matching assets
     * @param matchingPropertyNames the property names that match
     * @param matchingClassificationNames the classification names that match
     * @param matchingAttachmentGUIDs the unique identifiers of matching attachments
     * @param matchingRelationshipGUIDs the unique identifiers of matching relationships
     */
    void setSuspectDuplicateSubTypeProperties(List<String>         duplicateAnchorGUIDs,
                                              List<String>         matchingPropertyNames,
                                              List<String>         matchingClassificationNames,
                                              List<String>         matchingAttachmentGUIDs,
                                              List<String>         matchingRelationshipGUIDs)
    {
        this.duplicateAnchorGUIDs        = duplicateAnchorGUIDs;
        this.matchingPropertyNames       = matchingPropertyNames;
        this.matchingClassificationNames = matchingClassificationNames;
        this.matchingAttachmentGUIDs     = matchingAttachmentGUIDs;
        this.matchingRelationshipGUIDs   = matchingRelationshipGUIDs;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = new InstanceProperties();

        if (annotationType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ANNOTATION_TYPE_PROPERTY_NAME,
                                                                      annotationType,
                                                                      methodName);
        }

        if (summary != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
                                                                      summary,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.CONFIDENCE_LEVEL_PROPERTY_NAME,
                                                               confidenceLevel,
                                                               methodName);

        if (expression != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME,
                                                                      expression,
                                                                      methodName);
        }

        if (explanation != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.EXPLANATION_PROPERTY_NAME,
                                                                      explanation,
                                                                      methodName);
        }

        if (analysisStep != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ANALYSIS_STEP_PROPERTY_NAME,
                                                                      analysisStep,
                                                                      methodName);
        }

        if (jsonProperties != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.JSON_PROPERTIES_PROPERTY_NAME,
                                                                      jsonProperties,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }


        if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.CLASSIFICATION_ANNOTATION_TYPE_NAME))
        {
            return addClassificationAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DATA_CLASS_ANNOTATION_TYPE_NAME))
        {
            return addDataClassAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DATA_PROFILE_ANNOTATION_TYPE_NAME))
        {
            return addDataProfileAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DS_PHYSICAL_STATUS_ANNOTATION_TYPE_NAME))
        {
            return addDataSourcePhysicalStatusAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DATA_SOURCE_MEASUREMENT_ANNOTATION_TYPE_NAME))
        {
            return addDataSourceMeasurementAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DIVERGENT_VALUE_ANNOTATION_TYPE_NAME))
        {
            return addDivergentValueAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DIVERGENT_CLASSIFICATION_ANNOTATION_TYPE_NAME))
        {
            return addDivergentClassificationAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DIVERGENT_RELATIONSHIP_ANNOTATION_TYPE_NAME))
        {
            return addDivergentRelationshipAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DIVERGENT_ATTACHMENT_ANNOTATION_TYPE_NAME))
        {
            return addDivergentAttachmentValueAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DIVERGENT_ATTACHMENT_CLASS_ANNOTATION_TYPE_NAME))
        {
            return addDivergentAttachmentClassificationAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DIVERGENT_ATTACHMENT_REL_ANNOTATION_TYPE_NAME))
        {
            return addDivergentAttachmentRelationshipAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.QUALITY_ANNOTATION_TYPE_NAME))
        {
            return addQualityAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.RELATIONSHIP_ADVICE_ANNOTATION_TYPE_NAME))
        {
            return addRelationshipAdviceAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.REQUEST_FOR_ACTION_ANNOTATION_TYPE_NAME))
        {
            return addRequestForActionAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.SCHEMA_ANALYSIS_ANNOTATION_TYPE_NAME))
        {
            return addSchemaAnalysisAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.SEMANTIC_ANNOTATION_TYPE_NAME))
        {
            return addSemanticAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.SUSPECT_DUPLICATE_ANNOTATION_TYPE_NAME))
        {
            return this.addSuspectDuplicateAnnotationInstanceProperties(properties, methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addClassificationAnnotationInstanceProperties(InstanceProperties properties,
                                                                             String             methodName)
    {
        if ((candidateClassifications != null) && (!candidateClassifications.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.CANDIDATE_CLASSIFICATIONS_PROPERTY_NAME,
                                                                         candidateClassifications,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDataClassAnnotationInstanceProperties(InstanceProperties properties,
                                                                        String             methodName)
    {
        if ((candidateDataClassGUIDs != null) && (!candidateDataClassGUIDs.isEmpty()))
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.CANDIDATE_DATA_CLASS_GUIDS_PROPERTY_NAME,
                                                                           candidateDataClassGUIDs,
                                                                           methodName);
        }

        properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataAPIMapper.MATCHING_VALUES_PROPERTY_NAME,
                                                                matchingValues,
                                                                methodName);

        properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataAPIMapper.NON_MATCHING_VALUES_PROPERTY_NAME,
                                                                nonMatchingValues,
                                                                methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDataProfileAnnotationInstanceProperties(InstanceProperties properties,
                                                                          String             methodName)
    {
        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.LENGTH_PROPERTY_NAME,
                                                               length,
                                                               methodName);

        if (inferredDataType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.INFERRED_DATA_TYPE_PROPERTY_NAME,
                                                                      inferredDataType,
                                                                      methodName);
        }

        if (inferredFormat != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.INFERRED_FORMAT_PROPERTY_NAME,
                                                                      inferredFormat,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.INFERRED_LENGTH_PROPERTY_NAME,
                                                               inferredLength,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.INFERRED_PRECISION_PROPERTY_NAME,
                                                               inferredPrecision,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.INFERRED_SCALE_PROPERTY_NAME,
                                                               inferredScale,
                                                               methodName);

        if ((profileProperties != null) && (! profileProperties.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.PROFILE_PROPERTIES_PROPERTY_NAME,
                                                                         profileProperties,
                                                                         methodName);
        }

        if ((profileFlags != null) && (! profileFlags.isEmpty()))
        {
            properties = repositoryHelper.addBooleanMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.PROFILE_FLAGS_PROPERTY_NAME,
                                                                         profileFlags,
                                                                         methodName);
        }

        if ((profileCounts != null) && (! profileCounts.isEmpty()))
        {
            properties = repositoryHelper.addLongMapPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.PROFILE_COUNTS_PROPERTY_NAME,
                                                                       profileCounts,
                                                                       methodName);
        }

        if ((valueList != null) && (! valueList.isEmpty()))
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.VALUE_LIST_PROPERTY_NAME,
                                                                           valueList,
                                                                           methodName);
        }

        if ((valueCount != null) && (! valueCount.isEmpty()))
        {
            properties = repositoryHelper.addIntMapPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.VALUE_COUNT_PROPERTY_NAME,
                                                                      valueCount,
                                                                      methodName);
        }

        if (valueRangeFrom != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.VALUE_RANGE_FROM_PROPERTY_NAME,
                                                                      valueRangeFrom,
                                                                      methodName);
        }

        if (valueRangeTo != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.VALUE_RANGE_TO_PROPERTY_NAME,
                                                                      valueRangeTo,
                                                                      methodName);
        }

        if (averageValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.AVERAGE_VALUE_PROPERTY_NAME,
                                                                      averageValue,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDataSourcePhysicalStatusAnnotationInstanceProperties(InstanceProperties properties,
                                                                                       String             methodName)
    {
        properties = this.addDataSourceMeasurementAnnotationInstanceProperties(properties, methodName);

        if (createTime != null)
        {
            properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.SOURCE_CREATE_TIME_PROPERTY_NAME,
                                                                    createTime,
                                                                    methodName);
        }

        if (modifiedTime != null)
        {
            properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.SOURCE_UPDATE_TIME_PROPERTY_NAME,
                                                                    modifiedTime,
                                                                    methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.DS_PHYSICAL_SIZE_PROPERTY_NAME,
                                                               size,
                                                               methodName);

        if (encoding != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DS_PHYSICAL_ENCODING_PROPERTY_NAME,
                                                                      encoding,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDataSourceMeasurementAnnotationInstanceProperties(InstanceProperties properties,
                                                                                    String             methodName)
    {
        if ((dataSourceProperties != null) && (! dataSourceProperties.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.DATA_SOURCE_PROPERTIES_PROPERTY_NAME,
                                                                         dataSourceProperties,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDivergentValueAnnotationInstanceProperties(InstanceProperties properties,
                                                                             String             methodName)
    {
        // todo
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDivergentClassificationAnnotationInstanceProperties(InstanceProperties properties,
                                                                                      String             methodName)
    {
        // todo
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDivergentRelationshipAnnotationInstanceProperties(InstanceProperties properties,
                                                                                    String             methodName)
    {
        // todo
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDivergentAttachmentValueAnnotationInstanceProperties(InstanceProperties properties,
                                                                                       String             methodName)
    {
        // todo
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDivergentAttachmentClassificationAnnotationInstanceProperties(InstanceProperties properties,
                                                                                                String             methodName)
    {
        // todo
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addDivergentAttachmentRelationshipAnnotationInstanceProperties(InstanceProperties properties,
                                                                                              String             methodName)
    {
        // todo
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addQualityAnnotationInstanceProperties(InstanceProperties properties,
                                                                      String             methodName)
    {
        if (qualityDimension != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.QUALITY_DIMENSION_PROPERTY_NAME,
                                                                      qualityDimension,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.QUALITY_SCORE_PROPERTY_NAME,
                                                               qualityScore,
                                                               methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addRelationshipAdviceAnnotationInstanceProperties(InstanceProperties properties,
                                                                                 String             methodName)
    {
        if (relatedEntityGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.RELATED_ENTITY_GUID_PROPERTY_NAME,
                                                                      relatedEntityGUID,
                                                                      methodName);
        }

        if (relationshipTypeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.RELATIONSHIP_TYPE_NAME_PROPERTY_NAME,
                                                                      relationshipTypeName,
                                                                      methodName);
        }

        if ((relationshipProperties != null) && (! relationshipProperties.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.RELATIONSHIP_PROPERTIES_PROPERTY_NAME,
                                                                         relationshipProperties,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addRequestForActionAnnotationInstanceProperties(InstanceProperties properties,
                                                                               String             methodName)
    {
        if (discoveryActivity != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISCOVERY_ACTIVITY_PROPERTY_NAME,
                                                                      discoveryActivity,
                                                                      methodName);
        }

        if (actionRequested != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ACTION_REQUESTED_PROPERTY_NAME,
                                                                      actionRequested,
                                                                      methodName);
        }

        if ((actionProperties != null) && (! actionProperties.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.ACTION_PROPERTIES_PROPERTY_NAME,
                                                                         actionProperties,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addSchemaAnalysisAnnotationInstanceProperties(InstanceProperties properties,
                                                                             String             methodName)
    {
        if (schemaName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.SCHEMA_NAME_PROPERTY_NAME,
                                                                      schemaName,
                                                                      methodName);
        }

        if (schemaTypeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_PROPERTY_NAME,
                                                                      schemaTypeName,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addSemanticAnnotationInstanceProperties(InstanceProperties properties,
                                                                      String             methodName)
    {
        if (informalTerm != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.INFORMAL_TERM_PROPERTY_NAME,
                                                                      informalTerm,
                                                                      methodName);
        }

        if (informalTopic != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.INFORMAL_TOPIC_PROPERTY_NAME,
                                                                      informalTopic,
                                                                      methodName);
        }

        if ((candidateGlossaryTermGUIDs != null) && (! candidateGlossaryTermGUIDs.isEmpty()))
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.CANDIDATE_GLOSSARY_TERM_GUIDS_PROPERTY_NAME,
                                                                           candidateGlossaryTermGUIDs,
                                                                           methodName);
        }

        if ((candidateGlossaryCategoryGUIDs != null) && (! candidateGlossaryCategoryGUIDs.isEmpty()))
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.CANDIDATE_GLOSSARY_CATEGORY_GUIDS_PROPERTY_NAME,
                                                                           candidateGlossaryCategoryGUIDs,
                                                                           methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param properties properties to fill out
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties addSuspectDuplicateAnnotationInstanceProperties(InstanceProperties properties,
                                                                               String             methodName)
    {
        if (duplicateAnchorGUIDs != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.DUPLICATE_ANCHOR_GUIDS_PROPERTY_NAME,
                                                                           duplicateAnchorGUIDs,
                                                                           methodName);
        }

        if (matchingPropertyNames != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.MATCHING_PROPERTY_NAMES_PROPERTY_NAME,
                                                                           matchingPropertyNames,
                                                                           methodName);
        }

        if (matchingClassificationNames != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.MATCHING_CLASSIFICATION_NAMES_PROPERTY_NAME,
                                                                           matchingClassificationNames,
                                                                           methodName);
        }

        if (matchingAttachmentGUIDs != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.MATCHING_ATTACHMENT_GUIDS_PROPERTY_NAME,
                                                                           matchingAttachmentGUIDs,
                                                                           methodName);
        }

        if (matchingRelationshipGUIDs != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.MATCHING_RELATIONSHIP_GUIDS_PROPERTY_NAME,
                                                                           matchingRelationshipGUIDs,
                                                                           methodName);
        }

        return properties;
    }
}
