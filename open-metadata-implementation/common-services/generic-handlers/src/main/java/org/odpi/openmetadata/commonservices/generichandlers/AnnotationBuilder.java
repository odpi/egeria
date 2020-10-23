/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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
     * Attributes for the SchemaAnalysisAnnotation
     */
    private String schemaName     = null;
    private String schemaTypeName = null;

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
        else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.DATA_PROFILE_LOG_ANNOTATION_TYPE_NAME))
        {
            return addDataProfileLogAnnotationInstanceProperties(properties, methodName);
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
    private InstanceProperties addDataClassAnnotationInstanceProperties(InstanceProperties properties,
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
    private InstanceProperties addDataProfileAnnotationInstanceProperties(InstanceProperties properties,
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
    private InstanceProperties addDataProfileLogAnnotationInstanceProperties(InstanceProperties properties,
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
    private InstanceProperties addDataSourcePhysicalStatusAnnotationInstanceProperties(InstanceProperties properties,
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
    private InstanceProperties addDataSourceMeasurementAnnotationInstanceProperties(InstanceProperties properties,
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
    private InstanceProperties addRelationshipAdviceAnnotationInstanceProperties(InstanceProperties properties,
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
    private InstanceProperties addRequestForActionAnnotationInstanceProperties(InstanceProperties properties,
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
