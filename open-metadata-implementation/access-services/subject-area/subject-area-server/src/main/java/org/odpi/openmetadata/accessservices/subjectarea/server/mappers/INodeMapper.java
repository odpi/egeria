/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 * Interface for mapping between a Subject Area OMAS Node to an OMRS EntityDetail
 */
public interface INodeMapper<N extends Node> extends Mapper<EntityDetail, N> {
    /**
     * Map from an OMRS EntityDetail to a Subject Area OMAS Node
     * @param entityDetail OMRS EntityDetail
     * @return Subject Area OMAS Node
     */
    N map(EntityDetail entityDetail);

    /**
     * Map from a Subject Area OMAS Node to an OMRS EntityDetail
     * @param node a Subject Area OMAS Node
     * @return  an OMRS EntityDetail
     */
     EntityDetail map(N node) throws InvalidParameterException;
}
