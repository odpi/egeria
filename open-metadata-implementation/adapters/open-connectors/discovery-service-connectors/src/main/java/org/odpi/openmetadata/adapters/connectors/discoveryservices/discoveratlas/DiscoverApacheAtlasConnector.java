/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas;


import org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.ffdc.AtlasDiscoveryAuditCode;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.ffdc.AtlasDiscoveryErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasAttributeDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasCardinality;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasClassificationDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasMetrics;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasMetricsGeneral;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasMetricsTag;
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
     * Tracks the current analysis step - this is stored in the discovery analysis report so an external observer can track how far
     * the processing has got.
     */
    private String currentAnalysisStep = DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS;

    /**
     * This is the point after which the processing stops.  The default is PROFILE - which
     * is the last analysis step.  It can be changed through configuration properties or
     * analysis properties passed when this discovery service is called.
     */
    private String finalAnalysisStep   = DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_PROFILE;



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

            discoveryAnalysisReportStore.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_STATS);

            if (connectorToAsset instanceof ApacheAtlasRESTConnector atlasConnector)
            {
                AtlasVersion  atlasVersion = atlasConnector.getAtlasVersion();
                AtlasMetrics  atlasMetrics = atlasConnector.getAtlasMetrics();

                if ((atlasMetrics != null) && (atlasMetrics.getData() != null))
                {
                    AtlasMetricsGeneral atlasMetricsGeneral = atlasMetrics.getData().getGeneral();
                    AtlasMetricsTag     atlasMetricsTag     = atlasMetrics.getData().getTag();
                    AtlasTypesDef       atlasTypes          = atlasConnector.getAllTypes();

                    DataSourceMeasurementAnnotation measurementAnnotation = new DataSourceMeasurementAnnotation();
                    measurementAnnotation.setAnnotationType("Apache Atlas Summary Statistics");

                    Map<String, String> metrics = new HashMap<>();

                    if (atlasMetricsGeneral != null)
                    {
                        metrics.put("entityCount", Integer.toString(atlasMetricsGeneral.getEntityCount()));
                        metrics.put("tagCount", Integer.toString(atlasMetricsGeneral.getTagCount()));
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

                discoveryAnalysisReportStore.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_SCHEMA);

                SchemaAnalysisAnnotation schemaAnalysisAnnotation = new SchemaAnalysisAnnotation();

                schemaAnalysisAnnotation.setAnnotationType("Apache Atlas Type Analysis");
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

                discoveryAnalysisReportStore.setAnalysisStep(DiscoverApacheAtlasProvider.ANALYSIS_STEP_NAME_SCHEMA);

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
}
