/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;
import java.util.Optional;

/**
 * Queries OMRS layer. Incoming known exceptions are thrown as they are or a {@code GlossaryViewOmasException} is built and thrown.
 */
public class OMRSClient {

    private static final String DEFAULT_ACTION_DESCRIPTION_PREFIX = "Internal server error thrown by OMRS when executing";
    private static final String DEFAULT_SYSTEM_ACTION = "No known system actions performed";
    private static final String DEFAULT_USER_ACTION = "Possible user actions: retry or check logs";

    protected GlossaryViewInstanceHandler instanceHandler = new GlossaryViewInstanceHandler();

    /**
     * Extract an entity detail for the given GUID
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param guid entity to extract
     * @param entityTypeName entity type name
     * @param methodName calling method
     *
     * @return optional with entity details if found, empty optional if not
     *
     * @throws GlossaryViewOmasException if any exception is thrown from repository level, other than InvalidParameterException,
     *                                   UserNotAuthorizedException or PropertyServerException
     */
    protected Optional<EntityDetail> getEntityDetail(String userId, String serverName, String guid, String entityTypeName, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryViewOmasException {

        Optional<EntityDetail> entityDetail ;
        try {
            entityDetail = Optional.ofNullable( instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                    .getEntityByGUID(userId, guid, "guid", entityTypeName, methodName) );
        }catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        } catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(),
                    DEFAULT_ACTION_DESCRIPTION_PREFIX + " getEntityByGUID: guid " + guid + " entityTypeName" + entityTypeName,
                    e.getClass() + " - " + e.getMessage(), DEFAULT_SYSTEM_ACTION, DEFAULT_USER_ACTION);
        }
        return entityDetail;
    }

    /**
     * Extract entities related to provided guid
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityGUID target entity
     * @param entityTypeName entity type name
     * @param relationshipTypeGUID relationship type guid to navigate
     * @param relationshipTypeName relationship type name to navigate
     * @param from from
     * @param size size
     * @param methodName calling method
     *
     * @return entities if found
     *
     * @throws GlossaryViewOmasException if any exception is thrown from repository level, other than InvalidParameterException,
     *                                   UserNotAuthorizedException or PropertyServerException
     */
    protected List<EntityDetail> getRelatedEntities(String userId, String serverName, String entityGUID, String entityTypeName,
                                                    String relationshipTypeGUID, String relationshipTypeName, Integer from, Integer size,
                                                    String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryViewOmasException {
        List<EntityDetail> entityDetails;
        try {
            entityDetails = instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                    .getEntitiesForRelationshipType(userId, entityGUID, entityTypeName, relationshipTypeGUID, relationshipTypeName,
                            from, size, methodName);
        }catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        } catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(), DEFAULT_ACTION_DESCRIPTION_PREFIX +
                    " getEntitiesForRelationshipType: entityGUID " + entityGUID + " entityTypeName " + entityTypeName +
                    " relationshipTypeGUID " + relationshipTypeGUID + " relationshipTypeName " + relationshipTypeName,
                    e.getClass() + " - " + e.getMessage(), DEFAULT_SYSTEM_ACTION, DEFAULT_USER_ACTION);
        }
        return entityDetails;
    }


    /**
     * Extract entities related to provided guid
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityGUID target entity
     * @param entityTypeName entity type name
     * @param anchorAtEnd1 which end should the target entity be at
     * @param relationshipTypeGUID relationship type guid to navigate
     * @param relationshipTypeName relationship type name to navigate
     * @param from from
     * @param size size
     * @param methodName calling method
     *
     * @return entities if found
     *
     * @throws GlossaryViewOmasException if any exception is thrown from repository level, other than InvalidParameterException,
     *                                   UserNotAuthorizedException or PropertyServerException
     */
    protected List<EntityDetail> getSubEntities(String userId, String serverName, String entityGUID, String entityTypeName,
                                                boolean anchorAtEnd1, String relationshipTypeGUID, String relationshipTypeName,
                                                Integer from, Integer size, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryViewOmasException {

        List<EntityDetail> entityDetails;
        try {
            entityDetails = instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                                    .getEntitiesForRelationshipEnd(userId, entityGUID, entityTypeName, anchorAtEnd1,
                                                                   relationshipTypeGUID, relationshipTypeName,
                                                                   from, size, methodName);
        }catch(InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        }catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(), DEFAULT_ACTION_DESCRIPTION_PREFIX +
                    " getEntitiesForRelationshipEnd: guid " + entityGUID + " entityTypeName " + entityTypeName +
                    " anchorAtEnd1 " + anchorAtEnd1 + " relationshipTypeGUID " + relationshipTypeGUID +
                    " relationshipTypeName " + relationshipTypeName, e.getClass() + " - " + e.getMessage(),
                    DEFAULT_SYSTEM_ACTION, DEFAULT_USER_ACTION);
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
     * @param methodName calling method
     *
     * @return entities if found
     *
     * @throws GlossaryViewOmasException if any exception is thrown from repository level, other than InvalidParameterException,
     *                                   UserNotAuthorizedException or PropertyServerException
     */
    protected List<EntityDetail> getAllEntityDetails(String userId, String serverName, String entityTypeGUID, Integer from, Integer size,
                                                     String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryViewOmasException {
        List<EntityDetail> entities;
        try {
            entities = instanceHandler.getRepositoryHandler(userId, serverName, methodName)
                    .getEntitiesByType(userId, entityTypeGUID, from, size, methodName);
        }catch(InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        }catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(), DEFAULT_ACTION_DESCRIPTION_PREFIX +
                    " getEntitiesByType: entityTypeGUID " + entityTypeGUID, e.getClass() + " - " + e.getMessage(),
                    DEFAULT_SYSTEM_ACTION, DEFAULT_USER_ACTION);
        }

        return entities;
    }
}
