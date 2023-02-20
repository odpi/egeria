/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Date;
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

        Optional<EntityDetail> entityDetail;
        try {

            OpenMetadataAPIGenericHandler<GlossaryViewEntityDetail> entitiesHandler = instanceHandler.getEntitiesHandler(userId,
                    serverName, methodName);
            entityDetail = Optional.ofNullable(entitiesHandler.getEntityFromRepository(userId, guid,
                    OpenMetadataAPIMapper.GUID_PROPERTY_NAME, entityTypeName, null, null,
                    false, false, null, null, methodName));
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        } catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(),
                    DEFAULT_ACTION_DESCRIPTION_PREFIX + " getEntityFromRepository: guid " + guid + " entityTypeName" + entityTypeName,
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

            OpenMetadataAPIGenericHandler<GlossaryViewEntityDetail> entitiesHandler = instanceHandler.getEntitiesHandler(userId,
                    serverName, methodName);
            entityDetails = entitiesHandler.getAttachedEntities(userId, entityGUID, OpenMetadataAPIMapper.GUID_PROPERTY_NAME,
                    entityTypeName, relationshipTypeGUID, relationshipTypeName, null,
                    null, null, 0, false, false, from, size, null, methodName);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        } catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(), DEFAULT_ACTION_DESCRIPTION_PREFIX +
                    " getAttachedEntities: entityGUID " + entityGUID + " entityTypeName " + entityTypeName +
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
                                                String relationshipTypeGUID, String relationshipTypeName,
                                                Integer from, Integer size, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryViewOmasException {

        List<EntityDetail> entityDetails;
        try {
            OpenMetadataAPIGenericHandler<GlossaryViewEntityDetail> entitiesHandler = instanceHandler.getEntitiesHandler(userId,
                    serverName, methodName);
            entityDetails = entitiesHandler.getAttachedEntities(userId, entityGUID, OpenMetadataAPIMapper.GUID_PROPERTY_NAME,
                    entityTypeName, relationshipTypeGUID,relationshipTypeName, null,
                    null, null, 2, false,
                    false, null, from, size, null, methodName);
        }catch(InvalidParameterException | UserNotAuthorizedException | PropertyServerException e){
            throw e;
        }catch (Exception e){
            throw new GlossaryViewOmasException(500, this.getClass().getName(), DEFAULT_ACTION_DESCRIPTION_PREFIX +
                    " getAttachedEntities: guid " + entityGUID + " entityTypeName " + entityTypeName +
                    " relationshipTypeGUID " + relationshipTypeGUID + " relationshipTypeName " + relationshipTypeName,
                    e.getClass() + " - " + e.getMessage(), DEFAULT_SYSTEM_ACTION, DEFAULT_USER_ACTION);
        }
        return entityDetails;
    }


    /**
     * Extract all entity definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityTypeName entity type name
     * @param entityTypeGUID entity type guid
     * @param from from
     * @param size size
     * @param methodName calling method
     *
     * @return entities if found
     *
     * @throws GlossaryViewOmasException if any exception is thrown from repository level, other than InvalidParameterException,
     *                                   UserNotAuthorizedException or PropertyServerException
     */
    protected List<EntityDetail> getAllEntityDetails(String userId, String serverName, String entityTypeName,
                                                     String entityTypeGUID, Integer from, Integer size,
                                                     String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryViewOmasException {
        List<EntityDetail> entities;
        try {
            OpenMetadataAPIGenericHandler<GlossaryViewEntityDetail> entitiesHandler = instanceHandler.getEntitiesHandler(userId,
                    serverName, methodName);
            entities = entitiesHandler.getEntitiesByType(userId, entityTypeGUID, entityTypeName, null,
                    false, false, null, from, size, null,
                    methodName);
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
