/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.project;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface GlossaryAuthorViewProject {

    /**
     * Create a project.
     * <p>
     * The result is the project object
     *
     * @param userId       userId under which the request is performed
     * @param project     Project object to be created
     *
     * @return The Project
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Project create(String userId, Project project) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;


    /**
     * Get a Project.
     * <p>
     * The result is the requested Project object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Project object to be retrieved
     *
     * @return The requested Project
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Project getByGUID(String userId, String guid) throws PropertyServerException,UserNotAuthorizedException, InvalidParameterException ;

    /**
     * Update a Project.
     * <p>
     * The result is the updated Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be updated
     * @param project     Glossary object with updated values
     * @param isReplace    If the object is to be replaced
     *
     * @return The updated Project
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Project update(String userId, String guid, Project project, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Delete a Project.
     * <p>
     * The result Void object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Project object to be retrieved
     *
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    void delete(String userId, String guid) throws PropertyServerException;

    /**
     * Restore a soft-deleted Project.
     * <p>
     * The result is the restored Project object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Project object to be restored
     *
     * @return The restored Project
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Project restore(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException,InvalidParameterException  ;

    /**
     * Get a Project's relationships
     * <p>
     * The result is a list of Relationships
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Project object to be retrieved
     *
     * @return The list of Project relationships
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Relationship> getAllRelationships(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException ;


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
    List<Project> findAll(String userId) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;
    /**
     * Extract children within a Project
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
    List<Project> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

    /**
     * Extract Categories within a Project
     *
     * @param userId calling user
     * @param projectGuid  exactValue - when false values with trailing characters will match.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Category> getCategories(String userId, String projectGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

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

    /**
     * Extract relationships within a Project
     *
     * @param userId calling user
     * @param projectGuid  exactValue - when false values with trailing characters will match.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return list of  Relationships
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */

    List<Relationship> getRelationships(String userId, String projectGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;
}

