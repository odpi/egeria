/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.accessservices.assetlineage.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.ProcesRelationships;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class ProcessHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessHandler.class);

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private CommonHandler commonHandler;
    private ProcesRelationships procesRelationships = new ProcesRelationships();

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
    public ProcessHandler(String serviceName,
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
     * Retrieves the full context for a Process
     *
     * @param userId    String - userId of user making request.
     * @param processGuid guid of the asset that has been created
     * @return Map of the relationships between the Entities that are relevant to a Process
     */
    public Map<String, Set<Edge>> getProcessContext(String userId, String processGuid) {

        commonHandler = new CommonHandler(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler);

        try {

            Optional<EntityDetail> entityDetail = commonHandler.getEntityDetails(userId, processGuid);
            if (!entityDetail.isPresent()) {
                log.error("Something is wrong in the OMRS Connector when a specific operation" +
                        " is performed in the metadata collection. Entity not found with guid {}", processGuid);

                throw new AssetLineageException(ENTITY_NOT_FOUND.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                "Retrieving Entity",
                                                 ENTITY_NOT_FOUND.getErrorMessage(),
                                                 ENTITY_NOT_FOUND.getSystemAction(),
                                                 ENTITY_NOT_FOUND.getUserAction());
            }

            getEntitiesLinkedWithProcessPort(userId, entityDetail.get());
            return procesRelationships.getNeighbors();

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                                            e.getReportingClassName(),
                                            e.getReportingActionDescription(),
                                            e.getErrorMessage(),
                                            e.getReportedSystemAction(),
                                            e.getReportedUserAction());
        }
    }


    private void getEntitiesLinkedWithProcessPort(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {

        final String typeDefName = entityDetail.getType().getTypeDefName();
        getRelationships(userId, entityDetail.getGUID(), PROCESS_PORT, typeDefName);

    }

    private void getRelationships(String userId, String initialEntityGuid, String relationshipType, String typeDefName) throws UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {

        List<Relationship> relationships = commonHandler.getRelationshipByType(userId, initialEntityGuid, relationshipType, typeDefName);


        for(Relationship relationship: relationships){
            if(relationship.getEntityTwoProxy().getType().getTypeDefName().equals(PORT_ALIAS)){

                List<EntityDetail> entityDetails = mapRelationshipToElements(relationship,userId,initialEntityGuid);

                if(!entityDetails.isEmpty()){

                    for(EntityDetail entityDetail: entityDetails){
                        String entityGuid = entityDetail.getGUID();
                        relationships = commonHandler.getRelationshipByType(userId, entityGuid, PORT_DELEGATION, PORT_ALIAS);

                        mapRelationshipToElements(relationship,userId,entityGuid);
                    }
                }

            }
            getPortImplementation(relationships);

        }




    }

    private void getPortImplementation(List<Relationship> relationships){

    }

    private List<EntityDetail> mapRelationshipToElements(Relationship relationship, String userId, String guid) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        List<EntityDetail> entityDetails = new ArrayList<>();
        EntityDetail startEntity = commonHandler.getStartEntity(userId, guid, relationship);

        LineageEntity startVertex = createEntity(startEntity);
        procesRelationships.addVertex(startVertex);


        EntityDetail endEntity = commonHandler.getEntityAtTheEnd(userId, guid, relationship);
        LineageEntity endVertex = createEntity(endEntity);
        procesRelationships.addVertex(endVertex);

        Edge edge = new Edge(relationship.getType().getTypeDefName(),startVertex,endVertex);
        procesRelationships.addEdge(edge);

        entityDetails.add(endEntity);


        return entityDetails;
    }

    private List<EntityDetail> mapRelationshipsToElements(List<Relationship> relationships, String userId, String guid) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        List<EntityDetail> entityDetails = new ArrayList<>(relationships.size());
        EntityDetail startEntity = commonHandler.getStartEntity(userId, guid, relationships.get(0));

        LineageEntity startVertex = createEntity(startEntity);
        procesRelationships.addVertex(startVertex);

        for (Relationship relationship : relationships) {

            EntityDetail endEntity = commonHandler.getEntityAtTheEnd(userId, guid, relationship);
            LineageEntity endVertex = createEntity(endEntity);
            procesRelationships.addVertex(endVertex);

            Edge edge = new Edge(relationship.getType().getTypeDefName(),startVertex,endVertex);
            procesRelationships.addEdge(edge);

            entityDetails.add(endEntity);

        }
        return entityDetails;
    }

    private LineageEntity createEntity(EntityDetail entityDetail){
        LineageEntity lineageEntity = new LineageEntity();
        lineageEntity.setGuid(entityDetail.getGUID());
        lineageEntity.setCreatedBy(entityDetail.getCreatedBy());
        lineageEntity.setCreateTime(entityDetail.getCreateTime());
        lineageEntity.setTypeDefName(entityDetail.getType().getTypeDefName());
        lineageEntity.setUpdatedBy(entityDetail.getUpdatedBy());
        lineageEntity.setUpdateTime(entityDetail.getUpdateTime());

        Map<String, Object> properties = getPropertiesForEntity(entityDetail);
        lineageEntity.setProperties(properties);

        return lineageEntity;
        }

        private Map<String, Object> getPropertiesForEntity(EntityDetail entityDetail) {
            final String methodName = "getPropertiesForEntity";

            Map<String, InstancePropertyValue> instancePropertiesMap = entityDetail.getProperties().getInstanceProperties();
            Map<String, Object> properties = new HashMap<>();

            for (Map.Entry<String, InstancePropertyValue> entry : instancePropertiesMap.entrySet()) {

                String key = entry.getKey();
                InstancePropertyValue value = entry.getValue();

                if (value.getInstancePropertyCategory().getName().equalsIgnoreCase("PRIMITIVE")) {

                    if (value.getTypeName().equals("int")) {
                        properties.put(key, repositoryHelper.getIntProperty(ASSET_LINEAGE_OMAS, key, entityDetail.getProperties(), methodName)).toString();
                    }
                    properties.put(key, repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS, key, entityDetail.getProperties(), methodName));
                }

                if (value.getInstancePropertyCategory().getName().equalsIgnoreCase("ENUM")) {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                    properties.put(key, enumPropertyValue.getSymbolicName());
                }

                if (value.getInstancePropertyCategory().getName().equalsIgnoreCase("ARRAY")) {
                    properties.put(key, repositoryHelper.getStringArrayProperty(ASSET_LINEAGE_OMAS, key,
                            entityDetail.getProperties(), methodName));
                }

            }
            return properties;
        }
}

