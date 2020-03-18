/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The glossary handler is initialised with a SubjectAreaGlossary, that contains the server the call should be sent to.
 * The handler exposes methods for glossary functionality for the glossary author view
 */
public class GlossaryHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlossaryHandler.class);

    private SubjectAreaGlossary subjectAreaGlossary;

    /**
     * initialize the glossary handler with the subjectarea glossary object.
     * @param subjectAreaGlossary The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for glossaries. This is the same as the
     *                            The SubjectAreaDefinition Open Metadata View Service (OMVS) API for glossaries.
     */
    public GlossaryHandler(SubjectAreaGlossary subjectAreaGlossary) {
      this.subjectAreaGlossary =subjectAreaGlossary;
    }
    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     *
     * Glossaries with the same name can be confusing. Best practise is to createGlossaries that have unique names.
     * This Create call does not police that glossary names are unique. So it is possible to create Glossaries with the same name as each other.
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return the created glossary.
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
    public Glossary createGlossary(String userId, Glossary suppliedGlossary) throws MetadataServerUncontactableException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.createGlossary(userId, suppliedGlossary);
    }

    /**
     * Get a glossary by guid.
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to get
     * @return the requested glossary.
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
    public Glossary getGlossaryByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.getGlossaryByGuid(userId, guid);
    }
    /**
     * Get Glossary relationships
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the glossary to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Glossary guid
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
    public List<Line> getGlossaryRelationships(String userId, String guid, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws FunctionNotSupportedException, UnexpectedResponseException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.getGlossaryRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }
    /**
     * Find Glossary
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching glossary properties. If not specified then all glossaries are returned.
     * @param asOfTime Glossaries returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Glossaries meeting the search Criteria
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
    public List<Glossary> findGlossary(String userId, String searchCriteria, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.findGlossary(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }
    /**
     * Replace a Glossary. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return replaced glossary
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException         Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Glossary replaceGlossary(String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.replaceGlossary(userId, guid, suppliedGlossary);
    }
    /**
     * Update a Glossary. This means to update the glossary with any non-null attributes from the supplied glossary.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Glossary updateGlossary(String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.updateGlossary(userId, guid, suppliedGlossary);
    }
    /**
     * Delete a Glossary instance
     *
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     *
     * A delete (also known as a soft delete) means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to be deleted.
     * @return the deleted glossary
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws EntityNotDeletedException a delete was issued but the glossary was not deleted.
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server     */
    public Glossary deleteGlossary(String userId, String guid) throws  MetadataServerUncontactableException,UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, EntityNotDeletedException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.deleteGlossary(userId,guid);
    }
    /**
     * Purge a Glossary instance
     *
     * The purge of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     *
     * A purge means that the glossary will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to be deleted.
     *
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws GUIDNotPurgedException a hard delete was issued but the glossary was not purged
     * @throws FunctionNotSupportedException Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public void purgeGlossary(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, GUIDNotPurgedException, UnexpectedResponseException, FunctionNotSupportedException, InvalidParameterException, UserNotAuthorizedException {
        subjectAreaGlossary.purgeGlossary(userId,guid);
    }
    /**
     * Restore a Glossary
     *
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to restore
     * @return the restored glossary
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */
    public Glossary restoreGlossary(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, UserNotAuthorizedException {
        return subjectAreaGlossary.restoreGlossary(userId, guid);
    }

}
