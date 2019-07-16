/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class DataPlatformServicesInstance extends OCFOMASServiceInstance {

    private static AccessServiceDescription myDescription = AccessServiceDescription.DATA_PLATFORM_OMAS;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName         name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog            logging destination
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DataPlatformServicesInstance(String serviceName, OMRSRepositoryConnector repositoryConnector, OMRSAuditLog auditLog, String serverName) throws NewInstanceException {
        super(serviceName, repositoryConnector, auditLog);
        this.serverName = serverName;
        this.serviceName=myDescription.getAccessServiceName();
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        DataPlatformServicesInstanceMap.removeInstanceForJVM(serverName);
    }


}
