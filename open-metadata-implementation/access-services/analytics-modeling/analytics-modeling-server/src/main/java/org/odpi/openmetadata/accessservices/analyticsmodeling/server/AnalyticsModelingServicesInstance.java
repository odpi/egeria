/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server;


import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.analyticsmodeling.converter.DatabaseConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.converter.EmptyConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.converter.SchemaConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.Database;
import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.Schema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.AnalyticsArtifactHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.ExecutionContext;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * AnalyticsModelingServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AnalyticsModelingServicesInstance extends OMASServiceInstance
{
    private DatabaseContextHandler databaseContextHandler;
    
    private AnalyticsArtifactHandler artifactHandler;
    
    private RelationalDataHandler<Database,
								    Schema,
								    Object,
								    Object,
								    Object,
								    Object>	relationalDataHandler;


    /**
     * Set up the local repository connector that will service the REST Calls
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls
     * @param supportedZones      list of zones that OMAS is allowed to serve Assets from
     * @param auditLog            logging destination
     * @param localServerUserId   userId used for server initiated actions
     * @param maxPageSize         max number of results to return on single request
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AnalyticsModelingServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones,
                               AuditLog auditLog, String localServerUserId, int maxPageSize) throws NewInstanceException {
    	
        super(AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceFullName(), repositoryConnector, supportedZones, null,
              null, auditLog, localServerUserId, maxPageSize);
        
        OMEntityDao omEntityDao = new OMEntityDao(repositoryConnector, supportedZones, auditLog);

        this.relationalDataHandler = new RelationalDataHandler<>(
        		new DatabaseConverter(repositoryHelper, serviceName,serverName),
                Database.class,
                new SchemaConverter(repositoryHelper, serviceName,serverName),
                Schema.class,
                new EmptyConverter(repositoryHelper, serviceName,serverName),
                Object.class,
                new EmptyConverter(repositoryHelper, serviceName,serverName),
                Object.class,
                new EmptyConverter(repositoryHelper, serviceName, serverName),
                Object.class,
                new EmptyConverter(repositoryHelper, serviceName, serverName),
                Object.class,
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
        
        databaseContextHandler = new DatabaseContextHandler(relationalDataHandler, omEntityDao, invalidParameterHandler);
        
        ExecutionContext ctx = new ExecutionContext(
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

        artifactHandler = new AnalyticsArtifactHandler(ctx);
    }


    /**
     * Get the handler that does major work this OMAS is designed for.
     * @return Database Context Handler
     */
    public DatabaseContextHandler getContextBuilder() {
        return databaseContextHandler;
    }
    
    /**
     * Get the handler that to create repository assets.
     * @return Database Context Handler
     */
    public AnalyticsArtifactHandler getArtifactHandler() {
        return artifactHandler;
    }
}
