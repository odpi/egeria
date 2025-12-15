/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey;


import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.*;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasAnnotationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasMetric;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasRequestParameter;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.ffdc.AtlasSurveyAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.ffdc.AtlasSurveyErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.SchemaAnalysisAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;

import java.util.*;

/**
 * This connector builds a profile of the types and instances in an Apache Atlas server.
 */
public class SurveyApacheAtlasConnector extends SurveyActionServiceConnector
{
    /**
     * This map allows the survey service to look up the data field that corresponds to
     * a specific Apache Atlas Type.
     */
    private final Map<String, String> typeNameToDataFieldGUIDMap = new HashMap<>();

    /**
     * This is the point after which the processing stops.  The default is PROFILE - which
     * is the last analysis step.  It can be changed through configuration properties or
     * analysis properties passed when this discovery service is called.
     */
    private String finalAnalysisStep = AnalysisStep.PROFILE_DATA.getName();


    /**
     * Indicates that the survey service is completely configured and can begin processing.
     * This is where the function of the discovery service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        if (connectionBean.getConfigurationProperties() != null)
        {
            Object finalAnalysisStepPropertyObject = connectionBean.getConfigurationProperties().get(AtlasRequestParameter.FINAL_ANALYSIS_STEP.getName());

            if (finalAnalysisStepPropertyObject != null)
            {
                String finalAnalysisProperty = finalAnalysisStepPropertyObject.toString();

                if ((finalAnalysisProperty.equals(AnalysisStep.MEASURE_RESOURCE.getName())) ||
                    (finalAnalysisProperty.equals(AnalysisStep.SCHEMA_EXTRACTION.getName())))
                {
                    finalAnalysisStep = finalAnalysisProperty;
                }
            }
        }

        /*
         * The finalAnalysisStep property in analysisProperties takes precedent over the value in the
         * configuration properties.
         */
        if (surveyContext.getRequestParameters() != null)
        {
            String finalAnalysisProperty = surveyContext.getRequestParameters().get(AtlasRequestParameter.FINAL_ANALYSIS_STEP.getName());

            if ((finalAnalysisProperty.equals(AnalysisStep.MEASURE_RESOURCE.getName())) ||
                (finalAnalysisProperty.equals(AnalysisStep.SCHEMA_EXTRACTION.getName())))
            {
                finalAnalysisStep = finalAnalysisProperty;
            }
        }

