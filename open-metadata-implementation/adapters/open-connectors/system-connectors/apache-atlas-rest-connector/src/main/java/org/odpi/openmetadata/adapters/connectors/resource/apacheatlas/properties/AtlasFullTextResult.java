/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasFullTextResult returns search for entity results with match score.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasFullTextResult
{
    private AtlasEntityHeader entity = null;
    private Double            score = null;


    public AtlasFullTextResult()
    {
    }


    public AtlasEntityHeader getEntity()
    {
        return entity;
    }


    public void setEntity(AtlasEntityHeader entity)
    {
        this.entity = entity;
    }


    public Double getScore()
    {
        return score;
    }


    public void setScore(Double score)
    {
        this.score = score;
    }


    @Override
    public String toString()
    {
        return "AtlasFullTextResult{" +
                       "entity=" + entity +
                       ", score=" + score +
                       '}';
    }
}
