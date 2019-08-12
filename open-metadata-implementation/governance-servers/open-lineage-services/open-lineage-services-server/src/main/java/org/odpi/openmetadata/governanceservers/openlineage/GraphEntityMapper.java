/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetLineageEntityEvent;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.OpenLineageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;

public class GraphEntityMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphEntityMapper.class);


    public void  mapEntityToVertex(AssetLineageEntityEvent entity, Vertex vertex)throws OpenLineageException{
        final String methodName = "mapEntityDetailToVertex";

        mapEntitySummaryToVertex(entity, vertex);

        Map<String,Object> instanceProperties = entity.getProperties();
        if (instanceProperties != null) {

            //TODO check which is the best way for InstanceProperties either seperate values or as one String and create properties with prefix in GraphConstants
            for(Map.Entry<String,Object> entry: instanceProperties.entrySet()){

                vertex.property(entry.getKey(),entry.getValue());
            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonString;
//            try {
//                jsonString = objectMapper.writeValueAsString(instanceProperties);
//                log.debug("{} entity has serialized properties {}", methodName, jsonString);
//                vertex.property("instanceProperties", jsonString);
//            } catch (Throwable exc) {
//                log.error("{} Caught exception from entity mapper", methodName);
//                OpenLineageErrorCode errorCode = OpenLineageErrorCode.ENTITY_PROPERTIES_ERROR;
//                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
//                        this.getClass().getName());
//                throw new OpenLineageException(400,
//                        this.getClass().getName(),
//                        methodName,
//                        errorMessage,
//                        errorCode.getSystemAction(),
//                        errorCode.getUserAction());
//            }

        }

    }



    public void mapEntitySummaryToVertex(AssetLineageEntityEvent entity, Vertex vertex)
            throws OpenLineageException
    {
        String methodName = "mapEntitySummaryToVertex";


        // Some properties are mandatory. If any of these are null then throw exception
        boolean missingAttribute = false;
        String  missingAttributeName = null;

        if (entity.getGUID() != null)
            vertex.property(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());
        else {
            log.debug("{} missing attribute: guid", methodName);
            missingAttribute = true;
            missingAttributeName = "guid";
        }

        String instanceType = entity.getTypeDefName();
        if (instanceType != null)                               // ** name mapping
            vertex.property(PROPERTY_KEY_ENTITY_NAME, instanceType);
        else {
            log.debug("{} missing attribute: type name", methodName);
            missingAttribute = true;
            missingAttributeName = "type or typeDefName";
        }


        if (missingAttribute) {
            log.error("{} entity is missing core attribute {}", methodName, missingAttributeName);
            OpenLineageErrorCode errorCode = OpenLineageErrorCode.ENTITY_PROPERTIES_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                    this.getClass().getName());
            throw new OpenLineageException(400,
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        vertex.property(PROPERTY_KEY_ENTITY_VERSION, entity.getVersion());

        if (entity.getCreatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATED_BY, entity.getCreatedBy());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_CREATED_BY);
        }

        if (entity.getCreateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATE_TIME, entity.getCreateTime());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_CREATE_TIME);
        }

        if (entity.getUpdatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATED_BY, entity.getUpdatedBy());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_UPDATED_BY);
        }

        if (entity.getUpdateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATE_TIME, entity.getUpdateTime());
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
