/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Static mapping methods to map between the omas relationship Type and omrs relationship type.
 */
public class RelationshipTypeMapper {
    private static final Logger log = LoggerFactory.getLogger( RelationshipTypeMapper.class);
    private static final String className = RelationshipTypeMapper.class.getName();

    /**
     * Map omas RelationshipType to omrs relationship type guid.
     *
     * The omasRelationshipType is the type of node that is exposed in the omasRelationshipType API. The subject Area OMAS needs to convert
     * the node type into a guid of an relationship type so it can be used to call omrs.
     * @param omasRelationshipType omasRelationshipType this is the type of node that is exposed in the nodetype API
     * @return relationship Type guid.
     */
    static public String mapOMASRelationshipTypeToOMRSRelationshipTypeGuid(RelationshipType omasRelationshipType) {
        String relationshipTypeName = omasRelationshipType.name();
        if (omasRelationshipType.equals(RelationshipType.HasA)) {
            relationshipTypeName ="TermHASARelationship";
        } else if (omasRelationshipType.equals(RelationshipType.IsA)) {
            relationshipTypeName ="ISARelationship";
        } else if (omasRelationshipType.equals(RelationshipType.IsATypeOf)) {
            relationshipTypeName ="TermISATypeOFRelationship";
        } else if (omasRelationshipType.equals(RelationshipType.TypedBy)) {
            relationshipTypeName ="TermTYPEDBYRelationship";
        }

        return OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName(relationshipTypeName).getGUID();
    }
}
