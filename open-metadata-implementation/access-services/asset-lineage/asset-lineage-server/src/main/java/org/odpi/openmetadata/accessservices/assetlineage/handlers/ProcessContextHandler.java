/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.RELATIONSHIP_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PORT_ALIAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PROCESS_PORT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.immutableProcessRelationshipsTypes;

/**
 * The process context handler provides methods to build lineage context from processes.
 */
public class ProcessContextHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessContextHandler.class);

    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final List<String> supportedZones;
    private final CommonHandler commonHandler;

    private AssetContext graph;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param supportedZones          configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     */
    public ProcessContextHandler(String serviceName,
                                 String serverName,
                                 InvalidParameterHandler invalidParameterHandler,
                                 OMRSRepositoryHelper repositoryHelper,
                                 RepositoryHandler repositoryHandler,
                                 List<String> supportedZones) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.commonHandler = new CommonHandler(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler);
        this.supportedZones = supportedZones;
    }

    /**
     * Retrieves the full context for a Process
     *
     * @param userId      String - userId of user making request.
     * @param processGuid guid of the asset that has been created
     * @return Map of the relationships between the Entities that are relevant to a Process
     */
    public Map<String, Set<GraphContext>> getProcessContext(String userId, String processGuid) {

        final String methodName = "getProcessContext";

        graph = new AssetContext();

        try {

            Optional<EntityDetail> entityDetail = commonHandler.getEntityDetails(userId, processGuid, PROCESS);
            if (!entityDetail.isPresent()) {
                log.error("Entity with guid {} was not found in any metadata repository", processGuid);

                throw new AssetLineageException(ENTITY_NOT_FOUND.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                "Retrieving Entity",
                                                 ENTITY_NOT_FOUND.getErrorMessage(),
                                                 ENTITY_NOT_FOUND.getSystemAction(),
                                                 ENTITY_NOT_FOUND.getUserAction());
            }


            invalidParameterHandler.validateAssetInSupportedZone(processGuid,
                                                                 GUID_PARAMETER,
                                                                 commonHandler.getAssetZoneMembership(entityDetail.get().getClassifications()),
                                                                 supportedZones,
                                                                 ASSET_LINEAGE_OMAS,
                                                                 methodName);

            return checkIfAllRelationshipsExist(userId,entityDetail.get());

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                                            e.getReportingClassName(),
                                            e.getReportingActionDescription(),
                                            e.getErrorMessage(),
                                            e.getReportedSystemAction(),
                                            e.getReportedUserAction());
        }
    }

    private Map<String,Set<GraphContext>> checkIfAllRelationshipsExist(String userId, EntityDetail entityDetail) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException {

        boolean entitiesTillLastRelationshipExist = hasEntitiesLinkedWithProcessPort(userId, entityDetail);
        if(entitiesTillLastRelationshipExist){
            return graph.getNeighbors();
        }

        log.error("Some relationships are missing for the entity with guid {}",entityDetail.getGUID());

        throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        "Retrieving Relationships",
                                        RELATIONSHIP_NOT_FOUND.getErrorMessage(),
                                        RELATIONSHIP_NOT_FOUND.getSystemAction(),
                                        RELATIONSHIP_NOT_FOUND.getUserAction());
    }

    private boolean hasEntitiesLinkedWithProcessPort(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException,
                                                                                                                 PropertyServerException,
                                                                                                                 InvalidParameterException {

        final String typeDefName = entityDetail.getType().getTypeDefName();
        List<EntityDetail> entityDetails = getRelationshipsBetweenEntities(userId, entityDetail.getGUID(), PROCESS_PORT, typeDefName);

        if (entityDetails.isEmpty()){
            log.error("No relationships Process Port has been found for the entity with guid {}",entityDetail.getGUID());

            throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            "Retrieving Relationship",
                                            RELATIONSHIP_NOT_FOUND.getErrorMessage(),
                                            RELATIONSHIP_NOT_FOUND.getSystemAction(),
                                            RELATIONSHIP_NOT_FOUND.getUserAction());
    }

       return hasRelationshipBasedOnType(entityDetails, userId);

    }


    /**
     * Retrieves the relationships of an Entity
     *
     * @param userId           String - userId of user making request.
     * @param guid             guid of parent entity
     * @param relationshipType type of the relationship
     * @param typeDefName      type of the entity that has the Relationship
     * @return List of entities that are on the other end of the relationship, empty list if none
     */
    private List<EntityDetail> getRelationshipsBetweenEntities(String userId, String guid,
                                                               String relationshipType, String typeDefName) throws UserNotAuthorizedException,
                                                                                                                   PropertyServerException,
                                                                                                                   InvalidParameterException {
        List<Relationship> relationships = commonHandler.getRelationshipsByType(userId, guid, relationshipType,typeDefName);
        EntityDetail startEntity = repositoryHandler.getEntityByGUID(userId, guid, "guid", typeDefName, "getRelationships");

        if (startEntity == null) return Collections.emptyList();
        String startEntityType = startEntity.getType().getTypeDefName();

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {

            if(relationship.getType().getTypeDefName().equals(ATTRIBUTE_FOR_SCHEMA) &&
                    startEntityType.equals(TABULAR_COLUMN)){
                continue;
            }
            EntityDetail endEntity = commonHandler.buildGraphEdgeByRelationship(userId,startEntity,relationship,graph,false);
            if(endEntity == null) return Collections.emptyList();

            entityDetails.add(endEntity);
        }

        return entityDetails;

    }

    /**
     * Creates the full context for a Process. There are two cases, a process can have a relationship to either
     * a Port Alias or to a Port Implementation. In case of Port Alias it should take the context until Port Implementation
     * entities otherwise it should take the context down to TabularColumn entities.
     * @param entityDetails      list of entities
     * @param userId             String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasRelationshipBasedOnType(List<EntityDetail> entityDetails, String userId) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException {
        boolean relationshipsExist = false;
        if (checkIfEntityExistWithSpecificType(entityDetails,PORT_ALIAS)) {
            relationshipsExist = hasEndRelationship(entityDetails,userId);
        }

        if (checkIfEntityExistWithSpecificType(entityDetails,PORT_IMPLEMENTATION)) {
            relationshipsExist = hasTabularSchemaTypes(entityDetails,userId);
        }

        return relationshipsExist;
    }

    /**
     * Returns if the entities that are passed as an argument in the method have any relationships.
     * @param entityDetails      list of entities
     * @param userId             String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasEndRelationship(List<EntityDetail> entityDetails, String userId) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {
            result.addAll(getRelationshipsBetweenEntities(userId,
                                                          entityDetail.getGUID(),
                                                          immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()),
                                                          entityDetail.getType().getTypeDefName()));
        }
        return  !result.isEmpty();
    }

    /**
     * Returns if there are any TabularSchemaTypes that are related to a Port Implementation Entity.
     * @param entityDetails      list of entities
     * @param userId             String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasTabularSchemaTypes(List<EntityDetail> entityDetails, String userId) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException {
        List<EntityDetail>  result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {

            List<EntityDetail> tabularSchemaType = getRelationshipsBetweenEntities(userId,
                                                                                   entityDetail.getGUID(),
                                                                                   immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()),
                                                                                   entityDetail.getType().getTypeDefName());
            Optional<EntityDetail> first = tabularSchemaType.stream().findFirst();
            result.add(first.orElse(null));
        }
        return hasSchemaAttributes(result,userId);
    }

    /**
     * Returns if the TabularColumns are part of a TabularSchemaType.
     * @param entityDetails      list of entities
     * @param userId             String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasSchemaAttributes(List<EntityDetail> entityDetails, String userId) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException {
        List<EntityDetail>  result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {

            List<EntityDetail>  newListOfEntityDetails = getRelationshipsBetweenEntities(userId,
                                                                                         entityDetail.getGUID(),
                                                                                         immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()),
                                                                                         entityDetail.getType().getTypeDefName());
            result.addAll(newListOfEntityDetails);
        }
        return hasEndRelationship(result,userId);
    }

    /**
     * Check if the Type of the entity exists on Open Metadata Types.
     * @param entityDetails      list of entities
     * @param typeDefName        String - the type to be checked
     * @return Boolean if type exists or not
     */
    private boolean checkIfEntityExistWithSpecificType(List<EntityDetail> entityDetails,String typeDefName){
        return entityDetails.stream().anyMatch(entity -> entity.getType().getTypeDefName().equals(typeDefName));
    }
}

