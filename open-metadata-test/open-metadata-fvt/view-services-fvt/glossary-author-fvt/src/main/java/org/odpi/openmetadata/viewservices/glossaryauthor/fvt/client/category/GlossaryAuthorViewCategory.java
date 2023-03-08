/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.category;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.*;


import java.util.Map;

public interface GlossaryAuthorViewCategory {

    /**
     * Create a Category.
     * <p>
     * The result is the Category object
     *
     * @param userId       userId under which the request is performed
     * @param category     Glossary object to be created
     *
     * @return The Category
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Category create(String userId, Category category) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;


    /**
     * Get a Category.
     * <p>
     * The result is the requested Category object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Category object to be retrieved
     *
     * @return The requested Category
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Category getByGUID(String userId, String guid) throws PropertyServerException,UserNotAuthorizedException, InvalidParameterException ;

    /**
     * Update a Category.
     * <p>
     * The result is the updated Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be updated
     * @param category     Glossary object with updated values
     * @param isReplace    If the object is to be replaced
     *
     * @return The updated Category
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Category update(String userId, String guid, Category category, boolean isReplace) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Delete a Category.
     * <p>
     * The result Void object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Category object to be retrieved
     *
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    void delete(String userId, String guid) throws PropertyServerException;

    /**
     * Restore a soft-deleted Category.
     * <p>
     * The result is the restored Category object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Category object to be restored
     *
     * @return The restored Category
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Category restore(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException,InvalidParameterException  ;

    /**
     * Get a Category's relationships
     * <p>
     * The result is a list of Relationships
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Category object to be retrieved
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return The list of Category relationships
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Relationship> getRelationships(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get a Category's relationships
     * <p>
     * The result is a list of Relationships
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Category object to be retrieved
     *
     * @return The list of Category relationships
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Relationship> getAllRelationships(String userId, String guid) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException ;

    /**
     * Extract terms within a Category
     *
     * @param userId calling user
     * @param categoryGuid Category GUID
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return list of  terms
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Term> getTerms(String userId, String categoryGuid, FindRequest findRequest) throws PropertyServerException,UserNotAuthorizedException, InvalidParameterException ;


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
    List<Category> findAll(String userId) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;
    /**
     * Extract children within a Category
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
    List<Category> find(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException;

}

