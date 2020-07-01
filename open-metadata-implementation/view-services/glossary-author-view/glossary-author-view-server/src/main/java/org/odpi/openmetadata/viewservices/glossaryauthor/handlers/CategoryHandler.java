/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The category handler is initialised with a SubjectAreaCategory, that contains the server the call should be sent to.
 * The handler exposes methods for category functionality for the glossary author view
 */
public class CategoryHandler {
    private SubjectAreaCategory subjectAreaCategory;

    /**
     * Constructor for the CategoryHandler
     *
     * @param subjectAreaCategory The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for categories. This is the same as the
     *                            The Glossary author Open Metadata View Service (OMVS) API for categories.
     */
    public CategoryHandler(SubjectAreaCategory subjectAreaCategory) {
        this.subjectAreaCategory = subjectAreaCategory;
    }

    /**
     * Create a Category
     *
     * @param userId           userId under which the request is performed
     * @param suppliedCategory Category to create
     * @return the created category.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Category createCategory(String userId, Category suppliedCategory) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaCategory.category().create(userId, suppliedCategory);
    }

    /**
     * Get a category by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the category to get
     * @return the requested category.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Category getCategoryByGuid(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaCategory.category().getByGUID(userId, guid);
    }

    /**
     * Find Category
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Categorys meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public List<Category> findCategory(String userId, FindRequest findRequest) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaCategory.category().find(userId, findRequest);
    }

    /**
     * Replace a Category. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory category to be updated
     * @return replaced category
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Category replaceCategory(String userId, String guid, Category suppliedCategory) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException {
        return subjectAreaCategory.category().replace(userId, guid, suppliedCategory);
    }

    /**
     * Update a Category. This means to update the category with any non-null attributes from the supplied category.
     * <p>
     * Status is not updated using this call.
     * The GovernanceAction content if specified replaces what is on the server.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory category to be updated
     * @return a response which when successful contains the updated category
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Category updateCategory(String userId, String guid, Category suppliedCategory) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        return subjectAreaCategory.category().update(userId, guid, suppliedCategory);
    }

    /**
     * Delete a Category instance
     * <p>
     * A delete (also known as a soft delete) means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the category to be deleted.
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public void deleteCategory(String userId, String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException {
        subjectAreaCategory.category().delete(userId, guid);
    }

    /**
     * Purge a Category instance
     * <p>
     * A purge means that the category will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the category to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public void purgeCategory(String userId, String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException {
        subjectAreaCategory.category().purge(userId, guid);
    }

    /**
     * Restore a Category
     * <p>
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to restore
     * @return the restored category
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Category restoreCategory(String userId, String guid) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException {
        return subjectAreaCategory.category().restore(userId, guid);
    }

    /**
     * Get Category relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the category to get
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Category guid
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public List<Line> getCategoryRelationships(String userId, String guid, FindRequest findRequest) throws UserNotAuthorizedException, InvalidParameterException, PropertyServerException {
        return subjectAreaCategory.category().getRelationships(userId, guid, findRequest);

    }
}