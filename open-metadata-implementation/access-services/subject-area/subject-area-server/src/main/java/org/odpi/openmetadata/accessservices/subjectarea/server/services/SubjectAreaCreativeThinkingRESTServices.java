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


import org.odpi.openmetadata.accessservices.subjectarea.common.OMRSAPIHelper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 *
 * These services are useful when wanting to quickly put down words that could be useful as terms. These words /terms will be known as BriefTerms. This is a minimal API allowing words (an option description) to be added to a glossary.
 * There are also APIs to get update and delete these BriefTerms.
 */
@RestController
@RequestMapping("access-services/subject-area/creative-thinking")
public class SubjectAreaCreativeThinkingRESTServices {
    static private String accessServiceName = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaCreativeThinkingRESTServices.class);

    private static final String className = SubjectAreaCreativeThinkingRESTServices.class.getName();

    // The OMRSAPIGenHelper allows the junits to mock out the omrs layer.
    private OMRSAPIHelper oMRSAPIHelper = new OMRSAPIHelper();

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper) {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }


    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName   - name of this access service
     * @param repositoryConnector - OMRS Repository Connector to the property org.odpi.openmetadata.accessservices.subjectarea.server.
     */
    static public void setRepositoryConnector(String accessServiceName,
                                              OMRSRepositoryConnector repositoryConnector) {
        SubjectAreaCreativeThinkingRESTServices.accessServiceName = accessServiceName;
        SubjectAreaCreativeThinkingRESTServices.repositoryConnector = repositoryConnector;
    }

    /**
     * Default constructor
     */
    public SubjectAreaCreativeThinkingRESTServices() {
        //SubjectAreaRESTServices registers this omas.
    }


    //TODO this might a be a nice to have - check with stake holders.
}
