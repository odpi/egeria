/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.server;

import org.odpi.openmetadata.accessservices.digitalservice.ffdc.DigitalServiceErrorCode;
import org.odpi.openmetadata.accessservices.digitalservice.handlers.DigitalServiceEntityHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DigitalServiceServicesInstance caches references to objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DigitalServiceServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DIGITAL_SERVICE_OMAS;

     private DigitalServiceEntityHandler digitalServiceEntityHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that AssetManager is allowed to serve Assets from.
     * @param defaultZones list of zones that AssetManager sets up in new Asset instances.
     * @param publishZones list of zones that AssetManager sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicConnection topic of the client side listener
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DigitalServiceServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                          List<String>            supportedZones,
                                          List<String>            defaultZones,
                                          List<String>            publishZones,
                                          AuditLog                auditLog,
                                          String                  localServerUserId,
                                          int                     maxPageSize,
                                          Connection              outTopicConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              null,
              outTopicConnection);

        final String methodName = "new DigitalServiceInstance";

        if (repositoryHandler != null)
        {
            digitalServiceEntityHandler = new DigitalServiceEntityHandler(  repositoryHelper,
                                                                            repositoryHandler,
                                                                            invalidParameterHandler);
        }
        else
        {
            throw new NewInstanceException(DigitalServiceErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }

    public DigitalServiceEntityHandler getDigitalServiceEntityHandler() {
        return digitalServiceEntityHandler;
    }

}
