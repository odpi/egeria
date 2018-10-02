/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;


/**
 *  The SubjectArea Open Metadata Access Service (OMAS) API for categories.
 */
public interface SubjectAreaCategory
{
        /**
         * Create a Category
         * @param userid unique identifier for requesting user, under which the request is performed
         * @param suppliedCategory category to create
         * @return the created category.
         *
         * Exceptions returned by the server
         * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
         * @throws InvalidParameterException  one of the parameters is null or invalid.
         * @throws UnrecognizedGUIDException  the supplied guid was not recognised
         * @throws ClassificationException Error processing a classification
         * @throws FunctionNotSupportedException   Function not supported
         *
         * Client library Exceptions
         * @throws MetadataServerUncontactableException Unable to contact the server
         * @throws UnexpectedResponseException an unexpected response was returned from the server
         */

        public Category createCategory(String userid, Category suppliedCategory) throws
                MetadataServerUncontactableException,
                InvalidParameterException,
                UserNotAuthorizedException,
                UnrecognizedGUIDException,
                ClassificationException,
                FunctionNotSupportedException,
                UnexpectedResponseException;

        /**
         * Get a category by guid.
         * @param userid unique identifier for requesting user, under which the request is performed
         * @param guid guid of the category to get
         * @return the requested category.
         *
         * Exceptions returned by the server
         * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
         * @throws InvalidParameterException one of the parameters is null or invalid.
         * @throws UnrecognizedGUIDException the supplied guid was not recognised
         * @throws FunctionNotSupportedException Function not supported
         *
         * Client library Exceptions
         * @throws MetadataServerUncontactableException Unable to contact the server
         * @throws UnexpectedResponseException an unexpected response was returned from the server
         */

        public  Category getCategoryByGuid( String userid, String guid) throws
                MetadataServerUncontactableException,
                UnrecognizedGUIDException,
                UserNotAuthorizedException,
                InvalidParameterException,
                FunctionNotSupportedException,
                UnexpectedResponseException ;

        /**
         * Replace a Category. This means to override all the existing attributes with the supplied attributes.
         * <p>
         * Status is not updated using this call.
         *
         * @param userid         unique identifier for requesting user, under which the request is performed
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
        public Category replaceCategory(String userid, String guid, Category suppliedCategory) throws
                UnexpectedResponseException,
                UserNotAuthorizedException,
                UnrecognizedNameException,
                FunctionNotSupportedException,
                InvalidParameterException,
                MetadataServerUncontactableException;
        /**
         * Update a Category. This means to update the category with any non-null attributes from the supplied category.
         * <p>
         * If the caller has chosen to incorporate the category name in their Category Categorys or Categories qualified name, renaming the category will cause those
         * qualified names to mismatch the Category name.
         * If the caller has chosen to incorporate the category qualifiedName in their Category Categorys or Categories qualified name, changing the qualified name of the category will cause those
         * qualified names to mismatch the Category name.
         * Status is not updated using this call.
         *
         * @param userid          unique identifier for requesting user, under which the request is performed
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
        public Category updateCategory(String userid, String guid, Category suppliedCategory) throws
                UnexpectedResponseException,
                UserNotAuthorizedException,
                UnrecognizedNameException,
                FunctionNotSupportedException,
                InvalidParameterException,
                MetadataServerUncontactableException;

        /**
         * Delete a Category instance
         *
         * A delete (also known as a soft delete) means that the category instance will exist in a deleted state in the repository after the delete operation. This means
         * that it is possible to undo the delete.
         *
         * @param userid unique identifier for requesting user, under which the request is performed
         * @param guid guid of the category to be deleted.
         * @return the deleted category
         * @throws UnrecognizedGUIDException the supplied guid was not recognised
         * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
         * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
         * @throws InvalidParameterException one of the parameters is null or invalid.
         * @throws EntityNotDeletedException a delete was issued but the category was not deleted.
         * @throws MetadataServerUncontactableException unable to contact server
         */

        public Category deleteCategory(String userid,String guid) throws InvalidParameterException,
                MetadataServerUncontactableException,
                UserNotAuthorizedException,
                UnrecognizedGUIDException,
                FunctionNotSupportedException,
                UnexpectedResponseException,
                EntityNotDeletedException;
        /**
         * Purge a Category instance
         *
         * A purge means that the category will not exist after the operation.
         *
         * @param userid unique identifier for requesting user, under which the request is performed
         * @param guid guid of the category to be deleted.
         *
         * @throws UnrecognizedGUIDException the supplied guid was not recognised
         * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
         * @throws InvalidParameterException one of the parameters is null or invalid.
         * @throws GUIDNotPurgedException a hard delete was issued but the category was not purged
         * @throws MetadataServerUncontactableException unable to contact server
         */

        public  void purgeCategory(String userid,String guid) throws InvalidParameterException,
                UserNotAuthorizedException,
                MetadataServerUncontactableException,
                UnrecognizedGUIDException,
                GUIDNotPurgedException,
                UnexpectedResponseException;

}
