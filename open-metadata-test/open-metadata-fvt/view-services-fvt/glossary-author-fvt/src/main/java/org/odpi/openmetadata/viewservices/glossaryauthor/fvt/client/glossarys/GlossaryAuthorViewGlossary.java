/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.glossarys;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface GlossaryAuthorViewGlossary {

    /**
     * Create a Glossary.
     * <p>
     * The result is the Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param glossary     Glossary object to be created
     *
     * @return The Glossary
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */

    Glossary create(String userId, Glossary glossary) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Update a Glossary.
     * <p>
     * The result is the updated Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be updated
     * @param glossary     Glossary object with updated values
     * @param action       To replace existing Glossary or not
     *
     * @return The updated Glossary
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Glossary update(String userId, String guid, Glossary glossary, boolean action) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Get a Glossary.
     * <p>
     * The result is the requested Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be retrieved
     *
     * @return The requested Glossary
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Glossary getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;
    /**
     * Delete a Glossary.
     * <p>
     * The result Void object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be retrieved
     *
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    void delete(String userId, String guid) throws PropertyServerException;


    /**
     * Get a Glossary's relationships
     * <p>
     * The result is a list of Relationships
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be retrieved
     *
     * @return The list of Glossary relationships
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Relationship> getAllRelationships(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Restore a soft-deleted Glossary.
     * <p>
     * The result is the restored Glossary object
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of Glossary object to be restored
     *
     * @return The restored Glossary
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Glossary restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    /**
     * Get the Categories owned by this glossary.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param glossaryGuid        unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @return list of Categories
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    List<Category> getCategories(String userId, String glossaryGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Get the Categories owned by this glossary.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed.
     * @param glossaryGuid        unique identifier of the object to which the found objects should relate.
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     * @param onlyTop     when only the top categories (those categories without parents) are returned.
     * @return list of Categories
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    List<Category> getCategories(String userId, String glossaryGuid, FindRequest findRequest, boolean onlyTop) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;
    /**
     * Extract terms within a glossary
     *
     * @param userId calling user
     * @param glossaryGuid glossary GUID
     * @param findRequest information object for find calls. This include pageSize to limit the number of elements returned.
     *
     * @return list of  terms
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
    */
List<Term> getTerms(String userId, String glossaryGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Create multiple terms within a glossary
     *
     * @param userId calling user
     * @param glossaryGuid glossary GUID
     * @param termArray array of glossary objects that are to be created.
     *
     * @return list of terms created
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<Term> createTerms(String userId, String glossaryGuid, Term[] termArray) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

}
