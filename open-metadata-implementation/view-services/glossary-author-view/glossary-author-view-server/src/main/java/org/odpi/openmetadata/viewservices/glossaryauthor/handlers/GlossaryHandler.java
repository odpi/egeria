/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The glossary handler is initialised with a SubjectAreaGlossary, that contains the server the call should be sent to.
 * The handler exposes methods for glossary functionality for the glossary author view
 */
public class GlossaryHandler {
    private SubjectAreaGlossary subjectAreaGlossary;

    /**
     * initialize the glossary handler with the subjectarea glossary object.
     *
     * @param subjectAreaGlossary The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for glossaries. This is the same as the
     *                            The SubjectAreaDefinition Open Metadata View Service (OMVS) API for glossaries.
     */
    public GlossaryHandler(SubjectAreaGlossary subjectAreaGlossary) {
        this.subjectAreaGlossary = subjectAreaGlossary;
    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Glossaries with the same name can be confusing. Best practise is to createGlossaries that have unique names.
     * This Create call does not police that glossary names are unique. So it is possible to create Glossaries with the same name as each other.
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return the created glossary.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public Glossary createGlossary(String userId, Glossary suppliedGlossary) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaGlossary.glossary().create(userId, suppliedGlossary);
    }

    /**
     * Get a glossary by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to get
     * @return the requested glossary.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public Glossary getGlossaryByGuid(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaGlossary.glossary().getByGUID(userId, guid);
    }

    /**
     * Get Glossary relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the glossary to get
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Glossary guid
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public List<Line> getGlossaryRelationships(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaGlossary.glossary().getRelationships(userId, guid, findRequest);
    }

    /**
     * Find Glossary
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Glossaries meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public List<Glossary> findGlossary(String userId, FindRequest findRequest) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaGlossary.glossary().find(userId, findRequest);
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
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public Glossary replaceGlossary(String userId, String guid, Glossary suppliedGlossary) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaGlossary.glossary().replace(userId, guid, suppliedGlossary);
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
     * @throws PropertyServerException              Property Server Exception
     */
    public Glossary updateGlossary(String userId, String guid, Glossary suppliedGlossary) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaGlossary.glossary().update(userId, guid, suppliedGlossary);
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * A delete (also known as a soft delete) means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public void deleteGlossary(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaGlossary.glossary().delete(userId, guid);
    }

    /**
     * Purge a Glossary instance
     * <p>
     * The purge of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * A purge means that the glossary will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public void purgeGlossary(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaGlossary.glossary().purge(userId, guid);
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the glossary to restore
     * @return the restored glossary
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public Glossary restoreGlossary(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaGlossary.glossary().restore(userId, guid);
    }
}