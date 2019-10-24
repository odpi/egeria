/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PROCESS;

public class CommonHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonHandler.class);

    private static final String GUID_PARAMETER = "guid";
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public CommonHandler(String serviceName,
                         String serverName,
                         InvalidParameterHandler invalidParameterHandler,
                         OMRSRepositoryHelper repositoryHelper,
                         RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Query about the entity in the repositories based on the Guid
     *
     * @param userId    String - userId of user making request.
     * @param guid guid of the asset we need to retrieve from a repository
     * @return optional with entity details if found, empty optional if not found
     */
    public Optional<EntityDetail> getEntityDetails(String userId, String guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException {
        String methodName = "getEntityDetails";
        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER, PROCESS, methodName));
    }

    /**
     * Query about the relationships of an entity based on the type of the relationship
     *
     * @param userId    String - userId of user making request.
     * @param assetGuid guid of the asset we need to retrieve the relationships
     * @param relationshipType the type of the relationship
     * @param typeDefName type of the Entity
     *
     * @return List of the relationships if found, empty list if not found
     */
    public List<Relationship> getRelationshipByType(String userId, String assetGuid,
                                                    String relationshipType, String typeDefName) throws UserNotAuthorizedException,
                                                                                                        PropertyServerException{
        final String methodName = "getRelationshipByType";
        String typeGuid = getTypeName(userId, relationshipType);

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    assetGuid,
                                                                                    typeDefName,
                                                                                    typeGuid,
                                                                                    relationshipType,
                                                                                    methodName);

        if (relationships != null) {
            return relationships;
        }

        return new ArrayList<>();
    }

    /**
     * Retrieves guid for a specific type
     *
     * @param userId    String - userId of user making request.
     * @param typeDefName type of the Entity
     *
     * @return Guid of the type if found, null String if not found
     */
    public String getTypeName (String userId, String typeDefName){
        final TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);

        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }


    public EntityDetail getEntityAtTheEnd(String userId, String entityDetailGUID, Relationship relationship) throws InvalidParameterException,
                                                                                                                    PropertyServerException,
                                                                                                                    UserNotAuthorizedException {

        String methodName = "getEntityAtTheEnd";
        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return repositoryHandler.getEntityByGUID(userId,
                                                     relationship.getEntityTwoProxy().getGUID(),
                                                     GUID_PARAMETER,
                                      relationship.getEntityTwoProxy().getType().getTypeDefName(), methodName);
        } else {
            return repositoryHandler.getEntityByGUID(userId,
                                                     relationship.getEntityOneProxy().getGUID(),
                                                     GUID_PARAMETER,
                                      relationship.getEntityOneProxy().getType().getTypeDefName(), methodName);
        }
    }

    /**
     * Adds entities and relationships for the process Context structure
     *
     * @param userId           String - userId of user making request.
     * @param startEntity      parent entity of the relationship
     * @param relationship     the relationship of the parent node
     * @return Entity which is the child of the relationship, null if there is no Entity
     */
    protected EntityDetail writeEntitiesAndRelationships(String userId, EntityDetail startEntity,
                                                         Relationship relationship, AssetContext graph) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException {

        Converter converter = new Converter();
        EntityDetail endEntity = getEntityAtTheEnd(userId, startEntity.getGUID(), relationship);

        if (endEntity == null) return null;

        LineageEntity startVertex = converter.createEntity(startEntity);
        LineageEntity endVertex = converter.createEntity(endEntity);

        graph.addVertex(startVertex);
        graph.addVertex(endVertex);

        GraphContext edge = new GraphContext(relationship.getType().getTypeDefName(),relationship.getGUID(),startVertex, endVertex);
        graph.addEdge(edge);

        return endEntity;
    }


}
