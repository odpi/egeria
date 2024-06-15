/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetGraph;

import java.util.Arrays;
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
public class AssetGraphResponse extends AssetConsumerOMASAPIResponse
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
        if (assetGraph == null)
        {
            return null;
        }
        else
        {
            return assetGraph;
        }
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
                "graph=" + assetGraph +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
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
        if (!(objectToCompare instanceof AssetGraphResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetGraphResponse that = (AssetGraphResponse) objectToCompare;
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
