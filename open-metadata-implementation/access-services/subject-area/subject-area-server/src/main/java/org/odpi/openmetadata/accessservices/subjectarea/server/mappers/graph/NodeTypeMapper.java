/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;


/**
 * Static mapping methods to map between the node type and the Entity Type
 */
public class NodeTypeMapper {

    /**
     * Map nodeType to entity type guid.
     *
     * The nodeType is the type of node that is exposed in the nodeType API. The subject Area OMAs needs to convert
     * the node type into a guid of an entity type so it can be used to call omrs.
     *
     * The supplied NodeType is mapped to an EntityType name :
     * <ul>
     * <li> The NodeType name can match the EntityType name - e.g. Glossary </li>
     * <li> The NodeType name may need to be renamed to the appropriate entity type  e.g. Term </li>
     * <li> The NodeType name may map to en entity type with a classification  e.g. Taxonomy </li>
     * </ul>
     * @param nodeType nodeType this is the type of node that is exposed in the nodetype API
     * @return entity Type guid.
     */
    static public String  mapNodeTypeToEntityTypeGuid(NodeType nodeType) {
        String entityTypeName = nodeType.name();
        if (nodeType.name().equals("Term") ||nodeType.name().equals("Activity")) {
            entityTypeName = "GlossaryTerm";
        } else if (nodeType.name().equals("Category") || nodeType.name().equals("SubjectAreaDefinition")) {
            entityTypeName = "GlossaryCategory";
        } else if (nodeType.name().equals("Taxonomy") || nodeType.name().equals("CanonicalGlossary") || nodeType.name().equals("TaxonomyAndCanonicalGlossary")) {
              entityTypeName = "Glossary";
        } else if (nodeType.name().equals("GlossaryProject")) {
              entityTypeName = "Project";
        }
        return OpenMetadataTypesArchiveAccessor.getInstance().getEntityDefByName(entityTypeName).getGUID();
    }

    /**
     * convert an entity type guid to a NodeType
     * @param guid entity Type guid.
     * @return NodeType nodetype
     */
    public static NodeType mapEntityTypeGuidToNodeType(String guid) {
        String nodeTypeName = OpenMetadataTypesArchiveAccessor.getInstance().getEntityDefByGuid(guid).getName();
        if (nodeTypeName.equals("GlossaryTerm")) {
            nodeTypeName = "Term";
        }
        if (nodeTypeName.equals("GlossaryCategory")) {
            nodeTypeName = "Category";
        }
        NodeType nodeType = null;
        for (NodeType nodeTypeToCheck : NodeType.values()) {
            if (nodeTypeToCheck.name().equals(nodeTypeName)) {
                nodeType = nodeTypeToCheck;
                break;
            }
        }
        //TODO deal with nodetypes that are actually classifications in OMRS.
        return nodeType;
    }
}
