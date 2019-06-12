/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * Stores references to OMRS objects for a specific server. It is also responsible for registering this OMAS in the
 * Access Service Registry, and itself in the instance map
 */
public class GlossaryViewServiceInstance extends OCFOMASServiceInstance {

    /**
     * Set up the local repository connector that will service the REST Calls, registers this OMAS in the
     * Access Service Registry and in the instance map
     *
     * @param supportedZones                   supported zones
     * @param omrsRepositoryConnector responsible for servicing the REST calls
     * @param omrsAuditLog            audit log
     *
     * @throws NewInstanceException if a problem occurred during initialization
     */
    public GlossaryViewServiceInstance(List<String> supportedZones, OMRSRepositoryConnector omrsRepositoryConnector, OMRSAuditLog omrsAuditLog)
            throws NewInstanceException {

        super(AccessServiceDescription.GLOSSARY_VIEW_OMAS.getAccessServiceName(), omrsRepositoryConnector,
                supportedZones, null, omrsAuditLog);
    }

}
