/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.admin.serviceinstances;

import org.odpi.openmetadata.accessservices.subjectarea.*;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;


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
     * Set up the subject Area OMVS service instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public SubjectAreaViewServicesInstance(String serverName,
                                           OMRSAuditLog auditLog,
                                           String       localServerUserId,
                                           int          maxPageSize,
                                           String       remoteServerName,
                                           String       remoteServerURL) throws InvalidParameterException {
        super(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),auditLog, localServerUserId, maxPageSize, remoteServerName,
                remoteServerURL);
        subjectArea = new SubjectAreaImpl(remoteServerName,remoteServerURL);
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
