/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import java.util.Map;

/**
 * AtlasEntityExtInfo contains a map of GUIDs to entity instances.
 */
public class AtlasEntityExtInfo
{
    private Map<String, AtlasEntity> referredEntities;


    public AtlasEntityExtInfo()
    {
    }


    public Map<String, AtlasEntity> getReferredEntities()
    {
        return referredEntities;
    }


    public void setReferredEntities(
            Map<String, AtlasEntity> referredEntities)
    {
        this.referredEntities = referredEntities;
    }


    @Override
    public String toString()
    {
        return "AtlasEntityExtInfo{" +
                       "referredEntities=" + referredEntities +
                       '}';
    }
}
