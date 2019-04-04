/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.assets;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.views.ColumnContextBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseContextHandler {


    private static final Logger log = LoggerFactory.getLogger(DatabaseContextHandler.class);
    private org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao;
    private OMRSRepositoryConnector enterpriseConnector;
    private OMRSRepositoryHelper repositoryHelper;
    private OMRSAuditLog auditLog;
    private ColumnContextBuilder columnContextBuilder;

    public DatabaseContextHandler(OMEntityDao omEntityDao, OMRSRepositoryConnector enterpriseConnector,
                                  OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        this.repositoryHelper = enterpriseConnector.getRepositoryHelper();
        this.auditLog = auditLog;
        this.columnContextBuilder = new ColumnContextBuilder(enterpriseConnector);
    }

    public List<DatabaseSource> getDataStores(int startFrom, int pageSize) {
        InstanceProperties instanceProperties = omEntityDao.buildMatchingInstanceProperties(Collections.emptyMap(),
                true);
        try {
            List<EntityDetail> entities = omEntityDao.findEntities(instanceProperties, Constants.DATA_STORE, startFrom, pageSize);
            return getDatabaseSources(entities);
        } catch (Exception e) {
            // throw e; TODO throw specific exception
            return null;
        }
    }

    public List<TableSource> getTables(String databaseGuid, int startFrom, int pageSize) {
        EntityDetail database = getEntity(databaseGuid);
        if (database == null  || !repositoryHelper.isTypeOf("getTables", database.getType().getTypeDefName(), Constants.DATA_STORE))
            return null;
        try {
            return columnContextBuilder.getTablesForDatabase(databaseGuid, startFrom, pageSize);

        } catch (Exception e) {
            // throw e; TODO throw specific exception
            return null;
        }
    }

    public List<TableContextEvent> getTableContext(String tableGuid) {
        EntityDetail table = getEntity(tableGuid);
        if (table == null || !repositoryHelper.isTypeOf("getTableContext", table.getType().getTypeDefName(), Constants.RELATIONAL_TABLE))
            return null;
        try {
            List<Relationship> relationships = columnContextBuilder.getSchemaTypeRelationships(table, Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);
            if(relationships !=  null && !relationships.isEmpty()){
                return columnContextBuilder.getTableContext(relationships.get(0).getEntityTwoProxy().getGUID(), Constants.START_FROM, Constants.PAGE_SIZE);
            }else{
                log.error("Table doesn't have any schema");//TODO throw specific exception
                return null;
            }

        } catch (Exception e) {
            // throw e; TODO throw specific exception
            return null;
        }
    }

    private List<DatabaseSource> getDatabaseSources(List<EntityDetail> entities) {
        List<DatabaseSource> databaseSources = new ArrayList<>();
        if (entities != null && !entities.isEmpty()) {
            for (EntityDetail entityDetail : entities) {
                List<TableContextEvent> contexts;
                try {
                    contexts = columnContextBuilder.getDatabaseContext(entityDetail.getGUID());
                    databaseSources.add(contexts.get(0).getTableSource().getDatabaseSource());
                } catch (Exception e) {
                    log.error("Exception getting context for database:  ", e);
                }

            }
        }
        return databaseSources;
    }

    public List<TableColumn> getTableColumns(String tableGuid, int startFrom, int pageSize) {
        EntityDetail table = getEntity(tableGuid);
        if (table == null || !repositoryHelper.isTypeOf("getTableColumns", table.getType().getTypeDefName(), Constants.RELATIONAL_TABLE)) return null;
        try {
            List<Relationship> relationships = columnContextBuilder.getSchemaTypeRelationships(table, Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);

            if(relationships !=  null && !relationships.isEmpty()){
                return columnContextBuilder.getTableColumns(relationships.get(0).getEntityTwoProxy().getGUID(), startFrom, pageSize);
            }else{
                log.error("Table doesn't have any schema");//TODO throw specific exception
                return null;
            }

        } catch (Exception e) {
            // TODO throw specific exception
            return null;
        }
    }

    private EntityDetail getEntity(String guid) {
        EntityDetail entityDetail = null;
        try {
            entityDetail = omEntityDao.getEntityByGuid(guid);
        } catch (Exception e) {
            log.error("Exception retrieving entity: ", e);
        }
        return entityDetail;
    }
}
