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
public class SubjectAreaOMASGlossaryResource extends SubjectAreaRESTServices{
    private SubjectAreaGlossaryRESTServices restAPI = new SubjectAreaGlossaryRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaOMASGlossaryResource() {

    }

    /**
     * Create a Glossary
     * @param userId  userId under which the request is performed
     * @param suppliedGlossary
     * @return response, when successful contains the created glossary.
     *  when not successful the following Exception responses can occur
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException  the supplied guid was not recognised
     * @throws ClassificationException Error processing a classification 
     * @throws FunctionNotSupportedException   Function not supported
     * @throws StatusNotSupportedException A status value is not supported
     */
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/glossaries")
    public SubjectAreaOMASAPIResponse createGlossary(@PathVariable String userId, @RequestBody Glossary suppliedGlossary) {
        return restAPI.createGlossary(userId,suppliedGlossary);
    }

    /**
     * Get a glossary.
     * @param userId userId under which the request is performed
     * @param id id of the glossary to get, this can be a name or a guid depending on the
     * @param idIsName When set this indicates that the id is a name
     * @return response which when successful contains the glossary with the requested guid
     *      when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     *  * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws FunctionNotSupportedException   Function not supported
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/glossaries/{id}")
    public  SubjectAreaOMASAPIResponse getGlossary(@PathVariable String userId, @PathVariable String id,@RequestParam(value = "idIsName", required=false) Boolean idIsName) {
        if (idIsName == null || !idIsName) {
            return restAPI.getGlossaryByGuid(userId,id);
        } else {
            return restAPI.getGlossaryByName(userId,id);
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
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to update
     * @param glossary glossary to be updated
     * @Param isReplace when set the Glossary should be replaced with the supplied content. When not set, the Glossary should be updated with only the content that has been supplied.
     * @return a response which when successful contains the updated glossary
     *      when not successful the following Exceptions can occur
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse updateGlossary(@PathVariable String userId,@PathVariable String guid,Glossary glossary,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateGlossary(userId,guid,glossary,isReplace);
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
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     * @throws EntityNotDeletedException a soft delete was issued but the glossary was not deleted.
     * @throws GUIDNotPurgedException a hard delete was issued but the glossary was not purged
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse deleteGlossary(@PathVariable String userId,@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteGlossary(userId,guid,isPurge);
    }
}



