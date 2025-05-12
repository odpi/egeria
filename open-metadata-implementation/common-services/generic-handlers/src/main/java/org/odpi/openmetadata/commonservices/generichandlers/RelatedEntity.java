/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

/**
 * Defines a relationship and the entity at the other end of the relationship to a starting entity.
 *
 * @param relationship relationship providing the linkage
 * @param entityDetail entity at the far end
 */
public record RelatedEntity(Relationship relationship,
                            EntityDetail entityDetail)
{
}
