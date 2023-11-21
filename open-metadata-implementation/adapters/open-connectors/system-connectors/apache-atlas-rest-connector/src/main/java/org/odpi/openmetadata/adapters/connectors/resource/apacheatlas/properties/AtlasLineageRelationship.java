/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasLineageRelationship describes a lineage relationship between two entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasLineageRelationship
{
    private String fromEntityId = null;
    private String toEntityId = null;
    private String relationshipId = null;


    public AtlasLineageRelationship()
    {
    }


    public String getFromEntityId()
    {
        return fromEntityId;
    }


    public void setFromEntityId(String fromEntityId)
    {
        this.fromEntityId = fromEntityId;
    }


    public String getToEntityId()
    {
        return toEntityId;
    }


    public void setToEntityId(String toEntityId)
    {
        this.toEntityId = toEntityId;
    }


    public String getRelationshipId()
    {
        return relationshipId;
    }


    public void setRelationshipId(String relationshipId)
    {
        this.relationshipId = relationshipId;
    }


    @Override
    public String toString()
    {
        return "AtlasLineageRelationship{" +
                       "fromEntityId='" + fromEntityId + '\'' +
                       ", toEntityId='" + toEntityId + '\'' +
                       ", relationshipId='" + relationshipId + '\'' +
                       '}';
    }
}
