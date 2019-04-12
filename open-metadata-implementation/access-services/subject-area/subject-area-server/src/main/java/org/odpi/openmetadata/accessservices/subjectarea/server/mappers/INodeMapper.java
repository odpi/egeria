/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 * Interface for mapping between a Subject Area OMAS Node to an OMRS EntityDetail
 */
public interface INodeMapper
{
    /**
     * Map from an OMRS EntityDetail to a Subject Area OMAS Node
     * @param entityDetail OMRS EntityDetail
     * @return Subject Area OMAS Node
     * @throws InvalidParameterException a supplied parameter was null or invalid.
     */
    Node mapEntityDetailToNode(EntityDetail entityDetail) throws InvalidParameterException;

    /**
     * Map from a Subject Area OMAS Node to an OMRS EntityDetail
     * @param node a Subject Area OMAS Node
     * @return  an OMRS EntityDetail
     * @throws InvalidParameterException a supplied parameter was null or invalid.
     */
     EntityDetail mapNodeToEntityDetail(Node node) throws InvalidParameterException;
}