        try
        {
            String           assetGUID  = surveyContext.getAssetGUID();
            SurveyAssetStore assetStore = surveyContext.getAssetStore();

            connector = super.performCheckAssetAnalysisStep(ApacheAtlasRESTConnector.class,
                                                            OpenMetadataType.SOFTWARE_SERVER.typeName);

            ApacheAtlasRESTConnector atlasConnector = (ApacheAtlasRESTConnector)connector;
            OpenMetadataRootElement  assetUniverse  = assetStore.getAssetProperties();


            AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();
            OpenMetadataStore openMetadataStore = surveyContext.getOpenMetadataStore();

            /*
             * The STATS analysis step gathers simple statistics from Apache Atlas
             */
            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            AtlasVersion  atlasVersion = atlasConnector.getAtlasVersion();
            AtlasMetrics  atlasMetrics = atlasConnector.getAtlasMetrics();

            if ((atlasMetrics != null) && (atlasMetrics.getData() != null))
            {
                AtlasMetricsGeneral atlasMetricsGeneral = atlasMetrics.getData().getGeneral();
                AtlasMetricsTag     atlasMetricsTag     = atlasMetrics.getData().getTag();
                AtlasTypesDef       atlasTypes          = atlasConnector.getAllTypes();

                ResourceMeasureAnnotationProperties measurementAnnotation = new ResourceMeasureAnnotationProperties();
                super.setUpAnnotation(measurementAnnotation, AtlasAnnotationType.MEASUREMENTS);

                Map<String, String> metrics = new HashMap<>();

                if (atlasMetricsGeneral != null)
                {
                    metrics.put(AtlasMetric.ENTITY_INSTANCE_COUNT.getName(), Integer.toString(atlasMetricsGeneral.getEntityCount()));
                    metrics.put(AtlasMetric.CLASSIFICATION_COUNT.getName(), Integer.toString(atlasMetricsGeneral.getTagCount()));
                    metrics.put(AtlasMetric.TYPE_UNUSED_COUNT.getName(), Integer.toString(atlasMetricsGeneral.getTypeUnusedCount()));
                    metrics.put(AtlasMetric.TYPE_COUNT.getName(), Integer.toString(atlasMetricsGeneral.getTypeCount()));
                }

                if ((atlasMetricsTag != null) && (atlasMetricsTag.getTagEntities() != null))
                {
                    addMapMetrics(atlasMetricsTag.getTagEntities(), AtlasMetric.CLASSIFIED_ENTITY_COUNT.getName(), metrics);
                }

                if (atlasMetrics.getData().getEntity() != null)
                {
                    addMapMetrics(atlasMetrics.getData().getEntity().get("entityActive"), "entityInstanceCount", metrics);
                    addMapMetrics(atlasMetrics.getData().getEntity().get("entityActive-typeAndSubTypes"), "entityWithSubtypesInstanceCount", metrics);
                }

                if (atlasTypes != null)
                {
                    metrics.put(AtlasMetric.ENTITY_DEF_COUNT.getName(), Integer.toString(atlasTypes.getEntityDefs().size()));
                    metrics.put(AtlasMetric.RELATIONSHIP_DEF_COUNT.getName(), Integer.toString(atlasTypes.getRelationshipDefs().size()));
                    metrics.put(AtlasMetric.CLASSIFICATION_DEF_COUNT.getName(), Integer.toString(atlasTypes.getClassificationDefs().size()));

                    if (atlasTypes.getBusinessMetadataDefs() != null)
                    {
                        metrics.put(AtlasMetric.BUSINESS_METADATA_DEF_COUNT.getName(), Integer.toString(atlasTypes.getBusinessMetadataDefs().size()));
                    }
                }

                measurementAnnotation.setResourceProperties(metrics);

                annotationStore.addAnnotation(measurementAnnotation, null);
            }

            if (! AnalysisStep.MEASURE_RESOURCE.getName().equals(finalAnalysisStep))
            {
                /*
                 * The SCHEMA analysis step is starting now.  It uses the Apache Atlas types to perform a schema analysis that shows how the
                 * Apache Atlas types are linked.
                 */
                annotationStore.setAnalysisStep(AnalysisStep.SCHEMA_EXTRACTION.getName());

                SchemaAnalysisAnnotationProperties schemaAnalysisAnnotationProperties = new SchemaAnalysisAnnotationProperties();

                super.setUpAnnotation(schemaAnalysisAnnotationProperties, AtlasAnnotationType.TYPE_ANALYSIS);
                schemaAnalysisAnnotationProperties.setSchemaName("Apache Atlas Types: " + atlasVersion.getVersion());
                schemaAnalysisAnnotationProperties.setSchemaType(atlasVersion.getName());

                annotationStore.addAnnotation(schemaAnalysisAnnotationProperties, null);

                /*
                 * The schemaType is the root of the graph schema that describes the apache Atlas types.
                 * This schema type is of type GraphSchemaType, and it is connected to the asset via the
                 * AssetSchemaType Relationship. This call either confirms the root schema type is in place or
                 * creates a new one.
                 */
                this.upsertRootSchemaType(assetGUID, assetUniverse, openMetadataStore);

                /*
                 * Refresh the asse universe to be sure the schema type is present.
                 */
                assetUniverse = assetStore.getAssetProperties();

                /*
                 * Load the existing schema attributes representing the Atlas types.
                 */
                OpenMetadataRootElement rootSchemaType = super.getRootSchemaType(assetUniverse, OpenMetadataType.GRAPH_SCHEMA_TYPE.typeName);

                if ((rootSchemaType != null) && (rootSchemaType.getSchemaAttributes() != null))
                {
                    for (RelatedMetadataElementSummary schemaAttribute : rootSchemaType.getSchemaAttributes())
                    {
                        if ((schemaAttribute != null) && (schemaAttribute.getRelatedElement().getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties))
                        {
                            String schemaAttributeDisplayName = schemaAttributeProperties.getDisplayName();

                            if (schemaAttributeDisplayName != null)
                            {
                                typeNameToDataFieldGUIDMap.put(schemaAttributeDisplayName, schemaAttribute.getRelatedElement().getElementHeader().getGUID());
                            }
                        }
                    }
                }

                /*
                 * Retrieve all the type definitions from Apache Atlas.  They are organized by category.
                 */
                AtlasTypesDef atlasTypesDef = atlasConnector.getAllTypes();

                /*
                 * Step through the entity types
                 */
                for (AtlasEntityDef atlasEntityDef : atlasTypesDef.getEntityDefs())
                {
                    if (atlasEntityDef != null)
                    {
                        this.getSchemaAttributeForAtlasEntityDef(atlasEntityDef,
                                                                 assetUniverse,
                                                                 openMetadataStore);
                    }
                }

                /*
                 * Step through the classifications
                 */
                for (AtlasClassificationDef atlasClassificationDef : atlasTypesDef.getClassificationDefs())
                {
                    if (atlasClassificationDef != null)
                    {
                        this.getSchemaAttributeForAtlasClassificationDef(atlasClassificationDef,
                                                                         assetUniverse,
                                                                         openMetadataStore);
                    }
                }

                /*
                 * Step through the relationships
                 */
                for (AtlasRelationshipDef atlasRelationshipDef : atlasTypesDef.getRelationshipDefs())
                {
                    if (atlasRelationshipDef != null)
                    {
                        this.getSchemaAttributeForAtlasRelationshipDef(atlasRelationshipDef,
                                                                       assetUniverse,
                                                                       openMetadataStore);
                    }
                }

                /*
                 * Step through the business metadata
                 */
                if (atlasTypesDef.getBusinessMetadataDefs() != null)
                {
                    for (AtlasBusinessMetadataDef atlasBusinessMetadataDef : atlasTypesDef.getBusinessMetadataDefs())
                    {
                        if (atlasBusinessMetadataDef != null)
                        {
                            this.getSchemaAttributeForAtlasBusinessMetadataDef(atlasBusinessMetadataDef,
                                                                               assetUniverse,
                                                                               openMetadataStore);
                        }
                    }
                }

                if (AnalysisStep.SCHEMA_EXTRACTION.getName().equals(finalAnalysisStep))
                {
                    /*
                     * The final step in the analysis is to retrieve each entity instance from the Apache Atlas repository and
                     * create data profile annotations based on the number of instances of each type discovered.
                     */
                    annotationStore.setAnalysisStep(AnalysisStep.PROFILE_DATA.getName());

                    Map<String, EntityTypeMetrics>       entityTypeMetricsMap         = new HashMap<>();
                    Map<String, TagTypeMetrics>          classificationTypeMetricsMap = new HashMap<>();
                    Map<String, TagTypeMetrics>          businessMetadataMetricsMap   = new HashMap<>();
                    Map<String, RelationshipTypeMetrics> relationshipTypeMetricsMap   = new HashMap<>();


                    final int maxPageSize = 100;

                    /*
                     * Gather data in the maps
                     */
                    for (AtlasEntityDef entityType : atlasTypesDef.getEntityDefs())
                    {
                        /*
                         * Only process entity types that are top level (ie no subtypes) - this stops us counting an entity that has
                         * multiple subtypes.
                         */
                        if ((entityType.getSuperTypes() == null) || (entityType.getSuperTypes().isEmpty()))
                        {
                            int                     startFrom = 0;
                            List<AtlasEntityHeader> entities  = atlasConnector.getEntitiesForType(entityType.getName(), startFrom, maxPageSize);

                            while ((entities != null) && (! entities.isEmpty()))
                            {
                                for (AtlasEntityHeader entityHeader : entities)
                                {
                                    AtlasEntityWithExtInfo atlasEntityWithExtInfo = atlasConnector.getEntityByGUID(entityHeader.getGuid());

                                    this.addEntityProfile(atlasEntityWithExtInfo.getEntity(),
                                                          this.getAtlasRelationships(atlasEntityWithExtInfo.getEntity(), atlasConnector),
                                                          entityTypeMetricsMap,
                                                          classificationTypeMetricsMap,
                                                          businessMetadataMetricsMap,
                                                          relationshipTypeMetricsMap);
                                }

                                startFrom = startFrom + maxPageSize;
                                entities  = atlasConnector.getEntitiesForType(entityType.getName(), startFrom, maxPageSize);
                            }
                        }
                    }

                    /*
                     * Once the counting is complete, build the data profile annotations to attach to the data fields.
                     * Not all types will have instances, so this logic is driven by the metrics map that only includes types with instances.
                     */
                    for (String entityTypeName : entityTypeMetricsMap.keySet())
                    {
                        EntityTypeMetrics entityTypeMetrics = entityTypeMetricsMap.get(entityTypeName);

                        String dataFieldGUID = typeNameToDataFieldGUIDMap.get(entityTypeName);

                        Map<String, String> additionalProperties = new HashMap<>();
                        additionalProperties.put("entityInstanceCount", Integer.toString(entityTypeMetrics.instanceCount));

                        /*
                         * Attached classifications
                         */
                        ResourceProfileAnnotationProperties resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.ENTITY_ATTACHED_CLASSIFICATIONS);

                        resourceProfileAnnotation.setValueCount(entityTypeMetrics.classificationCount);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);

                        /*
                         * Attached to Relationship At End 1
                         */
                        resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.ENTITY_ATTACHED_TO_END1);
                        resourceProfileAnnotation.setValueCount(entityTypeMetrics.relationshipEnd1Count);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);

