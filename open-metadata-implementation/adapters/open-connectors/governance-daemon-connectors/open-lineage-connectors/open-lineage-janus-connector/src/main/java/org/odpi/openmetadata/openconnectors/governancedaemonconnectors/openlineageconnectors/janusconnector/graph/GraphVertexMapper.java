///* SPDX-License-Identifier: Apache-2.0 */
///* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;

import java.util.Map;
import java.util.Objects;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_CREATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_CREATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_UPDATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_UPDATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_VERSION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;

public class GraphVertexMapper {

    public void mapEntityToVertex(LineageEntity lineageEntity, Vertex vertex){

        mapEntitySummaryToVertex(lineageEntity, vertex);
        Map<String,String> instanceProperties = lineageEntity.getProperties();
        if (instanceProperties != null) {
            instanceProperties.entrySet().stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .forEach(e -> vertex.property(PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + e.getKey(), e.getValue()));
        }
    }

    public void mapEntitySummaryToVertex(LineageEntity lineageEntity, Vertex vertex){

        vertex.property(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        vertex.property(PROPERTY_KEY_LABEL, lineageEntity.getTypeDefName());
        vertex.property(PROPERTY_KEY_ENTITY_VERSION, lineageEntity.getVersion());

        if (lineageEntity.getCreatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATED_BY, lineageEntity.getCreatedBy());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_CREATED_BY);
        }

        if (lineageEntity.getCreateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATE_TIME, lineageEntity.getCreateTime());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_CREATE_TIME);
        }

        if (lineageEntity.getUpdatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATED_BY, lineageEntity.getUpdatedBy());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_UPDATED_BY);
        }

        if (lineageEntity.getUpdateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATE_TIME, lineageEntity.getUpdateTime());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_UPDATE_TIME);
        }
    }

    private void removeProperty(Vertex vertex, String qualifiedPropName) {
        // no value has been specified - remove the property from the vertex
        VertexProperty vp = vertex.property(qualifiedPropName);
        if (vp != null) {
            vp.remove();
        }
    }

}
