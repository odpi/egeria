/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.exception.OMRSExceptionWrapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Optional;

/**
 * Queries OMRS layer and handles incoming exceptions by reducing them to this OMAS exceptions.
 * See {@code OMRSExceptionWrapper} and {@code OMRSRuntimeExceptionWrapper} for details
 */
public class OMRSClient {

    protected GlossaryViewInstanceHandler instanceHandler = new GlossaryViewInstanceHandler();

    /**
     * Extract an entity detail for the given GUID
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param guid entity to extract
     * @param entityTypeName entity type name
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws OMRSExceptionWrapper if any exception is thrown from repository level
     */
    protected Optional<EntityDetail> getEntityDetail(String userId, String serverName, String guid, String entityTypeName, String methodName)
            throws OMRSExceptionWrapper {

        Optional<EntityDetail> entityDetail ;
        try {
            entityDetail = Optional.ofNullable( instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                    .getEntityByGUID(userId, guid, "guid", entityTypeName, methodName) );
        }catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }
        return entityDetail;
    }

    /**
     * Extract entities related to provided guid
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityGUID target entity
     * @param relationshipTypeGUID relationship type guid to navigate
     * @param relationshipTypeName relationship type name to navigate
     * @param from from
     * @param size size
     *
     * @return entities if found
     *
     * @throws OMRSExceptionWrapper if any exception is thrown from repository level
     */
    protected List<EntityDetail> getRelatedEntities(String userId, String serverName, String entityGUID, String entityTypeName,
                                                    String relationshipTypeGUID, String relationshipTypeName, Integer from, Integer size,
                                                    String methodName)
            throws OMRSExceptionWrapper{

        List<EntityDetail> entityDetails;
        try {
            entityDetails = instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                    .getEntitiesForRelationshipType(userId, entityGUID, entityTypeName, relationshipTypeGUID, relationshipTypeName,
                            from, size, methodName);
        }catch(InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }
        return entityDetails;
    }

    /**
     * Extract all entity definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityTypeGUID entity type
     * @param from from
     * @param size size
     *
     * @return entities if found
     *
     * @throws OMRSExceptionWrapper if any exception is thrown from repository level
     */
    protected List<EntityDetail> getAllEntityDetails(String userId, String serverName, String entityTypeGUID, Integer from, Integer size,
                                                     String methodName)
            throws OMRSExceptionWrapper{
        List<EntityDetail> entities;
        try {
            entities = instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                    .getEntitiesByName(userId, new InstanceProperties(), entityTypeGUID, from, size, methodName);
        }catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw new OMRSExceptionWrapper(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(),
                    e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }

        return entities;
    }

    /**
     * Returns the repository helper for the given serverName
     *
     * @param userId calling user
     * @param serverName instance to call
     *
     * @return optional with helper if found
     *         empty optional if not found
     */
    protected Optional<OMRSRepositoryHelper> getOMRSRepositoryHelper(String userId, String serverName){
        Optional<OMRSRepositoryHelper> helper = Optional.empty();
        try {
            helper = Optional.ofNullable(instanceHandler.getRepositoryConnector(userId, serverName, serverName).getRepositoryHelper());
        }catch(InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            return helper;
        }
        return helper;
    }

}