                        /*
                         * Attached to Relationship At End 2
                         */
                        resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.ENTITY_ATTACHED_TO_END2);
                        resourceProfileAnnotation.setValueCount(entityTypeMetrics.relationshipEnd2Count);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);

                        /*
                         * Attached Labels
                         */
                        resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.ENTITY_ATTACHED_LABELS);
                        resourceProfileAnnotation.setValueCount(entityTypeMetrics.labelCount);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);

                        /*
                         * Attached Business Metadata
                         */
                        resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.ENTITY_ATTACHED_BIZ_METADATA);
                        resourceProfileAnnotation.setValueCount(entityTypeMetrics.labelCount);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);
                    }

                    /*
                     * Each classification data field for classifications in use has a single "Attached Entity Types" data profile annotation.
                     */
                    for (String classificationName : classificationTypeMetricsMap.keySet())
                    {
                        TagTypeMetrics classificationTypeMetrics = classificationTypeMetricsMap.get(classificationName);

                        ResourceProfileAnnotationProperties resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.CLASSIFICATION_ATTACHED_ENTITIES);
                        resourceProfileAnnotation.setValueCount(classificationTypeMetrics.entityCount);

                        Map<String, String> additionalProperties = new HashMap<>();
                        additionalProperties.put("classificationInstanceCount", Integer.toString(classificationTypeMetrics.instanceCount));
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, typeNameToDataFieldGUIDMap.get(classificationName));
                    }

                    /*
                     * Similarly, the data field for each business metadata properties in use has a single "Attached Entity Types"
                     * data profile annotation.
                     */
                    for (String businessMetadataName : businessMetadataMetricsMap.keySet())
                    {
                        TagTypeMetrics businessMetadataMetrics = businessMetadataMetricsMap.get(businessMetadataName);

                        ResourceProfileAnnotationProperties resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.CLASSIFICATION_ATTACHED_ENTITIES);
                        resourceProfileAnnotation.setValueCount(businessMetadataMetrics.entityCount);

                        Map<String, String> additionalProperties = new HashMap<>();
                        additionalProperties.put("businessMetadataInstanceCount", Integer.toString(businessMetadataMetrics.instanceCount));
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, typeNameToDataFieldGUIDMap.get(businessMetadataName));
                    }

                    /*
                     * Each relationship data field has two attached data profile annotation: one for end1 and the other for end2.
                     * Each annotation provides details of the number of each type of entity attached to that end of the relationship.
                     */
                    for (String relationshipTypeName : relationshipTypeMetricsMap.keySet())
                    {
                        RelationshipTypeMetrics relationshipTypeMetrics = relationshipTypeMetricsMap.get(relationshipTypeName);

                        String dataFieldGUID = typeNameToDataFieldGUIDMap.get(relationshipTypeName);

                        Map<String, String> additionalProperties = new HashMap<>();
                        additionalProperties.put("relationshipInstanceCount", Integer.toString(relationshipTypeMetrics.instanceCount));

                        /*
                         * End 1
                         */
                        ResourceProfileAnnotationProperties resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.END1_ENTITY_TYPES);
                        resourceProfileAnnotation.setValueCount(relationshipTypeMetrics.entityAtEnd1Count);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);

                        /*
                         * End 2
                         */
                        resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.END2_ENTITY_TYPES);
                        resourceProfileAnnotation.setValueCount(relationshipTypeMetrics.entityAtEnd2Count);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);

                        /*
                         * Pair
                         */
                        resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

                        super.setUpAnnotation(resourceProfileAnnotation, AtlasAnnotationType.PAIRED_ENTITY_TYPES);
                        resourceProfileAnnotation.setValueCount(relationshipTypeMetrics.entityPairCount);
                        resourceProfileAnnotation.setAdditionalProperties(additionalProperties);

                        annotationStore.addAnnotation(resourceProfileAnnotation, dataFieldGUID);
                    }
                }
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(AtlasSurveyErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Retrieve all the relationships for an Apache Atlas entity.
     *
     * @param atlasEntity starting entity
     * @param atlasConnector client to Apache Atlas
     * @return list of relationships or null
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private List<AtlasRelationship> getAtlasRelationships(AtlasEntity              atlasEntity,
                                                          ApacheAtlasRESTConnector atlasConnector) throws PropertyServerException
    {
        if ((atlasEntity.getRelationshipAttributes() != null) || (! atlasEntity.getRelationshipAttributes().isEmpty()))
        {
            List<AtlasRelationship> relationships = new ArrayList<>();

            for (String relationshipAttribute : atlasEntity.getRelationshipAttributes().keySet())
            {
                if (atlasEntity.getRelationshipAttributes().get(relationshipAttribute) instanceof Map<?,?> relationshipProperties)
                {
                    Object relationshipGUID = relationshipProperties.get("relationshipGuid");

                    if (relationshipGUID != null)
                    {
                        AtlasRelationship relationship = atlasConnector.getRelationshipByGUID(relationshipGUID.toString());

                        if (relationship != null)
                        {
                            relationships.add(relationship);
                        }
                    }
                }
            }

            if (! relationships.isEmpty())
            {
                return relationships;
            }
        }

        return null;
    }


    /**
     * Add counts to the profile maps for a specific Apache Atlas entity and its relationships.
     *
     * @param atlasEntity atlas entity
     * @param atlasRelationships list of its relationships
     * @param entityTypeMetricsMap map of counts for entity types
     * @param classificationTypeMetricsMap map of counts for classification types
     * @param businessMetadataTypeMetricsMap map of counts for business metadata types
     * @param relationshipTypeMetricsMap map of counts for relationship types
     */
    private void addEntityProfile(AtlasEntity                            atlasEntity,
                                  List<AtlasRelationship>                atlasRelationships,
                                  Map<String, EntityTypeMetrics>         entityTypeMetricsMap,
                                  Map<String, TagTypeMetrics>            classificationTypeMetricsMap,
                                  Map<String, TagTypeMetrics>            businessMetadataTypeMetricsMap,
                                  Map<String, RelationshipTypeMetrics>   relationshipTypeMetricsMap)
    {
        if (atlasEntity != null)
        {
            String entityTypeName = atlasEntity.getTypeName();

            EntityTypeMetrics entityTypeMetrics = entityTypeMetricsMap.get(entityTypeName);

            if (entityTypeMetrics == null)
            {
                entityTypeMetrics = new EntityTypeMetrics();
                entityTypeMetricsMap.put(entityTypeName, entityTypeMetrics);
            }

            entityTypeMetrics.incrementInstanceCount();

            if (atlasEntity.getLabels() != null)
            {
                for (String label : atlasEntity.getLabels())
                {
                    entityTypeMetrics.incrementLabelCount(label);
                }
            }

            if (atlasEntity.getClassifications() != null)
            {
                for (AtlasClassification classification : atlasEntity.getClassifications())
                {
                    String classificationName = classification.getTypeName();

                    entityTypeMetrics.incrementClassificationCount(classificationName);

                    TagTypeMetrics classificationTypeMetrics = classificationTypeMetricsMap.get(classificationName);

                    if (classificationTypeMetrics == null)
                    {
                        classificationTypeMetrics = new TagTypeMetrics();
                        classificationTypeMetricsMap.put(classificationName, classificationTypeMetrics);
                    }

                    classificationTypeMetrics.incrementInstanceCount();
                    classificationTypeMetrics.incrementEntityCount(entityTypeName);
                }
            }

            if (atlasEntity.getBusinessAttributes() != null)
            {
                for (String businessMetadataName : atlasEntity.getBusinessAttributes().keySet())
                {
                    entityTypeMetrics.incrementBusinessMetadataCount(businessMetadataName);

                    TagTypeMetrics businessMetadataMetrics = businessMetadataTypeMetricsMap.get(businessMetadataName);

                    if (businessMetadataMetrics == null)
                    {
                        businessMetadataMetrics = new TagTypeMetrics();
                        classificationTypeMetricsMap.put(businessMetadataName, businessMetadataMetrics);
                    }

                    businessMetadataMetrics.incrementInstanceCount();
                    businessMetadataMetrics.incrementEntityCount(entityTypeName);
                }
            }


            /*
             * Process the relationships attached to the entity.  Notice that because we are processing relationships from an entity perspective,
             * each relationship is processed twice.  Therefore, the relationship oriented counts are only incremented when the entity is at end 1.
             */
            if ((atlasRelationships != null) && (! atlasRelationships.isEmpty()))
            {
                for (AtlasRelationship relationship : atlasRelationships)
                {
                    String relationshipTypeName = relationship.getTypeName();

                    RelationshipTypeMetrics relationshipTypeMetrics = relationshipTypeMetricsMap.get(relationshipTypeName);

                    if (relationshipTypeMetrics == null)
                    {
                        relationshipTypeMetrics = new RelationshipTypeMetrics();
                        relationshipTypeMetricsMap.put(relationshipTypeName, relationshipTypeMetrics);
                    }

                    if (relationship.getEnd1().getGuid().equals(atlasEntity.getGuid()))
                    {
                        relationshipTypeMetrics.incrementInstanceCount(); // only count relationship once
                        relationshipTypeMetrics.incrementEntityPairCount(relationship.getEnd1().getTypeName(),
                                                                         relationship.getEnd2().getTypeName());

                        relationshipTypeMetrics.incrementEntityAtEnd1Count(entityTypeName);
                        entityTypeMetrics.incrementRelationshipEnd1Count(relationshipTypeName);
                    }
                    else
                    {
                        relationshipTypeMetrics.incrementEntityAtEnd2Count(entityTypeName);
                        entityTypeMetrics.incrementRelationshipEnd2Count(relationshipTypeName);
                    }
                }
            }
        }
    }


    /**
     * Add a map of metrics to the consolidated metrics.
     *
     * @param namedMetrics raw metrics map
     * @param metricNamePrefix name of the group of metrics
     * @param consolidatedMetrics consolidated metrics map
     */
    private void addMapMetrics(Map<String, Integer> namedMetrics,
                               String               metricNamePrefix,
                               Map<String, String>  consolidatedMetrics)
    {
        int elementCount = 0;

        for (String elementName : namedMetrics.keySet())
        {
            elementCount ++;
            consolidatedMetrics.put(metricNamePrefix + ": " + elementName, Integer.toString(namedMetrics.get(elementName)));
        }

        consolidatedMetrics.put(metricNamePrefix, Integer.toString(elementCount));
    }


    /**
     * Validate there is a root schema type of the correct type attached to the asset.  If so return its guid.
     * It there is a schema type, but it is the wrong type then throw an exception.
     * If no schema type exists then create a new schema type and attach it to the asset.  Return the GUID of the new schema type.
     *
     * @param assetGUID unique identifier of the asset
     * @param assetUniverse details of the asset from the open metadata repository
     * @param openMetadataStore access to the open metadata repository to use if the schema type needs defining.
     *
     * @throws InvalidParameterException one of the properties passed oto the open metadata store is invalid
     * @throws PropertyServerException problem communicating with the open metadata store
     * @throws UserNotAuthorizedException not authorized to access the open metadata store
     * @throws ConnectorCheckedException the connector is not able to process because the existing asset information is invalid
     */
    private void upsertRootSchemaType(String                  assetGUID,
                                      OpenMetadataRootElement assetUniverse,
                                      OpenMetadataStore       openMetadataStore) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException,
                                                                                        ConnectorCheckedException
    {
        final String methodName = "addRootSchemaType";

        if (assetUniverse != null)
        {
            if (assetUniverse.getSchemaType() != null)
            {
                /*
                 * The root schema type is already in place - check it is of the correct type.
                 */
                if (propertyHelper.isTypeOf(assetUniverse.getSchemaType().getRelatedElement().getElementHeader(), OpenMetadataType.GRAPH_SCHEMA_TYPE.typeName))
                {
                    /*
                     * The root schema type is already defined and set up.
                     */
                    return;
                }

                /*
                 * There is a root schema type, but it is of the wrong type.
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        AtlasSurveyAuditCode.WRONG_ROOT_SCHEMA_TYPE.getMessageDefinition(assetGUID,
                                                                                                         assetUniverse.getSchemaType().getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                         OpenMetadataType.GRAPH_SCHEMA_TYPE.typeName,
                                                                                                         surveyActionServiceName,
                                                                                                         assetUniverse.getSchemaType().toString()));
                }

                throw new ConnectorCheckedException(AtlasSurveyErrorCode.WRONG_ROOT_SCHEMA_TYPE.getMessageDefinition(assetGUID,
                                                                                                                     assetUniverse.getSchemaType().getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                                     OpenMetadataType.GRAPH_SCHEMA_TYPE.typeName,
                                                                                                                     surveyActionServiceName,
                                                                                                                     assetUniverse.getSchemaType().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
            }
            else
            {
                /*
                 * The root schema type is not set up yet.
                 */
                if (assetUniverse.getProperties() instanceof AssetProperties assetProperties)
                {
                    ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                    assetProperties.getQualifiedName() + "_rootSchemaType");

                    properties = propertyHelper.addStringProperty(properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  "Root schema type for " + assetProperties.getResourceName());

                    properties = propertyHelper.addStringProperty(properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  "Graph schema showing the Apache Atlas Types and how they link together.");

                    NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

                    newElementOptions.setAnchorGUID(assetGUID);
                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setParentGUID(assetGUID);
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

                    openMetadataStore.createMetadataElementInStore(OpenMetadataType.GRAPH_SCHEMA_TYPE.typeName,
                                                                   newElementOptions,
                                                                   null,
                                                                   new NewElementProperties(properties),
                                                                   null);
                }
            }
        }
        else
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    AtlasSurveyAuditCode.MISSING_ASSET_UNIVERSE.getMessageDefinition(surveyActionServiceName));
            }

            throw new ConnectorCheckedException(AtlasSurveyErrorCode.MISSING_ASSET_UNIVERSE.getMessageDefinition(surveyActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }



    /**
     * Create a schema that represents the entity def and attach it to the schema analysis annotation.
     *
     * @param atlasEntityDef atlas entity type definition
     * @param assetUniverse details about the Apache atlas Server
     * @param openMetadataStore  store for saving new schema attribute
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the AnnotationProperties store.
     */
    private void getSchemaAttributeForAtlasEntityDef(AtlasEntityDef          atlasEntityDef,
                                                     OpenMetadataRootElement assetUniverse,
                                                     OpenMetadataStore       openMetadataStore) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        this.getSchemaAttributeForTypeDef(OpenMetadataType.GRAPH_VERTEX.typeName,
                                          assetUniverse,
                                          atlasEntityDef,
                                          "ENTITY",
                                          atlasEntityDef.getSuperTypes(),
                                          openMetadataStore);
    }


    /**
     * Create a data field that represents the classification def and attach it to the schema analysis annotation.
     *
     * @param atlasClassificationDef atlas classification type definition
     * @param assetUniverse details about the Apache atlas Server
     * @param openMetadataStore  store for saving new schema attribute
     * @throws InvalidParameterException the schema attribute is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the schema attribute to the open metadata repository.
     */
    private void getSchemaAttributeForAtlasClassificationDef(AtlasClassificationDef  atlasClassificationDef,
                                                             OpenMetadataRootElement assetUniverse,
                                                             OpenMetadataStore       openMetadataStore) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String schemaAttributeGUID = this.getSchemaAttributeForTypeDef(OpenMetadataType.GRAPH_VERTEX.typeName,
                                                                       assetUniverse,
                                                                       atlasClassificationDef,
                                                                       "CLASSIFICATION",
                                                                       atlasClassificationDef.getSuperTypes(),
                                                                       openMetadataStore);

        if (atlasClassificationDef.getEntityTypes() != null)
        {
            for (String entityType : atlasClassificationDef.getEntityTypes())
            {
                if ((entityType != null) && (typeNameToDataFieldGUIDMap.get(entityType) != null))
                {
                    openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName,
                                                                   schemaAttributeGUID,
                                                                   typeNameToDataFieldGUIDMap.get(entityType),
                                                                   null,
                                                                   null,
                                                                   null);
                }
            }
        }
    }


    /**
     * Create a data field that represents the classification def and attach it to the schema analysis annotation.
     *
     * @param atlasBusinessMetadataDef atlas classification type definition
     * @param assetUniverse details about the Apache atlas Server
     * @param openMetadataStore  store for saving new schema attribute
     * @throws InvalidParameterException the schema attribute is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the schema attribute to the open metadata repository.
     */
    private void getSchemaAttributeForAtlasBusinessMetadataDef(AtlasBusinessMetadataDef atlasBusinessMetadataDef,
                                                               OpenMetadataRootElement  assetUniverse,
                                                               OpenMetadataStore        openMetadataStore) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        this.getSchemaAttributeForTypeDef(OpenMetadataType.GRAPH_VERTEX.typeName,
                                          assetUniverse,
                                          atlasBusinessMetadataDef,
                                          "BUSINESS_METADATA",
                                          null,
                                          openMetadataStore);
    }


    /**
     * Create a data field that represents the relationship def and attach it to the schema analysis annotation.
     *
     * @param atlasRelationshipDef atlas classification type definition
     * @param assetUniverse details about the Apache atlas Server
     * @param openMetadataStore  store for saving new schema attribute
     * @throws InvalidParameterException the schema attribute is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the schema attribute to the open metadata repository.
     */
    private void getSchemaAttributeForAtlasRelationshipDef(AtlasRelationshipDef     atlasRelationshipDef,
                                                           OpenMetadataRootElement  assetUniverse,
                                                           OpenMetadataStore        openMetadataStore) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String dataFieldGUID = this.getSchemaAttributeForTypeDef(OpenMetadataType.GRAPH_EDGE.typeName,
                                                                 assetUniverse,
                                                                 atlasRelationshipDef,
                                                                 "RELATIONSHIP",
                                                                 null,
                                                                 openMetadataStore);

        this.addRelationshipEnd(dataFieldGUID, true, atlasRelationshipDef.getEndDef1(), openMetadataStore);
        this.addRelationshipEnd(dataFieldGUID, false, atlasRelationshipDef.getEndDef2(), openMetadataStore);
    }


    /**
     * Add a data field link between a relationship data field and an entity data field.
     *
     * @param relationshipSchemaAttributeGUID unique identifier of the relationship data field
     * @param isEnd1 which end is the entity attached to?
     * @param endDef what is the entity?
     * @param openMetadataStore annotation store for saving new data field link
     * @throws InvalidParameterException one of the  dataFields is invalid.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the data field link to the AnnotationProperties store.
     */
    private void addRelationshipEnd(String                   relationshipSchemaAttributeGUID,
                                    boolean                  isEnd1,
                                    AtlasRelationshipEndDef  endDef,
                                    OpenMetadataStore        openMetadataStore) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        if (endDef != null)
        {
            ElementProperties linkProperties = propertyHelper.addStringProperty(null,
                                                                                OpenMetadataProperty.LINK_TYPE_NAME.name,
                                                                                endDef.getName());
            linkProperties = propertyHelper.addStringProperty(linkProperties,
                                                              OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                              endDef.getType());

            linkProperties = propertyHelper.addStringProperty(linkProperties,
                                                              OpenMetadataProperty.DESCRIPTION.name,
                                                              endDef.getDescription());

            if (isEnd1)
            {
                linkProperties = propertyHelper.addIntProperty(linkProperties,
                                                               OpenMetadataProperty.RELATIONSHIP_END.name,
                                                               1);
            }
            else
            {
                linkProperties = propertyHelper.addIntProperty(linkProperties,
                                                               OpenMetadataProperty.RELATIONSHIP_END.name,
                                                               2);
            }

            if (endDef.getCardinality() == AtlasCardinality.SINGLE)
            {
                linkProperties = propertyHelper.addIntProperty(linkProperties,
                                                               OpenMetadataProperty.MAX_CARDINALITY.name,
                                                               1);
            }

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName,
                                                           relationshipSchemaAttributeGUID,
                                                           typeNameToDataFieldGUIDMap.get(endDef.getType()),
                                                           null,
                                                           null,
                                                           linkProperties);
        }
    }


    /**
     * Create a schema attribute that represents the type def and attach it to the schema analysis annotation.
     *
     * @param openMetadataTypeName the type of graph node to use in the Schema Attribute
     * @param assetUniverse details of the asset describing the Apache Atlas Server
     * @param atlasTypeDef atlas type definition
     * @param atlasCategoryName the category of Atlas type
     * @param atlasSuperTypes the set of super types
     * @param openMetadataStore annotation store for saving new data field
     * @return schema Attribute GUID
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the AnnotationProperties store.
     */
    private String getSchemaAttributeForTypeDef(String                   openMetadataTypeName,
                                                OpenMetadataRootElement  assetUniverse,
                                                AtlasStructDef           atlasTypeDef,
                                                String                   atlasCategoryName,
                                                Set<String>              atlasSuperTypes,
                                                OpenMetadataStore        openMetadataStore) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        if (atlasTypeDef != null)
        {
            String typeAttributeGUID = typeNameToDataFieldGUIDMap.get(atlasTypeDef.getName());

            if (typeAttributeGUID == null)
            {
                /*
                 * This is a new type
                 */
                if (assetUniverse.getProperties() instanceof AssetProperties assetProperties)
                {
                    String qualifiedName = assetProperties.getQualifiedName() + "_" + atlasCategoryName + "_" + atlasTypeDef.getName();
                    ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                           qualifiedName);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                         atlasTypeDef.getName());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                         atlasTypeDef.getDescription());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                         atlasTypeDef.getDescription());

                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                              OpenMetadataProperty.ALIASES.name,
                                                                              new ArrayList<>(atlasSuperTypes));


                    ElementProperties typeProperties = propertyHelper.addStringProperty(null,
                                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                        qualifiedName + "_type");

                    typeProperties = propertyHelper.addStringProperty(typeProperties,
                                                                      OpenMetadataProperty.SCHEMA_TYPE_NAME.name,
                                                                      OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName);

                    typeProperties = propertyHelper.addStringProperty(typeProperties,
                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                      atlasCategoryName);

                    typeProperties = propertyHelper.addStringProperty(typeProperties,
                                                                      OpenMetadataProperty.NAMESPACE.name,
                                                                      atlasTypeDef.getServiceType());

                    typeProperties = propertyHelper.addStringProperty(typeProperties,
                                                                      OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                      atlasTypeDef.getTypeVersion());

                    Map<String, NewElementProperties> initialClassifications = new HashMap<>();

                    initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                               new NewElementProperties(typeProperties));

                    NewElementOptions newElementOptions = new NewElementOptions();

                    newElementOptions.setAnchorGUID(assetUniverse.getElementHeader().getGUID());
                    newElementOptions.setIsOwnAnchor(false);

                    newElementOptions.setParentGUID(assetUniverse.getSchemaType().getRelatedElement().getElementHeader().getGUID());
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);
                    newElementOptions.setParentAtEnd1(true);

                    typeAttributeGUID = openMetadataStore.createMetadataElementInStore(openMetadataTypeName,
                                                                                       newElementOptions,
                                                                                       initialClassifications,
                                                                                       new NewElementProperties(elementProperties),
                                                                                       null);

                    typeNameToDataFieldGUIDMap.put(atlasTypeDef.getName(), typeAttributeGUID);

                    this.setUpAttributeSchemaAttributes(typeAttributeGUID,
                                                        qualifiedName,
                                                        assetUniverse.getElementHeader().getGUID(),
                                                        atlasTypeDef.getAttributeDefs(),
                                                        openMetadataStore);
                }
            }

            return typeAttributeGUID;
        }

        return null;
    }


    /**
     * Retrieve the list of attributes for this type and create a primitive schema attribute for each, attaching it
     * to the type schema attribute using the NestedSchemaAttribute relationship.
     *
     * @param typeSchemaAttributeGUID unique identifier of the schema attribute that describes the Atlas Type
     * @param typeSchemaAttributeQualifiedName unique name of the schema attribute that describes the Atlas Type
     * @param anchorGUID unique identifier of the Asset for the Apache Atlas Server
     * @param attributeDefs list of attributes from the Atlas TypeDef
     * @param openMetadataStore annotation store for saving new data field
     */
    private void setUpAttributeSchemaAttributes(String                  typeSchemaAttributeGUID,
                                                String                  typeSchemaAttributeQualifiedName,
                                                String                  anchorGUID,
                                                List<AtlasAttributeDef> attributeDefs,
                                                OpenMetadataStore       openMetadataStore) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        if (attributeDefs != null)
        {
            for (AtlasAttributeDef attributeDef : attributeDefs)
            {
                if (attributeDef != null)
                {
                    setUpAttributeSchemaAttribute(typeSchemaAttributeGUID,
                                                  typeSchemaAttributeQualifiedName,
                                                  anchorGUID,
                                                  attributeDef,
                                                  openMetadataStore);
                }
            }
        }
    }


    /**
     * Create a primitive schema attribute for an Atlas Attribute Type, attaching it
     * to the type schema attribute using the NestedSchemaAttribute relationship.
     *
     * @param typeSchemaAttributeGUID unique identifier of the schema attribute that describes the Atlas Type
     * @param typeSchemaAttributeQualifiedName unique name of the schema attribute that describes the Atlas Type
     * @param anchorGUID unique identifier of the Asset for the Apache Atlas Server
     * @param attributeDef attribute from the Atlas TypeDef
     * @param openMetadataStore annotation store for saving new data field
     */
    private void setUpAttributeSchemaAttribute(String            typeSchemaAttributeGUID,
                                               String            typeSchemaAttributeQualifiedName,
                                               String            anchorGUID,
                                               AtlasAttributeDef attributeDef,
                                               OpenMetadataStore openMetadataStore) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               typeSchemaAttributeQualifiedName + "_attribute_" + attributeDef.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             attributeDef.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             attributeDef.getDescription());

        ElementProperties relationshipProperties = propertyHelper.addIntProperty(elementProperties,
                                                                                 OpenMetadataProperty.MIN_CARDINALITY.name,
                                                                                 attributeDef.getValuesMinCount());

        relationshipProperties = propertyHelper.addIntProperty(relationshipProperties,
                                                               OpenMetadataProperty.MAX_CARDINALITY.name,
                                                               attributeDef.getValuesMaxCount());
        relationshipProperties = propertyHelper.addIntProperty(relationshipProperties,
                                                               OpenMetadataProperty.POSITION.name,
                                                               0);

        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        ElementProperties classificationProperties = propertyHelper.addStringProperty(null,
                                                                                      OpenMetadataProperty.SCHEMA_TYPE_NAME.name,
                                                                                      OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);

        classificationProperties = propertyHelper.addStringProperty(classificationProperties,
                                                                    OpenMetadataProperty.DATA_TYPE.name,
                                                                    attributeDef.getTypeName());

        classificationProperties = propertyHelper.addStringProperty(classificationProperties,
                                                                    OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                    attributeDef.getDefaultValue());

        initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                   new NewElementProperties(classificationProperties));

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setAnchorGUID(anchorGUID);
        newElementOptions.setIsOwnAnchor(false);

        newElementOptions.setParentGUID(typeSchemaAttributeGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        String schemaAttributeGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                    newElementOptions,
                                                                                    initialClassifications,
                                                                                    new NewElementProperties(elementProperties),
                                                                                    new NewElementProperties(relationshipProperties));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                       typeSchemaAttributeGUID,
                                                       schemaAttributeGUID,
                                                       null,
                                                       null,
                                                       null);
    }


    /**
     * EntityTypeMetrics maintains counts about entity instances of a certain type.
     */
    public static class EntityTypeMetrics
    {
        private       int                  instanceCount         = 0;
        private final Map<String, Integer> classificationCount   = new HashMap<>();
        private final Map<String, Integer> businessMetadataCount = new HashMap<>();
        private final Map<String, Integer> labelCount            = new HashMap<>();
        private final Map<String, Integer> relationshipEnd1Count = new HashMap<>();
        private final Map<String, Integer> relationshipEnd2Count = new HashMap<>();


        /**
         * Increment the number of instances of entities of a particular type.
         */
        void incrementInstanceCount()
        {
            instanceCount = instanceCount + 1;
        }


        /**
         * Increment the count of a particular classification's attachment to this type of entity.
         *
         * @param classificationName name of the classification.
         */
        void incrementClassificationCount(String classificationName)
        {
            Integer currentCount = classificationCount.get(classificationName);

            if (currentCount == null)
            {
                classificationCount.put(classificationName, 0);
            }
            else
            {
                classificationCount.put(classificationName, currentCount + 1);
            }
        }


        /**
         * Increment the count of a particular business metadata attachment to this type of entity.
         *
         * @param businessMetadataName name of the business metadata properties.
         */
        void incrementBusinessMetadataCount(String businessMetadataName)
        {
            Integer currentCount = businessMetadataCount.get(businessMetadataName);

            if (currentCount == null)
            {
                businessMetadataCount.put(businessMetadataName, 0);
            }
            else
            {
                businessMetadataCount.put(businessMetadataName, currentCount + 1);
            }
        }


        /**
         * Increment the count of a particular label attached to this type of entity.
         *
         * @param label label name
         */
        void incrementLabelCount(String label)
        {
            Integer currentCount = labelCount.get(label);

            if (currentCount == null)
            {
                labelCount.put(label, 0);
            }
            else
            {
                labelCount.put(label, currentCount + 1);
            }
        }


        /**
         * Increment the count of the number of entities of this type that are connected to a particular relationship at end1.
         *
         * @param relationshipTypeName name of attached relationship
         */
        void incrementRelationshipEnd1Count(String relationshipTypeName)
        {
            Integer currentCount = relationshipEnd1Count.get(relationshipTypeName);

            if (currentCount == null)
            {
                relationshipEnd1Count.put(relationshipTypeName, 0);
            }
            else
            {
                relationshipEnd1Count.put(relationshipTypeName, currentCount + 1);
            }
        }


        /**
         * Increment the count of the number of entities of this type that are connected to a particular relationship at end2.
         *
         * @param relationshipTypeName name of attached relationship
         */
        void incrementRelationshipEnd2Count(String relationshipTypeName)
        {
            Integer currentCount = relationshipEnd2Count.get(relationshipTypeName);

            if (currentCount == null)
            {
                relationshipEnd2Count.put(relationshipTypeName, 0);
            }
            else
            {
                relationshipEnd2Count.put(relationshipTypeName, currentCount + 1);
            }
        }
    }


    /**
     * TagTypeMetrics maintains counts of the classifications or business metadata attached to this type of entity.
     */
    public static class TagTypeMetrics
    {
        private int                        instanceCount = 0;
        private final Map<String, Integer> entityCount = new HashMap<>();


        /**
         * Increment the count of the instances of this classification
         */
        void incrementInstanceCount()
        {
            instanceCount = instanceCount + 1;
        }


        /**
         * Increment the count of the attachment of this type of classification to a particular entity.
         *
         * @param entityTypeName entity type name
         */
        void incrementEntityCount(String entityTypeName)
        {
            Integer currentCount = entityCount.get(entityTypeName);

            if (currentCount == null)
            {
                entityCount.put(entityTypeName, 0);
            }
            else
            {
                entityCount.put(entityTypeName, currentCount + 1);
            }
        }
    }


    /**
     * RelationshipTypeMetrics manages counts of the usage of a particular relationship type
     */
    public static class RelationshipTypeMetrics
    {
        private int                        instanceCount = 0;
        private final Map<String, Integer> entityAtEnd1Count = new HashMap<>();
        private final Map<String, Integer> entityAtEnd2Count = new HashMap<>();
        private final Map<String, Integer> entityPairCount   = new HashMap<>();


        /**
         * Increment the count of the number of instances of this relationship.
         */
        void incrementInstanceCount()
        {
            instanceCount = instanceCount + 1;
        }


        /**
         * Increment the count of entities of a particular type connected at end 1 of this relationship.
         *
         * @param entityTypeName entity type
         */
        void incrementEntityAtEnd1Count(String entityTypeName)
        {
            Integer currentCount = entityAtEnd1Count.get(entityTypeName);

            if (currentCount == null)
            {
                entityAtEnd1Count.put(entityTypeName, 0);
            }
            else
            {
                entityAtEnd1Count.put(entityTypeName, currentCount + 1);
            }
        }


        /**
         * Increment the count of entities of a particular type connected at end 2 of this relationship.
         *
         * @param entityTypeName entity type
         */
        void incrementEntityAtEnd2Count(String entityTypeName)
        {
            Integer currentCount = entityAtEnd2Count.get(entityTypeName);

            if (currentCount == null)
            {
                entityAtEnd2Count.put(entityTypeName, 0);
            }
            else
            {
                entityAtEnd2Count.put(entityTypeName, currentCount + 1);
            }
        }


        /**
         * Increment the count of entities of particular types connected via this relationship.
         *
         * @param entityOneTypeName entity type
         * @param entityTwoTypeName entity type
         */
        void incrementEntityPairCount(String entityOneTypeName,
                                      String entityTwoTypeName)
        {
            String pairName = entityOneTypeName + "::" + entityTwoTypeName;

            Integer currentCount = entityPairCount.get(pairName);

            if (currentCount == null)
            {
                entityPairCount.put(pairName, 0);
            }
            else
            {
                entityPairCount.put(entityOneTypeName, currentCount + 1);
            }
        }
    }
}
