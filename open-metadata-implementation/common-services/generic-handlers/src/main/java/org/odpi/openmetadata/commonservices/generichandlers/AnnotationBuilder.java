/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
    private final String               annotationType;
    private final String               summary;
    private final int                  confidenceLevel;
    private final String               expression;
    private final String               explanation;
    private final String               analysisStep;
    private final String               jsonProperties;
    private final Map<String, String>  additionalProperties;

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
     * Attributes for the ResourceProfileAnnotation
     */
    private int                  length            = 0;
    private String               inferredDataType  = null;
    private String               inferredFormat    = null;
    private int                  inferredLength    = 0;
    private int                  inferredPrecision = 0;
    private int                  inferredScale     = 0;
    private Date                 profileStartDate  = null;
    private Date                 profileEndDate    = null;
    private Map<String, String>  profileProperties = null;
    private Map<String, Boolean> profileFlags      = null;
    private Map<String, Long>    profileCounts     = null;
    private Map<String, Double>  profileDoubles    = null;
    private List<String>         valueList         = null;
    private Map<String, Integer> valueCount        = null;
    private String               valueRangeFrom    = null;
    private String               valueRangeTo      = null;
    private String               averageValue      = null;

    /*
     * Attributes for the ResourceMeasurementAnnotation and ResourcePhysicalStatusAnnotation
     */
    private Map<String, String> resourceProperties = null;

    private Date   createTime     = null;
    private Date   modifiedTime   = null;
    private long   size           = 0;
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
    private String              surveyActivity   = null;
    private String              actionRequested  = null;
    private Map<String, String> actionProperties = null;

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
     * @param profileStartDate profiling start time
     * @param profileEndDate profiling stop time
     * @param profileProperties the map of properties that make up the profile
     * @param profileFlags a set of boolean flags describing different aspects of the data
     * @param profileCounts the map of different profiling counts that have been calculated
     * @param profileDoubles the map of different large profiling counts that have been calculated
     * @param valueList the list of values found in the data field
     * @param valueCount  a map of values to value count for the data field
     * @param valueRangeFrom the lowest value of the data stored in this data field
     * @param valueRangeTo the upper value of the data stored in this data field
     * @param averageValue the average (mean) value of the values stored in the data field
     */
    void setResourceProfileSubtypeProperties(int                  length,
                                             String               inferredDataType,
                                             String               inferredFormat,
                                             int                  inferredLength,
                                             int                  inferredPrecision,
                                             int                  inferredScale,
                                             Date                 profileStartDate,
                                             Date                 profileEndDate,
                                             Map<String, String>  profileProperties,
                                             Map<String, Boolean> profileFlags,
                                             Map<String, Long>    profileCounts,
                                             Map<String, Double>  profileDoubles,
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
        this.profileStartDate  = profileStartDate;
        this.profileEndDate    = profileEndDate;
        this.profileProperties = profileProperties;
        this.profileFlags      = profileFlags;
        this.profileCounts     = profileCounts;
        this.profileDoubles    = profileDoubles;
        this.valueList         = valueList;
        this.valueCount        = valueCount;
        this.valueRangeFrom    = valueRangeFrom;
        this.valueRangeTo      = valueRangeTo;
        this.averageValue      = averageValue;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param resourceProperties properties of the data source
     */
    void setResourceMeasurementSubtypeProperties(Map<String, String> resourceProperties)
    {
        this.resourceProperties = resourceProperties;
    }


    /**
     * Add properties for annotation subtype.
     *
     * @param resourceProperties properties of the data source
     * @param createTime the date and time that the data source was created
     * @param modifiedTime the time that the file was last modified.
     * @param size the size in bytes of the data source
     * @param encoding the encoding of the data source
     */
    void setResourcePhysicalStatusSubtypeProperties(Map<String, String> resourceProperties,
                                                    Date                createTime,
                                                    Date                modifiedTime,
                                                    long                size,
                                                    String              encoding)
    {
        this.resourceProperties = resourceProperties;
        this.createTime         = createTime;
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
        this.surveyActivity  = discoveryActivity;
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
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                  annotationType,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SUMMARY.name,
                                                                  summary,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                               confidenceLevel,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXPRESSION.name,
                                                                  expression,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXPLANATION.name,
                                                                  explanation,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ANALYSIS_STEP.name,
                                                                  analysisStep,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.JSON_PROPERTIES.name,
                                                                  jsonProperties,
                                                                  methodName);
        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                     additionalProperties,
                                                                     methodName);

        setEffectivityDates(properties);

        if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.CLASSIFICATION_ANNOTATION.typeName))
        {
            return addClassificationAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.DATA_CLASS_ANNOTATION.typeName))
        {
            return addDataClassAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName))
        {
            return addResourceProfileAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName))
        {
            return addResourcePhysicalStatusAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName))
        {
            return addResourceMeasurementAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.QUALITY_ANNOTATION.typeName))
        {
            return addQualityAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName))
        {
            return addRelationshipAdviceAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
        {
            return addRequestForActionAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName))
        {
            return addSchemaAnalysisAnnotationInstanceProperties(properties, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.SEMANTIC_ANNOTATION.typeName))
        {
            return addSemanticAnnotationInstanceProperties(properties, methodName);
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
        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name,
                                                                     candidateClassifications,
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
    private InstanceProperties addDataClassAnnotationInstanceProperties(InstanceProperties properties,
                                                                        String             methodName)
    {
        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name,
                                                                       candidateDataClassGUIDs,
                                                                       methodName);

        properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.MATCHING_VALUES.name,
                                                                matchingValues,
                                                                methodName);

        properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.NON_MATCHING_VALUES.name,
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
    private InstanceProperties addResourceProfileAnnotationInstanceProperties(InstanceProperties properties,
                                                                              String             methodName)
    {
        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.LENGTH.name,
                                                               length,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.INFERRED_DATA_TYPE.name,
                                                                  inferredDataType,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.INFERRED_FORMAT.name,
                                                                  inferredFormat,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.INFERRED_LENGTH.name,
                                                               inferredLength,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.INFERRED_PRECISION.name,
                                                               inferredPrecision,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.INFERRED_SCALE.name,
                                                               inferredScale,
                                                               methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.PROFILE_START_DATE.name,
                                                                profileStartDate,
                                                                methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.PROFILE_END_DATE.name,
                                                                profileEndDate,
                                                                methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                                                     profileProperties,
                                                                     methodName);

        properties = repositoryHelper.addBooleanMapPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.PROFILE_FLAGS.name,
                                                                      profileFlags,
                                                                      methodName);

        properties = repositoryHelper.addLongMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataProperty.PROFILE_COUNTS.name,
                                                                   profileCounts,
                                                                   methodName);

        properties = repositoryHelper.addDoubleMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.PROFILE_DOUBLES.name,
                                                                     profileDoubles,
                                                                     methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.VALUE_LIST.name,
                                                                       valueList,
                                                                       methodName);

        properties = repositoryHelper.addIntMapPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.VALUE_COUNT.name,
                                                                  valueCount,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                                  valueRangeFrom,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                                  valueRangeTo,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.AVERAGE_VALUE.name,
                                                                  averageValue,
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
    private InstanceProperties addResourcePhysicalStatusAnnotationInstanceProperties(InstanceProperties properties,
                                                                                     String             methodName)
    {
        properties = this.addResourceMeasurementAnnotationInstanceProperties(properties, methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                                createTime,
                                                                methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                                modifiedTime,
                                                                methodName);

        properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.SIZE.name,
                                                                size,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ENCODING.name,
                                                                  encoding,
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
    private InstanceProperties addResourceMeasurementAnnotationInstanceProperties(InstanceProperties properties,
                                                                                  String             methodName)
    {
        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                                     resourceProperties,
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
    private InstanceProperties addQualityAnnotationInstanceProperties(InstanceProperties properties,
                                                                      String             methodName)
    {
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.QUALITY_DIMENSION.name,
                                                                  qualityDimension,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.QUALITY_SCORE.name,
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
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.RELATED_ENTITY_GUID.name,
                                                                  relatedEntityGUID,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name,
                                                                  relationshipTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name,
                                                                     relationshipProperties,
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
    private InstanceProperties addRequestForActionAnnotationInstanceProperties(InstanceProperties properties,
                                                                               String             methodName)
    {
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ACTION_SOURCE_NAME.name,
                                                                  surveyActivity,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ACTION_REQUESTED.name,
                                                                  actionRequested,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ACTION_PROPERTIES.name,
                                                                     actionProperties,
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
    private InstanceProperties addSchemaAnalysisAnnotationInstanceProperties(InstanceProperties properties,
                                                                             String             methodName)
    {
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SCHEMA_NAME.name,
                                                                  schemaName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SCHEMA_TYPE.name,
                                                                  schemaTypeName,
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
    private InstanceProperties addSemanticAnnotationInstanceProperties(InstanceProperties properties,
                                                                       String             methodName)
    {
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.INFORMAL_TERM.name,
                                                                  informalTerm,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.INFORMAL_CATEGORY.name,
                                                                  informalTopic,
                                                                  methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name,
                                                                       candidateGlossaryTermGUIDs,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name,
                                                                       candidateGlossaryCategoryGUIDs,
                                                                       methodName);

        return properties;
    }
}
