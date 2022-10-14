/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.subjectAreaDefinitionCategory;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface GlossaryAuthorViewSubjectAreaDefinitionCategory {

    /**
     * Create a subjectAreaDefinition.
     * <p>
     * The result is the subjectAreaDefinition object
     *
     * @param userId       userId under which the request is performed
     * @param subjectAreaDefinition     SubjectAreaDefinition object to be created
     *
     * @return The SubjectAreaDefinition
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    SubjectAreaDefinition create(String userId, SubjectAreaDefinition subjectAreaDefinition) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;


    /**
     * Get a SubjectAreaDefinition.
     * <p>
     * The result is the requested SubjectAreaDefinition object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of SubjectAreaDefinition object to be retrieved
     *
     * @return The requested SubjectAreaDefinition
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    SubjectAreaDefinition getByGUID(String userId, String guid) throws PropertyServerException,UserNotAuthorizedException, InvalidParameterException ;

    /**
     * Update a SubjectAreaDefinition.
     * <p>
     * The result is the updated Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be updated
     * @param subjectAreaDefinition     Glossary object with updated values
     * @param isReplace    If the object is to be replaced
     *
     * @return The updated SubjectAreaDefinition
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    SubjectAreaDefinition update(String userId, String guid, SubjectAreaDefinition subjectAreaDefinition, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Delete a SubjectAreaDefinition.
     * <p>
     * The result Void object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of SubjectAreaDefinition object to be retrieved
     *
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    void delete(String userId, String guid) throws PropertyServerException;

    /**
     * Restore a soft-deleted SubjectAreaDefinition.
     * <p>
     * The result is the restored SubjectAreaDefinition object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of SubjectAreaDefinition object to be restored
     *
     * @return The restored SubjectAreaDefinition
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    SubjectAreaDefinition restore(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException,InvalidParameterException  ;

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
    List<SubjectAreaDefinition> findAll(String userId) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;
    /**
     * Extract children within a SubjectAreaDefinition
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
    List<SubjectAreaDefinition> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

    /**
     * Extract categories for a SubjectAreaDefinition
     *
     * @param userId calling user
     * @param subjectAreaDefinitionGuid RelationShip GUID
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return list of  Categories
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Category> getCategories(String userId, String subjectAreaDefinitionGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

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

