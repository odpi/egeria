/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewTermRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * The GlossaryAuthorRESTServicesInstance provides the server implementation of the Glossary Author Open Metadata
 * View Service (OMVS) for terms.  This interface provides term authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/terms")

@Tag(name="View Server: Glossary Author OMVS", description="Glossary Author OMVS supports subject matter experts who are documenting their knowledge in a glossary.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omvs/glossary-author/overview/"))

public class GlossaryAuthorViewTermRESTResource {

    private GlossaryAuthorViewTermRESTServices restAPI = new GlossaryAuthorViewTermRESTServices();


    /**
     * Default constructor
     */
    public GlossaryAuthorViewTermRESTResource() {
    }


    /**
     * Create a Term.
     * <p>
     * Terms with the same name can be confusing. Best practise is to create terms that have unique names.
     * This Create call does not police that Term names are unique. So it is possible to create terms with the same name as each other.
     *
     * @param serverName   name of the local server.
     * @param userId       userid
     * @param suppliedTerm Term to create.
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse<Term> createTerm(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @RequestBody Term suppliedTerm) {
        return restAPI.createTerm(serverName, userId, suppliedTerm);

    }

    /**
     * Get a term. The server has a maximum page size defined, the number of categories (a field of Term) returned is limited by that maximum page size.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Term> getTerm(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String guid) {
        return restAPI.getTerm(serverName, userId, guid);

    }

    /**
     * Find Term
     *
     * @param serverName         local UI server name
     * @param userId             userid
     * @param searchCriteria     String expression matching Term property values.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param asOfTime           the terms returned as they were at this time. null indicates at the current time.
     * @param startingFrom          the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping()
    public SubjectAreaOMASAPIResponse<Term> findTerm(@PathVariable String serverName, @PathVariable String userId,
                                                     @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                     @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                     @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                     @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                     @RequestParam(value = "startingFrom", required = false) Integer startingFrom,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                     @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                     @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
    ) {
        return restAPI.findTerm(serverName, userId, asOfTime, searchCriteria, exactValue, ignoreCase, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Term relationships. The server has a maximum page size defined, the number of relationships returned is limited by that maximum page size.
     *
     * @param serverName         local UI server name
     * @param userId             userid
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom          the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the term relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/relationships")
    public SubjectAreaOMASAPIResponse<Relationship> getTermRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                                         @PathVariable String guid,
                                                                         @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                         @RequestParam(value = "startingFrom", required = false) Integer startingFrom,
                                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                         @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                                         @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty

                                                                        ) {
        return restAPI.getTermRelationships(serverName, userId, guid, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     * The Categories categorising this Term can be amended using this call. For an update (rather than a replace) with no categories supplied, no changes are made to the categories; otherwise the
     * supplied categories will replace the existing ones. The server has a maximum page size defined, the number of categories returned is limited by that maximum page size.
     *
     * @param serverName   local UI server name
     * @param userId       userid
     * @param guid         guid of the term to update
     * @param suppliedTerm term to update
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Term> updateTerm(
            @PathVariable String serverName,
            @PathVariable String userId,
            @PathVariable String guid,
            @RequestBody Term suppliedTerm,
            @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {

        return restAPI.updateTerm(serverName, userId, guid, suppliedTerm, isReplace);


    }

    /**
     * Delete a Term instance
     * <p>
     * The deletion of a term is only allowed if there is no term content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the term to be deleted.
     *
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Term> deleteTerm(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String guid
                                                      ) {
        return restAPI.deleteTerm(serverName, userId, guid);
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the term to restore
     * @return response which when successful contains the restored term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Term> restoreTerm(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String guid) {
        return restAPI.restoreTerm(serverName, userId, guid);

    }
    /**
     * Get the Categories categorizing this Term. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the category to get terms
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of categories categorizing this Term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "{guid}/categories")
    public SubjectAreaOMASAPIResponse<Category> getTermCategories(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String guid,
                                                                  @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        return restAPI.getCategories(serverName, userId, guid, startingFrom, pageSize);
    }
}
