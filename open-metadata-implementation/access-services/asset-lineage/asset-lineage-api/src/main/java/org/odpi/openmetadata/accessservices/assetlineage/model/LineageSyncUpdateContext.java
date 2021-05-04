/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
/**
 *
 * SyncUpdateContext contains the context for syncing the relationships of a node after an update.
 *
 */
public class LineageSyncUpdateContext {

    /**
     * The list GUIDs for the items published after lineage processing activity completed.
     * -- GETTER --
     * Get the GUID of the entity that was updated
     * @return the GUID of the entity that was updated
     * -- SETTER --
     * Set the entity that was updated.
     * @param entityGUID of the entity that was updated
     */
    private String entityGUID;
    /**
     * The list GUIDs of the nodes that have a direct relationship to the entityGUID
     * -- GETTER --
     * Get the list of GUIDs of nodes directly connected to the entity
     * @return the list of GUIDs of nodes directly connected to the entity
     * -- SETTER --
     * Set the list of neighbours
     * @param neighboursGUID list of node related to the entity
     */
    private Set<String> neighboursGUID;

}
