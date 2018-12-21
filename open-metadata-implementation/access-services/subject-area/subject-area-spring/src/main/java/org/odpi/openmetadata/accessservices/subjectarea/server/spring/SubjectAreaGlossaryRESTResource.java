/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
public class SubjectAreaGlossaryRESTResource extends SubjectAreaRESTServicesInstance
{
    private SubjectAreaGlossaryRESTServices restAPI = new SubjectAreaGlossaryRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaGlossaryRESTResource() {

    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>Taxonomy to create a Taxonomy </li>
     *     <li>CanonicalGlossary to create a canonical glossary </li>
     *     <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     *     <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  UnrecognizedGUIDException            the supplied guid was not recognised
     *  ClassificationException              Error processing a classification
     *  StatusNotSupportedException          A status value is not supported
     */
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/glossaries")
    public SubjectAreaOMASAPIResponse createGlossary(@PathVariable String serverName,@PathVariable String userId, @RequestBody Glossary suppliedGlossary) {
        return restAPI.createGlossary(serverName, userId,suppliedGlossary);
    }

    /**
     * Get a glossary.
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId userId under which the request is performed
     * @param id id of the glossary to get, this can be a name or a guid depending on the
     * @param idIsName When set this indicates that the id is a name
     * @return response which when successful contains the glossary with the requested guid
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/glossaries/{id}")
    public  SubjectAreaOMASAPIResponse getGlossary(@PathVariable String serverName,@PathVariable String userId, @PathVariable String id,@RequestParam(value = "idIsName", required=false) Boolean idIsName) {
        if (idIsName == null || !idIsName) {
            return restAPI.getGlossaryByGuid(serverName, userId,id);
        } else {
            return restAPI.getGlossaryByName(serverName, userId,id);
        }
    }
    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the glossary to update
     * @param glossary         glossary to update
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse updateGlossary(@PathVariable String serverName,@PathVariable String userId,@PathVariable String guid,@RequestBody Glossary glossary,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateGlossary(serverName, userId,guid,glossary,isReplace);
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse deleteGlossary(@PathVariable String serverName,@PathVariable String userId,@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteGlossary(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Glossary
     *
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/glossaries/{guid}")
    public SubjectAreaOMASAPIResponse restoreGlossary(@PathVariable String serverName,@PathVariable String userId,@PathVariable String guid)
    {
        return restAPI.restoreGlossary(serverName, userId,guid);
    }
}
