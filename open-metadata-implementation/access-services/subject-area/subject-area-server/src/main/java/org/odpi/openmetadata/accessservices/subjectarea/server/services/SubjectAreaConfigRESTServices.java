/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaConfigHandler;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaTermHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * The SubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides term authoring interfaces for subject area experts.
 */

public class SubjectAreaConfigRESTServices extends SubjectAreaRESTServicesInstance {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaConfigRESTServices.class);
    private static final SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();
    private static final String className = SubjectAreaConfigRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaConfigRESTServices() {
        //SubjectAreaRESTServicesInstance registers this omas.
    }

    /**
     * get the configuration
     * @param serverName server name
     * @param userId userid of the caller
     * @return the configuration
     */
    public SubjectAreaOMASAPIResponse<Config> getConfig(String serverName, String userId) {
        final String methodName = "getConfig";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse<Config> response = new SubjectAreaOMASAPIResponse<>();
        try {
            SubjectAreaConfigHandler handler = instanceHandler.getSubjectAreaConfigHandler(userId, serverName, methodName);
            response = handler.getConfig(userId);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }
}