/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasLineageOnDemandConstraints provides constraints for lineage on demand.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasLineageOnDemandConstraints
{
    private AtlasLineageDirection direction = null;
    private int              inputRelationsLimit = 0;
    private int              outputRelationsLimit = 0;
    private int              depth = 0;


    public AtlasLineageOnDemandConstraints()
    {
    }


    public AtlasLineageDirection getDirection()
    {
        return direction;
    }


    public void setDirection(AtlasLineageDirection direction)
    {
        this.direction = direction;
    }


    public int getInputRelationsLimit()
    {
        return inputRelationsLimit;
    }


    public void setInputRelationsLimit(int inputRelationsLimit)
    {
        this.inputRelationsLimit = inputRelationsLimit;
    }


    public int getOutputRelationsLimit()
    {
        return outputRelationsLimit;
    }


    public void setOutputRelationsLimit(int outputRelationsLimit)
    {
        this.outputRelationsLimit = outputRelationsLimit;
    }


    public int getDepth()
    {
        return depth;
    }


    public void setDepth(int depth)
    {
        this.depth = depth;
    }


    @Override
    public String toString()
    {
        return "AtlasLineageOnDemandConstraints{" +
                       "direction=" + direction +
                       ", inputRelationsLimit=" + inputRelationsLimit +
                       ", outputRelationsLimit=" + outputRelationsLimit +
                       ", depth=" + depth +
                       '}';
    }
}
