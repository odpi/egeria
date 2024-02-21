/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasClassification describes an Atlas classification tag.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasClassification extends AtlasStruct
{
    private String                  entityGuid                       = null;
    private AtlasInstanceStatus     entityStatus                     = AtlasInstanceStatus.ACTIVE;
    private boolean                 propagate                        = false;
    private List<AtlasTimeBoundary> validityPeriods                  = null;
    private Boolean                 removePropagationsOnEntityDelete = null;


    public AtlasClassification()
    {
    }


    public String getEntityGuid()
    {
        return entityGuid;
    }


    public void setEntityGuid(String entityGuid)
    {
        this.entityGuid = entityGuid;
    }


    public AtlasInstanceStatus getEntityStatus()
    {
        return entityStatus;
    }


    public void setEntityStatus(AtlasInstanceStatus entityStatus)
    {
        this.entityStatus = entityStatus;
    }


    public boolean getPropagate()
    {
        return propagate;
    }


    public void setPropagate(boolean propagate)
    {
        this.propagate = propagate;
    }


    public List<AtlasTimeBoundary> getValidityPeriods()
    {
        return validityPeriods;
    }


    public void setValidityPeriods(List<AtlasTimeBoundary> validityPeriods)
    {
        this.validityPeriods = validityPeriods;
    }


    public Boolean getRemovePropagationsOnEntityDelete()
    {
        return removePropagationsOnEntityDelete;
    }


    public void setRemovePropagationsOnEntityDelete(Boolean removePropagationsOnEntityDelete)
    {
        this.removePropagationsOnEntityDelete = removePropagationsOnEntityDelete;
    }


    @Override
    public String toString()
    {
        return "AtlasClassification{" +
                       "entityGuid='" + entityGuid + '\'' +
                       ", entityStatus=" + entityStatus +
                       ", propagate=" + propagate +
                       ", validityPeriods=" + validityPeriods +
                       ", removePropagationsOnEntityDelete=" + removePropagationsOnEntityDelete +
                       ", typeName='" + getTypeName() + '\'' +
                       ", attributes=" + getAttributes() +
                       '}';
    }
}
