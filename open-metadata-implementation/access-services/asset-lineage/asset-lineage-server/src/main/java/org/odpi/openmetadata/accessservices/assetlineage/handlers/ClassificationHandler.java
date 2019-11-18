package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.CLASSIFIED_ENTITY_GUID;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.TYPE_EMBEDDED_ATTRIBUTE;

public class ClassificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ContextHandler.class);
    private static final String GUID_PARAMETER = "guid";

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private CommonHandler commonHandler;
    private AssetContext graph;


    public ClassificationHandler( String serviceName,
                                  String serverName,
                                  InvalidParameterHandler invalidParameterHandler,
                                  OMRSRepositoryHelper repositoryHelper,
                                  RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.invalidParameterHandler = invalidParameterHandler;
        this.commonHandler = new CommonHandler(serviceName,serverName,invalidParameterHandler,repositoryHelper,repositoryHandler);
    }


    public Map<String, Set<GraphContext>> getAssetContextByClassification(String serverName, String userId, String guid, String type) {

        graph = new AssetContext();

        try {
            Optional<EntityDetail> entityDetail = getEntityDetails(userId, guid, type);
            if (!entityDetail.isPresent()) {

                log.error("Something is wrong in the OMRS Connector when a specific operation is performed in the metadata collection." +
                        "Classified Entity not found with guid {}", guid);

                throw new AssetLineageException(
                        ENTITY_NOT_FOUND.getHTTPErrorCode(),
                        this.getClass().getName(),
                        "Retrieving Classified Entity",
                        ENTITY_NOT_FOUND.getErrorMessage(),
                        ENTITY_NOT_FOUND.getSystemAction(),
                        ENTITY_NOT_FOUND.getUserAction());
            }

            buildGraphEdgeByClassificationType(userId, entityDetail.get(), TYPE_EMBEDDED_ATTRIBUTE, graph);

            return graph.getNeighbors();
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException  e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                    e.getReportingClassName(),
                    e.getReportingActionDescription(),
                    e.getErrorMessage(),
                    e.getReportedSystemAction(),
                    e.getReportedUserAction());
        }
    }

    public List<LineageEntity> buildGraphEdgeByClassificationType(String userId,
                                                                  EntityDetail startEntity,
                                                                  String classificationType,
                                                                  AssetContext graph) {

        List<LineageEntity> classificationLineageEntities = new ArrayList<>();

        for(Classification classification : startEntity.getClassifications())
        {
            if(classification.getType().getTypeDefName().equals(classificationType)){

                LineageEntity lineageEntity = new LineageEntity();
                mapClassificationsToLineageEntity(classification, lineageEntity);
                classificationLineageEntities.add(lineageEntity);
                log.debug("Adding Classification {} ", classification.toString());
            }
        }

        if (classificationLineageEntities.isEmpty()) return null;

        Converter converter = new Converter();
        LineageEntity startVertex = converter.createEntity(startEntity);


        for (LineageEntity endVertex : classificationLineageEntities) {

            graph.addVertex(startVertex);
            graph.addVertex(endVertex);

            GraphContext edge = new GraphContext(classificationType, CLASSIFIED_ENTITY_GUID, startVertex, endVertex);
            graph.addEdge(edge);

        }

        return classificationLineageEntities;

    }

    private void mapClassificationsToLineageEntity(Classification classification, LineageEntity lineageEntity) {

        final String methodName = "mapClassificationsToLineageEntity";

        Converter converter = new Converter();

        try {

            lineageEntity.setGuid(classification.getClassificationOriginGUID());
            lineageEntity.setVersion(classification.getVersion());
            lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
            lineageEntity.setCreatedBy(classification.getCreatedBy());
            lineageEntity.setUpdatedBy(classification.getUpdatedBy());
            lineageEntity.setCreateTime(classification.getCreateTime());
            lineageEntity.setUpdateTime(classification.getUpdateTime());
            lineageEntity.setProperties(converter.getMapProperties(classification.getProperties()));

            log.debug("Classfication mapping for lineage entity {}: ", lineageEntity);

        } catch (Throwable exc) {

            AssetLineageErrorCode errorCode = AssetLineageErrorCode.CLASSIFICATION_MAPPING_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(classification.getName(), methodName,
                    this.getClass().getName());

            log.error("Caught exception from classification mapper {}", errorMessage);
        }

    }

    private Optional<EntityDetail> getEntityDetails(String userId, String guid,String type) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        String methodName = "getEntityDetails";
        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER,type, methodName));
    }


}
