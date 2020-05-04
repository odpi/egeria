/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Static mapping methods to map between the LineType and Relationship type.
 */
public class LineTypeMapper {
    private static final Logger log = LoggerFactory.getLogger( LineTypeMapper.class);
    private static final String className = LineTypeMapper.class.getName();

    /**
     * Map lineType to relationship type guid.
     *
     * The lineType is the type of node that is exposed in the lineType API. The subject Area OMAs needs to convert
     * the node type into a guid of an relationship type so it can be used to call omrs.
     * @param lineType lineType this is the type of node that is exposed in the nodetype API
     * @return relationship Type guid.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    static public String  mapLineTypeToRelationshipTypeGuid(LineType lineType) throws InvalidParameterException {
        String relationshipTypeName = lineType.name();
        if (lineType.equals(LineType.Hasa)) {
            relationshipTypeName ="TermHASARelationship";
        } else if (lineType.equals(LineType.Isa)) {
            relationshipTypeName ="ISARelationship";
        } else if (lineType.equals(LineType.IsaTypeOf)) {
            relationshipTypeName ="TermISATypeOFRelationship";
        } else if (lineType.equals(LineType.TypedBy)) {
            relationshipTypeName ="TermTYPEDBYRelationship";
        }

        return OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName(relationshipTypeName).getGUID();
    }

    /**
     * convert an relationship type guid to a LineType
     * @param guid relationship Type guid.
     * @return LineType nodetype
     */
    public static LineType mapRelationshipTypeGuidToLineType(String guid) {
        String lineTypeName = OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByGuid(guid).getName();

        LineType lineType = null;
        for (LineType lineTypeToCheck : LineType.values()) {
            if (lineTypeToCheck.name().equals(lineTypeName)) {
                lineType = lineTypeToCheck;
                break;
            }
        }
        //TODO deal with nodetypes that are actually classifications in OMRS.
        return lineType;
    }
}
