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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

/**
 * The Classification handler
 */
public class ClassificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ContextHandler.class);

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private CommonHandler commonHandler;
    private AssetContext graph;


    /**
     * Instantiates a new Classification handler.
     *
     * @param serviceName             the service name
     * @param serverName              the server name
     * @param invalidParameterHandler the invalid parameter handler
     * @param repositoryHelper        the repository helper
     * @param repositoryHandler       the repository handler
     */
    public ClassificationHandler(String serviceName,
                                 String serverName,
                                 InvalidParameterHandler invalidParameterHandler,
                                 OMRSRepositoryHelper repositoryHelper,
                                 RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.invalidParameterHandler = invalidParameterHandler;
        this.commonHandler = new CommonHandler(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler);
    }


    /**
     * Gets asset context from the entity by classification type.
     *
     * @param serverName         the server name
     * @param userId             the user id
     * @param entityGuid         the entity guid for retrieving the classifications attached to it
     * @param classificationType the classification type for building the asset context
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> getAssetContextByClassification(String serverName, String userId, String entityGuid, String classificationType) {

        graph = new AssetContext();

        String methodName = "getAssetContextByClassification";

        try {

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(entityGuid, GUID_PARAMETER, methodName);

            Optional<EntityDetail> entityDetail = commonHandler.getEntityDetails(userId, entityGuid, classificationType);
            if (!entityDetail.isPresent()) {

                log.error("Something is wrong in the OMRS Connector when a specific operation is performed in the metadata collection." +
                        "Classified Entity not found with guid {}", entityGuid);

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
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                    e.getReportingClassName(),
                    e.getReportingActionDescription(),
                    e.getErrorMessage(),
                    e.getReportedSystemAction(),
                    e.getReportedUserAction());
        }
    }

    /**
     * Build graph edge by classification type list.
     *
     * @param userId             the user id
     * @param startEntity        the start entity
     * @param classificationType the classification type
     * @param graph              the graph
     * @return the list
     */
    private List<LineageEntity> buildGraphEdgeByClassificationType(String userId,
                                                                  EntityDetail startEntity,
                                                                  String classificationType,
                                                                  AssetContext graph) {

        List<LineageEntity> classificationEntities = new ArrayList<>();

        if (startEntity.getStatus() == InstanceStatus.ACTIVE) {

            for (Classification classification : startEntity.getClassifications()) {
                if (classification.getType().getTypeDefName().equals(classificationType)) {

                    LineageEntity lineageEntity = new LineageEntity();

                    //TODO: Set the classification guid to a new one?
                    lineageEntity.setGuid(UUID.randomUUID().toString());

                    mapClassificationsToLineageEntity(classification, lineageEntity);
                    classificationEntities.add(lineageEntity);
                    log.debug("Adding Classification {} ", classification.toString());
                }
            }

            if (classificationEntities.isEmpty()) return null;

            Converter converter = new Converter();
            LineageEntity startVertex = converter.createEntity(startEntity);

            for (LineageEntity endVertex : classificationEntities) {

                graph.addVertex(startVertex);
                graph.addVertex(endVertex);

                GraphContext edge = new GraphContext(classificationType, CLASSIFIED_ENTITY_GUID, startVertex, endVertex);
                //TODO: set the edge GUID to start entity now
                edge.setRelationshipGuid(startEntity.getGUID());
                graph.addEdge(edge);

            }
            return classificationEntities;
        } return null;
    }

    private void mapClassificationsToLineageEntity(Classification classification, LineageEntity lineageEntity) {

        final String methodName = "mapClassificationsToLineageEntity";

        Converter converter = new Converter();

        try {

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
}
