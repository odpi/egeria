/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.processor;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.Context;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.*;

public class ContextBuilder {

    private Map<String, String> relationalTableNames = new HashMap();

    public List<Context> buildContextForColumn(OMRSMetadataCollection metadataCollection, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, FunctionNotSupportedException, PagingErrorException {
        EntityDetail column = getEntity(metadataCollection, assetId);
        if (isRelationalColumnType(column)) {
            Context context = getDatabaseContextForColumn(metadataCollection, column);
            return Collections.singletonList(context);
        }
        return Collections.emptyList();
    }

    public List<Context> buildContextForTable(OMRSMetadataCollection metadataCollection, String assetId) throws InvalidParameterException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, EntityProxyOnlyException, UserNotAuthorizedException, RepositoryErrorException {
        Context context = new Context();
        context.setTable(getTableName(metadataCollection, assetId));
        context.setSchema(DEFAULT_SCHEMA_NAME);

        return Collections.singletonList(context);
    }


    public List<Context> buildContextForGlossaryTerm(OMRSMetadataCollection metadataCollection, String assetId) throws InvalidParameterException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException {
        List<String> columnsAssigned = getRelationalColumnsAssignedToGlossaryTerm(metadataCollection, assetId);
        if (columnsAssigned == null || columnsAssigned.isEmpty()) {
            return new ArrayList<>();
        }
        List<Context> contexts = new ArrayList<>(columnsAssigned.size());
        for (String columnId : columnsAssigned) {
            EntityDetail column = getEntity(metadataCollection, columnId);
            if (isRelationalColumnType(column)) {
                Context context = getDatabaseContextForColumn(metadataCollection, column);
                contexts.add(context);
            }
        }
        return contexts;
    }

    private Context getDatabaseContextForColumn(OMRSMetadataCollection metadataCollection, EntityDetail column) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, UserNotAuthorizedException, TypeDefNotKnownException, TypeErrorException, FunctionNotSupportedException, PagingErrorException, PropertyErrorException {
        Context context = new Context();
        String columnName = getColumnName(column);
        context.setColumn(columnName);
        context.setTable(getTableName(metadataCollection, column.getGUID()));
        context.setSchema(DEFAULT_SCHEMA_NAME);
        return context;
    }

    private EntityDetail getEntity(OMRSMetadataCollection metadataCollection, String relationalColumnGuid) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, UserNotAuthorizedException {
        return metadataCollection.getEntityDetail(GOVERNANCE_ENGINE, relationalColumnGuid);
    }

    private String getColumnName(EntityDetail entityDetail) {
        return getStringProperty(entityDetail.getProperties(), NAME);
    }

    private String getTableName(OMRSMetadataCollection metadataCollection, String relationalColumnGuid) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, TypeErrorException, FunctionNotSupportedException, EntityNotKnownException, PagingErrorException, PropertyErrorException, EntityProxyOnlyException {
        final String relationalTableTypeGUID = getRelationalTableTypeGUID(metadataCollection, relationalColumnGuid);
        if (relationalTableNames.containsKey(relationalTableTypeGUID)) {
            return relationalTableNames.get(relationalTableTypeGUID);
        }

        final EntityDetail relationalTable = getRelationalTable(metadataCollection, relationalTableTypeGUID);

        if (relationalTable != null && relationalTable.getProperties() != null) {
            String tableName = getStringProperty(relationalTable.getProperties(), NAME);
            relationalTableNames.put(relationalTableTypeGUID, tableName);
            return tableName;
        }

        return null;
    }

    private List<String> getRelationalColumnsAssignedToGlossaryTerm(OMRSMetadataCollection metadataCollection, String resourceID) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException, TypeErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, TypeDefNotKnownException {
        TypeDef semanticAssignment = metadataCollection.getTypeDefByName(GOVERNANCE_ENGINE, "SemanticAssignment");
        String relationshipGUID = semanticAssignment.getGUID();

        final List<Relationship> semanticAssignmentForGlossaryTerm = getSemanticAssignmentForGlossaryTerm(
                metadataCollection,
                resourceID,
                relationshipGUID);
        List<String> columnIds = new ArrayList<>();

        if (semanticAssignmentForGlossaryTerm == null || semanticAssignmentForGlossaryTerm.isEmpty()) {
            return Collections.emptyList();
        }
        for (Relationship relationship : semanticAssignmentForGlossaryTerm) {
            final String columnId = getTheOtherEntityGuid(resourceID, relationship);
            columnIds.add(columnId);
        }
        return columnIds;
    }

    private EntityDetail getRelationalTable(OMRSMetadataCollection metadataCollection, String relationalTableTypeGUID) throws InvalidParameterException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, EntityProxyOnlyException, UserNotAuthorizedException, RepositoryErrorException {
        return getEndOfRelationship(metadataCollection, relationalTableTypeGUID, SCHEMA_ATTRIBUTE_TYPE);
    }

    private String getRelationalTableTypeGUID(OMRSMetadataCollection metadataCollection, String relationalColumnGuid) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, EntityProxyOnlyException {
        final EntityDetail relationalTableType = getRelationalTableType(metadataCollection, relationalColumnGuid);
        if (relationalTableType == null) {
            return null;
        }
        return relationalTableType.getGUID();
    }

    private EntityDetail getRelationalTableType(OMRSMetadataCollection metadataCollection, String relationalColumnGuid) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, EntityProxyOnlyException {
        return getEndOfRelationship(metadataCollection, relationalColumnGuid, ATTRIBUTE_FOR_SCHEMA);
    }

    private EntityDetail getEndOfRelationship(OMRSMetadataCollection metadataCollection, String relationalTableTypeGUID, String relationshipName) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, EntityProxyOnlyException {
        final Relationship relationshipForEntity = getRelationshipForEntity(metadataCollection, relationalTableTypeGUID, relationshipName);
        if (relationshipForEntity == null) {
            return null;
        }
        String relationalTableGUID = getTheOtherEntityGuid(relationalTableTypeGUID, relationshipForEntity);
        if (relationalTableGUID == null) {
            return null;
        }
        return metadataCollection.getEntityDetail(GOVERNANCE_ENGINE, relationalTableGUID);
    }

    private Relationship getRelationshipForEntity(OMRSMetadataCollection metadataCollection, String relationalColumnGuid, String relationshipType) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException {
        String attributeForSchemaGUID = metadataCollection.getTypeDefByName(GOVERNANCE_ENGINE, relationshipType).getGUID();
        List<InstanceStatus> statusList = getActiveStatuses();

        final List<Relationship> relationshipsForEntity = metadataCollection
                .getRelationshipsForEntity(GOVERNANCE_ENGINE, relationalColumnGuid, attributeForSchemaGUID, 0, statusList, null, null, null, 0);
        if (relationshipsForEntity != null && !relationshipsForEntity.isEmpty()) {
            return relationshipsForEntity.get(0);
        }
        return null;
    }

    private String getTheOtherEntityGuid(String givenEntityId, Relationship relationship) {
        if (relationship.getEntityOneProxy().getGUID().equals(givenEntityId)) {
            return relationship.getEntityTwoProxy().getGUID();
        }
        return relationship.getEntityOneProxy().getGUID();
    }

    private List<InstanceStatus> getActiveStatuses() {
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        return statusList;
    }

    private String getStringProperty(InstanceProperties properties, String propertyName) {
        if (properties != null &&
                properties.getPropertyValue(propertyName) instanceof PrimitivePropertyValue
                && ((PrimitivePropertyValue) properties.getPropertyValue(propertyName)).getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING) {
            return (String) ((PrimitivePropertyValue) properties.getPropertyValue(propertyName)).getPrimitiveValue();
        }
        return "";
    }

    private List<Relationship> getSemanticAssignmentForGlossaryTerm(OMRSMetadataCollection metadataCollection, String resourceID, String relationshipGUID ) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException, TypeErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException {

        return metadataCollection.getRelationshipsForEntity(GOVERNANCE_ENGINE,
                resourceID,
                relationshipGUID,
                0,
                getActiveStatuses(),
                null,
                null,
                null,
                0);
    }

    private boolean isRelationalColumnType(EntityDetail entityDetail) {
        return entityDetail.getType().getTypeDefName().equals(RELATIONAL_COLUMN);
    }
}
