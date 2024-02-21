/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelationshipHeader contains a summary of a relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelationshipHeader extends AtlasStruct
{
    private String              guid          = null;
    private AtlasInstanceStatus status        = AtlasInstanceStatus.ACTIVE;
    private AtlasPropagateTags  propagateTags = AtlasPropagateTags.NONE;
    private String              label         = null;
    private AtlasObjectId       end1          = null;
    private AtlasObjectId       end2          = null;


    public AtlasRelationshipHeader()
    {
    }


    public String getGuid()
    {
        return guid;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public AtlasInstanceStatus getStatus()
    {
        return status;
    }


    public void setStatus(AtlasInstanceStatus status)
    {
        this.status = status;
    }


    public AtlasPropagateTags getPropagateTags()
    {
        return propagateTags;
    }


    public void setPropagateTags(AtlasPropagateTags propagateTags)
    {
        this.propagateTags = propagateTags;
    }


    public String getLabel()
    {
        return label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public AtlasObjectId getEnd1()
    {
        return end1;
    }


    public void setEnd1(AtlasObjectId end1)
    {
        this.end1 = end1;
    }


    public AtlasObjectId getEnd2()
    {
        return end2;
    }


    public void setEnd2(AtlasObjectId end2)
    {
        this.end2 = end2;
    }


    @Override
    public String toString()
    {
        return "AtlasRelationshipHeader{" +
                       "guid='" + guid + '\'' +
                       ", status=" + status +
                       ", propagateTags=" + propagateTags +
                       ", label='" + label + '\'' +
                       ", end1=" + end1 +
                       ", end2=" + end2 +
                       ", typeName='" + getTypeName() + '\'' +
                       ", attributes=" + getAttributes() +
                       '}';
    }
}
