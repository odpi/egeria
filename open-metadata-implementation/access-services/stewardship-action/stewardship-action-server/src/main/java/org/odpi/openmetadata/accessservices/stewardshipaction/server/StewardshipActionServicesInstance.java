/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server;

import org.odpi.openmetadata.accessservices.stewardshipaction.converters.ElementStubConverter;
import org.odpi.openmetadata.accessservices.stewardshipaction.ffdc.StewardshipActionErrorCode;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * StewardshipActionServicesInstance caches references to objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class StewardshipActionServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.STEWARDSHIP_ACTION_OMAS;

    private final ElementStubConverter<ElementStub> elementStubConverter;
    private final ReferenceableHandler<ElementStub> referenceableHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that StewardshipAction is allowed to serve Assets from.
     * @param auditLog destination for audit log events.
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum number of results that can be returned on a single call
     * @throws NewInstanceException a problem occurred during initialization
     */
    public StewardshipActionServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                             List<String>            supportedZones,
                                             AuditLog                auditLog,
                                             String                  localServerUserId,
                                             int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              null,
              null,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.elementStubConverter = new ElementStubConverter<>(repositoryHelper, serviceName, serverName);

            this.referenceableHandler = new ReferenceableHandler<>(new ElementStubConverter<>(repositoryHelper, serviceName, serverName),
                                                                   ElementStub.class,
                                                                   serviceName,
                                                                   serverName,
                                                                   invalidParameterHandler,
                                                                   repositoryHandler,
                                                                   repositoryHelper,
                                                                   localServerUserId,
                                                                   securityVerifier,
                                                                   supportedZones,
                                                                   defaultZones,
                                                                   publishZones,
                                                                   auditLog);
        }
        else
        {
            throw new NewInstanceException(StewardshipActionErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }



    /**
     * Return the element stub converter
     *
     * @return converter
     */
    ElementStubConverter<ElementStub> getElementStubConverter()
    {
        return elementStubConverter;
    }


    /**
     * Return the referencable handler
     *
     * @return handler
     */
    public ReferenceableHandler<ElementStub> getReferenceableHandler()
    {
        return referenceableHandler;
    }

}
