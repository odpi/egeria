/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas;


import org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.ffdc.AtlasDiscoveryAuditCode;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.ffdc.AtlasDiscoveryErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasAttributeDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasBusinessMetadataDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasCardinality;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasClassification;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasClassificationDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasMetrics;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasMetricsGeneral;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasMetricsTag;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasRelationship;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasRelationshipDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasRelationshipEndDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasStructDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasTypesDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasVersion;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnalysisReportStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnnotationStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAssetStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DataFieldLink;
import org.odpi.openmetadata.frameworks.discovery.properties.DataProfileAnnotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DataSourceMeasurementAnnotation;
import org.odpi.openmetadata.frameworks.discovery.properties.SchemaAnalysisAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This connector builds a profile of the types and instances in an Apache Atlas server.
 */
public class DiscoverApacheAtlasConnector extends DiscoveryService
{
    /**
     * This map allows the discovery service to look up the data field that corresponds to
     * a specific Apache Atlas Type.
     */
    private final Map<String, String> typeNameToDataFieldGUIDMap = new HashMap<>();

    /**
     * This is the point after which the processing stops.  The default is PROFILE - which
     * is the last analysis step.  It can be changed through configuration properties or
     * analysis properties passed when this discovery service is called.
     */
    private String finalAnalysisStep = DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE;

    private final static String measurementsAnnotationTypeName                  = "Apache Atlas Server Metrics";
    private final static String schemaAnalysisAnnotationTypeName                = "Apache Atlas Type Analysis";
    private final static String attachedEntityTypesAnnotationTypeName           = "Apache Atlas Attached Entity Types";
    private final static String attachedClassificationTypesAnnotationTypeName   = "Apache Atlas Attached Classification Types";
    private final static String attachedRelationshipTypesEnd1AnnotationTypeName = "Apache Atlas End 1 Attached Relationship Types";
    private final static String attachedRelationshipTypesEnd2AnnotationTypeName = "Apache Atlas End 2 Attached Relationship Types";
    private final static String attachedLabelsAnnotationTypeName                = "Apache Atlas Attached Labels";
    private final static String attachedBusinessMetadataTypesAnnotationTypeName = "Apache Atlas Attached Business Metadata Types";
    private final static String attachedEnd1EntityTypesAnnotationTypeName       = "Apache Atlas Attached End 1 Entity Types";
    private final static String attachedEnd2EntityTypesAnnotationTypeName       = "Apache Atlas Attached End 2 Entity Types";
    private final static String attachedPairedEntityTypesAnnotationTypeName     = "Apache Atlas Attached Entity Type Pairs";

    /**
     * Indicates that the discovery service is completely configured and can begin processing.
     * This is where the function of the discovery service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (connectionProperties.getConfigurationProperties() != null)
        {
            Object finalAnalysisStepPropertyObject = connectionProperties.getConfigurationProperties().get(DiscoverApacheAtlasProvider.FINAL_ANALYSIS_STEP_PROPERTY_NAME);

            if (finalAnalysisStepPropertyObject != null)
            {
                String finalAnalysisProperty = finalAnalysisStepPropertyObject.toString();

                if ((finalAnalysisProperty.equals(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS)) ||
                    (finalAnalysisProperty.equals(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_SCHEMA)))
                {
                    finalAnalysisStep = finalAnalysisProperty;
                }
            }
        }

        /*
         * The finalAnalysisStep property in analysisProperties takes precedent over the value in the
         * configuration properties.
         */
        if (discoveryContext.getAnalysisParameters() != null)
        {
            String finalAnalysisProperty = discoveryContext.getAnalysisParameters().get(DiscoverApacheAtlasProvider.FINAL_ANALYSIS_STEP_PROPERTY_NAME);

            if ((finalAnalysisProperty.equals(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS)) ||
                (finalAnalysisProperty.equals(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_SCHEMA)))
            {
                finalAnalysisStep = finalAnalysisProperty;
            }
        }

