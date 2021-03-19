/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.BreadCrumb;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewGlossaryRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * The GlossaryAuthorRESTServicesInstance provides the org.odpi.openmetadata.viewservices.glossaryauthor.server  implementation of the Glossary Author Open Metadata
 * View Service (OMVS) for glossaries.  This interface provides glossary authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/glossaries")

@Tag(name="Glossary Author OMVS", description="Develop a definition of a subject area by authoring glossaries, including terms for use by a user interface.", externalDocs=@ExternalDocumentation(description="Glossary Author View Services (OMVS)",url="https://egeria.odpi.org/open-metadata-implementation/view-services/glossary-author-view/"))

public class GlossaryAuthorViewGlossaryRESTResource {

    private final GlossaryAuthorViewGlossaryRESTServices restAPI = new GlossaryAuthorViewGlossaryRESTServices();


    /**
     * Default constructor
     */
    public GlossaryAuthorViewGlossaryRESTResource() {
    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalGlossary to create a canonical glossary </li>
     * <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     * <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     *
     * @param serverName       name of the local server.
     * @param userId           userid
     * @param suppliedGlossary Glossary to create.
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse<Glossary> createGlossary(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @RequestBody Glossary suppliedGlossary) {
        return restAPI.createGlossary(serverName, userId, suppliedGlossary);

    }

    /**
     * Get a glossary.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata repository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> getGlossary(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String guid) {
        return restAPI.getGlossary(serverName, userId, guid);

    }

    /**
     * Find Glossary
     *
     * @param serverName         local UI server name
     * @param userId             userid
     * @param searchCriteria     String expression matching Glossary property values .
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param startingFrom       the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    @GetMapping()
    public SubjectAreaOMASAPIResponse<Glossary> findGlossary(@PathVariable String serverName, @PathVariable String userId,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") int startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
    ) {
        return restAPI.findGlossary(serverName, userId, asOfTime, searchCriteria, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Glossary relationships. The server has a maximum page size defined, the number of relationships returned is limited by that maximum page size.
     *
     * @param serverName         local UI server name
     * @param userId             userid
     * @param guid               guid of the glossary to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the glossary relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/relationships")
    public SubjectAreaOMASAPIResponse<Relationship> getGlossaryRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                                             @PathVariable String guid,
                                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") int startingFrom,
                                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                             @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                                            ) {
        return restAPI.getGlossaryRelationships(serverName, userId, guid, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get terms that are owned by this glossary. The server has a maximum page size defined, the number of terms returned is limited by that maximum page size.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param asOfTime   the terms returned as they were at this time. null indicates at the current time.
     * @param searchCriteria String expression matching child Term property values.
     * @param startingFrom the starting element number for this set of results. This is used when retrieving elements
     * @param pageSize Return the maximum number of elements that can be returned on this request.
     * @param sequencingOrder sequencing order
     * @param sequencingProperty property used for sequencing.
     * @return A list of terms owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    @GetMapping(path = "/{guid}/terms")
    public SubjectAreaOMASAPIResponse<Term> getGlossaryTerms(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String guid,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty) {
        return restAPI.getTerms(serverName, userId, guid, searchCriteria,asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }


    /**
     * Get the Categories owned by this glossary. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the glossary to get terms
     * @param searchCriteria String expression matching child Category property values.
     * @param asOfTime     the categories returned as they were at this time. null indicates at the current time.
     * @param onlyTop      when only the top categories (those categories without parents) are returned.
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     *
     * @return A list of categories owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    @GetMapping(path = "/{guid}/categories")
    public SubjectAreaOMASAPIResponse<Category> getGlossaryCategories(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                                      @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                      @RequestParam(value = "onlyTop", required = false, defaultValue = "true") Boolean onlyTop,
                                                                      @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                      @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                                      @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                                     ) {
        return restAPI.getCategories(serverName, userId, guid, searchCriteria, asOfTime, onlyTop, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Create the supplied list of Terms in the glossary, identified by the supplied guid. Each term does not need to specify a glossary.
     *
     * @param serverName       local UI server name
     * @param userId           user identifier
     * @param guid             guid of the glossary under which the Terms will be created
     * @param terms            terms to create
     * @return a response which when successful contains a list of the responses from the Term creates (successful or otherwise). The order of the responses is the same as the supplied terms order.
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * </ul>
     */
    @PostMapping(path = "/{guid}/terms")
    public SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>> createMultipleTermsInAGlossary(@PathVariable String serverName,
                                                                                                   @PathVariable String userId,
                                                                                                   @PathVariable String guid,
                                                                                                   @RequestBody  Term[] terms
                                                                                                  ) {
        return restAPI.createMultipleTermsInAGlossary(serverName, userId, guid, terms);
    }
    /**
     * Get BreadCrumbTrail.
     *
     * The user interface experience can start with a particular Glossary, navigate to a child Category, then to another child category then to a categories term.
     * When such a user interface navigation occurs, it is helpful for the user to be displayed a 'breadcrumb' trail of where they have been, showing how nested they are in
     * the glossary artifacts. The Get BreadcrumbTrail API returns information that allows the user interface to easy construct a trail of breadcrumbs.
     * Each breadcrumb contains
     * <ul>
     *  <li> a displayName that is intended to be shown to the user</li>
     *  <li> a guid that uniquely identifies a breadcrumb but is not intended to be shown to the user</li>
     *  <li> a types, allowing the user interface to display an appropriate icon.
     *  <li> a path that can be used to determine all the elements in the breadcrumb </li>
     * </ul>
     * @param serverName       local UI server name
     * @param userId           user identifier
     * @param guid             Glossary guid.
     * @param rootCategoryGuid root Category guid. If specified, the Category with this guid must reside in the supplied glossary
     * @param leafCategoryGuid leaf Category guid. if specified, the Category with this guid must reside in the hierarchy under the root Category
     * @param termGuid         Term guid. If specified, the Term with this guid must be categorised by the rootCategory or the leafCategory if there is one, or if there are no categories owned by the Glossary.
     * @return a response which when successful contains a list of breadcrumbs corresponding to the supplied guids.
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/breadcrumbs")
    public SubjectAreaOMASAPIResponse<BreadCrumb> getBreadCrumbTrail(
            @PathVariable String serverName,
            @PathVariable String userId,
            @PathVariable String guid,
            @RequestParam(value = "rootCategoryGuid", required = false) String rootCategoryGuid,
            @RequestParam(value = "leafCategoryGuid", required = false) String leafCategoryGuid,
            @RequestParam(value = "termGuid",         required = false) String termGuid) {
        return restAPI.getBreadCrumbTrail(
                serverName,
                userId,
                guid,
                rootCategoryGuid,
                leafCategoryGuid,
                termGuid);
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
     * @param serverName       local UI server name
     * @param userId           userid
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to update
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * </ul>
     */
    @PutMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> updateGlossary(
            @PathVariable String serverName, @PathVariable String userId,
            @PathVariable String guid,
            @RequestBody Glossary suppliedGlossary,
            @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {

        return restAPI.updateGlossary(serverName, userId, guid, suppliedGlossary, isReplace);


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
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the glossary to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> EntityNotPurgedException             a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> deleteGlossary(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid,
                                                               @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
                                                     ) {
        return restAPI.deleteGlossary(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> restoreGlossary(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable String guid) {
        return restAPI.restoreGlossary(serverName, userId, guid);

    }
}
