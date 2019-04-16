/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.assets;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.*;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.views.ColumnContextBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseContextHandler {


    private static final Logger log = LoggerFactory.getLogger(DatabaseContextHandler.class);
    private org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao;
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

    public List<DatabaseSource> getDatabases(int startFrom, int pageSize) {
        InstanceProperties instanceProperties = omEntityDao.buildMatchingInstanceProperties(Collections.emptyMap(), true);
        try {
            List<EntityDetail> entities = omEntityDao.findEntities(instanceProperties, Constants.DATA_STORE, startFrom, pageSize);
            return buildDatabaseContext(entities);
        } catch (InvalidParameterException | PropertyErrorException | TypeErrorException | FunctionNotSupportedException | PagingErrorException | UserNotAuthorizedException | RepositoryErrorException e) {
            throw new RetrieveEntityException(DatabaseContextHandler.class.getName(),
                    InformationViewErrorCode.GET_ENTITY_EXCEPTION.getFormattedErrorMessage("type", Constants.DATA_STORE, e.getMessage()), InformationViewErrorCode.GET_ENTITY_EXCEPTION.getSystemAction(),
                    InformationViewErrorCode.GET_ENTITY_EXCEPTION.getUserAction(), e);
        }
    }

    public List<TableSource> getTables(String databaseGuid, int startFrom, int pageSize) {
        EntityDetail database = getEntity(databaseGuid, Constants.DATA_STORE);
        try {
            return columnContextBuilder.getTablesForDatabase(database.getGUID(), startFrom, pageSize);
        } catch (UserNotAuthorizedException | EntityProxyOnlyException | PagingErrorException | TypeErrorException | FunctionNotSupportedException | EntityNotKnownException | InvalidParameterException | PropertyErrorException | RepositoryErrorException e) {
            throw new EntityNotFoundException(DatabaseContextHandler.class.getName(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getFormattedErrorMessage(database.getGUID(), e.getMessage()), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getSystemAction(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getUserAction(), e);
        }
    }

    public List<TableContextEvent> getTableContext(String tableGuid) {
        EntityDetail table = getEntity(tableGuid , Constants.RELATIONAL_TABLE);
        try {
            List<Relationship> relationships = columnContextBuilder.getSchemaTypeRelationships(table, Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);
            if (relationships != null && !relationships.isEmpty()) {
                return columnContextBuilder.getTableContext(relationships.get(0).getEntityTwoProxy().getGUID(), Constants.START_FROM, Constants.PAGE_SIZE);
            } else {
                throw new ContextLoadException(InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getHttpErrorCode(), DatabaseContextHandler.class.getName(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getFormattedErrorMessage(tableGuid, "Schema attribute type relationship doesn't exist"), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getSystemAction(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getUserAction(), null);
            }
        } catch (PagingErrorException | TypeDefNotKnownException | TypeErrorException | EntityNotKnownException | UserNotAuthorizedException | RelationshipNotKnownException | PropertyErrorException | EntityProxyOnlyException | InvalidParameterException | FunctionNotSupportedException | RepositoryErrorException e) {
            throw new ContextLoadException(InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getHttpErrorCode(), DatabaseContextHandler.class.getName(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getFormattedErrorMessage(tableGuid, e.getMessage()), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getSystemAction(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getUserAction(), e);
        }
    }

    private List<DatabaseSource> buildDatabaseContext(List<EntityDetail> entities) {
        List<DatabaseSource> databaseSources = new ArrayList<>();
        if (entities != null && !entities.isEmpty()) {
            for (EntityDetail entityDetail : entities) {
                List<TableContextEvent> contexts;
                try {
                    contexts = columnContextBuilder.getDatabaseContext(entityDetail.getGUID());
                    databaseSources.add(contexts.get(0).getTableSource().getDatabaseSource());
                } catch (UserNotAuthorizedException | EntityProxyOnlyException | RepositoryErrorException | PagingErrorException | TypeErrorException | InvalidParameterException | EntityNotKnownException | PropertyErrorException | FunctionNotSupportedException e) {
                    throw new ContextLoadException(InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getHttpErrorCode(), DatabaseContextHandler.class.getName(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getFormattedErrorMessage(entityDetail.getGUID(), e.getMessage()), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getSystemAction(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getUserAction(), e);
                }
            }
        }
        return databaseSources;
    }

    public List<TableColumn> getTableColumns(String tableGuid, int startFrom, int pageSize) {
        EntityDetail table = getEntity(tableGuid, Constants.RELATIONAL_TABLE);
        List<Relationship> relationships;

        try {
            relationships = columnContextBuilder.getSchemaTypeRelationships(table, Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);
            if (relationships != null && !relationships.isEmpty()) {
                return columnContextBuilder.getTableColumns(relationships.get(0).getEntityTwoProxy().getGUID(), startFrom, pageSize);
            }
            else{
                throw new IncorrectModelException(DatabaseContextHandler.class.getName(),
                                                InformationViewErrorCode.INCORRECT_MODEL_EXCEPTION.getFormattedErrorMessage(tableGuid, "Table Schema Type is missing"),
                                                InformationViewErrorCode.INCORRECT_MODEL_EXCEPTION.getSystemAction(),
                                                InformationViewErrorCode.INCORRECT_MODEL_EXCEPTION.getUserAction(), null);
            }
        } catch (PagingErrorException | TypeDefNotKnownException | PropertyErrorException | UserNotAuthorizedException | RelationshipNotKnownException | EntityProxyOnlyException | InvalidParameterException | FunctionNotSupportedException | RepositoryErrorException | TypeErrorException | EntityNotKnownException e) {
            throw new ContextLoadException(InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getHttpErrorCode(), DatabaseContextHandler.class.getName(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getFormattedErrorMessage(tableGuid, e.getMessage()), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getSystemAction(), InformationViewErrorCode.BUILD_CONTEXT_EXCEPTION.getUserAction(), e);
        }
    }

    private EntityDetail getEntity(String guid, String typeName) {
        EntityDetail entityDetail;
        try {
            entityDetail = omEntityDao.getEntityByGuid(guid);
        } catch (EntityProxyOnlyException | EntityNotKnownException | UserNotAuthorizedException | InvalidParameterException | RepositoryErrorException e) {
            throw new EntityNotFoundException(DatabaseContextHandler.class.getName(), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage(guid, typeName), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(), e);
        }
        if (entityDetail == null)
            throw new EntityNotFoundException(InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getHttpErrorCode(), DatabaseContextHandler.class.getName(), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage(Constants.GUID, guid, typeName), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(), null);
        if(!repositoryHelper.isTypeOf("getEntity", entityDetail.getType().getTypeDefName(), typeName)){
            throw new IncorrectTypeException(InformationViewErrorCode.INCORRECT_TYPE_EXCEPTION.getHttpErrorCode(), DatabaseContextHandler.class.getName(), InformationViewErrorCode.INCORRECT_TYPE_EXCEPTION.getFormattedErrorMessage(Constants.GUID,  guid, typeName), InformationViewErrorCode.INCORRECT_TYPE_EXCEPTION.getSystemAction(), InformationViewErrorCode.INCORRECT_TYPE_EXCEPTION.getUserAction(), null);
        }
        return entityDetail;
    }
}