        try
        {
            String              assetGUID  = discoveryContext.getAssetGUID();
            DiscoveryAssetStore assetStore = discoveryContext.getAssetStore();

            Connector connectorToAsset = assetStore.getConnectorToAsset();

            DiscoveryAnnotationStore annotationStore = discoveryContext.getAnnotationStore();
            DiscoveryAnalysisReportStore discoveryAnalysisReportStore = annotationStore.getDiscoveryReport();

            if (connectorToAsset instanceof ApacheAtlasRESTConnector atlasConnector)
            {
                /*
                 * The STATS analysis step gathers simple statistics from Apache Atlas
                 */
                discoveryAnalysisReportStore.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS);

                AtlasVersion  atlasVersion = atlasConnector.getAtlasVersion();
                AtlasMetrics  atlasMetrics = atlasConnector.getAtlasMetrics();

                if ((atlasMetrics != null) && (atlasMetrics.getData() != null))
                {
                    AtlasMetricsGeneral atlasMetricsGeneral = atlasMetrics.getData().getGeneral();
                    AtlasMetricsTag     atlasMetricsTag     = atlasMetrics.getData().getTag();
                    AtlasTypesDef       atlasTypes          = atlasConnector.getAllTypes();

                    DataSourceMeasurementAnnotation measurementAnnotation = new DataSourceMeasurementAnnotation();
                    measurementAnnotation.setAnnotationType(measurementsAnnotationTypeName);
                    measurementAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS);
                    measurementAnnotation.setExplanation("Metrics maintained by the Apache Atlas server.");

                    Map<String, String> metrics = new HashMap<>();

                    if (atlasMetricsGeneral != null)
                    {
                        metrics.put("entityInstanceCount", Integer.toString(atlasMetricsGeneral.getEntityCount()));
                        metrics.put("classificationCount", Integer.toString(atlasMetricsGeneral.getTagCount()));
                        metrics.put("typeUnusedCount", Integer.toString(atlasMetricsGeneral.getTypeUnusedCount()));
                        metrics.put("typeCount", Integer.toString(atlasMetricsGeneral.getTypeCount()));
                    }

                    if ((atlasMetricsTag != null) && (atlasMetricsTag.getTagEntities() != null))
                    {
                        addMapMetrics(atlasMetricsTag.getTagEntities(), "classifiedEntityCount", metrics);
                    }

                    if (atlasMetrics.getData().getEntity() != null)
                    {
                        addMapMetrics(atlasMetrics.getData().getEntity().get("entityActive"), "entityInstanceCount", metrics);
                        addMapMetrics(atlasMetrics.getData().getEntity().get("entityActive-typeAndSubTypes"), "entityWithSubtypesInstanceCount", metrics);
                    }

                    if (atlasTypes != null)
                    {
                        metrics.put("entityDefs",         Integer.toString(atlasTypes.getEntityDefs().size()));
                        metrics.put("relationshipDefs",   Integer.toString(atlasTypes.getRelationshipDefs().size()));
                        metrics.put("classificationDefs", Integer.toString(atlasTypes.getClassificationDefs().size()));

                        if (atlasTypes.getBusinessMetadataDefs() != null)
                        {
                            metrics.put("businessMetadataDefs", Integer.toString(atlasTypes.getBusinessMetadataDefs().size()));
                        }
                    }

                    measurementAnnotation.setDataSourceProperties(metrics);

                    annotationStore.addAnnotationToDiscoveryReport(measurementAnnotation);
                }

                if (! DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS.equals(finalAnalysisStep))
                {
                    /*
                     * The SCHEMA analysis step is starting now.  It uses the Apache Atlas types to perform a schema analysis that shows how the
                     * Apache Atlas types are linked.
                     */
                    discoveryAnalysisReportStore.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_SCHEMA);

                    SchemaAnalysisAnnotation schemaAnalysisAnnotation = new SchemaAnalysisAnnotation();

