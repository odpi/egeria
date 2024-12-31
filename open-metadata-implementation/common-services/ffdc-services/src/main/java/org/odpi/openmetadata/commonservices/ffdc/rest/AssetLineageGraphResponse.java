/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraph;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetLineageGraphResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns an
 * AssetLineageGraph object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetLineageGraphResponse extends FFDCResponseBase
{
    private AssetLineageGraph assetLineageGraph = null;

    /**
     * Default constructor
     */
    public AssetLineageGraphResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetLineageGraphResponse(AssetLineageGraphResponse template)
    {
        super(template);

        if (template != null)
        {
            this.assetLineageGraph = template.getAssetLineageGraph();
        }
    }


    /**
     * Return the graph object.
     *
     * @return graph object
     */
    public AssetLineageGraph getAssetLineageGraph()
    {
        return assetLineageGraph;
    }


    /**
     * Set up the graph object.
     *
     * @param assetLineageGraph - graph object
     */
    public void setAssetLineageGraph(AssetLineageGraph assetLineageGraph)
    {
        this.assetLineageGraph = assetLineageGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetLineageGraphResponse{" +
                "assetLineageGraph=" + assetLineageGraph +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof AssetLineageGraphResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getAssetLineageGraph(), that.getAssetLineageGraph());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (assetLineageGraph == null)
        {
            return super.hashCode();
        }
        else
        {
            return assetLineageGraph.hashCode();
        }
    }
}
