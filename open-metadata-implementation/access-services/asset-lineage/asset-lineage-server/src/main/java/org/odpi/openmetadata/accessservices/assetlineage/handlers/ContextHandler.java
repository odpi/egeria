/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class ContextHandler {

    private static final Logger log = LoggerFactory.getLogger(ContextHandler.class);
    private static final String GUID_PARAMETER = "guid";

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private CommonHandler commonHandler;
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
     */
    public ContextHandler(String serviceName,
                             String serverName,
                             InvalidParameterHandler invalidParameterHandler,
                             OMRSRepositoryHelper repositoryHelper,
                             RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.commonHandler = new CommonHandler(serviceName,serverName,invalidParameterHandler,repositoryHelper,repositoryHandler);
    }


    public AssetContext getAssetContext(String serverName, String userId, String guid, String type) {

        graph = new AssetContext();
        try {
            Optional<EntityDetail> entityDetail = getEntityDetails(userId, guid, type);
            if (!entityDetail.isPresent()) {
                log.error("Something is wrong in the OMRS Connector when a specific operation is performed in the metadata collection." +
                        " Entity not found with guid {}", guid);

                throw new AssetLineageException(ENTITY_NOT_FOUND.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                "Retrieving Entity",
                                                ENTITY_NOT_FOUND.getErrorMessage(),
                                                ENTITY_NOT_FOUND.getSystemAction(),
                                                ENTITY_NOT_FOUND.getUserAction());
            }
            buildAssetContext(userId,entityDetail.get());
            return graph;

        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException|
                org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException |
                RepositoryErrorException e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                                            e.getReportingClassName(),
                                            e.getReportingActionDescription(),
                                            e.getErrorMessage(),
                                            e.getReportedSystemAction(),
                                            e.getReportedUserAction());
        }
    }


    private Optional<EntityDetail> getEntityDetails(String userId, String guid,String type) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException {
        String methodName = "getEntityDetails";
        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER,type, methodName));
    }


    //TODO: Cong - Schema Updates
    private void buildAssetContext(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException,
                                                                                    PropertyServerException,
                                                                                    InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                                    org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {

        final String typeDefName = entityDetail.getType().getTypeDefName();

        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(userId,typeDefName);

        //TODO check for Table entities
        if (isComplexSchemaType.isPresent()) {
//            setAssetDetails(userId, assetElement, knownAssetConnection, entityDetail);
        }

        if(hasSchemaAttributeType(typeDefName)) {
            //TODO Change it to new classification
            buildGraphByRelationshipType(userId, entityDetail, LINEAGE_MAPPING, typeDefName);
        }

        List<EntityDetail> schemaAttributes = buildGraphByRelationshipType(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, typeDefName);
        for (EntityDetail schemaAttribute : schemaAttributes) {

            if (isComplexSchemaType(userId, schemaAttribute.getType().getTypeDefName()).isPresent()) {
                setAssetDetails(userId, schemaAttribute);
            } else {
                buildAssetContext(userId, schemaAttribute);
/*                List<EntityDetail> schemaAttributeTypeEntities = buildGraphByRelationshipType(userId, schemaAttribute,
                                                                                                 LINEAGE_MAPPING,
                                                                                                 schemaAttribute.getType().getTypeDefName());*/

            }
        }
    }

    private List<EntityDetail> buildGraphByRelationshipType(String userId, EntityDetail startEntity,
                                                            String relationshipType, String typeDefName) throws UserNotAuthorizedException,
                                                                                                                   PropertyServerException,
                                                                                                                   InvalidParameterException {
        List<Relationship> relationships = commonHandler.getRelationshipsByType(userId, startEntity.getGUID(), relationshipType, typeDefName);

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {

            EntityDetail endEntity = commonHandler.buildEdgeByStartEntity(userId, startEntity, relationship, graph);
            if(endEntity == null) return Collections.emptyList();

            entityDetails.add(endEntity);
        }

        return entityDetails;

    }

    private void setAssetDetails(String userId, EntityDetail startEntity) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException {
        List<EntityDetail> assetEntity = buildGraphByRelationshipType(userId,startEntity, ASSET_SCHEMA_TYPE,startEntity.getType().getTypeDefName());
        Optional<EntityDetail> first = assetEntity.stream().findFirst();
        if(first.isPresent()){
            getAsset(userId, first.get());

        }
    }

    private void getAsset(String userId, EntityDetail dataSet) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException {
        final String typeDefName = dataSet.getType().getTypeDefName();
        if (typeDefName.equals(DATA_FILE)) {
            buildGraphByRelationshipType(userId, dataSet, NESTED_FILE,typeDefName);
        } else {
            buildGraphByRelationshipType(userId, dataSet, DATA_CONTENT_FOR_DATA_SET,typeDefName);

        }
    }

    private Optional<TypeDef> isComplexSchemaType(String userId,String typeDefName) throws RepositoryErrorException,
                                                                                           org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                                           org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {
        TypeDefGallery allTypes =  repositoryHandler.getMetadataCollection().getAllTypes(userId);
        return allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }

    private boolean hasSchemaAttributeType(String typeDefName){
        List<String> entitiesWithSchemaTypes = new ArrayList<>(Arrays.asList(TABULAR_COLUMN, RELATIONAL_COLUMN));
        return entitiesWithSchemaTypes.contains(typeDefName);
    }
}
