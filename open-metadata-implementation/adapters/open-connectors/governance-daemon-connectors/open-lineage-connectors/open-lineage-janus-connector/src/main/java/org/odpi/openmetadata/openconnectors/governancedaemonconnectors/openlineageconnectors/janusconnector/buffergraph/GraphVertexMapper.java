///* SPDX-License-Identifier: Apache-2.0 */
///* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class GraphVertexMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphVertexMapper.class);


    public void mapEntityToVertex(LineageEntity lineageEntity, Vertex vertex) throws JanusConnectorException {

        mapEntitySummaryToVertex(lineageEntity, vertex);

        Map<String,String> instanceProperties = lineageEntity.getProperties();
        if (instanceProperties != null) {

            for(Map.Entry<String,String> entry: instanceProperties.entrySet()){
                String key = "veprop"+entry.getKey();
                //check if a properrty already exist foe example create time is already there and then it is failing
//                if (entry.getValue().equals("createTime")){ continue;}
                vertex.property(key,entry.getValue());
            }
        }

    }



    public void mapEntitySummaryToVertex(LineageEntity lineageEntity, Vertex vertex) throws JanusConnectorException {
        String methodName = "mapEntitySummaryToVertex";

        // Some properties are mandatory. If any of these are null then throw exception
        boolean missingAttribute = false;
        String  missingAttributeName = null;

        if (lineageEntity.getGuid() != null)
            vertex.property(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        else {
            log.debug("{} missing attribute: guid", methodName);
            missingAttribute = true;
            missingAttributeName = "guid";
        }

        String instanceType = lineageEntity.getTypeDefName();
        if (instanceType != null)                               // ** name mapping
            vertex.property(PROPERTY_KEY_ENTITY_NAME, instanceType);
        else {
            log.debug("{} missing attribute: type name", methodName);
            missingAttribute = true;
            missingAttributeName = "type or typeDefName";
        }


        if (missingAttribute) {
            log.error("{} entity is missing core attribute {}", methodName, missingAttributeName);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.ENTITY_PROPERTIES_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(lineageEntity.getGuid(),
                    methodName,
                    this.getClass().getName());

            throw new JanusConnectorException(
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

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
