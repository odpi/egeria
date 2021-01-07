/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.server.processors;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.Context;
import org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class GovernedAssetContextBuilder
{

    private String serverUserName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;

    public GovernedAssetContextBuilder(String serverUserName, RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serverUserName = serverUserName;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }

    public Context buildContextForColumn(String userID, String assetId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "buildContextForColumn";
        EntityDetail column = repositoryHandler.getEntityByGUID(userID, assetId, "guid", Constants.RELATIONAL_COLUMN, methodName);
        if (column != null && isRelationalColumn(column)) {
            return getDatabaseContextForColumn(userID, column);
        }
        return null;
    }

    public Context buildContextForTable(String userID, String assetId) throws UserNotAuthorizedException, PropertyServerException {
        Context context = new Context();

        context.setTable(getTableName(userID, assetId));
        context.setSchema(Constants.DEFAULT_SCHEMA_NAME);

        return context;
    }

    private Context getDatabaseContextForColumn(String userID, EntityDetail column) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getDatabaseContextForColumn";

        Context context = new Context();

        String columnName = repositoryHelper.getStringProperty(serverUserName, Constants.NAME, column.getProperties(), methodName);
        context.setColumn(columnName);
        context.setTable(getTableName(userID, column.getGUID()));
        context.setSchema(Constants.DEFAULT_SCHEMA_NAME);

        return context;
    }

    private String getTableName(String userID, String relationalColumnGuid) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getTableName";
        final EntityDetail relationalTable = getRelationalTable(userID, relationalColumnGuid);

        if (relationalTable != null && relationalTable.getProperties() != null) {
            return repositoryHelper.getStringProperty(serverUserName, Constants.NAME, relationalTable.getProperties(), methodName);
        }

        return null;
    }

    private EntityDetail getRelationalTable(String userID, String relationalColumnGuid) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationalTable";
        return repositoryHandler.getEntityForRelationshipType(userID,
                                                              relationalColumnGuid,
                                                              Constants.RELATIONAL_COLUMN,
                                                              Constants.NESTED_SCHEMA_ATTRIBUTE_GUID,
                                                              Constants.NESTED_SCHEMA_ATTRIBUTE,
                                                              methodName);
    }

    private boolean isRelationalColumn(EntityDetail entityDetail) {
        if (entityDetail.getType() != null && entityDetail.getType().getTypeDefName() != null) {
            return entityDetail.getType().getTypeDefName().equals(Constants.RELATIONAL_COLUMN);
        }
        return false;
    }
}
