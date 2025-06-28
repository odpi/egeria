/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.SubjectAreaHandler;

/**
 * SubjectAreaInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.SUBJECT_AREA;

    private final SubjectAreaHandler subjectAreaHandler;



    /**
     * Set up the Subject Area OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public SubjectAreaInstance(String       serverName,
                                AuditLog     auditLog,
                                String       localServerUserId,
                                String       localServerUserPassword,
                                int          maxPageSize,
                                String       remoteServerName,
                                String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            subjectAreaHandler = new SubjectAreaHandler(serverName,
                                                        remoteServerName,
                                                        remoteServerURL,
                                                        auditLog,
                                                        AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceURLMarker(),
                                                        ViewServiceDescription.SUBJECT_AREA.getViewServiceFullName(),
                                                        maxPageSize);
        }
        else
        {
            subjectAreaHandler = new SubjectAreaHandler(serverName,
                                                        remoteServerName,
                                                        remoteServerURL,
                                                        localServerUserId,
                                                        localServerUserPassword,
                                                        auditLog,
                                                        AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceURLMarker(),
                                                        ViewServiceDescription.SUBJECT_AREA.getViewServiceFullName(),
                                                        maxPageSize);
        }
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * data design artifacts.
     *
     * @return client
     */
    public SubjectAreaHandler getSubjectAreaManagerHandler()
    {
        return subjectAreaHandler;
    }
}
