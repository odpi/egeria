/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.processor;

import org.odpi.openmetadata.accessservices.governanceengine.api.model.Context;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.*;

public class ContextBuilder {

    private String serverUserName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;

    public ContextBuilder(String serverUserName, RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serverUserName = serverUserName;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }

    public Context buildContextForColumn(String userID, String assetId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "buildContextForColumn";
        EntityDetail column = repositoryHandler.getEntityByGUID(userID, assetId, "guid", RELATIONAL_COLUMN, methodName);
        if (column != null && isRelationalColumn(column)) {
            return getDatabaseContextForColumn(userID, column);
        }
        return null;
    }

    public Context buildContextForTable(String userID, String assetId) throws UserNotAuthorizedException, PropertyServerException {
        Context context = new Context();

        context.setTable(getTableName(userID, assetId));
        context.setSchema(DEFAULT_SCHEMA_NAME);

        return context;
    }

    private Context getDatabaseContextForColumn(String userID, EntityDetail column) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getDatabaseContextForColumn";

        Context context = new Context();

        String columnName = repositoryHelper.getStringProperty(serverUserName, NAME, column.getProperties(), methodName);
        context.setColumn(columnName);
        context.setTable(getTableName(userID, column.getGUID()));
        context.setSchema(DEFAULT_SCHEMA_NAME);

        return context;
    }

    private String getTableName(String userID, String relationalColumnGuid) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getTableName";
        final EntityDetail relationalTable = getRelationalTable(userID, relationalColumnGuid);

        if (relationalTable != null && relationalTable.getProperties() != null) {
            return repositoryHelper.getStringProperty(serverUserName, NAME, relationalTable.getProperties(), methodName);
        }

        return null;
    }

    private EntityDetail getRelationalTable(String userID, String relationalColumnGuid) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationalTable";
        return repositoryHandler.getEntityForRelationshipType(userID,
                relationalColumnGuid,
                RELATIONAL_COLUMN,
                NESTED_SCHEMA_ATTRIBUTE_GUID,
                NESTED_SCHEMA_ATTRIBUTE,
                methodName);
    }

    private boolean isRelationalColumn(EntityDetail entityDetail) {
        if (entityDetail.getType() != null && entityDetail.getType().getTypeDefName() != null) {
            return entityDetail.getType().getTypeDefName().equals(RELATIONAL_COLUMN);
        }
        return false;
    }
}
