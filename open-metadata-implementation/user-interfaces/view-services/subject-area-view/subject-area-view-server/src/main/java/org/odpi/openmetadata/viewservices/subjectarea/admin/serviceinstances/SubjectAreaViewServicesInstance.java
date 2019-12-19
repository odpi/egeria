/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.admin.serviceinstances;

import org.odpi.openmetadata.accessservices.subjectarea.*;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;


/**
 * SubjectAreaViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaViewServicesInstance extends OMVSServiceInstance
{
    private SubjectArea subjectArea;
    private SubjectAreaGlossary subjectAreaGlossary;
    private SubjectAreaProject subjectAreaProject;
    private SubjectAreaTerm subjectAreaTerm;
    private SubjectAreaCategory subjectAreaCategory;
    private SubjectAreaRelationship subjectAreaRelationship;
    private SubjectAreaGraph subjectAreaGraph;

    /**
     * Set up the objects for the subject area view service
     * @param serverName name of the server
     * @param auditLog audit log
     * @param localServerUserId local server userId
     * @param maxPageSize maximum page size, for use with paging requests
     * @param metadataServerName  metadata server name
     * @param metadataServerURL metadata server URL
     */
    public SubjectAreaViewServicesInstance(String serverName,
                                           OMRSAuditLog auditLog,
                                           String       localServerUserId,
                                           int          maxPageSize,
                                           String       metadataServerName,
                                           String       metadataServerURL) throws InvalidParameterException {
        super(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),auditLog, localServerUserId, maxPageSize, metadataServerName,
                metadataServerURL);
        subjectArea = new SubjectAreaImpl(metadataServerName,metadataServerURL);
        subjectAreaGlossary = subjectArea.getSubjectAreaGlossary();
        subjectAreaProject = subjectArea.getSubjectAreaProject();
        subjectAreaCategory = subjectArea.getSubjectAreaCategory();
        subjectAreaTerm = subjectArea.getSubjectAreaTerm();
        subjectAreaGraph = subjectArea.getSubjectAreaGraph();
        subjectAreaRelationship = subjectArea.getSubjectAreaRelationship();
    }

    public String getViewServiceName()
    {
        return ViewServiceDescription.SUBJECT_AREA.getViewServiceName();
    }

    public SubjectAreaGlossary getSubjectAreaGlossary() {
        return subjectAreaGlossary;
    }

    public SubjectAreaProject getSubjectAreaProject() {
        return subjectAreaProject;
    }

    public SubjectAreaTerm getSubjectAreaTerm() {
        return subjectAreaTerm;
    }

    public SubjectAreaCategory getSubjectAreaCategory() {
        return subjectAreaCategory;
    }

    public SubjectAreaRelationship getSubjectAreaRelationship() {
        return subjectAreaRelationship;
    }

    public SubjectAreaGraph getSubjectAreaGraph() {
        return subjectAreaGraph;
    }
}
