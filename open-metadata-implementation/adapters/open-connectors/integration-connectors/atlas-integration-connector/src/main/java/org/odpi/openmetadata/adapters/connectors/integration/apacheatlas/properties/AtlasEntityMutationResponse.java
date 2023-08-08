/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import java.util.List;
import java.util.Map;

/**
 * AtlasEntityMutationResponse returns information of new/updated entities
 */
public class AtlasEntityMutationResponse
{
    private Map<AtlasEntityOperation, List<AtlasEntityHeader>> mutatedEntities;
    private Map<String, String>                                guidAssignments;


    public AtlasEntityMutationResponse()
    {
    }


    public Map<AtlasEntityOperation, List<AtlasEntityHeader>> getMutatedEntities()
    {
        return mutatedEntities;
    }


    public void setMutatedEntities(
            Map<AtlasEntityOperation, List<AtlasEntityHeader>> mutatedEntities)
    {
        this.mutatedEntities = mutatedEntities;
    }


    public Map<String, String> getGuidAssignments()
    {
        return guidAssignments;
    }


    public void setGuidAssignments(Map<String, String> guidAssignments)
    {
        this.guidAssignments = guidAssignments;
    }


    @Override
    public String toString()
    {
        return "AtlasEntityMutationResponse{" +
                       "mutatedEntities=" + mutatedEntities +
                       ", guidAssignments=" + guidAssignments +
                       '}';
    }
}
