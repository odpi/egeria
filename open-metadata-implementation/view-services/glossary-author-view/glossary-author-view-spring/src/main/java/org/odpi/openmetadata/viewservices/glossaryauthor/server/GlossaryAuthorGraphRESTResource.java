/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.GraphStatistics;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewGraphRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides term authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}")
@Tag(name="Subject Area OMAS", description="The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs=@ExternalDocumentation(description="Subject Area Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))
public class GlossaryAuthorGraphRESTResource {
    private final GlossaryAuthorViewGraphRESTServices restAPI = new GlossaryAuthorViewGraphRESTServices();
    /**
     * Default constructor
     */
    public GlossaryAuthorGraphRESTResource() {

    }

    /**
     * Get the graph of nodes and relationships radiating out from a node.
     *
     * Return the nodes and relationships that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of relationships, types of nodes and classifications as well as level.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipFilterStr comma separated list of relationship names to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @return A graph of nodeTypes.
     *
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    @GetMapping( path = "/graph/{guid}")
    public SubjectAreaOMASAPIResponse<Graph> getGraph(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String guid,
                                                      @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                      @RequestParam(value = "nodeFilter", required=false)String nodeFilterStr,
                                                      @RequestParam(value = "relationshipFilter", required=false)String relationshipFilterStr,
                                                      @RequestParam(value = "statusFilter", required=false)StatusFilter statusFilter   // may need to extend this for controlled terms
                                                     ) {

        return restAPI.getGraph(serverName, userId, guid, asOfTime,  nodeFilterStr, relationshipFilterStr, statusFilter);
    }
    /**
     * Get the graph statistics of nodes and relationships radiating out from a node.
     *
     * Return the nodes and relationships statistics that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of relationships, types of nodes and classifications as well as level.
     *
     * The graph statistics include counts that the user can use to scope the query, to reduce the number of nodes displayed.
     * This is combat the issue that a large number of nodes being displayed would make the screen too busy and unusable.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipFilterStr comma separated list of relationship names to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @return A graph of nodeTypes.
     *
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    @GetMapping( path = "/graph-counts/{guid}")
    public SubjectAreaOMASAPIResponse<GraphStatistics> getGraphCounts(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                                      @RequestParam(value = "nodeFilter", required=false)String nodeFilterStr,
                                                                      @RequestParam(value = "relationshipFilter", required=false)String relationshipFilterStr,
                                                                      @RequestParam(value = "statusFilter", required=false)StatusFilter statusFilter   // may need to extend this for controlled terms
                                                                     ) {

        return restAPI.getGraphCounts(serverName, userId, guid, asOfTime, nodeFilterStr, relationshipFilterStr, statusFilter);
    }
}
