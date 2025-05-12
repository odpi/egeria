/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.mermaid.AssetGraphMermaidGraphBuilder;
import org.odpi.openmetadata.commonservices.mermaid.AssetLineageGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetconsumer.handlers.LoggingHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetSearchMatches;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CommentType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.InformalTagProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.AssetsResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * The AssetConsumerRESTServices provides the server-side implementation of the Asset Consumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetConsumerRESTServices
{
    private static final AssetConsumerInstanceHandler   instanceHandler      = new AssetConsumerInstanceHandler();
    private        final RESTExceptionHandler           restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger                 restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(AssetConsumerRESTServices.class),
                                                                                                  instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public AssetConsumerRESTServices()
    {
    }


    /**
     * Return service description method.  This method is used to ensure Spring loads this module.
     *
     * @param serverName called server
     * @param userId calling user
     * @return service description
     */
    public RegisteredOMAGServiceResponse getServiceDescription(String serverName,
                                                               String userId)
    {
        final String methodName = "getServiceDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServiceResponse response = new RegisteredOMAGServiceResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setService(instanceHandler.getRegisteredOMAGService(userId,
                                                                         serverName,
                                                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceCode(),
                                                                         methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Return the connection object for the Discovery Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public OCFConnectionResponse getOutTopicConnection(String serverName,
                                                       String userId,
                                                       String callerId)
    {
        final String        methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse response = new OCFConnectionResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /*
     * ===========================================
     * AssetConsumerAssetInterface
     * ===========================================
     */

    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param serverName name of the server instances for this request
     * @param userId the userId of the requesting user.
     * @param connectionName  unique name for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException - there is no asset associated with this connection or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse getAssetForConnectionName(String   serverName,
                                                  String   userId,
                                                  String   connectionName)
    {
        final String connectionNameParameterName = "connectionName";
        final String methodName                  = "getAssetForConnectionName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(handler.getAssetForConnectionName(userId,
                                                               connectionName,
                                                               connectionNameParameterName,
                                                               false,
                                                               false,
                                                               handler.getSupportedZones(),
                                                               new Date(),
                                                               methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     * Note the number of anchored elements returned is controlled by the paging.
     *
     * @param serverName name of the server instances for this request
     * @param userId the userId of the requesting user
     * @param assetGUID  unique identifier for the asset
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetGraphResponse getAssetGraph(String   serverName,
                                            String   userId,
                                            String   assetGUID,
                                            int      startFrom,
                                            int      pageSize)
    {
        final String parameterName = "assetGUID";
        final String methodName    = "getAssetGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetGraphResponse response = new AssetGraphResponse();
        AuditLog           auditLog = null;

        try
        {
            AssetHandler<AssetElement>                   assetHandler           = instanceHandler.getAssetHandler(userId, serverName, methodName);
            ReferenceableHandler<MetadataElementSummary> metadataElementHandler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);
            ReferenceableHandler<MetadataRelationship>   metadataRelationshipHandler = instanceHandler.getMetadataRelationshipHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            /*
             * Retrieve the starting asset.  This proves that the supplied GUID is valid and retrieves
             * the asset details that form the root of the graph.
             */
            AssetElement asset = assetHandler.getBeanFromRepository(userId,
                                                                    assetGUID,
                                                                    parameterName,
                                                                    OpenMetadataType.ASSET.typeName,
                                                                    methodName);

            if (asset != null)
            {
                /*
                 * Save the asset details into the asset graph bean.
                 */
                AssetGraph assetGraph = new AssetGraph(asset);

                /*
                 * This map will hold all of the relationships retrieved for the graph.  It initially,
                 * holds the relationships connected to the starting asset - however, as the related
                 * elements are extracted, their relationships are added.
                 */
                Map<String, Relationship> receivedRelationships = new HashMap<>();

                /*
                 * This list holds the qualified names of information supply chains listed in the lineage relationships.
                 */
                List<String> iscQualifiedNames = new ArrayList<>();

                List<Relationship>  relationships = metadataRelationshipHandler.getAllAttachmentLinks(userId,
                                                                                                      asset.getElementHeader().getGUID(),
                                                                                                      parameterName,
                                                                                                      OpenMetadataType.ASSET.typeName,
                                                                                                      null,
                                                                                                      null,
                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                      null,
                                                                                                      false,
                                                                                                      false,
                                                                                                      new Date(),
                                                                                                      methodName);
                if (relationships != null)
                {
                    for (Relationship relationship : relationships)
                    {
                        if (relationship != null)
                        {
                            /*
                             * Save the relationship if it is structural.  The relationships relating to ongoing dynamic activity are ignored.
                             */
                            if ((! assetHandler.getRepositoryHelper().isTypeOf(instanceHandler.getServiceName(),
                                                                              relationship.getType().getTypeDefName(),
                                                                              OpenMetadataType.TARGET_FOR_ACTION_RELATIONSHIP.typeName)) &&
                                    (! assetHandler.getRepositoryHelper().isTypeOf(instanceHandler.getServiceName(),
                                                                                   relationship.getType().getTypeDefName(),
                                                                                   OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName)))
                            {
                                receivedRelationships.put(relationship.getGUID(), relationship);
                            }

                            /*
                             * The information supply chain is extracted from all relationships to build up the list of
                             * information supply chains that the asset is involved with.
                             */
                            String iscQualifiedName = assetHandler.getRepositoryHelper().getStringProperty(assetHandler.getServiceName(),
                                                                                                           OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                                           relationship.getProperties(),
                                                                                                           methodName);

                            if ((iscQualifiedName != null) && (!iscQualifiedNames.contains(iscQualifiedName)))
                            {
                                iscQualifiedNames.add(iscQualifiedName);
                            }
                        }
                    }
                }


                /*
                 * Now retrieve all the entities anchored to the starting asset.  This is done as a single
                 * query for performance reasons (each call to the repository costs).  Notice the
                 * results are limited by the paging values set by the caller.
                 */
                SearchClassifications         searchClassifications    = new SearchClassifications();
                List<ClassificationCondition> classificationConditions = new ArrayList<>();
                ClassificationCondition       classificationCondition  = new ClassificationCondition();
                SearchProperties              searchProperties         = new SearchProperties();
                List<PropertyCondition>       propertyConditions       = new ArrayList<>();
                PropertyCondition             propertyCondition        = new PropertyCondition();
                PrimitivePropertyValue        primitivePropertyValue   = new PrimitivePropertyValue();

                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(asset.getElementHeader().getGUID());
                primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

                propertyCondition.setProperty(OpenMetadataProperty.ANCHOR_GUID.name);
                propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                propertyCondition.setValue(primitivePropertyValue);
                propertyConditions.add(propertyCondition);
                searchProperties.setMatchCriteria(MatchCriteria.ALL);
                searchProperties.setConditions(propertyConditions);

                classificationCondition.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
                classificationCondition.setMatchProperties(searchProperties);
                classificationConditions.add(classificationCondition);
                searchClassifications.setMatchCriteria(MatchCriteria.ALL);
                searchClassifications.setConditions(classificationConditions);

                List<MetadataElementSummary> anchoredElements = metadataElementHandler.findBeans(userId,
                                                                                                 null,
                                                                                                 null,
                                                                                                 null,
                                                                                                 null,
                                                                                                 searchClassifications,
                                                                                                 null,
                                                                                                 null,
                                                                                                 SequencingOrder.CREATION_DATE_RECENT,
                                                                                                 true,
                                                                                                 false,
                                                                                                 startFrom,
                                                                                                 pageSize,
                                                                                                 assetHandler.getSupportedZones(),
                                                                                                 new Date(),
                                                                                                 methodName);

                assetGraph.setAnchoredElements(anchoredElements);

                /*
                 * For each anchored element, retrieve all of its relationships.  After this we have the information
                 * to create a graph.
                 */
                if (anchoredElements != null)
                {
                    final String anchoredElementParameterName = "anchoredElement.getGUID";

                    for (MetadataElementSummary metadataElement : anchoredElements)
                    {
                        if (metadataElement != null)
                        {
                            relationships = metadataRelationshipHandler.getAllAttachmentLinks(userId,
                                                                                              metadataElement.getElementHeader().getGUID(),
                                                                                              anchoredElementParameterName,
                                                                                              OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                              null,
                                                                                              null,
                                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                                              null,
                                                                                              true,
                                                                                              false,
                                                                                              new Date(),
                                                                                              methodName);
                            if (relationships != null)
                            {
                                for (Relationship relationship : relationships)
                                {
                                    if (relationship != null)
                                    {
                                        if ((! assetHandler.getRepositoryHelper().isTypeOf(instanceHandler.getServiceName(),
                                                                                           relationship.getType().getTypeDefName(),
                                                                                           OpenMetadataType.TARGET_FOR_ACTION_RELATIONSHIP.typeName)) &&
                                                (! assetHandler.getRepositoryHelper().isTypeOf(instanceHandler.getServiceName(),
                                                                                               relationship.getType().getTypeDefName(),
                                                                                               OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName)))
                                        {
                                            receivedRelationships.put(relationship.getGUID(), relationship);
                                        }

                                        String iscQualifiedName = assetHandler.getRepositoryHelper().getStringProperty(assetHandler.getServiceName(),
                                                                                                                       OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                                                       relationship.getProperties(),
                                                                                                                       methodName);

                                        if ((iscQualifiedName != null) && (!iscQualifiedNames.contains(iscQualifiedName)))
                                        {
                                            iscQualifiedNames.add(iscQualifiedName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (! receivedRelationships.isEmpty())
                {
                    OpenMetadataAPIGenericConverter<MetadataRelationship> converter = metadataRelationshipHandler.getConverter();

                    List<MetadataRelationship> metadataRelationships = new ArrayList<>();

                    for (Relationship relationship: receivedRelationships.values())
                    {
                        if (relationship != null)
                        {
                            MetadataRelationship metadataRelationship = converter.getNewRelationshipBean(MetadataRelationship.class,
                                                                                                         relationship,
                                                                                                         methodName);

                            metadataRelationships.add(metadataRelationship);
                        }
                    }

                    assetGraph.setRelationships(metadataRelationships);
                }

                /*
                 * This list contains the information supply chains associated with the asset.
                 */
                List<MetadataElementSummary> informationSupplyChains = new ArrayList<>();

                if (! iscQualifiedNames.isEmpty())
                {
                    final String iscQualifiedNameParameterName = "iscParameterName";

                    for (String iscQualifiedName : iscQualifiedNames)
                    {
                        MetadataElementSummary informationSupplyChain = metadataElementHandler.getBeanByUniqueName(userId,
                                                                                                                   iscQualifiedName,
                                                                                                                   iscQualifiedNameParameterName,
                                                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                   OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeGUID,
                                                                                                                   OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                                                                                                   null,
                                                                                                                   null,
                                                                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                   null,
                                                                                                                   true,
                                                                                                                   false,
                                                                                                                   new Date(),
                                                                                                                   methodName);
                        if (informationSupplyChain != null)
                        {
                            informationSupplyChains.add(informationSupplyChain);
                        }
                    }
                }

                if (! informationSupplyChains.isEmpty())
                {
                    assetGraph.setInformationSupplyChains(informationSupplyChains);
                }

                AssetGraphMermaidGraphBuilder graphBuilder = new AssetGraphMermaidGraphBuilder(assetGraph);
                assetGraph.setMermaidGraph(graphBuilder.getMermaidGraph());
                assetGraph.setInformationSupplyChainMermaidGraph(graphBuilder.getInformationSupplyChainMermaidGraph());
                assetGraph.setFieldLevelLineageGraph(graphBuilder.getFieldLevelLineageGraph());

                response.setAssetGraph(assetGraph);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return all the elements that are linked to an asset using lineage relationships.  The relationships are
     * retrieved both from the asset, and the anchored schema elements
     *
     * @param serverName name of the server instances for this request
     * @param userId the userId of the requesting user
     * @param assetGUID  unique identifier for the asset
     * @param requestBody list of relationship type names to use in the search plus optional supply chain information
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public AssetLineageGraphResponse getAssetLineageGraph(String                       serverName,
                                                          String                       userId,
                                                          String                       assetGUID,
                                                          AssetLineageGraphRequestBody requestBody,
                                                          int                          startFrom,
                                                          int                          pageSize)
    {
        final String methodName = "getAssetLineageGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetLineageGraphResponse response = new AssetLineageGraphResponse();
        AuditLog                  auditLog = null;

        try
        {
            AssetHandler<AssetElement>                   assetHandler                = instanceHandler.getAssetHandler(userId, serverName, methodName);
            ReferenceableHandler<MetadataRelationship>   metadataRelationshipHandler = instanceHandler.getMetadataRelationshipHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<AssetLineageGraphNode>         linkedAssets              = new ArrayList<>();
            List<AssetLineageGraphRelationship> lineageRelationships      = new ArrayList<>();
            Set<String>                         upstreamProcessedAssets   = new HashSet<>();
            Set<String>                         downstreamProcessedAssets = new HashSet<>();

            List<String> lineageRelationshipTypeNames    = null;
            String       limitToInformationSupplyChain   = null;
            String       highlightInformationSupplyChain = null;
            Date         asOfTime                        = null;
            Date         effectiveTime                   = new Date();

            if (requestBody != null)
            {
                lineageRelationshipTypeNames    = getLineageRelationshipTypeNames(requestBody.getRelationshipTypes());
                limitToInformationSupplyChain   = requestBody.getLimitToISCQualifiedName();
                highlightInformationSupplyChain = requestBody.getHighlightISCQualifiedName();
                asOfTime                        = requestBody.getAsOfTime();
                effectiveTime                   = requestBody.getEffectiveTime();
            } 
            
            this.getAssetLineageGraphNodes(userId,
                                           assetGUID,
                                           0,
                                           lineageRelationshipTypeNames,
                                           limitToInformationSupplyChain,
                                           asOfTime,
                                           effectiveTime,
                                           startFrom,
                                           pageSize,
                                           linkedAssets,
                                           lineageRelationships,
                                           upstreamProcessedAssets,
                                           downstreamProcessedAssets,
                                           assetHandler,
                                           metadataRelationshipHandler.getConverter());

            if (! linkedAssets.isEmpty())
            {
                AssetLineageGraph assetLineageGraph = new AssetLineageGraph(linkedAssets.get(0));

                if (linkedAssets.size() > 1)
                {
                    assetLineageGraph.setLinkedAssets(new ArrayList<>(linkedAssets.subList(1, linkedAssets.size())));
                }

                assetLineageGraph.setLineageRelationships(this.deDupLineageRelationships(lineageRelationships));

                AssetLineageGraphMermaidGraphBuilder graphBuilder = new AssetLineageGraphMermaidGraphBuilder(assetLineageGraph, highlightInformationSupplyChain);
                assetLineageGraph.setMermaidGraph(graphBuilder.getMermaidGraph());
                assetLineageGraph.setEdgeMermaidGraph(graphBuilder.getEdgeMermaidGraph());

                response.setAssetLineageGraph(assetLineageGraph);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove any duplicated relationships.
     *
     * @param lineageRelationships derived relationships
     * @return deduplicated list
     */
    private List<AssetLineageGraphRelationship> deDupLineageRelationships(List<AssetLineageGraphRelationship> lineageRelationships)
    {
        Map<String, AssetLineageGraphRelationship> relationshipMap = new HashMap<>();

        for (AssetLineageGraphRelationship lineageRelationship : lineageRelationships)
        {
            relationshipMap.put(lineageRelationship.getEnd1AssetGUID() + lineageRelationship.getEnd2AssetGUID(),
                                lineageRelationship);
        }

        return new ArrayList<>(relationshipMap.values());
    }


    /**
     * Construct a list of the lineage relationship types to search for,
     *
     * @param relationshipTypes relationship type names supplied by the user.
     * @return list of relationship type names
     */
    private List<String> getLineageRelationshipTypeNames(List<String> relationshipTypes)
    {
        List<String> lineageRelationshipTypeNames = relationshipTypes;

        if ((lineageRelationshipTypeNames == null) || (lineageRelationshipTypeNames.isEmpty()))
        {
            lineageRelationshipTypeNames = new ArrayList<>();

            lineageRelationshipTypeNames.add(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);

            /*
             * Pick up requests for actions and ToDos
             */
            lineageRelationshipTypeNames.add(OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName);
            lineageRelationshipTypeNames.add(OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP.typeName);
        }

        return lineageRelationshipTypeNames;
    }

    record LineageLink(Set<String> relationshipTypes,
                       Set<String> informationSupplyChains)
    {}

    /**
     * Retrieve the asset and its lineage relationships.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the asset
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param asOfTime repository time for the query
     * @param effectiveTime effectivity date to use
     * @param startFrom where to start from in retrieval
     * @param pageSize maximum number of elements to receive
     * @param assetHandler handler for retrieving entities/relationships
     * @param linkedAssets set of assets that are upstream of this asset
     * @param lineageRelationships relationships that link the assets together in the lineage graph
     * @param upstreamProcessedAssets list of assets covered so far upstream of the asset
     * @param downstreamProcessedAssets list of assets covered so far downstream of the asset
     * @param converter relationship converter
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    void getAssetLineageGraphNodes(String                                                userId,
                                   String                                                elementGUID,
                                   int                                                   direction,
                                   List<String>                                          lineageRelationshipTypeNames, 
                                   String                                                limitToInformationSupplyChain, 
                                   Date                                                  asOfTime, 
                                   Date                                                  effectiveTime,
                                   int                                                   startFrom,
                                   int                                                   pageSize,
                                   List<AssetLineageGraphNode>                           linkedAssets,
                                   List<AssetLineageGraphRelationship>                   lineageRelationships,
                                   Set<String>                                           upstreamProcessedAssets,
                                   Set<String>                                           downstreamProcessedAssets,
                                   AssetHandler<AssetElement>                            assetHandler,
                                   OpenMetadataAPIGenericConverter<MetadataRelationship> converter) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        Map<String, LineageLink> upstreamAssets   = new HashMap<>();
        Map<String, LineageLink> downstreamAssets = new HashMap<>();

        AssetLineageGraphNode asset = this.getAssetLineageGraphNode(userId,
                                                                    elementGUID,
                                                                    direction,
                                                                    lineageRelationshipTypeNames,
                                                                    limitToInformationSupplyChain,
                                                                    asOfTime,
                                                                    effectiveTime,
                                                                    startFrom,
                                                                    pageSize,
                                                                    upstreamAssets,
                                                                    downstreamAssets,
                                                                    assetHandler,
                                                                    converter);

        if (asset != null)
        {
            if ((direction == 0) || (direction == 1))
            {
                downstreamProcessedAssets.add(asset.getElementHeader().getGUID());
            }
            if ((direction == 0) || (direction == 2))
            {
                upstreamProcessedAssets.add(asset.getElementHeader().getGUID());
            }

            linkedAssets.add(asset);

            /*
             * Process upstream assets
             */
            for (String linkedAssetGUID : upstreamAssets.keySet())
            {
                AssetLineageGraphRelationship assetLineageGraphRelationship = new AssetLineageGraphRelationship();

                LineageLink lineageLink = upstreamAssets.get(linkedAssetGUID);

                if (lineageLink != null)
                {
                    if (lineageLink.relationshipTypes != null)
                    {
                        assetLineageGraphRelationship.setRelationshipTypes(new ArrayList<>(lineageLink.relationshipTypes));
                    }
                    if (lineageLink.informationSupplyChains != null)
                    {
                        assetLineageGraphRelationship.setInformationSupplyChains(new ArrayList<>(lineageLink.informationSupplyChains));
                    }
                }

                assetLineageGraphRelationship.setEnd1AssetGUID(linkedAssetGUID);
                assetLineageGraphRelationship.setEnd2AssetGUID(asset.getElementHeader().getGUID());

                lineageRelationships.add(assetLineageGraphRelationship);

                if (! upstreamProcessedAssets.contains(linkedAssetGUID))
                {
                    this.getAssetLineageGraphNodes(userId,
                                                   linkedAssetGUID,
                                                   2,
                                                   lineageRelationshipTypeNames,
                                                   limitToInformationSupplyChain,
                                                   asOfTime,
                                                   effectiveTime,
                                                   startFrom,
                                                   pageSize,
                                                   linkedAssets,
                                                   lineageRelationships,
                                                   upstreamProcessedAssets,
                                                   downstreamProcessedAssets,
                                                   assetHandler,
                                                   converter);
                }
            }

            /*
             * Process downstream assets
             */
            for (String linkedAssetGUID : downstreamAssets.keySet())
            {
                AssetLineageGraphRelationship assetLineageGraphRelationship = new AssetLineageGraphRelationship();

                LineageLink lineageLink = downstreamAssets.get(linkedAssetGUID);

                if (lineageLink != null)
                {
                    if (lineageLink.relationshipTypes != null)
                    {
                        assetLineageGraphRelationship.setRelationshipTypes(new ArrayList<>(lineageLink.relationshipTypes));
                    }
                    if (lineageLink.informationSupplyChains != null)
                    {
                        assetLineageGraphRelationship.setInformationSupplyChains(new ArrayList<>(lineageLink.informationSupplyChains));
                    }
                }

                assetLineageGraphRelationship.setEnd1AssetGUID(asset.getElementHeader().getGUID());
                assetLineageGraphRelationship.setEnd2AssetGUID(linkedAssetGUID);

                lineageRelationships.add(assetLineageGraphRelationship);

                if (! downstreamProcessedAssets.contains(linkedAssetGUID))
                {
                    this.getAssetLineageGraphNodes(userId,
                                                   linkedAssetGUID,
                                                   1,
                                                   lineageRelationshipTypeNames,
                                                   limitToInformationSupplyChain,
                                                   asOfTime,
                                                   effectiveTime,
                                                   startFrom,
                                                   pageSize,
                                                   linkedAssets,
                                                   lineageRelationships,
                                                   upstreamProcessedAssets,
                                                   downstreamProcessedAssets,
                                                   assetHandler,
                                                   converter);
                }
            }
        }
    }


    /**
     * Retrieve the asset and its lineage relationships.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param asOfTime repository time for the query
     * @param effectiveTime effectivity date to use
     * @param startFrom where to start from in retrieval
     * @param pageSize maximum number of elements to receive
     * @param upstreamAssets set of assets that are upstream of this asset
     * @param downstreamAssets set of assets that are downstream of this asset
     * @param assetHandler handler for retrieving entities/relationships
     * @param converter relationship converter
     * @return asset lineage node
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    AssetLineageGraphNode getAssetLineageGraphNode(String                                                userId,
                                                   String                                                assetGUID,
                                                   int                                                   direction,
                                                   List<String>                                          lineageRelationshipTypeNames,
                                                   String                                                limitToInformationSupplyChain,
                                                   Date                                                  asOfTime,
                                                   Date                                                  effectiveTime,
                                                   int                                                   startFrom,
                                                   int                                                   pageSize,
                                                   Map<String, LineageLink>                              upstreamAssets,
                                                   Map<String, LineageLink>                              downstreamAssets,
                                                   AssetHandler<AssetElement>                            assetHandler,
                                                   OpenMetadataAPIGenericConverter<MetadataRelationship> converter) throws InvalidParameterException,
                                                                                                                           PropertyServerException,
                                                                                                                           UserNotAuthorizedException
    {
        final String  assetGUIDParameterName = "assetGUID";
        final String  methodName = "getAssetLineageGraphNode";

        AssetElement asset = assetHandler.getBeanFromRepository(userId,
                                                                assetGUID,
                                                                assetGUIDParameterName,
                                                                OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                true,
                                                                false,
                                                                effectiveTime,
                                                                methodName);

        if (asset != null)
        {
            AssetLineageGraphNode assetLineageGraphNode = new AssetLineageGraphNode(asset);

            /*
             * Retrieve all the lineage relationships for the asset.
             */
            List<Relationship> relationships = this.getAssetLineageRelationships(userId,
                                                                                 asset.getElementHeader().getGUID(),
                                                                                 direction,
                                                                                 lineageRelationshipTypeNames,
                                                                                 limitToInformationSupplyChain,
                                                                                 asOfTime,
                                                                                 effectiveTime,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 assetHandler);

            List<Relationship> upstreamRelationships = new ArrayList<>();
            List<Relationship> downstreamRelationships = new ArrayList<>();
            List<Relationship> internalRelationship = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    String relationshipName = this.getRelationshipName(relationship, assetHandler.getRepositoryHelper());
                    if (lineageRelationshipTypeNames.contains(relationship.getType().getTypeDefName()))
                    {
                        String end1AnchorGUID = this.getAnchorGUID(relationship.getEntityOneProxy(), assetHandler);
                        String end2AnchorGUID = this.getAnchorGUID(relationship.getEntityTwoProxy(), assetHandler);

                        if (assetGUID.equals(end1AnchorGUID))
                        {
                            if (assetGUID.equals(end2AnchorGUID))
                            {
                                internalRelationship.add(relationship);
                            }
                            else
                            {
                                LineageLink  currentLineageLinks = downstreamAssets.get(relationship.getEntityTwoProxy().getGUID());

                                if (currentLineageLinks == null)
                                {
                                    currentLineageLinks = new LineageLink(null, null);
                                }

                                Set<String> currentRelationshipNames = currentLineageLinks.relationshipTypes;

                                if (currentRelationshipNames == null)
                                {
                                    currentRelationshipNames = new HashSet<>();
                                }

                                currentRelationshipNames.add(relationshipName);

                                Set<String> currentInformationSupplyChains = currentLineageLinks.informationSupplyChains;

                                String relationshipSupplyChain = assetHandler.getRepositoryHelper().getStringProperty(instanceHandler.getServiceName(),
                                                                                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                                                      relationship.getProperties(),
                                                                                                                      methodName);

                                if (relationshipSupplyChain != null)
                                {
                                    if (currentInformationSupplyChains == null)
                                    {
                                        currentInformationSupplyChains = new HashSet<>();
                                    }

                                    currentInformationSupplyChains.add(relationshipSupplyChain);
                                }

                                downstreamAssets.put(end2AnchorGUID, new LineageLink(currentRelationshipNames, currentInformationSupplyChains));
                                downstreamRelationships.add(relationship);
                            }
                        }
                        else
                        {
                            if (assetGUID.equals(end2AnchorGUID))
                            {
                                LineageLink  currentLineageLinks = upstreamAssets.get(relationship.getEntityTwoProxy().getGUID());

                                if (currentLineageLinks == null)
                                {
                                    currentLineageLinks = new LineageLink(null, null);
                                }

                                Set<String> currentRelationshipNames = currentLineageLinks.relationshipTypes;

                                if (currentRelationshipNames == null)
                                {
                                    currentRelationshipNames = new HashSet<>();
                                }

                                Set<String> currentInformationSupplyChains = currentLineageLinks.informationSupplyChains;

                                String relationshipSupplyChain = assetHandler.getRepositoryHelper().getStringProperty(instanceHandler.getServiceName(),
                                                                                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                                                      relationship.getProperties(),
                                                                                                                      methodName);

                                if (relationshipSupplyChain != null)
                                {
                                    if (currentInformationSupplyChains == null)
                                    {
                                        currentInformationSupplyChains = new HashSet<>();
                                    }

                                    currentInformationSupplyChains.add(relationshipSupplyChain);
                                }

                                currentRelationshipNames.add(relationshipName);

                                upstreamAssets.put(end1AnchorGUID, new LineageLink(currentRelationshipNames, currentInformationSupplyChains));
                                upstreamRelationships.add(relationship);
                            }
                        }
                    }
                }
            }

            assetLineageGraphNode.setUpstreamRelationships(convertRelationships(upstreamRelationships, converter));
            assetLineageGraphNode.setDownstreamRelationships(convertRelationships(downstreamRelationships, converter));
            assetLineageGraphNode.setInternalRelationships(convertRelationships(internalRelationship, converter));

            return assetLineageGraphNode;
        }

        return null;
    }


    /**
     * Extract the name of the relationship from a relationship.
     *
     * @param relationship relationship to query
     * @param repositoryHelper repository helper to extract properties
     * @return relationship name
     */
    private String getRelationshipName(Relationship         relationship,
                                       OMRSRepositoryHelper repositoryHelper)
    {
        final String methodName = "getRelationshipName";

        /*
         * The default name is the type name
         */
        String relationshipName = relationship.getType().getTypeDefName();

        /*
         * If the relationship has a label then this is used to embellish the relationship name.
         */
        String label = repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                          OpenMetadataProperty.LABEL.name,
                                                          relationship.getProperties(),
                                                          methodName);

        if (label != null)
        {
            relationshipName = label + " [" + relationshipName + "]";
        }

        return relationshipName;
    }


    /**
     * Retrieve the asset anchorGUID from an entity proxy.  It is possible that the anchor guid of the proxy is not an
     * asset. (for example, it may be the asset's schema type)  Therefore it is necessary to
     *
     * @param entityProxy end of a relationship
     * @param assetHandler handler
     * @return unique identifier of
     */
    private String getAnchorGUID(EntityProxy                entityProxy,
                                 AssetHandler<AssetElement> assetHandler)
    {
        final String methodName = "getAnchorGUID";

        if (entityProxy != null)
        {
            /*
             * If the end is an asset, use its GUID
             */
            if (assetHandler.getRepositoryHelper().isTypeOf(methodName,
                                                            entityProxy.getType().getTypeDefName(),
                                                            OpenMetadataType.ASSET.typeName))
            {
                return entityProxy.getGUID();
            }

            /*
             * For ends that are not assets, use the anchor.
             */
            List<Classification> classifications = entityProxy.getClassifications();

            if (classifications != null)
            {
                for (Classification classification : classifications)
                {
                    if ((classification != null) && (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getName())))
                    {
                        String anchorGUID = assetHandler.getRepositoryHelper().removeStringProperty(entityProxy.getGUID(),
                                                                                                    OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                                    classification.getProperties(),
                                                                                                    methodName);

                        if (anchorGUID == null)
                        {
                            anchorGUID = entityProxy.getGUID();
                        }

                        return anchorGUID;
                    }
                }
            }

            return entityProxy.getGUID();
        }

        return null;
    }


    /**
     * Convert a list of relationships into a list of metadata relationships.
     *
     * @param relationships list of relationships
     * @param converter converter
     * @return list of metadata elements.  Will be null if list of relationships is empty
     * @throws PropertyServerException problem in converter
     */
    private List<MetadataRelationship> convertRelationships(List<Relationship>                                    relationships,
                                                            OpenMetadataAPIGenericConverter<MetadataRelationship> converter) throws PropertyServerException
    {
        final String methodName = "convertRelationships";

        if (! relationships.isEmpty())
        {
            List<MetadataRelationship> results = new ArrayList<>();

            for (Relationship relationship: relationships)
            {
                if (relationship != null)
                {
                    MetadataRelationship metadataRelationship = converter.getNewRelationshipBean(MetadataRelationship.class,
                                                                                                 relationship,
                                                                                                 methodName);

                    results.add(metadataRelationship);
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve all the relevant lineage relationships for an asset.  This considers relationships from the
     * asst itself and any of its schema elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param startFrom where to start from in retrieval
     * @param pageSize maximum number of elements to receive
     * @param assetHandler handler for retrieving entities/relationships
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param asOfTime repository time for the query
     * @param effectiveTime effectivity date to use
     * @return list for relationships - may be empty
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    private List<Relationship> getAssetLineageRelationships(String                     userId,
                                                            String                     assetGUID,
                                                            int                        direction,
                                                            List<String>               lineageRelationshipTypeNames,
                                                            String                     limitToInformationSupplyChain,
                                                            Date                       asOfTime,
                                                            Date                       effectiveTime,
                                                            int                        startFrom,
                                                            int                        pageSize,
                                                            AssetHandler<AssetElement> assetHandler) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String  assetGUIDParameterName = "assetGUID";
        final String  methodName = "getAssetLineageRelationships";

        /*
         * Begin by retrieving all the relationships for the asset and filtering out the lineage relationships.
         */
        List<Relationship> lineageRelationships = new ArrayList<>(this.getLineageRelationshipsForElement(userId,
                                                                                                         assetGUID,
                                                                                                         assetGUIDParameterName,
                                                                                                         OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                                         direction,
                                                                                                         lineageRelationshipTypeNames,
                                                                                                         limitToInformationSupplyChain,
                                                                                                         asOfTime,
                                                                                                         effectiveTime,
                                                                                                         assetHandler));


        /*
         * Now find the SchemaElements that belong to the asset.
         */
        SearchClassifications         searchClassifications    = new SearchClassifications();
        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();
        SearchProperties              searchProperties         = new SearchProperties();
        List<PropertyCondition>       propertyConditions       = new ArrayList<>();
        PropertyCondition             propertyCondition        = new PropertyCondition();
        PrimitivePropertyValue        primitivePropertyValue   = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(assetGUID);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

        propertyCondition.setProperty(OpenMetadataProperty.ANCHOR_GUID.name);
        propertyCondition.setOperator(PropertyComparisonOperator.EQ);
        propertyCondition.setValue(primitivePropertyValue);
        propertyConditions.add(propertyCondition);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        searchProperties.setConditions(propertyConditions);

        classificationCondition.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
        classificationCondition.setMatchProperties(searchProperties);
        classificationConditions.add(classificationCondition);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);
        searchClassifications.setConditions(classificationConditions);

        List<EntityDetail> anchoredElements = assetHandler.findEntities(userId,
                                                                        OpenMetadataType.SCHEMA_ELEMENT.typeName,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        searchClassifications,
                                                                        asOfTime,
                                                                        null,
                                                                        null,
                                                                        true,
                                                                        false,
                                                                        startFrom,
                                                                        pageSize,
                                                                        assetHandler.getSupportedZones(),
                                                                        effectiveTime,
                                                                        methodName);

        /*
         * For each schema element in the asset, retrieve its lineage relationships
         */
        if (anchoredElements != null)
        {
            final String anchoredElementGUIDParameterName = "anchoredElement.getGUID()";

            for (EntityDetail anchoredElement : anchoredElements)
            {
                lineageRelationships.addAll(this.getLineageRelationshipsForElement(userId,
                                                                                   anchoredElement.getGUID(),
                                                                                   anchoredElementGUIDParameterName,
                                                                                   OpenMetadataType.SCHEMA_ELEMENT.typeName,
                                                                                   direction,
                                                                                   lineageRelationshipTypeNames,
                                                                                   limitToInformationSupplyChain,
                                                                                   asOfTime,
                                                                                   effectiveTime,
                                                                                   assetHandler));
            }
        }

        return lineageRelationships;
    }


    /**
     * Return the lineage relationships for a particular element.
     *
     * @param userId calling user
     * @param elementGUID starting element
     * @param elementGUIDParameterName parameter name of element's unique identifier
     * @param elementTypeName type name of element
     * @param direction is this asset upstream or downstream of the asset (or either direction, 0, to start)
     * @param lineageRelationshipTypeNames list of requested type names
     * @param limitToInformationSupplyChain qualified name to control retrieval
     * @param asOfTime repository time for the query
     * @param effectiveTime effectivity date to use
     * @param assetHandler handler for retrieving relationships
     * @return list for relationships - may be empty
     * @throws InvalidParameterException invalid parameter - not expected
     * @throws PropertyServerException problem accessing the repository
     * @throws UserNotAuthorizedException security problem
     */
    List<Relationship> getLineageRelationshipsForElement(String                     userId,
                                                         String                     elementGUID,
                                                         String                     elementGUIDParameterName,
                                                         String                     elementTypeName,
                                                         int                        direction,
                                                         List<String>               lineageRelationshipTypeNames,
                                                         String                     limitToInformationSupplyChain,
                                                         Date                       asOfTime,
                                                         Date                       effectiveTime,
                                                         AssetHandler<AssetElement> assetHandler) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getLineageRelationshipsForElement";

        List<Relationship> lineageRelationships = new ArrayList<>();

        /*
         * Begin by retrieving all the relationships for the asset and filtering out the lineage relationships.
         */
        List<Relationship> relationships = assetHandler.getAllAttachmentLinks(userId,
                                                                              elementGUID,
                                                                              elementGUIDParameterName,
                                                                              elementTypeName,
                                                                              null,
                                                                              asOfTime,
                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                              null,
                                                                              true,
                                                                              false,
                                                                              effectiveTime,
                                                                              methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if ((relationship != null) &&
                        (lineageRelationshipTypeNames.contains(relationship.getType().getTypeDefName())) &&
                        ((direction == 0)
                                || (direction == 1 && (elementGUID.equals(relationship.getEntityOneProxy().getGUID())))
                                || (direction == 2 && (elementGUID.equals(relationship.getEntityTwoProxy().getGUID())))))
                {
                    if (limitToInformationSupplyChain != null)
                    {
                        String relationshipSupplyChain = assetHandler.getRepositoryHelper().getStringProperty(instanceHandler.getServiceName(),
                                                                                                              OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                                                              relationship.getProperties(),
                                                                                                              methodName);

                        if (limitToInformationSupplyChain.equals(relationshipSupplyChain))
                        {
                            lineageRelationships.add(relationship);
                        }
                    }
                    else
                    {
                        lineageRelationships.add(relationship);
                    }
                }
            }
        }

        return lineageRelationships;
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetSearchMatchesListResponse findAssetsInDomain(String                  serverName,
                                                             String                  userId,
                                                             SearchStringRequestBody requestBody,
                                                             int                     startFrom,
                                                             int                     pageSize)
    {
        final String methodName = "findAssetsInDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetSearchMatchesListResponse response = new AssetSearchMatchesListResponse();
        AuditLog                       auditLog = null;

        try
        {
            AssetHandler<AssetElement>                   assetHandler           = instanceHandler.getAssetHandler(userId, serverName, methodName);
            ReferenceableHandler<MetadataElementSummary> metadataElementHandler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SearchProperties        searchProperties             = new SearchProperties();
                List<PropertyCondition> propertyConditions           = new ArrayList<>();
                PropertyCondition       qualNamePropertyCondition    = new PropertyCondition();
                PropertyCondition       namePropertyCondition        = new PropertyCondition();
                PropertyCondition       displayNamePropertyCondition = new PropertyCondition();
                PropertyCondition       descPropertyCondition        = new PropertyCondition();
                PropertyCondition       depImplTypePropertyCondition = new PropertyCondition();
                PrimitivePropertyValue  qualNamePropertyValue        = new PrimitivePropertyValue();
                PrimitivePropertyValue  namePropertyValue            = new PrimitivePropertyValue();
                PrimitivePropertyValue  displayNamePropertyValue     = new PrimitivePropertyValue();
                PrimitivePropertyValue  descPropertyValue            = new PrimitivePropertyValue();
                PrimitivePropertyValue  depImplTypePropertyValue     = new PrimitivePropertyValue();

                qualNamePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                qualNamePropertyValue.setPrimitiveValue(requestBody.getSearchString());
                qualNamePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                qualNamePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                namePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                namePropertyValue.setPrimitiveValue(requestBody.getSearchString());
                namePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                namePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                displayNamePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                displayNamePropertyValue.setPrimitiveValue(requestBody.getSearchString());
                displayNamePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                displayNamePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                descPropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                descPropertyValue.setPrimitiveValue(requestBody.getSearchString());
                descPropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                descPropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                depImplTypePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                depImplTypePropertyValue.setPrimitiveValue(requestBody.getSearchString());
                depImplTypePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                depImplTypePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

                qualNamePropertyCondition.setProperty(OpenMetadataProperty.QUALIFIED_NAME.name);
                qualNamePropertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                qualNamePropertyCondition.setValue(qualNamePropertyValue);
                propertyConditions.add(qualNamePropertyCondition);

                namePropertyCondition.setProperty(OpenMetadataProperty.NAME.name);
                namePropertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                namePropertyCondition.setValue(namePropertyValue);
                propertyConditions.add(namePropertyCondition);

                displayNamePropertyCondition.setProperty(OpenMetadataProperty.DISPLAY_NAME.name);
                displayNamePropertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                displayNamePropertyCondition.setValue(displayNamePropertyValue);
                propertyConditions.add(displayNamePropertyCondition);

                displayNamePropertyCondition.setProperty(OpenMetadataProperty.RESOURCE_NAME.name);
                displayNamePropertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                displayNamePropertyCondition.setValue(displayNamePropertyValue);
                propertyConditions.add(displayNamePropertyCondition);

                descPropertyCondition.setProperty(OpenMetadataProperty.DESCRIPTION.name);
                descPropertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                descPropertyCondition.setValue(descPropertyValue);
                propertyConditions.add(descPropertyCondition);

                depImplTypePropertyCondition.setProperty(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
                depImplTypePropertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                depImplTypePropertyCondition.setValue(depImplTypePropertyValue);
                propertyConditions.add(depImplTypePropertyCondition);

                searchProperties.setMatchCriteria(MatchCriteria.ANY);
                searchProperties.setConditions(propertyConditions);

                SearchClassifications         searchClassifications            = new SearchClassifications();
                List<ClassificationCondition> classificationConditions         = new ArrayList<>();
                ClassificationCondition       classificationCondition          = new ClassificationCondition();
                SearchProperties              classificationSearchProperties   = new SearchProperties();
                List<PropertyCondition>       classificationPropertyConditions = new ArrayList<>();
                PropertyCondition             classificationPropertyCondition  = new PropertyCondition();
                PrimitivePropertyValue        classificationPropertyValue      = new PrimitivePropertyValue();

                classificationPropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                classificationPropertyValue.setPrimitiveValue(OpenMetadataType.ASSET.typeName);
                classificationPropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                classificationPropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

                classificationPropertyCondition.setProperty(OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name);
                classificationPropertyCondition.setOperator(PropertyComparisonOperator.EQ);
                classificationPropertyCondition.setValue(classificationPropertyValue);
                classificationPropertyConditions.add(classificationPropertyCondition);
                classificationSearchProperties.setMatchCriteria(MatchCriteria.ALL);
                classificationSearchProperties.setConditions(classificationPropertyConditions);

                classificationCondition.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
                classificationCondition.setMatchProperties(classificationSearchProperties);
                classificationConditions.add(classificationCondition);
                searchClassifications.setMatchCriteria(MatchCriteria.ALL);
                searchClassifications.setConditions(classificationConditions);

                List<EntityDetail> anchoredEntities = metadataElementHandler.findEntities(userId,
                                                                                          null,
                                                                                          null,
                                                                                          searchProperties,
                                                                                          null,
                                                                                          searchClassifications,
                                                                                          requestBody.getAsOfTime(),
                                                                                          null,
                                                                                          null,
                                                                                          false,
                                                                                          false,
                                                                                          startFrom,
                                                                                          pageSize,
                                                                                          assetHandler.getSupportedZones(),
                                                                                          requestBody.getEffectiveTime(),
                                                                                          methodName);

                if (anchoredEntities != null)
                {
                    Map<String, Map<String, EntityDetail>> organizedEntities = new HashMap<>();

                    for (EntityDetail entityDetail : anchoredEntities)
                    {
                        if (entityDetail != null)
                        {
                            OpenMetadataAPIGenericHandler.AnchorIdentifiers anchorIdentifiers = metadataElementHandler.getAnchorsFromAnchorsClassification(entityDetail, methodName);

                            String anchorGUID;
                            if ((anchorIdentifiers == null) || (anchorIdentifiers.anchorGUID == null))
                            {
                                anchorGUID = entityDetail.getGUID();
                            }
                            else
                            {
                                anchorGUID = anchorIdentifiers.anchorGUID;
                            }

                            Map<String, EntityDetail>  assetEntityMap = organizedEntities.get(anchorGUID);

                            if (assetEntityMap == null)
                            {
                                assetEntityMap = new HashMap<>();
                            }

                            assetEntityMap.put(entityDetail.getGUID(), entityDetail);
                            organizedEntities.put(anchorGUID, assetEntityMap);
                        }
                    }

                    List<AssetSearchMatches> assetSearchMatchesList = new ArrayList<>();

                    final String                                            parameterName = "assetGUID";
                    OpenMetadataAPIGenericConverter<MetadataElementSummary> converter     = metadataElementHandler.getConverter();

                    for (String assetGUID : organizedEntities.keySet())
                    {
                        AssetElement asset;

                        try
                        {
                            asset = assetHandler.getBeanFromRepository(userId,
                                                                       assetGUID,
                                                                       parameterName,
                                                                       OpenMetadataType.ASSET.typeName,
                                                                       methodName);
                        }
                        catch (InvalidParameterException | UserNotAuthorizedException notVisible)
                        {
                            asset = null;
                        }

                        if (asset != null)
                        {
                            AssetSearchMatches           assetSearchMatches = new AssetSearchMatches(asset);
                            Map<String, EntityDetail>    assetEntityMap   = organizedEntities.get(assetGUID);
                            List<MetadataElementSummary> anchoredElements = new ArrayList<>();

                            for (EntityDetail anchoredEntity : assetEntityMap.values())
                            {
                                MetadataElementSummary metadataElement = converter.getNewBean(MetadataElementSummary.class,
                                                                                              anchoredEntity,
                                                                                              methodName);

                                anchoredElements.add(metadataElement);
                            }

                            assetSearchMatches.setMatchingElements(anchoredElements);
                            assetSearchMatchesList.add(assetSearchMatches);
                        }
                    }

                    response.setSearchMatches(assetSearchMatchesList);
                }

            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public GUIDListResponse findAssets(String                  serverName,
                                       String                  userId,
                                       SearchStringRequestBody requestBody,
                                       int                     startFrom,
                                       int                     pageSize)
    {
        final String searchStringParameter = "searchString";
        final String methodName            = "findAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUIDs(handler.findAssetGUIDs(userId,
                                                         requestBody.getSearchString(),
                                                         searchStringParameter,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of assets with the requested name.  This is an exact match search.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public GUIDListResponse getAssetsByName(String          serverName,
                                            String          userId,
                                            NameRequestBody requestBody,
                                            int             startFrom,
                                            int             pageSize)
    {
        final String nameParameterName = "name";
        final String methodName        = "getAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUIDs(handler.getAssetGUIDsByName(userId,
                                                              OpenMetadataType.ASSET.typeGUID,
                                                              OpenMetadataType.ASSET.typeName,
                                                              requestBody.getName(),
                                                              nameParameterName,
                                                              startFrom,
                                                              pageSize,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of assets that come from the requested metadata collection.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param metadataCollectionId guid to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param requestBody optional type name to restrict search by
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetsResponse getAssetsByMetadataCollectionId(String          serverName,
                                                          String          userId,
                                                          String          metadataCollectionId,
                                                          int             startFrom,
                                                          int             pageSize,
                                                          NameRequestBody requestBody)
    {
        final String nameParameterName = "metadataCollectionId";
        final String methodName        = "getAssetsByMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetsResponse response = new AssetsResponse();
        AuditLog       auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setAssets(handler.getAssetsByMetadataCollectionId(userId,
                                                                           requestBody.getName(),
                                                                           metadataCollectionId,
                                                                           nameParameterName,
                                                                           startFrom,
                                                                           pageSize,
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName));
            }
            else
            {
                response.setAssets(handler.getAssetsByMetadataCollectionId(userId,
                                                                           null,
                                                                           metadataCollectionId,
                                                                           nameParameterName,
                                                                           startFrom,
                                                                           pageSize,
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName));            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerFeedbackInterface
     * ===========================================
     */


    /**
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of referenceable (probably asset).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addRatingToAsset(String            serverName,
                                         String            userId,
                                         String            guid,
                                         RatingRequestBody requestBody)
    {
        final String methodName = "addRatingToAsset";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                RatingHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getRatingHandler(userId, serverName, methodName);

                int starRating = StarRating.NOT_RECOMMENDED.getOrdinal();

                if (requestBody.getStarRating() != null)
                {
                    starRating = requestBody.getStarRating().getOrdinal();
                }
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.saveRating(userId,
                                   null,
                                   null,
                                   guid,
                                   guidParameterName,
                                   starRating,
                                   requestBody.getReview(),
                                   requestBody.getIsPublic(),
                                   false,
                                   false,
                                   new Date(),
                                   methodName);

            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a star rating that was added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the rating object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeRatingFromAsset(String          serverName,
                                              String          userId,
                                              String          guid,
                                              NullRequestBody requestBody)
    {
        final String methodName = "removeRatingFromAsset";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            RatingHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getRatingHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeRating(userId,
                                 null,
                                 null,
                                 guid,
                                 guidParameterName,
                                 false,
                                 false,
                                 new Date(),
                                 methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a "LikeProperties" to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody feedback request body .
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLikeToAsset(String              serverName,
                                       String              userId,
                                       String              guid,
                                       FeedbackRequestBody requestBody)
    {
        final String methodName        = "addLikeToAsset";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                LikeHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getLikeHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.saveLike(userId,
                                 null,
                                 null,
                                 guid,
                                 guidParameterName,
                                 requestBody.getIsPublic(),
                                 false,
                                 false,
                                 new Date(),
                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a "LikeProperties" added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the like object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeLikeFromAsset(String          serverName,
                                            String          userId,
                                            String          guid,
                                            NullRequestBody requestBody)
    {
        final String methodName        = "removeLikeFromAsset";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            LikeHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getLikeHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeLike(userId,
                               null,
                               null,
                               guid,
                               guidParameterName,
                               false,
                               false,
                               new Date(),
                               methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a comment to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToAsset(String            serverName,
                                          String            userId,
                                          String            guid,
                                          CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentToAsset";
        final String        guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                int commentType = CommentType.STANDARD_COMMENT.getOrdinal();

                if (requestBody.getCommentType() != null)
                {
                    commentType = requestBody.getCommentType().getOrdinal();
                }
                CommentHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.attachNewComment(userId,
                                                           null,
                                                           null,
                                                           guid,
                                                           guid,
                                                           guidParameterName,
                                                           commentType,
                                                           requestBody.getCommentText(),
                                                           requestBody.getIsPublic(),
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param assetGUID     String - unique id of asset that this chain of comments is linked.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String             serverName,
                                        String             userId,
                                        String             assetGUID,
                                        String             commentGUID,
                                        CommentRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "addCommentReply";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                int commentType = CommentType.STANDARD_COMMENT.getOrdinal();

                if (requestBody.getCommentType() != null)
                {
                    commentType = requestBody.getCommentType().getOrdinal();
                }

                CommentHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.attachNewComment(userId,
                                                          null,
                                                          null,
                                                          assetGUID,
                                                          commentGUID,
                                                          guidParameterName,
                                                          commentType,
                                                          requestBody.getCommentText(),
                                                          requestBody.getIsPublic(),
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param assetGUID    unique identifier for the asset that the comment is attached to (directly or indirectly).
     * @param commentGUID  unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   updateComment(String             serverName,
                                        String             userId,
                                        String             assetGUID,
                                        String             commentGUID,
                                        CommentRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "updateComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                int commentType = CommentType.STANDARD_COMMENT.getOrdinal();

                if (requestBody.getCommentType() != null)
                {
                    commentType = requestBody.getCommentType().getOrdinal();
                }

                CommentHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateComment(userId,
                                      null,
                                      null,
                                      commentGUID,
                                      guidParameterName,
                                      null,
                                      commentType,
                                      requestBody.getCommentText(),
                                      requestBody.getIsPublic(),
                                      true,
                                      null,
                                      null,
                                      false,
                                      false,
                                      new Date(),
                                      methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param assetGUID  String - unique id for the asset object
     * @param commentGUID  String - unique id for the comment object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCommentFromAsset(String          serverName,
                                               String          userId,
                                               String          assetGUID,
                                               String          commentGUID,
                                               NullRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "removeAssetComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CommentHandler<OpenMetadataAPIDummyBean> handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeCommentFromElement(userId,
                                             null,
                                             null,
                                             commentGUID,
                                             guidParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerGlossaryInterface
     * ===========================================
     */


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the meaning.
     *
     * @return glossary term object or
     * InvalidParameterException the userId is null or invalid or
     * NoProfileForUserException the user does not have a profile or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public MeaningResponse getMeaning(String   serverName,
                                      String   userId,
                                      String   guid)
    {
        final String guidParameterName = "guid";
        final String methodName        = "getMeaning";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        MeaningResponse response = new MeaningResponse();
        AuditLog        auditLog = null;

        try
        {
            GlossaryTermHandler<MeaningElement> glossaryTermHandler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setMeaning(glossaryTermHandler.getTerm(userId,
                                                            guid,
                                                            guidParameterName,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the full definition (meaning) of the terms exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public MeaningsResponse getMeaningByName(String          serverName,
                                             String          userId,
                                             NameRequestBody requestBody,
                                             int             startFrom,
                                             int             pageSize)
    {
        final String methodName = "getMeaningByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        MeaningsResponse response = new MeaningsResponse();
        AuditLog         auditLog = null;

        try
        {
            GlossaryTermHandler<MeaningElement> glossaryTermHandler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                List<InstanceStatus> limitStatuses = new ArrayList<>();
                limitStatuses.add(InstanceStatus.ACTIVE);
                response.setElements(glossaryTermHandler.getTermsByName(userId,
                                                                        null,
                                                                        requestBody.getName(),
                                                                        limitStatuses,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName));}
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of term.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public MeaningsResponse findMeanings(String                  serverName,
                                         String                  userId,
                                         SearchStringRequestBody requestBody,
                                         int                     startFrom,
                                         int                     pageSize)
    {
        final String methodName = "findMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        MeaningsResponse response = new MeaningsResponse();
        AuditLog         auditLog = null;

        try
        {
            GlossaryTermHandler<MeaningElement> glossaryTermHandler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                List<InstanceStatus> limitStatuses = new ArrayList<>();
                limitStatuses.add(InstanceStatus.ACTIVE);
                response.setElements(glossaryTermHandler.findTerms(userId,
                                                                   null,
                                                                   requestBody.getSearchString(),
                                                                   limitStatuses,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific (meaning) either directly or via
     * fields in the schema.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse getAssetsByMeaning(String serverName,
                                               String userId,
                                               String termGUID,
                                               int    startFrom,
                                               int    pageSize)
    {
        final String guidParameterName = "termGUID";
        final String methodName        = "getAssetsByMeaning";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse  response = new GUIDListResponse();
        AuditLog          auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<EntityDetail> attachedEntities = handler.getAttachedEntities(userId,
                                                                              termGUID,
                                                                              guidParameterName,
                                                                              OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                              OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                                                              OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                              OpenMetadataType.REFERENCEABLE.typeName,
                                                                              null,
                                                                              null,
                                                                              1,
                                                                              null,
                                                                              null,
                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                              null,
                                                                              false,
                                                                              false,
                                                                              startFrom,
                                                                              pageSize,
                                                                              new Date(),
                                                                              methodName);

            if (attachedEntities != null)
            {
                final String entityGUIDParameterName = "attachedEntity.getGUID";
                OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);
                List<String> guids = new ArrayList<>();

                for (EntityDetail entity : attachedEntities)
                {
                    if (repositoryHelper.isTypeOf(serverName, entity.getType().getTypeDefName(), OpenMetadataType.ASSET.typeName))
                    {
                        if (! guids.contains(entity.getGUID()))
                        {
                            guids.add(entity.getGUID());
                        }
                    }
                    else
                    {
                        EntityDetail anchorEntity = handler.validateEntityAndAnchorForRead(userId,
                                                                                           OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                           entity,
                                                                                           entityGUIDParameterName,
                                                                                           false,
                                                                                           false,
                                                                                           false,
                                                                                           false,
                                                                                           handler.getSupportedZones(),
                                                                                           new Date(),
                                                                                           methodName);
                        if ((anchorEntity != null) && (repositoryHelper.isTypeOf(serverName, anchorEntity.getType().getTypeDefName(), OpenMetadataType.ASSET.typeName)))
                        {
                            if (! guids.contains(anchorEntity.getGUID()))
                            {
                                guids.add(anchorEntity.getGUID());
                            }
                        }
                    }
                }

                response.setGUIDs(guids);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerLoggingInterface
     * ===========================================
     */


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing:
     * connectorInstanceId  (String - (optional) id of connector in use (if any)),
     * connectionName  (String - (optional) name of the connection (extracted from the connector)),
     * connectorType  (String - (optional) type of connector in use (if any)),
     * contextId  (String - (optional) function name, or processId of the activity that the caller is performing),
     * message  (log record content).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the log message to the audit log for this asset or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLogMessageToAsset(String               serverName,
                                             String               userId,
                                             String               guid,
                                             LogRecordRequestBody requestBody)
    {
        final String        methodName = "addLogMessageToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;


        try
        {
            if (requestBody != null)
            {
                LoggingHandler loggingHandler = instanceHandler.getLoggingHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                loggingHandler.addLogMessageToAsset(userId,
                                                    guid,
                                                    requestBody.getConnectorInstanceId(),
                                                    requestBody.getConnectionName(),
                                                    requestBody.getConnectorType(),
                                                    requestBody.getContextId(),
                                                    requestBody.getMessage());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerTaggingInterface
     * ===========================================
     */


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createTag(String                serverName,
                                  String                userId,
                                  InformalTagProperties requestBody)
    {
        final String   methodName = "createTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createTag(userId,
                                                   null,
                                                   null,
                                                   requestBody.getName(),
                                                   requestBody.getDescription(),
                                                   (!requestBody.getIsPrivateTag()),
                                                   null,
                                                   null,
                                                   new Date(),
                                                   methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param tagGUID      unique id for the tag.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateTagDescription(String                serverName,
                                               String                userId,
                                               String                tagGUID,
                                               TagUpdateRequestBody  requestBody)
    {
        final String methodName           = "updateTagDescription";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateTagDescription(userId,
                                             null,
                                             null,
                                             tagGUID,
                                             tagGUIDParameterName,
                                             requestBody.getDescription(),
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   deleteTag(String          serverName,
                                    String          userId,
                                    String          tagGUID,
                                    NullRequestBody requestBody)
    {
        final String methodName           = "deleteTag";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteTag(userId,
                              null,
                              null,
                              tagGUID,
                              tagGUIDParameterName,
                              false,
                              false,
                              new Date(),
                              methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param serverName   name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagResponse getTag(String serverName,
                                      String userId,
                                      String guid)
    {
        final String methodName = "getTag";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagResponse response = new InformalTagResponse();
        AuditLog            auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement>  handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getTag(userId,
                                               guid,
                                               guidParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getTagsByName(String          serverName,
                                              String          userId,
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getTagsByName";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getTagsByName(userId,
                                                           requestBody.getName(),
                                                           nameParameterName,
                                                           startFrom,
                                                           pageSize,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getMyTagsByName(String          serverName,
                                                String          userId,
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize)
    {
        final String methodName = "getMyTagsByName";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getMyTagsByName(userId,
                                                             requestBody.getName(),
                                                             nameParameterName,
                                                             startFrom,
                                                             pageSize,
                                                             false,
                                                             false,
                                                             new Date(),
                                                             methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findTags(String                  serverName,
                                         String                  userId,
                                         SearchStringRequestBody requestBody,
                                         int                     startFrom,
                                         int                     pageSize)
    {
        final String methodName = "findTags";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findTags(userId,
                                                      requestBody.getSearchString(),
                                                      nameParameterName,
                                                      startFrom,
                                                      pageSize,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findMyTags(String                  serverName,
                                           String                  userId,
                                           SearchStringRequestBody requestBody,
                                           int                     startFrom,
                                           int                     pageSize)
    {
        final String methodName = "findMyTags";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findMyTags(userId,
                                                        requestBody.getSearchString(),
                                                        nameParameterName,
                                                        startFrom,
                                                        pageSize,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param assetGUID    unique id for the asset.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToAsset(String              serverName,
                                        String              userId,
                                        String              assetGUID,
                                        String              tagGUID,
                                        FeedbackRequestBody requestBody)
    {
        final String methodName             = "addTagToAsset";
        final String assetGUIDParameterName = "assetGUID";
        final String tagGUIDParameterName   = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        boolean  isPublic = false;

        if (requestBody != null)
        {
            isPublic = requestBody.getIsPublic();
        }

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addTagToElement(userId,
                                    null,
                                    null,
                                    assetGUID,
                                    assetGUIDParameterName,
                                    OpenMetadataType.ASSET.typeName,
                                    tagGUID,
                                    tagGUIDParameterName,
                                    instanceHandler.getSupportedZones(userId, serverName, methodName),
                                    isPublic,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a tag (either private of public) to an element attached to an asset - such as schema element, glossary term, ...
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToElement(String              serverName,
                                          String              userId,
                                          String              elementGUID,
                                          String              tagGUID,
                                          FeedbackRequestBody requestBody)
    {
        final String   methodName  = "addTagToElement";
        final String   elementGUIDParameterName  = "elementGUID";
        final String   tagGUIDParameterName  = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        boolean  isPublic = false;

        if (requestBody != null)
        {
            isPublic = requestBody.getIsPublic();
        }

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.addTagToElement(userId,
                                    null,
                                    null,
                                    elementGUID,
                                    elementGUIDParameterName,
                                    OpenMetadataType.REFERENCEABLE.typeName,
                                    tagGUID,
                                    tagGUIDParameterName,
                                    isPublic,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   removeTagFromAsset(String          serverName,
                                             String          userId,
                                             String          assetGUID,
                                             String          tagGUID,
                                             NullRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromAsset";
        final String   assetGUIDParameterName  = "assetGUID";
        final String   tagGUIDParameterName  = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeTagFromElement(userId,
                                         null,
                                         null,
                                         assetGUID,
                                         assetGUIDParameterName,
                                         OpenMetadataType.ASSET.typeName,
                                         tagGUID,
                                         tagGUIDParameterName,
                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from an element attached to an asset - such as schema element, glossary term, ... that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   removeTagFromElement(String          serverName,
                                               String          userId,
                                               String          elementGUID,
                                               String          tagGUID,
                                               NullRequestBody requestBody)
    {
        final String methodName               = "removeTagFromElement";
        final String elementGUIDParameterName = "elementGUID";
        final String tagGUIDParameterName     = "tagGUID";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeTagFromElement(userId,
                                         null,
                                         null,
                                         elementGUID,
                                         elementGUIDParameterName,
                                         OpenMetadataType.REFERENCEABLE.typeName,
                                         tagGUID,
                                         tagGUIDParameterName,
                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param serverName   name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse getAssetsByTag(String serverName,
                                           String userId,
                                           String tagGUID,
                                           int    startFrom,
                                           int    pageSize)
    {
        final String methodName           = "getAssetsByTag";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse  response = new GUIDListResponse();
        AuditLog          auditLog = null;

        try
        {
            AssetHandler<AssetElement>   handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getAssetGUIDsByTag(userId,
                                                         tagGUID,
                                                         tagGUIDParameterName,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}