/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server;


import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.datamanager.converters.DatabaseColumnConverter;
import org.odpi.openmetadata.accessservices.datamanager.converters.DatabaseConverter;
import org.odpi.openmetadata.accessservices.datamanager.converters.DatabaseSchemaConverter;
import org.odpi.openmetadata.accessservices.datamanager.converters.DatabaseTableConverter;
import org.odpi.openmetadata.accessservices.datamanager.converters.DatabaseViewConverter;
import org.odpi.openmetadata.accessservices.datamanager.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseSchemaElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseViewElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
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
    
    private RelationalDataHandler<DatabaseElement,
							    DatabaseSchemaElement,
							    DatabaseTableElement,
							    DatabaseViewElement,
							    DatabaseColumnElement,
							    SchemaTypeElement>                         relationalDataHandler;


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
        		new DatabaseConverter<>(repositoryHelper, serviceName,serverName),
                DatabaseElement.class,
                new DatabaseSchemaConverter<>(repositoryHelper, serviceName,serverName),
                DatabaseSchemaElement.class,
                new DatabaseTableConverter<>(repositoryHelper, serviceName,serverName),
                DatabaseTableElement.class,
                new DatabaseViewConverter<>(repositoryHelper, serviceName,serverName),
                DatabaseViewElement.class,
                new DatabaseColumnConverter<>(repositoryHelper, serviceName, serverName),
                DatabaseColumnElement.class,
                new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                SchemaTypeElement.class,
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
    }


    /**
     * Get the handler that does major work this OMAS is designed for.
     * @return Database Context Handler
     */
    public DatabaseContextHandler getContextBuilder() {
        return databaseContextHandler;
    }
}
