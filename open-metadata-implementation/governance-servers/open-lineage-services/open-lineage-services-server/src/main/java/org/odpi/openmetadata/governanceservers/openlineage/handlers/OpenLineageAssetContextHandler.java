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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OpenLineageAssetContextHandler {
    private static final String DATA_FILE = "DataFile";
    private static final String AVRO_FILE = "AvroFile";
    private static final String CSV_FILE = "CSVFile";
    private static final String JSON_FILE = "JSONFile";
    private static final String KEYSTORE_FILE = "KeystoreFile";
    private static final String LOG_FILE = "LogFile";
    private static final String MEDIA_FILE = "MediaFile";
    private static final String DOCUMENT = "Document";
    private static final List<String> DATA_FILE_TYPES = Arrays.asList(DATA_FILE, AVRO_FILE, CSV_FILE, JSON_FILE,
            KEYSTORE_FILE, LOG_FILE, MEDIA_FILE, DOCUMENT);
    private static final String RELATIONAL_TABLE = "RelationalTable";
    private static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";
    private static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";

    private final String localServerUserId;
    private final AssetLineage assetLineageClient;

    public OpenLineageAssetContextHandler(String localServerUserId, AssetLineage assetLineageClient) {
        this.localServerUserId = localServerUserId;
        this.assetLineageClient = assetLineageClient;
    }

    /**
     * Determines the publishing of the entity's asset contexts and returns the list of all entities inside the context.
     *
     * @param guid        the guid of the entity
     * @param typeDefName the type def name of the entity
     * @return a relationships list
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    public List<String> getAssetContextForEntity(String guid, String typeDefName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return assetLineageClient.publishAssetContext(localServerUserId, guid, typeDefName);
    }

    /**
     * Given the lineage relationship event, it searches among its entities for a data file or a relational table
     * and returns the found lineage entity.
     *
     * @param lineageRelationshipsEvent the lineage relationships event
     * @return the lineage entity
     */
    public Optional<LineageEntity> getAssetLineageEntity(LineageRelationshipsEvent lineageRelationshipsEvent) {
        Set<GraphContext> relationships = lineageRelationshipsEvent.getRelationshipsContext().getRelationships();

        Optional<LineageEntity> entity = getEntityByEntityTypeFromRelationships(relationships, NESTED_SCHEMA_ATTRIBUTE,
                Collections.singletonList(RELATIONAL_TABLE));

        if(!entity.isPresent()) {
            entity = getEntityByEntityTypeFromRelationships(relationships, ASSET_SCHEMA_TYPE, DATA_FILE_TYPES);
        }
        return entity;
    }

    /**
     * Given a set of relationships, it gets the first entity found by a given entity type also using the given
     * relationship type through which it gets to the entity.
     *
     * @param relationships    the relationships
     * @param relationshipType the relationship type
     * @param entityTypes       the entity types
     * @return the entity by entity type from relationships
     */
    private Optional<LineageEntity> getEntityByEntityTypeFromRelationships(Set<GraphContext> relationships,
                                                                          String relationshipType, List<String> entityTypes) {
        return relationships
                .stream()
                .filter(relationship -> relationshipType.equals(relationship.getRelationshipType()))
                .map(relationship -> getEntityByEntityType(entityTypes, relationship))
                .findFirst();
    }

    /**
     * Given a relationship, it gets the entity by entity type from the two vertices (to and from).
     *
     * @param entityTypes   the entity type
     * @param relationship the relationship
     * @return the entity by entity type
     */
    private LineageEntity getEntityByEntityType(List<String> entityTypes, GraphContext relationship) {
        return entityTypes.contains(relationship.getToVertex().getTypeDefName()) ? relationship.getToVertex() : relationship.getFromVertex();
    }

}
