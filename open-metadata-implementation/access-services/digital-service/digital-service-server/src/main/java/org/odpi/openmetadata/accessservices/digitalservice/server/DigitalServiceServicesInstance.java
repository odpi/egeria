/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.server;

import org.odpi.openmetadata.accessservices.digitalservice.ffdc.DigitalServiceErrorCode;
import org.odpi.openmetadata.accessservices.digitalservice.handlers.DigitalServiceEntityHandler;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalService;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DigitalServiceServicesInstance caches references to objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DigitalServiceServicesInstance extends OCFOMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DIGITAL_SERVICE_OMAS;

     private DigitalServiceEntityHandler digitalServiceEntityHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DigitalService is allowed to serve Assets from.
     * @param auditLog destination for audit log events.
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum number of results that can be returned on a single call
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DigitalServiceServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                  List<String>            supportedZones,
                                  AuditLog                auditLog,
                                  String                  localServerUserId,
                                  int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              null,
              auditLog,
              localServerUserId,
              maxPageSize);

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
