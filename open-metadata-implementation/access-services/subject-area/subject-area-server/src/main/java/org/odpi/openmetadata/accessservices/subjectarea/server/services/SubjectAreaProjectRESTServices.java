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
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.common.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GlossaryProject.GlossaryProject;

import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Project.Project;

import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaOmasREST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The SubjectAreaProjectRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS). This interface allow projects to be managed. There are some projects that are focussed on glossaries these care called Glossary projects.
 * The is OMAS allows the creation and deletion of glossary projects. Get and update operations can be performed on all projects.
 */
@RestController
@RequestMapping("access-services/subject-area")
public class SubjectAreaProjectRESTServices  extends SubjectAreaRESTServices{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaProjectRESTServices.class);

    private static final String className = SubjectAreaProjectRESTServices.class.getName();

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper) {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }

    /**
     * Default constructor
     */
    public SubjectAreaProjectRESTServices() {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Project
     * @param userId
     * @param suppliedProject
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = "users/{userId}/projects")
    public Project createProject(@PathVariable String userId, Project suppliedProject) throws Exception {
        final String methodName = "createProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        Project newProject =null;

        final String suppliedProjectName = suppliedProject.getName();
        if (suppliedProjectName == null) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_PROJECT_CREATE_WITHOUT_NAME;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        try {
            newProject = service.createProject(userId,suppliedProject);
        } catch (Exception e) {
            //TODO process the exceptions correctly
            e.printStackTrace();
        }
        // set the classification to say it is a glossary project
        String entityGuid = newProject.getSystemAttributes().getGUID();
        List<Classification> classifications = new ArrayList<>();
        classifications.add(new GlossaryProject());
        service.addProjectClassifications(userId,entityGuid,classifications);
        newProject = getProject(userId,entityGuid);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", created Node="+ newProject );
        }
        return newProject;
    }

    /**
     * Get a Project
     * @param userId
     * @param guid
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "users/{userId}/projects/{guid}")
    public Project getProject(@PathVariable String userId, String guid) throws Exception {
        final String methodName = "getProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid="+guid);
        }
        SubjectAreaOmasREST subjectAreaOmasREST = new SubjectAreaOmasREST() ;
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        Project gotProject = subjectAreaOmasREST.getProjectById(userId,guid);
        // TODO do we want to expose this as a object with inlined content e.g. icon. Wait for feedback
        // TODO do we want to only return a glossary project here?
        return gotProject;
    }

    /**
     * Update project by guid
     * @param userId
     * @param guid

     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/projects/{guid}")
    public Project updateProjectSummary(@PathVariable String userId,@PathVariable String guid,@PathVariable Project suppliedProject ) throws Exception {
        final String methodName = "updateProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();

        Project updatedProject=null;
        try {
            updatedProject =    service.updateProject(userId,guid,suppliedProject);
        } catch (Exception e) {
            //TODO process the exceptions correctly
            e.printStackTrace();
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Node="+ updatedProject );
        }
        return updatedProject;
    }

    /**
     * Delete glossary projects.
     * @param userId
     * @param guid
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "users/{userId}/projects/{guid}}")
    public void deleteProjectName(@PathVariable String userId, @PathVariable String guid, @RequestParam boolean isPurge) throws Exception {
        final String methodName = "deleteProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        Project project =service.getProjectById(userId,guid);
        List<Classification> classifications = project.getClassifications();
        boolean isGlossaryProject = false;
        for (Classification classification:classifications) {
            if (classification.getClassificationName().equals("GlossaryProject")) {
               isGlossaryProject=true;
            }
        }
        if (!isGlossaryProject) {
            // do not allow deletion of non-glossary projects.
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.INVALID_PROJECT_DELETION;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        if (isPurge) {
            service.purgeProjectByGuid(userId, guid);
        } else {
            service.deleteProjectByGuid(userId, guid);
            // TODO catch the exception saying that soft delete is not supported.
        }
    }
}
