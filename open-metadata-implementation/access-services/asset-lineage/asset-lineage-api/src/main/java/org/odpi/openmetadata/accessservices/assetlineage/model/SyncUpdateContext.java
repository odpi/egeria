/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;


import java.util.Objects;
import java.util.Set;

/**
 * Contains the context for syncing the relationships of a node after an update
 * */
public class SyncUpdateContext {

    private String entityGuid;
    private Set<String> neighboursGUID;

    public String getEntityGuid() {
        return entityGuid;
    }

    public void setEntityGuid(String entityGuid) {
        this.entityGuid = entityGuid;
    }

    public Set<String> getNeighboursGUID() {
        return neighboursGUID;
    }

    public void setNeighboursGUID(Set<String> neighboursGUID) {
        this.neighboursGUID = neighboursGUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyncUpdateContext that = (SyncUpdateContext) o;
        return Objects.equals(entityGuid, that.entityGuid) && Objects.equals(neighboursGUID, that.neighboursGUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityGuid, neighboursGUID);
    }
}
