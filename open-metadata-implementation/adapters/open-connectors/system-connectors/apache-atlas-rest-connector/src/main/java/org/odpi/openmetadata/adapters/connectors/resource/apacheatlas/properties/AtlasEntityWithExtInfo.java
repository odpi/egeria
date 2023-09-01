/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

/**
 * AtlasEntityWithExtInfo describes an entity with additional entity elements.
 */
public class AtlasEntityWithExtInfo extends AtlasEntityExtInfo
{
    private AtlasEntity entity = null;


    public AtlasEntityWithExtInfo()
    {
    }


    public AtlasEntity getEntity()
    {
        return entity;
    }


    public void setEntity(AtlasEntity entity)
    {
        this.entity = entity;
    }


    @Override
    public String toString()
    {
        return "AtlasEntityWithExtInfo{" +
                       "entity=" + entity +
                       ", referredEntities=" + getReferredEntities() +
                       '}';
    }
}
