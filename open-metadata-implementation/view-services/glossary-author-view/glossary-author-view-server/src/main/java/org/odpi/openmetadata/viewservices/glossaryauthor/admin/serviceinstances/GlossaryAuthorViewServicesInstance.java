/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.admin.serviceinstances;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationshipClients;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 *GlossaryAuthorViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GlossaryAuthorViewServicesInstance extends OMVSServiceInstance
{
    private final SubjectAreaNodeClients nodeClients;
    private final SubjectAreaRelationshipClients subjectAreaRelationshipClients;
    private final SubjectAreaConfigClient subjectAreaConfigClient;
    private final SubjectAreaGraphClient subjectAreaGraphClient;

    /**
     * Set up the Glossary Author OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public GlossaryAuthorViewServicesInstance(String       serverName,
                                              AuditLog     auditLog,
                                              String       localServerUserId,
                                              int          maxPageSize,
                                              String       remoteServerName,
                                              String       remoteServerURL) throws InvalidParameterException {
        super(serverName, ViewServiceDescription.GLOSSARY_AUTHOR.getViewServiceName(), auditLog, localServerUserId, maxPageSize, remoteServerName,
              remoteServerURL);
        final SubjectArea subjectArea = new SubjectAreaImpl(remoteServerName, remoteServerURL);
        this.subjectAreaRelationshipClients = subjectArea.relationshipClients();
        this.nodeClients = subjectArea.nodeClients();
        this.subjectAreaConfigClient = subjectArea.subjectAreaConfigClient();
        this.subjectAreaGraphClient = subjectArea.subjectAreaGraphClient();
    }

    public String getViewServiceName()
    {
        return ViewServiceDescription.GLOSSARY_AUTHOR.getViewServiceName();
    }

    public SubjectAreaNodeClients getNodeClients() { return nodeClients; }

    public SubjectAreaRelationshipClients getSubjectAreaRelationshipClients() {
        return subjectAreaRelationshipClients;
    }

    public SubjectAreaConfigClient getSubjectAreaConfigClient() {
        return subjectAreaConfigClient;
    }
    public SubjectAreaGraphClient getSubjectAreaGraphClient() {
        return subjectAreaGraphClient;
    }
    public int getGlossaryViewMaxPageSize() {
        return maxPageSize;
    }
}
