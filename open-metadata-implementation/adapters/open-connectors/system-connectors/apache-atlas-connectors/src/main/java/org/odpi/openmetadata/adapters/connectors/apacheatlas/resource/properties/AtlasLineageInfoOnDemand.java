/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasLineageInfoOnDemand controls dynamic lineage queries.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasLineageInfoOnDemand
{
    private boolean                         hasMoreInputs                 = false;
    private boolean                         hasMoreOutputs                = false;
    private int                             inputRelationsCount           = 0;
    private int                             outputRelationsCount          = 0;
    private boolean                         isInputRelationsReachedLimit  = false;
    private boolean                         isOutputRelationsReachedLimit = false;
    private AtlasLineageOnDemandConstraints onDemandConstraints           = null;


    public AtlasLineageInfoOnDemand()
    {
    }


    public boolean getHasMoreInputs()
    {
        return hasMoreInputs;
    }


    public void setHasMoreInputs(boolean hasMoreInputs)
    {
        this.hasMoreInputs = hasMoreInputs;
    }


    public boolean getHasMoreOutputs()
    {
        return hasMoreOutputs;
    }


    public void setHasMoreOutputs(boolean hasMoreOutputs)
    {
        this.hasMoreOutputs = hasMoreOutputs;
    }


    public int getInputRelationsCount()
    {
        return inputRelationsCount;
    }


    public void setInputRelationsCount(int inputRelationsCount)
    {
        this.inputRelationsCount = inputRelationsCount;
    }


    public int getOutputRelationsCount()
    {
        return outputRelationsCount;
    }


    public void setOutputRelationsCount(int outputRelationsCount)
    {
        this.outputRelationsCount = outputRelationsCount;
    }


    public boolean getInputRelationsReachedLimit()
    {
        return isInputRelationsReachedLimit;
    }


    public void setInputRelationsReachedLimit(boolean inputRelationsReachedLimit)
    {
        isInputRelationsReachedLimit = inputRelationsReachedLimit;
    }


    public boolean isOutputRelationsReachedLimit()
    {
        return isOutputRelationsReachedLimit;
    }


    public void setOutputRelationsReachedLimit(boolean outputRelationsReachedLimit)
    {
        isOutputRelationsReachedLimit = outputRelationsReachedLimit;
    }


    public AtlasLineageOnDemandConstraints getOnDemandConstraints()
    {
        return onDemandConstraints;
    }


    public void setOnDemandConstraints(AtlasLineageOnDemandConstraints onDemandConstraints)
    {
        this.onDemandConstraints = onDemandConstraints;
    }


    @Override
    public String toString()
    {
        return "AtlasLineageInfoOnDemand{" +
                       "hasMoreInputs=" + hasMoreInputs +
                       ", hasMoreOutputs=" + hasMoreOutputs +
                       ", inputRelationsCount=" + inputRelationsCount +
                       ", outputRelationsCount=" + outputRelationsCount +
                       ", isInputRelationsReachedLimit=" + isInputRelationsReachedLimit +
                       ", isOutputRelationsReachedLimit=" + isOutputRelationsReachedLimit +
                       ", onDemandConstraints=" + onDemandConstraints +
                       ", inputRelationsReachedLimit=" + getInputRelationsReachedLimit() +
                       ", outputRelationsReachedLimit=" + isOutputRelationsReachedLimit() +
                       '}';
    }
}
