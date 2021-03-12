/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.AssetLineage;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipsEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Optional;
import java.util.Set;

public class OpenLineageAssetContextHandler {
    private static final String DATA_FILE = "DataFile";
    private static final String RELATIONAL_TABLE = "RelationalTable";
    private static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";
    private static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";

    private final AssetLineage assetLineageClient;

    public OpenLineageAssetContextHandler(AssetLineage assetLineageClient) {
        this.assetLineageClient = assetLineageClient;
    }

    /**
     * Retrieve asset context for entity.
     *
     * @param guid        the guid of the entity
     * @param typeDefName the type def name of the entity
     * @return a relationships set
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    public Set<GraphContext> getAssetContextForEntity(String guid, String typeDefName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return assetLineageClient.provideAssetContext(guid, typeDefName).getRelationships();
    }

    /**
     * Given the lineage relationship event, it searches among its entities for a data file or a relational table
     * and returns the found lineage entity.
     *
     * @param lineageRelationshipsEvent the lineage relationships event
     * @return the lineage entity
     */
    public Optional<LineageEntity> getTableOrFileLineageEntityFromEvent(LineageRelationshipsEvent lineageRelationshipsEvent) {
        Set<GraphContext> relationships = lineageRelationshipsEvent.getRelationshipsContext().getRelationships();

        Optional<LineageEntity> entity = getEntityByEntityTypeFromRelationships(relationships, NESTED_SCHEMA_ATTRIBUTE, RELATIONAL_TABLE);

        if(!entity.isPresent()) {
            entity = getEntityByEntityTypeFromRelationships(relationships, ASSET_SCHEMA_TYPE, DATA_FILE);
        }
        return entity;
    }

    /**
     * Given a set of relationships, it gets the first entity found by a given entity type also using the given
     * relationship type through which it gets to the entity.
     *
     * @param relationships    the relationships
     * @param relationshipType the relationship type
     * @param entityType       the entity type
     * @return the entity by entity type from relationships
     */
    private Optional<LineageEntity> getEntityByEntityTypeFromRelationships(Set<GraphContext> relationships,
                                                                          String relationshipType, String entityType) {
        return relationships
                .stream()
                .filter(relationship -> relationshipType.equals(relationship.getRelationshipType()))
                .map(relationship -> getEntityByEntityType(entityType, relationship))
                .findFirst();
    }

    /**
     * Given a relationship, it gets the entity by entity type from the two vertices (to and from).
     *
     * @param entityType   the entity type
     * @param relationship the relationship
     * @return the entity by entity type
     */
    private LineageEntity getEntityByEntityType(String entityType, GraphContext relationship) {
        return entityType.equals(relationship.getToVertex().getTypeDefName()) ? relationship.getToVertex() : relationship.getFromVertex();
    }

}