                    schemaAnalysisAnnotation.setAnnotationType(schemaAnalysisAnnotationTypeName);
                    schemaAnalysisAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_SCHEMA);
                    schemaAnalysisAnnotation.setExplanation("Linked data fields describing the types defined in the Apache Atlas Server.");
                    schemaAnalysisAnnotation.setSchemaName("Apache Atlas Types: " + atlasVersion.getVersion());
                    schemaAnalysisAnnotation.setSchemaTypeName(atlasVersion.getName());

                    String schemaAnalysisAnnotationGUID = annotationStore.addAnnotationToDiscoveryReport(schemaAnalysisAnnotation);

                    AtlasTypesDef atlasTypesDef = atlasConnector.getAllTypes();

                    for (AtlasEntityDef atlasEntityDef : atlasTypesDef.getEntityDefs())
                    {
                        if (atlasEntityDef != null)
                        {
                            this.getDataFieldForAtlasEntityDef(atlasEntityDef,
                                                               schemaAnalysisAnnotationGUID,
                                                               annotationStore);
                        }
                    }

                    for (AtlasClassificationDef atlasClassificationDef : atlasTypesDef.getClassificationDefs())
                    {
                        if (atlasClassificationDef != null)
                        {
                            this.getDataFieldForAtlasClassificationDef(atlasClassificationDef,
                                                                       schemaAnalysisAnnotationGUID,
                                                                       annotationStore);
                        }
                    }

                    for (AtlasRelationshipDef atlasRelationshipDef : atlasTypesDef.getRelationshipDefs())
                    {
                        if (atlasRelationshipDef != null)
                        {
                            this.getDataFieldForAtlasRelationshipDef(atlasRelationshipDef,
                                                                     schemaAnalysisAnnotationGUID,
                                                                     annotationStore);
                        }
                    }

                    if (atlasTypesDef.getBusinessMetadataDefs() != null)
                    {
                        for (AtlasBusinessMetadataDef atlasBusinessMetadataDef : atlasTypesDef.getBusinessMetadataDefs())
                        {
                            if (atlasBusinessMetadataDef != null)
                            {
                                this.getDataFieldForAtlasBusinessMetadataDef(atlasBusinessMetadataDef,
                                                                             schemaAnalysisAnnotationGUID,
                                                                             annotationStore);
                            }
                        }
                    }

                    if (DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE.equals(finalAnalysisStep))
                    {
                        /*
                         * The final step in the analysis is to retrieve each entity instance from the Apache Atlas repository and
                         * create data profile annotations based on the number of instances of each type discovered.
                         */
                        discoveryAnalysisReportStore.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);

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
                            DataProfileAnnotation dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedClassificationTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of classification types attached to this type of entity.");
                            dataProfileAnnotation.setValueCount(entityTypeMetrics.classificationCount);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);

                            /*
                             * Attached to Relationship At End 1
                             */
                            dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedRelationshipTypesEnd1AnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of different types of relationships attached to this type of entity at End 1.");
                            dataProfileAnnotation.setValueCount(entityTypeMetrics.relationshipEnd1Count);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);

                            /*
                             * Attached to Relationship At End 2
                             */
                            dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedRelationshipTypesEnd2AnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of different types of relationships attached to this type of entity at End 2.");
                            dataProfileAnnotation.setValueCount(entityTypeMetrics.relationshipEnd2Count);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);

                            /*
                             * Attached Labels
                             */
                            dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedLabelsAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of the different labels attached to this type of entity.");
                            dataProfileAnnotation.setValueCount(entityTypeMetrics.labelCount);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);

                            /*
                             * Attached Business Metadata
                             */
                            dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedBusinessMetadataTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of the different types of business metadata properties attached to this type of entity.");
                            dataProfileAnnotation.setValueCount(entityTypeMetrics.labelCount);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);
                        }

                        /*
                         * Each classification data field for classifications in use has a single "Attached Entity Types" data profile annotation.
                         */
                        for (String classificationName : classificationTypeMetricsMap.keySet())
                        {
                            TagTypeMetrics classificationTypeMetrics = classificationTypeMetricsMap.get(classificationName);

                            DataProfileAnnotation dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedEntityTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of entities by type where this classification is attached, organized by entity type.");

                            dataProfileAnnotation.setValueCount(classificationTypeMetrics.entityCount);

                            Map<String, String> additionalProperties = new HashMap<>();
                            additionalProperties.put("classificationInstanceCount", Integer.toString(classificationTypeMetrics.instanceCount));
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(typeNameToDataFieldGUIDMap.get(classificationName), dataProfileAnnotation);
                        }

                        /*
                         * Similarly, the data field for each business metadata properties in use has a single "Attached Entity Types"
                         * data profile annotation.
                         */
                        for (String businessMetadataName : businessMetadataMetricsMap.keySet())
                        {
                            TagTypeMetrics businessMetadataMetrics = businessMetadataMetricsMap.get(businessMetadataName);

                            DataProfileAnnotation dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedEntityTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of entities where this type of business metadata properties are attached, organized by entity type.");
                            dataProfileAnnotation.setValueCount(businessMetadataMetrics.entityCount);

                            Map<String, String> additionalProperties = new HashMap<>();
                            additionalProperties.put("businessMetadataInstanceCount", Integer.toString(businessMetadataMetrics.instanceCount));
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(typeNameToDataFieldGUIDMap.get(businessMetadataName), dataProfileAnnotation);
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
                            DataProfileAnnotation dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedEnd1EntityTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of entity types attached at end 1 of this type of relationship.");
                            dataProfileAnnotation.setValueCount(relationshipTypeMetrics.entityAtEnd1Count);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);

                            /*
                             * End 2
                             */
                            dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedEnd2EntityTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of entity types attached at end 2 of this type of relationship.");
                            dataProfileAnnotation.setValueCount(relationshipTypeMetrics.entityAtEnd2Count);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);

                            /*
                             * Pair
                             */
                            dataProfileAnnotation = new DataProfileAnnotation();

                            dataProfileAnnotation.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE);
                            dataProfileAnnotation.setAnnotationType(attachedPairedEntityTypesAnnotationTypeName);
                            dataProfileAnnotation.setExplanation("Count of entity type pairs for this type of relationship.");
                            dataProfileAnnotation.setValueCount(relationshipTypeMetrics.entityPairCount);
                            dataProfileAnnotation.setAdditionalProperties(additionalProperties);

                            annotationStore.addAnnotationToDataField(dataFieldGUID, dataProfileAnnotation);
                        }
                    }
                }
            }
            else
            {
                String connectorToAssetClassName = "null";

                if (connectorToAsset != null)
                {
                    connectorToAssetClassName = connectorToAsset.getClass().getName();
                }

                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        AtlasDiscoveryAuditCode.WRONG_REST_CONNECTOR.getMessageDefinition(discoveryServiceName,
                                                                                                          connectorToAssetClassName,
                                                                                                          ApacheAtlasRESTConnector.class.getName(),
                                                                                                          assetGUID));
                }

                throw new ConnectorCheckedException(AtlasDiscoveryErrorCode.WRONG_REST_CONNECTOR.getMessageDefinition(discoveryServiceName,
                                                                                                                      connectorToAssetClassName,
                                                                                                                      ApacheAtlasRESTConnector.class.getName(),
                                                                                                                      assetGUID),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(AtlasDiscoveryErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(discoveryServiceName,
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
     * Create a data field that represents the entity def and attach it to the schema analysis annotation.
     *
     * @param atlasEntityDef atlas entity type definition
     * @param schemaAnalysisAnnotationGUID schema analysis annotation to attach the data field to
     * @param annotationStore annotation store for saving new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    private void   getDataFieldForAtlasEntityDef(AtlasEntityDef           atlasEntityDef,
                                                 String                   schemaAnalysisAnnotationGUID,
                                                 DiscoveryAnnotationStore annotationStore) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        this.getBasicDataFieldForTypeDef(atlasEntityDef,
                                         "ENTITY",
                                         atlasEntityDef.getSuperTypes(),
                                         schemaAnalysisAnnotationGUID,
                                         annotationStore);
    }


    /**
     * Create a data field that represents the classification def and attach it to the schema analysis annotation.
     *
     * @param atlasClassificationDef atlas classification type definition
     * @param schemaAnalysisAnnotationGUID schema analysis annotation to attach the data field to
     * @param annotationStore annotation store for saving new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    private void   getDataFieldForAtlasClassificationDef(AtlasClassificationDef   atlasClassificationDef,
                                                         String                   schemaAnalysisAnnotationGUID,
                                                         DiscoveryAnnotationStore annotationStore) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        String dataFieldGUID = this.getBasicDataFieldForTypeDef(atlasClassificationDef,
                                                                "CLASSIFICATION",
                                                                atlasClassificationDef.getSuperTypes(),
                                                                schemaAnalysisAnnotationGUID,
                                                                annotationStore);

        if (atlasClassificationDef.getEntityTypes() != null)
        {
            for (String entityType : atlasClassificationDef.getEntityTypes())
            {
                if ((entityType != null) && (typeNameToDataFieldGUIDMap.get(entityType) != null))
                {
                    annotationStore.linkDataFields(typeNameToDataFieldGUIDMap.get(entityType),
                                                   null,
                                                   dataFieldGUID);
                }
            }
        }
    }


    /**
     * Create a data field that represents the classification def and attach it to the schema analysis annotation.
     *
     * @param atlasBusinessMetadataDef atlas classification type definition
     * @param schemaAnalysisAnnotationGUID schema analysis annotation to attach the data field to
     * @param annotationStore annotation store for saving new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    private void   getDataFieldForAtlasBusinessMetadataDef(AtlasBusinessMetadataDef atlasBusinessMetadataDef,
                                                           String                   schemaAnalysisAnnotationGUID,
                                                           DiscoveryAnnotationStore annotationStore) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        this.getBasicDataFieldForTypeDef(atlasBusinessMetadataDef,
                                         "BUSINESS_METADATA",
                                         null,
                                         schemaAnalysisAnnotationGUID,
                                         annotationStore);
    }


    /**
     * Create a data field that represents the relationship def and attach it to the schema analysis annotation.
     *
     * @param atlasRelationshipDef atlas classification type definition
     * @param schemaAnalysisAnnotationGUID schema analysis annotation to attach the data field to
     * @param annotationStore annotation store for saving new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    private void   getDataFieldForAtlasRelationshipDef(AtlasRelationshipDef     atlasRelationshipDef,
                                                       String                   schemaAnalysisAnnotationGUID,
                                                       DiscoveryAnnotationStore annotationStore) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        String dataFieldGUID = this.getBasicDataFieldForTypeDef(atlasRelationshipDef,
                                                                "RELATIONSHIP",
                                                                null,
                                                                schemaAnalysisAnnotationGUID,
                                                                annotationStore);

        this.addRelationshipEnd(dataFieldGUID, true, atlasRelationshipDef.getEndDef1(), annotationStore);
        this.addRelationshipEnd(dataFieldGUID, false, atlasRelationshipDef.getEndDef2(), annotationStore);
    }


    /**
     * Add a data field link between a relationship data field and an entity data field.
     *
     * @param relationshipDataFieldGUID unique identifier of the relationship data field
     * @param isEnd1 which end is the entity attached to?
     * @param endDef what is the entity?
     * @param annotationStore annotation store for saving new data field link
     * @throws InvalidParameterException one of the  dataFields is invalid.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the data field link to the Annotation store.
     */
    private void addRelationshipEnd(String                   relationshipDataFieldGUID,
                                    boolean                  isEnd1,
                                    AtlasRelationshipEndDef  endDef,
                                    DiscoveryAnnotationStore annotationStore) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        if (endDef != null)
        {
            DataFieldLink relationshipProperties = new DataFieldLink();

            relationshipProperties.setName(endDef.getName());
            relationshipProperties.setTypeName(endDef.getType());
            relationshipProperties.setDescription(endDef.getDescription());

            if (isEnd1)
            {
                relationshipProperties.setRelationshipEnd(1);
            }
            else
            {
                relationshipProperties.setRelationshipEnd(2);
            }

            if (endDef.getCardinality() == AtlasCardinality.SINGLE)
            {
                relationshipProperties.setMaxCardinality(1);
            }

            annotationStore.linkDataFields(relationshipDataFieldGUID,
                                           relationshipProperties,
                                           endDef.getType());
        }
    }


    /**
     * Create a data field that represents the type def and attach it to the schema analysis annotation.
     *
     * @param atlasTypeDef atlas type definition
     * @param atlasCategoryName the category of Atlas type
     * @param atlasSuperTypes the set of super types
     * @param schemaAnalysisAnnotationGUID schema analysis annotation to attach the data field to
     * @param annotationStore annotation store for saving new data field
     * @return data field GUID
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    private String getBasicDataFieldForTypeDef(AtlasStructDef           atlasTypeDef,
                                               String                   atlasCategoryName,
                                               Set<String>              atlasSuperTypes,
                                               String                   schemaAnalysisAnnotationGUID,
                                               DiscoveryAnnotationStore annotationStore) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        DataField dataField = new DataField();

        dataField.setDataFieldName(atlasTypeDef.getName());
        dataField.setDataFieldType(atlasCategoryName);

        if (atlasSuperTypes != null)
        {
            dataField.setDataFieldAliases(new ArrayList<>(atlasSuperTypes));
        }

        dataField.setDataFieldNamespace(atlasTypeDef.getServiceType());
        dataField.setDataFieldDescription(atlasTypeDef.getDescription());
        dataField.setVersion(atlasTypeDef.getVersion());
        dataField.setVersionIdentifier(atlasTypeDef.getTypeVersion());
        dataField.setAdditionalProperties(this.getAttributeNames(atlasTypeDef.getAttributeDefs()));

        String dataFieldGUID = annotationStore.addDataFieldToDiscoveryReport(schemaAnalysisAnnotationGUID, dataField);

        typeNameToDataFieldGUIDMap.put(atlasTypeDef.getName(), dataFieldGUID);

        return dataFieldGUID;
    }


    /**
     * Retrieve the list of attributes for this type.
     *
     * @param attributeDefs list of attributes
     * @return map of attribute names to attribute types
     */
    private Map<String, String> getAttributeNames(List<AtlasAttributeDef> attributeDefs)
    {
        if (attributeDefs != null)
        {
            Map<String, String> additionalProperties = new HashMap<>();

            for (AtlasAttributeDef attributeDef : attributeDefs)
            {
                if (attributeDef != null)
                {
                    additionalProperties.put(attributeDef.getName(), attributeDef.getTypeName());
                }
            }

            if (! additionalProperties.isEmpty())
            {
                return additionalProperties;
            }
        }

        return null;
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
            String pairName = entityOneTypeName + ":" + entityTwoTypeName;

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
