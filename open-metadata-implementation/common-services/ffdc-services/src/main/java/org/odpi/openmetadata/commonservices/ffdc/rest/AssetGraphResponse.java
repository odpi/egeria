/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetGraph;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetGraphResponse is the response structure used on the Asset Consumer OMAS REST API calls that returns an
 * AssetGraph object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetGraphResponse extends FFDCResponseBase
{
    private AssetGraph assetGraph = null;

    /**
     * Default constructor
     */
    public AssetGraphResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetGraphResponse(AssetGraphResponse template)
    {
        super(template);

        if (template != null)
        {
            this.assetGraph = template.getAssetGraph();
        }
    }


    /**
     * Return the graph object.
     *
     * @return graph object
     */
    public AssetGraph getAssetGraph()
    {
        return assetGraph;
    }


    /**
     * Set up the graph object.
     *
     * @param assetGraph - graph object
     */
    public void setAssetGraph(AssetGraph assetGraph)
    {
        this.assetGraph = assetGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetGraphResponse{" +
                "assetGraph=" + assetGraph +
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
        if (!(objectToCompare instanceof AssetGraphResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getAssetGraph(), that.getAssetGraph());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (assetGraph == null)
        {
            return super.hashCode();
        }
        else
        {
            return assetGraph.hashCode();
        }
    }
}
