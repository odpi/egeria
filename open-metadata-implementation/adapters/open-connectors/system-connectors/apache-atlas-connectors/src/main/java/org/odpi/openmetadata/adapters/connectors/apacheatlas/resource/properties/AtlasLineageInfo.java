/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasLineageInfo captures lineage for an entity instance like hive_table.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasLineageInfo
{
    private String                                       baseEntityGuid         = null;
    private AtlasLineageDirection                        lineageDirection       = null;
    private int                                          lineageDepth           = 0;
    private Map<String, AtlasEntityHeader>               guidEntityMap          = null;
    private Set<AtlasLineageRelationship>                relations              = null;
    private Set<String>                                  visitedEdges           = null;
    private Map<String, AtlasLineageInfoOnDemand>        relationsOnDemand      = null;
    private Map<String, AtlasLineageOnDemandConstraints> lineageOnDemandPayload = null;


    public AtlasLineageInfo()
    {
    }


    public String getBaseEntityGuid()
    {
        return baseEntityGuid;
    }


    public void setBaseEntityGuid(String baseEntityGuid)
    {
        this.baseEntityGuid = baseEntityGuid;
    }


    public AtlasLineageDirection getLineageDirection()
    {
        return lineageDirection;
    }


    public void setLineageDirection(AtlasLineageDirection lineageDirection)
    {
        this.lineageDirection = lineageDirection;
    }


    public int getLineageDepth()
    {
        return lineageDepth;
    }


    public void setLineageDepth(int lineageDepth)
    {
        this.lineageDepth = lineageDepth;
    }


    public Map<String, AtlasEntityHeader> getGuidEntityMap()
    {
        return guidEntityMap;
    }


    public void setGuidEntityMap(Map<String, AtlasEntityHeader> guidEntityMap)
    {
        this.guidEntityMap = guidEntityMap;
    }


    public Set<AtlasLineageRelationship> getRelations()
    {
        return relations;
    }


    public void setRelations(Set<AtlasLineageRelationship> relations)
    {
        this.relations = relations;
    }


    public Set<String> getVisitedEdges()
    {
        return visitedEdges;
    }


    public void setVisitedEdges(Set<String> visitedEdges)
    {
        this.visitedEdges = visitedEdges;
    }


    public Map<String, AtlasLineageInfoOnDemand> getRelationsOnDemand()
    {
        return relationsOnDemand;
    }


    public void setRelationsOnDemand(Map<String, AtlasLineageInfoOnDemand> relationsOnDemand)
    {
        this.relationsOnDemand = relationsOnDemand;
    }


    public Map<String, AtlasLineageOnDemandConstraints> getLineageOnDemandPayload()
    {
        return lineageOnDemandPayload;
    }


    public void setLineageOnDemandPayload(Map<String, AtlasLineageOnDemandConstraints> lineageOnDemandPayload)
    {
        this.lineageOnDemandPayload = lineageOnDemandPayload;
    }


    @Override
    public String toString()
    {
        return "AtlasLineageInfo{" +
                       "baseEntityGuid='" + baseEntityGuid + '\'' +
                       ", lineageDirection=" + lineageDirection +
                       ", lineageDepth=" + lineageDepth +
                       ", guidEntityMap=" + guidEntityMap +
                       ", relations=" + relations +
                       ", visitedEdges=" + visitedEdges +
                       ", relationsOnDemand=" + relationsOnDemand +
                       ", lineageOnDemandPayload=" + lineageOnDemandPayload +
                       '}';
    }
}
