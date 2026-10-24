/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * The boundaries between the layers of lineage that the Darwin Product Dependency Manager works across.  Lineage is
 * built up in layers - schema detail, the data fabric, the system fabric and the data mesh - and a lineage path in a
 * lower layer between elements with different owners proves a relationship between those owners in the layer above.
 * Each boundary says which element types make up its upper layer, which relationship types make up the paths in its
 * lower layer, which relationship type makes elements equivalent (a data set and the content it is a view over), which
 * relationship type each one proves in the upper layer, which types form paths of their own, and how far a path may go.  The owner function of
 * each boundary is supplied by the connector, since it needs the repository.
 * <p>
 * See docs/darwin-layered-lineage.md for the design.
 */
enum LineageBoundary
{
    /**
     * DataMapping relationships between schema attributes, and between schema attributes and processes, prove
     * DataFlow relationships between the data assets that own the schema attributes and the processes.  Only a
     * direct mapping counts: a path does not pass through elements that belong to no data asset.
     */
    SCHEMA_DETAIL_TO_DATA_FABRIC("schema detail to data fabric",
                                 List.of(OpenMetadataType.DATA_ASSET.typeName,
                                         OpenMetadataType.PROCESS.typeName),
                                 List.of(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName),
                                 null,
                                 null,
                                 OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                 Map.of(),
                                 List.of(),
                                 1,
                                 false),

    /**
     * The lineage between data fabric elements proves relationships between the IT infrastructure whose software
     * capabilities use them.  A path may pass through any number of elements that no infrastructure uses.  The
     * relationship that reaches the infrastructure's element decides the type proved: a ProcessCall proves a
     * ProcessCall, and any other data lineage a DataFlow.  ControlFlow relationships form paths of their own, and
     * prove ControlFlow relationships.  The DataMapping relationships between a table and a process are followed too
     * (see tableMappingRelationshipTypeName).
     */
    DATA_FABRIC_TO_SYSTEM_FABRIC("data fabric to system fabric",
                                 List.of(OpenMetadataType.IT_INFRASTRUCTURE.typeName),
                                 List.of(OpenMetadataType.DATA_LINEAGE_RELATIONSHIP.typeName,
                                         OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName),
                                 OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                                 OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                 OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                 Map.of(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName, OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                        OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName),
                                 List.of(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName),
                                 0,
                                 false),

    /**
     * The data lineage between data fabric elements proves DigitalProductDependency relationships between the
     * digital products they are members of.  A path may pass through any number of elements that belong to no
     * product.  ControlFlow is not followed: a product depends on another product's data, not on the order in which
     * their processes run.  The DataMapping relationships between a table and a process are followed too (see
     * tableMappingRelationshipTypeName).  The dependent product - the one the data reaches - is at end 1.
     */
    DATA_FABRIC_TO_DATA_MESH("data fabric to data mesh",
                             List.of(OpenMetadataType.DIGITAL_PRODUCT.typeName),
                             List.of(OpenMetadataType.DATA_LINEAGE_RELATIONSHIP.typeName),
                             OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                             OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                             OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                             Map.of(),
                             List.of(),
                             0,
                             true);


    final String              displayName;
    final List<String>        upperLayerElementTypeNames;
    final List<String>        lowerLayerRelationshipTypeNames;
    final String              tableMappingRelationshipTypeName;
    final String              equivalenceRelationshipTypeName;
    final String              defaultUpperTypeName;
    final Map<String, String> upperTypeNameOverrides;
    final List<String>        separatePathTypeNames;
    final int                 maxSteps;
    final boolean             consumerAtEnd1;


    /**
     * Constructor.
     *
     * @param displayName name of the boundary for messages
     * @param upperLayerElementTypeNames the element types (with their subtypes) that make up the upper layer - a
     *                                   relationship belongs to the upper layer when both of its ends are of these types
     * @param lowerLayerRelationshipTypeNames the relationship types (with their subtypes) that make up a path in the lower layer
     * @param tableMappingRelationshipTypeName a relationship type from the layer beneath that is also followed when it
     *                                         runs between a table and a process - null for none.  A data set over a
     *                                         table is equivalent to the table, and a table's lineage to and from its
     *                                         processes is recorded at the level of the table rather than its asset.
     * @param equivalenceRelationshipTypeName the relationship type whose ends a walk treats as the same element - null for none
     * @param defaultUpperTypeName the relationship type proved in the upper layer
     * @param upperTypeNameOverrides the lower-layer relationship types that prove a different upper-layer type from the
     *                               default, mapped to that type
     * @param separatePathTypeNames the lower-layer relationship types that only join paths of their own type; all the
     *                              other types join each other's paths
     * @param maxSteps the number of relationships a path may follow - zero means the connector's maxLineageDepth,
     *                 and only paths cut short by that are reported as abandoned
     * @param consumerAtEnd1 true if the derived relationship has the owner the data reaches at end 1; false if it has
     *                       the owner the data comes from at end 1
     */
    LineageBoundary(String              displayName,
                    List<String>        upperLayerElementTypeNames,
                    List<String>        lowerLayerRelationshipTypeNames,
                    String              tableMappingRelationshipTypeName,
                    String              equivalenceRelationshipTypeName,
                    String              defaultUpperTypeName,
                    Map<String, String> upperTypeNameOverrides,
                    List<String>        separatePathTypeNames,
                    int                 maxSteps,
                    boolean             consumerAtEnd1)
    {
        this.displayName                      = displayName;
        this.upperLayerElementTypeNames       = upperLayerElementTypeNames;
        this.lowerLayerRelationshipTypeNames  = lowerLayerRelationshipTypeNames;
        this.tableMappingRelationshipTypeName = tableMappingRelationshipTypeName;
        this.equivalenceRelationshipTypeName  = equivalenceRelationshipTypeName;
        this.defaultUpperTypeName             = defaultUpperTypeName;
        this.upperTypeNameOverrides           = upperTypeNameOverrides;
        this.separatePathTypeNames            = separatePathTypeNames;
        this.maxSteps                         = maxSteps;
        this.consumerAtEnd1                   = consumerAtEnd1;
    }


    /**
     * Return the relationship type that a lower-layer relationship proves in the upper layer, when it is the one
     * that reaches an owned element.
     *
     * @param lowerTypeName type of the lower-layer relationship
     * @return upper-layer relationship type
     */
    String getUpperTypeName(String lowerTypeName)
    {
        return upperTypeNameOverrides.getOrDefault(lowerTypeName, defaultUpperTypeName);
    }


    /**
     * Return the family of paths that a lower-layer relationship can join.  Relationships of a separate path type
     * only join paths of their own type; every other relationship joins the shared family, which is null.
     *
     * @param lowerTypeName type of the lower-layer relationship
     * @return path family - null for the shared family
     */
    String getPathFamily(String lowerTypeName)
    {
        if (separatePathTypeNames.contains(lowerTypeName))
        {
            return lowerTypeName;
        }

        return null;
    }
}
