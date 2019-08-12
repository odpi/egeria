/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.OpenLineageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

public class GraphRelationshipMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphRelationshipMapper.class);


    public void mapRelationshipToEdge(RelationshipEvent relationship, Edge edge) {
        final String methodName = "mapRelationshipToEdge";

        // Some properties are mandatory. If any of these are null then throw exception

        if (relationship.getGUID() != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_GUID, relationship.getGUID());
        else {
            log.error("{} relationship is missing a core attribute {}", methodName, "GUID");
            OpenLineageErrorCode errorCode = OpenLineageErrorCode.RELATIONSHIP_PROPERTIES_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                    this.getClass().getName());
            throw new OpenLineageException(400,
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }



        edge.property(PROPERTY_KEY_RELATIONSHIP_VERSION, relationship.getVersion());

        // Other properties can be removed if set to null
        if (relationship.getCreateTime() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_CREATE_TIME, relationship.getCreateTime());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_CREATE_TIME);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getUpdatedBy() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY, relationship.getUpdatedBy());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getUpdateTime() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME, relationship.getUpdateTime());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME);
            if (ep != null)
                ep.remove();
        }


            edge.property("version", relationship.getVersion());

    }

}
