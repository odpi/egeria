/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * OCFOMASServiceInstance provides an instance base class for Open Metadata Access Services (OMASs)
 * that are built around the Open Connector Framework (OCF).
 */
public class OCFOMASServiceInstance extends OMASServiceInstance
{
    protected AssetHandler              assetHandler;
    protected SchemaTypeHandler         schemaTypeHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    @Deprecated
    public OCFOMASServiceInstance(String                  serviceName,
                                  OMRSRepositoryConnector repositoryConnector,
                                  List<String>            supportedZones,
                                  List<String>            defaultZones,
                                  AuditLog                auditLog,
                                  String                  localServerUserId,
                                  int                     maxPageSize) throws NewInstanceException
    {
        this(serviceName, repositoryConnector, supportedZones, defaultZones, null, auditLog, localServerUserId, maxPageSize);
    }


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that the access service is allowed to serve Assets from.
     * @param defaultZones list of zones that the access service should set in all new Assets.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    @Deprecated
    public OCFOMASServiceInstance(String                  serviceName,
                                  OMRSRepositoryConnector repositoryConnector,
                                  List<String>            supportedZones,
                                  List<String>            defaultZones,
                                  List<String>            publishZones,
                                  AuditLog                auditLog,
                                  String                  localServerUserId,
                                  int                     maxPageSize) throws NewInstanceException
    {
        super(serviceName, repositoryConnector, supportedZones, defaultZones, publishZones, auditLog, localServerUserId, maxPageSize);


        this.assetHandler = new AssetHandler(serviceName,
                                             serverName,
                                             invalidParameterHandler,
                                             repositoryHandler,
                                             repositoryHelper,
                                             supportedZones,
                                             publishZones,
                                             defaultZones);

        this.schemaTypeHandler = new SchemaTypeHandler(serviceName,
                                                       serverName,
                                                       invalidParameterHandler,
                                                       repositoryHandler,
                                                       repositoryHelper);

    }
}
