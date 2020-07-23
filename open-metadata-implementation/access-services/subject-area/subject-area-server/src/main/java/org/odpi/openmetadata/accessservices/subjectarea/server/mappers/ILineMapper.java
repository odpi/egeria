/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

/**
 * Interface for mapping between a Subject Area OMAS Line to an OMRS Relationship
 */
public interface ILineMapper<L extends Line> extends Mapper<Relationship, L> {
    /**
     * Map from an OMRS Relationship to a Subject Area OMAS Line
     *
     * @param relationship OMRS Lines
     * @return Subject Area OMAS Line
     */
    L map(Relationship relationship);

    /**
     * Map from a Subject Area OMAS Line to an OMRS Relationship
     *
     * @param line a Subject Area OMAS Line
     * @return an OMRS Relationship
     */
    Relationship map(L line);
}
