/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;

/**
 * Interface for mapping between a Subject Area OMAS Relationship to an OMRS Relationship
 */
public interface IRelationshipMapper<R extends Relationship> extends Mapper<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship, R> {
    /**
     * Map from an OMRS Relationship to a Subject Area OMAS relationship
     *
     * @param relationship OMRS relationships
     * @return Subject Area OMAS relationship
     */
    R map(org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship);

    /**
     * Map from a Subject Area OMAS relationship to an OMRS Relationship
     *
     * @param relationship a Subject Area OMAS relationship
     * @return an OMRS Relationship
     */
    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship map(R relationship);
}
