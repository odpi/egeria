/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * Stores references to OMRS objects for a specific server. It is also responsible for registering this OMAS in the
 * Access Service Registry, and itself in the instance map
 */
public class GlossaryViewServiceInstance extends OMASServiceInstance
{

    /**
     * Set up the local repository connector that will service the REST Calls, registers this OMAS in the
     * Access Service Registry and in the instance map
     *
     * @param omrsRepositoryConnector responsible for servicing the REST calls
     * @param omrsAuditLog            audit log
     * @param localServerUserId       userId for server requests
     * @param maxPageSize             maximum values to return on any single call
     * @throws NewInstanceException if a problem occurred during initialization
     */
    public GlossaryViewServiceInstance(OMRSRepositoryConnector omrsRepositoryConnector,
                                       OMRSAuditLog            omrsAuditLog,
                                       String                  localServerUserId,
                                       int                     maxPageSize) throws NewInstanceException {

        super(AccessServiceDescription.GLOSSARY_VIEW_OMAS.getAccessServiceFullName(),
              omrsRepositoryConnector, null, null, null, omrsAuditLog, localServerUserId, maxPageSize);
    }

}
