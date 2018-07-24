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
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
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
     * Get a glossary by guid.
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     *      when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws FunctionNotSupportedException   Function not supported
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/glossaries/{guid}")
    public  SubjectAreaOMASAPIResponse getGlossaryByGuid(@PathVariable String userId, @PathVariable String guid) {
        return restAPI.getGlossaryByGuid(userId,guid);
    }
    /**
     *  Update a Glossary's name.
     *
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to update
     * @param name name to be updated
     * @return a response which when successful contains the updated glossary
     *      when not successful the following Exceptions can occur
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}/name/{name}")
    public  SubjectAreaOMASAPIResponse updateGlossaryName(String userId,String guid,@PathVariable String name) {
        return restAPI.updateGlossaryName(userId,guid,name);
    }
    /**
     *  Update a Glossary's description.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to update
     * @param description new description
     * @return a response which when successful contains the updated glossary
     *       when not successful the following Exceptions can occur
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}/description/{description}")
    public  SubjectAreaOMASAPIResponse updateGlossaryDescription(@PathVariable String userId,@PathVariable String guid,@PathVariable String description) {
        return restAPI.updateGlossaryDescription(userId,guid,description);
    }
    /**
     *  Update a Glossary's qualified name.
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to update
     * @param qualifiedName new qualifiedName
     * @return a response which when successful contains the updated glossary
     *      when not successful the following Exceptions can occur
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}/qualifiedName/{qualifiedName}")
    public  SubjectAreaOMASAPIResponse updateGlossaryQualifiedName(@PathVariable String userId,@PathVariable String guid,@PathVariable String qualifiedName) {
        return restAPI.updateGlossaryQualifiedName(userId,guid,qualifiedName);
    }

    /**
     *  Update a Glossary's usage
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to update
     * @param usage new usage
     * @return a response which when successful contains the updated glossary
     *      when not successful the following Exceptions can occur
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}/usage/{usage}")
    public  SubjectAreaOMASAPIResponse updateGlossaryUsage(@PathVariable String userId,@PathVariable String guid,@PathVariable String usage) {
        return restAPI.updateGlossaryUsage(userId,guid,usage);
    }

    /**
     *  Update a Glossary's language
     *
     * @param userId userId under which the request is performed
     * @param guid guid of the glossary to update
     * @param language new language for this glossary
     * @return a response which when successful contains the updated glossary
     *      when not successful the following Exceptions can occur
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException   Function not supported
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/glossaries/{guid}/language/{language}")
    public SubjectAreaOMASAPIResponse updateGlossaryLanguage(@PathVariable String userId,@PathVariable String guid,@PathVariable String language) {
        return restAPI.updateGlossaryLanguage(userId,guid,language);
    }

    /**
     * Get a Glossary by name
     *
     * Glossaries should have unique names. If repositories were not able to contact each other on the network, it is possible that glossaries of the same
     * name might be added. If this has occured this operation may not retun the glossary you are interested in. The guid of the glossary is the way to
     * uniquely identify a glossary; a get for glossary by guid can be issued to find glossaries with particular guids.
     *
     * @param userId userId under which the request is performed
     * @param name find the glossary with this name.
     * @return the requested glossary.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException  not able to communicate with a Metadata respository service.
     * @throws FunctionNotSupportedException   Function not supported
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/glossaries")
    public  SubjectAreaOMASAPIResponse getGlossaryByName(@PathVariable String userId,@RequestParam String name) {
        return restAPI.getGlossaryByName(userId,name);
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
     * @throws PropertyServerException  not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
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



