/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.exception.OMRSExceptionWrapper;
import org.odpi.openmetadata.accessservices.glossaryview.exception.OMRSRuntimeExceptionWrapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.glossaryview.server.admin.GlossaryViewServicesInstanceMap.containsInstanceForJvm;
import static org.odpi.openmetadata.accessservices.glossaryview.server.admin.GlossaryViewServicesInstanceMap.getInstanceForJVM;

/**
 * Queries OMRS layer and handles incoming exceptions by reducing them to this OMAS exceptions.
 * See {@code OMRSExceptionWrapper} and {@code OMRSRuntimeExceptionWrapper} for details
 */
public class OMRSClient {

    /**
     * Functional interfaces that extract the GUID of an entity from relationship's end. Their purpose is to be used as
     * an argument for method getEntityDetails
     */
    protected Function<Relationship, String> entityProxyTwoGUIDExtractor = relationship -> relationship.getEntityTwoProxy().getGUID();
    protected Function<Relationship, String> entityProxyOneGUIDExtractor = relationship -> relationship.getEntityOneProxy().getGUID();

    /**
     * Extract an entity detail for the given GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param guid GUID
     *
     * @return optional with entity details if found
     *         empty optional if not found or if {@code serverName} not registered in instance map
     *
     * @throws OMRSExceptionWrapper if any exception is thrown from repository level
     */
    protected Optional<EntityDetail> getEntityDetail(String userId, String serverName, String guid) throws OMRSExceptionWrapper {
        Optional<EntityDetail> entityDetail = Optional.empty();
        if( !containsInstanceForJvm(serverName) ){
            return entityDetail;
        }

        try {
            entityDetail = Optional.ofNullable( getInstanceForJVM(serverName).getMetadataCollection().getEntityDetail(userId, guid) );
        }catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException
                | UserNotAuthorizedException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }

        return entityDetail;
    }

    /**
     * Extract the GUIDs of requested
     * {@code org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef}
     * A GUID belongs to the entity type definition, not the entity detail
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param typeDefNames names of TypeDef
     *
     * @return list of GUIDs if found
     *         empty list if not found or if {@code serverName} not registered in instance map
     *
     * @throws  OMRSRuntimeExceptionWrapper if any exception is thrown from repository level
     */
    protected List<String> getTypeDefGUIDs(String userId, String serverName, String... typeDefNames) throws OMRSRuntimeExceptionWrapper{
        if( !containsInstanceForJvm(serverName) ){
            return Collections.emptyList();
        }

        return Arrays.stream(typeDefNames).map(typeDefName -> {
            try {
                return getInstanceForJVM(serverName).getMetadataCollection().getTypeDefByName(userId, typeDefName).getGUID();
            }catch(InvalidParameterException | RepositoryErrorException | TypeDefNotKnownException | UserNotAuthorizedException e){
                throw new OMRSRuntimeExceptionWrapper(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * Extract relationships of given type and entity
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param entityGUID entity to extract relationships from
     * @param relationshipTypeGUID type of relationship to extract
     *
     * @return relationships if found
     *         empty list if not found or if {@code serverName} not registered in instance map
     *
     * @throws OMRSExceptionWrapper if any exception is thrown from repository level
     */
    protected List<Relationship> getRelationships(String userId, String serverName, String entityGUID, String relationshipTypeGUID)
            throws OMRSExceptionWrapper{
        if( !containsInstanceForJvm(serverName) ){
            return Collections.emptyList();
        }

        List<Relationship> relationships;
        try {
            relationships = getInstanceForJVM(serverName).getMetadataCollection()
                    .getRelationshipsForEntity(userId, entityGUID, relationshipTypeGUID, 0, null,
                            null, null, SequencingOrder.ANY, 0);
        }catch(InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException |
                PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }

        return relationships;
    }

    /**
     * Extract entities related to provided guid
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param entityGUID entity to extract
     * @param relationshipTypeGUIDs type of relationships to extract
     *
     * @return relationships if found
     *         empty list if not found or if {@code serverName} not registered in instance map
     *
     * @throws OMRSExceptionWrapper if any exception is thrown from repository level
     */
    protected List<EntityDetail> getNeighbourhood(String userId, String serverName, String entityGUID, List<String> relationshipTypeGUIDs)
            throws OMRSExceptionWrapper{
        if( !containsInstanceForJvm(serverName) ){
            return Collections.emptyList();
        }

        List<EntityDetail> entityDetails;
        try {
            InstanceGraph graph = getInstanceForJVM(serverName).getMetadataCollection()
                    .getEntityNeighborhood(userId, entityGUID, null, relationshipTypeGUIDs, null,
                            null, null, 0);
            entityDetails = graph.getEntities();
        }catch(InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException |
                PropertyErrorException | FunctionNotSupportedException | UserNotAuthorizedException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }

        return entityDetails;
    }

    /**
     * Extract entity details from given relationships
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param relationships entity to extract relationships from
     * @param guidExtractor use {@code entityProxyOneGUIDExtractor} or {@code entityProxyTwoGUIDExtractor}, depending on
     *                      which end of the relationship is needed
     *
     * @return entity details if found
     *         empty list if {@code serverName} not registered in instance map
     *
     * @throws OMRSRuntimeExceptionWrapper if any exception is thrown from repository level
     */
    protected List<EntityDetail> getEntityDetails(String userId, String serverName, List<Relationship> relationships,
                                                  Function<Relationship, String> guidExtractor) throws OMRSRuntimeExceptionWrapper{
        if( !containsInstanceForJvm(serverName) ){
            return Collections.emptyList();
        }

        return relationships.stream().map( relationship -> {
            try{
                return getInstanceForJVM(serverName).getMetadataCollection().getEntityDetail(userId, guidExtractor.apply(relationship));
            }catch( InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException
                    | UserNotAuthorizedException e){
                throw new OMRSRuntimeExceptionWrapper(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * Extract all entity definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param typeDefName entity type name
     *
     * @return EntityDetail entities
     */
    protected List<EntityDetail> getAllEntityDetails(String userId, String serverName, String typeDefName) throws OMRSExceptionWrapper{
        List<EntityDetail> entities = Collections.emptyList();
        if( !containsInstanceForJvm(serverName) ){
            return entities;
        }

        try {
            TypeDef glossaryTypeDef = getInstanceForJVM(serverName).getMetadataCollection().getTypeDefByName(userId, typeDefName);
            entities = getInstanceForJVM(serverName).getMetadataCollection()
                    .findEntitiesByProperty(userId, glossaryTypeDef.getGUID(), new InstanceProperties(), MatchCriteria.ALL,
                            0, null, null, null,
                            null, SequencingOrder.LAST_UPDATE_RECENT, 0);
        }catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | PropertyErrorException
                | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException
                | TypeDefNotKnownException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }

        return entities;
    }

    /**
     * Returns the repository helper for the given serverName
     *
     * @param serverName instance to call
     *
     * @return optional with helper if found
     *         empty optional if not found
     */
    protected Optional<OMRSRepositoryHelper> getOMRSRepositoryHelper(String serverName){
        Optional<OMRSRepositoryHelper> helper = Optional.empty();
        if( !containsInstanceForJvm(serverName) ){
            return helper;
        }

        return Optional.ofNullable(getInstanceForJVM(serverName).getRepositoryConnector().getRepositoryHelper());
    }

}
