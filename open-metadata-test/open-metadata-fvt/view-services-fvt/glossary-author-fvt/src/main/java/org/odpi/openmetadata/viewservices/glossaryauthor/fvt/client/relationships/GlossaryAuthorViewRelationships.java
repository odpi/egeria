/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface GlossaryAuthorViewRelationships {


    /**
     * Create a Relationship.
     * <p>
     * The result is the Relationship object
     *
     * @param userId       userId under which the request is performed
     * @param relationship     Relationship object to be created
     *
     * @return The Relationship
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Relationship create(String userId, Relationship relationship) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;


    /**
     * Get a Relationship.
     * <p>
     * The result is the requested Relationship object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Relationship object to be retrieved
     *
     * @return The requested Relationship
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Relationship getByGUID(String userId, String guid) throws PropertyServerException,UserNotAuthorizedException, InvalidParameterException ;

    /**
     * Update a Relationship.
     * <p>
     * The result is the updated Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be updated
     * @param relationship     Glossary object with updated values
     * @param isReplace    If the object is to be replaced
     *
     * @return The updated Relationship
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Relationship update(String userId, String guid, Relationship relationship, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Delete a Relationship.
     * <p>
     * The result Void object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Relationship object to be retrieved
     *
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    void delete(String userId, String guid) throws PropertyServerException;

    /**
     * Restore a soft-deleted Relationship.
     * <p>
     * The result is the restored Relationship object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Relationship object to be restored
     *
     * @return The restored Relationship
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Relationship restore(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException,InvalidParameterException  ;

    /**
     * Extract children within a Category
     *
     * @param userId calling user
     * @param parentGuid Category GUID
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param exactValue  exactValue - when false values with trailing characters will match.
     * @param ignoreCase  ignore the case when matching.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Category> getCategoryChildren(String userId, String parentGuid, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

    /**
     * Find Category
     *
     * @param userId calling user
     *
     * @return Categories belonging to Userid
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Relationship> findAll(String userId) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;
    /**
     * Extract children within a Relationship
     *
     * @param userId calling user
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param exactValue  exactValue - when false values with trailing characters will match.
     * @param ignoreCase  ignore the case when matching.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Relationship> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

    /**
     * Extract categories for a Relationship
     *
     * @param userId calling user
     * @param relationshipGuid RelationShip GUID
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Category> getCategories(String userId, String relationshipGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get config for server
     *
     * @param userId calling user
     *
     * @return Config for view server
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    OMAGServerConfig getConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get list of view service config on the server
     *
     * @param userId calling user
     *
     * @return Config for view server
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<ViewServiceConfig> getViewServiceConfigs(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get service config for a particular view Service
     *
     * @param userId calling user
     *
     * @return Config for view server
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    ViewServiceConfig getGlossaryAuthViewServiceConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

}

