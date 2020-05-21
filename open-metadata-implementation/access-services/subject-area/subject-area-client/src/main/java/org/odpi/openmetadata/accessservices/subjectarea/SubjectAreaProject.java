/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;


import java.util.Date;
import java.util.List;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for projects.
 */
public interface SubjectAreaProject
{
    /**
     * Create a Project.
     *
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     *
     * Projects that are created using this call will be GlossaryProjects.
     * <p>
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedProject Project to create
     * @return the created Project.
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

     Project createProject(String userId, Project suppliedProject) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException;
    /**
     * Get a Project by guid.
     * @param userId userId under which the request is performed
     * @param guid guid of the Project to get
     * @return the requested Project.
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

      Project getProjectByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException ;
    /**
     * Get Project relationships
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Project guid
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
     List<Line> getProjectRelationships(String userId,
                                        String guid,
                                        Date asOfTime,
                                        int offset,
                                        int pageSize,
                                        org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                        String sequencingProperty) throws
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException,
            MetadataServerUncontactableException;
    /**
     * Get the terms in this project.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @return the terms that are in the requested Project
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

    List<Term> getProjectTerms(
                                      String userId,
                                      String guid,
                                      Date asOfTime

    ) throws InvalidParameterException, UserNotAuthorizedException, FunctionNotSupportedException, UnexpectedResponseException, MetadataServerUncontactableException;
    /**
     * Find Project
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Project properties. If not specified then all projects are returned.
     * @param asOfTime Projects returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Projects meeting the search Criteria
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
    List<Project> findProject(String userId,
                                String searchCriteria,
                                Date asOfTime,
                                int offset,
                                int pageSize,
                                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                String sequencingProperty) throws
            MetadataServerUncontactableException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnexpectedResponseException;
    /**
     * Replace a Project. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * If the caller has chosen to incorporate the Project name in their Project Terms or Categories qualified name, renaming the Project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the Project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the Project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the Project to update
     * @param suppliedProject Project to be updated
     * @return replaced Project
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException         Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
     Project replaceProject(String userId, String guid, Project suppliedProject) throws
                                                                                                              UnexpectedResponseException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              InvalidParameterException,
                                                                                                              FunctionNotSupportedException,
                                                                                                              MetadataServerUncontactableException ;
    /**
     * Update a Project. This means to update the Project with any non-null attributes from the supplied Project.
     * <p>
     * If the caller has chosen to incorporate the Project name in their Project Terms or Categories qualified name, renaming the Project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the Project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the Project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the Project to update
     * @param suppliedProject Project to be updated
     * @return a response which when successful contains the updated Project
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
     Project updateProject(String userId, String guid, Project suppliedProject) throws UnexpectedResponseException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    InvalidParameterException,
                                                                                                                    FunctionNotSupportedException,
                                                                                                                    MetadataServerUncontactableException ;

    /**
     * Delete a Project instance
     *
     * The deletion of a Project is only allowed if there is no Project content
     *
     * A delete (also known as a soft delete) means that the Project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the Project to be deleted.
     * @return the deleted Project
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws EntityNotDeletedException a delete was issued but the Project was not deleted.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

     Project deleteProject(String userId, String guid) throws InvalidParameterException,
                                                                                        MetadataServerUncontactableException,
                                                                                        UserNotAuthorizedException,
                                                                                        UnrecognizedGUIDException,
                                                                                        FunctionNotSupportedException,
                                                                                        UnexpectedResponseException,
                                                                                        EntityNotDeletedException ;
    /**
     * Purge a Project instance
     *
     * The purge of a Project is only allowed if there is no Project content.
     *
     * A purge means that the Project will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the Project to be deleted.
     *
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws EntityNotPurgedException a hard delete was issued but the Project was not purged
     * @throws FunctionNotSupportedException Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

      void purgeProject(String userId, String guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UnrecognizedGUIDException,
                                                                                    EntityNotPurgedException,
                                                                                    UnexpectedResponseException,
                                                                                    FunctionNotSupportedException ;
    /**
     * Restore a Project
     *
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Project to restore
     * @return the restored Project
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
      Project restoreProject(String userId, String guid) throws InvalidParameterException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException,
            UnrecognizedGUIDException,
            FunctionNotSupportedException,
            UnexpectedResponseException;
}
