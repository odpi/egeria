/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The category handler is initialised with a SubjectAreaCategory, that contains the server the call should be sent to.
 * The handler exposes methods for category functionality for the glossary author view
 */
public class CategoryHandler
{
    private static final Logger log = LoggerFactory.getLogger(CategoryHandler.class);

    private SubjectAreaCategory subjectAreaCategory;

    /**
     * Constructor for the CategoryHandler
     * @param subjectAreaCategory The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for categories. This is the same as the
     *                           The Glossary author Open Metadata View Service (OMVS) API for categories.
     */
    public CategoryHandler(SubjectAreaCategory subjectAreaCategory) {
      this.subjectAreaCategory =subjectAreaCategory;
    }
    /**
     * Create a Category
     * @param userId  userId under which the request is performed
     * @param suppliedCategory Category to create
     * @return the created category.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws ClassificationException Error processing a classification
     * @throws FunctionNotSupportedException   Function not supported
     * @throws UnrecognizedGUIDException Unrecognised GUID
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Category createCategory(String userId, Category suppliedCategory) throws MetadataServerUncontactableException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException {
        return subjectAreaCategory.createCategory(userId, suppliedCategory);
    }

    /**
     * Get a category by guid.
     * @param userId userId under which the request is performed
     * @param guid guid of the category to get
     * @return the requested category.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws UnrecognizedGUIDException unrecognised GUID
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Category getCategoryByGuid(String userId, String guid) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException {
        return subjectAreaCategory.getCategoryByGuid(userId, guid);
    }
    /**
     * Find Category
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Category property values (this does not include the CategorySummary content).
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Categorys meeting the search Criteria
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public List<Category> findCategory(String userId, String searchCriteria, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaCategory.findCategory(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
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
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Category replaceCategory(String userId, String guid, Category suppliedCategory) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaCategory.replaceCategory(userId, guid, suppliedCategory);
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
     * @throws FunctionNotSupportedException        Function not supported
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Category updateCategory(String userId, String guid, Category suppliedCategory) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaCategory.updateCategory(userId, guid, suppliedCategory);
    }
    /**
     * Delete a Category instance
     *
     * A delete (also known as a soft delete) means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the category to be deleted.
     * @return the deleted category
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws EntityNotDeletedException      a soft delete was issued but the relationship was not deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Category deleteCategory(String userId, String guid) throws  MetadataServerUncontactableException,UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, EntityNotDeletedException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaCategory.deleteCategory(userId,guid);
    }
    /**
     * Purge a Category instance
     *
     * A purge means that the category will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the category to be deleted.
     *
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws EntityNotPurgedException a hard delete was issued but the category was not purged
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws FunctionNotSupportedException   Function not supported
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server     */
    public void purgeCategory(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, EntityNotPurgedException, UnexpectedResponseException, FunctionNotSupportedException, InvalidParameterException, UserNotAuthorizedException {
        subjectAreaCategory.purgeCategory(userId,guid);
    }
    /**
     * Restore a Category
     *
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to restore
     * @return the restored category
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Category restoreCategory(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaCategory.restoreCategory(userId, guid);
    }
    /**
     * Get Category relationships
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Category guid
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public List<Line> getCategoryRelationships(String userId,
                                    String guid,
                                    Date asOfTime,
                                    int offset,
                                    int pageSize,
                                    SequencingOrder sequencingOrder,
                                    String sequencingProperty) throws
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException {
        return subjectAreaCategory.getCategoryRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);

    }

}
