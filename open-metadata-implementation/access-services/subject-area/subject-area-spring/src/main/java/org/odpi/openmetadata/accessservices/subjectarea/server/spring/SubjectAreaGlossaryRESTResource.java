/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/open-metadata/access-services/subject-area")
public class SubjectAreaGlossaryRESTResource extends SubjectAreaRESTServices{
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
     * @param userid unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException  the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException  one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException  the supplied guid was not recognised</li>
     * <li> ClassificationException Error processing a classification </li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * <li> StatusNotSupportedException A status value is not supported
     * </ul>
     */
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userid}/glossaries")
    public SubjectAreaOMASAPIResponse createGlossary(@PathVariable String userid, @RequestBody Glossary suppliedGlossary) {
        return restAPI.createGlossary(userid,suppliedGlossary);
    }

    /**
     * Get a glossary.
     * @param userid userid under which the request is performed
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userid}/glossaries/{id}")
    public  SubjectAreaOMASAPIResponse getGlossary(@PathVariable String userid, @PathVariable String id,@RequestParam(value = "idIsName", required=false) Boolean idIsName) {
        if (idIsName == null || !idIsName) {
            return restAPI.getGlossaryByGuid(userid,id);
        } else {
            return restAPI.getGlossaryByName(userid,id);
        }
    }
    /**
     *  Update a Glossary
     *
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userid userid under which the request is performed
     * @param guid guid of the glossary to update
     * @param glossary glossary to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.     * @return a response which when successful contains the updated glossary
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException function not supported</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userid}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse updateGlossary(@PathVariable String userid,@PathVariable String guid,Glossary glossary,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateGlossary(userid,guid,glossary,isReplace);
    }

    /**
     * Delete a Glossary instance
     *
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     *
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support 
     * is optional. Soft delete is the default.
     *
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     *    when not successful the following Exceptions can occur
     * @param userid userid under which the request is performed
     * @param guid guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException a soft delete was issued but the glossary was not deleted.</li>
     * <li> GUIDNotPurgedException a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userid}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse deleteGlossary(@PathVariable String userid,@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteGlossary(userid,guid,isPurge);
    }
}
